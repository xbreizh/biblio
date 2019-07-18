package org.library.business.contract;


import org.troparo.services.loanservice.BusinessExceptionLoan;

import javax.xml.datatype.XMLGregorianCalendar;

public interface LoanManager {


    boolean renewLoan(String token, int id) throws BusinessExceptionLoan;

    boolean isRenewable(String token, int id) throws BusinessExceptionLoan;

    String getStatus(String token, int id) throws BusinessExceptionLoan;

    boolean renew(String token, String isbn, String login, XMLGregorianCalendar startDate) throws BusinessExceptionLoan;


}
