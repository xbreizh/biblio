package org.mail.impl;


import org.apache.log4j.Logger;
import org.mail.contract.ConnectManager;
import org.mail.contract.EmailManager;
import org.mail.model.Mail;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.troparo.entities.mail.*;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;
import org.troparo.services.mailservice.MailService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Named
@PropertySource("classpath:mail.properties")
@PropertySource("classpath:Overdue.html")
public class EmailManagerImpl implements EmailManager {

    private static final String MAIL_LIST_SIZE = "mailList size: ";
    private static final String SUBJECT_RESET = "subjectPasswordReset";
    private static final String SUBJECT_OVERDUE = "subjectOverDue";
    private static final String SUBJECT_LOAN_READY = "subjectLoanReady";
    private static final String SUBJECT_REMINDER = "subjectReminder";

    private ConnectManager connectManager;
    private PropertiesLoad propertiesLoad;
    private MailService mailService;
    private Logger logger = Logger.getLogger(EmailManagerImpl.class);

    public EmailManagerImpl() {
    }

    @Inject
    public EmailManagerImpl(ConnectManager connectManager, PropertiesLoad propertiesLoad) {
        this.connectManager = connectManager;
        this.propertiesLoad = propertiesLoad;
    }


    @Scheduled(cron = "* 00 11 * * *") // runs every day at 11:00
    public void sendOverdueMailCron() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        sendOverdueMail();
    }

    @Scheduled(cron = "0 8,14 * * 1-5 *") // runs every week day at 08:00 and 14:00
    public void sendReadyEmailCron() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        sendReadyEmail();
    }

    @Scheduled(cron = "0 9,13 * * 1-5 *") // runs every week day at 09:00 and 13:00
    public void sendReminderEmailCron() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        sendReminderEmail();
    }

    @Scheduled(fixedRate = 60000) // runs every mn
    public void sendPasswordResetEmailCron() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        sendPasswordResetEmail();

    }


    @Override
    public boolean sendOverdueMail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        String template = "Overdue.html";
        if (checkIfFileExist(template)) {
            String token = connectManager.authenticate();
            if (token != null) {
                List<Mail> overdueList = getOverdueList(token);
                return sendEmail(template, SUBJECT_OVERDUE, overdueList);
            }
            return false;
        }
        shoutFileError();
        return false;
    }


    @Override
    public boolean sendReadyEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        logger.info("sending Book ready email");
        String template = "LoanReady.html";
        if (checkIfFileExist(template)) {
            String token = connectManager.authenticate();
            if (token != null) {
                List<Mail> loanReady = getReadyList(token);
                return sendEmail(template, SUBJECT_LOAN_READY, loanReady);
            }

            return false;
        }
        shoutFileError();
        return false;


    }


    @Override
    public boolean sendReminderEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        logger.info("sending Reminder email");
        List<Mail> reminderList;
        String template = "Reminder.html";
        if (checkIfFileExist(template)) {
            String token = connectManager.authenticate();
            if (token != null) {
                reminderList = getReminderList(token);
                logger.info("list for reminder: " + reminderList.size());
                return sendEmail(template, SUBJECT_REMINDER, reminderList);
            }
            return false;
        }

        shoutFileError();
        return false;

    }


    @Override
    public boolean sendPasswordResetEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail {
        logger.info("checking if password reset email");
        String template = "resetPassword.html";
        if (checkIfFileExist(template)) {
            String token = connectManager.authenticate();
            if (token != null) {
                List<Mail> passwordResetList = getPasswordResetList(token);
                if (!passwordResetList.isEmpty()) {
                    logger.info("there are password reset email to be sent");
                    return sendEmail(template, SUBJECT_RESET, passwordResetList);
                } else {
                    return true;
                }
            }
            return false;
        }
        shoutFileError();
        return false;

    }

    boolean sendEmail(String template, String subject, List<Mail> mailList) throws MessagingException, IOException {
        Map<String, String> input;
        if ((mailList != null) && (!mailList.isEmpty())) {
            logger.info(MAIL_LIST_SIZE + mailList.size());
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
                    if (readyToSend()) Transport.send(message);
                }
            }
        }
        return true;
    }

    // special method added for testing purpose only
    boolean readyToSend() {
        return true;
    }

    private void shoutFileError() {
        logger.error("There is an issue with the file source");
    }

    boolean checkIfFileExist(String template) {
        if (template == null) {
            logger.error("File not found: " + template);
            return false;
        }
        return true;
    }

    Map<String, String> getItemsForSubject(String subject, Mail mail) {
        logger.info("trying to get items");
        String[] validSubjectsList = {SUBJECT_RESET, SUBJECT_OVERDUE, SUBJECT_LOAN_READY, SUBJECT_REMINDER};
        if (Arrays.asList(validSubjectsList).contains(subject)) {
            if (subject.equals(SUBJECT_RESET)) return getPasswordResetTemplateItems(mail);
            return getTemplateItems(mail);
        }
        logger.warn("wrong email subject: " + subject + ", returning null");
        return null;
    }


    Map<String, String> getPasswordResetTemplateItems(Mail mail) {
        //Set key values
        Map<String, String> input = new HashMap<>();
        if (mail == null) return input;
        input.put("TOKEN", mail.getToken());
        input.put("EMAIL", mail.getEmail());
        input.put("LOGIN", mail.getLogin());
        input.put("PWDACTION", propertiesLoad.getProperty("pwdResetAction"));
        logger.info("getting template items: " + input);
        return input;
    }


    // general
    Message prepareMessage(Mail mail, String template, String subject, Map<String, String> input) throws MessagingException, IOException {
        final String username = propertiesLoad.getProperty("mailFrom");
        Properties props = new Properties();
        props.put("mail.smtp.auth", propertiesLoad.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", propertiesLoad.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", propertiesLoad.getProperty("mailServer"));
        props.put("mail.smtp.port", propertiesLoad.getProperty("mailServerPort"));
        Session session = getSession(props);
        Message message = createNewMimeMessage(session);
        message.setFrom(new InternetAddress(username));
        String recipient = mail.getEmail();


        // adding condition for testing purposes
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

    MimeMessage createNewMimeMessage(Session session) {
        return new MimeMessage(session);
    }


    //Method to replace the values for keys

    String replaceValuesForKeys(String template, Map<String, String> input) throws IOException {
        logger.info("replacing values for keys");
        String msg = null;
        if (checkIfFileExist(template)) {
            try {
                File file = new File("resources/" + template);
                msg = readContentFromFile(file);


                Set<Map.Entry<String, String>> entries = input.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
                }
            } catch (NullPointerException e) {
                logger.error("Issue while getting file");
            }
        }

        return msg;
    }


    Map<String, String> getTemplateItems(Mail mail) {
        logger.info("getting overdue template items");
        //Set key values
        Map<String, String> input = new HashMap<>();
        if (mail == null) {
            logger.warn("item passed was null");
            return input;
        }
        if (mail.getDueDate() != null) {
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            String dueDate = dt1.format(mail.getDueDate());
            input.put("DUEDATE", dueDate);
        }
        int overDays = mail.getDiffdays();
        input.put("DIFFDAYS", Integer.toString(overDays));
        input.put("FIRSTNAME", mail.getFirstname());
        input.put("LASTNAME", mail.getLastname());
        input.put("ISBN", mail.getIsbn());
        input.put("TITLE", mail.getTitle());
        input.put("AUTHOR", mail.getAuthor());
        input.put("EDITION", mail.getEdition());
        if (mail.getEndAvailableDate() != null) {
            SimpleDateFormat dt2 = new SimpleDateFormat("dd-MM-yyyy");
            String endAvailableDate = dt2.format(mail.getEndAvailableDate());
            input.put("ENDAVAILABLEDATE", endAvailableDate);
        }
        return input;

    }


    //Method to read HTML file as a String

    String readContentFromFile(File file) throws IOException {
        logger.info("trying to read content from html file and returning a String");
        StringBuilder contents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line);
            }
        }

        logger.info("html file converted ok");
        return contents.toString();
    }


    List<Mail> getOverdueList(String token) throws BusinessExceptionMail {
        logger.info("getting overdue list");
        GetOverdueMailListRequest requestType = new GetOverdueMailListRequest();
        requestType.setToken(token);
        GetOverdueMailListResponse response = getMailServicePort().getOverdueMailList(requestType);

        return convertListTypeIntoMailList(response);

    }

    List<Mail> getReadyList(String token) throws BusinessExceptionMail {
        logger.info("getting ready list");
        GetLoanReadyRequest requestType = new GetLoanReadyRequest();
        requestType.setToken(token);
        GetLoanReadyResponse response = getMailServicePort().getLoanReady(requestType);

        return convertListTypeIntoMailList(response);

    }

    List<Mail> getReminderList(String token) throws BusinessExceptionMail {
        logger.info("getting reminder list");
        GetReminderMailListRequest requestType = new GetReminderMailListRequest();
        requestType.setToken(token);
        GetReminderMailListResponse response = getMailServicePort().getReminderMailList(requestType);
        logger.info("nb Elements returned: " + response.getMailListType().getMailTypeOut().size());
        return convertListTypeIntoMailList(response);
    }


    List<Mail> getPasswordResetList(String token) throws BusinessExceptionMail {
        List<Mail> mailList = new ArrayList<>();
        logger.info("getting password reset list");
        GetPasswordResetListRequest request = new GetPasswordResetListRequest();
        request.setToken(token);

        GetPasswordResetListResponse response = getMailServicePort().getPasswordResetList(request);
        if (response.getPasswordResetListType().getPasswordResetTypeOut().isEmpty()) {
            logger.info("nothing to send");
            return mailList;
        }
        return convertPasswordResetListTypeIntoMailList(response);

    }

    IMailService getMailServicePort() {
        logger.info("getting email service port");
        if (mailService == null) mailService = new MailService();
        return mailService.getMailServicePort();
    }

    List<Mail> convertPasswordResetListTypeIntoMailList(GetPasswordResetListResponse response) {
        logger.info("trying to convert password reset list into mailList");
        List<Mail> mailList = new ArrayList<>();
        if (response.getPasswordResetListType() != null) {
            for (PasswordResetTypeOut passwordResetTypeOutTypeOut : response.getPasswordResetListType().getPasswordResetTypeOut()) {
                Mail mail = new Mail();
                mail.setEmail(passwordResetTypeOutTypeOut.getEmail());
                mail.setLogin(passwordResetTypeOutTypeOut.getLogin());
                mail.setToken(passwordResetTypeOutTypeOut.getToken());

                mailList.add(mail);
            }
            if (!mailList.isEmpty()) logger.info(MAIL_LIST_SIZE + mailList.size());
        }
        return mailList;
    }


    List<Mail> convertListTypeIntoMailList(Object response) {
        List<MailTypeOut> mailTypeOutList = null;
        logger.info("type: " + response.getClass());
        if (response.getClass() == GetLoanReadyResponse.class) {
            mailTypeOutList = ((GetLoanReadyResponse) response).getMailListType().getMailTypeOut();
        } else if (response.getClass() == GetReminderMailListResponse.class) {
            mailTypeOutList = ((GetReminderMailListResponse) response).getMailListType().getMailTypeOut();
        } else if (response.getClass() == GetOverdueMailListResponse.class) {
            mailTypeOutList = ((GetOverdueMailListResponse) response).getMailListType().getMailTypeOut();
        }
        List<Mail> mailList = new ArrayList<>();
        if (mailTypeOutList != null && !mailTypeOutList.isEmpty()) {
            logger.info(mailTypeOutList.size());

            for (MailTypeOut mailTypeOut : mailTypeOutList) {
                logger.info("due date: " + mailTypeOut.getDueDate());
                Mail mail = new Mail();
                mail.setEmail(mailTypeOut.getEmail());
                mail.setFirstname(mailTypeOut.getFirstName());
                mail.setLastname(mailTypeOut.getLastName());
                mail.setIsbn(mailTypeOut.getIsbn());
                mail.setTitle(mailTypeOut.getTitle());
                mail.setAuthor(mailTypeOut.getAuthor());
                mail.setDiffdays(mailTypeOut.getDiffDays());
                mail.setEdition(mailTypeOut.getEdition());
                if (mailTypeOut.getDueDate() != null) {
                    mail.setDueDate(convertGregorianCalendarIntoDate(mailTypeOut.getDueDate()));
                }
                if (mailTypeOut.getEndAvailableDate() != null) {
                    mail.setEndAvailableDate(convertGregorianCalendarIntoDate(mailTypeOut.getEndAvailableDate()));
                }
                mailList.add(mail);
            }
            if (!mailList.isEmpty()) logger.info(MAIL_LIST_SIZE + mailList.size());
        }
        return mailList;
    }


    Date convertGregorianCalendarIntoDate(XMLGregorianCalendar startDate) {
        logger.info("converting xml date into Date");
        if (startDate == null) return null;
        return startDate.toGregorianCalendar().getTime();
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


    MailService getMailService() {
        return mailService;
    }

    void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    void setConnectManager(ConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    void setPropertiesLoad(PropertiesLoad propertiesLoad) {
        this.propertiesLoad = propertiesLoad;
    }
}
