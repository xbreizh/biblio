package org.troparo.business.contract;

import org.troparo.model.Loan;
import org.troparo.model.Mail;

import java.util.Date;
import java.util.List;


public interface MailManager {

    List<Mail> getOverdueEmailList();

    //List<Mail> createMailListfromLoans(List<Loan> loans);

    int calculateDaysBetweenDates(Date d1, Date d2);

    void setLoanManager(LoanManager loanManager);

}