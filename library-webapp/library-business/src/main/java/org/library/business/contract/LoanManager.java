package org.library.business.contract;


import org.library.model.Loan;
import org.troparo.services.loanservice.BusinessExceptionLoan;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

public interface LoanManager {


    boolean renewLoan(String token, int id) throws BusinessExceptionLoan;

    boolean isRenewable(String token, int id) throws BusinessExceptionLoan;

    String getStatus(String token, int id) throws BusinessExceptionLoan;

    boolean renew(String token, String isbn, String login, XMLGregorianCalendar startDate) throws BusinessExceptionLoan;


    List<Loan> getLoansForIsbn(String token, String isbn);
}
