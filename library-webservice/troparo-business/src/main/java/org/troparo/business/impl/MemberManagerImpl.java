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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
        System.out.println("login: "+member.getLogin());
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
    public List<Member> getMembersByCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = new HashMap<>();
        for (HashMap.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("?") && !entry.getValue().equals("")) {
                criterias.put(entry.getKey(), entry.getValue());
            }
        }
        logger.info("criterias: " + criterias);
        return memberDAO.getMembersByCriterias(criterias);
    }


    @Override
    public String updateMember(Member member) {
        logger.info("entering");
        String exception;
        if (member == null) return "No member passed";
        String login = member.getLogin();

        Member memberFromDatabase = memberDAO.getMemberByLogin(login);
        if (memberFromDatabase == null) return "No member found with that login";
        logger.info("reaching here");
        exception = checkValidityOfParametersForUpdateMember(member);
        if (!exception.equals("")) {
            logger.info("ex " + exception);
            return exception;
        }
        Member memberToTestIfAnyChange = memberFromDatabase;
        memberFromDatabase = transfertUpdatedDetails(member, memberFromDatabase);
        if (memberFromDatabase == memberToTestIfAnyChange) return "nothing to update";

        memberDAO.updateMember(memberFromDatabase);
        logger.info("updated: " + memberFromDatabase.getId());
        return "";
    }

    Member transfertUpdatedDetails(Member newMember, Member memberFromDatabase) {
        String firstName = newMember.getFirstName();
        String lastName = newMember.getLastName();
        String password = encryptPassword(newMember.getPassword());
        String email = newMember.getEmail();
        String role = newMember.getRole();
        if (firstName != null && !firstName.equals("") && !firstName.equals("?")) {
            memberFromDatabase.setFirstName(firstName);
        }
        if (lastName != null && !lastName.equals("") && !lastName.equals("?")) {
            memberFromDatabase.setLastName(lastName);
        }
        if (password != null && !password.equals(encryptPassword("")) && !password.equals(encryptPassword("?"))) {
            memberFromDatabase.setPassword(password);
        }
        if (email != null && !email.equals("") && !email.equals("?")) {
            memberFromDatabase.setEmail(email);
        }
        if (role != null && !role.equals("") && !role.equals("?")) {
            memberFromDatabase.setRole(role);
        }
        return memberFromDatabase;
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
        Member m;
        try {
            logger.info("trying to get token from business");
            m = getMemberByLogin(login.toUpperCase());
            // checking password match
            logger.info("member found: " + m);
            logger.info(login);
            logger.info(password);
            if (m != null) {
                if (checkPassword(password, m.getPassword())) {
                    logger.info("indere");
                    String token = generateToken();
                    m.setToken(token);
                    m.setDateConnect(new Date());
                    memberDAO.updateMember(m);
                    return token;
                } else {
                    logger.info("no token");
                }
            } else {
                logger.info("member is null");
            }
        } catch (NullPointerException e) {
            logger.info("wrong login or pwd");
        }

        return "wrong login or pwd";
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
            if (!memberDAO.updateMember(m)) return true;
        } catch (Exception e) {
            logger.error("issue while invalidating the token: " + token);
            return false;
        }
        return true;
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
        boolean tokenValid = false;
        String uuid = null;
        while (!tokenValid) {
            uuid = UUID.randomUUID().toString();
            logger.info("generating token: " + uuid);
            // checks if token already in use
            if (memberDAO.getMemberByToken(uuid) == null) {
                logger.info("here");
                tokenValid = true;
            }
            logger.info("token created: " + uuid);
        }
        return uuid;
    }

}
