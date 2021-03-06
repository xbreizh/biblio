package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.troparo.business.impl.MailManagerImpl;
import org.troparo.entities.mail.*;
import org.troparo.model.Mail;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.web.service.helper.DateConvertedHelper;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    private MailServiceImpl mailService;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    @Mock
    private MailManagerImpl mailManager;
    @Mock
    private ConnectServiceImpl connectService;


    @BeforeEach
    void init() {
        mailService = new MailServiceImpl();
        mailService.setAuthentication(connectService);
        mailService.setMailManager(mailManager);

    }

    @Test
    @DisplayName("should return mail list")
    void getOverdueMailList() throws BusinessExceptionMail {
        List<Mail> list = new ArrayList<>();
        when(connectService.checkToken(anyString())).thenReturn(true);
        when(mailManager.getOverdueEmailList()).thenReturn(list);
        GetOverdueMailListRequest parameters = new GetOverdueMailListRequest();
        parameters.setToken("tchok");
        //assertDoesNotThrow(()->mailService.getOverdueMailList(parameters));
        assertNotNull(mailService.getOverdueMailList(parameters));
    }

    @Test
    @DisplayName("should return mail list")
    void getReadyMailList() throws BusinessExceptionMail {
        List<Mail> list = new ArrayList<>();
        when(connectService.checkToken(anyString())).thenReturn(true);
        when(mailManager.getLoansReadyForStart(anyString())).thenReturn(list);
        GetLoanReadyRequest parameters = new GetLoanReadyRequest();
        parameters.setToken("tchok");
        //assertDoesNotThrow(()->mailService.getOverdueMailList(parameters));
        assertNotNull(mailService.getLoanReady(parameters));
    }

    @Test
    @DisplayName("should return reminder List")
    void  getReminderMailList() throws BusinessExceptionMail {
        GetReminderMailListRequest request = new GetReminderMailListRequest();
        String token = "token123";
        request.setToken(token);
        List<Mail> list = new ArrayList<>();
        when(mailManager.getLoansReminder(anyString())).thenReturn(list);
        assertEquals(0, mailService.getReminderMailList(request).getMailListType().getMailTypeOut().size());
    }




    @Test
    @DisplayName("should convert list")
    void convertMailListIntoPasswordResetListType(){
        List<Mail> list = new ArrayList<>();
        Mail mail = new Mail();
        String email = "test@test.test";
        String login = "loginash";
        String token = "token123";
        mail.setEmail(email);
        mail.setLogin(login);
        mail.setToken(token);
        list.add(mail);
        PasswordResetListType passwordResetListType = mailService.convertMailListIntoPasswordResetListType(list);
        assertAll(
                ()-> assertEquals(email, passwordResetListType.getPasswordResetTypeOut().get(0).getEmail()),
                ()-> assertEquals(token, passwordResetListType.getPasswordResetTypeOut().get(0).getToken()),
                ()-> assertEquals(login, passwordResetListType.getPasswordResetTypeOut().get(0).getLogin())
        );
    }

    @Test
    @DisplayName("should convert Date into Xml Date")
    void conversionDateFormats() throws ParseException {
        DateConvertedHelper dateConvertedHelper = new DateConvertedHelper();
        mailService.setDateConvertedHelper(dateConvertedHelper);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse("2019-01-31 23:59:59");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        XMLGregorianCalendar dateGregorian = dateConvertedHelper.convertDateIntoXmlDate(date);
        logger.info(date);
        logger.info(dateGregorian);

        assertAll(
                () -> assertEquals(year, dateGregorian.getYear()),
                () -> assertEquals(month + 1, dateGregorian.getMonth()),
                () -> assertEquals(day, dateGregorian.getDay()),
                () -> assertEquals(hour, dateGregorian.getHour()),
                () -> assertEquals(minute, dateGregorian.getMinute()),
                () -> assertEquals(second, dateGregorian.getSecond())
        );


    }

    @Test
    @DisplayName("should convert List into MailListType")
    void convertmailListIntoMailListType() {
        DateConvertedHelper dateConvertedHelper = new DateConvertedHelper();
        mailService.setDateConvertedHelper(dateConvertedHelper);
        MailListType listType;
        List<Mail> mailList = new ArrayList<>();
        Mail mail = new Mail();
        String isbn = "IBNHH091";
        mail.setIsbn(isbn);
        String author = "Bobby Sand";
        mail.setAuthor(author);
        String firstname = "Jean";
        mail.setFirstName(firstname);
        String lastname = "Valjo";
        mail.setLastName(lastname);
        int diffdays = 23;
        mail.setDiffDays(diffdays);
        Date dueDate = new Date();
        mail.setDueDate(dueDate);
        String edition = "polono";
        mail.setEdition(edition);
        String title = "Macholi";
        mail.setTitle(title);
        String email = "sasa@test.test";
        mail.setEmail(email);
        mailList.add(mail);
        listType = mailService.convertMailListIntoMailListType(mailList);
        MailTypeOut mailTypeOut = listType.getMailTypeOut().get(0);
        assertAll(
                () -> assertEquals(author, mailTypeOut.getAuthor()),
                () -> assertEquals(firstname, mailTypeOut.getFirstName()),
                () -> assertEquals(lastname, mailTypeOut.getLastName()),
                () -> assertEquals(email, mailTypeOut.getEmail()),
                () -> assertEquals(edition, mailTypeOut.getEdition()),
                () -> assertEquals(title, mailTypeOut.getTitle()),
                () -> assertEquals(isbn, mailTypeOut.getIsbn())

        );


    }
}