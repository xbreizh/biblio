package org.troparo.consumer.contract;

import org.troparo.model.Loan;

import java.util.Map;
import java.util.List;
import java.util.Map;


public interface LoanDAO {

    List<Loan> getLoans();

    boolean addLoan(Loan loan);

    boolean updateLoan(Loan loan);

    Loan getLoanById(int id);

    List<Loan> getLoanByIsbn(String isbn);

    List<Loan> getLoanByLogin(String login);


    List<Loan> getLoansByCriterias(Map<String, String> map);


}