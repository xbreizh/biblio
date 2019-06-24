package org.troparo.business.integration;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.MemberManagerImpl;
import org.troparo.business.impl.validator.StringValidator;
import org.troparo.model.Member;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class MemberManagerImplIntegrationTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Inject
    private MemberManager memberManager;

    @Sql({"classpath:/src/main/resources/resetDb.sql"})
    @BeforeEach
    void reset() {
        logger.info("reset db");
    }

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
/*
    @Test
    @DisplayName("should return \\token \\ if credentials ok")
    void getToken3() {
        assertNotEquals("wrong credentials",memberManager.getToken("LOKII", "123"));
    }*/

}
