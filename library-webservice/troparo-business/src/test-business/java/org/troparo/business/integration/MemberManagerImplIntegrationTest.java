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
import org.troparo.business.impl.validator.StringValidatorMember;
import org.troparo.model.Member;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class MemberManagerImplIntegrationTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Inject
    private MemberManager memberManager;

    @Sql({"classpath:resetDb.sql"})
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
        memberManager1.setStringValidatorMember(new StringValidatorMember());
        Member member = new Member();
        member.setLogin("ba");
        assertEquals("Login must be between 5 or 10 characters: ba", memberManager1.checkValidityOfParametersForInsertMember(member));
    }

    @Test
    @DisplayName("should return \"No Login provided\" when no login passed")
    void checkValidityOfParametersForUpdateMember() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        memberManager1.setStringValidatorMember(new StringValidatorMember());
        Member member = new Member();
        assertEquals("Login must be between 5 or 10 characters: null", memberManager1.checkValidityOfParametersForUpdateMember(member));
    }


    @Test
    @DisplayName("should return \"invalid email\" when email invalid")
    void checkValidityOfParametersForUpdateMember2() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        memberManager1.setStringValidatorMember(new StringValidatorMember());
        Member member = new Member();
        member.setLogin("kolio");
        member.setLastName("parollier");
        String email = "dede@fr";
        member.setEmail(email);
        assertEquals("Invalid Email: " + email, memberManager1.checkValidityOfParametersForUpdateMember(member));
    }
    // TRANFERT UPDATE DETAILS

    @Test
    @DisplayName("should return false when role null or ? or empty")
    void updateRole() {
        MemberManagerImpl memberManager = new MemberManagerImpl();
        String[] wrongRoles = {"", null, "?"};
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbRole = "admin";
        memberDb.setRole(dbRole);
        for (String newRole : wrongRoles
        ) {
            newMember.setRole(newRole);
            assertFalse(memberManager.updateRole(memberDb, newMember));
        }

    }

    @Test
    @DisplayName("should return true when role valid")
    void updateRole1() {
        MemberManagerImpl memberManager = new MemberManagerImpl();
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbRole = "admin";
        memberDb.setRole(dbRole);
        String newDbRole = "superAdmin";
        newMember.setRole(newDbRole);
        assertTrue(memberManager.updateRole(memberDb, newMember));

    }

    @Test
    @DisplayName("should return false when email null or ? or empty")
    void updateEmail() {
        MemberManagerImpl memberManager = new MemberManagerImpl();
        String[] wrongEmails = {"", null, "?"};
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbEmail = "admin@admin.admin";
        memberDb.setEmail(dbEmail);
        for (String newEmail : wrongEmails
        ) {
            newMember.setEmail(newEmail);
            assertFalse(memberManager.updateEmail(memberDb, newMember));
        }

    }

    @Test
    @DisplayName("should return true when email valid")
    void updateEmail1() {
        MemberManagerImpl memberManager = new MemberManagerImpl();
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbEmail = "admin@admin.admin";
        memberDb.setEmail(dbEmail);
        String newEmail = "user@user.user";
        newMember.setEmail(newEmail);
        assertTrue(memberManager.updateEmail(memberDb, newMember));

    }


    @Test
    @DisplayName("should return false when lastname null or ? or empty")
    void updateLastName() {
        MemberManagerImpl memberManager = new MemberManagerImpl();
        String[] wrongLastNames = {"", null, "?"};
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbLastName = "Jomano";
        memberDb.setLastName(dbLastName);
        for (String newLastName : wrongLastNames
        ) {
            newMember.setLastName(newLastName);
            assertFalse(memberManager.updateLastName(memberDb, newMember));
        }

    }

    @Test
    @DisplayName("should return true when role valid")
    void updateLastName1() {
        MemberManagerImpl memberManager = new MemberManagerImpl();
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbLastName = "Sacomano";
        memberDb.setLastName(dbLastName);
        String newLastName = "Romarin";
        newMember.setLastName(newLastName);
        assertTrue(memberManager.updateLastName(memberDb, newMember));

    }

    @Test
    @DisplayName("should return false when firstName null or ? or empty")
    void updateFirstName() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        String[] wrongFirstNames = {"", null, "?"};
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbFirstName = "Jomano";
        memberDb.setFirstName(dbFirstName);
        for (String newFirstName : wrongFirstNames
        ) {
            newMember.setFirstName(newFirstName);
            assertFalse(memberManager1.updateFirstName(memberDb, newMember));
        }

    }

    @Test
    @DisplayName("should return true when role valid")
    void updateFirstName1() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        Member newMember = new Member();
        Member memberDb = new Member();
        String dbFirstName = "Sacomano";
        memberDb.setFirstName(dbFirstName);
        String newFirstName = "Romarin";
        newMember.setFirstName(newFirstName);
        assertTrue(memberManager1.updateFirstName(memberDb, newMember));

    }

    @Test
    @DisplayName("should update Member")
    void transfertUpdatedDetails() {
        MemberManagerImpl memberManager1 = new MemberManagerImpl();
        Member memberDb = new Member();
        String firstName = "Joe";
        String lastName = "Morris";
        String email = "joe.morris@frfr.te";
        String role = "admin";
        Member newMember = new Member();

        newMember.setFirstName(firstName);
        newMember.setLastName(lastName);
        newMember.setEmail(email);
        newMember.setRole(role);
        assertAll(
                () -> assertEquals(firstName, memberManager1.transfertUpdatedDetails(newMember, memberDb).getFirstName()),
                () -> assertEquals(lastName, memberManager1.transfertUpdatedDetails(newMember, memberDb).getLastName()),
                () -> assertEquals(email, memberManager1.transfertUpdatedDetails(newMember, memberDb).getEmail()),
                () -> assertEquals(role, memberManager1.transfertUpdatedDetails(newMember, memberDb).getRole())
        );

    }

    @Test
    @DisplayName("should return empty string")
    void updateMember4() {
        Member member1 = memberManager.getMemberById(2);
        String email = "email@111.222.333.44444";
        member1.setEmail(email);
        assertEquals("", memberManager.updateMember(member1));
    }

    @Test
    @DisplayName("should return \\wrong credentials\\ ")
    void getToken() {
        assertEquals("wrong credentials", memberManager.getToken("LOKII", "123"));
    }

    @Test
    @DisplayName("should return a token")
    void getToken1() {
        String login = "lokii";
        String password = "123";
        memberManager.updatePassword("lokii", "LOKI@LOKI.LOKII", "123");
        assertNotEquals("wrong credentials", memberManager.getToken(login, password));
    }


}
