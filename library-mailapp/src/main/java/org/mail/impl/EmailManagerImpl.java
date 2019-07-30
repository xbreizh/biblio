package org.mail.impl;


import org.apache.log4j.Logger;
import org.mail.contract.ConnectManager;
import org.mail.contract.EmailManager;
import org.mail.model.Mail;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.troparo.entities.mail.*;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;
import org.troparo.services.mailservice.MailService;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@PropertySource("classpath:mail.properties")
@PropertySource("classpath:templates/Overdue.html")
public class EmailManagerImpl implements EmailManager {


    @Inject
    ConnectManager connectManager;

    private MailService mailService;
    private Logger logger = Logger.getLogger(ConnectManagerImpl.class);

    @Inject
    PropertiesLoad propertiesLoad;

    private static final String AES = "AES";
    private static final String MAIL_LIST_SIZE ="mailList size: ";


    @Override
    @Scheduled(cron = "* 00 11 * * *")
    //@Scheduled(fixedRate = 500000)
    public void sendOverdueMail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException {
        String template = "templates/Overdue.html";
        String subject = "subjectOverDue";
        String token = connectManager.authenticate();
        if (token != null) {
            List<Mail> overdueList = getOverdueList(token);
            sendEmail(template, subject, overdueList);
        }
    }

    @Override
    //@Scheduled(cron = "0 8,14 * * 1-5 *") // runs every week day at 08:00 and 14:00
    @Scheduled(fixedRate = 500000)
    public void sendReadyEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException {
        logger.info("sending Book ready email");
        String template = "templates/LoanReady.html";
        String subject = "subjectLoanReady";
        String token = connectManager.authenticate();
        if (token != null) {
            List<Mail> loanReady = getReadyList(token);
            sendEmail(template, subject, loanReady);
        }
    }

    @Override
    @Scheduled(cron = "0 9,13 * * 1-5 *") // runs every week day at 08:00 and 14:00
    //@Scheduled(fixedRate = 500000)
    public void sendReminderEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException {
        logger.info("sending Reminder email");
        List<Mail> reminderList ;
        String template = "templates/Reminder.html";
        String subject = "subjectReminder";
        String token = connectManager.authenticate();
        if (token != null) {
           reminderList = getReminderList(token);
            logger.info("list for reminder: "+reminderList.size());
            sendEmail(template, subject, reminderList);
        }
    }



