/*
package org.troparo.web.service;

import org.troparo.business.contract.MemberManager;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Authentication {

    @Inject
    MemberManager memberManager;

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
*/
