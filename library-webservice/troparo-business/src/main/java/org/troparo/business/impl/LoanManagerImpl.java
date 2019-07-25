package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.consumer.enums.LoanStatus;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;


@Configuration
@Transactional
@Named
public class LoanManagerImpl implements LoanManager {
    @Inject
    LoanDAO loanDAO;
    @Inject
    BookManager bookManager;
    @Inject
    MemberManager memberManager;
    @Value("${loanDuration}")
    private String loanDurationString;
    private int loanDuration;
    @Value("${renewDuration}")
    private String renewDurationString;
    private int renewDuration;
    @Value("${maxBooks}")
    private String maxBooksString;
    private int maxBooks;
    @Value("${maxReserve}")
    private String maxReserveString;
    private int maxReserve;
    private static final String STATUS = "status";
    private static final String LOGIN = "login";


    public int getMaxReserve() {
        return maxReserve;
    }


    private static  Logger logger = Logger.getLogger(LoanManagerImpl.class);

    public LoanManagerImpl() {
        workaroundConfigFile();
    }

    void workaroundConfigFile() {
        if (loanDurationString != null && !loanDurationString.isEmpty()) {
            loanDuration = Integer.parseInt(loanDurationString);
        } else {
            loanDuration = 28;
        }
        if (renewDurationString != null && !renewDurationString.isEmpty()) {
            renewDuration = Integer.parseInt(renewDurationString);
        } else {
            renewDuration = 28;
        }
        if (maxBooksString != null && !maxBooksString.isEmpty()) {
            maxBooks = Integer.parseInt(maxBooksString);
        } else {
            maxBooks = 4;
        }
        if (maxReserveString != null && !maxReserveString.isEmpty()) {
            maxReserve = Integer.parseInt(maxReserveString);
        } else {
            maxReserve = 3;
        }
    }

    @Override
    public String addLoan(String login, int id) {
        Member member = memberManager.getMemberByLogin(login);
        Book book = bookManager.getBookById(id);
        String x1 = checkAddLoanDetailsAreValid(member, book);
        if (!x1.isEmpty()) return x1;

        Loan loan = new Loan();
        loan.setStartDate(getTodayDate());
        setPlannedEndDate(loan);
        loan.setBook(book);
        loan.setBorrower(member);
        logger.info("new Booking");
        return "";
    }

    private String checkAddLoanDetailsAreValid(Member member, Book book) {

        if(member == null) return "Invalid member";

        if(book == null) return "Invalid book";
        return "";
    }

  /*  String newBooking(Loan loan) {
        loan.setStartDate(getTodayDate());
        setPlannedEndDate(loan);
        String x = checkBookAndMemberValidity(loan.getBorrower(), loan.getBook().getIsbn());
        if (!x.isEmpty()) return x;

        // checks if loan is possible
        *//*if(loanDAO.getListBooksAvailableOnThoseDates(loan).isEmpty()){
            return "book is not available: " + loan.getBook().getId();
        }*//*
        // checks if borrower can borrow
        if (checkIfBorrowerHasReachedMaxLoan(loan)) return "max number of books rented reached";
        return "";
    }*/

    private boolean checkIfBorrowerHasReachedMaxLoan(Member member) {

      return memberManager.getMemberById(member.getId()).getLoanList().size() >= maxBooks;

    }

