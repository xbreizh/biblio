package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.validator.StringValidator;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Named
public class MemberManagerImpl implements MemberManager {
    @Value("${pepper}")
    private static String pepper;


    @Inject
    MemberDAO memberDAO;
    @Inject
    StringValidator stringValidator;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public void setStringValidator(StringValidator stringValidator) {
        this.stringValidator = stringValidator;
    }

    @Override
    public String addMember(Member member) {
        String exception;

        exception = checkValidityOfParametersForInsertMember(member);
        if (!exception.equals("")) {
            return exception;
        }
        if (memberDAO.existingLogin(member.getLogin())) {
            return "Login already existing";
        }
        member.setPassword(encryptPassword(member.getPassword())); // encrypting password
        // setting dateJoin
        member.setDateJoin(new Date());
        memberDAO.addMember(member);
        logger.info("exception: " + exception);
        return exception;
    }


    public String checkValidityOfParametersForInsertMember(Member member) {
        if (member == null) return "no member provided";

        String[][] memberParameters = {{"login", member.getLogin()},
                {"firstName", member.getFirstName()},
                {"lastName", member.getLastName()},
                {"password", member.getPassword()},
                {"email", member.getEmail()}};

        for (String[] param : memberParameters) {
            if (!stringValidator.validateExpression(param[0], param[1])) {
                return stringValidator.getException(param[0]) + param[1];
            }
        }

        return "";
    }


    public String checkValidityOfParametersForUpdateMember(Member member) {

        if (member == null) return "no member provided";

        String[][] memberParameters = {
                {"login", member.getLogin()},
                {"firstName", member.getFirstName()},
                {"lastName", member.getLastName()},
                {"password", member.getPassword()},
                {"email", member.getEmail()}};

        for (String[] param : memberParameters) {

            if (!stringValidator.validateForUpdateMember(param[0], param[1])) {
                return stringValidator.getException(param[0]) + param[1];
            }
        }

        return "";
    }


    @Override
    public List<Member> getMembers() {
        return memberDAO.getAllMembers();
    }

    @Override
    public Member getMemberById(int id) {
        logger.info("getting id (from business): " + id);
        Member member = memberDAO.getMemberById(id);
        if (member != null) {
            logger.info("member");
            return member;
        } else {
            logger.info("member is probably null");
            return null;
        }
    }


    @Override
    public Member getMemberByLogin(String login) {
        logger.info("getting id (from business): " + login);
        login = login.toUpperCase();
        Member member = memberDAO.getMemberByLogin(login);
        logger.info("member returned: " + member);
        if (member != null) {
            logger.info("member: " + member);
            return member;
        } else {
            logger.info("member is probably null");
            return null;
        }
    }

