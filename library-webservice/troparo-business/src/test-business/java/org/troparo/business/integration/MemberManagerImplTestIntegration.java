package org.troparo.business.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.MemberManager;
import org.troparo.model.Member;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration("classpath:application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional
class MemberManagerImplTestIntegration {


    @Inject
    private MemberManager memberManager;


    @Test
    @DisplayName("should return members from database")
    void getMembers() {
        assertNotNull(memberManager.getMembers());
    }

    @Test
    @DisplayName("should update member")
    void updateMember() {
        String email = "trok.dede@dede.ded";
        String login = "JpoliNo";
        Member member = memberManager.getMemberByLogin(login);
        member.setEmail(email);
        memberManager.updateMember(member);
        assertEquals(email, memberManager.getMemberByLogin(login).getEmail());
    }

    @Test
    @DisplayName("should return invalid login when add member with no login")
    void addMember() {
        Member member = new Member();

        assertEquals("Login must be 5 or 20 characters: null", memberManager.addMember(member));
    }




}
