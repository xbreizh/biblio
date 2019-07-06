package org.library.business.impl;


import org.apache.log4j.Logger;
import org.library.business.contract.LoanManager;
import org.library.business.contract.MemberManager;
import org.library.model.Book;
import org.library.model.Loan;
import org.library.model.Member;
import org.troparo.entities.connect.CheckTokenRequestType;
import org.troparo.entities.connect.RequestPasswordResetLinkRequestType;
import org.troparo.entities.connect.RequestPasswordResetLinkResponseType;
import org.troparo.entities.connect.ResetPasswordRequestType;
import org.troparo.entities.member.*;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.memberservice.BusinessExceptionMember;
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
    private static final Logger logger = Logger.getLogger(MemberManagerImpl.class);
    @Inject
    LoanManager loanManager;
    private MemberService memberService;



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
            logger.info("trying to pass loan to member");

            logger.info("member loan size: " + member.getLoanList());
            return member;
        } catch (NullPointerException e) {
            logger.info("Issue while trying to get member details");
        } catch (BusinessExceptionMember businessExceptionMember) {
            logger.error(businessExceptionMember.getMessage());
        } catch (BusinessExceptionLoan businessExceptionLoan) {
            logger.error(businessExceptionLoan.getMessage());
        }

        return null;
    }

    @Override
    public boolean resetPassword(String login, String password, String token) throws BusinessExceptionConnect {

        System.out.println("Manager /d login: "+login+" / password: "+password+" / token: "+token);
        //System.out.println("password has been reset");
        CheckTokenRequestType checkTokenRequestType = new CheckTokenRequestType();
        checkTokenRequestType.setToken(token);
        //if(connectService.getConnectServicePort().checkToken(checkTokenRequestType).isReturn()) {
            ResetPasswordRequestType resetPasswordRequestType = new ResetPasswordRequestType();
            resetPasswordRequestType.setLogin(login);
            resetPasswordRequestType.setPassword(password);
        logger.info("trying to reset");
        System.out.println(connectService);
            return getConnectServicePort().resetPassword(resetPasswordRequestType).isReturn();
       // }
       // return false;
    }

    @Override
    public boolean sendResetPasswordlink(String login, String email) throws BusinessExceptionConnect {
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

    public Member convertMemberTypeOutIntoMember(String token, MemberTypeOut memberTypeOut) throws BusinessExceptionLoan {
        Member member = new Member();
        member.setFirstName(memberTypeOut.getFirstName());
        member.setLastName(memberTypeOut.getLastName());
        member.setLogin(memberTypeOut.getLogin());
        member.setEmail(memberTypeOut.getEmail());
        // converting xml date into Date
        Date date = convertGregorianCalendarIntoDate(memberTypeOut.getDateJoin().toGregorianCalendar());
        member.setDateJoin(date);
        member.setRole(memberTypeOut.getRole());
        member.setLoanList(convertLoanListTypeIntoList(token, memberTypeOut.getLoanListType()));
        logger.info("member from business: " + member);
        return member;
    }

    public List<Loan> convertLoanListTypeIntoList(String token, LoanListType loanListType) throws BusinessExceptionLoan {
        List<Loan> loanList = new ArrayList<>();
        logger.info("trying to convert LoanListType into List<Loan>");
        for (LoanTypeOut loanTypeOut : loanListType.getLoanTypeOut()
        ) {
            if (loanTypeOut.getEndDate() == null) {
                Loan loan = new Loan();
                loan.setId(loanTypeOut.getId());
                Date date = convertGregorianCalendarIntoDate(loanTypeOut.getStartDate().toGregorianCalendar());
                loan.setStartDate(date);
                date = convertGregorianCalendarIntoDate(loanTypeOut.getPlannedEndDate().toGregorianCalendar());
                loan.setPlannedEndDate(date);
                if (loanTypeOut.getEndDate() != null) {
                    date = convertGregorianCalendarIntoDate(loanTypeOut.getEndDate().toGregorianCalendar());
                    loan.setEndDate(date);
                }
                loan.setRenewable(loanManager.isRenewable(token, loan.getId()));
                loan.setStatus(loanManager.getStatus(token, loan.getId()));
                loan.setBook(convertBookTypeOutIntoBook(loanTypeOut.getBookTypeOut()));
                loanList.add(loan);
                logger.info("book added to list: " + loan.getBook().getTitle());
            }
        }

        logger.info("loan list size: " + loanList.size());
        return loanList;
    }

    public Book convertBookTypeOutIntoBook(BookTypeOut bookTypeOut) {
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

    private ConnectService connectService;

    public void setLoanManager(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }


}