    void setPlannedEndDate(Loan loan) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(loan.getStartDate());
        cal.add(Calendar.DATE, loanDuration);
        loan.setPlannedEndDate(cal.getTime());
    }

    @Override
    public String reserve(String token, String isbn) {
        Member member = memberManager.getMemberByToken(token);
        String x1 = checkReserveLoanDetailsAreValid(member, isbn);
        if (!x1.isEmpty()) return x1;
        Loan loan = new Loan();
        loan.setIsbn(isbn);
        loan.setReservationDate(getTodayDate());
        loan.setBorrower(member);
        if (!(loanDAO.addLoan(loan))) return "Issue while reserving";
        getBookIfAvailable(loan);
        if(loan.getBook()!=null){
            loan.setAvailableDate(getTodayDate());
            return "The book has been reserved and is available for collection for 4 days";
        }
        return "The book has been reserved but is currently unavailable. We will contact you as soon as it's ready";
    }

    private void getBookIfAvailable(Loan loan) {
        loan.setBook(loanDAO.getNextAvailableBook(loan.getIsbn()));
    }


    @Override
    public boolean checkinBooking(String token, int id) {
        if (!memberManager.checkAdmin(token))return false;

        Loan loan = loanDAO.getLoanById(id);
        return loanDAO.updateLoan(loan);

    }

    String checkReserveLoanDetailsAreValid(Member member, String isbn) {
        String x;


        x = checkBookAndMemberValidity(member, isbn);
        if (!x.isEmpty()) return x;

        x = checkIfReserveLimitNotReached(member.getLogin());
        if (!x.isEmpty()) return x;

        return "";
    }

    String checkLoanStartDateIsNotInPastOrNull(Date date) {
        if (date == null || date.before(getTodayDate()))return "startDate should be in the future";
        return "";
    }

   /* String checkIfNoOverLapping(Loan loan) {
        Date startDate = loan.getStartDate();
        Date plannedEndDate = loan.getPlannedEndDate();
        if(startDate == null || plannedEndDate==null)return "startDate and plannedEndDate should be filled";
       // List<Book> bookList = loanDAO.getListBooksAvailableOnThoseDates(loan);

        if (!bookList.isEmpty()) {
            loan.setBook(bookList.get(0));
            return "";
        }
        return "No book available for those dates!";
    }*/



    boolean checkIfOverDue(Member member) {
        Map<String, String> map = new HashMap<>();
        map.put(LOGIN, member.getLogin());
        map.put(STATUS, LoanStatus.OVERDUE.toString());
        return !loanDAO.getLoansByCriteria(map).isEmpty();
    }

    String checkIfReserveLimitNotReached(String login) {
        Map<String, String> map = new HashMap<>();
        map.put(LOGIN, login);
        map.put(STATUS, LoanStatus.RESERVED.toString());
        List<Loan> loanList = loanDAO.getLoansByCriteria(map);
        if (loanList.size() >= maxReserve)
            return "You have already reached the maximum number of reservation: " + maxReserve;
        return "";
    }

    private String checkStartDateIsTodayOrFuture(Date date) {
        if (date == null) return "No startDate passed";
        Calendar startDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        startDate.setTime(date);
        today.setTime(getTodayDate());
        if (
                (startDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)) &&
                        (startDate.get(Calendar.MONTH) < today.get(Calendar.MONTH)) &&
                        (startDate.get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR))) {
            return "the booking date cannot be in the past";
        }
        return "";
    }


    String checkBookAndMemberValidity(Member member, String isbn) {

        if ( member== null) {
            return "invalid member";
        }
        if (bookManager.getBookByIsbn(isbn)==null) {
            return "invalid book";
        }

        if(checkIfBorrowerHasReachedMaxLoan(member))return "Max Loans reached";

        if(checkIfOverDue(member))return "There are Overdue Items";

        // if borrower already has the book in renting, he can't reserve it
        if (checkIfSimilarLoanPlannedOrInProgress(member, isbn)) return "That book is already has a renting in progress or planned for that user";
        return "";
    }

    boolean checkIfSimilarLoanPlannedOrInProgress(Member member, String isbn) {
       List<Loan> loanList = loanDAO.getLoanByLogin(member.getLogin());

       if(loanList!=null && !loanList.isEmpty()){
           for(Loan loan: loanList){
               if (loan.getIsbn().equals(isbn) && loan.getEndDate() !=null){
                   logger.error("there is already a loan with that book and that login");
                   return true;
               }
           }
       }
       return false;
    }


    @Override
    public List<Loan> getLoans() {
        return loanDAO.getLoans();
    }

    @Override
    public Loan getLoanById(int id) {
        logger.info("getting id (from business): " + id);

        return loanDAO.getLoanById(id);
    }

    @Override
    public List<Loan> getLoansByCriteria(Map<String, String> map) {
        String[] validCriteria = {"LOGIN", "BOOK_ID", "STATUS", "ISBN"};
        List<String> validCriteriaList = Arrays.asList(validCriteria);
        Map<String, String> criteria = new HashMap<>();

        String[] validStatus = {"terminated", "progress", "overdue"};
        List<String> validStatusList = Arrays.asList(validStatus);

        List<Loan> loanList = new ArrayList<>();

        for (Map.Entry<String, String> entry : map.entrySet()
        ) {

            if (entry.getKey() != null && entry.getValue() != null &&
                    !entry.getValue().equals("?") && !entry.getValue().equals("") && !entry.getValue().equals("-1")
                    && validCriteriaList.contains(entry.getKey().toUpperCase())) {
                if (entry.getKey().equalsIgnoreCase(STATUS) && !validStatusList.contains(entry.getValue().toLowerCase())) {
                    return loanList;
                }
                criteria.put(entry.getKey(), entry.getValue());

            }

        }


        logger.info("map: " + criteria);
        logger.info("map: " + map);
        logger.info("criteria: " + criteria);
        return loanDAO.getLoansByCriteria(criteria);
    }

    @Override
    public String renewLoan(int id) {
        Loan loan = loanDAO.getLoanById(id);

        if (loan.getEndDate() != null) {
            return "loan already terminated: " + loan.getEndDate();
        }

        if (isRenewable(id)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loan.getPlannedEndDate());
            cal.add(Calendar.DATE, renewDuration);
            loan.setPlannedEndDate(cal.getTime());
            loanDAO.updateLoan(loan);
            return "";

        }
        return "loan has already been renewed";

    }

    @Override
    public String removeLoan(String token, int id) {
        /*Loan loan =*/ loanDAO.getLoanById(id);
       /* logger.info("loan check: "+loan.isChecked());
        if(memberManager.checkAdmin(token)|| (!loan.isChecked() && loan.getBorrower().getToken().equals(token))){
            if(loanDAO.removeLoan(loan)){
                return "loan removed";
            }

        }*/
        return "You can't remove that loan, please contact the Administration";
    }


    public boolean checkIfPendingReservation(String isbn){
        return loanDAO.getPendingReservation(isbn) != null;

    }


    @Override
    public boolean isRenewable(int id) {
        logger.info("checking if loan " + id + " is renewable");
        Loan loan = loanDAO.getLoanById(id);
        logger.info("renew duration: " + loanDuration);

        if (loan.getEndDate() != null) {
            logger.info("endDate false");
            return false;
        }

        Date start = loan.getStartDate();
        Date end = loan.getPlannedEndDate();

        //we get the number of days between the start and the planned end date
        //if difference > loanDuration, it means the loan has already been renewed(return false)
        int diffInDays = (int) ((end.getTime() - start.getTime())
                / (1000 * 60 * 60 * 24));
        logger.info("diffDays: " + diffInDays);


        // we check that by adding the renewDuration to the plannedEndDate, we have a later date than today
        if (checkAddingRenewDurationGivesLaterThanToday(end)) return false;

        return diffInDays < (loanDuration + renewDuration);

    }

    boolean checkAddingRenewDurationGivesLaterThanToday(Date end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        cal.add(Calendar.DATE, renewDuration);
        Date date = cal.getTime();
        return date.before(getTodayDate());
    }

    @Override
    public String terminate(int id) {
        Loan loan;
        try {
            loan = loanDAO.getLoanById(id);
            if (loan.getEndDate() == null) {
                loan.setEndDate(new Date());
                transferBookToPendingLoanIfAny(loan.getBook());
                loanDAO.updateLoan(loan);
            } else {
                return "loan already terminated";
            }
        } catch (NullPointerException e) {
            return "loan couldn't be terminated!";
        }
        return "";
    }

    public void transferBookToPendingLoanIfAny(Book book) {
        Loan pendingLoan = loanDAO.getPendingReservation(book.getIsbn());
        if (pendingLoan!=null){
            pendingLoan.setBook(book);
            pendingLoan.setAvailableDate(getTodayDate());
            loanDAO.updateLoan(pendingLoan);
        }

    }


    @Override
    public String getLoanStatus(int id) {
        Loan loan;
        logger.info("getting loan status");
        Date today = getTodayDate();
        logger.info("today: " + today);
        try {
            loan = loanDAO.getLoanById(id);
            if (loan.getEndDate() != null) {
                return "TERMINATED";
            }
            if (loan.getPlannedEndDate().before(today)) {
                return "OVERDUE";
            }
            if (loan.getStartDate().after(today)) {
                return "PLANNED";
            }
                return "PROGRESS";

        } catch (NullPointerException e) {
            logger.error("error while getting loan status");
            return null;
        }
    }

    public Date getTodayDate() {
        return new Date();
    }

    @Override
    public void setLoanDAO(LoanDAO loanDAO) {
        this.loanDAO = loanDAO;
    }


    public BookManager getBookManager() {
        return bookManager;
    }

    public void setBookManager(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    public MemberManager getMemberManager() {
        return memberManager;
    }

    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }
}
