package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.member.*;
import org.troparo.model.Member;
import org.troparo.services.memberservice.BusinessExceptionMember;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    private MemberServiceImpl memberService;
    private MemberManager memberManager;
    private ConnectServiceImpl connectService;


    @BeforeEach
    void init() {
        memberService = new MemberServiceImpl();
        memberManager = mock(MemberManager.class);
        connectService = mock(ConnectServiceImpl.class);
        memberService.setMemberManager(memberManager);
        memberService.setAuthentication(connectService);
    }

    @Test
    @DisplayName("should return exception if authentication fails")
    void checkAuthentication() throws Exception {
        when(connectService.checkToken("tchok")).thenReturn(false);
        assertThrows(Exception.class, () -> memberService.checkAuthentication(""));
    }

    @Test
    @DisplayName("should not return exception if authentication succeeds")
    void checkAuthentication1() {
        when(connectService.checkToken("tchok")).thenReturn(true);
        assertThrows(Exception.class, () -> memberService.checkAuthentication(""));
    }

    @Test
    @DisplayName("should add member with no exception")
    void addMember() throws BusinessExceptionMember {
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
        assertDoesNotThrow(()-> memberService.addMember(parameters));

    }

    @Test
    @DisplayName("should add member with no exception")
    void addMember1() throws Exception {
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
        assertThrows(BusinessExceptionMember.class, ()->memberService.addMember(parameters));

    }

    @Test
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
        assertThrows(BusinessExceptionMember.class, ()->memberService.updateMember(parameters));
    }

    @Test
    void updateMember1() {
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
        when(memberManager.updateMember(any(Member.class))).thenReturn("");
        assertDoesNotThrow( ()->memberService.updateMember(parameters));
    }

    @Test
    void getMemberById() {
        fail();
    }

    @Test
    void getMemberByLogin() {
        fail();
    }

    @Test
    void getAllMembers() {
        fail();
    }

    @Test
    void getMemberByCriterias() {
        fail();
    }

    @Test
    void removeMember() {
        when(connectService.checkToken("tchok")).thenReturn(true);
        RemoveMemberRequestType parameters = new RemoveMemberRequestType();
        parameters.setToken("tchok");
        parameters.setId(2);
        when(memberManager.remove(anyInt())).thenReturn("");
        assertDoesNotThrow( ()->memberService.removeMember(parameters));
    }

    @Test
    void removeMember1() {
        when(connectService.checkToken("tchok")).thenReturn(true);
        RemoveMemberRequestType parameters = new RemoveMemberRequestType();
        parameters.setToken("tchok");
        parameters.setId(2);
        when(memberManager.remove(anyInt())).thenReturn("dede");
        assertThrows(BusinessExceptionMember.class, ()->memberService.removeMember(parameters));
    }
}