package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.member.*;
import org.troparo.model.Member;
import org.troparo.services.memberservice.BusinessExceptionMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    void addMember1()  {
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
    @DisplayName("should throw exception when getting member by Id")
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
        HashMap<String, String> map = new HashMap<>();
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
       /* HashMap<String, String> map = new HashMap<>();
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
}