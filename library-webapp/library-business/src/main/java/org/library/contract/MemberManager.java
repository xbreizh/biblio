package org.library.contract;


import org.library.model.Member;
import org.springframework.security.core.context.SecurityContext;

public interface MemberManager {


    public Member getMember(String token, String login);
}
