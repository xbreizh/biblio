package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.member.*;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;
import org.troparo.services.memberservice.BusinessExceptionMember;
import org.troparo.services.memberservice.IMemberService;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@WebService(serviceName = "MemberService", endpointInterface = "org.troparo.services.memberservice.IMemberService",
        targetNamespace = "http://troparo.org/services/MemberService/", portName = "MemberServicePort", name = "MemberServiceImpl")
public class MemberServiceImpl implements IMemberService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private MemberManager memberManager;


    @Inject
    private ConnectServiceImpl authentication;

    private String exception = "";
    private List<Member> memberList = new ArrayList<>();
    private MemberTypeOut memberTypeOut = null;
    private MemberTypeIn memberTypeIn = null;
    private MemberListType memberListType = new MemberListType();
    private Member member = null;

    // Create
    @Override
    public AddMemberResponseType addMember(AddMemberRequestType parameters) throws BusinessExceptionMember {
        AddMemberResponseType ar = new AddMemberResponseType();
        /*checkAuthentication(parameters.getToken());*/
        ar.setReturn(true);
        memberTypeIn = parameters.getMemberTypeIn();
        convertMemberTypeInIntoMember();
        logger.info("memberManager: " + memberManager);
        exception = memberManager.addMember(member);
        if (!exception.equals("")) {
            logger.info(exception);
            throw new BusinessExceptionMember(exception);
        }

        return ar;
    }

    // Converts Input into Member for business
    private void convertMemberTypeInIntoMember() {
        member = new Member();

        member.setLogin(memberTypeIn.getLogin().toUpperCase());
        member.setFirstName(memberTypeIn.getFirstName().toUpperCase());
        member.setLastName(memberTypeIn.getLastName().toUpperCase());
        member.setPassword(memberTypeIn.getPassword().toUpperCase());
        member.setEmail(memberTypeIn.getEmail().toUpperCase());
        member.setRole(memberTypeIn.getRole().toUpperCase());
        logger.info("conversion memberType into member done");
    }

    // Converts Input into Member for business
    private void convertMemberTypeUpdateIntoMember(MemberTypeUpdate memberTypeUpdate) {
        member = new Member();
        member.setLogin(memberTypeUpdate.getLogin().toUpperCase());
        member.setFirstName(memberTypeUpdate.getFirstName().toUpperCase());
        member.setPassword(memberTypeUpdate.getPassword().toUpperCase());
        member.setLastName(memberTypeUpdate.getLastName().toUpperCase());
        member.setEmail(memberTypeUpdate.getEmail().toUpperCase());
        member.setRole(memberTypeUpdate.getRole().toUpperCase());
        logger.info("conversion memberTypeUpdate into member done");
    }

    // Update
    @Override
    public UpdateMemberResponseType updateMember(UpdateMemberRequestType parameters) throws BusinessExceptionMember {
        UpdateMemberResponseType ar = new UpdateMemberResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);
        MemberTypeUpdate memberTypeUpdate = parameters.getMemberTypeUpdate();
        // update
        convertMemberTypeUpdateIntoMember(memberTypeUpdate);
        logger.info("memberManager: " + memberManager);
        exception = memberManager.updateMember(member);
        if (!exception.equals("")) {
            throw new BusinessExceptionMember(exception);
        }

        return ar;
    }


