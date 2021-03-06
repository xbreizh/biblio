package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.member.*;
import org.troparo.model.Book;
import org.troparo.model.Loan;
import org.troparo.model.Member;
import org.troparo.services.memberservice.BusinessExceptionMember;
import org.troparo.services.memberservice.IMemberService;
import org.troparo.web.service.helper.DateConvertedHelper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@WebService(serviceName = "MemberService", endpointInterface = "org.troparo.services.memberservice.IMemberService",
        targetNamespace = "http://troparo.org/services/MemberService/", portName = "MemberServicePort", name = "MemberServiceImpl")
public class MemberServiceImpl implements IMemberService {
    private Logger logger = Logger.getLogger(MemberServiceImpl.class);

    @Inject
    private MemberManager memberManager;


    @Inject
    private ConnectServiceImpl authentication;
    @Inject
    private DateConvertedHelper dateConvertedHelper;

    void setDateConvertedHelper(DateConvertedHelper dateConvertedHelper) {
        this.dateConvertedHelper = dateConvertedHelper;
    }

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
        member.setPassword(memberTypeIn.getPassword());
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
        GetMemberByIdResponseType responseType = new GetMemberByIdResponseType();
        MemberTypeOut memberTypeOut = new MemberTypeOut();
        Member member = memberManager.getMemberById(parameters.getId());
        return (GetMemberByIdResponseType) getMemberResponseType(responseType, memberTypeOut, member);
    }

    @Override
    public SwitchReminderResponseType switchReminder(SwitchReminderRequestType parameters) throws BusinessExceptionMember {
        SwitchReminderResponseType responseType = new SwitchReminderResponseType();
        boolean verdict = memberManager.switchReminder(parameters.getToken(), parameters.getLogin());
        responseType.setReturn(verdict);
        logger.info("reminder activation: "+verdict);
        return responseType;
    }

    LoanListType convertingListOfLoansIntoLoanListMember(List<Loan> loanList) {
        LoanListType loanListType = new LoanListType();

        for (Loan l : loanList
        ) {
            LoanTypeOut loanTypeOut = new LoanTypeOut();
            loanTypeOut.setId(l.getId());
            XMLGregorianCalendar xmlCalendar = dateConvertedHelper.convertDateIntoXmlDate(l.getStartDate());
            loanTypeOut.setStartDate(xmlCalendar);
            xmlCalendar = dateConvertedHelper.convertDateIntoXmlDate(l.getPlannedEndDate());
            loanTypeOut.setPlannedEndDate(xmlCalendar);
            if (l.getEndDate() != null) {
                xmlCalendar = dateConvertedHelper.convertDateIntoXmlDate(l.getEndDate());
                loanTypeOut.setEndDate(xmlCalendar);
            }
            loanTypeOut.setBookTypeOut(convertBookIntoBookTypeOut(l.getBook()));
            loanTypeOut.setISBN(l.getIsbn());
            loanListType.getLoanTypeOut().add(loanTypeOut);
        }
        return loanListType;
    }

    BookTypeOut convertBookIntoBookTypeOut(Book book) {
        if (book == null) return null;
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
        GetMemberByLoginResponseType responseType = new GetMemberByLoginResponseType();
        MemberTypeOut bt = new MemberTypeOut();
        Member member = memberManager.getMemberByLogin(parameters.getLogin().toUpperCase());
        return (GetMemberByLoginResponseType) getMemberResponseType(responseType, bt, member);
    }

    private Object getMemberResponseType(Object response, MemberTypeOut memberTypeOut, Member member) throws BusinessExceptionMember {

        if (member == null) {
            throw new BusinessExceptionMember("no member found");
        } else {
            memberTypeOut.setId(member.getId());
            memberTypeOut.setLogin(member.getLogin());
            memberTypeOut.setFirstName(member.getFirstName());
            memberTypeOut.setLastName(member.getLastName());
            memberTypeOut.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = dateConvertedHelper.convertDateIntoXmlDate(member.getDateJoin());
            memberTypeOut.setDateJoin(xmlCalendar);
            memberTypeOut.setReminder(member.isReminder());

            // getting the loanList

            memberTypeOut.setLoanListType(convertingListOfLoansIntoLoanListMember(member.getLoanList()));
            if (response.getClass() == GetMemberByLoginResponseType.class) {
                GetMemberByLoginResponseType responseType = new GetMemberByLoginResponseType();
                responseType.setMemberTypeOut(memberTypeOut);
                return responseType;
            } else {
                GetMemberByIdResponseType responseType = new GetMemberByIdResponseType();
                responseType.setMemberTypeOut(memberTypeOut);
                return responseType;
            }
        }
    }


    // Get All
    @Override
    public MemberListResponseType getAllMembers(MemberListRequestType parameters) throws BusinessExceptionMember {
        MemberListType memberListType;
        List<Member> memberList;
        checkAuthentication(parameters.getToken());
        memberList = memberManager.getMembers();
        logger.info("size list: " + memberList.size());

        MemberListResponseType memberListResponseType = new MemberListResponseType();


        memberListType = convertMemberIntoMemberTypeOut(memberList);
        // add memberType to the movieListType

        memberListResponseType.setMemberListType(memberListType);
        return memberListResponseType;
    }




    // Get List By Criterias
    @Override
    public GetMemberByCriteriasResponseType getMemberByCriterias(GetMemberByCriteriasRequestType parameters) throws BusinessExceptionMember {
        MemberListType memberListType = new MemberListType();
        List<Member> memberList;
        checkAuthentication(parameters.getToken());
        MemberCriterias criteria = parameters.getMemberCriterias();
        Map<String, String> newMap = cleanCriteriaMap(criteria);


        logger.info("after: " + newMap.size());
        memberList = memberManager.getMembersByCriteria(newMap);
        GetMemberByCriteriasResponseType getMemberByCriteriasResponseType = new GetMemberByCriteriasResponseType();
        logger.info("memberListType beg: " + memberListType.getMemberTypeOut().size());

        memberListType = convertMemberIntoMemberTypeOut(memberList);

        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
        getMemberByCriteriasResponseType.setMemberListType(memberListType);
        return getMemberByCriteriasResponseType;
    }

    private Map<String, String> cleanCriteriaMap(MemberCriterias criteria) {
        Map<String, String> map = new HashMap<>();
        String[][] criteriaList = {
                {"login", criteria.getLogin()},
                {"firstName", criteria.getFirstName()},
                {"lastName", criteria.getLastName()},
                {"role", criteria.getRole()},
                {"email", criteria.getEmail()}};

        for (String[] s : criteriaList
        ) {
            if (s[0] != null && !s[0].equalsIgnoreCase("") || !s[0].equalsIgnoreCase("?")) {
                map.put(s[0], s[1]);
            }
        }
        return map;

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
    MemberListType convertMemberIntoMemberTypeOut(List<Member> memberList) {
        MemberListType memberListType = new MemberListType();
        MemberTypeOut memberTypeOut;
        for (Member member : memberList) {

            // set values retrieved from DAO class
            memberTypeOut = new MemberTypeOut();
            memberTypeOut.setId(member.getId());
            memberTypeOut.setLogin(member.getLogin());
            memberTypeOut.setFirstName(member.getFirstName());
            memberTypeOut.setLastName(member.getLastName());
            memberTypeOut.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = dateConvertedHelper.convertDateIntoXmlDate(member.getDateJoin());

            logger.info("new date: " + xmlCalendar);

            // converting xml into Date

            memberTypeOut.setDateJoin(dateConvertedHelper.convertDateIntoXmlDate(member.getDateJoin()));

            memberListType.getMemberTypeOut().add(memberTypeOut);
        }
        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
        return memberListType;
    }


    void checkAuthentication(String token) throws BusinessExceptionMember {
        logger.info("tok tok token");

        if (token == null || !authentication.checkToken(token)) {
            logger.info("bam exception");
            throw new BusinessExceptionMember("invalid token");
        }

    }


    void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    void setAuthentication(ConnectServiceImpl authentication) {
        this.authentication = authentication;
    }
}
