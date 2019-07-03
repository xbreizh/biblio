package org.mail.impl;

import org.mail.contract.ConnectManager;
import org.springframework.context.annotation.PropertySource;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.GetTokenResponseType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;
import org.troparo.services.connectservice.IConnectService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@PropertySource("classpath:mail.properties")
public class ConnectManagerImpl implements ConnectManager {



    public PropertiesLoad getPropertiesLoad() {
        return propertiesLoad;
    }

    public void setPropertiesLoad(PropertiesLoad propertiesLoad) {
        this.propertiesLoad = propertiesLoad;
    }

    @Inject
    PropertiesLoad propertiesLoad;

    private ConnectService connectService;

    @Override
    public String authenticate() throws BusinessExceptionConnect {
        GetTokenRequestType getTokenRequestType = new GetTokenRequestType();
        getTokenRequestType.setLogin(propertiesLoad.getProperty("login"));
        getTokenRequestType.setPassword(propertiesLoad.getProperty("passwordApp"));

        GetTokenResponseType responseType = getConnectServicePort(connectService).getToken(getTokenRequestType);
        String token = responseType.getReturn();


        if (!token.equals("wrong LOGIN or pwd")) {
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
