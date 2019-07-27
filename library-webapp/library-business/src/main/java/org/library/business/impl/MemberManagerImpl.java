package org.library.business.impl;


import org.apache.log4j.Logger;
import org.library.business.contract.BookManager;
import org.library.business.contract.LoanManager;
import org.library.business.contract.MemberManager;
import org.library.model.Book;
import org.library.model.Loan;
import org.library.model.Member;
import org.troparo.entities.connect.CheckTokenRequestType;
import org.troparo.entities.connect.RequestPasswordResetLinkRequestType;
import org.troparo.entities.connect.ResetPasswordRequestType;
import org.troparo.entities.member.*;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.memberservice.IMemberService;
import org.troparo.services.memberservice.MemberService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Named
public class MemberManagerImpl implements MemberManager {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    @Inject
    LoanManager loanManager;
    private MemberService memberService;
    private ConnectService connectService;
    @Inject
    private BookManager bookManager;

    public MemberManagerImpl() {
        this.memberService = new MemberService();
        this.connectService = new ConnectService();
    }

    @Override
    public Member getMember(String token, String login) {
        MemberTypeOut memberTypeOut;
        logger.info("token: " + token);
        logger.info("login: " + login);
        try {
            GetMemberByLoginRequestType requestType = new GetMemberByLoginRequestType();
            requestType.setToken(token);
            requestType.setLogin(login);

            GetMemberByLoginResponseType responseType = getMemberServicePort().getMemberByLogin(requestType);
            logger.info("response: " + responseType.getMemberTypeOut().getEmail());
            memberTypeOut = responseType.getMemberTypeOut();
            // converting into Member
            Member member = convertMemberTypeOutIntoMember(token, memberTypeOut);
            logger.info("member: " + member);
            logger.info("trying to pass loan to member: "+member.getLoanList().get(0).getIsbn());

            logger.info("member loan size: " + member.getLoanList().size());
            return member;
        } catch (Exception e) {
            logger.error("Issue while trying to get member details");
            logger.error(e.getMessage());

        }

        return null;
    }

    @Override
    public boolean resetPassword(String login, String password, String token) throws BusinessExceptionConnect {

        CheckTokenRequestType checkTokenRequestType = new CheckTokenRequestType();
        checkTokenRequestType.setToken(token);
        ResetPasswordRequestType resetPasswordRequestType = new ResetPasswordRequestType();
        resetPasswordRequestType.setLogin(login);
        resetPasswordRequestType.setPassword(password);
        logger.info("trying to reset");
        return getConnectServicePort().resetPassword(resetPasswordRequestType).isReturn();
    }

    @Override
    public boolean sendResetPasswordLink(String login, String email) throws BusinessExceptionConnect {
        RequestPasswordResetLinkRequestType requestPasswordResetLinkRequestType = new RequestPasswordResetLinkRequestType();
        requestPasswordResetLinkRequestType.setEmail(email);
        requestPasswordResetLinkRequestType.setLogin(login);

        return getConnectServicePort().requestPasswordResetLink(requestPasswordResetLinkRequestType).isReturn();
    }


    private IConnectService getConnectServicePort() {
        return connectService.getConnectServicePort();
    }

    private IMemberService getMemberServicePort() {
        if (memberService == null) memberService = new MemberService();
        return memberService.getMemberServicePort();
    }

    public Member convertMemberTypeOutIntoMember(String token, MemberTypeOut memberTypeOut) throws BusinessExceptionLoan, BusinessExceptionBook {
        Member member = new Member();
        member.setFirstName(memberTypeOut.getFirstName());
        member.setLastName(memberTypeOut.getLastName());
        member.setLogin(memberTypeOut.getLogin());
        member.setEmail(memberTypeOut.getEmail());
        // converting xml date into Date
        Date date = convertGregorianCalendarIntoDate(memberTypeOut.getDateJoin().toGregorianCalendar());
        member.setDateJoin(date);
        member.setRole(memberTypeOut.getRole());
        List<Loan> loans = convertLoanListTypeIntoList(token, memberTypeOut.getLoanListType());
        logger.info("got the loans converted: " + loans.size());
        member.setLoanList(loans);
        logger.info("all infos passed to the member");

        return member;
    }

    public List<Loan> convertLoanListTypeIntoList(String token, LoanListType loanListType) throws BusinessExceptionLoan, BusinessExceptionBook {
        List<Loan> loanList = new ArrayList<>();
        logger.info("trying to convert LoanListType into List<Loan>");
        for (LoanTypeOut loanTypeOut : loanListType.getLoanTypeOut()
        ) {
            if (loanTypeOut.getEndDate() == null) {
                Loan loan = new Loan();
                loan.setId(loanTypeOut.getId());
                logger.info("ISBN get: "+loanTypeOut.getISBN());
                if (loanTypeOut.getStartDate() != null) {
                    logger.info("converting dates: " + loanTypeOut.getStartDate());
                    Date date;
                    date = convertGregorianCalendarIntoDate(loanTypeOut.getStartDate().toGregorianCalendar());
                    logger.info("converted startDate");
                    loan.setStartDate(date);
                    date = convertGregorianCalendarIntoDate(loanTypeOut.getPlannedEndDate().toGregorianCalendar());
                    logger.info("converted plannedEndDate");
                    loan.setPlannedEndDate(date);
                    loan.setRenewable(loanManager.isRenewable(token, loan.getId()));

                }
                logger.info("getting book: " + loanTypeOut.getBookTypeOut());
                if (loanTypeOut.getBookTypeOut() == null) {
                    logger.info("creating new Book");
                    logger.info("isbn: " + loanTypeOut.getISBN());
                    Book book = bookManager.getBookByISBN(token, loanTypeOut.getISBN());
                    loan.setBook(book);
                    logger.info("title: " + book.getTitle());
                } else {
                    loan.setBook(convertBookTypeOutIntoBook(loanTypeOut.getBookTypeOut()));
                }
                loan.setIsbn(loanTypeOut.getISBN());
                logger.info("setting isbn: " + loan.getIsbn());
                loan.setStatus(loanManager.getStatus(token, loan.getId()));
                logger.info("trying to convert Book");
                loanList.add(loan);
                logger.info("loan added to the list");
            }
        }

        logger.info("loan list size: " + loanList.size());
        return loanList;
    }

    public Book convertBookTypeOutIntoBook(BookTypeOut bookTypeOut) {
        if (bookTypeOut == null) return null;
        logger.info("trying to convert book");
        Book book = new Book();
        book.setId(bookTypeOut.getId());
        book.setIsbn(bookTypeOut.getISBN());
        book.setTitle(bookTypeOut.getTitle());
        book.setAuthor(bookTypeOut.getAuthor());
        book.setEdition(bookTypeOut.getEdition());
        book.setPublicationYear(bookTypeOut.getPublicationYear());
        book.setNbPages(bookTypeOut.getNbPages());
        book.setKeywords(bookTypeOut.getKeywords());
        logger.info("book converted: " + book);
        return book;
    }

    public Date convertGregorianCalendarIntoDate(GregorianCalendar gDate) {
        if (gDate != null) {
            XMLGregorianCalendar xmlCalendar;
            try {
                xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gDate);
                logger.info("converting date");
                return xmlCalendar.toGregorianCalendar().getTime();
            } catch (DatatypeConfigurationException e) {
                logger.error(e.getMessage());
            }
        }
        return null;

    }

    public ConnectService getConnectService() {
        return connectService;
    }

    public void setConnectService(ConnectService connectService) {
        this.connectService = connectService;
    }

    public void setLoanManager(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }


}
