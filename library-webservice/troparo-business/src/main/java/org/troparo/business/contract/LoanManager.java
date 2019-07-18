package org.troparo.business.contract;

import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import java.util.List;
import java.util.Map;


public interface LoanManager {

    String addLoan(Loan loan);

    List<Loan> getLoans();

    Loan getLoanById(int id);

    List<Loan> getLoansByCriteria(Map<String, String> map);

    String renewLoan(int id);

    boolean isRenewable(int id);

    String terminate(int id);

    String getLoanStatus(int id);

    void setLoanDAO(LoanDAO loanDAO);

    String reserve(Loan loan);

    void checkBooking(Loan loan);
}