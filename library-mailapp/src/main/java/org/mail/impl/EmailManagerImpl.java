package org.mail.impl;


import org.mail.contract.ConnectManager;
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
@PropertySource("classpath:docker/mail.properties")
@PropertySource("classpath:docker/Overdue.html")
public class EmailManagerImpl {

    @Inject
    ConnectManager connectManager;

    private MailService mailService;

    @Inject
    PropertiesLoad propertiesLoad;

    private static final String AES = "AES";


    // */10 * * * * *
    // "* 00 11 * * *"
    //@Scheduled(cron = "* 00 11 * * *")

    //@Scheduled(fixedRate = 500000)
    public void sendOverdueMail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException {
        String template = "docker/Overdue.html";
        String subject = "subjectOverDue";
        String token = connectManager.authenticate();
        if (token != null) {
            List<Mail> overdueList = getOverdueList(token);
            sendEmail(template, subject, overdueList);
        }
    }

    @Scheduled(fixedRate = 2000000000)
    public void sendPasswordResetEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        String template = "docker/resetPassword.html";
        String subject = "subjectPasswordReset";
        String token = connectManager.authenticate();
        if (token != null) {
            List<Mail> passwordResetList = getPasswordResetList(token);

            sendEmail(template, subject, passwordResetList);
        }
    }

    private void sendEmail(String template, String subject, List<Mail> overdueList) throws MessagingException, IOException {
        Map<String, String> input;
        if (overdueList != null && !overdueList.isEmpty()) {
            for (Mail mail : overdueList
            ) {
                input = getItemsForSubject(subject, mail);
                if (input != null) {
                    Message message = prepareMessage(mail, template, subject, input);
                    Transport.send(message);
                }
            }
        }
    }

    private Map<String, String> getItemsForSubject(String subject, Mail mail) {
        Map<String, String> input;
        switch (subject) {
            case "subjectPasswordReset":
                input = getPasswordResetTemplateItems(mail);
                break;
            case "subjectOverDue":
                input = getOverdueTemplateItems(mail);
                break;
            default:
                input = null;
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
            recipient = propertiesLoad.getProperty("testRecipient");
        }

        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipient));
        String htmlText = replaceValuesForKeys(template, input);

        message.setContent(htmlText, "text/html");
        message.setSubject(propertiesLoad.getProperty(subject));
        return message;

    }


    //Method to replace the values for keys
    String replaceValuesForKeys(String template, Map<String, String> input) throws IOException {

        File file = new File(EmailManagerImpl.class.getClassLoader().getResource(template).getFile());

        String msg = readContentFromFile(file);

        Set<Map.Entry<String, String>> entries = input.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
        }

        return msg;
    }

    Map<String, String> getOverdueTemplateItems(Mail mail) {

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


    //Method to read HTML file as a String
    String readContentFromFile(File file) throws IOException {
        StringBuilder contents = new StringBuilder();
        String todelete = "";
        //use buffering, reading one line at a time

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            todelete = e.getMessage();
        } finally {
            reader.close();
        }

        return contents.toString();
    }


    private byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    private List<Mail> getOverdueList(String token) throws BusinessExceptionMail, DatatypeConfigurationException {
        GetOverdueMailListRequest requestType = new GetOverdueMailListRequest();
        requestType.setToken(token);
        GetOverdueMailListResponse response = getMailServicePort().getOverdueMailList(requestType);
        return convertOverdueListTypeIntoMailList(response);

    }


    private List<Mail> getPasswordResetList(String token) throws BusinessExceptionMail {
        GetPasswordResetListRequest request = new GetPasswordResetListRequest();
        request.setToken(token);

        GetPasswordResetListResponse response = getMailServicePort().getPasswordResetList(request);
        return convertPasswordResetListTypeIntoMailList(response);

    }

    private IMailService getMailServicePort() {
        if (mailService == null) mailService = new MailService();
        return mailService.getMailServicePort();
    }

    List<Mail> convertPasswordResetListTypeIntoMailList(GetPasswordResetListResponse response) {
        List<Mail> mailList = new ArrayList<>();

        for (PasswordResetTypeOut passwordResetTypeOutTypeOut : response.getPasswordResetListType().getPasswordResetTypeOut()) {
            Mail mail = new Mail();
            mail.setEmail(passwordResetTypeOutTypeOut.getEmail());
            mail.setLogin(passwordResetTypeOutTypeOut.getLogin());
            mail.setToken(passwordResetTypeOutTypeOut.getToken());

            mailList.add(mail);
        }
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
        return mailList;
    }


    Date convertGregorianCalendarIntoDate(GregorianCalendar gregorianCalendar) throws DatatypeConfigurationException {
        XMLGregorianCalendar xmlCalendar;
        xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        return xmlCalendar.toGregorianCalendar().getTime();


    }


    private Session getSession(Properties props) {
        return Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(propertiesLoad.getProperty("mailUserName"), propertiesLoad.getProperty("mailPwd"));
                    }
                });
    }

    private String getPassword() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String tempKey = propertiesLoad.getProperty("Key");
        String password = propertiesLoad.getProperty("Encrypted_Password");

        byte[] bytekey = hexStringToByteArray(tempKey);
        SecretKeySpec sks = new SecretKeySpec(bytekey, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(password));


        return new String(decrypted);
    }


    public MailService getMailService() {
        return mailService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }
}
