package org.library.business.impl;

import org.apache.log4j.Logger;
import org.library.business.contract.LoanManager;
import org.library.model.Book;
import org.library.model.Loan;
import org.troparo.entities.loan.*;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;
import org.troparo.services.loanservice.LoanService;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class LoanManagerImpl implements LoanManager {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private LoanService loanService;
    private DateConvertedHelper dateConvertedHelper;


    public LoanManagerImpl() {
        this.setDateConvertedHelper(new DateConvertedHelper());
    }

    private void setDateConvertedHelper(DateConvertedHelper dateConvertedHelper) {
        this.dateConvertedHelper = dateConvertedHelper;
    }

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
    public String renew(String token, int bookId, String login) throws BusinessExceptionLoan {
        AddLoanRequestType addLoanRequestType = new AddLoanRequestType();
        addLoanRequestType.setToken(token);
        LoanTypeIn loanTypeIn = new LoanTypeIn();
        loanTypeIn.setLogin(login);
        loanTypeIn.setBookId(bookId);
        loanTypeIn.setLogin(login);
        addLoanRequestType.setLoanTypeIn(loanTypeIn);
        AddLoanResponseType responseType = getLoanServicePort().addLoan(addLoanRequestType);
        return responseType.getReturn();
    }

    @Override
    public List<Loan> getLoansForIsbn(String token, String isbn) {
        GetLoanByCriteriasResponseType responseType;
        logger.info("getting into manager: " + isbn);
        GetLoanByCriteriasRequestType getLoanByCriteriasRequestType = new GetLoanByCriteriasRequestType();
        List<Loan> loans = new ArrayList<>();
        getLoanByCriteriasRequestType.setToken(token);
        LoanCriterias loanCriterias = new LoanCriterias();
        loanCriterias.setISBN(isbn);
        getLoanByCriteriasRequestType.setLoanCriterias(loanCriterias);
        logger.info("getting feedback: " + loanCriterias);
        try {
            responseType = getLoanServicePort().getLoanByCriteria(getLoanByCriteriasRequestType);
            logger.info("size returned: " + responseType.getLoanListType().getLoanTypeOut().size());
            loans = convertLoanByCriteriaIntoLoanList(responseType.getLoanListType());
        } catch (BusinessExceptionLoan businessExceptionLoan) {
            logger.error(businessExceptionLoan.getMessage());
        }
        logger.info("returning loanList");
        return loans;
    }

    private List<Loan> convertLoanByCriteriaIntoLoanList(LoanListType list) {
        List<Loan> loanList = new ArrayList<>();
        logger.info("trying to convert");
        if (list.getLoanTypeOut().isEmpty()) return loanList;

        for (LoanTypeOut loanTypeOut : list.getLoanTypeOut()
        ) {
            Loan loan = new Loan();
            loan.setStartDate(dateConvertedHelper.convertXmlDateIntoDate(loanTypeOut.getStartDate()));
            loan.setPlannedEndDate(dateConvertedHelper.convertXmlDateIntoDate(loanTypeOut.getPlannedEndDate()));
            Book book = convertLoanBookIntoBook(loanTypeOut.getLoanBook());
            loan.setBook(book);
            loan.setIsbn(loanTypeOut.getISBN());
            loanList.add(loan);
        }

        logger.info("converted " + loanList.size() + " items");
        return loanList;
    }


    private Book convertLoanBookIntoBook(LoanBook loanBook) {
        Book book = new Book();
        book.setTitle(loanBook.getTitle());
        book.setAuthor(loanBook.getAuthor());
        book.setIsbn(loanBook.getISBN());
        book.setKeywords(loanBook.getKeywords());
        book.setPublicationYear(loanBook.getPublicationYear());
        book.setEdition(loanBook.getEdition());
        return book;
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
