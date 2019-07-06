package org.troparo.business.integration;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.MailManager;
import org.troparo.business.impl.LoanManagerImpl;
import org.troparo.business.impl.MailManagerImpl;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.consumer.impl.LoanDAOImpl;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Sql("classpath:resetDb.sql")
@Transactional
class MailManagerImplIntegrationTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    /*@Inject*/
    private MailManager mailManager;

    private LoanManagerImpl loanManager;
    private LoanDAOImpl loanDAO;


    @BeforeEach
    void reset() {
        mailManager = new MailManagerImpl();
        loanManager = new LoanManagerImpl();
        loanDAO = new LoanDAOImpl();
        loanManager.setLoanDAO(loanDAO);
        mailManager.setLoanManager(loanManager);
        logger.info("reset db");
    }


    @Test
    @DisplayName("should return overdue emails from database")
    void getOverdueEmailList() {
        assertNotNull(mailManager.getOverdueEmailList());
    }


}
