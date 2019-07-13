package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.consumer.impl.LoanStatus;
import org.troparo.model.Loan;

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


    public int getMaxReserve() {
        return maxReserve;
    }


    private Logger logger = Logger.getLogger(this.getClass().getName());

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
    public String addLoan(Loan loan) {
        if(loan.getStartDate()!=null){
            System.out.println("it's a reservation!!");
            return "reservation";
        }
        return "new Booking";

      /*  loan.setStartDate(getTodayDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(loan.getStartDate());
        cal.add(Calendar.DATE, loanDuration);
        loan.setPlannedEndDate(cal.getTime());
        String x = checkBookAndMemberValidity(loan);
        if (x != null) return x;

        // checks if loan is possible
        if (!bookManager.isAvailable(loan.getBook().getId())) {
            return "book is not available: " + loan.getBook().getId();
        }
        // checks if borrower can borrow
        if (memberManager.getMemberById(loan.getBorrower().getId()).getLoanList().size() < maxBooks) {
            loanDAO.addLoan(loan);
        } else {
            return "max number of books rented reached";
        }
        return "";*/

    }

    @Override
    public String reserve(Loan loan) {
        String x1 = checkReserveLoanDetailsAreValid(loan);
        System.out.println(x1);
        if (x1 != null) return x1;
        if(!(loanDAO.addLoan(loan)))return "Issue while reserving";
        return "";
    }

    String checkReserveLoanDetailsAreValid(Loan loan) {
        String x;
        x = checkBookAndMemberValidity(loan);
        if (!x.isEmpty()) return x;
        x = checkStartDateIsTodayOrFuture(loan);
        if (!x.isEmpty()) return x;
        x = checkIfReserveLimitNotReached(loan);
        if (!x.isEmpty()) return x;
        if(checkIfOverDue(loan))return "There are Overdue Items preventing the reservation";
        return "";
    }

    boolean checkIfOverDue(Loan loan) {
        Map<String, String> map = new HashMap<>();
        map.put("login", loan.getBorrower().getLogin());
        map.put("status", LoanStatus.OVERDUE.toString());
        if (loanDAO.getLoansByCriteria(map).isEmpty())return false;
        return true;
    }

    String checkIfReserveLimitNotReached(Loan loan) {
        Map<String, String> map = new HashMap<>();
        map.put("login", loan.getBorrower().getLogin());
        map.put("status", LoanStatus.RESERVED.toString());
        List<Loan> loanList = loanDAO.getLoansByCriteria(map);
        if (loanList.size() >= maxReserve)
            return "You have already reached the maximum number of reservation: " + maxReserve;
        return "";
    }

    private String checkStartDateIsTodayOrFuture(Loan loan) {
        if (loan.getStartDate() == null) return "You must specify a start Date";
        Calendar startDate = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        startDate.setTime(loan.getStartDate());
        today.setTime(getTodayDate());
        if (
                (startDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)) &&
                        (startDate.get(Calendar.MONTH) < today.get(Calendar.MONTH)) &&
                        (startDate.get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR))) {
            return "the booking date cannot be in the past";
        }
        return "";
    }


    String checkBookAndMemberValidity(Loan loan) {
        if (loan.getBorrower() == null) {
            return "invalid member";
        }
        if (loan.getBook() == null) {
            return "invalid book";
        }
        if (checkIfLoanAlreadyInProgress(loan)) return "loan already in progress";
        return "";
    }

    private boolean checkIfLoanAlreadyInProgress(Loan loan) {
        Map<String, String> map = new HashMap<>();
        map.put("login", loan.getBorrower().getLogin());
        map.put("BOOK_ID", Integer.toString(loan.getBook().getId()));
        if (loanDAO.getLoansByCriteria(map)!=null&&!loanDAO.getLoansByCriteria(map).isEmpty()) {
            return true;
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
        String[] validCriterias = {"LOGIN", "BOOK_ID", "STATUS"};
        List<String> validCriteriasList = Arrays.asList(validCriterias);
        Map<String, String> criterias = new HashMap<>();

        String[] validStatus = {"terminated", "progress", "overdue"};
        List<String> validStatuslist = Arrays.asList(validStatus);

        List<Loan> loanList = new ArrayList<>();

        for (Map.Entry<String, String> entry : map.entrySet()
        ) {

            if (entry.getKey() != null && entry.getValue() != null &&
                    !entry.getValue().equals("?") && !entry.getValue().equals("") && !entry.getValue().equals("-1")
                    && validCriteriasList.contains(entry.getKey().toUpperCase())) {
                if (entry.getKey().equalsIgnoreCase("status") && !validStatuslist.contains(entry.getValue().toLowerCase())) {
                    return loanList;
                }
                criterias.put(entry.getKey(), entry.getValue());

            }

        }


        logger.info("map: " + criterias);
        logger.info("map: " + map);
        logger.info("criterias: " + criterias);
        return loanDAO.getLoansByCriteria(criterias);
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
                loanDAO.updateLoan(loan);
            } else {
                return "loan already terminated";
            }
        } catch (NullPointerException e) {
            return "loan couldn't be terminated!";
        }
        return "";
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
            } else {
                return "PROGRESS";
            }
        } catch (NullPointerException e) {
            logger.error("error while getting loan status");
            return null;
        }
    }

    Date getTodayDate() {
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
