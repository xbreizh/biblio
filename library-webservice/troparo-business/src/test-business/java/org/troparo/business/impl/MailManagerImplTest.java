package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MailManager;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Mail;
import org.troparo.model.Member;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
class MailManagerImplTest {

    @Inject
    private MailManager mailManager;

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("should set loanmanager")
    void setLoanManager() {
        LoanManager loanManager = new LoanManagerImpl();
        mailManager.setLoanManager(loanManager);
        assertEquals(loanManager, mailManager.getLoanManager());
    }

    @Test
    @DisplayName("should return date")
    void getTodaySDate() {
        MailManagerImpl mailManager = new MailManagerImpl();
        assertEquals(new Date(), mailManager.getTodaySDate());
    }


    @Test
    @DisplayName("should return the overdue mailing list")
    void getOverdueEmailList1() {

        assertNotNull(mailManager.getOverdueEmailList());
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
    @DisplayName("should return data")
    void gettingDataForLoan() throws ParseException {
        List<Loan> loanList = new ArrayList<>();
        List<Mail> mailList = new ArrayList<>();
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

        MailManagerImpl mailManager1 = new MailManagerImpl();
        assertAll(
                ()-> assertEquals(title, mailManager1.gettingDataForLoan(loanList).get(0).getTitle()),
                ()-> assertEquals(author, mailManager1.gettingDataForLoan(loanList).get(0).getAuthor()),
                ()-> assertEquals(edition, mailManager1.gettingDataForLoan(loanList).get(0).getEdition()),
                ()-> assertEquals(firstName, mailManager1.gettingDataForLoan(loanList).get(0).getFirstName()),
                ()-> assertEquals(lastName, mailManager1.gettingDataForLoan(loanList).get(0).getLastName()),
                ()-> assertEquals(email, mailManager1.gettingDataForLoan(loanList).get(0).getEmail())

        );

    }


}