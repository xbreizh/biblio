package org.troparo.business.contract;

import org.troparo.model.Mail;
import org.troparo.model.Member;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;


public interface MailManager {

    List<Mail> getOverdueEmailList();


    int calculateDaysBetweenDates(Date d1, Date d2);

    LoanManager getLoanManager();

    void setLoanManager(LoanManager loanManager);

    List<Mail> getPasswordResetList(String token);

}