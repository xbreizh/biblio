package org.library.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.contract.LoanManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.loan.RenewLoanRequestType;
import org.troparo.entities.loan.RenewLoanResponseType;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookManagerImpl.class)
class LoanManagerImplTest {

    private LoanManagerImpl loanManager;

    @BeforeEach
    void init(){
        loanManager = mock(LoanManagerImpl.class);
    }

    @Test
    void renewLoan() throws BusinessExceptionLoan {
        ILoanService IloanService = mock(ILoanService.class);
        when(loanManager.getLoanServicePort()).thenReturn(IloanService);
        RenewLoanRequestType renewLoanRequestType = new RenewLoanRequestType();
        renewLoanRequestType.setToken("");
        renewLoanRequestType.setId(4);
        RenewLoanResponseType renewLoanResponseType = new RenewLoanResponseType();
        renewLoanResponseType.setReturn("return");
        when(IloanService.renewLoan(renewLoanRequestType)).thenReturn(renewLoanResponseType);
        assertFalse(loanManager.renewLoan("", 4));
    }

    @Test
    void getLoanServicePort() {
    }

    @Test
    void isRenewable() {
    }

    @Test
    void getStatus() {
    }
}