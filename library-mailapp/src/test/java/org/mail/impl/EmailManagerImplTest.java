package org.mail.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mail.contract.ConnectManager;
import org.mail.model.Mail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.mail.GetOverdueMailListResponse;
import org.troparo.entities.mail.MailListType;
import org.troparo.entities.mail.MailTypeOut;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;
import org.troparo.services.mailservice.MailService;

import javax.mail.MessagingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmailManagerImpl.class, ConnectManagerImpl.class, PropertiesLoad.class})
class EmailManagerImplTest {

    private EmailManagerImpl emailManager;
    private ConnectManager connectManager;


    @BeforeEach
    void init() {
        emailManager = spy(EmailManagerImpl.class);
        MailService mailService = mock(MailService.class);
        emailManager.setMailService(mailService);
        //IMailService iMailService = mock(IMailService.class);
        connectManager = mock(ConnectManager.class);
        emailManager.setConnectManager(connectManager);
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
    @DisplayName("should convert MailLisType into List<Mail>")
    void convertListTypeIntoMailList() throws DatatypeConfigurationException, ParseException {
        GetOverdueMailListResponse getOverdueMailListResponse = new GetOverdueMailListResponse();
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
        getOverdueMailListResponse.setMailListType(mailListType);


        assertAll(
                () -> assertEquals(title, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getTitle()),
                () -> assertEquals(author, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getAuthor()),
                () -> assertEquals(email, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getEmail()),
                () -> assertEquals(edition, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getEdition()),
                () -> assertEquals(firstName, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getFirstname()),
                () -> assertEquals(lastName, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getLastname()),
                () -> assertEquals(diffDays, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getDiffdays()),
                () -> assertEquals(isbn, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getIsbn()),
                () -> assertEquals(date1, emailManager.convertListTypeIntoMailList(getOverdueMailListResponse).get(0).getDueDate())
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
    void sendReminderEmail() throws BusinessExceptionConnect, MessagingException, BusinessExceptionMail, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(true).when(emailManager1).sendReminderEmail();
        when(emailManager1.sendReminderEmail()).thenReturn(true);
        assertTrue(emailManager1.sendReminderEmail());
    }

    @Test
    void sendPasswordResetEmail() throws BusinessExceptionConnect, BusinessExceptionMail, MessagingException, IOException {
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        mailList.add(mail);
        EmailManagerImpl emailManager1 = spy(emailManager);
        when(connectManager.authenticate()).thenReturn("");
        doReturn(true).when(emailManager1).sendPasswordResetEmail();
        when(emailManager1.sendPasswordResetEmail()).thenReturn(true);
        assertTrue(emailManager1.sendPasswordResetEmail());
    }

    @Test
    void convertOverdueListTypeIntoMailList() {
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
}
