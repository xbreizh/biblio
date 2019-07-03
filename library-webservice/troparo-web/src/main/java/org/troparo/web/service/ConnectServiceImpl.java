package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.connect.*;
import org.troparo.services.connectservice.IConnectService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;

@Named
@WebService(serviceName = "ConnectService", endpointInterface = "org.troparo.services.connectservice.IConnectService",
        targetNamespace = "http://troparo.org/services/ConnectService/", portName = "ConnectServicePort", name = "ConnectServiceImpl")
public class ConnectServiceImpl implements IConnectService {
    private Logger logger = Logger.getLogger(ConnectServiceImpl.class);

    @Inject
    private MemberManager memberManager;


    @Override
    public InvalidateTokenResponseType invalidateToken(InvalidateTokenRequestType parameters) {
        InvalidateTokenResponseType ar = new InvalidateTokenResponseType();
        ar.setReturn(memberManager.invalidateToken(parameters.getToken()));
        return ar;
    }

    @Override
    public GetTokenResponseType getToken(GetTokenRequestType parameters) {
        GetTokenResponseType ar = new GetTokenResponseType();
        logger.info("entering get token method");
        logger.info("login: " + parameters.getLogin());
        logger.info("password: " + parameters.getPassword());
        logger.info("mgr: " + memberManager);
        String token = memberManager.getToken(parameters.getLogin(), parameters.getPassword());
        logger.info("token returned: " + token);
        if (token == null) {
            ar.setReturn("something went wrong");
        } else {
            ar.setReturn(token);
        }
        return ar;
    }

    @Override
    public CheckTokenResponseType checkToken(CheckTokenRequestType parameters) {
        CheckTokenResponseType ar = new CheckTokenResponseType();

        ar.setReturn(memberManager.checkToken(parameters.getToken()));
        return ar;
    }

    @Override
    public RequestPasswordResetLinkResponseType requestPasswordResetLink(RequestPasswordResetLinkRequestType parameters) {
        RequestPasswordResetLinkResponseType ar = new RequestPasswordResetLinkResponseType();
        ar.setReturn(memberManager.requestPasswordLink(parameters.getLogin(), parameters.getEmail()));
        return ar;
    }

    @Override
    public ResetPasswordResponseType resetPassword(ResetPasswordRequestType parameters) {
        ResetPasswordResponseType ar = new ResetPasswordResponseType();
        boolean result;
        logger.info("trying to reset pwd");
        logger.info("trying to reset pwd for: " + parameters.getLogin());
        String login = parameters.getLogin();
        String password = parameters.getPassword();
        result = memberManager.resetPassword(login, password);

        ar.setReturn(result);
        return ar;
    }


    boolean checkToken(String token) {

        return memberManager.checkToken(token);
    }


    public void setMemberManager(MemberManager memberManager) {
        this.memberManager = memberManager;
    }
}
