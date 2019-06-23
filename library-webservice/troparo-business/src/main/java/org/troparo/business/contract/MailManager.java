package org.troparo.business.contract;

import org.troparo.business.impl.LoanManagerImpl;
import org.troparo.model.Mail;

import java.util.Date;
import java.util.List;


public interface MailManager {

    List<Mail> getOverdueEmailList();


    int calculateDaysBetweenDates(Date d1, Date d2);

    LoanManager getLoanManager();

    void setLoanManager(LoanManager loanManager);
}