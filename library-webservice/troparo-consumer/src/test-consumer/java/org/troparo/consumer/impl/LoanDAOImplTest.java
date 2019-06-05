package org.troparo.consumer.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;

import javax.inject.Inject;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class LoanDAOImplTest {

    @Inject
    private LoanDAO loanDAO;
    @Inject
    private BookDAO bookDAO;

    @Test
    @DisplayName("should return list of loans")
    void getLoans() {
        assertEquals(5, loanDAO.getLoans().size());
    }

    @Test
    @DisplayName("should add a loan")
    void addLoan() {
        loanDAO.addLoan(new Loan());
        assertEquals(6, loanDAO.getLoans().size());
    }

    @Test
    @DisplayName("should update loan")
    void updateLoan() {
        Loan loan = loanDAO.getLoanById(4);
        Book newBook = bookDAO.getBookById(5);
        assertNotEquals(newBook, loan.getBook());
        loan.setBook(newBook);
        assertEquals(newBook, loan.getBook());
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
        assertNull( loanDAO.getLoanByIsbn(null));
    }

    @Test
    @DisplayName("should return not empty list if existing loan for a login")
    void getLoanByLogin() {
        assertEquals(1, loanDAO.getLoanByLogin("Jpolino").size());
    }

    @Test
    @DisplayName("should return  empty list if none-existing loan for a login")
    void getLoanByLogin1() {
        assertEquals(0, loanDAO.getLoanByLogin("Jpoline").size());
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
    void getLoansByCriterias1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("book_id", "3");
        assertEquals(1, loanDAO.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list of loans if criterias null")
    void getLoansByCriterias2() {
        assertEquals(0, loanDAO.getLoansByCriterias(null).size());
    }
}