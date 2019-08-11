package org.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


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
    @DisplayName("should get checked")
    void isChecked() {
        loan.setChecked(true);
        assertTrue(loan.isChecked());
    }

    @Test
    @DisplayName("should set checked")
    void setChecked() {
        loan.setChecked(true);
        assertTrue(loan.isChecked());
    }

    @Test
    @DisplayName("should set isbn")
    void getIsbn() {
        loan.setIsbn("ISBN123");
        assertEquals("ISBN123", loan.getIsbn());
    }


    @Test
    @DisplayName("should set isbn")
    void setIsbn() {
        loan.setIsbn("ISBN123");
        assertEquals("ISBN123", loan.getIsbn());
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
    @DisplayName("should get renewable")
    void isRenewable() {
        loan.setRenewable(false);
        assertFalse(loan.isRenewable());
    }

    @Test
    @DisplayName("should set renewable")
    void setRenewable() {
        loan.setRenewable(false);
        assertFalse(loan.isRenewable());
    }

    @Test
    @DisplayName("should get status")
    void getStatus() {
        String status = "status";
        loan.setStatus(status);
        assertEquals(status, loan.getStatus());
    }

    @Test
    @DisplayName("should set status")
    void setStatus() {
        String status = "status";
        loan.setStatus(status);
        assertEquals(status, loan.getStatus());
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
        assertEquals("Loan{id=123, startDate=Thu Dec 31 00:00:00 UTC 2009, plannedEndDate=Thu Jan 07 00:00:00 UTC 2010, endDate=Sun Jan 17 00:00:00 UTC 2010, renewable=false, status=null, book=Captain Cook}", loan.toString());
    }


    @Test
    @DisplayName("should return false when null")
    void compareTo(){
        Loan loan2 = new Loan();
        loan2.setStatus("");
        loan.setStatus("plok");
        assertEquals(4,  loan.compareTo(loan2));
    }

    @Test
    @DisplayName("should return false when different class")
    void equals(){
        assertFalse( loan.equals(new Book()));
    }

    @Test
    @DisplayName("should return false when different ")
    void equals2(){
        loan.setIsbn("isbn123");
        assertFalse( loan.equals(new Loan()));
    }

    @Test
    @DisplayName("should return true when equals ")
    void equals4(){
        Date startDate = new Date();
        Loan loan2 = new Loan();
        loan.setStartDate(startDate);
        assertFalse(loan.equals(loan2));
    }

    @Test
    @DisplayName("should return true when equals ")
    void equals5(){
        Loan loan2 = new Loan();
        Date startDate = new Date();
        String isbn = "isbn123";
        loan2.setStartDate(startDate);
        loan.setStartDate(startDate);
        loan.setIsbn(isbn);
        assertFalse(loan.equals(loan2));
    }

    @Test
    @DisplayName("should return true when equals ")
    void equals6(){
        Loan loan2 = new Loan();
        Date startDate = new Date();
        String isbn = "isbn123";
        Book book = new Book();
        loan2.setStartDate(startDate);
        loan2.setIsbn(isbn);
        loan.setStartDate(startDate);
        loan.setIsbn(isbn);
        loan.setBook(book);
        assertFalse(loan.equals(loan2));
    }

    @Test
    @DisplayName("should return true when equals ")
    void equals7(){
        Loan loan2 = new Loan();
        Date startDate = new Date();
        String isbn = "isbn123";
        Book book = new Book();
        Member member = new Member();
        loan2.setStartDate(startDate);
        loan2.setIsbn(isbn);
        loan2.setBook(book);
        loan.setStartDate(startDate);
        loan.setIsbn(isbn);
        loan.setBook(book);
        loan.setBorrower(member);
        assertFalse(loan.equals(loan2));
    }

    @Test
    @DisplayName("should return true when equals ")
    void equals8(){
        Loan loan2 = new Loan();
        Date startDate = new Date();
        String isbn = "isbn123";
        Book book = new Book();
        Member member = new Member();
        Date endDate = new Date();
        loan2.setStartDate(startDate);
        loan2.setEndDate(endDate);
        loan2.setIsbn(isbn);
        loan2.setBook(book);
        loan.setStartDate(startDate);
        loan.setEndDate(endDate);
        loan.setIsbn(isbn);
        loan.setBook(book);
        loan.setBorrower(member);
        assertFalse(loan.equals(loan2));
    }

    @Test
    @DisplayName("should return true when equals ")
    void equals3(){
        Date startDate = new Date();
        Date endDate = new Date();
        Book book = new Book();
        Member member = new Member();
        String isbn = "isbn123";
        Loan loan2 = new Loan();
        loan2.setStartDate(startDate);
        loan2.setEndDate(endDate);
        loan2.setIsbn(isbn);
        loan2.setBook(book);
        loan2.setBorrower(member);
        loan.setStartDate(startDate);
        loan.setEndDate(endDate);
        loan.setIsbn(isbn);
        loan.setBook(book);
        loan.setBorrower(member);
        assertTrue(loan.equals(loan2));
    }

    @Test
    @DisplayName("should return hashCode")
    void hashCode1(){
        assertEquals(1, loan.hashCode());

    }

}
