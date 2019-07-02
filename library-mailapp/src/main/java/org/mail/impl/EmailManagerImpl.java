package org.mail.impl;


import org.apache.log4j.Logger;
import org.mail.contract.ConnectManager;
import org.mail.model.Mail;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.troparo.entities.mail.GetOverdueMailListRequest;
import org.troparo.entities.mail.GetOverdueMailListResponse;
import org.troparo.entities.mail.MailTypeOut;
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
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@PropertySource("classpath:mail.properties")
public class EmailManagerImpl {
    private Logger logger = Logger.getLogger(EmailManagerImpl.class);

    @Inject
    ConnectManager connectManager;


    private String mailFrom = "xavier.lamourec@gmail.com";


    private String subject = "mail Reminder ** LOAN OVERDUE **";


    private String mailServer = "smtp.gmail.com";

    private String port = "587";

    private MailService mailService;


    private boolean test;


   /* @Value("${sender}")
    private String mailFrom;
    @Value("${fileLocation}")
    private String fileLocation;
    @Value("${subject}")
    private String subject;
    @Value("${body}")
    private String body;
    @Value("${mailTemplateLocation}")
    private String templateLocation;
    @Value("${mailServer}")
    private String mailServer;
    @Value("${mailServerPort}")
    private String port;*/

    private static final String AES = "AES";


    // */10 * * * * *
    // "* 00 11 * * *"
    //@Scheduled(cron = "* 00 11 * * *")

    @Scheduled(fixedRate = 500000)
    public void sendMail() throws BusinessExceptionConnect {
        String token;
        token = connectManager.authenticate();
        if (token != null) {

            final String username = "xavier.lamourec@gmail.com";
            logger.info("trying to send mail");

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", mailServer);
            props.put("mail.smtp.port", port);
            logger.info("properties passed ok");
            Session session = getSession(username, props);

            try {
                logger.info("authentication ok");
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailFrom));

                message.setSubject(subject);


                List<Mail> overdueList = getOverdueList(token);

                if (overdueList != null && !overdueList.isEmpty()) {
                    for (Mail mail : overdueList
                    ) {


                        String recipient = mail.getEmail();

                        // adding condition for testign purposes
                        if (test) {
                            recipient = "dontkillewok@gmail.com";
                        }
                        getMessage(message, mail, recipient);
                        Transport.send(message);


                    }
                }


            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getMessage(Message message, Mail mail, String recipient) throws MessagingException {
        try {
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            //HTML mail content
            String htmlText = readEmailFromHtml("/usr/app/resources/HTMLTemplate.html", mail);

            message.setContent(htmlText, "text/html");

            logger.info("mail content: " + message.getContent().toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Session getSession(String username, Properties props) {
        return Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                try {
                                    return new PasswordAuthentication(username, getPassword());

                                } catch (Exception e) {
                                    logger.error(e.getMessage());
                                }
                                return null;
                            }
                        });
    }

    //Method to replace the values for keys

    String readEmailFromHtml(String filePath, Mail mail) {
        Map<String, String> input;
        input = getTemplateItems(mail);

        logger.info("trying to get content from file");
        String msg = readContentFromFile(filePath);
        try {
            Set<Map.Entry<String, String>> entries = input.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                logger.info("entry: " + entry.getKey() + " / " + entry.getValue());
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
        return msg;
    }
    Map<String, String> getTemplateItems(Mail mail) {

        //Set key values
        Map<String, String> input = new HashMap<>();
        input.put("FIRSTNAME", mail.getFirstname());
        input.put("LASTNAME", mail.getLastname());
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        String dueDate = dt1.format(mail.getDueDate());
        input.put("DUEDATE", dueDate);

        logger.info("date: " + dueDate);
        int overDays = mail.getDiffdays();
        input.put("Isbn", mail.getIsbn());
        input.put("DIFFDAYS", Integer.toString(overDays));
        input.put("TITLE", mail.getTitle());
        input.put("AUTHOR", mail.getAuthor());
        input.put("EDITION", mail.getEdition());
        return input;
    }

    //Method to read HTML file as a String

    String readContentFromFile(String fileName) {
        logger.info("trying to buffer file");
        StringBuilder contents = new StringBuilder();

        try {
            //use buffering, reading one line at a time
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
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

    private List<Mail> getOverdueList(String token) {
        logger.info("getting overdue list");
        List<Mail> mailList = new ArrayList<>();
        GetOverdueMailListRequest requestType = new GetOverdueMailListRequest();
        requestType.setToken(token);
        try {
            GetOverdueMailListResponse response = getMailServicePort().getOverdueMailList(requestType);
            return convertMailingListTypeIntoMailList(response);
        } catch (BusinessExceptionMail businessExceptionMail) {
            logger.error(businessExceptionMail.getMessage());
        }
        return mailList;
    }

    private IMailService getMailServicePort() {
        if(mailService==null)mailService = new MailService();
        return mailService.getMailServicePort();
    }

    List<Mail> convertMailingListTypeIntoMailList(GetOverdueMailListResponse response) {
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


    private Date convertGregorianCalendarIntoDate(GregorianCalendar gDate) {
        XMLGregorianCalendar xmlCalendar;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDate);
            return xmlCalendar.toGregorianCalendar().getTime();
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage());
        }
        return null;

    }


    private String getPassword() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String tempKey;
        String password;
        Properties prop = new Properties();
        InputStream input;
        input = new FileInputStream("/usr/app/resources/mail.properties");
        // load a properties file
        prop.load(input);
        tempKey = prop.getProperty("Key");
        password = prop.getProperty("Encrypted_Password");

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
