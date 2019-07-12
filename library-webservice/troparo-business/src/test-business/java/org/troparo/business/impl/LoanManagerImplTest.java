package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.consumer.impl.LoanDAOImpl;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Sql("classpath:resetDb.sql")
@Transactional
class LoanManagerImplTest {


    private LoanManagerImpl loanManager;

    private LoanDAO loanDAO;

    private BookManager bookManager1;

    private MemberManager memberManager1;
    private MemberManager memberManager;

    private BookManager bookManager;


    @BeforeEach
    void init() {
        loanDAO = mock(LoanDAO.class);
        loanManager = new LoanManagerImpl();
        loanManager.setLoanDAO(loanDAO);
        bookManager = mock(BookManagerImpl.class);
        loanManager.setBookManager(bookManager);
        memberManager = mock(MemberManagerImpl.class);
        loanManager.setMemberManager(memberManager);

    }

    @Test
    @DisplayName("should return \"invalid member\" when member is null")
    void addLoan() {
        Loan loan = new Loan();
        assertEquals("invalid member", loanManager.addLoan(loan));

    }

    @Test
    @DisplayName("should return \"invalid book\" when book is null")
    void addLoan1() {
        Loan loan = new Loan();
        loan.setBorrower(new Member());
        assertEquals("invalid book", loanManager.addLoan(loan));

    }

    @Test
    @DisplayName("should return \"book is not available: \" when bookmanager.isAvailable returns false")
    void addLoan2() {
        Loan loan = new Loan();
        loan.setBorrower(new Member());
        Book book = new Book();
        book.setId(2);
        loan.setBook(book);
        when(bookManager.isAvailable(anyInt())).thenReturn(false);
        assertEquals("book is not available: " + book.getId(), loanManager.addLoan(loan));
    }

    @Test
    @DisplayName("should return \"max number of books rented reached\" when member has reached the max possible loans")
    void addLoan3() {
        LoanManagerImpl loanManager1 = new LoanManagerImpl();
        MemberManagerImpl memberManager2 = mock(MemberManagerImpl.class);
        loanManager1.setMemberManager(memberManager2);
        Member member = new Member();
        member.setId(2);
        member.getLoanList().add(new Loan());
        member.getLoanList().add(new Loan());
        member.getLoanList().add(new Loan());
        member.getLoanList().add(new Loan());
        member.setLogin("george");
        Loan loan = new Loan();
        loan.setBorrower(member);
        Book book = new Book();
        book.setId(3);
        book.setTitle("borring");
        loan.setBook(book);
        loanManager1.setBookManager(bookManager);
        when(bookManager.isAvailable(anyInt())).thenReturn(true);
        loanManager1.setMemberManager(memberManager2);
        when(memberManager2.getMemberById(2)).thenReturn(member);
        System.out.println("loan: " + member.getLoanList().size());
        LoanDAOImpl loanDAO = mock(LoanDAOImpl.class);
        loanManager1.setLoanDAO(loanDAO);
        assertEquals("max number of books rented reached", loanManager1.addLoan(loan));
    }

    @Test
    @DisplayName("should return the list of loans")
    void getLoans() {
        List<Loan> loanList = new ArrayList<>();
        when(loanDAO.getLoans()).thenReturn(loanList);
        assertEquals(loanList, loanManager.getLoans());
    }

    @Test
    @DisplayName("should return a loan")
    void getLoanById() {
        Loan loan = new Loan();
        when(loanDAO.getLoanById(2)).thenReturn(loan);
        assertEquals(loan, loanManager.getLoanById(2));
    }

    @Test
    @DisplayName("should return null")
    void getLoanById1() {
        when(loanDAO.getLoanById(2)).thenReturn(null);
        assertNull(loanManager.getLoanById(2));
    }

