package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.connect.CheckTokenRequestType;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.InvalidateTokenRequestType;
import org.troparo.entities.connect.ResetPasswordRequestType;
import org.troparo.services.connectservice.BusinessExceptionConnect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/*@ContextConfiguration("classpath:/org/troparo/web/config/spring-hibernate-jax-ws-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional*/
@ExtendWith(MockitoExtension.class)
class ConnectServiceImplTest {


    private ConnectServiceImpl connectService;
    @Mock
    private MemberManager memberManager;


    @BeforeEach
    void init() {
        connectService = new ConnectServiceImpl();
        connectService.setMemberManager(memberManager);

    }

    @Test
    @DisplayName("should return true if existing token")
    void invalidateToken() {
        String token = "7ca1c74f-02cd-41d9-82f4-5f717a96bcf3";
        InvalidateTokenRequestType parameters = new InvalidateTokenRequestType();
        parameters.setToken(token);
        when(memberManager.invalidateToken(token)).thenReturn(true);
        assertTrue(connectService.invalidateToken(parameters).isReturn());
    }

    @Test
    @DisplayName("should return false if none-existing token")
    void invalidateToken1() {
        String token = "7ca1c74f-02cd-41d9-82f4-5f717a96bcf3";
        InvalidateTokenRequestType parameters = new InvalidateTokenRequestType();
        parameters.setToken(token);
        when(memberManager.invalidateToken(token)).thenReturn(false);
        assertFalse(connectService.invalidateToken(parameters).isReturn());
    }

    @Test
    @DisplayName("should return a token")
    void getToken()  {
        GetTokenRequestType parameters = new GetTokenRequestType();
        String token = "token123";
        parameters.setLogin("lokii");
        parameters.setPassword("123");
        when(memberManager.getToken(anyString(), anyString())).thenReturn(token);
        assertEquals(token, connectService.getToken(parameters).getReturn());
    }

    @Test
    @DisplayName("should return wrong login or password when wrong credentials")
    void getToken1()  {
        GetTokenRequestType parameters = new GetTokenRequestType();
        parameters.setLogin("Lokii");
        parameters.setPassword("1243");
        String error = "wrong login or pwd";
        when(memberManager.getToken(anyString(), anyString())).thenReturn(error);
        assertEquals(error, connectService.getToken(parameters).getReturn());
    }

    @Test
    @DisplayName("should return \"something went wrong\" when token is null")
    void getToken2(){
        GetTokenRequestType parameters = new GetTokenRequestType();
        parameters.setLogin(null);
        parameters.setPassword(null);
        //when(memberManager.getToken(anyString(), anyString())).thenReturn(null);
        assertEquals("something went wrong", connectService.getToken(parameters).getReturn());
    }

    @Test
    @DisplayName("should return false when token invalid")
    void checkToken() {
        CheckTokenRequestType parameters = new CheckTokenRequestType();
        parameters.setToken("dedede");
        when(memberManager.checkToken(anyString())).thenReturn(false);
        assertFalse(connectService.checkToken(parameters).isReturn());
    }


    @Test
    @DisplayName("should return true when token valid")
    void checkToken1() {
        CheckTokenRequestType parameters = new CheckTokenRequestType();
        parameters.setToken("dedede");
        when(memberManager.checkToken(anyString())).thenReturn(true);
        assertTrue(connectService.checkToken(parameters).isReturn());
    }

    @Test
    @DisplayName("should return true if credentials are correct")
    void resetPassword(){
        ResetPasswordRequestType parameters = new ResetPasswordRequestType();
        parameters.setLogin("jpolino");
        parameters.setPassword("mik");
        when(memberManager.resetPassword(anyString(),  anyString())).thenReturn(true);
        assertTrue(connectService.resetPassword(parameters).isReturn());
    }

    @Test
    @DisplayName("should return false if credentials are incorrect")
    void resetPassword1() {
        ResetPasswordRequestType parameters = new ResetPasswordRequestType();
        parameters.setLogin("jpolino");
        parameters.setPassword("mik");
        when(memberManager.resetPassword(anyString(),  anyString())).thenReturn(false);
        assertFalse(connectService.resetPassword(parameters).isReturn());
    }


    @Test
    @DisplayName("should return false if one of the credentials is null")
    void resetPassword12()  {
        ResetPasswordRequestType parameters = new ResetPasswordRequestType();
        parameters.setLogin(null);
        parameters.setPassword("mik");
        assertFalse(connectService.resetPassword(parameters).isReturn());
    }
}