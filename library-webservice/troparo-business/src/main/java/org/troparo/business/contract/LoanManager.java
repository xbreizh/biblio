package org.troparo.business.contract;

import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Book;
import org.troparo.model.Loan;

import java.util.List;
import java.util.Map;


public interface LoanManager {

    String addLoan(String login, int id);

    List<Loan> getLoans();

    Loan getLoanById(int id);

    List<Loan> getLoansByCriteria(Map<String, String> map);

    String renewLoan(int id);

    String cancelLoan(String token, int loanId);

    boolean isRenewable(int id);

    String terminate(int id);

    String getLoanStatus(int id);

    void setLoanDAO(LoanDAO loanDAO);

    String reserve(String token, String isbn);

    boolean checkinBooking( String token, int id);

    boolean checkIfPendingReservation(String isbn);

    void transferBookToPendingLoanIfAny(Book book);

    void cleanupExpiredReservation();
}