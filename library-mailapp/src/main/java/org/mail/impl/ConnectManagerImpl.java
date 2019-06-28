package org.mail.impl;

import org.apache.log4j.Logger;
import org.mail.contract.ConnectManager;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.GetTokenResponseType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;

import javax.inject.Named;

@Named
public class ConnectManagerImpl implements ConnectManager {

    private static final Logger logger = Logger.getLogger(ConnectManagerImpl.class);
    private String token;
    private static final String LOGIN = "lokii";
    private static final String PWD = "123";

    /* @Override*/
    public String authenticate() {
        ConnectService cs = new ConnectService();
        GetTokenRequestType t = new GetTokenRequestType();
        t.setLogin(LOGIN);
        t.setPassword(PWD);
        try {
            GetTokenResponseType responseType = cs.getConnectServicePort().getToken(t);
            token = responseType.getReturn();
        } catch (BusinessExceptionConnect businessExceptionConnect) {
            logger.error("issue while trying to get the token");
        }



        if (!token.equals("wrong LOGIN or pwd")) {


            return token;
        } else {
            return null;


        }

    }


}
