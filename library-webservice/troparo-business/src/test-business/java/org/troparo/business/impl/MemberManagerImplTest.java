package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.troparo.business.EmailValidator;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberManagerImplTest {


    private MemberManager memberManager;

    private MemberDAO memberDAO;


    @BeforeEach
    void init() {
        memberManager = new MemberManagerImpl();
        memberDAO = mock(MemberDAO.class);
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
    @DisplayName("should update member")
    void updateMember() {
       /* MemberManagerImpl manager = mock(MemberManagerImpl.class);
        Member member = new Member();
       *//* member.setLogin("dedeww");
        member.setFirstName("Gedeon");
        member.setLastName("Poligo");
        member.setPassword("123");
        member.setEmail("sasa@tet.tet");*//*
        when(manager.checkRequiredValuesNotNull(member)).thenReturn("");
        when(manager.checkValidityOfParametersForMember(member)).thenReturn("");
        //when(manager.checkIfLoginHasBeenPassed("Bobby")).thenReturn(false);
        HashMap<String, String> map = new HashMap<>();
        map.put("Login", member.getLogin());


        when(memberDAO.getMembersByCriterias(map)).thenReturn(null);
        manager.updateMember(member);
        //assertEquals("No Item found with that Login", manager.updateMember(member));*/
        fail();
    }

    @Test
    @DisplayName("should return error if param empty or null")
    void checkRequiredValuesNotNull(){
        fail();
    }

    @Test
    @DisplayName("should return an empty string if remove successful")
    void remove() {
        Member member = new Member();
        when(memberDAO.getMemberById(2)).thenReturn(member);
        when(memberDAO.remove(member)).thenReturn(true);
        assertEquals("", memberManager.remove(2));
    }

    @Test
    @DisplayName("should return \"No item found\" if member couldn't be found")
    void remove1() {
        Member member = new Member();
        when(memberDAO.getMemberById(2)).thenReturn(null);
        assertEquals("No item found", memberManager.remove(2));
    }

    @Test
    @DisplayName("should return \"No item found\" if member couldn't be found")
    void remove2() {
        Member member = new Member();
        when(memberDAO.getMemberById(2)).thenReturn(member);
        when(memberDAO.remove(member)).thenReturn(false);
        assertEquals("Issue while removing member", memberManager.remove(2));
    }

    @Test
    @DisplayName("should return wrong login or password if credentials are wrong")
    void getToken() {
        MemberManager memberMgr = spy(memberManager);
        String login = "lpl";
        String pwd = "lk";
        when(memberMgr.checkPassword(login, pwd)).thenReturn(false);
        assertEquals("wrong login or pwd", memberMgr.getToken(login, pwd));
    }

    @Test
    void checkToken() {
        String token = "123";
        when(memberDAO.checkToken(anyString())).thenReturn(true);
        assertNotNull(memberManager.checkToken(anyString()));
    }

    @Test
    @DisplayName("should return a token")
    void generateToken() {
        MemberManagerImpl memberManager1 = spy(MemberManagerImpl.class);
        memberManager1.setMemberDAO(memberDAO);
        assertNotNull(memberManager1.generateToken());

    }

    @Test
    @DisplayName("should return false if any issue while invalidating a token")
    void invalidateToken() {
        //memberManager.invalidateToken("token123");
        assertFalse(memberManager.invalidateToken("token123"));
    }

    @Test
    @DisplayName("should return false if any issue updating the member (DAO)")
    void invalidateToken1() {
        Member member = new Member();
        member.setToken("token123");
        when(memberDAO.updateMember(member)).thenReturn(false);
        assertFalse(memberManager.invalidateToken("token123"));
    }

    @Test
    @DisplayName("should return true if invalidate token is successful")
    void invalidateToken3() {
        Member member = new Member();
        member.setToken("token123");
        when(memberDAO.updateMember(member)).thenReturn(true);
        assertFalse(memberManager.invalidateToken("token123"));
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
    @DisplayName("should return false if member is null")
    void updatePassword() {
        String login = "KOL";
        String password = "kokl";
        String email = "cdcd@test.fr";
        memberManager.setMemberDAO(memberDAO);
        memberManager.getMemberByLogin(login);
        assertFalse(memberManager.updatePassword(login, password, email));

    }

    @Test
    @DisplayName("should return false if member login, password or email is null")
    void updatePasswordNoLogin() {
        MemberManagerImpl mgr = new MemberManagerImpl();
        String login = "login";
        String email = "email";
        String password = "password";
        assertAll(
                () -> assertFalse(mgr.updatePassword(null, email, password)),
                () -> assertFalse(mgr.updatePassword(login, null, password)),
                () -> assertFalse(mgr.updatePassword(login, email, null))
        );


    }

    @Test
    @DisplayName("should return false if member email is different")
    void updatePassword3() {
        String login = "KOL";
        String password = "kokl";
        String email = "cdcd@test.fr";
        Member member = new Member();
        member.setLogin(login);
        member.setEmail("different@email.com");
        when(memberManager.getMemberByLogin(login)).thenReturn(member);
        assertFalse(memberManager.updatePassword(login, password, email));

    }

    @Test
    @DisplayName("should return true if update password ok")
    void updatePassword4() {
        String login = "KOL";
        String email = "cdcd@test.fr";
        Member member = new Member();
        member.setEmail(email);
        when(memberManager.getMemberByLogin(login)).thenReturn(member);
        when(memberDAO.updateMember(member)).thenReturn(true);
        assertTrue(memberManager.updatePassword(login, email, anyString()));

    }


    @Test
    @DisplayName("should return false if member password is null")
    void updatePassword2() {
        String login = "KOL";
        String password = "kokl";
        String email = "cdcd@test.fr";
        Member member = new Member();
        member.setLogin(login);
        member.setEmail(email);
        memberManager.setMemberDAO(memberDAO);
        when(memberManager.getMemberByLogin(login)).thenReturn(member);
        memberManager.getMemberByLogin(login);
        assertFalse(memberManager.updatePassword(login, password, email));

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