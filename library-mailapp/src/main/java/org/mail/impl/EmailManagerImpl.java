package org.mail.impl;


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
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@PropertySource("classpath:mail.properties")
@PropertySource("classpath:HTMLTemplate.html")
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

    @Scheduled(fixedRate = 500000)
    public void sendMail() throws BusinessExceptionConnect, MessagingException, IOException {
        String token;
        token = connectManager.authenticate();
        if (token != null) {

            final String username = propertiesLoad.getProperty("mailFrom");

            Properties props = new Properties();
            props.put("mail.smtp.auth", propertiesLoad.getProperty("mail.smtp.auth"));
            props.put("mail.smtp.starttls.enable", propertiesLoad.getProperty("mail.smtp.starttls.enable"));
            props.put("mail.smtp.host", propertiesLoad.getProperty("mailServer"));
            props.put("mail.smtp.port", propertiesLoad.getProperty("mailServerPort"));
            Session session = getSession( props);


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            message.setSubject(propertiesLoad.getProperty("subjectOverDue"));


            List<Mail> overdueList = getOverdueList(token);

            if (overdueList != null && !overdueList.isEmpty()) {
                for (Mail mail : overdueList
                ) {


                    String recipient = mail.getEmail();

                    // adding condition for testign purposes
                    if (propertiesLoad.getProperty("test").equalsIgnoreCase("true")) {
                        recipient = propertiesLoad.getProperty("testRecipient");
                    }
                    getMessage(message, mail, recipient);
                    Transport.send(message);


                }
            }


        }
    }

    private void getMessage(Message message, Mail mail, String recipient) throws MessagingException, IOException {

        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipient));

        File file = new File(EmailManagerImpl.class.getClassLoader().getResource("HTMLTemplate.html").getFile());
        String htmlText = readEmailFromHtml(file, mail);

        message.setContent(htmlText, "text/html");

    }

    private Session getSession( Properties props) {
        return Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(propertiesLoad.getProperty("mailUserName"), propertiesLoad.getProperty("mailPwd"));
                    }
                });
    }

    //Method to replace the values for keys

    String readEmailFromHtml(File file, Mail mail) throws IOException {
        Map<String, String> input;
        input = getTemplateItems(mail);

        String msg = readContentFromFile(file);

        Set<Map.Entry<String, String>> entries = input.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
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

        //use buffering, reading one line at a time

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private List<Mail> getOverdueList(String token) {
        List<Mail> mailList = new ArrayList<>();
        GetOverdueMailListRequest requestType = new GetOverdueMailListRequest();
        requestType.setToken(token);
        try {
            GetOverdueMailListResponse response = getMailServicePort().getOverdueMailList(requestType);
            return convertMailingListTypeIntoMailList(response);
        } catch (BusinessExceptionMail businessExceptionMail) {
        }
        return mailList;
    }

    private IMailService getMailServicePort() {
        if (mailService == null) mailService = new MailService();
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


    Date convertGregorianCalendarIntoDate(GregorianCalendar gregorianCalendar) {
        XMLGregorianCalendar xmlCalendar;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            return xmlCalendar.toGregorianCalendar().getTime();
        } catch (DatatypeConfigurationException e) {
        }
        return null;

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
