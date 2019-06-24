package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.business.contract.LoanManager;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.loan.*;
import org.troparo.model.Loan;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.loanservice.ILoanService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@Named
@WebService(serviceName = "LoanService", endpointInterface = "org.troparo.services.loanservice.ILoanService",
        targetNamespace = "http://troparo.org/services/LoanService/", portName = "LoanServicePort", name = "LoanServiceImpl")
public class LoanServiceImpl implements ILoanService {
    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Inject
    private LoanManager loanManager;
    @Inject
    private BookManager bookManager;
    @Inject
    private MemberManager memberManager;

    @Inject
    private ConnectServiceImpl authentication;

    private String exception = "";
    private LoanTypeOut loanTypeOut = null;
    private LoanTypeIn loanTypeIn = null;

    private Loan loan = null;

    // Create
    @Override
    public AddLoanResponseType addLoan(AddLoanRequestType parameters) throws BusinessExceptionLoan {
        exception = "";
        AddLoanResponseType ar = new AddLoanResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);
        loanTypeIn = parameters.getLoanTypeIn();
        Loan loan = convertLoanTypeInIntoLoan(loanTypeIn);
        logger.info("loanManager: " + loanManager);
        exception = loanManager.addLoan(loan);
        if (!exception.equals("")) {
            logger.info("exception found: " + exception);
            throw new BusinessExceptionLoan(exception);
        }

        return ar;
    }


    // Update


    // Get One
    @Override
    public GetLoanByIdResponseType getLoanById(GetLoanByIdRequestType parameters) throws BusinessExceptionLoan {
        exception = "";
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
            loanTypeOut.setStartDate(convertDateIntoXmlDate(loan.getStartDate()));
            loanTypeOut.setPlannedEndDate(convertDateIntoXmlDate(loan.getPlannedEndDate()));
            if (loan.getEndDate() != null) {
                loanTypeOut.setEndDate(convertDateIntoXmlDate(loan.getEndDate()));
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
        LoanListType loanListType = new LoanListType();
        checkAuthentication(parameters.getToken());
        List<Loan> loanList = new ArrayList<>();
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
    public GetLoanByCriteriasResponseType getLoanByCriterias(GetLoanByCriteriasRequestType parameters) throws BusinessExceptionLoan {
        List<Loan> loanList = new ArrayList<>();
        LoanListType loanListType = new LoanListType();
        GetLoanByCriteriasResponseType responseType = new GetLoanByCriteriasResponseType();
        //String[] validCriterias = {"borrower.login", "book.bookId", "status"};
        checkAuthentication(parameters.getToken());
        logger.info("troko");
        Map<String, String> map = new HashMap<>();
        if (parameters.getLoanCriterias() == null) {
            responseType.setLoanListType(loanListType);
            return responseType;
        }
        if (parameters.getLoanCriterias().getBookId() == 0 && parameters.getLoanCriterias().getLogin() == null && parameters.getLoanCriterias().getStatus() == null)
            return null;
        //LoanCriterias criterias = parameters.getLoanCriterias();
        //logger.info(parameters.getLoanCriterias());
        if (parameters.getLoanCriterias().getLogin() != null) {
            if (!parameters.getLoanCriterias().getLogin().equals("") || !parameters.getLoanCriterias().getLogin().equals("?")) {
                map.put("login", parameters.getLoanCriterias().getLogin().toUpperCase());
            }
        }
        if (parameters.getLoanCriterias().getBookId() != -1 && parameters.getLoanCriterias().getBookId() != 0) {
            map.put("book.bookId", Integer.toString(parameters.getLoanCriterias().getBookId()));
        }
        if (parameters.getLoanCriterias().getStatus() != null) {
            if (!parameters.getLoanCriterias().getStatus().equals("")) {
                map.put("status", parameters.getLoanCriterias().getStatus().toUpperCase());
            }
        }
        logger.info("map: " + map);
        logger.info(map);

        loanList = loanManager.getLoansByCriterias(map);
        logger.info("stuff");

        logger.info("loanListType beg: " + loanListType.getLoanTypeOut().size());
        logger.info(loanList);
        if (loanList != null) {
            if (loanList.size() > 0) {
                loanListType = convertLoanIntoLoanTypeOut(loanList);
            } /*else {
                logger.info("stuff");
                return null;
            }*/
        }/* else {
            return null;
        }*/
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
    public RenewLoanResponseType renewLoan(RenewLoanRequestType parameters) throws BusinessExceptionLoan {
        checkAuthentication(parameters.getToken());
        RenewLoanResponseType ar = new RenewLoanResponseType();
        String feedback = loanManager.renewLoan(parameters.getId());
       /* if (feedback.equals("")) {
            ar.setReturn(feedback);
        }*/
        ar.setReturn(feedback);
        return ar;
    }

    @Override
    public TerminateLoanResponseType terminateLoan(TerminateLoanRequestType parameters) throws BusinessExceptionLoan {
        checkAuthentication(parameters.getToken());
        TerminateLoanResponseType ar = new TerminateLoanResponseType();
        //ar.setReturn(false);
        String feedback = loanManager.terminate(parameters.getId());
       /* if (feedback.equals("")) {
        }*/
        ar.setReturn(feedback);
        return ar;
    }


    // Delete


    // Converts Loan from Business into output
    private LoanListType convertLoanIntoLoanTypeOut(List<Loan> loanList) {
        LoanListType loanListType = new LoanListType();
        loanListType.getLoanTypeOut().clear();

        for (Loan loan : loanList) {

            // set values retrieved from DAO class
            loanTypeOut = new LoanTypeOut();
            loanTypeOut.setId(loan.getId());
            loanTypeOut.setLogin(loan.getBorrower().getLogin());
            loanTypeOut.setBookId(loan.getBook().getId());
            XMLGregorianCalendar startDate = convertDateIntoXmlDate(loan.getStartDate());
            XMLGregorianCalendar plannedEndDate = convertDateIntoXmlDate(loan.getPlannedEndDate());
            if (loan.getEndDate() != null) {
                XMLGregorianCalendar endDate = convertDateIntoXmlDate(loan.getEndDate());
                loanTypeOut.setEndDate(endDate);
            } else {
                loanTypeOut.setEndDate(null);
            }

            loanTypeOut.setStartDate(startDate);
            loanTypeOut.setPlannedEndDate(plannedEndDate);


            logger.info("conversion done");

            // converting xml into Date

          /*  XMLGregorianCalendar xcal = xmlCalendar;
            java.util.Date dt = xcal.toGregorianCalendar().getTime();*/


            loanListType.getLoanTypeOut().add(loanTypeOut);
        }

        logger.info("loanListType end: " + loanListType.getLoanTypeOut().size());
        return loanListType;
    }


    // Converts Input into Loan for business
    private Loan convertLoanTypeInIntoLoan(LoanTypeIn loanTypeIn) {
        logger.info(loanTypeIn);
        loan = new Loan();
        loan.setBorrower(memberManager.getMemberByLogin(loanTypeIn.getLogin().toUpperCase()));
        loan.setBook(bookManager.getBookById(loanTypeIn.getId()));
        logger.info("conversion loanType into loan done");
        return loan;
    }


    private XMLGregorianCalendar convertDateIntoXmlDate(Date date) {
        // converting Date into XML date

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage());
        }
        return xmlCalendar;
    }

    private void checkAuthentication(String token) throws BusinessExceptionLoan {
        try {
            authentication.checkToken(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BusinessExceptionLoan("invalid token");
        }
    }

    void setLoanManager(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    void setBookManager(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    void setAuthentication(ConnectServiceImpl authentication) {
        this.authentication = authentication;
    }

}
