package org.library.business.contract;


import org.library.model.Member;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.memberservice.BusinessExceptionMember;

public interface MemberManager {


    Member getMember(String token, String login);

    boolean resetPassword(String login, String password, String token) throws BusinessExceptionConnect;

    boolean sendResetPasswordLink(String login, String email) throws BusinessExceptionConnect;

    boolean switchReminder(String token, String login, boolean reminder) throws BusinessExceptionMember;
}
