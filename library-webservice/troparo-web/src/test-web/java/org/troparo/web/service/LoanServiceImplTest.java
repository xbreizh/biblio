package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.BookManagerImpl;
import org.troparo.business.impl.LoanManagerImpl;
import org.troparo.entities.loan.*;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.web.service.helper.DateConvertedHelper;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Sql("classpath:resetDb.sql")
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
        loanService.setConnectService(connectService);
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
        loanTypeIn.setBookId(12345);
        parameters.setLoanTypeIn(loanTypeIn);
        DateConvertedHelper dateConvertedHelper = new DateConvertedHelper();
        loanService.setDateConvertedHelper(dateConvertedHelper);
        when(loanManager.addLoan(anyString(), anyInt())).thenReturn("");
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertDoesNotThrow(() -> loanService.addLoan(parameters));

    }

    @Test
    @DisplayName("should return empty string")
    void reserve() throws BusinessExceptionLoan {
        ReserveRequestType request = new ReserveRequestType();
        String isbn = "isbn123";
        request.setISBN(isbn);
        String token = "token123";
        request.setToken(token);

        when(loanManager.reserve(anyString(), anyString())).thenReturn("");
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertTrue(loanService.reserve(request).getReturn().isEmpty());

    }


    @Test
    @DisplayName("should throw an exception when trying to add loan")
    void addLoan1() throws BusinessExceptionLoan {
        loanService = spy(LoanServiceImpl.class);
        AddLoanRequestType parameters = new AddLoanRequestType();
        String token = "tok123";
        parameters.setToken(token);
        LoanTypeIn loanTypeIn = new LoanTypeIn();
        loanTypeIn.setLogin("Bobb");
        loanTypeIn.setBookId(12345);
        parameters.setLoanTypeIn(loanTypeIn);
        DateConvertedHelper dateConvertedHelper = new DateConvertedHelper();
        loanService.setDateConvertedHelper(dateConvertedHelper);
        XMLGregorianCalendar date = dateConvertedHelper.convertDateIntoXmlDate(new Date());
        doThrow(new BusinessExceptionLoan()).when(loanService).checkAuthentication(token);
        assertThrows(BusinessExceptionLoan.class, () -> loanService.addLoan(parameters));

    }


    @Test
    @DisplayName("should throw an exception when trying to remove loan")
    void removeLoan() throws BusinessExceptionLoan {
        loanService = spy(LoanServiceImpl.class);
        CancelLoanRequestType parameters = new CancelLoanRequestType();
        String token = "tok123";
        parameters.setToken(token);
        parameters.setId(2);
        doThrow(new BusinessExceptionLoan()).when(loanService).checkAuthentication(token);
        assertThrows(BusinessExceptionLoan.class, () -> loanService.cancelLoan(parameters));

    }

    @Test
    @DisplayName("should return exception is non empty string returned from manager")
    void removeLoan1() throws BusinessExceptionLoan {
        CancelLoanRequestType parameters = new CancelLoanRequestType();
        String token = "tok123";
        int id = 1;
        parameters.setId(id);
        parameters.setToken(token);
        String exception = "exception";
        when(loanManager.cancelLoan(anyString(), anyInt())).thenReturn(exception);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertEquals(exception, loanService.cancelLoan(parameters).getReturn());

    }

    @Test
    @DisplayName("should return empty string if removal ok")
    void removeLoan2() throws BusinessExceptionLoan {
        CancelLoanRequestType parameters = new CancelLoanRequestType();
        String token = "tok123";
        int id = 1;
        parameters.setId(id);
        parameters.setToken(token);
        String exception = "";
        when(loanManager.cancelLoan(anyString(), anyInt())).thenReturn(exception);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertEquals(exception, loanService.cancelLoan(parameters).getReturn());

    }

    @Test
    @DisplayName("should throw an exception if token is invalid")
    void checkAuthentication() {
        ConnectServiceImpl connectService1 = mock(ConnectServiceImpl.class);
        loanService.setConnectService(connectService1);
        when(connectService1.checkToken(anyString())).thenReturn(false);
        assertThrows(BusinessExceptionLoan.class, () -> loanService.checkAuthentication("trok"));

    }


    @Test
    @DisplayName("should return member by ID")
    void getLoanById() {
        DateConvertedHelper dateConvertedHelper = new DateConvertedHelper();
        loanService.setDateConvertedHelper(dateConvertedHelper);
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
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertAll(
                () -> assertEquals(bookId, loanService.getLoanById(parameters).getLoanTypeOut().getBookId()),
                () -> assertEquals(login, loanService.getLoanById(parameters).getLoanTypeOut().getLogin())
        );


    }

    @Test
    @DisplayName("should throw an exception")
    void getLoanById1() {
        assertThrows(BusinessExceptionLoan.class, () -> loanService.getLoanById(new GetLoanByIdRequestType()));


    }

    @Test
    @DisplayName("should return true when checkinBooking is true")
    void checkInLoan() {
        CheckInLoanRequestType request = new CheckInLoanRequestType();
        request.setToken("token123");
        request.setId(3);
        loanManager = mock(LoanManagerImpl.class);
        loanService.setLoanManager(loanManager);
        when(loanManager.checkinBooking(anyString(), anyInt())).thenReturn(true);
        assertTrue(loanService.checkInLoan(request).isReturn());
    }

    @Test
    @DisplayName("should not throw an exception when getting all loans")
    void getAllLoans() {
        LoanListRequestType parameters = new LoanListRequestType();
        parameters.setToken("token123");
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertDoesNotThrow(() -> loanService.getAllLoans(parameters));
    }

    @Test
    @DisplayName("should not throw exception if criterias null")
    void getLoanByCriterias() {
        GetLoanByCriteriasRequestType parameters = new GetLoanByCriteriasRequestType();
        parameters.setToken("token123");
        parameters.setLoanCriterias(null);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertDoesNotThrow(() -> loanService.getLoanByCriteria(parameters));
    }

    @Test
    @DisplayName("should return empty list if no item found")
    void getLoanByCriterias1() throws BusinessExceptionLoan {
        GetLoanByCriteriasRequestType parameters = new GetLoanByCriteriasRequestType();
        parameters.setToken("token123");
        LoanCriterias loanCriterias = new LoanCriterias();
        loanCriterias.setLogin("kolio");
        parameters.setLoanCriterias(loanCriterias);
        Map<String, String> map = new HashMap<>();
        map.put("login", "KOLIO");
        List<Loan> loanList = new ArrayList<>();
        when(loanManager.getLoansByCriteria(map)).thenReturn(loanList);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertEquals(0, loanService.getLoanByCriteria(parameters).getLoanListType().getLoanTypeOut().size());
    }

    @Test
    @DisplayName("should return empty list if invalid criterias")
    void getLoanByCriterias2() throws BusinessExceptionLoan {
        GetLoanByCriteriasRequestType parameters = new GetLoanByCriteriasRequestType();
        parameters.setToken("token123");
        LoanCriterias loanCriterias = new LoanCriterias();
        loanCriterias.setLogin("kolio");
        parameters.setLoanCriterias(loanCriterias);
        Map<String, String> map = new HashMap<>();
        map.put("invalid.criteria", "invalid");
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertEquals(0, loanService.getLoanByCriteria(parameters).getLoanListType().getLoanTypeOut().size());
    }


    @Test
    @DisplayName("should return loan status")
    void getLoanStatus() throws BusinessExceptionLoan {
        GetLoanStatusRequestType parameters = new GetLoanStatusRequestType();
        parameters.setToken("tok");
        parameters.setId(3);
        when(loanManager.getLoanStatus(anyInt())).thenReturn("OVERDUE");
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertEquals("OVERDUE", loanService.getLoanStatus(parameters).getStatus());

    }

    @Test
    @DisplayName("should return true id manager gives true")
    void isRenewable() throws BusinessExceptionLoan {
        IsRenewableRequestType parameters = new IsRenewableRequestType();
        parameters.setToken("tok123");
        parameters.setId(3);
        when(loanManager.isRenewable(anyInt())).thenReturn(true);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertTrue(loanService.isRenewable(parameters).isReturn());
    }

    @Test
    @DisplayName("should return true id manager gives true")
    void isRenewable1() throws BusinessExceptionLoan {
        IsRenewableRequestType parameters = new IsRenewableRequestType();
        parameters.setToken("tok123");
        parameters.setId(3);
        when(loanManager.isRenewable(anyInt())).thenReturn(false);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertFalse(loanService.isRenewable(parameters).isReturn());
    }


    @Test
    void renewLoan() throws BusinessExceptionLoan {
        RenewLoanRequestType parameters = new RenewLoanRequestType();
        parameters.setToken("tok123");
        parameters.setId(3);
        String stringFromManager = "feedback from manager";
        when(loanManager.renewLoan(anyInt())).thenReturn(stringFromManager);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertEquals(stringFromManager, loanService.renewLoan(parameters).getReturn());
    }


    @Test
    void terminateLoan() throws BusinessExceptionLoan {
        TerminateLoanRequestType parameters = new TerminateLoanRequestType();
        parameters.setToken("tok123");
        parameters.setId(3);
        String stringFromManager = "feedback from manager";
        when(loanManager.terminate(anyInt())).thenReturn(stringFromManager);
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertEquals(stringFromManager, loanService.terminateLoan(parameters).getReturn());
    }

}