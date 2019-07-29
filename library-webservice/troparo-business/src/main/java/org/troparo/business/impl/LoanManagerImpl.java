package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
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
    private LoanDAO loanDAO;

    private BookManager bookManager;

    private MemberManager memberManager;
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
    @Value("${nbDaysReservation}")
    private String nbDaysReservationString;
    private int nbDaysReservation;
    private static final String STATUS = "status";
    private static final String LOGIN = "login";


    public int getMaxReserve() {
        return maxReserve;
    }

    public LoanManagerImpl( BookManager bookManager, MemberManager memberManager) {
        this.bookManager = bookManager;
        this.memberManager = memberManager;
    }

    private static Logger logger = Logger.getLogger(LoanManagerImpl.class);

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
        if (nbDaysReservationString != null && !nbDaysReservationString.isEmpty()) {
            nbDaysReservation = Integer.parseInt(nbDaysReservationString);
        } else {
            nbDaysReservation = 3;
        }
    }


    @Override
    public String addLoan(String login, int id) {
        logger.info("trying to add loan");
        logger.info("login: " + login);
        Member member = memberManager.getMemberByLogin(login);
        Book book = bookManager.getBookById(id);
        String x = checkAddLoanDetailsAreValid(member, book);
        if (!x.isEmpty()) {
            logger.error(x);
            return x;
        }
        Loan loan = new Loan();
        loan.setStartDate(getTodayDate());
        setPlannedEndDate(loan);
        loan.setBook(book);
        loan.setBorrower(member);
        logger.info("new Booking");
        return "";
    }

    private String checkAddLoanDetailsAreValid(Member member, Book book) {
        logger.info("member to check: " + member);

        if (member == null) return "Invalid member";

        if (book == null) return "Invalid book";

        if (checkIfBorrowerHasReachedMaxLoan(member)) return "max number of books rented reached";

        return "";
    }


    boolean checkIfBorrowerHasReachedMaxLoan(Member member) {
        logger.info("checking if max loans reached");
        logger.info("memberList: " + member.getLoanList());
        if (member.getLoanList() == null || member.getLoanList().isEmpty()) {
            logger.info("member list empty");
            return false;
        }
        List<Loan> wholeLoanList = new ArrayList<>();
        for (Loan loan : member.getLoanList()
        ) {
            if (loan.getStartDate() != null && loan.getEndDate() != null) {
                wholeLoanList.add(loan);
                logger.info("adding loan");
            }
        }
        logger.info("current Loans: " + wholeLoanList.size());

        return wholeLoanList.size() >= maxBooks;

    }

    void setPlannedEndDate(Loan loan) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(loan.getStartDate());
        cal.add(Calendar.DATE, loanDuration);
        loan.setPlannedEndDate(cal.getTime());
    }

    @Override
    public String reserve(String token, String isbn) {
        logger.info("trying to reserve: " + isbn);
        Member member = memberManager.getMemberByToken(token);
        String x1 = checkReserveLoanDetailsAreValid(member, isbn);
        if (!x1.isEmpty()) return x1;
        Loan loan = new Loan();
        loan.setIsbn(isbn);
        loan.setReservationDate(getTodayDate());
        loan.setBorrower(member);
        if (!(loanDAO.addLoan(loan))) return "Issue while reserving";
        logger.info("loan has been reserved: " + isbn);
        getBookIfAvailable(loan);
        if (loan.getBook() != null) {
            loan.setAvailableDate(getTodayDate());
            loanDAO.updateLoan(loan);
            return "The book has been reserved and is available for collection for 4 days";
        }
        return "The book has been reserved but is currently unavailable. We will contact you as soon as it's ready";
    }


    private void getBookIfAvailable(Loan loan) {
        logger.info("trying to get available book");
        loan.setBook(loanDAO.getNextAvailableBook(loan.getIsbn()));
    }


    @Override
    public boolean checkinBooking(String token, int id) {
        if (!memberManager.checkAdmin(token)) return false;

        Loan loan = loanDAO.getLoanById(id);
        return loanDAO.updateLoan(loan);

    }

    String checkReserveLoanDetailsAreValid(Member member, String isbn) {
        String x;


        x = checkBookAndMemberValidity(member, isbn);
        if (!x.isEmpty()) {
            logger.error("error found: " + x);
            return x;
        }

        x = checkIfReserveLimitNotReached(member.getLogin());
        if (!x.isEmpty()) {
            logger.error("error found: " + x);
            return x;
        }

        return "";
    }


    String checkLoanStartDateIsNotInPastOrNull(Date date) {
        if (date == null || date.before(getTodayDate())) return "startDate should be in the future";
        return "";
    }


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


    String checkBookAndMemberValidity(Member member, String isbn) {

        if (member == null) {
            return "invalid member";
        }
        if (bookManager.getBookByIsbn(isbn) == null) {
            return "invalid book";
        }

        if (checkIfBorrowerHasReachedMaxLoan(member)) return "Max Loans reached";

        if (checkIfOverDue(member)) return "There are Overdue Items";

        // if borrower already has the book in renting, he can't reserve it
        if (checkIfSimilarLoanPlannedOrInProgress(member, isbn))
            return "That book is already has a renting in progress or planned for that user";
        return "";
    }

    boolean checkIfSimilarLoanPlannedOrInProgress(Member member, String isbn) {
        List<Loan> loanList = loanDAO.getLoanByLogin(member.getLogin());
        logger.info("isbn received : " + isbn);
        if (loanList != null && !loanList.isEmpty()) {
            for (Loan loan : loanList) {
                logger.info("isbn from loan: " + loan.getIsbn());
                logger.info("end date: " + loan.getEndDate());
                if (loan.getIsbn().equals(isbn) && loan.getStartDate() != null && loan.getEndDate() == null) {
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
    public String cancelLoan(String token, int id) {
        Member member = memberManager.getMemberByToken(token);
        if (member == null) return "Invalid Member";
        Loan loan = loanDAO.getLoanById(id);
        if (loan == null) return "Invalid Loan";
        if (memberManager.checkAdmin(token) || (loan.getBorrower().equals(member) && loan.getStartDate() == null)) {
            loan.setEndDate(getTodayDate());
            if (loanDAO.updateLoan(loan)) return "The loan has been cancelled";

        }
        return "You can't remove that loan, please contact the Administration";
    }


    public boolean checkIfPendingReservation(String isbn) {
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
        if (pendingLoan != null) {
            pendingLoan.setBook(book);
            pendingLoan.setAvailableDate(getTodayDate());
            loanDAO.updateLoan(pendingLoan);
            logger.info("pending loan updated");
        } else {
            logger.info("no pending loan");
        }
    }

    @Override
    @Scheduled(cron = "5 0 * * 1-5") // runs every week day at 00:05
    public void cleanupExpiredReservation() {
        logger.info("cleaning expired reservations");
        loanDAO.cleanupExpiredReservation(nbDaysReservation);
    }


    @Override
    public int getNbDaysReservation() {
        return nbDaysReservation;
    }

    @Override
    public void fillPendingReservation(){
       List<Loan> pendingLoanList = loanDAO.getAllPendingReservationWithNoBook();
       List<Loan> updatedList = new ArrayList<>();
       if (!pendingLoanList.isEmpty()){
           logger.info("pending list is not empty");
           for (Loan loan: pendingLoanList
                ) {
               getBookIfAvailable(loan);
              updatedList.add(loan);
           }
       }
       logger.info("number of updates: "+updatedList.size());

    }


    @Override
    public String getLoanStatus(int id) {
        Loan loan;
        logger.info("getting loan status");
        Date today = getTodayDate();
        logger.info("today: " + today);
        try {
            loan = loanDAO.getLoanById(id);
            if (loan.getStartDate() == null) {
                logger.info("it's planned");
                return "PLANNED";
            }
            if (loan.getEndDate() != null) {
                return "TERMINATED";
            }
            if (loan.getPlannedEndDate().before(today)) {
                return "OVERDUE";
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
