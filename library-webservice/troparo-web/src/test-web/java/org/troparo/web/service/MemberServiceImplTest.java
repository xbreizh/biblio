package org.troparo.web.service;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.member.*;
import org.troparo.model.Book;
import org.troparo.model.Member;
import org.troparo.services.memberservice.BusinessExceptionMember;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    private MemberServiceImpl memberService;

    @Mock
    private MemberManager memberManager;
    @Mock
    private ConnectServiceImpl connectService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @BeforeEach
    void init() {
        memberService = new MemberServiceImpl();
        memberService.setMemberManager(memberManager);
        memberService.setAuthentication(connectService);
        //when(connectService.checkToken(anyString())).thenReturn(true);
    }

    @Test
    @DisplayName("should return exception if authentication fails")
    void checkAuthentication() {
        when(connectService.checkToken("tchok")).thenReturn(false);
        assertThrows(Exception.class, () -> memberService.checkAuthentication(""));
    }

    @Test
    @DisplayName("should not return exception if authentication succeeds")
    void checkAuthentication1() {
        when(connectService.checkToken(anyString())).thenReturn(true);
        assertDoesNotThrow(() -> memberService.checkAuthentication(""));
    }


    @Test
    @DisplayName("should return exception if token null")
    void checkAuthentication2() {
        assertThrows(BusinessExceptionMember.class, () -> memberService.checkAuthentication(null));
    }


    @Test
    @DisplayName("should add member with no exception")
    void addMember() {
        when(connectService.checkToken("tchok")).thenReturn(true);
        AddMemberRequestType parameters = new AddMemberRequestType();
        parameters.setToken("tchok");
        MemberTypeIn memberTypeIn = new MemberTypeIn();
        memberTypeIn.setRole("admin");
        memberTypeIn.setLogin("topki");
        memberTypeIn.setFirstName("John");
        memberTypeIn.setLastName("Bonham");
        memberTypeIn.setEmail("dsdsd@dede.fr");
        memberTypeIn.setPassword("123dd");
        parameters.setMemberTypeIn(memberTypeIn);
        when(memberManager.addMember(any(Member.class))).thenReturn("");
        assertDoesNotThrow(() -> memberService.addMember(parameters));

    }

    @Test
    @DisplayName("should add member with no exception")
    void addMember1() {
        when(connectService.checkToken("tchok")).thenReturn(true);
        AddMemberRequestType parameters = new AddMemberRequestType();
        parameters.setToken("tchok");
        MemberTypeIn memberTypeIn = new MemberTypeIn();
        memberTypeIn.setRole("admin");
        memberTypeIn.setLogin("topki");
        memberTypeIn.setFirstName("John");
        memberTypeIn.setLastName("Bonham");
        memberTypeIn.setEmail("dsdsd@dede.fr");
        memberTypeIn.setPassword("123dd");
        parameters.setMemberTypeIn(memberTypeIn);
        when(memberManager.addMember(any(Member.class))).thenReturn("pas bon");
        assertThrows(BusinessExceptionMember.class, () -> memberService.addMember(parameters));

    }

    @Test
    @DisplayName("should throw an exception when trying to update member")
    void updateMember() {
        when(connectService.checkToken("tchok")).thenReturn(true);
        UpdateMemberRequestType parameters = new UpdateMemberRequestType();
        parameters.setToken("tchok");
        MemberTypeUpdate memberTypeIn = new MemberTypeUpdate();
        memberTypeIn.setRole("admin");
        memberTypeIn.setLogin("topki");
        memberTypeIn.setFirstName("John");
        memberTypeIn.setLastName("Bonham");
        memberTypeIn.setEmail("dsdsd@dede.fr");
        memberTypeIn.setPassword("123dd");
        parameters.setMemberTypeUpdate(memberTypeIn);
        when(memberManager.updateMember(any(Member.class))).thenReturn("pas bon");
        assertThrows(BusinessExceptionMember.class, () -> memberService.updateMember(parameters));
    }

    @Test
    @DisplayName("should not throw an exception when trying to update member")
    void updateMember1() {
        when(connectService.checkToken(anyString())).thenReturn(true);
        UpdateMemberRequestType parameters = new UpdateMemberRequestType();
        parameters.setToken("tchok");
        MemberTypeUpdate memberTypeIn = new MemberTypeUpdate();
        memberTypeIn.setRole("admin");
        memberTypeIn.setLogin("topki");
        memberTypeIn.setFirstName("John");
        memberTypeIn.setLastName("Bonham");
        memberTypeIn.setEmail("dsdsd@dede.fr");
        memberTypeIn.setPassword("123dd");
        parameters.setMemberTypeUpdate(memberTypeIn);
        when(memberManager.updateMember(any(Member.class))).thenReturn("");
        assertDoesNotThrow(() -> memberService.updateMember(parameters));
    }

    @Test
    @DisplayName("should not throw exception when getting member by Id")
    void getMemberById() {
        when(connectService.checkToken(anyString())).thenReturn(true);
        GetMemberByIdRequestType parameters = new GetMemberByIdRequestType();
        parameters.setToken("fr");
        parameters.setId(3);
        when(memberManager.getMemberById(anyInt())).thenReturn(new Member());
        assertDoesNotThrow(() -> memberService.getMemberById(parameters));
    }

    @Test
    @DisplayName("should return member when getting member by Id")
    void getMemberById1() throws BusinessExceptionMember {
        when(connectService.checkToken(anyString())).thenReturn(true);
        GetMemberByIdRequestType parameters = new GetMemberByIdRequestType();
        parameters.setToken("fr");
        parameters.setId(3);
        Member member = new Member();
        String firstname = "Mauluo";
        member.setFirstName(firstname);
        when(memberManager.getMemberById(anyInt())).thenReturn(member);
        //assertDoesNotThrow(() -> memberService.getMemberById(parameters));
        assertEquals(firstname, memberService.getMemberById(parameters).getMemberTypeOut().getFirstName());
    }


    @Test
    @DisplayName("should return exception when getting member by Id")
    void getMemberById2(){
        when(connectService.checkToken(anyString())).thenReturn(true);
        GetMemberByIdRequestType parameters = new GetMemberByIdRequestType();
        parameters.setToken("fr");
        parameters.setId(3);
        when(memberManager.getMemberById(anyInt())).thenReturn(null);
        //assertDoesNotThrow(() -> memberService.getMemberById(parameters));
        assertThrows(BusinessExceptionMember.class, ()-> memberService.getMemberById(parameters));
    }

    @Test
    @DisplayName("should return member")
    void getMemberByLogin() throws BusinessExceptionMember {
        when(connectService.checkToken(anyString())).thenReturn(true);
        GetMemberByLoginRequestType parameters = new GetMemberByLoginRequestType();
        parameters.setToken("de");
        String login = "LOKIO";
        parameters.setLogin(login);
        Member member = new Member();
        String firstname = "molkolo";
        member.setFirstName(firstname);
        member.setLogin(login);
        when(memberManager.getMemberByLogin(login)).thenReturn(member);
        assertEquals(firstname, memberService.getMemberByLogin(parameters).getMemberTypeOut().getFirstName());


    }

    @Test
    @DisplayName("should return exception when getting member by Login")
    void getMemberByLogin1(){
        when(connectService.checkToken(anyString())).thenReturn(true);
        GetMemberByLoginRequestType parameters = new GetMemberByLoginRequestType();
        parameters.setToken("fr");
        parameters.setLogin("Caren");
        when(memberManager.getMemberByLogin(anyString())).thenReturn(null);
        assertThrows(BusinessExceptionMember.class, ()-> memberService.getMemberByLogin(parameters));
    }

    @Test
    @DisplayName("should convertBookIntoBookTypeOut")
    void convertBookIntoBookTypeOut(){
        Book book = new Book();
        int id = 3;
        String isbn = "isbn123";
        String title = "title";
        String author = "author";
        String edition = "edition";
        int nbPages = 132;
        int publicationYear = 1980;
        String keywords = "";
        book.setId(id);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setEdition(edition);
        book.setNbPages(nbPages);
        book.setPublicationYear(publicationYear);
        book.setKeywords(keywords);
        assertAll(
                ()-> assertEquals(id, memberService.convertBookIntoBookTypeOut(book).getId()),
                ()-> assertEquals(isbn, memberService.convertBookIntoBookTypeOut(book).getISBN()),
                ()-> assertEquals(title, memberService.convertBookIntoBookTypeOut(book).getTitle()),
                ()-> assertEquals(author, memberService.convertBookIntoBookTypeOut(book).getAuthor()),
                ()-> assertEquals(edition, memberService.convertBookIntoBookTypeOut(book).getEdition()),
                ()-> assertEquals(nbPages, memberService.convertBookIntoBookTypeOut(book).getNbPages()),
                ()-> assertEquals(publicationYear, memberService.convertBookIntoBookTypeOut(book).getPublicationYear()),
                ()-> assertEquals(keywords, memberService.convertBookIntoBookTypeOut(book).getKeywords())
        );


    }

    @Test
    @DisplayName("should not throw exception when getting all members")
    void getAllMembers() {
        when(connectService.checkToken(anyString())).thenReturn(true);
        MemberListRequestType parameters = new MemberListRequestType();
        parameters.setToken("boh");
        List<Member> list = new ArrayList<>();
        when(memberManager.getMembers()).thenReturn(list);
        assertDoesNotThrow(() -> memberService.getAllMembers(parameters));
    }

    @Test
    @DisplayName("should not throw exception when getting member by Id")
    void getMemberByCriterias() {
        when(connectService.checkToken(anyString())).thenReturn(true);
        GetMemberByCriteriasRequestType parameters = new GetMemberByCriteriasRequestType();
        parameters.setToken("tchok");
        MemberCriterias memberCriterias = new MemberCriterias();
        memberCriterias.setLogin("bobb");
        parameters.setMemberCriterias(memberCriterias);
        Map<String, String> map = new HashMap<>();
        map.put("Login", "BOBB");
        logger.info(map.size());
        List<Member> list = new ArrayList<>();
        Member member = new Member();
        list.add(member);
        when(memberManager.getMembersByCriterias(map)).thenReturn(list);
        assertDoesNotThrow(() -> memberService.getMemberByCriterias(parameters));
    }

    @Test
    @DisplayName("should throw an exception when getting members by Criterias")
    void getMemberByCriterias1() {
        //when(connectService.checkToken(anyString())).thenReturn(true);
        GetMemberByCriteriasRequestType parameters = new GetMemberByCriteriasRequestType();/*
        parameters.setToken("de");*/
        /*MemberCriterias memberCriterias = new MemberCriterias();
        //memberCriterias.setLogin("bobb");
        parameters.setMemberCriterias(memberCriterias);*/
       /* Map<String, String> map = new HashMap<>();
        map.put("login", "bobb");*/
        /*logger.info(map.size());
        List<Member> list = new ArrayList<>();
        Member member = new Member();
        list.add(member);*/
        // when(memberManager.getMembersByCriterias(map)).thenReturn(list);
        assertThrows(BusinessExceptionMember.class, () -> memberService.getMemberByCriterias(parameters));
    }

    @Test
    @DisplayName("shouldn't throw exception when removing member")
    void removeMember() {
        when(connectService.checkToken(anyString())).thenReturn(true);
        RemoveMemberRequestType parameters = new RemoveMemberRequestType();
        parameters.setToken("tchok");
        parameters.setId(2);
        when(memberManager.remove(anyInt())).thenReturn("");
        assertDoesNotThrow(() -> memberService.removeMember(parameters));
    }

    @Test
    @DisplayName("should throw exception when removing member")
    void removeMember1() {
        when(connectService.checkToken(anyString())).thenReturn(true);
        RemoveMemberRequestType parameters = new RemoveMemberRequestType();
        parameters.setToken("tchok");
        parameters.setId(2);
        when(memberManager.remove(anyInt())).thenReturn("dede");
        assertThrows(BusinessExceptionMember.class, () -> memberService.removeMember(parameters));
    }

    @Test
    @DisplayName("should return null if date null")
    void convertDateIntoXmlDate(){

        assertNull(memberService.convertDateIntoXmlDate(null));
    }

    @Test
    @DisplayName("should convert date")
    void convertDateIntoXmlDate1() throws DatatypeConfigurationException, ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("2018-09-09 12:02:48");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        assertEquals("2018-09-09T12:02:48.000Z", memberService.convertDateIntoXmlDate(date).toString());
    }
}