package org.troparo.consumer.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;
import static org.mockito.Mockito.*;
import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Sql(scripts = "classpath:resetDb.sql")
@Transactional
class LoanDAOImplTest {

    private Logger logger = Logger.getLogger(LoanDAOImplTest.class);



    @Inject
    private LoanDAO loanDAO;
    @Inject
    private BookDAO bookDAO;

    @BeforeEach
    void reset() {
        logger.info("size: " + loanDAO.getLoans().size());
        logger.info("reset db");
    }

    @Test
    @DisplayName("should return the oldest reservation")
    void getPendingReservation() throws InterruptedException {
        Loan loan = loanDAO.getLoanById(3);
        loan.setBook(null);
        Loan loan1 = loanDAO.getLoanById(1);
        loan1.setBook(null);
        loan.setReservationDate(new Date());
        loan.setStartDate(null);
        loan1.setStartDate(null);
        wait(500);
        loan1.setReservationDate(new Date());
        loanDAO.updateLoan(loan);
        loanDAO.updateLoan(loan1);
        assertEquals(loan, loanDAO.getPendingReservation("12345678OK"));

    }

    @Test
    @DisplayName("should return null when no pending reservation")
    void getPendingReservation1() {

        assertNull(loanDAO.getPendingReservation("22"));

    }


    @Test
    @DisplayName("should return pending loans with no books")
    void getAllPendingReservationWithNoBook(){
        assertEquals(1, loanDAO.getAllPendingReservationWithNoBook().size());
    }

    @Test
    @DisplayName("should return null if invalid isbn")
    void getNextAvailableBook() {
        assertNull(loanDAO.getNextAvailableBook("invalidIsbn"));

    }

    @Test
    @DisplayName("should return reminder list")
    void getReminderLoans(){
       // System.out.println(loanDAO.getReminderLoans(3).get(0).getClass());
        assertEquals(1,loanDAO.getReminderLoans(5).size());

    }



    @Test
    @DisplayName("should return loans having books but no startDate")
    void getLoansReadyForStart(){
        assertEquals(1, loanDAO.getLoansReadyForStart().size());
    }

    @Test
    @DisplayName("should return null if currently rented")
    void getNextAvailableBook1() {
        assertNull(loanDAO.getNextAvailableBook("555784913P"));

    }

    @Test
    @DisplayName("should return book is any available")
    void getNextAvailableBook2() {
        Book book = bookDAO.getBookById(1);
        assertEquals(book, loanDAO.getNextAvailableBook("12345678OK"));

    }

    @Test
    @DisplayName("should return list of loans")
    void getLoans() {
        assertEquals(9, loanDAO.getLoans().size());
    }

