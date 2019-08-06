package org.library.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.library.business.contract.BookManager;
import org.library.model.Member;
import org.troparo.entities.connect.RequestPasswordResetLinkRequestType;
import org.troparo.entities.connect.RequestPasswordResetLinkResponseType;
import org.troparo.entities.connect.ResetPasswordRequestType;
import org.troparo.entities.connect.ResetPasswordResponseType;
import org.troparo.entities.member.*;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.memberservice.BusinessExceptionMember;
import org.troparo.services.memberservice.IMemberService;
import org.troparo.services.memberservice.MemberService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemberManagerImplTest {


    private MemberManagerImpl memberManager;

    private LoanManagerImpl loanManager;
    private MemberService memberService;
    private IMemberService iMemberService;
    private ConnectService connectService;
    private IConnectService iConnectService;

    @BeforeEach
    void init() {
        loanManager = mock(LoanManagerImpl.class);
        BookManager bookManager = mock(BookManager.class);
        memberService = mock(MemberService.class);
        iMemberService = mock(IMemberService.class);
        iConnectService = mock(IConnectService.class);
        // memberManager.setMemberService(memberService);
        connectService = mock(ConnectService.class);
        memberManager = new MemberManagerImpl(loanManager, bookManager);
        memberManager.setConnectService(connectService);
        memberManager.setMemberService(memberService);
        //memberManager.setLoanManager(loanManager);
    }

    @Test
    @DisplayName("should return member")
    void getMember() throws BusinessExceptionMember, DatatypeConfigurationException {
        GetMemberByLoginResponseType response = new GetMemberByLoginResponseType();
        MemberTypeOut memberTypeOut = new MemberTypeOut();
        String email = "loki.fr@frfr.eter";
        String firstName = "John";
        String login = "bob";
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        memberTypeOut.setDateJoin(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        Member member = new Member();
        member.setEmail(email);
        memberTypeOut.setEmail(email);
        memberTypeOut.setFirstName(firstName);
        memberTypeOut.setLogin(login);
        LoanListType loanListType = new LoanListType();
        memberTypeOut.setLoanListType(loanListType);
        response.setMemberTypeOut(memberTypeOut);
        when(memberService.getMemberServicePort()).thenReturn(iMemberService);
        when(memberService.getMemberServicePort().getMemberByLogin(any(GetMemberByLoginRequestType.class))).thenReturn(response);

        assertEquals(email, memberManager.getMember("", login).getEmail());
    }

    @Test
    @DisplayName("should return member")
    void getMember1() throws BusinessExceptionMember {
        GetMemberByLoginResponseType response = new GetMemberByLoginResponseType();
        when(memberService.getMemberServicePort()).thenReturn(iMemberService);
        when(memberService.getMemberServicePort().getMemberByLogin(any(GetMemberByLoginRequestType.class))).thenReturn(response);

        assertNull(memberManager.getMember("", "login"));
    }


    @Test
    @DisplayName("should return true when trying to reset password")
    void sendResetPasswordLink() throws BusinessExceptionConnect {
        RequestPasswordResetLinkResponseType response = new RequestPasswordResetLinkResponseType();
        response.setReturn(true);
        when(connectService.getConnectServicePort()).thenReturn(iConnectService);
        when(connectService.getConnectServicePort().requestPasswordResetLink(any(RequestPasswordResetLinkRequestType.class))).thenReturn(response);

        assertTrue(memberManager.sendResetPasswordLink("", "login"));
    }

    @Test
    @DisplayName("should return true when trying to send Password links")
    void resetPassword() throws BusinessExceptionConnect {
        ResetPasswordResponseType response = new ResetPasswordResponseType();
        response.setReturn(true);
        when(connectService.getConnectServicePort()).thenReturn(iConnectService);
        when(connectService.getConnectServicePort().resetPassword(any(ResetPasswordRequestType.class))).thenReturn(response);

        assertTrue(memberManager.resetPassword("", "", "login"));
    }


    @Test
    @DisplayName("should return true if memberService returns true")
    void switchReminder() throws BusinessExceptionMember {
        SwitchReminderRequestType request = new SwitchReminderRequestType();
        String token = "token123";
        String login = "loginash";
        request.setToken(login);
        request.setLogin(login);
        SwitchReminderResponseType response = new SwitchReminderResponseType();
        response.setReturn(true);
        when(memberService.getMemberServicePort()).thenReturn(iMemberService);
        when(memberService.getMemberServicePort().switchReminder(any(SwitchReminderRequestType.class))).thenReturn(response);
        assertTrue(memberManager.switchReminder(token, login, false));
    }

    @Test
    @DisplayName("should return false if memberService returns true")
    void switchReminder1() throws BusinessExceptionMember {
        when(memberService.getMemberServicePort()).thenReturn(iMemberService);
        SwitchReminderRequestType request = new SwitchReminderRequestType();
        String token = "token123";
        String login = "loginash";
        request.setToken(login);
        request.setLogin(login);
        SwitchReminderResponseType response = new SwitchReminderResponseType();
        response.setReturn(false);
        when(memberService.getMemberServicePort().switchReminder(any(SwitchReminderRequestType.class))).thenReturn(response);

        assertFalse(memberManager.switchReminder(token, login, false));

    }


    @Test
    void convertMemberTypeOutIntoMember() throws DatatypeConfigurationException {
        MemberTypeOut memberTypeOut = new MemberTypeOut();
        String firstName = "Maurice";
        String login = "Momo";
        String lastName = "Pologo";
        String email = "dffrfr@frfr.fr";
        String role = "admin";
        memberTypeOut.setFirstName(firstName);
        memberTypeOut.setEmail(email);
        memberTypeOut.setLogin(login);
        memberTypeOut.setLastName(lastName);
        memberTypeOut.setRole(role);
        memberTypeOut.setLoanListType(new LoanListType());
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        memberTypeOut.setDateJoin(xmlGregorianCalendar);
        assertAll(
                () -> assertEquals(email, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getEmail()),
                () -> assertEquals(firstName, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getFirstName()),
                () -> assertEquals(lastName, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getLastName()),
                () -> assertEquals(role, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getRole()),
                () -> assertEquals(login, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getLogin()),
                () -> assertEquals(memberManager.convertGregorianCalendarIntoDate(c), memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getDateJoin())
        );
    }

    @Test
    void convertLoanListTypeIntoList() throws DatatypeConfigurationException, ParseException, BusinessExceptionLoan {
        LoanListType loanListType = new LoanListType();
        LoanTypeOut loanTypeOut = new LoanTypeOut();
        BookTypeOut bookTypeOut = new BookTypeOut();
        String author = "Joricho";
        String title = "le temps des pruneaux";
        String edition = "duchat";
        String isbn = "njahsui2";
        String keywords = "pluie, eau";
        int nbPages = 231;
        int publicationYear = 1982;
        int id = 4;
        bookTypeOut.setAuthor(author);
        bookTypeOut.setTitle(title);
        bookTypeOut.setEdition(edition);
        bookTypeOut.setISBN(isbn);
        bookTypeOut.setKeywords(keywords);
        bookTypeOut.setNbPages(nbPages);
        bookTypeOut.setPublicationYear(publicationYear);
        loanTypeOut.setBookTypeOut(bookTypeOut);
        loanTypeOut.setId(id);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date1 = simpleDateFormat.parse("2018-09-09");
        Date date2 = simpleDateFormat.parse("2019-01-01");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date1);
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        GregorianCalendar c1 = new GregorianCalendar();
        c.setTime(date2);
        XMLGregorianCalendar xmlGregorianCalendar1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c1);
        loanTypeOut.setStartDate(xmlGregorianCalendar);
        loanTypeOut.setPlannedEndDate(xmlGregorianCalendar1);
        loanListType.getLoanTypeOut().add(loanTypeOut);
        when(loanManager.isRenewable("", id)).thenReturn(true);
        when(loanManager.getStatus("", id)).thenReturn("plouf");
        assertAll(
                () -> assertEquals(edition, memberManager.convertLoanListTypeIntoList("", loanListType).get(0).getBook().getEdition())
        );


    }


    @Test
    void convertBookTypeOutIntoBook() {
        BookTypeOut bookTypeOut = new BookTypeOut();
        String author = "Joricho";
        String title = "le temps des pruneaux";
        String edition = "duchat";
        String isbn = "njahsui2";
        String keywords = "pluie, eau";
        int nbPages = 231;
        int publicationYear = 1982;
        bookTypeOut.setAuthor(author);
        bookTypeOut.setTitle(title);
        bookTypeOut.setEdition(edition);
        bookTypeOut.setISBN(isbn);
        bookTypeOut.setKeywords(keywords);
        bookTypeOut.setNbPages(nbPages);
        bookTypeOut.setPublicationYear(publicationYear);

        assertAll(
                () -> assertEquals(title, memberManager.convertBookTypeOutIntoBook(bookTypeOut).getTitle()),
                () -> assertEquals(author, memberManager.convertBookTypeOutIntoBook(bookTypeOut).getAuthor()),
                () -> assertEquals(edition, memberManager.convertBookTypeOutIntoBook(bookTypeOut).getEdition()),
                () -> assertEquals(publicationYear, memberManager.convertBookTypeOutIntoBook(bookTypeOut).getPublicationYear()),
                () -> assertEquals(keywords, memberManager.convertBookTypeOutIntoBook(bookTypeOut).getKeywords()),
                () -> assertEquals(nbPages, memberManager.convertBookTypeOutIntoBook(bookTypeOut).getNbPages()),
                () -> assertEquals(isbn, memberManager.convertBookTypeOutIntoBook(bookTypeOut).getIsbn())

        );
    }

   /* @Test
    void setBookManager(){
        memberManager.setBookManager(bookManager);
        assertEquals(bookManager, memberManager.getBookManager());
    }

    @Test
    void getBookManager(){
        memberManager.setBookManager(bookManager);
        assertEquals(bookManager, memberManager.getBookManager());
    }


    @Test
    void getLoanManager(){
        LoanManagerImpl loanManager1 = new LoanManagerImpl();
        memberManager.setLoanManager(loanManager1);
        assertEquals(loanManager1, memberManager.getLoanManager());
    }
    @Test
    void setLoanManager(){
        LoanManagerImpl loanManager1 = new LoanManagerImpl();
        memberManager.setLoanManager(loanManager1);
        assertEquals(loanManager1, memberManager.getLoanManager());
    }

    @Test
    void getMemberService(){
        MemberService memberService1 = new MemberService();
        memberManager.setMemberService(memberService1);
        assertEquals(memberService1, memberManager.getMemberService());
    }

    @Test
    void setMemberService(){
        MemberService memberService1 = new MemberService();
        memberManager.setMemberService(memberService1);
        assertEquals(memberService1, memberManager.getMemberService());
    }*/


    @Test
    void convertGregorianCalendarIntoDate() throws ParseException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2018-09-09");
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        assertEquals(date, memberManager.convertGregorianCalendarIntoDate(gc));

    }
}
