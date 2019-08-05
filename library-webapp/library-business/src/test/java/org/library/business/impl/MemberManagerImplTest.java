package org.library.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.business.contract.MemberManager;
import org.troparo.entities.member.GetMemberByLoginRequestType;
import org.troparo.entities.member.GetMemberByLoginResponseType;
import org.troparo.entities.member.MemberTypeOut;
import org.troparo.services.memberservice.BusinessExceptionMember;
import org.troparo.services.memberservice.MemberService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemberManagerImplTest {


    private MemberManager memberManager;

    private MemberService memberService;

    @BeforeEach
    void init(){
        memberManager = new MemberManagerImpl();
        memberService = mock(MemberService.class);
        memberManager.setMemberService(memberService);
    }

    @Test
    void getMember() throws BusinessExceptionMember {

        GetMemberByLoginResponseType responseType = new GetMemberByLoginResponseType();
        MemberTypeOut memberTypeOut = new MemberTypeOut();
        String login = "Bob";
        String email = "xadsds@frrfr.frfr";
        memberTypeOut.setLogin(login);
        memberTypeOut.setEmail(email);
        responseType.setMemberTypeOut(memberTypeOut);
        System.out.println(memberService.getMemberServicePort());
        when(memberService.getMemberServicePort().getMemberByLogin(any(GetMemberByLoginRequestType.class))).thenReturn(responseType);
        assertAll(
                ()-> assertEquals(login, memberManager.getMember("dede", login).getLogin()),
                ()-> assertEquals(email, memberManager.getMember("dede", login).getEmail())
        );

    }

    @Test
    void resetPassword() {
    }

    @Test
    void sendResetPasswordLink() {
    }

    @Test
    void switchReminder() {
    }

    @Test
    void convertMemberTypeOutIntoMember() {
    }

    @Test
    void convertLoanListTypeIntoList() {
    }

    @Test
    void convertBookTypeOutIntoBook() {
    }

    @Test
    void convertGregorianCalendarIntoDate() {
    }

    @Test
    void getConnectService() {
    }

    @Test
    void setConnectService() {
    }

    @Test
    void setLoanManager() {
    }

    @Test
    void setMemberService() {
    }
}