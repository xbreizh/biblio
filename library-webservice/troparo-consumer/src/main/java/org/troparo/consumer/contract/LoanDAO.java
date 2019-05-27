package org.troparo.consumer.contract;

import org.troparo.model.Loan;

import java.util.HashMap;
import java.util.List;


public interface LoanDAO {

    List<Loan> getLoans();

    boolean addLoan(Loan loan);

    boolean updateLoan(Loan loan);

    Loan getLoanById(int id);

    Loan getLoanByIsbn(String isbn);

    Loan getLoanByLogin(String login);


    List<Loan> getLoansByCriterias(HashMap<String, String> map);


    /*boolean renew(int id);

    boolean terminate(Loan loan);*/

}