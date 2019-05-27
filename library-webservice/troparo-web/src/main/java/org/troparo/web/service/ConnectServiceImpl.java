package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.connect.*;
import org.troparo.entities.member.MemberListType;
import org.troparo.entities.member.MemberTypeIn;
import org.troparo.entities.member.MemberTypeOut;
import org.troparo.model.Member;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.IConnectService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@Named
@WebService(serviceName = "ConnectService", endpointInterface = "org.troparo.services.connectservice.IConnectService",
        targetNamespace = "http://troparo.org/services/ConnectService/", portName = "ConnectServicePort", name = "ConnectServiceImpl")
public class ConnectServiceImpl implements IConnectService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private MemberManager memberManager;


    /*@Inject
    private Authentication authentication;*/

    private String exception = "";
    private List<Member> memberList = new ArrayList<>();
    private MemberTypeOut memberTypeOut = null;
    private MemberTypeIn memberTypeIn = null;
    private MemberListType memberListType = new MemberListType();
    private Member member = null;


    private void checkAuthentication(String token) throws BusinessExceptionConnect {
        try {
            checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionConnect("invalid token");
        }
    }

    @Override
    public InvalidateTokenResponseType invalidateToken(InvalidateTokenRequestType parameters) throws BusinessExceptionConnect {
        InvalidateTokenResponseType ar = new InvalidateTokenResponseType();
        ar.setReturn(memberManager.invalidateToken(parameters.getToken()));
        return ar;
    }

    @Override
    public GetTokenResponseType getToken(GetTokenRequestType parameters) throws BusinessExceptionConnect {
        GetTokenResponseType ar = new GetTokenResponseType();
        logger.info("entering get token method");
        logger.info("login: " + parameters.getLogin());
        logger.info("password: " + parameters.getPassword());
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
    public CheckTokenResponseType checkToken(CheckTokenRequestType parameters) throws BusinessExceptionConnect {
        CheckTokenResponseType ar = new CheckTokenResponseType();
        boolean tokenIsValid = false;
        tokenIsValid = memberManager.checkToken(parameters.getToken());
        ar.setReturn(tokenIsValid);
        return ar;
    }

    @Override
    public ResetPasswordResponseType resetPassword(ResetPasswordRequestType parameters) throws BusinessExceptionConnect {
        ResetPasswordResponseType ar = new ResetPasswordResponseType();
        boolean result;

        logger.info("trying to reset pwd for: " + parameters.getLogin());
        String login = parameters.getLogin();
        String password = parameters.getPassword();
        String email = parameters.getEmail();
        result = memberManager.updatePassword(login, email, password);

        ar.setReturn(result);
        return ar;
    }


    boolean checkToken(String token) throws Exception {
        if (!memberManager.checkToken(token)) {
            throw new Exception("invalid token");
        }
        return true;
    }

    boolean checkAdmin(String token) throws Exception {
        checkToken(token);
        if (!memberManager.checkAdmin(token)) {
            throw new Exception("insufficient rights");
        }
        return true;
    }
}
