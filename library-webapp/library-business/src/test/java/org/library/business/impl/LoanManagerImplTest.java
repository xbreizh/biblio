package org.library.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.loan.*;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;
import org.troparo.services.loanservice.LoanService;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("should return false")
    void renewLoan() throws BusinessExceptionLoan {
        String response = "plok";
        RenewLoanResponseType renewLoanResponseType = new RenewLoanResponseType();
        renewLoanResponseType.setReturn(response);
        ILoanService iLoanService = mock(ILoanService.class);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(loanService.getLoanServicePort().renewLoan(any(RenewLoanRequestType.class))).thenReturn(renewLoanResponseType);
        assertFalse(loanManager.renewLoan("", 1));
    }

    @Test
    @DisplayName("should return true")
    void renewLoan1() throws BusinessExceptionLoan {
        String response = "";
        RenewLoanResponseType renewLoanResponseType = new RenewLoanResponseType();
        renewLoanResponseType.setReturn(response);
        ILoanService iLoanService = mock(ILoanService.class);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(loanService.getLoanServicePort().renewLoan(any(RenewLoanRequestType.class))).thenReturn(renewLoanResponseType);
        assertTrue(loanManager.renewLoan("", 1));
    }

    @Test
    void getLoanServicePort() {
        loanManager.setLoanService(null);
        assertNotNull(loanManager.getLoanServicePort());
    }

    @Test
    @DisplayName("should return true")
    void isRenewable() throws BusinessExceptionLoan {
        IsRenewableResponseType responseType = mock(IsRenewableResponseType.class);
        responseType.setReturn(true);
        ILoanService iLoanService = mock(ILoanService.class);
        when(responseType.isReturn()).thenReturn(true);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(iLoanService.isRenewable(any(IsRenewableRequestType.class))).thenReturn(responseType);
        assertTrue(loanManager.isRenewable("", 2));

    }

    @Test
    @DisplayName("should return false")
    void isRenewable1() throws BusinessExceptionLoan {
        IsRenewableResponseType responseType = mock(IsRenewableResponseType.class);
        responseType.setReturn(false);
        ILoanService iLoanService = mock(ILoanService.class);
        when(responseType.isReturn()).thenReturn(true);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(iLoanService.isRenewable(any(IsRenewableRequestType.class))).thenReturn(responseType);
        assertTrue(loanManager.isRenewable("", 2));

    }

    @Test
    @DisplayName("should return status")
    void getStatus() throws BusinessExceptionLoan {
        String status = "dol";
        GetLoanStatusResponseType getLoanStatusResponseType = mock(GetLoanStatusResponseType.class);
        getLoanStatusResponseType.setStatus(status);
        ILoanService iLoanService = mock(ILoanService.class);
        when(getLoanStatusResponseType.getStatus()).thenReturn(status);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(iLoanService.getLoanStatus(any(GetLoanStatusRequestType.class))).thenReturn(getLoanStatusResponseType);
        assertEquals(status, loanManager.getStatus("", 2));

    }

    @Test
    @DisplayName("should return null")
    void getStatus1() throws BusinessExceptionLoan {
        GetLoanStatusResponseType getLoanStatusResponseType = mock(GetLoanStatusResponseType.class);
        ILoanService iLoanService = mock(ILoanService.class);
        when(getLoanStatusResponseType.getStatus()).thenReturn(null);
        when(loanManager.getLoanServicePort()).thenReturn(iLoanService);
        when(iLoanService.getLoanStatus(any(GetLoanStatusRequestType.class))).thenReturn(getLoanStatusResponseType);
        assertNull(loanManager.getStatus("", 2));

    }


   /* @Test
    void createArrayFromLoanDates() throws ParseException {
        List<Loan> loanList = new ArrayList<>();
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date start = simpleDateFormat.parse("12/20/2019");
        Date end = simpleDateFormat.parse("12/24/2019");
        Loan loan = new Loan();
        loan.setStartDate(start);
        loan.setPlannedEndDate(end);
        loanList.add(loan);
        assertEquals("[12/20/2019, 12/21/2019, 12/22/2019, 12/23/2019]", Arrays.toString(loanManager.createArrayFromLoanDates(loanList)));



    }*/


}