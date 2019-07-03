package org.troparo.consumer.contract;

import org.troparo.model.Member;

import java.util.List;
import java.util.Map;


public interface MemberDAO {

    List<Member> getAllMembers();

    boolean addMember(Member member);

    Member getMemberById(int id);

    Member getMemberByLogin(String login);

    Member getMemberByToken(String token);

    List<Member> getMembersByCriterias(Map<String, String> map);

    boolean existingLogin(String login);

    boolean updateMember(Member member);

    boolean remove(Member member);


  /*  boolean checkToken(String token);*/

    boolean invalidateToken(String token);
}