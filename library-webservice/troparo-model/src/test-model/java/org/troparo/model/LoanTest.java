package org.troparo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanTest {

    private Loan loan;

    @BeforeEach
    void init() {
        loan = new Loan();
    }

    @Test
    @DisplayName("should get id")
    void getId() {
        loan.setId(123);
        assertEquals(123, loan.getId());
    }

    @Test
    @DisplayName("should set id")
    void setId() {
        loan.setId(123);
        assertEquals(123, loan.getId());
    }

    @Test
    @DisplayName("should get reservation date")
    void getReservationDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setReservationDate(date);
        assertEquals(date, loan.getReservationDate());
    }

    @Test
    @DisplayName("should set reservation date")
    void setReservationDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setReservationDate(date);
        assertEquals(date, loan.getReservationDate());
    }

    @Test
    @DisplayName("should get available date")
    void getAvailableDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setAvailableDate(date);
        assertEquals(date, loan.getAvailableDate());
    }

    @Test
    @DisplayName("should set reservation date")
    void setAvailableDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setAvailableDate(date);
        assertEquals(date, loan.getAvailableDate());
    }

    @Test
    @DisplayName("should get start date")
    void getStartDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setStartDate(date);
        assertEquals(date, loan.getStartDate());
    }

    @Test
    @DisplayName("should set start date")
    void setStartDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setStartDate(date);
        assertEquals(date, loan.getStartDate());
    }

    @Test
    @DisplayName("should get planned end date")
    void getPlannedEndDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setPlannedEndDate(date);
        assertEquals(date, loan.getPlannedEndDate());
    }

    @Test
    @DisplayName("should set planned end date")
    void setPlannedEndDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setPlannedEndDate(date);
        assertEquals(date, loan.getPlannedEndDate());
    }

    @Test
    @DisplayName("should get end date")
    void getEndDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setEndDate(date);
        assertEquals(date, loan.getEndDate());
    }

    @Test
    @DisplayName("should set end date")
    void setEndDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setEndDate(date);
        assertEquals(date, loan.getEndDate());
    }

    @Test
    @DisplayName("should get Isbn")
    void getIsbn() {
        String isbn = "isbn123";
        loan.setIsbn(isbn);
        assertEquals(isbn, loan.getIsbn());
    }

    @Test
    @DisplayName("should set Borrower")
    void setIsbn() {
        String isbn = "isbn123";
        loan.setIsbn(isbn);
        assertEquals(isbn, loan.getIsbn());
    }

    @Test
    @DisplayName("should get Borrower")
    void getBorrower() {
        Member borrower = new Member();
        loan.setBorrower(borrower);
        assertEquals(borrower, loan.getBorrower());
    }

    @Test
    @DisplayName("should set Borrower")
    void setBorrower() {
        Member borrower = new Member();
        loan.setBorrower(borrower);
        assertEquals(borrower, loan.getBorrower());
    }

    @Test
    @DisplayName("should Get Book")
    void getBook() {
        Book book = new Book();
        loan.setBook(book);
        assertEquals(book, loan.getBook());
    }

    @Test
    @DisplayName("should Set Book")
    void setBook() {
        Book book = new Book();
        loan.setBook(book);
        assertEquals(book, loan.getBook());
    }

    @Test
    @DisplayName("should return Loan details")
    void toString1() throws ParseException {
        loan.setId(123);
        Book book = new Book();
        book.setTitle("Captain Cook");
        loan.setBook(book);
        Member borrower = new Member();
        borrower.setLogin("Diego23");
        loan.setBorrower(borrower);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setStartDate(date);
        loan.setPlannedEndDate(format.parse("2010-01-07"));
        loan.setEndDate(format.parse("2010-01-17"));
        assertEquals("Loan{id=123, reservationDate=null, availableDate=null, startDate=Thu Dec 31 00:00:00 UTC 2009, plannedEndDate=Thu Jan 07 00:00:00 UTC 2010, endDate=Sun Jan 17 00:00:00 UTC 2010, isbn=null, borrower=Member{id=0, login='Diego23', firstName='null', lastName='null', password='null', role='null', token='null', email='null', dateJoin=null, dateConnect=null, loanList=0, tokenExpiration=null, reminder=false}, book=Captain Cook}", loan.toString());
    }

    @Test
    @DisplayName("should return Loan details")
    void toString2() throws ParseException {
        loan.setId(123);
        Member borrower = new Member();
        borrower.setLogin("Diego23");
        loan.setBorrower(borrower);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2009-12-31");
        loan.setStartDate(date);
        loan.setPlannedEndDate(format.parse("2010-01-07"));
        loan.setEndDate(format.parse("2010-01-17"));
        assertEquals("Loan{id=123, reservationDate=null, availableDate=null, startDate=Thu Dec 31 00:00:00 UTC 2009, plannedEndDate=Thu Jan 07 00:00:00 UTC 2010, endDate=Sun Jan 17 00:00:00 UTC 2010, isbn=null, borrower=Member{id=0, login='Diego23', firstName='null', lastName='null', password='null', role='null', token='null', email='null', dateJoin=null, dateConnect=null, loanList=0, tokenExpiration=null, reminder=false}, book=no book}", loan.toString());
    }

}