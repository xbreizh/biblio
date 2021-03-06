package org.troparo.consumer.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class MemberDAOImplTest {
    @Inject
    MemberDAO memberDAO;
    private Logger logger = Logger.getLogger(MemberDAOImplTest.class);


    @BeforeEach
    @Sql(scripts = "classpath:resetDb.sql")
    void reset() {
        logger.info("size: " + memberDAO.getAllMembers().size());
        logger.info("reset db");
    }


    @Test
    @DisplayName("should add member")
    void addMember() {
        assertTrue(memberDAO.addMember(new Member()));
    }

    @Test
    @DisplayName("should not insert null member")
    void addMember1() {
        memberDAO.addMember(null);
        assertEquals(3, memberDAO.getAllMembers().size());
    }

    @Test
    @DisplayName("should return all members")
    void getAllMembers() {
        assertEquals(3, memberDAO.getAllMembers().size());
    }

    @Test
    @DisplayName("should return an exception is sessionFactory is null")
    void getAllMembers1() {
        MemberDAOImpl memberDAO1 = new MemberDAOImpl();
        memberDAO1.setSessionFactory(null);
        assertEquals(0, memberDAO1.getAllMembers().size());
    }

    @Test
    @DisplayName("should return a member")
    void getMemberById() {
        final Member member = memberDAO.getMemberById(1);
        assertAll(
                () -> assertEquals("JPOLINO", member.getLogin()),
                () -> assertEquals("JOHN", member.getFirstName()),
                () -> assertEquals(1, member.getId()),
                () -> assertEquals("POLI@KOL.FR", member.getEmail())
        );

    }

    @Test
    @DisplayName("should return null if id not existing")
    void getMemberById1() {
        assertNull(memberDAO.getMemberById(33));

    }

    @Test
    @DisplayName("should return true if the login exists")
    void existingLogin() {
        assertTrue(memberDAO.existingLogin("jpolino"));
    }

    @Test
    @DisplayName("should return true if the login exists")
    void existingLogin1() {
        assertFalse(memberDAO.existingLogin("anything"));
    }

    @Test
    @DisplayName("should return empty list if member doesn't exist")
    void getMembersByCriterias() {
        Map<String, String> map = new HashMap<>();
        map.put("LOgIN", "jpolinfo");
        assertEquals(0, memberDAO.getMembersByCriterias(map).size());
    }

    @Test
    @DisplayName("should ignore if wrong key")
    void getMembersByCriterias1() {
        Map<String, String> map = new HashMap<>();
        map.put("LOGINo", "jpolinfo");
        assertEquals(0, memberDAO.getMembersByCriterias(map).size());
    }


    @Test
    @DisplayName("should return empty list if map is null")
    void getMembersByCriterias2() {

        assertEquals(0, memberDAO.getMembersByCriterias(null).size());
    }

    @Test
    @DisplayName("should return members")
    void getMembersByCriterias3() {
        Map<String, String> map = new HashMap<>();
        map.put("LOGIN", "jpolino");
        assertEquals(1, memberDAO.getMembersByCriterias(map).size());
    }

    @Test
    @DisplayName("should return members")
    void getMembersByCriterias4() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "o");
        map.put("email", "@");
        assertEquals(3, memberDAO.getMembersByCriterias(map).size());
    }

    @Test
    @DisplayName("should return members when using several criterias")
    void getMembersByCriterias5() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "admin");
        assertEquals(1, memberDAO.getMembersByCriterias(map).size());
    }


    @Test
    @DisplayName("should return members")
    void getMembersByCriterias6() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "admin");
        MemberDAOImpl memberDAO1 = new MemberDAOImpl();
        memberDAO1.setSessionFactory(null);
        assertEquals(0, memberDAO1.getMembersByCriterias(map).size());
    }


    @Test
    @DisplayName("should update member")
    void updateMember() {
        Member member = memberDAO.getMemberByLogin("JPOLINO");
        String newMail = "NEWEMAIL@MAIL.MAIL";
        assertNotEquals(newMail, member.getEmail());
        member.setEmail(newMail);
        assertEquals(newMail, member.getEmail());
    }

    @Test
    @DisplayName("should return false if session null")
    void updateMember1() {
        MemberDAOImpl memberDAO1 = new MemberDAOImpl();
        memberDAO1.setSessionFactory(null);
        assertFalse(memberDAO1.updateMember(new Member()));
    }

    @Test
    @DisplayName("should remove member")
    void remove() {
        Member member = memberDAO.getMemberById(1);
        memberDAO.remove(member);
        assertNull(memberDAO.getMemberById(1));

    }

    @Test
    @DisplayName("should remove member")
    void remove1() {
        MemberDAOImpl memberDAO1 = new MemberDAOImpl();
        memberDAO1.setSessionFactory(null);
        assertFalse(memberDAO1.remove(new Member()));

    }


    @Test
    @DisplayName("should invalidate token")
    void invalidateToken() {
        Member member = memberDAO.getMemberByLogin("LOKII");
        String token = member.getToken();
        member.setTokenexpiration(new Date()); // setting last connectDate to now
        memberDAO.updateMember(member);
        assertAll(
                () -> assertNotNull(memberDAO.getMemberByToken(token)),
                () -> assertTrue(memberDAO.invalidateToken(token)),
                () -> assertNull(memberDAO.getMemberByToken(token))
        );

    }

    @Test
    @DisplayName("return false if token doesn't exist")
    void invalidateToken1() {
        String token = "62751f44-b7db-49f5-a1dd9c-5b98edef50db";
        assertFalse(memberDAO.invalidateToken(token));
    }

    @Test
    @DisplayName("return false if session null")
    void invalidateToken2() {
        MemberDAOImpl memberDAO1 = new MemberDAOImpl();
        memberDAO1.setSessionFactory(null);
        String token = "62751f44-b7db-49f5-a1dd9c-5b98edef50db";
        assertFalse(memberDAO1.invalidateToken(token));
    }

    @Test
    @DisplayName("should get member by login")
    void getMemberByLogin() {
        Member member = memberDAO.getMemberByLogin("JPOLINO");
        assertAll(
                () -> assertEquals("JPOLINO", member.getLogin()),
                () -> assertEquals("JOHN", member.getFirstName()),
                () -> assertEquals(1, member.getId()),
                () -> assertEquals("POLI@KOL.FR", member.getEmail())
        );
    }

    @Test
    @DisplayName("should return null if member doesn't exist")
    void getMemberByLogin1() {
        assertNull(memberDAO.getMemberByLogin("anything"));
    }

    @Test
    @DisplayName("should return member if token exists")
    void getMemberByToken() {
        Member member = memberDAO.getMemberByLogin("LOKII");
        String token = member.getToken();
        assertNotNull(memberDAO.getMemberByToken(token));
    }

    @Test
    @DisplayName("should return null if token doesn't exist")
    void getMemberByToken1() {
        assertNull(memberDAO.getMemberByToken("12751f44-b7db-49f5-a19c-5b98edef50db"));
    }
}