    @Override
    public List<Member> getMembersByCriterias(Map<String, String> map) {
        List<Member> memberList = new ArrayList<>();
        if (map == null || map.isEmpty()) return memberList;
        Map<String, String> criterias = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("?") && !entry.getValue().equals("")) {
                criterias.put(entry.getKey(), entry.getValue());
            }
        }
        if (criterias.isEmpty()) return memberList;
        logger.info("criterias: " + criterias);
        return memberDAO.getMembersByCriterias(criterias);
    }


    @Override
    public String updateMember(Member member) {
        logger.info("trying to update member");
        String exception;
        if (member == null) return "No member passed";
        String login = member.getLogin();

        Member memberFromDatabase = memberDAO.getMemberByLogin(login);
        if (memberFromDatabase == null) return "No member found with that login";
        exception = checkValidityOfParametersForUpdateMember(member);
        if (!exception.equals("")) {
            logger.info("ex " + exception);
            return exception;
        }
        Member memberToTestIfAnyChange = memberFromDatabase;
        memberFromDatabase = transfertUpdatedDetails(member, memberFromDatabase);

        memberDAO.updateMember(memberFromDatabase);
        logger.info("updated: " + memberFromDatabase.getId());
        return "";
    }

    public Member transfertUpdatedDetails(Member newMember, Member memberFromDatabase) {
        updateFirstName(memberFromDatabase, newMember);
        updateLastName(memberFromDatabase, newMember);
        updatePassword(memberFromDatabase, newMember);
        updateEmail(memberFromDatabase, newMember);
        updateRole(memberFromDatabase, newMember);
        return memberFromDatabase;
    }

    public boolean updateRole(Member memberFromDatabase, Member newMember) {
        String role = newMember.getRole();
        if (role != null && !role.equals("") && !role.equals("?")) {
            memberFromDatabase.setRole(role);
            return true;
        }
        return false;
    }

    public boolean updateEmail(Member memberFromDatabase, Member newMember) {
        String email = newMember.getEmail();
        if (email != null && !email.equals("") && !email.equals("?")) {
            memberFromDatabase.setEmail(email);
            return true;
        }
        return false;
    }

    public boolean updatePassword(Member memberFromDatabase, Member newMember) {
        String pass = newMember.getPassword();
        if (pass != null && !pass.equals("") && !pass.equals("?")) {
            memberFromDatabase.setPassword(encryptPassword(pass));
            return true;
        }
        return false;
    }

    public boolean updateLastName(Member memberFromDatabase, Member newMember) {
        String lastName = newMember.getLastName();
        if (lastName != null && !lastName.equals("") && !lastName.equals("?")) {
            memberFromDatabase.setLastName(lastName);

            return true;
        }
        return false;
    }

    public boolean updateFirstName(Member memberFromDatabase, Member newMember) {
        String firstName = newMember.getFirstName();
        if (firstName != null && !firstName.equals("") && !firstName.equals("?")) {
            memberFromDatabase.setFirstName(firstName);
            return true;
        }
        return false;
    }


    @Override
    public String remove(int id) {
        Member member = memberDAO.getMemberById(id);

        if (member == null) {
            return "No item found";
        } else {
            if (!memberDAO.remove(member)) return "Issue while removing member";
        }
        return "";
    }


    // Login
    @Override
    public String getToken(String login, String password) {
        String wrongCredentials = "wrong credentials";
        if(login==null || password==null)return wrongCredentials;
        Member member;
        member = getMemberByLogin(login.toUpperCase());
        if (member == null) return wrongCredentials;
        logger.info("trying to get token from business");
        // checking password match
        logger.info("member found: " + member);
        logger.info("login: "+login+" / password: "+password);
            if (checkPassword(password, member.getPassword())) {
                String token = generateToken();
                member.setToken(token);
                member.setDateConnect(new Date());
                memberDAO.updateMember(member);
                return token;
            }


        return wrongCredentials;
    }

    @Override
    public boolean checkToken(String token) {
        return memberDAO.checkToken(token);
    }

    @Override
    public boolean invalidateToken(String token) {
        logger.info("getting here");
        try {
            Member m = memberDAO.getMemberByToken(token);
            m.setToken(null);
            return memberDAO.updateMember(m);
        } catch (Exception e) {
            logger.error("issue while invalidating the token: " + token);
            return false;
        }
    }


    @Override
    public String encryptPassword(String password) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String pwd = bcrypt.encode(password + pepper);
        logger.info("hashed pwd: " + pwd);
        return pwd;
    }



    @Override
    public boolean checkPassword(String pwd1, String pwd2) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.matches(pwd1 + pepper, pwd2);
    }

    // Update Password
    @Override
    public boolean updatePassword(String login, String email, String password) {
        logger.info("here");
        logger.info("member received: " + login);
        logger.info("email received: " + email);
        logger.info("pwd received: " + password);
        if (login == null || email == null || password == null) return false;

        Member m = getMemberByLogin(login.toUpperCase());
        if (m != null) {
            logger.info(m.getEmail() + " / " + email);
            if (m.getEmail().equalsIgnoreCase(email)) {
                logger.info("member not null");
                logger.info("email passed: " + m.getEmail());
                m.setPassword(encryptPassword(password));

                return memberDAO.updateMember(m);
            }

        } else {
            logger.info("member couldn't be found");
        }
        return false;
    }

    @Override
    public boolean checkAdmin(String token) {
        Member m = memberDAO.getMemberByToken(token);
        if (m != null && m.getRole() != null) {
            return m.getRole().equals("Admin");
        }

        return false;
    }

    String generateToken() {
        int nbTries = 0;
        while(nbTries < 5){
            String uuid = createToken();
            logger.info("generating token: " + uuid);
            // checks if token already in use
            if (memberDAO.getMemberByToken(uuid) == null) {
                logger.info("token created: " + uuid);
                return uuid;
            }
            nbTries++;
        }
        logger.error("no valid token could be created");
        return null;
    }

    String createToken() {
        String uuid;
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

}