/*
    @Override
    public CheckTokenResponseType checkToken(CheckTokenRequestType parameters) throws BusinessException {

        CheckTokenResponseType ar = new CheckTokenResponseType();
        boolean tokenIsValid = false;
        tokenIsValid = memberManager.checkToken(parameters.getToken());
        ar.setReturn(tokenIsValid);
        return ar;
    }*/


    // Get By Id
    @Override
    public GetMemberByIdResponseType getMemberById(GetMemberByIdRequestType parameters) throws BusinessExceptionMember {
        checkAuthentication(parameters.getToken());
        logger.info("new method added");
        GetMemberByIdResponseType rep = new GetMemberByIdResponseType();
        MemberTypeOut bt = new MemberTypeOut();
        Member member = memberManager.getMemberById(parameters.getId());
        if (member == null) {
            throw new BusinessExceptionMember("no member found with that id");
        } else {
            bt.setId(member.getId());
            bt.setLogin(member.getLogin());
            bt.setFirstName(member.getFirstName());
            bt.setLastName(member.getLastName());
            bt.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(member.getDateJoin());
            bt.setDateJoin(xmlCalendar);
            LoanListType loanListType = new LoanListType();


            bt.setLoanListType(settingLoanListMember(member.getLoanList()));
            rep.setMemberTypeOut(bt);

        }
        return rep;
    }

    private LoanListType settingLoanListMember(List<Loan> loanList) {
        LoanListType loanListType = new LoanListType();
        /* List<LoanTypeOut> listOut = new ArrayList<>();*/
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
    public GetMemberByLoginResponseType getMemberByLogin(GetMemberByLoginRequestType parameters) throws BusinessExceptionMember {
        checkAuthentication(parameters.getToken());
        logger.info("new method added");
        GetMemberByLoginResponseType rep = new GetMemberByLoginResponseType();
        MemberTypeOut bt = new MemberTypeOut();
        Member member = memberManager.getMemberByLogin(parameters.getLogin().toUpperCase());
        if (member == null) {
            throw new BusinessExceptionMember("no member found with that login");
        } else {
            bt.setId(member.getId());
            bt.setLogin(member.getLogin());
            bt.setFirstName(member.getFirstName());
            bt.setLastName(member.getLastName());
            bt.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(member.getDateJoin());
            bt.setDateJoin(xmlCalendar);

            // getting the loanList
            LoanListType loanListType = new LoanListType();

            bt.setLoanListType(settingLoanListMember(member.getLoanList()));
            rep.setMemberTypeOut(bt);
        }
        return rep;
    }

  /*  // Get By Login
    @Override
    public GetMemberByLoginResponseType getMemberByLogin(GetMemberByIdRequestType parameters) throws BusinessExceptionMember {
        checkAuthentication(parameters.getToken());
        logger.info("new method added");
        GetMemberByIdResponseType rep = new GetMemberByIdResponseType();
        MemberTypeOut bt = new MemberTypeOut();
        Member member = memberManager.getMemberById(parameters.getId());
        if (member == null) {
            throw new BusinessExceptionMember("no member found with that id");
        } else {
            bt.setId(member.getId());
            bt.setLogin(member.getLogin());
            bt.setFirstName(member.getFirstName());
            bt.setLastName(member.getLastName());
            bt.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(member.getDateJoin());
            bt.setDateJoin(xmlCalendar);
            rep.setMemberTypeOut(bt);
        }
        return rep;
    }*/

  /*  @Override
    public InvalidateTokenResponseType invalidateToken(InvalidateTokenRequestType parameters) throws BusinessException {
        InvalidateTokenResponseType ar = new InvalidateTokenResponseType();
        ar.setReturn(memberManager.invalidateToken(parameters.getToken()));
        return ar;
    }*/


    // Get All
    @Override
    public MemberListResponseType getAllMembers(MemberListRequestType parameters) throws BusinessExceptionMember {
        checkAuthentication(parameters.getToken());
        memberList = memberManager.getMembers();
        logger.info("size list: " + memberList.size());

        MemberListResponseType memberListResponseType = new MemberListResponseType();


        convertMemberIntoMemberTypeOut();
        // add memberType to the movieListType

        memberListResponseType.setMemberListType(memberListType);
        return memberListResponseType;
    }

  /*  @Override
    public GetTokenResponseType getToken(GetTokenRequestType parameters) throws BusinessException {
        GetTokenResponseType ar = new GetTokenResponseType();
        String token = memberManager.getToken(parameters.getLogin(), parameters.getPassword());
        ar.setReturn(token);
        return ar;
    }*/


    // Get List By Criterias
    @Override
    public GetMemberByCriteriasResponseType getMemberByCriterias(GetMemberByCriteriasRequestType parameters) throws BusinessExceptionMember {
        HashMap<String, String> map = new HashMap<>();
        checkAuthentication(parameters.getToken());
        MemberCriterias criterias = parameters.getMemberCriterias();
        map.put("Login", criterias.getLogin().toUpperCase());
        map.put("FirstName", criterias.getFirstName().toUpperCase());
        map.put("LastName", criterias.getLastName().toUpperCase());
        map.put("Email", criterias.getEmail().toUpperCase());
        map.put("role", criterias.getRole().toUpperCase());
        logger.info("map: " + map);

        memberList = memberManager.getMembersByCriterias(map);
        GetMemberByCriteriasResponseType brt = new GetMemberByCriteriasResponseType();
        logger.info("memberListType beg: " + memberListType.getMemberTypeOut().size());

        convertMemberIntoMemberTypeOut();

        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
        brt.setMemberListType(memberListType);
        return brt;
    }


    // Delete
    @Override
    public RemoveMemberResponseType removeMember(RemoveMemberRequestType parameters) throws BusinessExceptionMember {
        RemoveMemberResponseType ar = new RemoveMemberResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);

        logger.info("memberManager: " + memberManager);
        exception = memberManager.remove(parameters.getId());
        if (!exception.equals("")) {
            throw new BusinessExceptionMember(exception);
        }

        return ar;

    }

   /* @Override
    public ResetPasswordResponseType resetPassword(ResetPasswordRequestType parameters) throws BusinessException {
        ResetPasswordResponseType ar = new ResetPasswordResponseType();
        boolean result;

        logger.info("trying to reset pwd for: " + parameters.getLogin());
        String login = parameters.getLogin();
        String password = parameters.getPassword();
        String email = parameters.getEmail();
        result = memberManager.updatePassword(login, email, password);

        ar.setReturn(result);
        return ar;
    }
*/

    // Converts Member from Business into output
    private void convertMemberIntoMemberTypeOut() {
        memberListType.getMemberTypeOut().clear();
        for (Member member : memberList) {

            // set values retrieved from DAO class
            memberTypeOut = new MemberTypeOut();
            memberTypeOut.setId(member.getId());
            memberTypeOut.setLogin(member.getLogin());
            memberTypeOut.setFirstName(member.getFirstName());
            memberTypeOut.setLastName(member.getLastName());
            memberTypeOut.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(member.getDateJoin());

            logger.info("new date: " + xmlCalendar);

            // converting xml into Date

          /*  XMLGregorianCalendar xcal = xmlCalendar;
            java.util.Date dt = xcal.toGregorianCalendar().getTime();*/


            memberTypeOut.setDateJoin(convertDateIntoXmlDate(member.getDateJoin()));

            memberListType.getMemberTypeOut().add(memberTypeOut);
        }
        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
    }

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

    private void checkAuthentication(String token) throws BusinessExceptionMember {
        try {
            authentication.checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionMember("invalid token");
        }
    }

}
