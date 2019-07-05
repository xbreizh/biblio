package org.library.business.contract;


import org.library.model.Member;
import org.troparo.services.connectservice.BusinessExceptionConnect;

public interface MemberManager {


    public Member getMember(String token, String login);

    boolean resetPassword(String login, String password, String token) throws BusinessExceptionConnect;

    boolean sendResetPasswordlink(String login, String email) throws BusinessExceptionConnect;
}
