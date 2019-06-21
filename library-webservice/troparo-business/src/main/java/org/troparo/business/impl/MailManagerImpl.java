package org.troparo.business.impl;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MailManager;
import org.troparo.model.Loan;
import org.troparo.model.Mail;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Named
@Transactional
public class MailManagerImpl implements MailManager {
    @Inject
    LoanManager loanManager;

    private Logger logger = Logger.getLogger(MailManagerImpl.class);

    @Override
    public int calculateDaysBetweenDates(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (int) diff / (24 * 60 * 60 * 1000);
    }




    @Override
    public List<Mail> getOverdueEmailList() {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("status", "OVERDUE");
        logger.info("getting overdue list");
        List<Loan> loans = loanManager.getLoansByCriterias(criterias);

        logger.info("loans: " + loans.size());
        List<Mail> mailList = new ArrayList<>();
        for (Loan loan : loans
        ) {
            Mail mail = new Mail();
            mail.setEmail(loan.getBorrower().getEmail());
            mail.setFirstname(loan.getBorrower().getFirstName());
            mail.setLastname(loan.getBorrower().getLastName());
            mail.setIsbn(loan.getBook().getIsbn());
            mail.setTitle(loan.getBook().getTitle());
            mail.setAuthor(loan.getBook().getAuthor());
            mail.setEdition(loan.getBook().getEdition());
            mail.setDueDate(loan.getPlannedEndDate());
            int overDays = calculateDaysBetweenDates(getTodaySDate(), loan.getPlannedEndDate());
            mail.setDiffdays(overDays);
            mailList.add(mail);
        }
        return mailList;
    }

    Date getTodaySDate() {
        return new Date();
    }

    @Override
    public void setLoanManager(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    @Override
    public LoanManager getLoanManager() {
        return loanManager;
    }

}
