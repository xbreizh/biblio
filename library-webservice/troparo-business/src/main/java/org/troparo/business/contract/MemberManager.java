package org.troparo.business.contract;

import org.troparo.business.impl.validator.StringValidatorMember;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import java.util.List;
import java.util.Map;


public interface MemberManager {

    String addMember(Member member);

    List<Member> getMembers();

    Member getMemberById(int id);

    Member getMemberByLogin(String login);

    List<Member> getMembersByCriteria(Map<String, String> map);

    String updateMember(Member member);

    String remove(int id);


    //Connection

    String getToken(String login, String password);

    boolean checkToken(String token);

    boolean invalidateToken(String token);

    void setMemberDAO(MemberDAO memberDao);

    void setStringValidatorMember(StringValidatorMember stringValidatorMember);

    String encryptPassword(String password);

    boolean checkPassword(String pwd1, String pwd2);

    boolean resetPassword(String login, String password);

    boolean checkAdmin(String token);

    boolean requestPasswordLink(String login, String email);

    Member getMemberByToken(String token);
}