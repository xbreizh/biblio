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
import javax.inject.Named;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@Named
@WebService(serviceName = "MemberService", endpointInterface = "org.troparo.services.memberservice.IMemberService",
        targetNamespace = "http://troparo.org/services/MemberService/", portName = "MemberServicePort", name = "MemberServiceImpl")
public class MemberServiceImpl implements IMemberService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private MemberManager memberManager;


    @Inject
    private ConnectServiceImpl authentication;


    private List<Member> memberList = new ArrayList<>();


    private MemberListType memberListType = new MemberListType();

    // Create
    @Override
    public AddMemberResponseType addMember(AddMemberRequestType parameters) throws BusinessExceptionMember {
        logger.info("parameters: " + parameters.getMemberTypeIn());
        MemberTypeIn memberTypeIn;
        Member member;
        String exception;
        AddMemberResponseType ar = new AddMemberResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);
        memberTypeIn = parameters.getMemberTypeIn();
        member = convertMemberTypeInIntoMember(memberTypeIn);
        exception = memberManager.addMember(member);
        logger.info("exception: ");
        if (!exception.equals("")) {
            logger.info(exception);
            logger.info("exx: " + exception);
            throw new BusinessExceptionMember(exception);
        }

        return ar;
    }

    // Converts Input into Member for business
    private Member convertMemberTypeInIntoMember(MemberTypeIn memberTypeIn) {
        Member member = new Member();
        logger.info(memberTypeIn);
        member.setLogin(memberTypeIn.getLogin().toUpperCase());
        member.setFirstName(memberTypeIn.getFirstName().toUpperCase());
        member.setLastName(memberTypeIn.getLastName().toUpperCase());
        member.setPassword(memberTypeIn.getPassword().toUpperCase());
        member.setEmail(memberTypeIn.getEmail().toUpperCase());
        member.setRole(memberTypeIn.getRole().toUpperCase());
        logger.info("conversion memberType into member done");
        return member;
    }

    // Converts Input into Member for business
    private void convertMemberTypeUpdateIntoMember(MemberTypeUpdate memberTypeUpdate) {
        Member member = new Member();
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
        String exception;
        Member member = new Member();
        UpdateMemberResponseType ar = new UpdateMemberResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);
        MemberTypeUpdate memberTypeUpdate = parameters.getMemberTypeUpdate();
        // update
        convertMemberTypeUpdateIntoMember(memberTypeUpdate);

        exception = memberManager.updateMember(member);
        if (!exception.equals("")) {
            throw new BusinessExceptionMember(exception);
        }

        return ar;
    }


    // Get By Id
    @Override
    public GetMemberByIdResponseType getMemberById(GetMemberByIdRequestType parameters) throws BusinessExceptionMember {
        checkAuthentication(parameters.getToken());
        logger.info("new method added");
        GetMemberByIdResponseType rep = new GetMemberByIdResponseType();
        MemberTypeOut bt = new MemberTypeOut();
        Member member = memberManager.getMemberById(parameters.getId());
        if (member == null) {
            throw new BusinessExceptionMember("no member found with that bookId");
        } else {
            bt.setId(member.getId());
            bt.setLogin(member.getLogin());
            bt.setFirstName(member.getFirstName());
            bt.setLastName(member.getLastName());
            bt.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(member.getDateJoin());
            bt.setDateJoin(xmlCalendar);


            bt.setLoanListType(settingLoanListMember(member.getLoanList()));
            rep.setMemberTypeOut(bt);

        }
        return rep;
    }

    private LoanListType settingLoanListMember(List<Loan> loanList) {
        LoanListType loanListType = new LoanListType();

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


    // Get List By Criterias
    @Override
    public GetMemberByCriteriasResponseType getMemberByCriterias(GetMemberByCriteriasRequestType parameters) throws BusinessExceptionMember {

        checkAuthentication(parameters.getToken());
        MemberCriterias criterias = parameters.getMemberCriterias();
        Map<String, String> newMap = cleanCriteriasMap(criterias);


        logger.info("after: " + newMap.size());
        memberList = memberManager.getMembersByCriterias(newMap);
        GetMemberByCriteriasResponseType getMemberByCriteriasResponseType = new GetMemberByCriteriasResponseType();
        logger.info("memberListType beg: " + memberListType.getMemberTypeOut().size());

        convertMemberIntoMemberTypeOut();

        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
        getMemberByCriteriasResponseType.setMemberListType(memberListType);
        return getMemberByCriteriasResponseType;
    }

    private Map<String, String> cleanCriteriasMap(MemberCriterias criterias) {
        Map<String, String> map = new HashMap<>();
        if (criterias.getLogin() != null) map.put("Login", criterias.getLogin().toUpperCase());
        if (criterias.getFirstName() != null) map.put("FirstName", criterias.getFirstName().toUpperCase());
        if (criterias.getLastName() != null) map.put("LastName", criterias.getLastName().toUpperCase());
        if (criterias.getEmail() != null) map.put("Email", criterias.getEmail().toUpperCase());
        if (criterias.getRole() != null) map.put("role", criterias.getRole().toUpperCase());
        logger.info("map: " + map);

        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("") && !entry.getValue().equals("?")) {
                newMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return newMap;
    }


    // Delete
    @Override
    public RemoveMemberResponseType removeMember(RemoveMemberRequestType parameters) throws BusinessExceptionMember {
        String exception;
        RemoveMemberResponseType ar = new RemoveMemberResponseType();
        checkAuthentication(parameters.getToken());
        ar.setReturn(true);

        exception = memberManager.remove(parameters.getId());
        if (!exception.equals("")) {
            throw new BusinessExceptionMember(exception);
        }

        return ar;

    }


    // Converts Member from Business into output
    private void convertMemberIntoMemberTypeOut() {
        MemberTypeOut memberTypeOut;
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

            memberTypeOut.setDateJoin(convertDateIntoXmlDate(member.getDateJoin()));

            memberListType.getMemberTypeOut().add(memberTypeOut);
        }
        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
    }

    private XMLGregorianCalendar convertDateIntoXmlDate(Date date) {
        // converting Date into XML date
        if (date == null) return null;
        GregorianCalendar cal = new GregorianCalendar();
        logger.info(date);
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage());
        }
        return xmlCalendar;
    }

    void checkAuthentication(String token) throws BusinessExceptionMember {
        logger.info("tok tok token");

        if (token == null || !authentication.checkToken(token)) {
            logger.info("bam exception");
            throw new BusinessExceptionMember("invalid token");
        }

    }


    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    void setAuthentication(ConnectServiceImpl authentication) {
        this.authentication = authentication;
    }
}
