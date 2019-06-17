package org.troparo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("should get Email")
    void getEmail() {
        mail.setEmail("toptop@test.com");
        assertEquals("toptop@test.com", mail.getEmail());
    }

    @Test
    @DisplayName("should set Email")
    void setEmail() {
        mail.setEmail("toptop@test.com");
        assertEquals("toptop@test.com", mail.getEmail());
    }

    @Test
    @DisplayName("should get firstname")
    void getFirstname() {
        mail.setFirstname("Bobby");
        assertEquals("Bobby", mail.getFirstname());
    }

    @Test
    @DisplayName("should set firstname")
    void setFirstname() {
        mail.setFirstname("Bobby");
        assertEquals("Bobby", mail.getFirstname());
    }

    @Test
    @DisplayName("should get lastname")
    void getLastname() {
        mail.setLastname("Sand");
        assertEquals("Sand", mail.getLastname());
    }

    @Test
    @DisplayName("should set lastname")
    void setLastname() {
        mail.setLastname("Sand");
        assertEquals("Sand", mail.getLastname());
    }

    @Test
    @DisplayName("should get due date")
    void getDueDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        mail.setDueDate(date);
        assertEquals(date, mail.getDueDate());
    }

    @Test
    @DisplayName("should set due date")
    void setDueDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        mail.setDueDate(date);
        assertEquals(date, mail.getDueDate());
    }

    @Test
    @DisplayName("should get diffDays")
    void getDiffdays() {
        mail.setDiffdays(14);
        assertEquals(14, mail.getDiffdays());
    }

    @Test
    @DisplayName("should set diffDays")
    void setDiffdays() {
        mail.setDiffdays(14);
        assertEquals(14, mail.getDiffdays());
    }

    @Test
    @DisplayName("should get ISBN")
    void getIsbn() {
        mail.setIsbn("QNH1233");
        assertEquals("QNH1233", mail.getIsbn());
    }

    @Test
    @DisplayName("should set ISBN")
    void setIsbn() {
        mail.setIsbn("QNH1233");
        assertEquals("QNH1233", mail.getIsbn());
    }

    @Test
    @DisplayName("should get Title")
    void getTitle() {
        mail.setTitle("Bosco");
        assertEquals("Bosco", mail.getTitle());
    }

    @Test
    @DisplayName("should set Title")
    void setTitle() {
        mail.setTitle("Bosco");
        assertEquals("Bosco", mail.getTitle());
    }

    @Test
    @DisplayName("should get Author")
    void getAuthor() {
        mail.setAuthor("Manu");
        assertEquals("Manu", mail.getAuthor());
    }

    @Test
    @DisplayName("should set Author")
    void setAuthor() {
        mail.setAuthor("Manu");
        assertEquals("Manu", mail.getAuthor());
    }

    @Test
    @DisplayName("should get Edition")
    void getEdition() {
        mail.setEdition("Dumarron");
        assertEquals("Dumarron", mail.getEdition());
    }

    @Test
    @DisplayName("should set Edition")
    void setEdition() {
        mail.setEdition("Dumarron");
        assertEquals("Dumarron", mail.getEdition());
    }

    @Test
    @DisplayName("should return Mail")
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
        assertEquals("Mail{email='null', firstname='Jean', lastname='Maurice', dueDate=Thu Dec 31 00:00:00 UTC 2009, diffdays=23, " +
                "isbn='AER111', title='la grande traverse', author='James Block', edition='Potirron'}", mail.toString());
    }
}