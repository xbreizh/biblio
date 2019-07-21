package org.troparo.business.integration;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.LoanManagerImpl;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Sql("classpath:resetDb.sql")
@Transactional
class LoanManagerImplIntegrationTest {
    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Inject
    private LoanManager loanManager;

    @Inject
    private MemberManager memberManager;

    @Inject
    private BookManager bookManager;


    @BeforeEach
    void reset() {
        System.out.println("db size loans : " + loanManager.getLoans().size());
        logger.info("reset db");
    }


    @Test
    @DisplayName("should return loans from database")
    void getLoans() {
        assertEquals(5, loanManager.getLoans().size());
    }

    @Test
    @DisplayName("should return 4")
    void getLoansByCriteria1() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "JPOLINO");
        assertEquals(4, loanManager.getLoansByCriteria(map).size());
    }

    @Test
    @DisplayName("should return 1")
    void getLoansByCriteria2() {
        Map<String, String> map = new HashMap<>();
        map.put("book_id", "6");
        assertEquals(1, loanManager.getLoansByCriteria(map).size());
    }

    @Test
    @DisplayName("should return an empty list when wrong status")
    void getLoansByCriteria3() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "pol");
        map.put("status", "termindeated");
        assertEquals(0, loanManager.getLoansByCriteria(map).size());
    }
    @Test
    @DisplayName("should return an empty list when wrong status")
    void getLoansByCriteria4() {
        Map<String, String> map = new HashMap<>();
        map.put("isbn", "8574596258");
        assertEquals(1, loanManager.getLoansByCriteria(map).size());
    }


    @Test
    @DisplayName("should add loan")
    void addLoan() {
        Loan loan = new Loan();
        Member member = memberManager.getMemberById(3);
        Book book = bookManager.getBookById(4);
        loan.setBorrower(member);
        loan.setBook(book);
        assertEquals("", loanManager.addLoan(loan));
    }

    @Test
    @DisplayName("should reserve loan")
    void addLoan1() throws ParseException {
        Loan loan = new Loan();
        Member member = memberManager.getMemberById(3);
        Book book = bookManager.getBookById(5);
        loan.setBorrower(member);
        loan.setBook(book);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date today = simpleDateFormat.parse("2019-09-14");
        loan.setStartDate(today);
        assertEquals("", loanManager.addLoan(loan));
    }

    @Test
    @DisplayName("should return an error when reserving loan")
    void addLoan3() throws ParseException {
        Loan loan = new Loan();
        Member member = memberManager.getMemberById(3);
        Book book = bookManager.getBookById(5);
        loan.setBorrower(member);
        loan.setBook(book);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date today = simpleDateFormat.parse("2019-02-14");
        loan.setStartDate(today);
        assertEquals("startDate should be in the future", loanManager.addLoan(loan));
    }

    @Test
    @DisplayName("should return an error when reserving loan date in past")
    void addLoan4() throws ParseException {
        Loan loan = new Loan();
        Member member = memberManager.getMemberById(3);
        Book book = bookManager.getBookById(5);
        loan.setBorrower(member);
        loan.setBook(book);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date today = simpleDateFormat.parse("2019-02-14");
        loan.setStartDate(today);
        assertEquals("startDate should be in the future", loanManager.addLoan(loan));
    }

    @Test
    @DisplayName("should return an error when reserving book not available")
    void addLoan5() throws ParseException {
        Loan loan = new Loan();
        Member member = memberManager.getMemberById(3);
        Book book = bookManager.getBookById(2);
        loan.setBorrower(member);
        loan.setBook(book);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date today = simpleDateFormat.parse("2019-08-13");
        loan.setStartDate(today);
        assertEquals("the book is unavailable for that date", loanManager.addLoan(loan));
    }

    @Test
    @DisplayName("should return an error when loan not terminated")
    @Disabled
    void addLoan6() throws ParseException {
        Loan loan = new Loan();
        Member member = memberManager.getMemberById(3);
        Book book = bookManager.getBookById(6);
        loan.setBorrower(member);
        loan.setBook(book);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date today = simpleDateFormat.parse("2019-08-13");
        loan.setStartDate(today);
        assertEquals("the book is unavailable for that date", loanManager.addLoan(loan));
    }

}
