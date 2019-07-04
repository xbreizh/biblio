package org.library.business.contract;


import org.library.model.Member;

public interface MemberManager {


    public Member getMember(String token, String login);

    boolean resetPassword(String login, String password, String token);
}
