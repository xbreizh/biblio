package org.troparo.consumer.contract;

import org.troparo.model.Loan;

import java.util.HashMap;
import java.util.List;


public interface LoanDAO {

    List<Loan> getLoans();

    boolean addLoan(Loan loan);

    boolean updateLoan(Loan loan);

    Loan getLoanById(int id);

    List<Loan> getLoanByIsbn(String isbn);

    List<Loan> getLoanByLogin(String login);


    List<Loan> getLoansByCriterias(HashMap<String, String> map);


}