    //@Scheduled(fixedRate = 2000000000)
    @Override
    @Scheduled(fixedRate = 500000)
    public void sendPasswordResetEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        logger.info("sending password reset email");
        String template = "templates/resetPassword.html";
        String subject = "subjectPasswordReset";
        String token = connectManager.authenticate();
        if (token != null) {
            List<Mail> passwordResetList = getPasswordResetList(token);

            sendEmail(template, subject, passwordResetList);
        }
    }

    private void sendEmail(String template, String subject, List<Mail> mailList) throws MessagingException, IOException {
        logger.info(MAIL_LIST_SIZE + mailList.size());
        Map<String, String> input;
        if (!mailList.isEmpty()) {
            for (Mail mail : mailList
            ) {
                input = getItemsForSubject(subject, mail);
                if (input != null) {
                    logger.info("sending Email");
                    Message message = prepareMessage(mail, template, subject, input);
                    logger.info("mail: " + mail);
                    logger.info("template: " + template);
                    logger.info("subject: " + subject);
                    logger.info("input: " + input);
                    Transport.send(message);
                }
            }
        }
    }

    private Map<String, String> getItemsForSubject(String subject, Mail mail) {
        logger.info("trying to get items");
        Map<String, String> input;
        switch (subject) {
            case "subjectPasswordReset":
                input = getPasswordResetTemplateItems(mail);
                break;
            case "subjectOverDue":
                input = getOverdueTemplateItems(mail);
                break;
            case "subjectLoanReady":
                input = getReadyTemplateItems(mail);
                break;
            case "subjectReminder":
                input = getReminderTemplateItems(mail);
                break;
            default:
                input = null;
                logger.warn("wrong email subject: " + subject + ", returning null");
                break;
        }
        return input;
    }



    private Map<String, String> getPasswordResetTemplateItems(Mail mail) {
        //Set key values
        Map<String, String> input = new HashMap<>();
        input.put("TOKEN", mail.getToken());
        input.put("EMAIL", mail.getEmail());
        input.put("LOGIN", mail.getLogin());
        input.put("PWDACTION",propertiesLoad.getProperty("pwdResetAction"));
        logger.info("getting template items: " + input);
        return input;
    }


    // general

    private Message prepareMessage(Mail mail, String template, String subject, Map<String, String> input) throws MessagingException, IOException {
        final String username = propertiesLoad.getProperty("mailFrom");

        Properties props = new Properties();
        props.put("mail.smtp.auth", propertiesLoad.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", propertiesLoad.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", propertiesLoad.getProperty("mailServer"));
        props.put("mail.smtp.port", propertiesLoad.getProperty("mailServerPort"));
        Session session = getSession(props);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));

        String recipient = mail.getEmail();

        // adding condition for testign purposes
        if (propertiesLoad.getProperty("test").equalsIgnoreCase("true")) {
            logger.info("test email detected");
            recipient = propertiesLoad.getProperty("testRecipient");
        }

        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipient));
        String htmlText = replaceValuesForKeys(template, input);

        message.setContent(htmlText, "text/html");
        message.setSubject(propertiesLoad.getProperty(subject));
        logger.info("message ready");
        return message;

    }


    //Method to replace the values for keys
    private String replaceValuesForKeys(String template, Map<String, String> input) throws IOException {
        logger.info("replacing values for keys");
        String msg = null;
        try {
            File file = new File(EmailManagerImpl.class.getClassLoader().getResource(template).getFile());
            msg = readContentFromFile(file);


            Set<Map.Entry<String, String>> entries = input.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        } catch (NullPointerException e) {
            logger.error("Issue while getting file");
        }

        return msg;
    }

    private Map<String, String> getOverdueTemplateItems(Mail mail) {
        logger.info("getting overdue template items");
        //Set key values
        Map<String, String> input = new HashMap<>();
        input.put("FIRSTNAME", mail.getFirstname());
        input.put("LASTNAME", mail.getLastname());
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        String dueDate = dt1.format(mail.getDueDate());
        input.put("DUEDATE", dueDate);

        int overDays = mail.getDiffdays();
        input.put("Isbn", mail.getIsbn());
        input.put("DIFFDAYS", Integer.toString(overDays));
        input.put("TITLE", mail.getTitle());
        input.put("AUTHOR", mail.getAuthor());
        input.put("EDITION", mail.getEdition());
        return input;
    }

    private Map<String, String> getReminderTemplateItems(Mail mail) {
        return getOverdueTemplateItems(mail);
    }

    private Map<String, String> getReadyTemplateItems(Mail mail) {
        logger.info("getting overdue template items");
        //Set key values
        Map<String, String> input = new HashMap<>();
        input.put("FIRSTNAME", mail.getFirstname());
        input.put("LASTNAME", mail.getLastname());

        int overDays = mail.getDiffdays();
        input.put("Isbn", mail.getIsbn());
        input.put("DIFFDAYS", Integer.toString(overDays));
        input.put("TITLE", mail.getTitle());
        input.put("AUTHOR", mail.getAuthor());
        input.put("EDITION", mail.getEdition());
        SimpleDateFormat dt2 = new SimpleDateFormat("dd-MM-yyyy");
        String endAvailableDate = dt2.format(mail.getEndAvailableDate());
        input.put("ENDAVAILABLEDATE", endAvailableDate);
        return input;

    }


    //Method to read HTML file as a String
    private String readContentFromFile(File file) throws IOException {
        logger.info("trying to read content from html file and returning a String");
        StringBuilder contents = new StringBuilder();
        //use buffering, reading one line at a time

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            reader.close();
        }
        logger.info("html file converted ok");
        return contents.toString();
    }


    private byte[] hexStringToByteArray(String s) {
        logger.info("converting hexString into ByteArray");
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    private List<Mail> getOverdueList(String token) throws BusinessExceptionMail, DatatypeConfigurationException {
        logger.info("getting overdue list");
        GetOverdueMailListRequest requestType = new GetOverdueMailListRequest();
        requestType.setToken(token);
        GetOverdueMailListResponse response = getMailServicePort().getOverdueMailList(requestType);

        return convertOverdueListTypeIntoMailList(response);

    }

    private List<Mail> getReadyList(String token) throws BusinessExceptionMail, DatatypeConfigurationException {
        logger.info("getting ready list");
        GetLoanReadyRequest requestType = new GetLoanReadyRequest();
        requestType.setToken(token);
        GetLoanReadyResponse response = getMailServicePort().getLoanReady(requestType);

        return convertReadyListTypeIntoMailList(response);

    }

    private List<Mail> getReminderList(String token) throws BusinessExceptionMail, DatatypeConfigurationException {
        logger.info("getting reminder list");
        GetReminderMailListRequest requestType = new GetReminderMailListRequest();
        requestType.setToken(token);
        GetReminderMailListResponse response = getMailServicePort().getReminderMailList(requestType);
        logger.info("nb Elements returned: "+response.getMailListType().getMailTypeOut().size());
        return convertReminderListTypeIntoMailList(response);
    }




    private List<Mail> getPasswordResetList(String token) throws BusinessExceptionMail {
        logger.info("getting password reset list");
        GetPasswordResetListRequest request = new GetPasswordResetListRequest();
        request.setToken(token);

        GetPasswordResetListResponse response = getMailServicePort().getPasswordResetList(request);
        return convertPasswordResetListTypeIntoMailList(response);

    }

    private IMailService getMailServicePort() {
        logger.info("getting email service port");
        if (mailService == null) mailService = new MailService();
        return mailService.getMailServicePort();
    }

    private List<Mail> convertPasswordResetListTypeIntoMailList(GetPasswordResetListResponse response) {
        logger.info("trying to convert password reset list into mailList");
        List<Mail> mailList = new ArrayList<>();

        for (PasswordResetTypeOut passwordResetTypeOutTypeOut : response.getPasswordResetListType().getPasswordResetTypeOut()) {
            Mail mail = new Mail();
            mail.setEmail(passwordResetTypeOutTypeOut.getEmail());
            mail.setLogin(passwordResetTypeOutTypeOut.getLogin());
            mail.setToken(passwordResetTypeOutTypeOut.getToken());

            mailList.add(mail);
        }
        if (!mailList.isEmpty()) logger.info(MAIL_LIST_SIZE + mailList.size());
        return mailList;
    }


    private List<Mail> convertReadyListTypeIntoMailList(GetLoanReadyResponse response) throws DatatypeConfigurationException {


        List<Mail> mailList = new ArrayList<>();

        for (MailTypeOut mailTypeOut : response.getMailListType().getMailTypeOut()) {
            Mail mail = new Mail();
            mail.setEmail(mailTypeOut.getEmail());
            mail.setFirstname(mailTypeOut.getFirstName());
            mail.setLastname(mailTypeOut.getLastName());
            mail.setIsbn(mailTypeOut.getIsbn());
            mail.setTitle(mailTypeOut.getTitle());
            mail.setAuthor(mailTypeOut.getAuthor());
            mail.setDiffdays(mailTypeOut.getDiffDays());
            mail.setEdition(mailTypeOut.getEdition());
            mail.setEndAvailableDate(convertGregorianCalendarIntoDate(mailTypeOut.getEndAvailableDate().toGregorianCalendar()));
            mailList.add(mail);
        }
        if (!mailList.isEmpty()) logger.info(MAIL_LIST_SIZE + mailList.size());
        return mailList;
    }


    private List<Mail> convertReminderListTypeIntoMailList(GetReminderMailListResponse response) throws DatatypeConfigurationException {
        List<Mail> mailList = new ArrayList<>();
        for (MailTypeOut mailTypeOut : response.getMailListType().getMailTypeOut()) {

            Mail mail = new Mail();
            mail.setEmail(mailTypeOut.getEmail());
            mail.setFirstname(mailTypeOut.getFirstName());
            mail.setLastname(mailTypeOut.getLastName());
            mail.setIsbn(mailTypeOut.getIsbn());
            mail.setTitle(mailTypeOut.getTitle());
            mail.setAuthor(mailTypeOut.getAuthor());
            mail.setDiffdays(mailTypeOut.getDiffDays());
            mail.setDueDate(convertGregorianCalendarIntoDate(mailTypeOut.getDueDate().toGregorianCalendar()));
            mail.setEdition(mailTypeOut.getEdition());
            mailList.add(mail);
        }
        if (!mailList.isEmpty()) logger.info(MAIL_LIST_SIZE + mailList.size());
        return mailList;
    }


    List<Mail> convertOverdueListTypeIntoMailList(GetOverdueMailListResponse response) throws DatatypeConfigurationException {


        List<Mail> mailList = new ArrayList<>();

        for (MailTypeOut mailTypeOut : response.getMailListType().getMailTypeOut()) {
            Mail mail = new Mail();
            mail.setEmail(mailTypeOut.getEmail());
            mail.setFirstname(mailTypeOut.getFirstName());
            mail.setLastname(mailTypeOut.getLastName());
            mail.setIsbn(mailTypeOut.getIsbn());
            mail.setTitle(mailTypeOut.getTitle());
            mail.setAuthor(mailTypeOut.getAuthor());
            mail.setDiffdays(mailTypeOut.getDiffDays());
            mail.setDueDate(convertGregorianCalendarIntoDate(mailTypeOut.getDueDate().toGregorianCalendar()));
            mail.setEdition(mailTypeOut.getEdition());

            mailList.add(mail);
        }
        if (!mailList.isEmpty()) logger.info(MAIL_LIST_SIZE + mailList.size());
        return mailList;
    }


    Date convertGregorianCalendarIntoDate(GregorianCalendar gregorianCalendar) throws DatatypeConfigurationException {
        logger.info("converting xml date into Date");
        XMLGregorianCalendar xmlCalendar;
        xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        return xmlCalendar.toGregorianCalendar().getTime();
    }


    private Session getSession(Properties props) {
        logger.info("getting session");
        return Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(propertiesLoad.getProperty("mailUserName"), propertiesLoad.getProperty("mailPwd"));
                    }
                });
    }

    private String getPassword() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        logger.info("trying to get Password");
        String tempKey = propertiesLoad.getProperty("Key");
        String password = propertiesLoad.getProperty("Encrypted_Password");

        byte[] byteKey = hexStringToByteArray(tempKey);
        SecretKeySpec sks = new SecretKeySpec(byteKey, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(password));


        return new String(decrypted);
    }


    MailService getMailService() {
        return mailService;
    }

    void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
}
