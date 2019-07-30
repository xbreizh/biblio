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
import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.LoanManagerImpl;
import org.troparo.business.impl.MailManagerImpl;
import org.troparo.consumer.impl.LoanDAOImpl;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Sql("classpath:resetDb.sql")
@Transactional
class MailManagerImplIntegrationTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    @Inject
    private MailManager mailManager;

    private MemberManager memberManager;


   /* private LoanManagerImpl loanManager;

    private LoanDAOImpl loanDAO;*/


    @BeforeEach
    void reset() {
        memberManager = mock(MemberManager.class);
      mailManager.setMemberManager(memberManager);
        logger.info("reset db");
    }


    @Test
    @DisplayName("should return overdue emails from database")
    void getOverdueEmailList() {
        assertNotNull(mailManager.getOverdueEmailList());
    }

    @Test
    @DisplayName("should return loans having books but no startDate")
    void getLoansReadyForStart(){
        when(memberManager.checkAdmin(anyString())).thenReturn(true);
        assertEquals(1, mailManager.getLoansReadyForStart("dede").size());
    }

}
