package org.mail.impl;

import org.apache.log4j.Logger;
import org.mail.contract.ConnectManager;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.GetTokenResponseType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;

import javax.inject.Named;

@Named
public class ConnectManagerImpl implements ConnectManager {

    private static final Logger logger = Logger.getLogger(ConnectManagerImpl.class);

    private static final String LOGIN = "lokii";
    private static final String PWD = "123";

    private ConnectService connectService;

    @Override
    public String authenticate() throws BusinessExceptionConnect {
        GetTokenRequestType getTokenRequestType = new GetTokenRequestType();
        getTokenRequestType.setLogin(LOGIN);
        getTokenRequestType.setPassword(PWD);

        GetTokenResponseType responseType = getConnectServicePort(connectService).getToken(getTokenRequestType);
        String token = responseType.getReturn();


        if (!token.equals("wrong LOGIN or pwd")) {
            logger.info("token: "+token);
            return token;
        } else {
            return null;


        }

    }

    ConnectService getConnectService() {
        return connectService;
    }

    void setConnectService(ConnectService connectService) {
        this.connectService = connectService;
    }

    IConnectService getConnectServicePort(ConnectService connectService) {
        if (connectService == null) connectService = new ConnectService();
        return connectService.getConnectServicePort();
    }


}
