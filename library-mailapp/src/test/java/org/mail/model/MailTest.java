package org.mail.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MailTest {

    private Mail mail;

    @BeforeEach
    void init(){
        mail = new Mail();
    }

    @Test
    void getEmail() {
        String email = "frfr@frfr.fr";
        mail.setEmail(email);
        assertEquals(email, mail.getEmail());
    }

    @Test
    void setEmail() {
        String email = "frfr@frfr.fr";
        mail.setEmail(email);
        assertEquals(email, mail.getEmail());
    }

    @Test
    void getToken() {
        String token = "dede343fgfgfgf";
        mail.setToken(token);
        assertEquals(token, mail.getToken());
    }

    @Test
    void setToken() {
        String token = "dede343fgfgfgf";
        mail.setToken(token);
        assertEquals(token, mail.getToken());
    }

    @Test
    void getLogin() {
        String login = "George1";
        mail.setLogin(login);
        assertEquals(login, mail.getLogin());
    }

    @Test
    void setLogin() {
        String login = "George1";
        mail.setLogin(login);
        assertEquals(login, mail.getLogin());
    }

    @Test
    void getFirstname() {
        String firstName = "George";
        mail.setFirstname(firstName);
        assertEquals(firstName, mail.getFirstname());
    }

    @Test
    void setFirstname() {
        String firstName = "George";
        mail.setFirstname(firstName);
        assertEquals(firstName, mail.getFirstname());
    }

    @Test
    void getLastname() {
        String lastName = "Boulet";
        mail.setLastname(lastName);
        assertEquals(lastName, mail.getLastname());
    }

    @Test
    void setLastname() {
        String lastName = "Boulet";
        mail.setLastname(lastName);
        assertEquals(lastName, mail.getLastname());
    }

    @Test
    void getDueDate() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        mail.setDueDate(date);
        assertEquals(date, mail.getDueDate());
    }

    @Test
    void setDueDate() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        mail.setDueDate(date);
        assertEquals(date, mail.getDueDate());
    }

    @Test
    void getEndAvailableDate() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        mail.setEndAvailableDate(date);
        assertEquals(date, mail.getEndAvailableDate());
    }

    @Test
    void setEndAvailableDate() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        mail.setEndAvailableDate(date);
        assertEquals(date, mail.getEndAvailableDate());
    }

    @Test
    void getDiffdays() {
        int diffDays = 34;
        mail.setDiffdays(diffDays);
        assertEquals(diffDays, mail.getDiffdays());
    }

    @Test
    void setDiffdays() {
        int diffDays = 34;
        mail.setDiffdays(diffDays);
        assertEquals(diffDays, mail.getDiffdays());
    }

    @Test
    void getIsbn() {
        String isbn= "hbbhr444";
        mail.setIsbn(isbn);
        assertEquals(isbn, mail.getIsbn());
    }

    @Test
    void setIsbn() {
        String isbn= "hbbhr444";
        mail.setIsbn(isbn);
        assertEquals(isbn, mail.getIsbn());
    }

    @Test
    void getTitle() {
        String title= "Bonimoko";
        mail.setTitle(title);
        assertEquals(title, mail.getTitle());
    }

    @Test
    void setTitle() {
        String title= "Bonimoko";
        mail.setTitle(title);
        assertEquals(title, mail.getTitle());
    }

    @Test
    void getAuthor() {
        String author= "Geronimo";
        mail.setAuthor(author);
        assertEquals(author, mail.getAuthor());
    }

    @Test
    void setAuthor() {
        String author= "Geronimo";
        mail.setAuthor(author);
        assertEquals(author, mail.getAuthor());
    }

    @Test
    void getEdition() {
        String edition= "Dujardin";
        mail.setEdition(edition);
        assertEquals(edition, mail.getEdition());
    }

    @Test
    void setEdition() {
        String edition= "Dujardin";
        mail.setEdition(edition);
        assertEquals(edition, mail.getEdition());
    }

    @Test
    void toString1() throws ParseException {
        String email = "frfr@frfr.fr";
        mail.setEmail(email);
        String firstName = "George";
        mail.setFirstname(firstName);
        String lastName = "Boulet";
        mail.setLastname(lastName);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        mail.setDueDate(date);
        int diffDays = 34;
        mail.setDiffdays(diffDays);
        String isbn= "hbbhr444";
        mail.setIsbn(isbn);
        String title= "Bonimoko";
        mail.setTitle(title);
        String author= "Geronimo";
        mail.setAuthor(author);
        String edition= "Dujardin";
        mail.setEdition(edition);
        assertEquals("Mail{email='frfr@frfr.fr', firstname='George', lastname='Boulet', dueDate=Sun Sep 09 00:00:00 UTC 2018, diffdays=34, isbn='hbbhr444', title='Bonimoko', author='Geronimo', edition='Dujardin'}", mail.toString());
    }
}
