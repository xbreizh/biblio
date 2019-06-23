package org.troparo.business.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.MemberManagerImpl;
import org.troparo.business.impl.validator.StringValidator;
import org.troparo.model.Member;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:application-context-test.xml")
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

        assertEquals("Login must be between 5 or 10 characters: null", memberManager.addMember(member));
    }

    @Test
    @DisplayName("should return an empty string if member valid")
    void checkValidityOfParametersForMember1() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        memberManager1.setStringValidator(new StringValidator());
        Member member = new Member();
        member.setLogin("ba");
        assertEquals("Login must be between 5 or 10 characters: ba", memberManager1.checkValidityOfParametersForInsertMember(member));
    }

    @Test
    @DisplayName("should return \"No Login provided\" when no login passed")
    void checkValidityOfParametersForUpdateMember() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        memberManager1.setStringValidator(new StringValidator());
        Member member = new Member();
        assertEquals("Login must be between 5 or 10 characters: null", memberManager1.checkValidityOfParametersForUpdateMember(member));
    }


    @Test
    @DisplayName("should return \"invalid email\" when email invalid")
    void checkValidityOfParametersForUpdateMember2() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        memberManager1.setStringValidator(new StringValidator());
        Member member = new Member();
        member.setLogin("kolio");
        member.setLastName("parollier");
        String email = "dede@fr";
        member.setEmail(email);
        assertEquals("Invalid Email: " + email, memberManager1.checkValidityOfParametersForUpdateMember(member));
    }

   /* @Test
    @DisplayName("should return \"nothing to update\" when member is null")
    void updateMember3() {
        Member oldMember = new Member();
        String lastname = "Margo";
        oldMember.setLastName(lastname);
        Member newMember = new Member();
        newMember.setLogin("poliko");
        newMember.setLastName(lastname);
        when(memberDAO.getMemberByLogin(anyString())).thenReturn(oldMember);
        assertEquals("nothing to update", memberManager.updateMember(newMember));

    }*/

}
