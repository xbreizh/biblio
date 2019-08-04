package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.loan.*;
import org.troparo.model.Loan;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;
import org.troparo.web.service.helper.DateConvertedHelper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@WebService(serviceName = "LoanService", endpointInterface = "org.troparo.services.loanservice.ILoanService",
        targetNamespace = "http://troparo.org/services/LoanService/", portName = "LoanServicePort", name = "LoanServiceImpl")
public class LoanServiceImpl implements ILoanService {
    private Logger logger = Logger.getLogger(LoanServiceImpl.class);


    @Inject
    private LoanManager loanManager;
    @Inject
    private BookManager bookManager;
    @Inject
    private MemberManager memberManager;

    @Inject
    private ConnectServiceImpl connectService;
    @Inject
    private DateConvertedHelper dateConvertedHelper;

    void setDateConvertedHelper(DateConvertedHelper dateConvertedHelper) {
        this.dateConvertedHelper = dateConvertedHelper;
    }

    // Create
    @Override
    public AddLoanResponseType addLoan(AddLoanRequestType parameters) throws BusinessExceptionLoan {
        AddLoanResponseType ar = new AddLoanResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn("success");
        logger.info("loanManager: " + loanManager);
        String login = parameters.getLoanTypeIn().getLogin();
        int bookId = parameters.getLoanTypeIn().getBookId();
        ar.setReturn(loanManager.addLoan(login, bookId));

        return ar;
    }


    @Override
    public ReserveResponseType reserve(ReserveRequestType parameters) throws BusinessExceptionLoan {
        ReserveResponseType ar = new ReserveResponseType();
        checkAuthentication(parameters.getToken());
        logger.info("new reserve call. token: "+parameters.getToken()+" / isbn: "+parameters.getISBN());
        ar.setReturn(loanManager.reserve(parameters.getToken(), parameters.getISBN()));
        logger.info("getting reserve return: "+ar.getReturn());
        return ar;
    }

    // Get One
    @Override
    public GetLoanByIdResponseType getLoanById(GetLoanByIdRequestType parameters) throws BusinessExceptionLoan {
        checkAuthentication(parameters.getToken());
        logger.info("new method added");
        GetLoanByIdResponseType rep = new GetLoanByIdResponseType();
        LoanTypeOut loanTypeOut = new LoanTypeOut();
        Loan loan = loanManager.getLoanById(parameters.getId());
        if (loan == null) {
            throw new BusinessExceptionLoan("no loan found with that bookId");
        } else {
            loanTypeOut.setId(loan.getId());
            loanTypeOut.setBookId(loan.getBook().getId());
            loanTypeOut.setLogin(loan.getBorrower().getLogin());

            loanTypeOut.setStartDate(dateConvertedHelper.convertDateIntoXmlDate(loan.getStartDate()));
            loanTypeOut.setPlannedEndDate(dateConvertedHelper.convertDateIntoXmlDate(loan.getPlannedEndDate()));
            if (loan.getEndDate() != null) {
                loanTypeOut.setEndDate(dateConvertedHelper.convertDateIntoXmlDate(loan.getEndDate()));
            } else {
                loanTypeOut.setEndDate(null);
            }
            rep.setLoanTypeOut(loanTypeOut);
        }
        return rep;
    }


    // Get All
    @Override
    public LoanListResponseType getAllLoans(LoanListRequestType parameters) throws BusinessExceptionLoan {
        LoanListType loanListType;
        checkAuthentication(parameters.getToken());
        List<Loan> loanList;
        loanList = loanManager.getLoans();

        logger.info("size list: " + loanList.size());

        LoanListResponseType loanListResponseType = new LoanListResponseType();

        loanListType = convertLoanIntoLoanTypeOut(loanList);
        // add loanType to the movieListType

        loanListResponseType.setLoanListType(loanListType);
        return loanListResponseType;
    }


    // Get List By Criterias
    @Override
    public GetLoanByCriteriasResponseType getLoanByCriteria(GetLoanByCriteriasRequestType parameters) throws BusinessExceptionLoan {
        List<Loan> loanList;
        LoanListType loanListType = new LoanListType();
        GetLoanByCriteriasResponseType responseType = new GetLoanByCriteriasResponseType();

        checkAuthentication(parameters.getToken());
        Map<String, String> map = new HashMap<>();
        if (parameters.getLoanCriterias() == null) {
            responseType.setLoanListType(loanListType);
            return responseType;
        }
        if (parameters.getLoanCriterias().getBookId() == 0 && parameters.getLoanCriterias().getLogin() == null && parameters.getLoanCriterias().getStatus() == null && parameters.getLoanCriterias().getISBN() == null)
            return null;

        if (parameters.getLoanCriterias().getLogin() != null && !parameters.getLoanCriterias().getLogin().equals("") && !parameters.getLoanCriterias().getLogin().equals("?") && !parameters.getLoanCriterias().getLogin().equalsIgnoreCase("null")) {
            map.put("login", parameters.getLoanCriterias().getLogin().toUpperCase());
        }

        if (parameters.getLoanCriterias().getBookId() != -1 && parameters.getLoanCriterias().getBookId() != 0) {
            map.put("book.bookId", Integer.toString(parameters.getLoanCriterias().getBookId()));
        }
        if (parameters.getLoanCriterias().getStatus() != null && !parameters.getLoanCriterias().getStatus().equals("") && !parameters.getLoanCriterias().getStatus().equalsIgnoreCase("null")) {
            map.put("status", parameters.getLoanCriterias().getStatus().toUpperCase());
        }
        if (parameters.getLoanCriterias().getISBN() != null && !parameters.getLoanCriterias().getISBN().equals("")) {
            map.put("isbn", parameters.getLoanCriterias().getISBN().toUpperCase());
        }

        logger.info("map: " + map);
        logger.info(map);

        loanList = loanManager.getLoansByCriteria(map);

        logger.info("loanListType beg: " + loanListType.getLoanTypeOut().size());
        logger.info(loanList);
        if (loanList != null && !loanList.isEmpty()) {
            loanListType = convertLoanIntoLoanTypeOut(loanList);
        }

        logger.info("loanListType end: " + loanListType.getLoanTypeOut().size());
        responseType.setLoanListType(loanListType);
        return responseType;
    }

