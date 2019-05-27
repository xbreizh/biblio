package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.EmailManager;
import org.troparo.entities.mail.*;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Mail;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@WebService(serviceName = "MailService", endpointInterface = "org.troparo.services.mailservice.IMailService",
        targetNamespace = "http://troparo.org/services/MailService/", portName = "MailServicePort", name = "MailServiceImpl")
public class MailServiceImpl implements IMailService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private EmailManager mailManager;


    @Inject
    private ConnectServiceImpl authentication;

    private String exception = "";
   /* private List<Mail> mailList = new ArrayList<>();
    private MailTypeOut mailTypeOut = null;
    private MailTypeIn mailTypeIn = null;
    private MailListType mailListType = new MailListType();
    private Mail mail = null;*/

    @Override
    public GetOverdueMailListResponse getOverdueMailList(GetOverdueMailListRequest parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        GetOverdueMailListResponse ar = new GetOverdueMailListResponse();
        List<Mail> mailList = mailManager.getOverdueEmailList();
        MailListType mailListType = convertmailListIntoMailListType(mailList);
        ar.setMailListType(mailListType);
        return ar;
    }



    MailListType convertmailListIntoMailListType(List<Mail> mailList){
        MailListType mlt = new MailListType();


        for (Mail mail: mailList ) {
            MailTypeOut mout = new MailTypeOut();
            mout.setEmail(mail.getEmail());
            mout.setFirstName(mail.getFirstname());
            mout.setLastName(mail.getLastname());
            mout.setDueDate(convertDateIntoXmlDate(mail.getDueDate()));
            mout.setDiffDays(mail.getDiffdays());
            mout.setIsbn(mail.getIsbn());
            mout.setTitle(mail.getTitle());
            mout.setAuthor(mail.getAuthor());
            mout.setEdition(mail.getEdition());
            mlt.getMailTypeOut().add(mout);

        }

        return mlt;
    }



  /*  // Get All
    @Override
    public MailListResponseType getAllMails(MailListRequestType parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        mailList = mailManager.getMails();
        logger.info("size list: " + mailList.size());

        MailListResponseType mailListResponseType = new MailListResponseType();


        convertMailIntoMailTypeOut();
        // add mailType to the movieListType

        mailListResponseType.setMailListType(mailListType);
        return mailListResponseType;
    }

    // Converts Input into Mail for business
    private void convertMailTypeInIntoMail() {
        mail = new Mail();

        mail.setLogin(mailTypeIn.getLogin().toUpperCase());
        mail.setFirstName(mailTypeIn.getFirstName().toUpperCase());
        mail.setLastName(mailTypeIn.getLastName().toUpperCase());
        mail.setPassword(mailTypeIn.getPassword().toUpperCase());
        mail.setEmail(mailTypeIn.getEmail().toUpperCase());
        mail.setRole(mailTypeIn.getRole().toUpperCase());
        logger.info("conversion mailType into mail done");
    }

    // Converts Input into Mail for business
    private void convertMailTypeUpdateIntoMail(MailTypeUpdate mailTypeUpdate) {
        mail = new Mail();
        mail.setLogin(mailTypeUpdate.getLogin().toUpperCase());
        mail.setFirstName(mailTypeUpdate.getFirstName().toUpperCase());
        mail.setPassword(mailTypeUpdate.getPassword().toUpperCase());
        mail.setLastName(mailTypeUpdate.getLastName().toUpperCase());
        mail.setEmail(mailTypeUpdate.getEmail().toUpperCase());
        mail.setRole(mailTypeUpdate.getRole().toUpperCase());
        logger.info("conversion mailTypeUpdate into mail done");
    }

    // Update
    @Override
    public UpdateMailResponseType updateMail(UpdateMailRequestType parameters) throws BusinessExceptionMail {
        UpdateMailResponseType ar = new UpdateMailResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);
        MailTypeUpdate mailTypeUpdate = parameters.getMailTypeUpdate();
        // update
        convertMailTypeUpdateIntoMail(mailTypeUpdate);
        logger.info("mailManager: " + mailManager);
        exception = mailManager.updateMail(mail);
        if (!exception.equals("")) {
            throw new BusinessExceptionMail(exception);
        }

        return ar;
    }


*//*
    @Override
    public CheckTokenResponseType checkToken(CheckTokenRequestType parameters) throws BusinessException {

        CheckTokenResponseType ar = new CheckTokenResponseType();
        boolean tokenIsValid = false;
        tokenIsValid = mailManager.checkToken(parameters.getToken());
        ar.setReturn(tokenIsValid);
        return ar;
    }*//*




    private LoanListType settingLoanListMail(List<Loan> loanList) {
        LoanListType loanListType = new LoanListType();
        *//* List<LoanTypeOut> listOut = new ArrayList<>();*//*
        for (Loan l : loanList
        ) {
            LoanTypeOut lout = new LoanTypeOut();
            lout.setId(l.getId());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(l.getStartDate());
            lout.setStartDate(xmlCalendar);
            xmlCalendar = convertDateIntoXmlDate(l.getPlannedEndDate());
            lout.setPlannedEndDate(xmlCalendar);
            if (l.getEndDate() != null) {
                xmlCalendar = convertDateIntoXmlDate(l.getEndDate());
                lout.setEndDate(xmlCalendar);
            }
            lout.setBookTypeOut(convertBookIntoBookTypeOut(l.getBook()));
            loanListType.getLoanTypeOut().add(lout);
        }
        return loanListType;
    }

    private BookTypeOut convertBookIntoBookTypeOut(Book book) {
        BookTypeOut bookTypeOut = new BookTypeOut();
        bookTypeOut.setId(book.getId());
        bookTypeOut.setISBN(book.getIsbn());
        bookTypeOut.setTitle(book.getTitle());
        bookTypeOut.setAuthor(book.getAuthor());
        bookTypeOut.setEdition(book.getEdition());
        bookTypeOut.setNbPages(book.getNbPages());
        bookTypeOut.setPublicationYear(book.getPublicationYear());
        bookTypeOut.setKeywords(book.getKeywords());
        return bookTypeOut;
    }

    @Override
    public GetMailByLoginResponseType getMailByLogin(GetMailByLoginRequestType parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        logger.info("new method added");
        GetMailByLoginResponseType rep = new GetMailByLoginResponseType();
        MailTypeOut bt = new MailTypeOut();
        Mail mail = mailManager.getMailByLogin(parameters.getLogin().toUpperCase());
        if (mail == null) {
            throw new BusinessExceptionMail("no mail found with that login");
        } else {
            bt.setId(mail.getId());
            bt.setLogin(mail.getLogin());
            bt.setFirstName(mail.getFirstName());
            bt.setLastName(mail.getLastName());
            bt.setEmail(mail.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(mail.getDateJoin());
            bt.setDateJoin(xmlCalendar);

            // getting the loanList
            LoanListType loanListType = new LoanListType();

            bt.setLoanListType(settingLoanListMail(mail.getLoanList()));
            rep.setMailTypeOut(bt);
        }
        return rep;
    }

  *//*  // Get By Login
    @Override
    public GetMailByLoginResponseType getMailByLogin(GetMailByIdRequestType parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        logger.info("new method added");
        GetMailByIdResponseType rep = new GetMailByIdResponseType();
        MailTypeOut bt = new MailTypeOut();
        Mail mail = mailManager.getMailById(parameters.getId());
        if (mail == null) {
            throw new BusinessExceptionMail("no mail found with that id");
        } else {
            bt.setId(mail.getId());
            bt.setLogin(mail.getLogin());
            bt.setFirstName(mail.getFirstName());
            bt.setLastName(mail.getLastName());
            bt.setEmail(mail.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(mail.getDateJoin());
            bt.setDateJoin(xmlCalendar);
            rep.setMailTypeOut(bt);
        }
        return rep;
    }*//*

  *//*  @Override
    public InvalidateTokenResponseType invalidateToken(InvalidateTokenRequestType parameters) throws BusinessException {
        InvalidateTokenResponseType ar = new InvalidateTokenResponseType();
        ar.setReturn(mailManager.invalidateToken(parameters.getToken()));
        return ar;
    }*//*




  *//*  @Override
    public GetTokenResponseType getToken(GetTokenRequestType parameters) throws BusinessException {
        GetTokenResponseType ar = new GetTokenResponseType();
        String token = mailManager.getToken(parameters.getLogin(), parameters.getPassword());
        ar.setReturn(token);
        return ar;
    }*//*


    // Get List By Criterias
    @Override
    public GetMailByCriteriasResponseType getMailByCriterias(GetMailByCriteriasRequestType parameters) throws BusinessExceptionMail {
        HashMap<String, String> map = new HashMap<>();
        checkAuthentication(parameters.getToken());
        MailCriterias criterias = parameters.getMailCriterias();
        map.put("Login", criterias.getLogin().toUpperCase());
        map.put("FirstName", criterias.getFirstName().toUpperCase());
        map.put("LastName", criterias.getLastName().toUpperCase());
        map.put("Email", criterias.getEmail().toUpperCase());
        map.put("role", criterias.getRole().toUpperCase());
        logger.info("map: " + map);

        mailList = mailManager.getMailsByCriterias(map);
        GetMailByCriteriasResponseType brt = new GetMailByCriteriasResponseType();
        logger.info("mailListType beg: " + mailListType.getMailTypeOut().size());

        convertMailIntoMailTypeOut();

        logger.info("mailListType end: " + mailListType.getMailTypeOut().size());
        brt.setMailListType(mailListType);
        return brt;
    }


    // Delete
    @Override
    public RemoveMailResponseType removeMail(RemoveMailRequestType parameters) throws BusinessExceptionMail {
        RemoveMailResponseType ar = new RemoveMailResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);

        logger.info("mailManager: " + mailManager);
        exception = mailManager.remove(parameters.getId());
        if (!exception.equals("")) {
            throw new BusinessExceptionMail(exception);
        }

        return ar;

    }

   *//* @Override
    public ResetPasswordResponseType resetPassword(ResetPasswordRequestType parameters) throws BusinessException {
        ResetPasswordResponseType ar = new ResetPasswordResponseType();
        boolean result;

        logger.info("trying to reset pwd for: " + parameters.getLogin());
        String login = parameters.getLogin();
        String password = parameters.getPassword();
        String email = parameters.getEmail();
        result = mailManager.updatePassword(login, email, password);

        ar.setReturn(result);
        return ar;
    }
*//*

    // Converts Mail from Business into output
    private void convertMailIntoMailTypeOut() {
        mailListType.getMailTypeOut().clear();
        for (Mail mail : mailList) {

            // set values retrieved from DAO class
            mailTypeOut = new MailTypeOut();
            mailTypeOut.setId(mail.getId());
            mailTypeOut.setLogin(mail.getLogin());
            mailTypeOut.setFirstName(mail.getFirstName());
            mailTypeOut.setLastName(mail.getLastName());
            mailTypeOut.setEmail(mail.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(mail.getDateJoin());

            logger.info("new date: " + xmlCalendar);

            // converting xml into Date

          *//*  XMLGregorianCalendar xcal = xmlCalendar;
            java.util.Date dt = xcal.toGregorianCalendar().getTime();*//*


            mailTypeOut.setDateJoin(convertDateIntoXmlDate(mail.getDateJoin()));

            mailListType.getMailTypeOut().add(mailTypeOut);
        }
        logger.info("mailListType end: " + mailListType.getMailTypeOut().size());
    }*/

    private XMLGregorianCalendar convertDateIntoXmlDate(Date date) {
        // converting Date into XML date

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlCalendar;
    }

    private void checkAuthentication(String token) throws BusinessExceptionMail {
        try {
            authentication.checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionMail("invalid token");
        }
    }


}
