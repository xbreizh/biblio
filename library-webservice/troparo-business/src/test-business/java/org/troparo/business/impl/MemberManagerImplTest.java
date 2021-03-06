package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.impl.validator.StringValidatorMember;
import org.troparo.consumer.impl.MemberDAOImpl;
import org.troparo.model.Member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Sql("classpath:resetDb.sql")
@Transactional
class MemberManagerImplTest {


    private MemberManagerImpl memberManager;
    private StringValidatorMember stringValidatorMember;
    private MemberDAOImpl memberDAO;


    @BeforeEach
    void init() {
        memberManager = new MemberManagerImpl();
        memberDAO = mock(MemberDAOImpl.class);
        memberManager.setMemberDAO(memberDAO);
        stringValidatorMember = mock(StringValidatorMember.class);
        memberManager.setStringValidatorMember(stringValidatorMember);
    }


    //  ADD MEMBER

    @Test
    @DisplayName("should return nothing when trying to insert a member")
    void addMember() {
        Member member = new Member();
        member.setLogin("tomoni");
        member.setFirstName("Tom");
        member.setLastName("Jones");
        member.setPassword("123");
        member.setEmail("rer.xax@gtgt.gt");
        when(stringValidatorMember.validateExpression(anyString(), anyString())).thenReturn(true);
        when(memberDAO.existingLogin("tomoni")).thenReturn(false);
        assertEquals("", memberManager.addMember(member));
    }

    @Test
    @DisplayName("should return exception when checking returns false")
    void addMember1() {
        Member member = new Member();
        String login = "tomoni";
        member.setLogin(login);
        when(stringValidatorMember.validateExpression("login", login)).thenReturn(false);
        when(stringValidatorMember.getException("login")).thenReturn("exception for: ");
        assertEquals("exception for: " + login, memberManager.addMember(member));
    }

    @Test
    @DisplayName("should return exception when login not found in DAO")
    void addMember2() {
        Member member = new Member();
        String login = "tomoni";
        member.setLogin(login);
        member.setFirstName("Tom");
        member.setLastName("Jones");
        member.setPassword("123");
        member.setEmail("rer.xax@gtgt.gt");
        when(stringValidatorMember.validateExpression(anyString(), anyString())).thenReturn(true);
        when(memberDAO.existingLogin(login)).thenReturn(true);
        assertEquals("Login already existing", memberManager.addMember(member));
    }


