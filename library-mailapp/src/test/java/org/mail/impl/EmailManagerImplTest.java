package org.mail.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.mail.GetOverdueMailListResponse;
import org.troparo.entities.mail.MailListType;
import org.troparo.entities.mail.MailTypeOut;
import org.troparo.services.mailservice.MailService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmailManagerImpl.class, ConnectManagerImpl.class, PropertiesLoad.class})
class EmailManagerImplTest {

    private EmailManagerImpl emailManager;
    private MailService mailService;


    @BeforeEach
    void init(){

        emailManager = spy(EmailManagerImpl.class);
        mailService = mock(MailService.class);
        emailManager.setMailService(mailService);
    }


    @Test
    @DisplayName("should set mailService")
    void getMailService(){
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
        assertEquals(date1, emailManager.convertGregorianCalendarIntoDate(xmlGregorianCalendar.toGregorianCalendar()));

    }




    @Test
    @DisplayName("should convert MailLisType into List<Mail>")
    void convertMailingListTypeIntoMailList() throws DatatypeConfigurationException, ParseException {
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
        mailTypeOut.setDueDate(xmlGregorianCalendar );
        mailListType.getMailTypeOut().add(mailTypeOut);
        getOverdueMailListResponse.setMailListType(mailListType);

        assertAll(
                ()-> assertEquals(title, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getTitle()),
                ()-> assertEquals(author, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getAuthor()),
                ()-> assertEquals(email, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getEmail()),
                ()-> assertEquals(edition, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getEdition()),
                ()-> assertEquals(firstName, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getFirstname()),
                ()-> assertEquals(lastName, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getLastname()),
                ()-> assertEquals(diffDays, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getDiffdays()),
                ()-> assertEquals(isbn, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getIsbn()),
                ()-> assertEquals(date1, emailManager.convertOverdueListTypeIntoMailList(getOverdueMailListResponse).get(0).getDueDate())
        );



    }

}
