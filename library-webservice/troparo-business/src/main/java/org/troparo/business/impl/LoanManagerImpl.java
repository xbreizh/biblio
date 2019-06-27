package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;


@Configuration
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
    }

    @Override
    public String addLoan(Loan loan) {
        loan.setStartDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(loan.getStartDate());
        cal.add(Calendar.DATE, loanDuration);
        loan.setPlannedEndDate(cal.getTime());
        if (loan.getBorrower() == null) {
            return "invalid member";
        }
        if (loan.getBook() == null) {
            return "invalid book";
        }

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
        return "";

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
    public List<Loan> getLoansByCriterias(Map<String, String> map) {
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
        return loanDAO.getLoansByCriterias(criterias);
    }

    @Override
    public String renewLoan(int id) {
        Loan loan = loanDAO.getLoanById(id);

        if (loan.getEndDate() != null) {
            return "loan already terminated: " + loan.getEndDate();
        }
        Date start = loan.getStartDate();
        Date end = loan.getPlannedEndDate();

        int diffInDays = (int) ((end.getTime() - start.getTime())
                / (1000 * 60 * 60 * 24));
        logger.info("diff days is: " + diffInDays);
        if (diffInDays > loanDuration) {
            return "loan has already been renewed";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(loan.getPlannedEndDate());
            cal.add(Calendar.DATE, renewDuration);
            loan.setPlannedEndDate(cal.getTime());
            loanDAO.updateLoan(loan);
        }
        return "";
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
        return diffInDays < (loanDuration + renewDuration);

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

    public int getLoanDuration() {
        return loanDuration;
    }


    public void setBookManager(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }
}