    @Test
    @DisplayName("should return an empty list")
    void getLoansByCriterias() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "pol");
        map.put("price", "3.4");
        assertEquals(0, loanManager.getLoansByCriterias(map).size());
    }


    @Test
    @DisplayName("should return \"loan already terminated\"")
    void renewLoan() {
        Loan loan = new Loan();
        loan.setEndDate(new Date());
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        //logger.info("loanDuration: " + loanManager.getLoanDuration());
        assertEquals("loan already terminated: " + loan.getEndDate(), loanManager.renewLoan(45));
    }

    @Test
    @DisplayName("should return an empty string if loan renewed")
    void renewLoan1() {
        Loan loan = new Loan();
        loan.setStartDate(new Date());
        loan.setPlannedEndDate(new Date());
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        assertEquals("", loanManager.renewLoan(45));
    }

    @Test
    @DisplayName("should return \"loan has already been renewed\" if loanDuration > 56")
    void renewLoan2() {
        Date initDate = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(initDate);
        c1.add(Calendar.DATE, 10);
        Date startDate = c1.getTime();
        c1.add(Calendar.DATE, 150);
        Date plannedEndDate = c1.getTime();


        Loan loan = new Loan();
        loan.setStartDate(startDate);
        loan.setPlannedEndDate(plannedEndDate);
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        assertEquals("loan has already been renewed", loanManager.renewLoan(45));
    }

    @Test
    @DisplayName("should return false if not renewable")
    void isRenewable() throws ParseException {
        Loan loan = new Loan();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        loan.setStartDate(format.parse("01-01-2019"));
        loan.setPlannedEndDate(format.parse("12-03-2019"));
        when(loanDAO.getLoanById(2)).thenReturn(loan);
        assertFalse(loanManager.isRenewable(2));
    }

    @Test
    @DisplayName("should return true if renewable")
    void isRenewable1() throws ParseException {
        LoanManagerImpl loanManager = spy(LoanManagerImpl.class);
        loanManager.setLoanDAO(loanDAO);
        Loan loan = new Loan();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        loan.setStartDate(format.parse("01-01-2019"));
        loan.setPlannedEndDate(format.parse("10-01-2019"));
        Date today = format.parse("02-02-2019");
        when(loanManager.getTodayDate()).thenReturn(today);
        when(loanDAO.getLoanById(2)).thenReturn(loan);
        assertTrue(loanManager.isRenewable(2));
    }

    @Test
    @DisplayName("should return \" loan already terminated\" if loan has an end date")
    void terminate() {
        Loan loan = new Loan();
        loan.setEndDate(new Date());
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        assertEquals("loan already terminated", loanManager.terminate(44));
    }

    @Test
    @DisplayName("should return \" loan couldn't be terminated!\" if issue while terminating")
    void terminate1() {
        Loan loan = new Loan();
        loan.setEndDate(new Date());
        when(loanDAO.getLoanById(anyInt())).thenReturn(null);
        assertEquals("loan couldn't be terminated!", loanManager.terminate(44));
        //fail();
    }

    @Test
    @DisplayName("should return empty string if loan terminated")
    void terminate2() {
        Loan loan = new Loan();
        //loan.setEndDate(new Date());
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        assertEquals("", loanManager.terminate(44));
        //fail();
    }

    @Test
    @DisplayName("should return TERMINATED if endDate is not null")
    void getLoanStatus() {
        Loan loan = new Loan();
        loan.setEndDate(new Date());
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        assertEquals("TERMINATED", loanManager.getLoanStatus(55));

    }

    @Test
    @DisplayName("should return OVERDUE if endDate is not null")
    void getLoanStatus2() throws ParseException {
        LoanManagerImpl loanManager = spy(LoanManagerImpl.class);
        loanManager.setLoanDAO(loanDAO);
        Loan loan = new Loan();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date today = format.parse("02-02-2019");
        when(loanManager.getTodayDate()).thenReturn(today);
        loan.setPlannedEndDate(format.parse("12-01-2019"));
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        assertEquals("OVERDUE", loanManager.getLoanStatus(55));

    }

    @Test
    @DisplayName("should return PROGRESS if endDate is not null")
    void getLoanStatus3() throws ParseException {
        LoanManagerImpl loanManager = spy(LoanManagerImpl.class);
        loanManager.setLoanDAO(loanDAO);
        Loan loan = new Loan();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date today = format.parse("02-02-2019");
        when(loanManager.getTodayDate()).thenReturn(today);
        loan.setPlannedEndDate(format.parse("12-02-2019"));
        when(loanDAO.getLoanById(anyInt())).thenReturn(loan);
        assertEquals("PROGRESS", loanManager.getLoanStatus(55));

    }

    @Test
    @DisplayName("should return null if loan not found")
    void getLoanStatus4() {
        when(loanDAO.getLoanById(anyInt())).thenReturn(null);
        assertNull(loanManager.getLoanStatus(55));

    }

    @Test
    @DisplayName("should return bookManager")
    void getBookManager() {
        LoanManagerImpl loanManager = new LoanManagerImpl();
        BookManagerImpl bookManager = new BookManagerImpl();
        loanManager.setBookManager(bookManager);
        assertEquals(bookManager, loanManager.getBookManager());
    }

    @Test
    @DisplayName("should set bookManager")
    void setBookManager() {
        LoanManagerImpl loanManager = new LoanManagerImpl();
        BookManagerImpl bookManager = new BookManagerImpl();
        loanManager.setBookManager(bookManager);
        assertEquals(bookManager, loanManager.getBookManager());
    }

    @Test
    @DisplayName("should return memberManager")
    void getMemberManager() {
        LoanManagerImpl loanManager = new LoanManagerImpl();
        MemberManagerImpl memberManager = new MemberManagerImpl();
        loanManager.setMemberManager(memberManager);
        assertEquals(memberManager, loanManager.getMemberManager());
    }

    @Test
    @DisplayName("should set memberManager")
    void setMemberManager() {
        LoanManagerImpl loanManager = new LoanManagerImpl();
        MemberManagerImpl memberManager = new MemberManagerImpl();
        loanManager.setMemberManager(memberManager);
        assertEquals(memberManager, loanManager.getMemberManager());
    }


}


