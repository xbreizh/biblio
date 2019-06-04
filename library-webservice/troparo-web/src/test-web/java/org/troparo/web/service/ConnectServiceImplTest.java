package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.entities.connect.CheckTokenRequestType;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.InvalidateTokenRequestType;
import org.troparo.entities.connect.ResetPasswordRequestType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/org/troparo/web/config/spring-hibernate-jax-ws-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional
class ConnectServiceImplTest {

    private IConnectService connectService;



    @BeforeEach
    void init() {
        connectService = new ConnectService().getConnectServicePort();
    }

    @Test
    @DisplayName("should return true if existing token")
    void invalidateToken() throws BusinessExceptionConnect {
        InvalidateTokenRequestType parameters = new InvalidateTokenRequestType();
        parameters.setToken("7ca1c74f-02cd-41d9-82f4-5f717a96bcf3");
        assertTrue(connectService.invalidateToken(parameters).isReturn());
    }

    @Test
    @DisplayName("should return false if none-existing token")
    void invalidateToken1() throws BusinessExceptionConnect {
        InvalidateTokenRequestType parameters = new InvalidateTokenRequestType();
        parameters.setToken("4b2a646a-2b5e-4af3-a465-9add0c47712090");
        assertFalse(connectService.invalidateToken(parameters).isReturn());
    }

    @Test
    @DisplayName("should return a token")
    void getToken() throws BusinessExceptionConnect {
        GetTokenRequestType parameters = new GetTokenRequestType();
        parameters.setLogin("Lokii");
        parameters.setPassword("123");
        assertNotNull(connectService.getToken(parameters).getReturn());
    }

    @Test
    @DisplayName("should return wrong login or password when wrong credentials")
    void getToken1() throws BusinessExceptionConnect {
        GetTokenRequestType parameters = new GetTokenRequestType();
        parameters.setLogin("Lokii");
        parameters.setPassword("1243");
        assertEquals("wrong login or pwd", connectService.getToken(parameters).getReturn());
    }

    @Test
    @DisplayName("should return wrong login or password when login or pwd null")
    void getToken2() throws BusinessExceptionConnect {
        GetTokenRequestType parameters = new GetTokenRequestType();
        parameters.setLogin(null);
        parameters.setPassword(null);
        assertEquals("wrong login or pwd", connectService.getToken(parameters).getReturn());
    }

    @Test
    @DisplayName("should return false when token invalid")
    void checkToken() throws BusinessExceptionConnect {
        CheckTokenRequestType parameters = new CheckTokenRequestType();
        parameters.setToken("dedede");
        assertFalse( connectService.checkToken(parameters).isReturn());
    }


    @Test
    @DisplayName("should return true when token valid")
    void checkToken1() throws BusinessExceptionConnect {
        CheckTokenRequestType parameters = new CheckTokenRequestType();

        parameters.setToken("4b2a646a-2b5e-4af3-a465-9a0c47712090");
        assertTrue( connectService.checkToken(parameters).isReturn());
    }

    @Test
    @DisplayName("should return true if credentials are correct")
    void resetPassword() throws BusinessExceptionConnect {
        ResetPasswordRequestType parameters = new ResetPasswordRequestType();
        parameters.setEmail("POLI@KOL.FR");
        parameters.setLogin("jpolino");
        parameters.setPassword("mik");
        assertTrue(connectService.resetPassword(parameters).isReturn());
    }

    @Test
    @DisplayName("should return false if credentials are incorrect")
    void resetPassword1() throws BusinessExceptionConnect {
        ResetPasswordRequestType parameters = new ResetPasswordRequestType();
        parameters.setEmail("POLI@KOLs.FR");
        parameters.setLogin("jpolino");
        parameters.setPassword("mik");
        assertFalse(connectService.resetPassword(parameters).isReturn());
    }


   /* @Test
    void checkAdmin() {
        CheckA
    }*/
}