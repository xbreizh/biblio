package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.troparo.business.contract.LoanManager;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PropertySource("classpath:config.properties")
class LoanManagerImplTest {

    private LoanManager loanManager;
    private LoanDAO loanDAO;



    @BeforeEach
    void init() {
        loanManager = new LoanManagerImpl();
        loanDAO = mock(LoanDAO.class);
        loanManager.setLoanDAO(loanDAO);
    }

    @Test
    @DisplayName("")
    void addLoan() {
        fail();
    }

    @Test
    @DisplayName("should return the list of loans")
    void getLoans() {
        List<Loan> loanList = new ArrayList<>();
        when(loanDAO.getLoans()).thenReturn(loanList);
        assertEquals(loanList, loanManager.getLoans());
    }

    @Test
    @DisplayName("should return a loan")
    void getLoanById() {
        Loan loan = new Loan();
        when(loanDAO.getLoanById(2)).thenReturn(loan);
        assertEquals(loan, loanManager.getLoanById(2));
    }

    @Test
    @DisplayName("should return null")
    void getLoanById1() {
        when(loanDAO.getLoanById(2)).thenReturn(null);
        assertEquals(null, loanManager.getLoanById(2));
    }

    @Test
    @DisplayName("")
    void getLoansByCriterias() {
        fail();
    }

    @Test
    @DisplayName("")
    void renewLoan() {
        fail();
    }

    @Test
    @DisplayName("")
    void isRenewable() throws ParseException {
        Loan loan = new Loan();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        loan.setStartDate(format.parse("01-01-2019"));
        loan.setPlannedEndDate(format.parse("12-01-2019"));
        when(loanDAO.getLoanById(2)).thenReturn(loan);
        assertTrue(loanManager.isRenewable(2));
    }

    @Test
    @DisplayName("")
    void terminate() {
        fail();
    }

    @Test
    @DisplayName("")
    void getLoanStatus() {
        fail();
    }
}