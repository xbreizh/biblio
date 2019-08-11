package org.library.business.impl;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.GetTokenResponseType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;

import javax.inject.Named;
import javax.xml.ws.WebServiceException;
import java.util.Arrays;
import java.util.List;

@Named
public class ConnectManagerImpl implements AuthenticationProvider {

    private static final String ROLE = "USER";
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private ConnectService connectService;


    @Override
    public UsernamePasswordAuthenticationToken authenticate(Authentication authentication) {
        String token = "";
        logger.info(authentication.getPrincipal().toString());
        GetTokenRequestType getTokenRequestType = new GetTokenRequestType();
        String login = authentication.getName().toUpperCase();
        String password = (String) authentication.getCredentials();
        getTokenRequestType.setLogin(login);
        getTokenRequestType.setPassword(password);
        logger.info("login: " + login + " \n passwordd: " + password);
        String exception = "";
        GetTokenResponseType responseType;
        try {
            responseType = getConnectServicePort().getToken(getTokenRequestType);
            token = responseType.getReturn();
        } catch (WebServiceException e) {
            exception = "issue connecting to remote API: " + e.getMessage();
            logger.error(exception);

        } catch (BusinessExceptionConnect businessExceptionConnect) {
            exception = "issue while trying to get the token";

        }

        logger.info("token found: " + token);

        if (!token.equals("wrong credentials") && exception.isEmpty()) {

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(login, token, buildUserAuthority());

            logger.info("trucko: " + auth.getAuthorities());
            logger.info("cred: " + auth.getCredentials());
            logger.info("login: " + auth.getName());

            auth.setDetails(token);
            return auth;
        } else {
            throw new
                    BadCredentialsException("External system authentication failed");

        }

    }


    private IConnectService getConnectServicePort() {
        if (connectService == null) connectService = new ConnectService();
        return connectService.getConnectServicePort();
    }


    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }


    public List<GrantedAuthority> buildUserAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority(ROLE));
    }


    public void setConnectService(ConnectService connectService) {
        this.connectService = connectService;
    }
}
