package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.BookManagerImpl;
import org.troparo.business.impl.MemberManagerImpl;
import org.troparo.entities.loan.*;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;
import org.troparo.services.loanservice.BusinessExceptionLoan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    private LoanServiceImpl loanService;
    @Mock
    private LoanManager loanManager;
    @Mock
    private ConnectServiceImpl connectService;
    @Mock
    private MemberManager memberManager;
    @Mock
    private BookManagerImpl bookManager;


    @BeforeEach
    void init() {
        loanService = new LoanServiceImpl();
        loanService.setAuthentication(connectService);
        loanService.setLoanManager(loanManager);
        loanService.setBookManager(bookManager);
        loanService.setMemberManager(memberManager);

    }

    @Test
    @DisplayName("should not throw any exception when trying to add loan")
    void addLoan() {
        AddLoanRequestType parameters = new AddLoanRequestType();
        parameters.setToken("derr");
        LoanTypeIn loanTypeIn = new LoanTypeIn();
        loanTypeIn.setLogin("Bobb");
        parameters.setLoanTypeIn(loanTypeIn);
        when(loanManager.addLoan(any(Loan.class))).thenReturn("");
        assertDoesNotThrow(()->loanService.addLoan(parameters));

    }

    @Test
    @DisplayName("should throw an exception when trying to add loan")
    void addLoan1() {
        AddLoanRequestType parameters = new AddLoanRequestType();
        parameters.setToken("derr");
        LoanTypeIn loanTypeIn = new LoanTypeIn();
        loanTypeIn.setLogin("Bobb");
        parameters.setLoanTypeIn(loanTypeIn);
        String exception = "exception";
        when(loanManager.addLoan(any(Loan.class))).thenReturn(exception);
       assertThrows(BusinessExceptionLoan.class,()-> loanService.addLoan(parameters));

    }



    @Test
    @DisplayName("should return member by ID")
    void getLoanById() {
        GetLoanByIdRequestType parameters = new GetLoanByIdRequestType();
        parameters.setToken("token123");
        parameters.setId(2);
        Book book = new Book();
        int bookId = 23;
        book.setId(bookId);
        Member member = new Member();
        String login = "Jonny";
        member.setLogin(login);
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBorrower(member);
        loan.setStartDate(new Date());
        loan.setPlannedEndDate(new Date());
        when(loanManager.getLoanById(2)).thenReturn(loan);
        assertAll(
                () -> assertEquals(bookId, loanService.getLoanById(parameters).getLoanTypeOut().getBookId()),
                () -> assertEquals(login, loanService.getLoanById(parameters).getLoanTypeOut().getLogin())
        );


    }

    @Test
    @DisplayName("should not throw an exception when getting all loans")
    void getAllLoans() {
        LoanListRequestType parameters = new LoanListRequestType();
        parameters.setToken("token123");
        assertDoesNotThrow(()->loanService.getAllLoans(parameters));
    }

    @Test
    @DisplayName("should not throw exception if criterias null")
    void getLoanByCriterias() {
        GetLoanByCriteriasRequestType parameters = new GetLoanByCriteriasRequestType();
        parameters.setToken("token123");
        parameters.setLoanCriterias(null);
        assertDoesNotThrow(()->loanService.getLoanByCriterias(parameters));
    }

    @Test
    @DisplayName("should not throw exception if criterias null")
    void getLoanByCriterias1() throws BusinessExceptionLoan {
        GetLoanByCriteriasRequestType parameters = new GetLoanByCriteriasRequestType();
        parameters.setToken("token123");
        LoanCriterias loanCriterias = new LoanCriterias();
        loanCriterias.setLogin("kolio");
        parameters.setLoanCriterias(loanCriterias);
        HashMap<String, String> map = new HashMap<>();
        map.put("borrower.login", "KOLIO");
        List<Loan> loanList = new ArrayList<>();
        when(loanManager.getLoansByCriterias(map)).thenReturn(loanList);
        assertEquals(0, loanService.getLoanByCriterias(parameters).getLoanListType().getLoanTypeOut().size());
    }

    @Test
    void getLoanStatus() {
        fail();
    }

    @Test
    void isRenewable() {
        fail();
    }

    @Test
    void renewLoan() {
        fail();
    }

    @Test
    void terminateLoan() {
        fail();
    }
}