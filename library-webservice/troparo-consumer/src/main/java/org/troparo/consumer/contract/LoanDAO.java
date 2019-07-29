package org.troparo.consumer.contract;

import org.troparo.model.Book;
import org.troparo.model.Loan;

import java.util.List;
import java.util.Map;


public interface LoanDAO {

    List<Loan> getLoans();

    boolean addLoan(Loan loan);

    boolean updateLoan(Loan loan);

    boolean removeLoan(Loan loan);

    Loan getLoanById(int id);

    List getLoanByIsbn(String isbn);

    List<Loan> getLoanByLogin(String login);

    List<Loan> getLoansByCriteria(Map<String, String> map);

    Loan getPendingReservation(String isbn);

    Book getNextAvailableBook(String isbn);

    boolean cleanupExpiredReservation(int expiration);

    int cleanupExpiredReservationCount(int expiration);

    List<Loan> getAllPendingReservationWithNoBook();

    List<Loan> getLoansReadyForStart();
}