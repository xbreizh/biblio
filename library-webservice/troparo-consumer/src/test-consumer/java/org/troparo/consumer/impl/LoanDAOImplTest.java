package org.troparo.consumer.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Book;
import org.troparo.model.Loan;

import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class LoanDAOImplTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private LoanDAO loanDAO;
    @Inject
    private BookDAO bookDAO;

    @Sql({"classpath:/src/main/resources/resetDb.sql"})
    @BeforeEach
    void reset() {
        logger.info("reset db");
    }

    @Test
    @DisplayName("should return list of loans")
    void getLoans() {
        assertEquals(5, loanDAO.getLoans().size());
    }

    @Test
    @DisplayName("should return empty list if session is null")
    void getLoans1() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoans().size());
    }


    @Test
    @DisplayName("should add a loan")
    void addLoan() {
        assertAll(
                () -> assertTrue(loanDAO.addLoan(new Loan())),
                () -> assertEquals(6, loanDAO.getLoans().size())

        );

    }

    @Test
    @DisplayName("should add a loan")
    void addLoan1() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertFalse(loanDAO1.addLoan(new Loan()));
    }


    @Test
    @DisplayName("should update loan")
    void updateLoan() {
        Loan loan = loanDAO.getLoanById(4);
        Book newBook = bookDAO.getBookById(1);
        assertNotEquals(newBook, loanDAO.getLoanById(4).getBook());
        loan.setBook(newBook);
        assertAll(
                () -> assertTrue(loanDAO.updateLoan(loan)),
                () -> assertEquals(newBook, loanDAO.getLoanById(4).getBook())

        );

    }

    @Test
    @DisplayName("should update loan")
    void updateLoan1() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertFalse(loanDAO1.updateLoan(new Loan()));
    }


    @Test
    @DisplayName("should return loan if existing id")
    void getLoanById() {
        assertNotNull(loanDAO.getLoanById(4));
    }

    @Test
    @DisplayName("should return loan if none-existing id")
    void getLoanById1() {
        assertNull(loanDAO.getLoanById(34));
    }

    @Test
    @DisplayName("shoult return list of loans if existing ISBN")
    void getLoanByIsbn() {
        assertNotEquals(0, loanDAO.getLoanByIsbn("12345678OK"));
    }

    @Test
    @DisplayName("shoult return empty list if none-existing ISBN")
    void getLoanByIsbn1() {
        assertEquals(0, loanDAO.getLoanByIsbn("12345678sK").size());
    }

    @Test
    @DisplayName("shoult return empty list if ISBN is null")
    void getLoanByIsbn2() {
        assertEquals(0,loanDAO.getLoanByIsbn(null).size());
    }

    @Test
    @DisplayName("shoult return empty list if ISBN is null")
    void getLoanByIsbn3() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoanByIsbn("ded").size());
    }



    @Test
    @DisplayName("should return not empty list if existing loan for a login")
    void getLoanByLogin() {
        assertEquals(4, loanDAO.getLoanByLogin("Jpolino").size());
    }

    @Test
    @DisplayName("should return  empty list if none-existing loan for a login")
    void getLoanByLogin1() {
        assertEquals(0, loanDAO.getLoanByLogin("Jpoline").size());
    }

    @Test
    @DisplayName("should return  empty list if session is null")
    void getLoanByLogin2() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoanByLogin("Jpoline").size());
    }



    @Test
    @DisplayName("should return empty list if invalid criteria")
    void getLoansByCriterias() {
        HashMap<String, String> map = new HashMap<>();
        map.put("plouf", "jpolinfo");
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_bookId() {
        HashMap<String, String> map = new HashMap<>();
        map.put("book_id", "5");
        assertEquals(1, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list of loans if criterias null")
    void getLoansByCriterias2() {
        assertEquals(0, loanDAO.getLoansByCriterias(null).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_login() {
        HashMap<String, String> map = new HashMap<>();
        map.put("login", "lokii");
        assertEquals(1, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_Status() {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        assertEquals(3, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list if loan status invalid")
    void getLoansByCriterias_Status1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "wrongOne");
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list if loan is null")
    void getLoansByCriterias_Status2() {
        HashMap<String, String> map = new HashMap<>();
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoansByCriterias(map).size());
    }


    @Test
    @DisplayName("should return list if several valid criterias")
    void getLoansByCriterias_Status3() {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        map.put("book_id", "5");
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return \"and endDate > current_date\" string if invalid criteria")
    void addStatusToRequest(){
       LoanDAOImpl loanDAO = new LoanDAOImpl();

       assertEquals(" and endDate > current_date", loanDAO.addStatusToRequest("dede", 3));
    }

    @Test
    @DisplayName("should return \"where endDate > current_date\" string if invalid criteria")
    void addStatusToRequest1(){
        LoanDAOImpl loanDAO = new LoanDAOImpl();

        assertEquals(" where endDate > current_date", loanDAO.addStatusToRequest("dede", 1));
    }

    @Test
    @DisplayName("should return \" endDate is null\" string if progress")
    void addStatusToRequest2(){
        LoanDAOImpl loanDAO = new LoanDAOImpl();

        assertEquals(" and endDate is null", loanDAO.addStatusToRequest("progress", 3));
    }

    @Test
    @DisplayName("should return \"endDate is null and plannedEndDate < current_date\" string if overdue")
    void addStatusToRequest3(){
        LoanDAOImpl loanDAO = new LoanDAOImpl();

        assertEquals(" and endDate is null and plannedEndDate < current_date", loanDAO.addStatusToRequest("overdue", 3));
    }

}