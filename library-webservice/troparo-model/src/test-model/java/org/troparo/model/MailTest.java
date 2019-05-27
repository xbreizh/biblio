package org.troparo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MailTest {

    private Mail mail;

    @BeforeEach
    void init(){
        mail = new Mail();
    }

    @Test
    void getEmail() {
        mail.setEmail("toptop@test.com");
        assertEquals("toptop@test.com", mail.getEmail());
    }

    @Test
    void setEmail() {
        mail.setEmail("toptop@test.com");
        assertEquals("toptop@test.com", mail.getEmail());
    }

    @Test
    void getFirstname() {
        mail.setFirstname("Bobby");
        assertEquals("Bobby", mail.getFirstname());
    }

    @Test
    void setFirstname() {
        mail.setFirstname("Bobby");
        assertEquals("Bobby", mail.getFirstname());
    }

    @Test
    void getLastname() {
        mail.setLastname("Sand");
        assertEquals("Sand", mail.getLastname());
    }

    @Test
    void setLastname() {
        mail.setLastname("Sand");
        assertEquals("Sand", mail.getLastname());
    }

    @Test
    void getDueDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        mail.setDueDate(date);
        assertEquals(date, mail.getDueDate());
    }

    @Test
    void setDueDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        mail.setDueDate(date);
        assertEquals(date, mail.getDueDate());
    }

    @Test
    void getDiffdays() {
        mail.setDiffdays(14);
        assertEquals(14, mail.getDiffdays());
    }

    @Test
    void setDiffdays() {
        mail.setDiffdays(14);
        assertEquals(14, mail.getDiffdays());
    }

    @Test
    void getIsbn() {
        mail.setIsbn("QNH1233");
        assertEquals("QNH1233", mail.getIsbn());
    }

    @Test
    void setIsbn() {
        mail.setIsbn("QNH1233");
        assertEquals("QNH1233", mail.getIsbn());
    }

    @Test
    void getTitle() {
        mail.setTitle("Bosco");
        assertEquals("Bosco", mail.getTitle());
    }

    @Test
    void setTitle() {
        mail.setTitle("Bosco");
        assertEquals("Bosco", mail.getTitle());
    }

    @Test
    void getAuthor() {
        mail.setAuthor("Manu");
        assertEquals("Manu", mail.getAuthor());
    }

    @Test
    void setAuthor() {
        mail.setAuthor("Manu");
        assertEquals("Manu", mail.getAuthor());
    }

    @Test
    void getEdition() {
        mail.setEdition("Dumarron");
        assertEquals("Dumarron", mail.getEdition());
    }

    @Test
    void setEdition() {
        mail.setEdition("Dumarron");
        assertEquals("Dumarron", mail.getEdition());
    }

    @Test
    void toString1() throws ParseException {
        mail.setEdition("Potirron");
        mail.setAuthor("James Block");
        mail.setTitle("la grande traverse");
        mail.setIsbn("AER111");
        mail.setDiffdays(23);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        mail.setDueDate(date);
        mail.setFirstname("Jean");
        mail.setLastname("Maurice");
        System.out.println(mail);
        assertEquals("Mail{email='null', firstname='Jean', lastname='Maurice', dueDate=Thu Dec 31 00:00:00 CET 2009, diffdays=23, " +
                "isbn='AER111', title='la grande traverse', author='James Block', edition='Potirron'}", mail.toString());
    }
}