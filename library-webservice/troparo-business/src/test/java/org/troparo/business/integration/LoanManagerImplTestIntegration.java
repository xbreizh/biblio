package org.troparo.business.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration("classpath:/application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional
class LoanManagerImplTestIntegration {


    @Inject
    private LoanManager loanManager;


    @Test
    @DisplayName("should return loans from database")
    void getLoans() {
        assertNotNull(loanManager.getLoans());
    }


}
