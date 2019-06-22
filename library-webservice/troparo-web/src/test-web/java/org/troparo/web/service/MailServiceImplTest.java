package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.troparo.business.impl.MailManagerImpl;
import org.troparo.entities.mail.GetOverdueMailListRequest;
import org.troparo.entities.mail.MailListType;
import org.troparo.entities.mail.MailTypeOut;
import org.troparo.model.Mail;
import org.troparo.services.mailservice.BusinessExceptionMail;

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
    @DisplayName("should convert Date into Xml Date")
    void conversionDateFormats() throws ParseException {
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
        XMLGregorianCalendar dateGregorian = mailService.convertDateIntoXmlDate(date);
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
        listType = mailService.convertmailListIntoMailListType(mailList);
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