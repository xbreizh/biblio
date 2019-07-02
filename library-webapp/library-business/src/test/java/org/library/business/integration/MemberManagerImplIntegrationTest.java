package org.library.business.integration;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.impl.ConnectManagerImpl;
import org.library.business.impl.LoanManagerImpl;
import org.library.business.impl.MemberManagerImpl;
import org.library.model.Loan;
import org.library.model.Member;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.loan.GetLoanStatusRequestType;
import org.troparo.entities.member.*;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.services.memberservice.BusinessExceptionMember;
import org.troparo.services.memberservice.IMemberService;
import org.troparo.services.memberservice.MemberService;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MemberManagerImpl.class, LoanManagerImpl.class})
class MemberManagerImplIntegrationTest {


    private MemberManagerImpl memberManager;

    private LoanManagerImpl loanManager;
    private MemberService memberService;

    @BeforeEach
    void init(){
        memberManager = spy(MemberManagerImpl.class);
        loanManager = mock(LoanManagerImpl.class);
        memberService = mock(MemberService.class);
        memberManager.setMemberService(memberService);
        memberManager.setLoanManager(loanManager);
    }

    @Test
    @DisplayName("should return member")
    void getMember() throws BusinessExceptionMember, BusinessExceptionLoan, DatatypeConfigurationException {
        GetMemberByLoginResponseType getMemberByLoginResponseType = new GetMemberByLoginResponseType();
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
        getMemberByLoginResponseType.setMemberTypeOut(memberTypeOut);
        IMemberService iMemberService = mock(IMemberService.class);
        when(memberService.getMemberServicePort()).thenReturn(iMemberService);
        when(memberService.getMemberServicePort().getMemberByLogin(any(GetMemberByLoginRequestType.class))).thenReturn(getMemberByLoginResponseType);
        when(memberManager.convertMemberTypeOutIntoMember("", memberTypeOut)).thenReturn(member);
        assertEquals(email, memberManager.getMember("", login).getEmail());
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
                ()-> assertEquals(email, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getEmail()),
                ()-> assertEquals(firstName, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getFirstName()),
                ()-> assertEquals(lastName, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getLastName()),
                ()-> assertEquals(role, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getRole()),
                ()-> assertEquals(login, memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getLogin()),
                ()-> assertEquals(memberManager.convertGregorianCalendarIntoDate(c), memberManager.convertMemberTypeOutIntoMember("", memberTypeOut).getDateJoin())
        );
    }

    @Test
    void convertLoanListTypeIntoList() throws DatatypeConfigurationException, ParseException, BusinessExceptionLoan {
        LoanListType loanListType = new LoanListType();
        LoanTypeOut loanTypeOut = new LoanTypeOut();
        BookTypeOut bookTypeOut = new BookTypeOut();
        String author= "Joricho";
        String title= "le temps des pruneaux";
        String edition= "duchat";
        String isbn= "njahsui2";
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
        System.out.println(loanListType.getLoanTypeOut().get(0).getBookTypeOut().getEdition());
        when(loanManager.isRenewable("", id)).thenReturn(true);
        when(loanManager.getStatus("", id)).thenReturn("plouf");
        assertAll(
                ()-> assertEquals(edition, memberManager.convertLoanListTypeIntoList("", loanListType).get(0).getBook().getEdition())
        );


    }

    @Test
    void convertBookTypeOutIntoBook() {
        BookTypeOut bookTypeOut = new BookTypeOut();
        String author= "Joricho";
        String title= "le temps des pruneaux";
        String edition= "duchat";
        String isbn= "njahsui2";
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
                ()-> assertEquals(title, memberManager.convertBookTypeOutIntoBook( bookTypeOut).getTitle()),
                ()-> assertEquals(author, memberManager.convertBookTypeOutIntoBook( bookTypeOut).getAuthor()),
                ()-> assertEquals(edition, memberManager.convertBookTypeOutIntoBook( bookTypeOut).getEdition()),
                ()-> assertEquals(publicationYear, memberManager.convertBookTypeOutIntoBook( bookTypeOut).getPublicationYear()),
                ()-> assertEquals(keywords, memberManager.convertBookTypeOutIntoBook( bookTypeOut).getKeywords()),
                ()-> assertEquals(nbPages, memberManager.convertBookTypeOutIntoBook( bookTypeOut).getNbPages()),
                ()-> assertEquals(isbn, memberManager.convertBookTypeOutIntoBook( bookTypeOut).getIsbn())

        );
    }

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