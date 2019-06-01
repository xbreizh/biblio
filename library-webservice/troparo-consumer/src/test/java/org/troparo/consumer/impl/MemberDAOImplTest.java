package org.troparo.consumer.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class MemberDAOImplTest {


    @Inject
    MemberDAO memberDAO;


    @Test
    @DisplayName("should add member")
    void addMember() {
        Member member = new Member();
        memberDAO.addMember(member);
        assertEquals(3, memberDAO.getAllMembers().size());
    }

    @Test
    @DisplayName("should not insert null member")
    void addMember1() {
        Member member = new Member();
        memberDAO.addMember(null);
        assertEquals(2, memberDAO.getAllMembers().size());
    }

    @Test
    @DisplayName("should return all members")
    void getAllMembers() {
        assertEquals(2, memberDAO.getAllMembers().size());
    }

    @Test
    @DisplayName("should return a member")
    void getMemberById() {
        final Member member = memberDAO.getMemberById(2);
        assertAll(
                () -> assertEquals("JPOLINO", member.getLogin()),
                () -> assertEquals("JOHN", member.getFirstName()),
                () -> assertEquals(2, member.getId()),
                () -> assertEquals("POLI@KOL.FR", member.getEmail())
        );

    }

    @Test
    @DisplayName("should return null if id not existing")
    void getMemberById1() {
        assertNull(memberDAO.getMemberById(3));

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
        HashMap<String, String> map = new HashMap<>();
        map.put("LOgIN", "jpolinfo");
        assertEquals(0, memberDAO.getMembersByCriterias(map).size());
    }

    @Test
    @DisplayName("should ignore if wrong key")
    void getMembersByCriterias1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("LOGINo", "jpolinfo");
        assertEquals(0, memberDAO.getMembersByCriterias(map));
    }

    @Test
    @DisplayName("should return empty list if map is null")
    void getMembersByCriterias2() {

        assertEquals(0, memberDAO.getMembersByCriterias(null).size());
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
    @DisplayName("should remove member")
    void remove() {
        Member member = memberDAO.getMemberById(1);
        memberDAO.remove(member);
        assertNull(memberDAO.getMemberById(1));

    }

    @Test
    @DisplayName("should return true if token valid")
    void checkToken() {
        Member member = memberDAO.getMemberByToken("62751f44-b7db-49f5-a19c-5b98edef50db");
        member.setDateConnect(new Date()); // setting last connectDate to now
        memberDAO.updateMember(member);
        assertTrue(memberDAO.checkToken("62751f44-b7db-49f5-a19c-5b98edef50db"));
    }

    @Test
    @DisplayName("should return false if token invalid")
    void checkToken1() {
        assertFalse(memberDAO.checkToken("62751f44-b7db-49f5-a19c-5b98edef50db"));
    }

    @Test
    @DisplayName("should invalidate token")
    void invalidateToken() {
        String token = "62751f44-b7db-49f5-a19c-5b98edef50db";
        Member member = memberDAO.getMemberByToken(token);
        member.setDateConnect(new Date()); // setting last connectDate to now
        memberDAO.updateMember(member);
        assertAll(
                () -> assertTrue(memberDAO.checkToken(token)),
                () -> assertTrue(memberDAO.invalidateToken(token)),
                () -> assertFalse(memberDAO.checkToken(token))
        );

    }

    @Test
    @DisplayName("return false if token doesn't exist")
    void invalidateToken1() {
        String token = "62751f44-b7db-49f5-a1dd9c-5b98edef50db";
        assertFalse(memberDAO.invalidateToken(token));
        assertFalse(memberDAO.checkToken(token));
    }

    @Test
    @DisplayName("should get member by login")
    void getMemberByLogin() {
        final Member member = memberDAO.getMemberByLogin("JPOLINO");
        assertAll(
                () -> assertEquals("JPOLINO", member.getLogin()),
                () -> assertEquals("JOHN", member.getFirstName()),
                () -> assertEquals(2, member.getId()),
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
        assertNotNull(memberDAO.getMemberByToken("62751f44-b7db-49f5-a19c-5b98edef50db"));
    }

    @Test
    @DisplayName("should return null if token doesn't exist")
    void getMemberByToken1() {
        assertNull(memberDAO.getMemberByToken("12751f44-b7db-49f5-a19c-5b98edef50db"));
    }
}