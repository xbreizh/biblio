package org.library.business.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.contract.LoanManager;
import org.library.business.impl.BookManagerImpl;
import org.library.business.impl.LoanManagerImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LoanManagerImpl.class)
@EnableTransactionManagement
class LoanManagerImplIntegrationTest {

    @Inject
    LoanManager loanManager;

    @Test
    void renewLoan() {
        assertFalse(loanManager.renewLoan("", 3));
    }

    @Test
    void isRenewable() {
        assertFalse(loanManager.isRenewable("", 3));
    }

    @Test
    void getStatus() {
        assertEquals("OVERDUE", loanManager.getStatus("", 4));


    }
}