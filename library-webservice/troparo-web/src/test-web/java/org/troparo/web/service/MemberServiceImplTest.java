package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.troparo.business.contract.MemberManager;
import org.troparo.services.memberservice.BusinessExceptionMember;
import org.troparo.services.memberservice.MemberService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemberServiceImplTest {

    private MemberServiceImpl memberService;
    private MemberManager memberManager;
    private ConnectServiceImpl connectService;


    @BeforeEach
        void init(){
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
        assertThrows(BusinessExceptionMember.class,()-> memberService.checkAuthentication("tchok"));
    }

    @Test
    void addMember() throws Exception {
        when(connectService.checkToken(anyString())).thenReturn(false);

    }

    @Test
    void updateMember() {
        fail();
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
        fail();
    }
}