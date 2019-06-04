package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.business.contract.EmailManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
class EmailManagerImplTest {

    @Inject
    private EmailManager emailManager;
    /*@Inject
    private LoanManager loanManager;*/

    @BeforeEach
    void init(){
        /*emailManager = new EmailManagerImpl();
        loanManager = spy(LoanManager.class);
        emailManager.setLoanManager(loanManager);*/

    }

  /*  @Test
    @DisplayName("should return the overdue list")
    void getOverdueEmailList() throws ParseException {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("status", "OVERDUE");
        List<Loan> loanList = new ArrayList<>();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Member member = new Member();
        member.setEmail("test@test.test");
        member.setFirstName("Bob");
        Loan loan = new Loan();
        loan.setBorrower(member);
        Book book = new Book();
        book.setTitle("Boravo");
        book.setIsbn("ISBN123");
        loan.setBook(book);
        Date startDate = simpleDateFormat.parse("2018-09-01");
        loan.setStartDate(startDate);
        Date plannedEndDate = simpleDateFormat.parse("2018-09-10");
        loan.setPlannedEndDate(plannedEndDate);
        loan.setId(2);
        loanList.add(loan);
        when(loanManager.getLoansByCriterias(criterias)).thenReturn(loanList);
        assertAll(
                ()->assertNotNull(emailManager.getOverdueEmailList()),
                ()-> assertEquals(1, emailManager.getOverdueEmailList().size()),
                ()->assertEquals("Bob", emailManager.getOverdueEmailList().get(0).getFirstname()),
                ()->assertEquals("Boravo", emailManager.getOverdueEmailList().get(0).getTitle()),
                ()->assertEquals(plannedEndDate, emailManager.getOverdueEmailList().get(0).getDueDate()),
                ()->assertEquals("ISBN123", emailManager.getOverdueEmailList().get(0).getIsbn())
        );
    }*/

    @Test
    @DisplayName("should return the overdue mailing list")
    void getOverdueEmailList1() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse("2018-09-02");
        EmailManagerImpl emailManager = mock(EmailManagerImpl.class);
        when(emailManager.getTodaySDate()).thenReturn(date1);
        assertNotNull(emailManager.getOverdueEmailList());
    }

    @Test
    @DisplayName("should return the days difference")
    void calculateDaysBetweenDates() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse("2018-09-02");
        Date date2= simpleDateFormat.parse("2018-09-09");
        assertEquals(7, emailManager.calculateDaysBetweenDates(date1, date2) );
    }




}