package org.library.business.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.impl.ConnectManagerImpl;
import org.library.business.impl.LoanManagerImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.GetTokenResponseType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;
import org.troparo.services.loanservice.LoanService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConnectManagerImpl.class)
class ConnectManagerImplIntegrationTest {


    private ConnectManagerImpl connectManager;
    private ConnectService connectService;

    @BeforeEach
    void init() {
        connectManager = spy(ConnectManagerImpl.class);
        connectService = mock(ConnectService.class);
        connectManager.setConnectService(connectService);
    }


    @Test
    @DisplayName("should authenticate")
    void authenticate() throws BusinessExceptionConnect {
        String login = "bob";
        String pwd = "pwd123";
        Set<GrantedAuthority> setAuths = new HashSet<>();
        setAuths.add(new SimpleGrantedAuthority("USER"));
        Collection<GrantedAuthority> result = new ArrayList<>(setAuths);
        Authentication auth1 = new UsernamePasswordAuthenticationToken(login, pwd, result);
        IConnectService iConnectService= mock(IConnectService.class);
        GetTokenResponseType responseType = new GetTokenResponseType();
        String token = "dedekfri493494";
        responseType.setReturn(token);
        when(connectService.getConnectServicePort()).thenReturn(iConnectService);
        when(connectService.getConnectServicePort().getToken(any(GetTokenRequestType.class))).thenReturn(responseType);

        assertEquals(token, connectManager.authenticate(auth1).getDetails());
    }



    @Test
    void supports() {
        assertFalse(connectManager.supports(ConnectManagerImpl.class));
    }

    @Test
    void buildUserAuthority() {
        Set<GrantedAuthority> setAuths = new HashSet<>();
        setAuths.add(new SimpleGrantedAuthority("USER"));
        Collection<GrantedAuthority> result = new ArrayList<>(setAuths);

        assertEquals(result, connectManager.buildUserAuthority());


    }
}