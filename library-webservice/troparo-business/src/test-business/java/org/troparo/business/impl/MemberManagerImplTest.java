package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.troparo.business.EmailValidator;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.validation.Validator;
import java.io.IOException;

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
    }

    @Test
    void getMemberById() {
    }

    @Test
    void getMemberByLogin() {
    }

    @Test
    void getMembersByCriterias() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void remove() {
    }

    @Test
    void getToken() {
    }

    @Test
    void checkToken() {
        String token = "123";
        when(memberDAO.checkToken(anyString())).thenReturn(true);
        assertNotNull(memberManager.checkToken(anyString()));
    }

    @Test
    void invalidateToken() {
    }

    @Test
    void disconnect() {
    }

    @Test
    void connect() {
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
    void checkPassword() {
        String password1 = "test123";
        String password2 = memberManager.encryptPassword(password1);
        String password3 = "plouf";
        assertTrue(memberManager.checkPassword(password1, password2));
        assertFalse(memberManager.checkPassword(password1, password3));
    }

    @Test
    void updatePassword() {
    }

    @Test
    void checkAdmin() {
    }
}