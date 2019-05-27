package org.troparo.business.contract;

import org.troparo.model.Member;

import java.util.HashMap;
import java.util.List;


public interface MemberManager {

    String addMember(Member member);

    List<Member> getMembers();

    Member getMemberById(int id);

    Member getMemberByLogin(String login);

    List<Member> getMembersByCriterias(HashMap<String, String> map);

    String updateMember(Member member);

    String remove(int id);


    //Connection

    String getToken(String login, String password);

    boolean connect(String login, String password);

    boolean checkToken(String token);

    boolean invalidateToken(String token);

    boolean disconnect(String token);

    String encryptPassword(String password);

    boolean checkPassword(String pwd1, String pwd2);

    boolean updatePassword(String login, String email, String password);

    boolean checkAdmin(String token);

}