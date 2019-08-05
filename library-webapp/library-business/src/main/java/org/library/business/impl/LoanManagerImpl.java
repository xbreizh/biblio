package org.library.business.impl;

import org.apache.log4j.Logger;
import org.library.business.contract.LoanManager;
import org.troparo.entities.loan.*;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;
import org.troparo.services.loanservice.LoanService;

import javax.inject.Named;

@Named
public class LoanManagerImpl implements LoanManager {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private LoanService loanService;


    void setLoanService(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public boolean renewLoan(String token, int id) throws BusinessExceptionLoan {
        logger.info("trying to renew: " + id);
        RenewLoanRequestType renewLoanRequestType = new RenewLoanRequestType();
        renewLoanRequestType.setToken(token);
        renewLoanRequestType.setId(id);
        RenewLoanResponseType renewLoanResponseType;
        renewLoanResponseType = getLoanServicePort().renewLoan(renewLoanRequestType);
        return renewLoanResponseType.getReturn().isEmpty();

    }

    @Override
    public boolean removeLoan(String token, int id) throws BusinessExceptionLoan {
        logger.info("trying to renew: " + id);
        CancelLoanRequestType cancelLoanRequestType = new CancelLoanRequestType();
        cancelLoanRequestType.setToken(token);
        cancelLoanRequestType.setId(id);
        CancelLoanResponseType removeLoanResponseType;
        removeLoanResponseType = getLoanServicePort().cancelLoan(cancelLoanRequestType);
        return removeLoanResponseType.getReturn().isEmpty();

    }


    ILoanService getLoanServicePort() {
        if (loanService == null) loanService = new LoanService();
        return loanService.getLoanServicePort();
    }

    @Override
    public boolean isRenewable(String token, int id) throws BusinessExceptionLoan {
        IsRenewableRequestType requestType = new IsRenewableRequestType();
        requestType.setToken(token);
        requestType.setId(id);
        IsRenewableResponseType responseType = getLoanServicePort().isRenewable(requestType);
        return responseType.isReturn();
    }

    @Override
    public String getStatus(String token, int id) throws BusinessExceptionLoan {
        logger.info("getting loan status");
        GetLoanStatusRequestType requestType = new GetLoanStatusRequestType();
        requestType.setToken(token);
        requestType.setId(id);
        return getLoanServicePort().getLoanStatus(requestType).getStatus();
    }


    @Override
    public String reserve(String token, String isbn) {
        logger.info("inside reserve: " + isbn);
        ReserveRequestType requestType = new ReserveRequestType();
        requestType.setToken(token);
        requestType.setISBN(isbn);

        ReserveResponseType responseType;
        try {
            responseType = getLoanServicePort().reserve(requestType);
            logger.info("request type set");
            return responseType.getReturn();
        } catch (BusinessExceptionLoan businessExceptionLoan) {
            logger.error(businessExceptionLoan.getMessage());
        }
        return "Issue while reserving";
    }

}
