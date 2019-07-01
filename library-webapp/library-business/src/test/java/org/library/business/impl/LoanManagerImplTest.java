package org.library.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.loan.GetLoanStatusRequestType;
import org.troparo.entities.loan.GetLoanStatusResponseType;
import org.troparo.entities.loan.RenewLoanRequestType;
import org.troparo.entities.loan.RenewLoanResponseType;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;
import org.troparo.services.loanservice.LoanService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookManagerImpl.class)
class LoanManagerImplTest {

    private LoanManagerImpl loanManager;
    private LoanService loanService;

    @BeforeEach
    void init() {
        loanManager = spy(LoanManagerImpl.class);
        loanService = mock(LoanService.class);
        loanManager.setLoanService(loanService);
    }

    @Test
    void renewLoan() throws BusinessExceptionLoan {
        RenewLoanRequestType renewLoanRequestType = new RenewLoanRequestType();
        String token = "token123";
        int id = 3;
        String response = "ploc";
        renewLoanRequestType.setToken(token);
        renewLoanRequestType.setId(id);
        RenewLoanResponseType renewLoanResponseType = new RenewLoanResponseType();
        renewLoanResponseType.setReturn(response);
        ILoanService iLoanService = mock(ILoanService.class);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(iLoanService.renewLoan(renewLoanRequestType)).thenReturn(renewLoanResponseType);
        System.out.println(renewLoanResponseType.getReturn());
        loanManager.renewLoan(token, id);
        /*loanManager.setLoanService(loanService);
        loanManager.getLoanServicePort();
        loanManager.renewLoan("", 2);*/
    }

    @Test
    void getLoanServicePort() {
    }

    @Test
    void isRenewable() {
    }

    @Test
    void getStatus() throws BusinessExceptionLoan {
        GetLoanStatusRequestType getLoanStatusRequestType = new GetLoanStatusRequestType();
        String token ="token123";
        int id = 3;
        String status = "dol";
        getLoanStatusRequestType.setToken(token);
        getLoanStatusRequestType.setId(id);
        GetLoanStatusResponseType getLoanStatusResponseType = mock(GetLoanStatusResponseType.class);
        getLoanStatusResponseType.setStatus(status);
        ILoanService iLoanService = mock(ILoanService.class);
        when(getLoanStatusResponseType.getStatus()).thenReturn(status);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(iLoanService.getLoanStatus(getLoanStatusRequestType)).thenReturn(getLoanStatusResponseType);
        assertEquals(status, loanManager.getLoanServicePort().getLoanStatus(getLoanStatusRequestType).getStatus());

    }
}