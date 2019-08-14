package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.MailManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class MailManagerImplTest {

    /*@Inject*/
    private MailManager mailManager;

    private LoanManagerImpl loanManager;

    private MemberManager memberManager;

    @BeforeEach
    void init() {
        mailManager = new MailManagerImpl();
        loanManager = mock(LoanManagerImpl.class);
        memberManager = mock(MemberManagerImpl.class);
        mailManager.setMemberManager(memberManager);
        mailManager.setLoanManager(loanManager);
    }


    @Test
    @DisplayName("should add 2 days to date")
    void calculateEndAvailableDate() throws ParseException {
        Loan loan = new Loan();
        String dt = "2008-01-01";  // Start date
        String dt2 = "2008-01-05";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        c.setTime(sdf.parse(dt));
        Date availableDate = c.getTime();

        c.setTime(sdf.parse(dt2));
        Date endAvailable = c.getTime();
        loan.setAvailableDate(availableDate);

        assertEquals(endAvailable, mailManager.calculateEndAvailableDate(loan, 4));

    }

    @Test
    @DisplayName("should return empty list")
    void getLoansReminder() {
        String token = "token1234";
        when(memberManager.checkAdmin(anyString())).thenReturn(false);
        assertEquals(0, mailManager.getLoansReminder(token).size());

    }

    @Test
    @DisplayName("should return empty list")
    void getLoansReminder1() {
        List<Loan> loanList = new ArrayList<>();
        Loan loan = new Loan();
        loanList.add(loan);
        String token = "token1234";
        Member member = new Member();
        member.setEmail("dede@dede.de");
        member.setFirstName("Paul");
        member.setLastName("Jorki");
        Book book = new Book();
        book.setTitle("koko");
        book.setAuthor("Joe dassi");
        book.setEdition("darmond");
        book.setIsbn("isbn332");
        loan.setBook(book);
        loan.setBorrower(member);
        when(loanManager.getReminderLoans(anyInt())).thenReturn(loanList);
        when(memberManager.checkAdmin(anyString())).thenReturn(true);
        assertEquals(1, mailManager.getLoansReminder(token).size());

    }

    @Test
    @DisplayName("should set loanManager")
    void setLoanManager() {

        assertEquals(loanManager, mailManager.getLoanManager());
    }

    @Test
    @DisplayName("should return date")
    void getTodaySDate() {
        MailManagerImpl mailManager = new MailManagerImpl();
        assertEquals(new Date(), mailManager.getTodaySDate());
    }

    @Test
    @DisplayName("should return password ResetList")
    void convertMemberListIntoMailList() {
        MailManagerImpl mailManager = new MailManagerImpl();
        List<Member> memberList = new ArrayList<>();
        Member member = new Member();
        String login = "logni";
        String token = "dededkfkfk";
        String email = "ffr@frfr.fr";
        member.setLogin(login);
        member.setToken(token);
        member.setEmail(email);
        memberList.add(member);
        assertAll(
                () -> assertEquals(login, mailManager.convertMemberListIntoMailList(memberList).get(0).getLogin()),
                () -> assertEquals(token, mailManager.convertMemberListIntoMailList(memberList).get(0).getToken()),
                () -> assertEquals(email, mailManager.convertMemberListIntoMailList(memberList).get(0).getEmail())

        );

    }


    @Test
    @DisplayName("should return the overdue mailing list")
    void getOverdueEmailList() {

        assertNotNull(mailManager.getOverdueEmailList());
    }

    @Test
    @DisplayName("should return the overdue mailing list")
    void getOverdueEmailList1() {

        assertNotNull(mailManager.getOverdueEmailList());
    }

    @Test
    @DisplayName("should remove TEMP from token")
    void removeTempFromToken() {
        MailManagerImpl mailManager = new MailManagerImpl();
        String newToken = "dedekbnkjnkjn4nln4kl";
        String oldToken = "TEMP" + newToken;
        assertEquals(newToken, mailManager.removeTempFromToken(oldToken));

    }

    @Test
    @DisplayName("should return the days difference")
    void calculateDaysBetweenDates() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse("2018-09-02");
        Date date2 = simpleDateFormat.parse("2018-09-09");
        assertEquals(7, mailManager.calculateDaysBetweenDates(date1, date2));
    }

    @Test
    @DisplayName("should return empty list")
    void gettingDataForLoan() {
        MailManagerImpl mailManager1 = new MailManagerImpl();
        List<Loan> loanList = new ArrayList<>();

        assertTrue(mailManager1.gettingDataForLoan(loanList).isEmpty());
    }

    @Test
    @DisplayName("should return data")
    void gettingDataForLoan1() throws ParseException {
        MailManagerImpl mailManager1 = new MailManagerImpl();
        List<Loan> loanList = new ArrayList<>();
        String email = "email";
        String isbn = "isbn123";
        String firstName = "firstname";
        String lastName = "lastName";
        String author = "author";
        String title = "title";
        String edition = "edition";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date plannedEndDate = simpleDateFormat.parse("2019-05-22");
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setEdition(edition);
        Member member = new Member();
        member.setEmail(email);
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setLogin(lastName);
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBorrower(member);
        loan.setPlannedEndDate(plannedEndDate);
        loanList.add(loan);


        assertAll(
                () -> assertEquals(title, mailManager1.gettingDataForLoan(loanList).get(0).getTitle()),
                () -> assertEquals(author, mailManager1.gettingDataForLoan(loanList).get(0).getAuthor()),
                () -> assertEquals(edition, mailManager1.gettingDataForLoan(loanList).get(0).getEdition()),
                () -> assertEquals(firstName, mailManager1.gettingDataForLoan(loanList).get(0).getFirstName()),
                () -> assertEquals(lastName, mailManager1.gettingDataForLoan(loanList).get(0).getLastName()),
                () -> assertEquals(email, mailManager1.gettingDataForLoan(loanList).get(0).getEmail())

        );

    }


}