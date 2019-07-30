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

    void setMemberManager(MemberManager memberManager);

    List<Mail> getPasswordResetList(String token);

    List<Mail> getLoansReadyForStart(String token);

    List<Mail> getLoansReminder(String token);

    Date calculateEndAvailableDate(Loan loan, int nbDays);

}