    @Override
    public GetLoanStatusResponseType getLoanStatus(GetLoanStatusRequestType parameters) throws BusinessExceptionLoan {
        checkAuthentication(parameters.getToken());
        GetLoanStatusResponseType ar = new GetLoanStatusResponseType();
        ar.setStatus(loanManager.getLoanStatus(parameters.getId()));
        return ar;
    }


    @Override
    public IsRenewableResponseType isRenewable(IsRenewableRequestType parameters) throws BusinessExceptionLoan {
        checkAuthentication(parameters.getToken());
        IsRenewableResponseType ar = new IsRenewableResponseType();

        ar.setReturn(loanManager.isRenewable(parameters.getId()));

        return ar;
    }

    @Override
    public CancelLoanResponseType cancelLoan(CancelLoanRequestType parameters) throws BusinessExceptionLoan {
        String token = parameters.getToken();
        checkAuthentication(token);
        CancelLoanResponseType ar = new CancelLoanResponseType();
        String feedback = loanManager.cancelLoan(token, parameters.getId());

        ar.setReturn(feedback);
        return ar;
    }


    @Override
    public RenewLoanResponseType renewLoan(RenewLoanRequestType parameters) throws BusinessExceptionLoan {
        checkAuthentication(parameters.getToken());
        RenewLoanResponseType ar = new RenewLoanResponseType();
        String feedback = loanManager.renewLoan(parameters.getId());

        ar.setReturn(feedback);
        return ar;
    }

    @Override
    public TerminateLoanResponseType terminateLoan(TerminateLoanRequestType parameters) throws BusinessExceptionLoan {
        checkAuthentication(parameters.getToken());
        TerminateLoanResponseType ar = new TerminateLoanResponseType();

        String feedback = loanManager.terminate(parameters.getId());

        ar.setReturn(feedback);
        return ar;
    }

    @Override
    public CheckInLoanResponseType checkInLoan(CheckInLoanRequestType parameters) {
        CheckInLoanResponseType ar = new CheckInLoanResponseType();
        boolean feedback = loanManager.checkinBooking(parameters.getToken(), parameters.getId());
        ar.setReturn(feedback);
        return ar;
    }


    // Delete


    // Converts Loan from Business into output
    private LoanListType convertLoanIntoLoanTypeOut(List<Loan> loanList) {
        LoanTypeOut loanTypeOut;
        LoanListType loanListType = new LoanListType();
        loanListType.getLoanTypeOut().clear();

        for (Loan loan : loanList) {

            // set values retrieved from DAO class
            loanTypeOut = new LoanTypeOut();
            loanTypeOut.setId(loan.getId());
            loanTypeOut.setLogin(loan.getBorrower().getLogin());
            if(loan.getBook()!=null) {
                loanTypeOut.setBookId(loan.getBook().getId());
                loanTypeOut.setISBN(loan.getIsbn());
                LoanBook bookLoan = new LoanBook();
                bookLoan.setAuthor(loan.getBook().getAuthor());
                bookLoan.setTitle(loan.getBook().getTitle());
                bookLoan.setISBN(loan.getBook().getIsbn());
                bookLoan.setEdition(loan.getBook().getEdition());
                bookLoan.setKeywords(loan.getBook().getKeywords());
                bookLoan.setNbPages(loan.getBook().getNbPages());
                bookLoan.setPublicationYear(loan.getBook().getPublicationYear());
                loanTypeOut.setLoanBook(bookLoan);
            }
            XMLGregorianCalendar startDate = dateConvertedHelper.convertDateIntoXmlDate(loan.getStartDate());
            XMLGregorianCalendar plannedEndDate = dateConvertedHelper.convertDateIntoXmlDate(loan.getPlannedEndDate());
            if (loan.getEndDate() != null) {
                XMLGregorianCalendar endDate = dateConvertedHelper.convertDateIntoXmlDate(loan.getEndDate());
                loanTypeOut.setEndDate(endDate);
            } else {
                loanTypeOut.setEndDate(null);
            }

            loanTypeOut.setStartDate(startDate);
            loanTypeOut.setPlannedEndDate(plannedEndDate);


            logger.info("conversion done");

            // converting xml into Date

            loanListType.getLoanTypeOut().add(loanTypeOut);
        }

        logger.info("loanListType end: " + loanListType.getLoanTypeOut().size());
        return loanListType;
    }


    void checkAuthentication(String token) throws BusinessExceptionLoan {

            if(!connectService.checkToken(token)){

                logger.error("Invalid token");
                throw new BusinessExceptionLoan("invalid token");
            }

    }

    void setLoanManager(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    void setBookManager(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    void setConnectService(ConnectServiceImpl connectService) {
        this.connectService = connectService;
    }

}
