package org.library.business.contract;


import org.troparo.services.loanservice.BusinessExceptionLoan;

public interface LoanManager {


    boolean renewLoan(String token, int id) throws BusinessExceptionLoan;

    boolean isRenewable(String token, int id) throws BusinessExceptionLoan;

    String getStatus(String token, int id) throws BusinessExceptionLoan;

    //String renew(String token, int bookId, String login) throws BusinessExceptionLoan;

    boolean removeLoan(String token, int id) throws BusinessExceptionLoan;


    //List<Loan> getLoansForIsbn(String token, String isbn);

    String reserve(String token, String isbn);
}
