package org.library.business.impl;

import org.apache.log4j.Logger;
import org.library.business.contract.BookManager;
import org.library.business.contract.LoanManager;
import org.troparo.entities.loan.*;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.LoanService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Named
public class LoanManagerImpl implements LoanManager {
    private static final Logger logger = Logger.getLogger(LoanManager.class.toString());




    @Override
    public boolean renewLoan(String token, int id) {
        logger.info("trying to renew: " + id);
        LoanService loanService = new LoanService();
        RenewLoanRequestType renewLoanRequestType = new RenewLoanRequestType();
        renewLoanRequestType.setToken(token);
        renewLoanRequestType.setId(id);
       // RenewLoanResponseType responseType = new RenewLoanResponseType();
        try {
            loanService.getLoanServicePort().renewLoan(renewLoanRequestType);
        } catch (BusinessExceptionLoan businessExceptionLoan) {
            logger.error(businessExceptionLoan.getMessage());
        }

        return false;
    }

    @Override
    public boolean isRenewable(String token, int id) {
        LoanService loanService = new LoanService();
        IsRenewableRequestType requestType = new IsRenewableRequestType();
        requestType.setToken(token);
        requestType.setId(id);
        try {
            IsRenewableResponseType responseType = loanService.getLoanServicePort().isRenewable(requestType);
            return responseType.isReturn();

        } catch (BusinessExceptionLoan businessExceptionLoan) {
            logger.error(businessExceptionLoan.getMessage());
        }
        return false;
    }

    @Override
    public String getStatus(String token, int id) {
        LoanService loanService = new LoanService();
        GetLoanStatusRequestType requestType = new GetLoanStatusRequestType();
        requestType.setToken(token);
        requestType.setId(id);

        try {
            GetLoanStatusResponseType responseType = loanService.getLoanServicePort().getLoanStatus(requestType);
            return responseType.getStatus();
        } catch (BusinessExceptionLoan businessExceptionLoan) {
            logger.error(businessExceptionLoan.getMessage());
        }

        return null;
    }


}
