package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Transactional
@Configuration
@PropertySource("classpath:config.properties")
public class LoanManagerImpl implements LoanManager {
    @Value("${loanDuration}")
    private int loanDuration;
    @Value("${renewDuration}")
    private int renewDuration;
    @Value("${maxBooks}")
    private int maxBooks;

    @Inject
    LoanDAO loanDAO;
    @Inject
    BookManager bookManager;
    @Inject
    MemberManager memberManager;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String exception = "";

    @Override
    public String addLoan(Loan loan) {

        exception = "";
        loan.setStartDate(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(loan.getStartDate());
        cal.add(Calendar.DATE,loanDuration);
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
        return exception;

    }


    @Override
    public List<Loan> getLoans() {
        return loanDAO.getLoans();
    }

    @Override
    public Loan getLoanById(int id) {
        logger.info("getting id (from business): " + id);
        Loan loan = loanDAO.getLoanById(id);
        if (loan != null) {
            logger.info("loan");
            return loan;
        } else {
            logger.info("loan is probably null");
            return null;
        }
    }

    @Override
    public List<Loan> getLoansByCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = new HashMap<>();
        for (HashMap.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("?") && !entry.getValue().equals("") && !entry.getValue().equals("-1")) {
                criterias.put(entry.getKey(), entry.getValue());
            }

        }
        logger.info("map: " + map);
        logger.info("criterias: " + criterias);
        return loanDAO.getLoansByCriterias(criterias);
    }

    @Override
    public String renewLoan(int id) {
        exception = "";
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
        return exception;
    }

    @Override
    public boolean isRenewable(int id) {
        logger.info("checking if loan "+id+" is renewable");
        Loan loan = loanDAO.getLoanById(id);

        if (loan.getEndDate() != null) {
            return false;
        }

        Date start = loan.getStartDate();
        Date end = loan.getPlannedEndDate();

        int diffInDays = (int) ((end.getTime() - start.getTime())
                / (1000 * 60 * 60 * 24));
        logger.info("diff days is: " + diffInDays);
        if (diffInDays > renewDuration) {
            return false;
        } else {
            return true;
        }


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
        Date today = new Date();
        try {
            loan = loanDAO.getLoanById(id);
            if (loan.getEndDate() == null && loan.getPlannedEndDate().before(today)) {
                return "OVERDUE";
            }
            if(loan.getEndDate()!=null){
                return "TERMINATED";
            }
            else{
                return "PROGRESS";
            }
        } catch (NullPointerException e) {
            logger.error("error while getting loan status");
        }
        return null;
    }
}
