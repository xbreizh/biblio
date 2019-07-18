package org.library.business.impl;

import org.apache.log4j.Logger;
import org.library.business.contract.LoanManager;
import org.library.model.Loan;
import org.troparo.entities.book.BookCriterias;
import org.troparo.entities.book.BookListType;
import org.troparo.entities.book.GetBookByCriteriasRequestType;
import org.troparo.entities.book.GetBookByCriteriasResponseType;
import org.troparo.entities.loan.*;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;
import org.troparo.services.loanservice.LoanService;

import javax.inject.Named;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
public class LoanManagerImpl implements LoanManager {
    private static final Logger logger = Logger.getLogger(LoanManager.class.toString());
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
        GetLoanStatusRequestType requestType = new GetLoanStatusRequestType();
        requestType.setToken(token);
        requestType.setId(id);
        return getLoanServicePort().getLoanStatus(requestType).getStatus();
    }

    @Override
    public boolean renew(String token, String isbn, String login, XMLGregorianCalendar startDate) throws BusinessExceptionLoan {
        AddLoanRequestType addLoanRequestType = new AddLoanRequestType();
        addLoanRequestType.setToken(token);
        LoanTypeIn loanTypeIn = new LoanTypeIn();
        loanTypeIn.setISBN(isbn);
        loanTypeIn.setLogin(login);
        loanTypeIn.setStartDate(startDate);
        addLoanRequestType.setLoanTypeIn(loanTypeIn);
        AddLoanResponseType responseType =  getLoanServicePort().addLoan(addLoanRequestType);
        return responseType.isReturn();
    }

    @Override
    public List<Loan> getLoansForIsbn(String token, String isbn) {
        GetLoanByCriteriasResponseType responseType = new GetLoanByCriteriasResponseType();
        GetLoanByCriteriasRequestType getLoanByCriteriasRequestType = new GetLoanByCriteriasRequestType();
        List<Loan> loans = new ArrayList<>();
        getLoanByCriteriasRequestType.setToken(token);
        LoanCriterias loanCriterias = new LoanCriterias();
        /*loanCriterias.getBookIsbn
        getBookByCriteriasRequestType.setBookCriterias(bookCriteria);*/
        return loans;
    }

  /*  private List<Loan> convertBookByCriteriaIntoBookList(LoanListType loanListType) {
        List<Loan> loanList = new ArrayList<>();
        if (loanListType.getLoanTypeOut().isEmpty()) return loanList;

        for (LoanTypeOut loanTypeOut: loanListType.getLoanTypeOut()
             ) {
            Loan loan = new Loan();
            loan.setStartDate(loanTypeOut.);
        }
    }*/


}
