package org.mail.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.GetTokenResponseType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConnectManagerImpl.class)
class ConnectManagerImplTest {

    private ConnectManagerImpl connectManager;
    private ConnectService connectService;

    @BeforeEach
    void init() {
        connectManager = spy(ConnectManagerImpl.class);
        connectService = mock(ConnectService.class);
        connectManager.setConnectService(connectService);
    }

    @Test
    @DisplayName("should do stuff")
    void authenticate() throws BusinessExceptionConnect {
        GetTokenResponseType getTokenResponseType = new GetTokenResponseType();
        String feedback = "";
        getTokenResponseType.setReturn(feedback);
        IConnectService iConnectService = mock(IConnectService.class);
        when(connectService.getConnectServicePort()).thenReturn(iConnectService);
        when(connectManager.getConnectServicePort(connectService).getToken(any(GetTokenRequestType.class))).thenReturn(getTokenResponseType);
        assertEquals(feedback, connectManager.authenticate());
    }

    @Test
    @DisplayName("should return null")
    void authenticate1() throws BusinessExceptionConnect {
        GetTokenResponseType getTokenResponseType = new GetTokenResponseType();
        getTokenResponseType.setReturn("wrong LOGIN or pwd");
        IConnectService iConnectService = mock(IConnectService.class);
        when(connectService.getConnectServicePort()).thenReturn(iConnectService);
        when(connectManager.getConnectServicePort(connectService).getToken(any(GetTokenRequestType.class))).thenReturn(getTokenResponseType);
        assertNull( connectManager.authenticate());
    }

    @Test
    @DisplayName("should set connectService")
    void setConnectService(){
        ConnectService connectService1 = new ConnectService();
        connectManager.setConnectService(connectService1);
        assertEquals(connectService1, connectManager.getConnectService());
    }

    @Test
    @DisplayName("should return IConnectService")
    void getConnectServicePort(){
        ConnectService connectService2 = new ConnectService();
        assertNotNull(connectManager.getConnectServicePort(connectService2));
    }


}
