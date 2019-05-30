package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.troparo.business.EmailValidator;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberManagerImplTest {


    private MemberManager memberManager;

    MemberDAO memberDAO = mock(MemberDAO.class);


    @BeforeEach
    void init() {
        memberManager = new MemberManagerImpl();
        memberManager.setMemberDAO(memberDAO);
        EmailValidator validator = mock(EmailValidator.class);
        memberManager.setValidator(validator);
    }

    @Test
    @DisplayName("should say that the login already exist")
    void addMember() {
        when(memberDAO.existingLogin("tomoni")).thenReturn(true);
        Member member = new Member();
        member.setLogin("tomoni");
        member.setFirstName("");
        assertEquals("Login already existing", memberManager.addMember(member));

    }


    @Test
    @DisplayName("should return nothing when trying to insert a member")
    void addMember2() throws IOException, SAXException {
        String email = "rer.xax@gtgt.gt";
        Member member = new Member();
        member.setLogin("tomoni");
        member.setFirstName("Tom");
        member.setLastName("Jones");
        member.setPassword("123");
        member.setEmail("rer.xax@gtgt.gt");
        when(memberManager.validateEmail(member)).thenReturn(true);
        when(memberDAO.existingLogin("tomoni")).thenReturn(false);
        assertEquals("", memberManager.addMember(member));
    }

    @Test
    void getMembers() {
        List<Member> list = new ArrayList<>();
        when(memberDAO.getAllMembers()).thenReturn(list);
        assertNotNull(memberManager.getMembers());
    }

    @Test
    @DisplayName("should return member if member id existing")
    void getMemberById() {
        Member m = new Member();
        when(memberDAO.getMemberById(anyInt())).thenReturn(m);
        assertNotNull(memberManager.getMemberById(3));
    }

    @Test
    @DisplayName("should return null if member id not existing")
    void getMemberById1() {
        when(memberDAO.getMemberById(anyInt())).thenReturn(null);
        assertNull(memberManager.getMemberById(3));
    }

    @Test
    @DisplayName("should return member if member login existing")
    void getMemberByLogin() {
        Member m = new Member();
        when(memberDAO.getMemberByLogin(anyString())).thenReturn(m);
        assertNotNull(memberManager.getMemberByLogin("bob"));
    }

    @Test
    @DisplayName("should return null if member login not existing")
    void getMemberByLogin1() {
        when(memberDAO.getMemberByLogin(anyString())).thenReturn(null);
        assertNull(memberManager.getMemberByLogin("bob"));
    }

    @Test
    @DisplayName("should return member if member criterias existing")
    void getMembersByCriterias() {
        List<Member> list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        when(memberDAO.getMembersByCriterias(map)).thenReturn(list);
        assertNotNull(memberManager.getMembersByCriterias(map));
    }

    @Test
    @DisplayName("should return null if member criterias not existing")
    void getMembersByCriterias1() {
        HashMap<String, String> map = new HashMap<>();
        when(memberDAO.getMembersByCriterias(map)).thenReturn(null);
        assertNull(memberManager.getMembersByCriterias(map));
    }

    @Test
    void updateMember() {
    fail();
    }

    @Test
    void remove() {
        fail();
    }

    @Test
    void getToken() {
        fail();
    }

    @Test
    void checkToken() {
        String token = "123";
        when(memberDAO.checkToken(anyString())).thenReturn(true);
        assertNotNull(memberManager.checkToken(anyString()));
    }

    @Test
    void invalidateToken() {
        fail();
    }

    @Test
    @DisplayName("")
    void disconnect() {
        fail();
    }

    @Test
    @DisplayName("")
    void connect() {
        fail();
    }

    @Test
    @DisplayName("should return a token")
    void encryptPassword() {
        assertNotNull(memberManager.encryptPassword("test123"));
    }

    @Test
    @DisplayName("should return an encrypted password different from the initial")
    void encryptPassword1() {
        assertNotEquals("test123", memberManager.encryptPassword("test123"));
    }

    @Test
    @DisplayName("should return true if password correct")
    void checkPassword() {
        String password1 = "test123";
        String password2 = memberManager.encryptPassword(password1);
        String password3 = "plouf";
        assertTrue(memberManager.checkPassword(password1, password2));
    }

    @Test
    @DisplayName("should return false if password incorrect")
    void checkPassword1() {
        String password1 = "test123";
        String password3 = "plouf";
        assertFalse(memberManager.checkPassword(password1, password3));
    }

    @Test
    @DisplayName("")
    void updatePassword() {

        fail();
    }

    @Test
    @DisplayName("should return true when member role is Admin")
    void checkAdmin() {
        Member member = new Member();
        member.setRole("Admin");
        when(memberDAO.getMemberByToken(anyString())).thenReturn(member);
        assertTrue(memberManager.checkAdmin(anyString()));
    }

    @Test
    @DisplayName("should return false when member role is not Admin")
    void checkAdmin1() {
        Member member = new Member();
        member.setRole("dede");
        when(memberDAO.getMemberByToken(anyString())).thenReturn(member);
        assertFalse(memberManager.checkAdmin(anyString()));
    }

    @Test
    @DisplayName("should return false when member role is null")
    void checkAdmin2() {
        Member member = new Member();
        when(memberDAO.getMemberByToken(anyString())).thenReturn(member);
        assertFalse(memberManager.checkAdmin(anyString()));
    }
}