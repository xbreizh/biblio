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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class LoanDAOImplTest {

    private Logger logger = Logger.getLogger(LoanDAOImplTest.class);

    @Inject
    private LoanDAO loanDAO;
    @Inject
    private BookDAO bookDAO;

    @BeforeEach
    @Sql(scripts = "classpath:resetDb.sql")
    void reset() {
        logger.info("size: " + loanDAO.getLoans().size());
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

        assertTrue(loanDAO.addLoan(new Loan()));

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
        int loanId = 14;
        int bookId=1;
        Loan loan = loanDAO.getLoanById(loanId);
        Book newBook = bookDAO.getBookById(bookId);
        assertNotEquals(newBook, loanDAO.getLoanById(loanId).getBook());
        loan.setBook(newBook);
        assertAll(
                () -> assertTrue(loanDAO.updateLoan(loan)),
                () -> assertEquals(newBook, loanDAO.getLoanById(loanId).getBook())

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
        assertNotNull(loanDAO.getLoanById(14));
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
        assertEquals(0, loanDAO.getLoanByIsbn(null).size());
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
        Map<String, String> map = new HashMap<>();
        map.put("plouf", "jpolinfo");
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_bookId() {
        Map<String, String> map = new HashMap<>();
        map.put("book_id", "5");
        assertEquals(1, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list of loans if criterias null")
    void getLoansByCriterias2() {
        assertEquals(0, loanDAO.getLoansByCriterias(null).size());
    }

    @Test
    @DisplayName("should return empty list of loans if criterias value is empty")
    void getLoansByCriterias3() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "");
        assertEquals(0, loanDAO.getLoansByCriterias(null).size());
    }

    @Test
    @DisplayName("should return empty list of loans if empty map passed")
    void getLoansByCriterias4() {
        Map<String, String> map = new HashMap<>();
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_login() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "lokii");
        assertEquals(1, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_login1() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "");
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }


    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_Status() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        assertEquals(3, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list if loan status invalid")
    void getLoansByCriterias_Status1() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "wrongOne");
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list if loan is null")
    void getLoansByCriterias_Status2() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        map.put("book_id", "5");
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoansByCriterias(map).size());
    }


    @Test
    @DisplayName("should return list if several valid criterias")
    void getLoansByCriterias_Status3() {
        Map<String, String> map = new HashMap<>();
        map.put("book_id", "5");
        map.put("status", "terminated");
        assertEquals(0, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return nothing if invalid status")
    void addStatusToRequest() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "dede");
        assertEquals("", loanDAO.addStatusToRequest(map));
    }


    @Test
    @DisplayName("should return \" endDate is null\" string if progress")
    void addStatusToRequest2() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "progress");
        assertEquals(" where endDate is null", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return \"endDate is null and plannedEndDate < current_date\" string if overdue")
    void addStatusToRequest3() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "overdue");
        assertEquals(" where endDate is null and plannedEndDate < current_date", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return nothing if invalid status")
    void addStatusToRequest4() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "dede");
        assertEquals("", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return nothing if invalid status")
    void addStatusToRequest5() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "");
        assertEquals("", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return \"where endDate is not null\" if empty map")
    void addStatusToRequest6() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        map.put("login", "bob");
        map.put("book_id", "15");
        assertEquals(" and endDate is not null", loanDAO.addStatusToRequest(map));
    }


    @Test
    @DisplayName("should return an empty string when map null")
    void extractStatusFromMap() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        assertEquals("", loanDAO.extractStatusFromMap(null));
    }

    @Test
    @DisplayName("should return an empty string when map empty")
    void extractStatusFromMap1() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        assertEquals("", loanDAO.extractStatusFromMap(map));
    }

    @Test
    @DisplayName("should return terminated if passed in status")
    void extractStatusFromMap2() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        assertEquals("TERMINATED", loanDAO.extractStatusFromMap(map));
    }

    @Test
    @DisplayName("should return empty string if passed in status is incorrect")
    void extractStatusFromMap3() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("bassine", "terminated");
        assertEquals("", loanDAO.extractStatusFromMap(map));
    }

    @Test
    @DisplayName("should return string for 1 criteria")
    void createRequestFromMap() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("login", "bob");
        assertEquals("where borrower.login = :login", loanDAO.createRequestFromMap(map));
    }

    @Test
    @DisplayName("should return string for 2 criteria")
    void createRequestFromMap2() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("login", "bob");
        map.put("book_id", "ob");
        assertEquals("where book_id = :book_id and borrower.login = :login", loanDAO.createRequestFromMap(map));
    }


    @Test
    @DisplayName("should return true")
    void checkValidStatus() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        assertTrue(loanDAO.checkValidStatus("terminated"));
    }

    @Test
    @DisplayName("should return false")
    void checkValidStatus1() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        assertFalse(loanDAO.checkValidStatus("fini"));
    }


}