    @Test
    @DisplayName("should return empty list if session is null")
    void getLoans1() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoans().size());
    }

    @Test
    @DisplayName("should remove existing loan")
    void removeLoan() {
        Loan loan = loanDAO.getLoanById(2);
        assertAll(
                () -> assertEquals(9, loanDAO.getLoans().size()),
                () -> assertTrue(loanDAO.removeLoan(loan)),
                () -> assertEquals(8, loanDAO.getLoans().size())
        );
    }

    @Test
    @DisplayName("remove loan")
    void removeLoan1() {
        Loan loan = loanDAO.getLoanById(22);
        assertFalse(loanDAO.removeLoan(loan));
    }


    @Test
    @DisplayName("should add a loan")
    void addLoan() {
        Loan loan = new Loan();
        loan.setIsbn("hijj");
        assertTrue(loanDAO.addLoan(loan));

    }

    @Test
    @DisplayName("should add a loan")
    void addLoan1() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertFalse(loanDAO1.addLoan(new Loan()));
    }


    @Test
    @DisplayName("should update loan")
    void updateLoan() {
        int loanId = 4;
        int bookId = 1;
        Loan loan = loanDAO.getLoanById(loanId);
        Book newBook = bookDAO.getBookById(bookId);
        assertNotEquals(newBook, loanDAO.getLoanById(loanId).getBook());
        loan.setBook(newBook);
        assertAll(
                () -> assertTrue(loanDAO.updateLoan(loan)),
                () -> assertEquals(newBook, loanDAO.getLoanById(loanId).getBook())

        );

    }

    @Test
    @DisplayName("should update loan")
    void updateLoan1() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertFalse(loanDAO1.updateLoan(new Loan()));
    }


    @Test
    @DisplayName("should return loan if existing id")
    void getLoanById() {
        assertNotNull(loanDAO.getLoanById(4));
    }

    @Test
    @DisplayName("should return loan if none-existing id")
    void getLoanById1() {
        assertNull(loanDAO.getLoanById(34));
    }

    @Test
    @DisplayName("shoult return list of loans if existing ISBN")
    void getLoanByIsbn() {
        assertNotEquals(0, loanDAO.getLoanByIsbn("12345678OK"));
    }

    @Test
    @DisplayName("shoult return empty list if none-existing ISBN")
    void getLoanByIsbn1() {
        assertEquals(0, loanDAO.getLoanByIsbn("12345678sK").size());
    }

    @Test
    @DisplayName("shoult return empty list if ISBN is null")
    void getLoanByIsbn2() {
        assertEquals(0, loanDAO.getLoanByIsbn(null).size());
    }

    @Test
    @DisplayName("shoult return empty list if ISBN is null")
    void getLoanByIsbn3() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoanByIsbn("ded").size());
    }


    @Test
    @DisplayName("should return not empty list if existing loan for a login")
    void getLoanByLogin() {
        assertEquals(7, loanDAO.getLoanByLogin("Jpolino").size());
    }

    @Test
    @DisplayName("should return  empty list if none-existing loan for a login")
    void getLoanByLogin1() {
        assertEquals(0, loanDAO.getLoanByLogin("Jpoline").size());
    }

    @Test
    @DisplayName("should return  empty list if session is null")
    void getLoanByLogin2() {
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoanByLogin("Jpoline").size());
    }


    @Test
    @DisplayName("should return empty list if invalid criteria")
    void getLoansByCriterias() {
        Map<String, String> map = new HashMap<>();
        map.put("plouf", "jpolinfo");
        assertEquals(0, loanDAO.getLoansByCriteria(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriterias_bookId() {
        Map<String, String> map = new HashMap<>();
        map.put("book_id", "6");
        assertEquals(1, loanDAO.getLoansByCriteria(map).size());
    }


    @Test
    @DisplayName("should return empty list of loans if criterias null")
    void getLoansByCriterias2() {
        assertEquals(0, loanDAO.getLoansByCriteria(null).size());
    }

    @Test
    @DisplayName("should return empty list of loans if criterias value is empty")
    void getLoansByCriterias3() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "");
        assertEquals(0, loanDAO.getLoansByCriteria(null).size());
    }

    @Test
    @DisplayName("should return empty list of loans if empty map passed")
    void getLoansByCriteria4() {
        Map<String, String> map = new HashMap<>();
        assertEquals(0, loanDAO.getLoansByCriteria(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriteria_login() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "lokii");
        assertEquals(1, loanDAO.getLoansByCriteria(map).size());
    }

    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriteria_login1() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "");
        assertEquals(0, loanDAO.getLoansByCriteria(map).size());
    }


    @Test
    @DisplayName("should return list of loans if valid criteria and result")
    void getLoansByCriteria_Status() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        assertEquals(3, loanDAO.getLoansByCriteria(map).size());
    }

    @Test
    @DisplayName("should return empty list if loan status invalid")
    void getLoansByCriteria_Status1() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "wrongOne");
        assertEquals(0, loanDAO.getLoansByCriteria(map).size());
    }

    @Test
    @DisplayName("should return empty list if loan is null")
    void getLoansByCriteria_Status2() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        map.put("book_id", "5");
        LoanDAOImpl loanDAO1 = new LoanDAOImpl();
        loanDAO1.setSessionFactory(null);
        assertEquals(0, loanDAO1.getLoansByCriteria(map).size());
    }


    @Test
    @DisplayName("should return list if several valid criterias")
    void getLoansByCriteria_Status3() {
        Map<String, String> map = new HashMap<>();
        map.put("book_id", "5");
        map.put("status", "terminated");
        assertEquals(0, loanDAO.getLoansByCriteria(map).size());
    }


    @Test
    @DisplayName("should return nothing if invalid status")
    void addStatusToRequest() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "dede");
        assertEquals("", loanDAO.addStatusToRequest(map));
    }


    @Test
    @DisplayName("should return \" endDate is null\" string if progress")
    void addStatusToRequest2() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "progress");
        assertEquals(" where endDate is null", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return \"endDate is null and plannedEndDate < current_date\" string if overdue")
    void addStatusToRequest3() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "overdue");
        assertEquals(" where endDate is null and plannedEndDate < current_date", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return nothing if invalid status")
    void addStatusToRequest4() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "dede");
        assertEquals("", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return nothing if invalid status")
    void addStatusToRequest5() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "");
        assertEquals("", loanDAO.addStatusToRequest(map));
    }

    @Test
    @DisplayName("should return true when cleaning expired reservations")
    void cleanupExpiredReservation() {

        assertTrue( loanDAO.cleanupExpiredReservation(4));

    }


    @Test
    @DisplayName("should return 1")
    void cleanupExpiredReservationCount() {
        assertEquals(2, loanDAO.cleanupExpiredReservationCount(4));
    }




    @Test
    @DisplayName("should return \"where endDate is not null\" if empty map")
    void addStatusToRequest6() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        map.put("login", "bob");
        map.put("book_id", "15");
        assertEquals(" and endDate is not null", loanDAO.addStatusToRequest(map));
    }


    @Test
    @DisplayName("should return an empty string when map null")
    void extractStatusFromMap() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        assertEquals("", loanDAO.extractStatusFromMap(null));
    }

    @Test
    @DisplayName("should return an empty string when map empty")
    void extractStatusFromMap1() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        assertEquals("", loanDAO.extractStatusFromMap(map));
    }

    @Test
    @DisplayName("should return terminated if passed in status")
    void extractStatusFromMap2() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("status", "terminated");
        assertEquals("TERMINATED", loanDAO.extractStatusFromMap(map));
    }

    @Test
    @DisplayName("should return empty string if passed in status is incorrect")
    void extractStatusFromMap3() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("bassine", "terminated");
        assertEquals("", loanDAO.extractStatusFromMap(map));
    }

    @Test
    @DisplayName("should return string for 1 criteria")
    void createRequestFromMap() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("login", "bob");
        assertEquals("where borrower.login = :login", loanDAO.createRequestFromMap(map));
    }

    @Test
    @DisplayName("should return string for 2 criteria")
    void createRequestFromMap2() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        Map<String, String> map = new HashMap<>();
        map.put("login", "bob");
        map.put("book_id", "ob");
        assertEquals("where book_id = :book_id and borrower.login = :login", loanDAO.createRequestFromMap(map));
    }


    @Test
    @DisplayName("should return emptyList when no book available for dates")
    @Disabled
    void getListBooksAvailableOnThoseDates() throws ParseException {
        Loan loan = new Loan();
        Book book = new Book();
        String title = "test";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date startDate = simpleDateFormat.parse("2019-08-12");
        Date plannedEndDate = simpleDateFormat.parse("2019-08-19");
        book.setTitle(title);
        Member member = new Member();
        member.setLogin("jo");
        loan.setBorrower(member);
        loan.setBook(book);
        loan.setStartDate(startDate);
        loan.setPlannedEndDate(plannedEndDate);

        // assertTrue(loanDAO.getListBooksAvailableOnThoseDates(loan).isEmpty());
    }


    @Test
    @Disabled
    @DisplayName("should return bookList when book(s) available for dates")
    void getListBooksAvailableOnThoseDates1() throws ParseException {
        Loan loan = new Loan();
        Book book = new Book();
        String title = "LA GRANDE AVENTURE";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date startDate = simpleDateFormat.parse("2019-07-16");
        Date plannedEndDate = simpleDateFormat.parse("2019-08-10");
        book.setTitle(title);
        loan.setBook(book);
        loan.setStartDate(startDate);
        loan.setPlannedEndDate(plannedEndDate);
        /*System.out.println(loanDAO.getListBooksAvailableOnThoseDates(loan).get(0).getClass());
        assertFalse(loanDAO.getListBooksAvailableOnThoseDates(loan).isEmpty());*/
    }

    @Test
    @DisplayName("should return bookList when book(s) available for dates")
    @Disabled
    void getListBooksAvailableOnThoseDates2() throws ParseException {
        Loan loan = new Loan();
        Book book = new Book();
        String title = "bokana";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date startDate = simpleDateFormat.parse("2019-07-16");
        Date plannedEndDate = simpleDateFormat.parse("2019-08-10");
        book.setTitle(title);
        loan.setBook(book);
        loan.setStartDate(startDate);
        loan.setPlannedEndDate(plannedEndDate);
        // assertTrue(loanDAO.getListBooksAvailableOnThoseDates(loan).isEmpty());
    }

    @Test
    @DisplayName("should return true")
    void checkValidStatus() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        assertAll(
                () -> assertTrue(loanDAO.checkValidStatus("overdue")),
                () -> assertTrue(loanDAO.checkValidStatus("terminated")),
                () -> assertTrue(loanDAO.checkValidStatus("RESERVED")),
                () -> assertTrue(loanDAO.checkValidStatus("PROGRESS"))

        );

    }

    @Test
    @DisplayName("should return false")
    void checkValidStatus1() {
        LoanDAOImpl loanDAO = new LoanDAOImpl();
        assertFalse(loanDAO.checkValidStatus("fini"));
    }


}