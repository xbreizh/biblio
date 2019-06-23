package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MailManager;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
class MailManagerImplTest {

    @Inject
    private MailManager mailManager;

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("should set loanmanager")
    void setLoanManager(){
        LoanManager loanManager = new LoanManagerImpl();
        mailManager.setLoanManager(loanManager);
        assertEquals(loanManager, mailManager.getLoanManager());
    }

    @Test
    @DisplayName("should return date")
    void getTodaySDate(){
        MailManagerImpl mailManager = new MailManagerImpl();
        assertEquals(new Date(), mailManager.getTodaySDate());
    }



    @Test
    @DisplayName("should return the overdue mailing list")
    void getOverdueEmailList1() {

        assertNotNull(mailManager.getOverdueEmailList());
    }

    @Test
    @DisplayName("should return the days difference")
    void calculateDaysBetweenDates() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse("2018-09-02");
        Date date2 = simpleDateFormat.parse("2018-09-09");
        assertEquals(7, mailManager.calculateDaysBetweenDates(date1, date2));
    }


}