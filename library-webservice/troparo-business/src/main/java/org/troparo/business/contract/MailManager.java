package org.troparo.business.contract;

import org.troparo.model.Loan;
import org.troparo.model.Mail;

import java.util.Date;
import java.util.List;


public interface MailManager {

    List<Mail> getOverdueEmailList();


    int calculateDaysBetweenDates(Date d1, Date d2);

    LoanManager getLoanManager();

    void setLoanManager(LoanManager loanManager);

    List<Mail> getPasswordResetList(String token);

    List<Mail> getLoansReadyForStart();

    List<Mail> getLoansReminder();

    Date calculateEndAvailableDate(Loan loan, int nbDays);

}