package org.mail.impl;

import org.apache.log4j.Logger;
import org.mail.contract.ConnectManager;
import org.springframework.context.annotation.PropertySource;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.GetTokenResponseType;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@PropertySource("classpath:mail.properties")
public class ConnectManagerImpl implements ConnectManager {

    private Logger logger = Logger.getLogger(ConnectManagerImpl.class);
    private ConnectService connectService;
    private PropertiesLoad propertiesLoad;



    @Inject
    public ConnectManagerImpl(PropertiesLoad propertiesLoad) {
        this.propertiesLoad = propertiesLoad;
    }


    @Override
    public String authenticate()  {

        GetTokenRequestType getTokenRequestType = new GetTokenRequestType();
        getTokenRequestType.setLogin(propertiesLoad.getProperty("login"));
        getTokenRequestType.setPassword(propertiesLoad.getProperty("passwordApp"));
        String token = null;
        try {
            GetTokenResponseType responseType = getConnectServicePort(connectService).getToken(getTokenRequestType);
            token = responseType.getReturn();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (token != null && !token.equals("wrong LOGIN or pwd")) {
            logger.info("authentication successful!");
            logger.info("token: " + token);
            return token;
        } else {
            logger.error("authentication issue!");
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


    void setPropertiesLoad(PropertiesLoad propertiesLoad) {
        this.propertiesLoad = propertiesLoad;
    }

}
