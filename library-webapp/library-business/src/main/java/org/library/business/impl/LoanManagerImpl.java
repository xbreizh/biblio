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
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Named
public class LoanManagerImpl implements LoanManager {
    private static final Logger logger = Logger.getLogger(LoanManager.class.toString());
    private LoanService loanService;


    private DateConvertedHelper dateConvertedHelper;

    public LoanManagerImpl() {
        dateConvertedHelper = new DateConvertedHelper();
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
        AddLoanResponseType responseType = getLoanServicePort().addLoan(addLoanRequestType);
        return responseType.isReturn();
    }

    @Override
    public List<Loan> getLoansForIsbn(String token, String isbn) {
        GetLoanByCriteriasResponseType responseType ;
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
            businessExceptionLoan.printStackTrace();
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
            loanList.add(loan);
        }

        logger.info("converted " + loanList.size() + " items");
        return loanList;
    }


    private Book convertLoanBookIntoBook(LoanBook loanBook){
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
    public String[] createArrayFromLoanDates(List<Loan> loanList) {
        String[] dateArray = null;
        if(loanList.isEmpty())return dateArray;
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        for (Loan loan: loanList){
            Date start = loan.getStartDate();
            Date end = loan.getPlannedEndDate();

            Calendar c = Calendar.getInstance();
            c.setTime(loan.getStartDate());
            Date dateToAdd= start;
            while(!format.format(dateToAdd).equals(format.format(end))) {
                dateList.add(format.format(dateToAdd));
                c.add(Calendar.DAY_OF_MONTH, 1);
                dateToAdd = c.getTime();
            }
        }


        
        return dateList.toArray(new String[dateList.size()]);
        
    }
    
}
