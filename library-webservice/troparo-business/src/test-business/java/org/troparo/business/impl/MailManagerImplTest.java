package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.business.contract.MailManager;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
class MailManagerImplTest {

    @Inject
    private MailManager mailManager;
    /*@Inject
    private LoanManager loanManager;*/

    @BeforeEach
    void init(){
        /*mailManager = new MailManagerImpl();
        loanManager = spy(LoanManager.class);
        mailManager.setLoanManager(loanManager);*/

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
                ()->assertNotNull(mailManager.getOverdueEmailList()),
                ()-> assertEquals(1, mailManager.getOverdueEmailList().size()),
                ()->assertEquals("Bob", mailManager.getOverdueEmailList().get(0).getFirstname()),
                ()->assertEquals("Boravo", mailManager.getOverdueEmailList().get(0).getTitle()),
                ()->assertEquals(plannedEndDate, mailManager.getOverdueEmailList().get(0).getDueDate()),
                ()->assertEquals("ISBN123", mailManager.getOverdueEmailList().get(0).getIsbn())
        );
    }*/

    @Test
    @DisplayName("should return the overdue mailing list")
    void getOverdueEmailList1() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse("2018-09-02");
        MailManagerImpl emailManager = mock(MailManagerImpl.class);
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
        assertEquals(7, mailManager.calculateDaysBetweenDates(date1, date2) );
    }




}