package org.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void init(){
        book = new Book();
    }

    @Test
    void getId() {
        book.setId(23);
        assertEquals(23, book.getId());
    }

    @Test
    void setId() {
        book.setId(23);
        assertEquals(23, book.getId());
    }

    @Test
    void getIsbn() {
        book.setIsbn("23BHBHG");
        assertEquals("23BHBHG", book.getIsbn());
    }

    @Test
    void setIsbn() {
        book.setIsbn("23BHBHG");
        assertEquals("23BHBHG", book.getIsbn());
    }

    @Test
    void getTitle() {
        book.setTitle("Monte Bianco");
        assertEquals("Monte Bianco", book.getTitle());
    }

    @Test
    void setTitle() {
        book.setTitle("Monte Bianco");
        assertEquals("Monte Bianco", book.getTitle());
    }

    @Test
    void getAuthor() {
        book.setAuthor("Jean Renadin");
        assertEquals("Jean Renadin", book.getAuthor());
    }

    @Test
    void setAuthor() {
        book.setAuthor("Jean Renadin");
        assertEquals("Jean Renadin", book.getAuthor());
    }

    @Test
    void getInsert_date() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        book.setInsert_date(date);
        assertEquals(date, book.getInsert_date());
    }

    @Test
    void setInsert_date() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        book.setInsert_date(date);
        assertEquals(date, book.getInsert_date());
    }

    @Test
    void getPublicationYear() {
        book.setPublicationYear(1980);
        assertEquals(1980, book.getPublicationYear());
    }

    @Test
    void setPublicationYear() {
        book.setPublicationYear(1980);
        assertEquals(1980, book.getPublicationYear());
    }

    @Test
    void getEdition() {
        book.setEdition("Maroni");
        assertEquals("Maroni",book.getEdition());
    }

    @Test
    void setEdition() {
        book.setEdition("Maroni");
        assertEquals("Maroni",book.getEdition());
    }

    @Test
    void getNbPages() {
        book.setNbPages(233);
        assertEquals(233,book.getNbPages());
    }

    @Test
    void setNbPages() {
        book.setNbPages(233);
        assertEquals(233,book.getNbPages());
    }

    @Test
    void getKeywords() {
        book.setKeywords("top, montagne, hiver");
        assertEquals("top, montagne, hiver", book.getKeywords());
    }

    @Test
    void setKeywords() {
        book.setKeywords("top, montagne, hiver");
        assertEquals("top, montagne, hiver", book.getKeywords());
    }

    @Test
    void getLoanList() {
        List<Loan> loanList = new ArrayList<>();
        assertEquals(loanList, book.getLoanList());
    }

    @Test
    void setLoanList() {
        List<Loan> loanList = new ArrayList<>();
        assertEquals(loanList, book.getLoanList());
    }

    @Test
    void toString1() throws ParseException {
        book.setAuthor("Marxo");
        book.setTitle("Le grand cirque");
        book.setIsbn("AbG231");
        book.setId(123);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date       = format.parse ( "2009-12-31" );
        book.setInsert_date(date);
        book.setNbPages(125);
        book.setEdition("maroko");
        book.setPublicationYear(1984);
        List<Loan> loanList = new ArrayList<>();
        book.setLoanList(loanList);
        book.setKeywords("top, montagne, hiver");
        System.out.println(book);

        assertEquals("Book{Id=123, isbn='AbG231', title='Le grand cirque', author='Marxo', insert_date=Thu Dec 31 00:00:00 UTC 2009, publicationYear=1984, edition='maroko', nbPages=125, keywords='top, montagne, hiver', nbAvailable='0', loanList=0}", book.toString());
    }
}