    @Test
    @DisplayName("should return member list")
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
        String login = "bob";
        when(memberDAO.getMemberByLogin(login)).thenReturn(null);
        assertNull(memberManager.getMemberByLogin(login));
    }

    // GET MEMBERS BY CRITERIAS

    @Test
    @DisplayName("should return member if member criterias existing")
    void getMembersByCriterias() {
        List<Member> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("login", "Maurice");
        when(memberDAO.getMembersByCriterias(map)).thenReturn(list);
        assertNotNull(memberManager.getMembersByCriteria(map));
    }

    @Test
    @DisplayName("should return empty list if criteria map null or empty")
    void getMembersByCriterias1() {
        Map<String, String> map = new HashMap<>();
        assertAll(
                () -> assertEquals(0, memberManager.getMembersByCriteria(map).size()),
                () -> assertEquals(0, memberManager.getMembersByCriteria(null).size())
        );

    }

    @Test
    @DisplayName("should return empty list if member criterias not existing")
    void getMembersByCriterias2() {
        Map<String, String> map = new HashMap<>();
        assertEquals(0, memberManager.getMembersByCriteria(map).size());
    }


    @Test
    @DisplayName("should ignore empty params")
    void getMembersByCriterias3() {
        Map<String, String> map = new HashMap<>();
        map.put("firstname", "");
        assertEquals(0, memberManager.getMembersByCriteria(map).size());
    }

    @Test
    @DisplayName("should ignore ? params")
    void getMembersByCriterias4() {
        Map<String, String> map = new HashMap<>();
        map.put("firstname", "?");
        assertEquals(0, memberManager.getMembersByCriteria(map).size());
    }

    // UPDATE MEMBER

    @Test
    @DisplayName("should return \" No member passed \" when member is null")
    void updateMember1() {
        assertEquals("No member passed", memberManager.updateMember(null));

    }

    @Test
    @DisplayName("should return \"No member found with that login\" when member is null")
    void updateMember2() {
        Member member = new Member();
        member.setLogin("polonium");
        when(memberDAO.getMemberByLogin(anyString())).thenReturn(null);
        assertEquals("No member found with that login", memberManager.updateMember(member));

    }


    @Test
    @DisplayName("should return \"No member found with that login\" when member is null")
    void updateMember3() {
        Member member = new Member();
        String login = "Polonium";
        member.setLogin(login);
        when(memberDAO.getMemberByLogin(anyString())).thenReturn(member);
        when(memberManager.checkValidityOfParametersForUpdateMember(member)).thenReturn("exception: ");
        assertEquals("exception: " + login, memberManager.updateMember(member));

    }


    @Test
    @DisplayName("should return an empty string if member valid")
    void checkValidityOfParametersForMember() {
        Member member = new Member();
        member.setLogin("lokoo");
        member.setFirstName("Basile");
        member.setLastName("brokl");
        member.setPassword("sdd");
        member.setEmail("sw.ddd@dede.fr");
        when(stringValidatorMember.validateExpression(anyString(), anyString())).thenReturn(true);
        assertEquals("", memberManager.checkValidityOfParametersForInsertMember(member));
    }


    @Test
    @DisplayName("should return \\no member provided \\ if member is null(insert)")
    void checkValidityOfParametersForMember1() {
        assertEquals("no member provided", memberManager.checkValidityOfParametersForInsertMember(null));
    }


    @Test
    @DisplayName("should return \\no member provided \\ if member is null (update)")
    void checkValidityOfParametersForUpdateMember() {
        assertEquals("no member provided", memberManager.checkValidityOfParametersForUpdateMember(null));
    }

    @Test
    @DisplayName("should return empty string when updating")
    void checkValidityOfParametersForUpdateMember1() {
        Member member = new Member();
        member.setLogin("lokoo");
        member.setFirstName("Basile");
        member.setLastName("brokl");
        member.setPassword("sdd");
        member.setEmail("sw.ddd@dede.fr");
        when(stringValidatorMember.validateForUpdateMember(anyString(), anyString())).thenReturn(true);
        assertEquals("", memberManager.checkValidityOfParametersForUpdateMember(member));
    }

    @Test
    @DisplayName("should return exception if member params invalid (update)")
    void checkValidityOfParametersForUpdateMember2() {
        Member member = new Member();
        String login = "Basil34";
        member.setLogin(login);
        when(stringValidatorMember.validateForUpdateMember("login", login)).thenReturn(false);
        when(stringValidatorMember.getException("login")).thenReturn("exception: ");
        assertEquals("exception: " + login, memberManager.checkValidityOfParametersForUpdateMember(member));
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

    // GET TOKEN

    @Test
    @DisplayName("should return \\wrong credentials \\ if credentials are wrong")
    void getToken() {
        Member member = new Member();
        String login = "bob";
        String password = "123";
        when(memberDAO.getMemberByLogin(anyString())).thenReturn(member);
        assertEquals("wrong credentials", memberManager.getToken(login, password));
    }

    @Test
    @DisplayName("should return \\wrong credentials \\ if one credential is null( or both)")
    void getToken1() {
        assertAll(
                () -> assertEquals("wrong credentials", memberManager.getToken(null, "plok")),
                () -> assertEquals("wrong credentials", memberManager.getToken("plok", null)),
                () -> assertEquals("wrong credentials", memberManager.getToken(null, null))
        );
    }

    @Test
    @DisplayName("should return \\wrong credentials \\ if member is null")
    void getToken2() {
        String login = "bob";
        String password = "123";
        when(memberDAO.getMemberByLogin(login)).thenReturn(null);
        assertEquals("wrong credentials", memberManager.getToken(login, password));
    }

    @Test
    @DisplayName("should add 20mn to current date")
    void adding20MnToCurrentDate() {
        Date now = memberManager.getNow();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MINUTE, 20);  // number of mn to add

        assertEquals(c.getTime(), memberManager.adding20MnToCurrentDate());
    }


    @Test
    void checkToken() throws ParseException {
        Member member = new Member();
        String token = "token123";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse("2020-09-09");
        member.setToken(token);
        member.setTokenexpiration(date);
        when(memberDAO.getMemberByToken(token)).thenReturn(member);
        assertTrue(memberManager.checkToken(token));
    }

    // GENERATE TOKEN

    @Test
    @DisplayName("should return a token")
    void generateToken() {
        MemberManagerImpl memberManager1 = spy(MemberManagerImpl.class);
        memberManager1.setMemberDAO(memberDAO);
        when(memberDAO.getMemberByToken(anyString())).thenReturn(null);
        assertNotNull(memberManager1.generateToken());

    }

    @Test
    @DisplayName("should return null")
    void generateToken1() {
        MemberManagerImpl memberManager1 = spy(MemberManagerImpl.class);
        memberManager1.setMemberDAO(memberDAO);
        Member member = new Member();
        when(memberDAO.getMemberByToken(anyString())).thenReturn(member);
        assertNull(memberManager1.generateToken());

    }

    // INVALIDATE TOKEN

    @Test
    @DisplayName("should return false if any issue while invalidating a token")
    void invalidateToken() {
        when(memberDAO.getMemberByToken(anyString())).thenReturn(null);
        assertFalse(memberManager.invalidateToken("token123"));
    }

    @Test
    @DisplayName("should return false if any issue updating the member (DAO)")
    void invalidateToken1() {
        Member member = new Member();
        member.setToken("token123");
        when(memberDAO.getMemberByToken(anyString())).thenReturn(member);
        when(memberDAO.updateMember(member)).thenReturn(false);
        assertFalse(memberManager.invalidateToken("token123"));
    }

    @Test
    @DisplayName("should return true if invalidate token is successful")
    void invalidateToken3() {
        Member member = new Member();
        when(memberDAO.getMemberByToken(anyString())).thenReturn(member);
        when(memberDAO.updateMember(member)).thenReturn(true);
        assertTrue(memberManager.invalidateToken(anyString()));
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
    void updatePasswords() {
        String login = "KOL";
        String password = "kokl";
        memberManager.setMemberDAO(memberDAO);
        memberManager.getMemberByLogin(login);
        assertFalse(memberManager.resetPassword(login, password));

    }

    @Test
    @DisplayName("should return false if member login, password or email is null")
    void updatePasswordNoLogin() {
        MemberManagerImpl mgr = new MemberManagerImpl();
        String login = "login";
        String password = "password";
        assertAll(
                () -> assertFalse(mgr.resetPassword(null, password)),
                () -> assertFalse(mgr.resetPassword(login, null))
        );


    }

    @Test
    @DisplayName("should return false if member email is different")
    void updatePassword3() {
        String login = "KOL";
        String password = "kokl";
        Member member = new Member();
        member.setLogin(login);
        member.setRole("ploc");
        when(memberManager.getMemberByLogin(login)).thenReturn(member);
        assertFalse(memberManager.resetPassword(login, password));

    }

    @Test
    @DisplayName("should return true if update password ok")
    void updatePassword4() {
        String login = "KOL";
        String email = "cdcd@test.fr";
        Member member = new Member();
        member.setEmail(email);
        member.setRole("de");
        when(memberManager.getMemberByLogin(login)).thenReturn(member);
        when(memberDAO.updateMember(member)).thenReturn(true);
        assertTrue(memberManager.resetPassword(login, anyString()));

    }


    @Test
    @DisplayName("should return false if member password is null")
    void updatePassword2() {
        String login = "KOL";
        String password = "kokl";
        Member member = new Member();
        member.setLogin(login);
        member.setRole("plok");
        memberManager.setMemberDAO(memberDAO);
        when(memberManager.getMemberByLogin(login)).thenReturn(member);
        memberManager.getMemberByLogin(login);
        assertFalse(memberManager.resetPassword(login, password));

    }

    //  CHECK ADMIN

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

    @Test
    @DisplayName("should return false when member is null")
    void checkAdmin3() {
        when(memberDAO.getMemberByToken(anyString())).thenReturn(null);
        assertFalse(memberManager.checkAdmin(anyString()));
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

}