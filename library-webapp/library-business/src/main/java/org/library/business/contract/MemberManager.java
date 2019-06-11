package org.library.business.contract;


import org.library.model.Member;

public interface MemberManager {


    public Member getMember(String token, String login);
}
