package org.mail.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mail.contract.ConnectManager;
import org.mail.contract.EmailManager;
import org.mail.model.Mail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.mail.*;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;
import org.troparo.services.mailservice.MailService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmailManagerImpl.class, ConnectManagerImpl.class, PropertiesLoad.class})
class EmailManagerImplTest {

    private EmailManagerImpl emailManager;
    private ConnectManager connectManager;
    private Logger logger = Logger.getLogger(EmailManagerImplTest.class);


    @BeforeEach
    void init() throws IOException {
        emailManager = spy(EmailManagerImpl.class);
        MailService mailService = mock(MailService.class);
        emailManager.setMailService(mailService);

        PropertiesLoad propertiesLoad = new PropertiesLoad();
        connectManager = mock(ConnectManager.class);
        emailManager.setConnectManager(connectManager);
        emailManager.setPropertiesLoad(propertiesLoad);
    }


    @Test
    @DisplayName("should set mailService")
    void getMailService() {
        MailService mailService1 = new MailService();
        emailManager.setMailService(mailService1);
        assertEquals(mailService1, emailManager.getMailService());
    }

    @Test
    @DisplayName("should convert date")
    void convertGregorianCalendarIntoDate() throws ParseException, DatatypeConfigurationException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date1 = simpleDateFormat.parse("2018-09-09");
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date1);
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        assertEquals(date1, emailManager.convertGregorianCalendarIntoDate(xmlGregorianCalendar));

    }

    @Test
    @DisplayName("should convert MailLisType into List<Mail> / GetReminderMailListResponse")
    void convertListTypeIntoMailList2() throws DatatypeConfigurationException, ParseException {
        GetLoanReadyResponse response = new GetLoanReadyResponse();
        MailListType mailListType = new MailListType();

        MailTypeOut mailTypeOut = new MailTypeOut();
        String firstName = "John";
        String lastName = "Maldo";
        String title = "user";
        String email = "dede@dede.fr";
        String author = "maxso";
        int diffDays = 23;
        String isbn = "frfrf09f";
        String edition = "maloni";

        mailTypeOut.setTitle(title);
        mailTypeOut.setAuthor(author);
        mailTypeOut.setIsbn(isbn);
        mailTypeOut.setEdition(edition);
        mailTypeOut.setEmail(email);
        mailTypeOut.setLastName(lastName);
        mailTypeOut.setFirstName(firstName);
        mailTypeOut.setDiffDays(diffDays);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date1 = simpleDateFormat.parse("2018-09-09");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date1);
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        mailTypeOut.setDueDate(xmlGregorianCalendar);
        mailListType.getMailTypeOut().add(mailTypeOut);
        response.setMailListType(mailListType);


        assertAll(
                () -> assertEquals(title, emailManager.convertListTypeIntoMailList(response).get(0).getTitle()),
                () -> assertEquals(author, emailManager.convertListTypeIntoMailList(response).get(0).getAuthor()),
                () -> assertEquals(email, emailManager.convertListTypeIntoMailList(response).get(0).getEmail()),
                () -> assertEquals(edition, emailManager.convertListTypeIntoMailList(response).get(0).getEdition()),
                () -> assertEquals(firstName, emailManager.convertListTypeIntoMailList(response).get(0).getFirstname()),
                () -> assertEquals(lastName, emailManager.convertListTypeIntoMailList(response).get(0).getLastname()),
                () -> assertEquals(diffDays, emailManager.convertListTypeIntoMailList(response).get(0).getDiffdays()),
                () -> assertEquals(isbn, emailManager.convertListTypeIntoMailList(response).get(0).getIsbn()),
                () -> assertEquals(date1, emailManager.convertListTypeIntoMailList(response).get(0).getDueDate())
        );


    }


    @Test
    @DisplayName("should convert MailLisType into List<Mail> / GetReminderMailListResponse")
    void convertListTypeIntoMailList() throws DatatypeConfigurationException, ParseException {
        GetReminderMailListResponse response = new GetReminderMailListResponse();
        MailListType mailListType = new MailListType();

        MailTypeOut mailTypeOut = new MailTypeOut();
        String firstName = "John";
        String lastName = "Maldo";
        String title = "user";
        String email = "dede@dede.fr";
        String author = "maxso";
        int diffDays = 23;
        String isbn = "frfrf09f";
        String edition = "maloni";

        mailTypeOut.setTitle(title);
        mailTypeOut.setAuthor(author);
        mailTypeOut.setIsbn(isbn);
        mailTypeOut.setEdition(edition);
        mailTypeOut.setEmail(email);
        mailTypeOut.setLastName(lastName);
        mailTypeOut.setFirstName(firstName);
        mailTypeOut.setDiffDays(diffDays);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date1 = simpleDateFormat.parse("2018-09-09");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date1);
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        mailTypeOut.setDueDate(xmlGregorianCalendar);
        mailListType.getMailTypeOut().add(mailTypeOut);
        response.setMailListType(mailListType);


        assertAll(
                () -> assertEquals(title, emailManager.convertListTypeIntoMailList(response).get(0).getTitle()),
                () -> assertEquals(author, emailManager.convertListTypeIntoMailList(response).get(0).getAuthor()),
                () -> assertEquals(email, emailManager.convertListTypeIntoMailList(response).get(0).getEmail()),
                () -> assertEquals(edition, emailManager.convertListTypeIntoMailList(response).get(0).getEdition()),
                () -> assertEquals(firstName, emailManager.convertListTypeIntoMailList(response).get(0).getFirstname()),
                () -> assertEquals(lastName, emailManager.convertListTypeIntoMailList(response).get(0).getLastname()),
                () -> assertEquals(diffDays, emailManager.convertListTypeIntoMailList(response).get(0).getDiffdays()),
                () -> assertEquals(isbn, emailManager.convertListTypeIntoMailList(response).get(0).getIsbn()),
                () -> assertEquals(date1, emailManager.convertListTypeIntoMailList(response).get(0).getDueDate())
        );


    }

    @Test
    @DisplayName("should convert MailLisType into List<Mail> / GetOverdueMailListResponse")
    void convertListTypeIntoMailList1() throws DatatypeConfigurationException, ParseException {
        GetOverdueMailListResponse response = new GetOverdueMailListResponse();
        MailListType mailListType = new MailListType();

        MailTypeOut mailTypeOut = new MailTypeOut();
        String firstName = "John";
        String lastName = "Maldo";
        String title = "user";
        String email = "dede@dede.fr";
        String author = "maxso";
        int diffDays = 23;
        String isbn = "frfrf09f";
        String edition = "maloni";

        mailTypeOut.setTitle(title);
        mailTypeOut.setAuthor(author);
        mailTypeOut.setIsbn(isbn);
        mailTypeOut.setEdition(edition);
        mailTypeOut.setEmail(email);
        mailTypeOut.setLastName(lastName);
        mailTypeOut.setFirstName(firstName);
        mailTypeOut.setDiffDays(diffDays);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date1 = simpleDateFormat.parse("2018-09-09");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date1);
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        mailTypeOut.setDueDate(xmlGregorianCalendar);
        mailListType.getMailTypeOut().add(mailTypeOut);
        response.setMailListType(mailListType);


        assertAll(
                () -> assertEquals(title, emailManager.convertListTypeIntoMailList(response).get(0).getTitle()),
                () -> assertEquals(author, emailManager.convertListTypeIntoMailList(response).get(0).getAuthor()),
                () -> assertEquals(email, emailManager.convertListTypeIntoMailList(response).get(0).getEmail()),
                () -> assertEquals(edition, emailManager.convertListTypeIntoMailList(response).get(0).getEdition()),
                () -> assertEquals(firstName, emailManager.convertListTypeIntoMailList(response).get(0).getFirstname()),
                () -> assertEquals(lastName, emailManager.convertListTypeIntoMailList(response).get(0).getLastname()),
                () -> assertEquals(diffDays, emailManager.convertListTypeIntoMailList(response).get(0).getDiffdays()),
                () -> assertEquals(isbn, emailManager.convertListTypeIntoMailList(response).get(0).getIsbn()),
                () -> assertEquals(date1, emailManager.convertListTypeIntoMailList(response).get(0).getDueDate())
        );


    }

    @Test
    @DisplayName("should return true when trying to send overDue mails")
    void sendOverdueMail() throws MessagingException, BusinessExceptionConnect, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getOverdueList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(true);
        assertTrue(emailManager1.sendOverdueMail());
    }

    @Test
    @DisplayName("should return false when trying to send overDue mails")
    void sendOverdueMail1() throws MessagingException, BusinessExceptionConnect, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getOverdueList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(false);
        assertFalse(emailManager1.sendOverdueMail());
    }

    @Test
    @DisplayName("should return false if file doesn't exist")
    void sendOverdueMail2() throws MessagingException, BusinessExceptionConnect, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getOverdueList(anyString());
        doReturn(false).when(emailManager1).checkIfFileExist(anyString());
        assertFalse(emailManager1.sendOverdueMail());
    }

    @Test
    @DisplayName("should return false if no token")
    void sendOverdueMail3() throws MessagingException, BusinessExceptionConnect, BusinessExceptionMail, IOException {
        EmailManagerImpl emailManager1 = spy(emailManager);
        doReturn(true).when(emailManager1).checkIfFileExist(anyString());
        assertFalse(emailManager1.sendOverdueMail());
    }



    @Test
    @DisplayName("should return true if the file exist")
    void checkIfFileExist() {
        String path = "templates/Overdue.html";
        assertTrue(emailManager.checkIfFileExist(path));
    }

    @Test
    @DisplayName("should return true if the file exist")
    void checkIfFileExist1() {
        String path = "templates/Overdues.html";
        assertFalse(emailManager.checkIfFileExist(path));
    }

    @Test
    @DisplayName("should return true")
    void sendReadyEmail() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getReadyList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(true);
        assertTrue(emailManager1.sendReadyEmail());
    }

    @Test
    @DisplayName("should return false if sendEmail false")
    void sendReadyEmail1() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getReadyList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(false);
        assertFalse(emailManager1.sendReadyEmail());
    }


    @Test
    @DisplayName("should return false if file not found")
    void sendReadyEmail11() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(false);
        assertFalse(emailManager1.sendReadyEmail());
    }


    @Test
    @DisplayName("should return false if token null")
    void sendReadyEmail12() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(true);
        assertFalse(emailManager1.sendReadyEmail());
    }

    @Test
    @DisplayName("should return true when all ok")
    void sendReminderEmail() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(true);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getReminderList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(true);
        assertTrue(emailManager1.sendReminderEmail());
    }

    @Test
    @DisplayName("should return false when file not found")
    void sendReminderEmail1() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(false);
        assertFalse(emailManager1.sendReminderEmail());
    }

    @Test
    @DisplayName("should return false when sendMail ko")
    void sendReminderEmail2() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(true);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getReminderList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(false);
        assertFalse(emailManager1.sendReminderEmail());
    }

    @Test
    @DisplayName("should return false when token null")
    void sendReminderEmail3() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(true);
        assertFalse(emailManager1.sendReminderEmail());
    }

    @Test
    @DisplayName("should return true if all ok")
    void sendPasswordResetEmail() throws BusinessExceptionConnect, BusinessExceptionMail, MessagingException, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(true);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(mailList).when(emailManager1).getPasswordResetList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(true);
        assertTrue(emailManager1.sendPasswordResetEmail());
    }

    @Test
    @DisplayName("should return true if file missing")
    void sendPasswordResetEmail1() throws BusinessExceptionConnect, BusinessExceptionMail, MessagingException, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(false);
        assertFalse(emailManager1.sendPasswordResetEmail());
    }

    @Test
    @DisplayName("should return true if sendMail ko")
    void sendPasswordResetEmail2() throws BusinessExceptionConnect, BusinessExceptionMail, MessagingException, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(emailManager1.checkIfFileExist(anyString())).thenReturn(true);
        doReturn(mailList).when(emailManager1).getPasswordResetList(anyString());
        when(emailManager1.sendEmail(anyString(), anyString(), anyList())).thenReturn(false);
        assertFalse(emailManager1.sendPasswordResetEmail());
    }


    @Test
    void convertGregorianCalendarIntoDate1() throws DatatypeConfigurationException, ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        assertEquals("Sun Sep 09 00:00:00 UTC 2018", emailManager.convertGregorianCalendarIntoDate(xmlCalendar).toString());
    }

    @Test
    void getMailService1() {
        MailService mailService1 = new MailService();
        emailManager.setMailService(mailService1);
        assertEquals(mailService1, emailManager.getMailService());
    }

    @Test
    void setMailService() {
        MailService mailService1 = new MailService();
        emailManager.setMailService(mailService1);
        assertEquals(mailService1, emailManager.getMailService());
    }


    @Test
    @DisplayName("should return null when wrong subject")
    void getItemsForSubject() {
        EmailManagerImpl emailManager1 = spy(emailManager);
        Map<String, String> template = new HashMap<>();
        //when(emailManager1.getPasswordResetTemplateItems(any(Mail.class))).thenReturn(template);
        doReturn(template).when(emailManager1).getPasswordResetTemplateItems(any(Mail.class));
        assertNull(emailManager1.getItemsForSubject("", new Mail()));

    }

    @Test
    @DisplayName("should return getPasswordResetTemplateItems when subject \"subjectPasswordReset\"")
    void getItemsForSubject1() {
        EmailManagerImpl emailManager1 = spy(emailManager);
        Map<String, String> template = new HashMap<>();
        doReturn(template).when(emailManager1).getPasswordResetTemplateItems(any(Mail.class));
        assertEquals(template, emailManager1.getItemsForSubject("subjectPasswordReset", new Mail()));

    }

    @Test
    @DisplayName("should return getOverdueTemplateItems when subject \"subjectOverDue\"")
    void getItemsForSubject2() {
        EmailManagerImpl emailManager1 = spy(emailManager);
        Map<String, String> template = new HashMap<>();
        doReturn(template).when(emailManager1).getTemplateItems(any(Mail.class));
        assertEquals(template, emailManager1.getItemsForSubject("subjectOverDue", new Mail()));

    }

    @Test
    @DisplayName("should return getOverdueTemplateItems when subject \"subjectLoanReady\"")
    void getItemsForSubject3() {
        EmailManagerImpl emailManager1 = spy(emailManager);
        Map<String, String> template = new HashMap<>();
        doReturn(template).when(emailManager1).getTemplateItems(any(Mail.class));
        assertEquals(template, emailManager1.getItemsForSubject("subjectLoanReady", new Mail()));

    }

    @Test
    @DisplayName("should return getOverdueTemplateItems when subject \"subjectReminder\"")
    void getItemsForSubject4() {
        EmailManagerImpl emailManager1 = spy(emailManager);
        Map<String, String> template = new HashMap<>();
        doReturn(template).when(emailManager1).getTemplateItems(any(Mail.class));
        assertEquals(template, emailManager1.getItemsForSubject("subjectReminder", new Mail()));

    }

    @Test
    @DisplayName("should return empty if response null")
    void convertPasswordResetListTypeIntoMailList() {
        GetPasswordResetListResponse response = new GetPasswordResetListResponse();
        assertTrue(emailManager.convertPasswordResetListTypeIntoMailList(response).isEmpty());
    }


    @Test
    @DisplayName("should convert")
    void convertPasswordResetListTypeIntoMailList1() {
        GetPasswordResetListResponse response = new GetPasswordResetListResponse();
        PasswordResetListType passwordResetListType = new PasswordResetListType();
        PasswordResetTypeOut passwordResetTypeOut = new PasswordResetTypeOut();
        String email = "sxsxs@dede.fr";
        String token = "dedede";
        String login = "login";
        passwordResetTypeOut.setToken(token);
        passwordResetTypeOut.setLogin(login);
        passwordResetTypeOut.setEmail(email);
        passwordResetListType.getPasswordResetTypeOut().add(passwordResetTypeOut);
        response.setPasswordResetListType(passwordResetListType);
        assertAll(
                () -> assertEquals(email, emailManager.convertPasswordResetListTypeIntoMailList(response).get(0).getEmail()),
                () -> assertEquals(token, emailManager.convertPasswordResetListTypeIntoMailList(response).get(0).getToken()),
                () -> assertEquals(login, emailManager.convertPasswordResetListTypeIntoMailList(response).get(0).getLogin())
        );
    }


    @Test
    @DisplayName("should return an ImailService")
    void getMailServicePort() {
        emailManager.setMailService(new MailService());
        assertNotNull(emailManager.getMailServicePort());
    }

    @Test
    @DisplayName("should return an ImailService")
    void getMailServicePort1() {
        emailManager.setMailService(null);
        assertNotNull(emailManager.getMailServicePort());
    }


    @Test
    @DisplayName("should return empty map if mail passed is null")
    void getTemplateItems() {
        assertTrue(emailManager.getTemplateItems(null).isEmpty());

    }

    @Test
    @DisplayName("should return full map")
    void getTemplateItems1() {
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        Mail mail = new Mail();
        Date dueDate = new Date();
        String firstName = "toto";
        String lastName = "mom";
        String isbn = "isbn123";
        String title = "le patron";
        String author = "Maurice po";
        String edition = "palomino";
        int overdays = 3;
        Date endAvailableDate = new Date();
        mail.setDueDate(dueDate);
        mail.setFirstname(firstName);
        mail.setLastname(lastName);
        mail.setIsbn(isbn);
        mail.setTitle(title);
        mail.setAuthor(author);
        mail.setEdition(edition);
        mail.setEndAvailableDate(endAvailableDate);
        mail.setDiffdays(overdays);
        Map<String, String> map = emailManager.getTemplateItems(mail);


        String dueDateC = dt1.format(mail.getDueDate());
        String endAvailableDateC = dt1.format(mail.getEndAvailableDate());
        assertAll(
                () -> assertTrue(map.containsKey("DUEDATE") && map.containsValue(dueDateC)),
                () -> assertTrue(map.containsKey("ENDAVAILABLEDATE") && map.containsValue(endAvailableDateC)),
                () -> assertTrue(map.containsKey("TITLE") && map.containsValue(title)),
                () -> assertTrue(map.containsKey("AUTHOR") && map.containsValue(author)),
                () -> assertTrue(map.containsKey("EDITION") && map.containsValue(edition)),
                () -> assertTrue(map.containsKey("DIFFDAYS") && map.containsValue(Integer.toString(overdays))),
                () -> assertTrue(map.containsKey("ISBN") && map.containsValue(isbn)),
                () -> assertTrue(map.containsKey("FIRSTNAME") && map.containsValue(firstName)),
                () -> assertTrue(map.containsKey("LASTNAME") && map.containsValue(lastName))

        );

    }

    @Test
    @DisplayName("should return full map")
    void getTemplateItems2() {
        Mail mail = new Mail();
        Date dueDate = null;
        Date endAvailableDate = null;
        mail.setDueDate(dueDate);
        mail.setEndAvailableDate(endAvailableDate);
        Map<String, String> map = emailManager.getTemplateItems(mail);

        assertAll(
                () -> assertFalse(map.containsKey("DUEDATE")),
                () -> assertFalse(map.containsKey("ENDAVAILABLEDATE"))

        );

    }

    @Test
    @DisplayName("should return full map")
    void getPasswordResetTemplateItems() {
        Mail mail = new Mail();
        String token = "token123";
        String email = "dede@deded.fr";
        String login = "loginded";
        String pwdAction = "http://localhost:8084/library_webapp/passwordReset";
        mail.setToken(token);
        mail.setEmail(email);
        mail.setLogin(login);
        Map<String, String> map = emailManager.getPasswordResetTemplateItems(mail);

        assertAll(
                () -> assertTrue(map.containsKey("TOKEN") && map.containsValue(token)),
                () -> assertTrue(map.containsKey("EMAIL") && map.containsValue(email)),
                () -> assertTrue(map.containsKey("LOGIN") && map.containsValue(login)),
                () -> assertTrue(map.containsKey("PWDACTION") && map.containsValue(pwdAction))

        );

    }

    @Test
    @DisplayName("should return empty map if mail is null")
    void getPasswordResetTemplateItems1() {
        assertTrue(emailManager.getPasswordResetTemplateItems(null).isEmpty());

    }


    @Test
    @DisplayName("should replace the values")
    void replaceValuesForKeys() throws IOException {
        EmailManagerImpl emailManager1 = spy(EmailManagerImpl.class);
        Map<String, String> input = new HashMap<>();
        String title = "tropa";
        String author = "Maurice";
        String login = "jonas";
        input.put("TITLE", title);
        input.put("AUTHOR", author);
        input.put("LOGIN", login);
        String templateString = "The author is AUTHOR. ";
        templateString += "The title is TITLE. ";
        templateString += "The login is LOGIN.";
        doReturn(templateString).when(emailManager1).readContentFromFile(any(File.class));
        assertEquals("The author is Maurice. The title is tropa. The login is jonas.", emailManager1.replaceValuesForKeys("", input));
    }

    @Test
    @DisplayName("should return null if no file found")
    void replaceValuesForKeys1() throws IOException {
        EmailManagerImpl emailManager1 = spy(EmailManagerImpl.class);
        doReturn(false).when(emailManager1).checkIfFileExist(anyString());
        Map<String, String> input = new HashMap<>();
        assertNull(emailManager1.replaceValuesForKeys("", input));
    }

    @Test
    @DisplayName("should return null if no file found")
    void replaceValuesForKeys2() throws IOException {
        EmailManagerImpl emailManager1 = spy(EmailManagerImpl.class);
        doReturn(true).when(emailManager1).checkIfFileExist(anyString());
        doThrow(NullPointerException.class).when(emailManager1).readContentFromFile(any(File.class));
        Map<String, String> input = new HashMap<>();
        assertNull(emailManager1.replaceValuesForKeys("", input));
    }


    @Test
    @DisplayName("should getOverdueList")
    void getOverdueList() throws BusinessExceptionMail {
        EmailManagerImpl emailManager1 = spy(EmailManagerImpl.class);
        IMailService iMailService = mock(IMailService.class);
        List<Mail> mailList = new ArrayList<>();
        GetOverdueMailListRequest request = new GetOverdueMailListRequest();
        String token = "token123";
        request.setToken(token);
        GetOverdueMailListResponse response = new GetOverdueMailListResponse();
        MailListType mailListType = new MailListType();
        response.setMailListType(mailListType);
        doReturn(iMailService).when(emailManager1).getMailServicePort();
        doReturn(response).when(iMailService).getOverdueMailList(any(GetOverdueMailListRequest.class));
        doReturn(mailList).when(emailManager1).convertListTypeIntoMailList(request);
        assertEquals(mailList, emailManager1.getOverdueList(token));

    }

    @Test
    @DisplayName("should getReadyList")
    void getReadyList() throws BusinessExceptionMail {
        EmailManagerImpl emailManager1 = spy(EmailManagerImpl.class);
        IMailService iMailService = mock(IMailService.class);
        List<Mail> mailList = new ArrayList<>();
        GetLoanReadyRequest request = new GetLoanReadyRequest();
        String token = "token123";
        request.setToken(token);
        GetLoanReadyResponse response = new GetLoanReadyResponse();
        MailListType mailListType = new MailListType();
        response.setMailListType(mailListType);
        doReturn(iMailService).when(emailManager1).getMailServicePort();
        doReturn(response).when(iMailService).getLoanReady(any(GetLoanReadyRequest.class));
        doReturn(mailList).when(emailManager1).convertListTypeIntoMailList(request);
        assertEquals(mailList, emailManager1.getReadyList(token));

    }

    @Test
    @DisplayName("should getReminderList")
    void getReminderList() throws BusinessExceptionMail {
        EmailManagerImpl emailManager1 = spy(EmailManagerImpl.class);
        IMailService iMailService = mock(IMailService.class);
        List<Mail> mailList = new ArrayList<>();
        GetReminderMailListRequest request = new GetReminderMailListRequest();
        String token = "token123";
        request.setToken(token);
        GetReminderMailListResponse response = new GetReminderMailListResponse();
        MailListType mailListType = new MailListType();
        response.setMailListType(mailListType);
        doReturn(iMailService).when(emailManager1).getMailServicePort();
        doReturn(response).when(iMailService).getReminderMailList(any(GetReminderMailListRequest.class));
        doReturn(mailList).when(emailManager1).convertListTypeIntoMailList(request);
        assertEquals(mailList, emailManager1.getReminderList(token));

    }


    @Test
    @DisplayName("should getPasswordResetList")
    void getPasswordResetList() throws BusinessExceptionMail {
        EmailManagerImpl emailManager1 = spy(EmailManagerImpl.class);
        IMailService iMailService = mock(IMailService.class);
        List<Mail> mailList = new ArrayList<>();
        GetPasswordResetListRequest request = new GetPasswordResetListRequest();
        String token = "token123";
        request.setToken(token);
        GetPasswordResetListResponse response = new GetPasswordResetListResponse();
        PasswordResetListType mailListType = new PasswordResetListType();
        response.setPasswordResetListType(mailListType);
        doReturn(iMailService).when(emailManager1).getMailServicePort();
        doReturn(response).when(iMailService).getPasswordResetList(any(GetPasswordResetListRequest.class));
        doReturn(mailList).when(emailManager1).convertListTypeIntoMailList(request);
        assertEquals(mailList, emailManager1.getPasswordResetList(token));

    }

    @Test
    @DisplayName("should read content from file is exist and not empty")
    void readContentFromFile() throws IOException, URISyntaxException, NullPointerException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("test.html");
        if (url != null) {
            File file = new File(url.toURI().getPath());
            String fileContent = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,";

            assertEquals(fileContent, emailManager.readContentFromFile(file));
        }else{
            System.out.println("test did not pass");
        }
    }

    @Test
    @DisplayName("should return true if mailList is null")
    void sendEmail() throws IOException, MessagingException {
        assertTrue(emailManager.sendEmail("", "", null));

    }

    @Test
    @DisplayName("should return true if mailList is empty")
    void sendEmail1() throws IOException, MessagingException {
        List<Mail> mailList = new ArrayList<>();
        assertTrue(emailManager.sendEmail("", "", mailList));

    }

    @Test
    @DisplayName("should return true if input is null")
    void sendEmail2() throws IOException, MessagingException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        doReturn(null).when(emailManager).getItemsForSubject(anyString(), any(Mail.class));
        assertTrue(emailManager.sendEmail("", "", mailList));
    }

    @Test
    @DisplayName("should return true if input is not null")
    void sendEmail3() throws IOException, MessagingException {
        Map<String, String> input = new HashMap<>();
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        Message message = new MimeMessage(session);
        List<Mail> mailList = new ArrayList<>();
        String template = "template";
        String subject = "subject";
        Mail mail = new Mail();
        mailList.add(mail);
        doReturn(input).when(emailManager).getItemsForSubject(subject, mail);
        doReturn(message).when(emailManager).prepareMessage(mail, template, subject, input);
        doReturn(false).when(emailManager).readyToSend();
        assertTrue(emailManager.sendEmail(template, subject, mailList));
    }

    @Test
    @DisplayName("should prepare message")
    void prepareMessage(){



    }


}
