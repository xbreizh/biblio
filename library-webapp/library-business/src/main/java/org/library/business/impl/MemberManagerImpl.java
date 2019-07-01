package org.library.business.impl;


import org.apache.log4j.Logger;
import org.library.business.contract.LoanManager;
import org.library.business.contract.MemberManager;
import org.library.model.Book;
import org.library.model.Loan;
import org.library.model.Member;
import org.troparo.entities.member.*;
import org.troparo.services.memberservice.BusinessExceptionMember;
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


    @Override
    public Member getMember(String token, String login) {
        MemberTypeOut memberTypeOut;
        logger.info("token: " + token);
        logger.info("login: " + login);
        try {
            MemberService memberService = new MemberService();
            GetMemberByLoginRequestType requestType = new GetMemberByLoginRequestType();
            requestType.setToken(token);
            requestType.setLogin(login);

            GetMemberByLoginResponseType responseType = memberService.getMemberServicePort().getMemberByLogin(requestType);
            logger.info("response: " + responseType.getMemberTypeOut().getEmail());
            memberTypeOut = responseType.getMemberTypeOut();

            // converting into Member
            Member member = convertMemberTypeOutIntoMember(token, memberTypeOut);
            logger.info("trying to pass loan to member");

            logger.info("member loan size: " + member.getLoanList());
            logger.info("loan list for that member: " + memberTypeOut.getLoanListType().getLoanTypeOut().get(0).getBookTypeOut().getTitle());
            return member;
        } catch (NullPointerException e) {
            logger.info("Issue while trying to get member details");
        } catch (BusinessExceptionMember businessExceptionMember) {
            logger.error(businessExceptionMember.getMessage());
        }

        return null;
    }

    Member convertMemberTypeOutIntoMember(String token, MemberTypeOut memberTypeOut) {
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

    List<Loan> convertLoanListTypeIntoList(String token, LoanListType loanListType) {
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

    Book convertBookTypeOutIntoBook(BookTypeOut bookTypeOut) {
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

    Date convertGregorianCalendarIntoDate(GregorianCalendar gDate) {
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

}
