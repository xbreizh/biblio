package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.troparo.business.EmailValidator;
import org.troparo.business.contract.MemberManager;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Named
public class MemberManagerImpl implements MemberManager {
    private final String pepper = "Tipiak";
    @Inject
    MemberDAO memberDAO;
    @Inject
    EmailValidator validator;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String exception = "";

    @Override
    public String addMember(Member member) {
        exception = "";
        // checking if already existing
        if (memberDAO.existingLogin(member.getLogin())) {
            exception = "Login already existing";
            return exception;
        }
        // checking that all values are provided
        exception = checkRequiredValuesNotNull(member);
        if (!exception.equals("")) {
            return exception;
        }

        // checking that all values are valid
        exception = checkInsertion(member);
        if (!exception.equals("")) {
            return exception;
        }
        member.setPassword(encryptPassword(member.getPassword())); // encrypting password
        // setting dateJoin
        member.setDateJoin(new Date());
        memberDAO.addMember(member);
        logger.info("exception: " + exception);
        return exception;
    }


    private String checkInsertion(Member member) {
        if (member.getLogin().length() < 5 || member.getLogin().length() > 20) {
            return exception = "Login must be 5 or 20 characters: " + member.getLogin();
        }
        if (member.getFirstName().length() < 2 || member.getFirstName().length() > 50) {
            return exception = "FirstName should have between 2 and 200 characters: " + member.getFirstName();
        }
        if (member.getLastName().length() < 2 || member.getLastName().length() > 50) {
            return exception = "LastName should have between 2 and 200 characters: " + member.getLastName();
        }
        if (member.getPassword().length() < 2 || member.getPassword().length() > 200) {
            return exception = "Password should have between 2 and 200 characters: " + member.getPassword();
        }
        if (!validator.validate(member.getEmail())) {
            return exception = "Invalid Email: " + member.getEmail();
        }

        return exception;
    }

    private String checkRequiredValuesNotNull(Member member) {

        if (member.getLogin().equals("") || member.getLogin().equals("?")) {
            return "login should be filled";
        }
        if (member.getFirstName().equals("") || member.getFirstName().equals("?")) {
            return "FirstName should be filled";
        }
        if (member.getLastName().equals("") || member.getLastName().equals("?")) {
            return "LastName should be filled";
        }
        if (member.getPassword().equals("") || member.getPassword().equals("?")) {
            return "Password should be filled";
        }
        if (member.getEmail().equals("") || member.getEmail().equals("?")) {
            return "Email should be filled";
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
        Member member = memberDAO.getMemberByLogin(login);
        if (member != null) {
            logger.info("member");
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
        exception = "";
        boolean receivedCriteria = false;
        if (member.getLogin().equals("") || member.getLogin().equals("?")) {
            return "you must provide an Login";
        } else {
            logger.info("member received: " + member);
        }

        List<Member> loginList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("Login", member.getLogin());
        logger.info("map size: " + map.size());
        loginList = memberDAO.getMembersByCriterias(map);
        if (loginList.size() == 0) {
            return "No Item found with that Login";
        }
        logger.info("getting list: " + loginList.size());
        for (Member b : loginList
        ) {
            if (!member.getFirstName().equals("") && !member.getFirstName().equals("?")) {
                if (member.getFirstName().length() < 2 || member.getFirstName().length() > 50) {
                    return exception = "FirstName should have between 2 and 200 characters: " + member.getFirstName();
                }
                receivedCriteria = true;
                b.setFirstName(member.getFirstName());
            }
            if (!member.getLastName().equals("") && !member.getLastName().equals("?")) {
                if (member.getLastName().length() < 2 || member.getLastName().length() > 50) {
                    return exception = "LastName should have between 2 and 200 characters: " + member.getLastName();
                }
                receivedCriteria = true;
                b.setLastName(member.getLastName());
            }
            if (!member.getPassword().equals("") && !member.getPassword().equals("?")) {
                if (member.getPassword().length() < 2 || member.getPassword().length() > 200) {
                    return exception = "Password should have between 2 and 200 characters: " + member.getPassword();
                }
                receivedCriteria = true;
                b.setPassword(encryptPassword(member.getPassword())); // encrypting password
            }
            if (!member.getEmail().equals("") && !member.getEmail().equals("?")) {
                if (!validator.validate(member.getEmail())) {
                    return exception = "Invalid Email: " + member.getEmail();
                }
                receivedCriteria = true;
                b.setEmail(member.getEmail());
            }
            if (!receivedCriteria) {
                return "No criteria was passed in";
            }
            logger.info(b.getLogin());
            memberDAO.updateMember(b);
            logger.info("updated: " + b.getId());
        }

        return exception;
    }

    @Override
    public String remove(int id) {
        Member login = memberDAO.getMemberById(id);

        if (login == null) {
            return exception = "No item found";
        } else {
            memberDAO.remove(login);
        }
        return exception;
    }




    /*Login*/

    // Login
    @Override
    public String getToken(String login, String password) {
        Member m;
        try {
            logger.info("trying to get token from business");
            m = getMemberByLogin(login.toUpperCase());
            // checking password match
            if (m != null) {
                if (checkPassword(password, m.getPassword())) {
                    String token = generateToken();
                    m.setToken(token);
                    m.setDateConnect(new Date());
                    memberDAO.updateMember(m);
                    return token;
                } else {
                    logger.info("no token");
                }
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
        try {
            Member m = memberDAO.getMemberByToken(token);
            m.setToken(null);
            memberDAO.updateMember(m);
            return true;
        } catch (Exception e) {
            logger.error("issue while invalidating the token: " + token);
            return false;
        }
    }

    @Override
    public boolean disconnect(String token) {
        return false;
    }

    @Override
    public boolean connect(String login, String password) {
        return false;
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
        logger.info("member received: " + login);
        logger.info("email received: " + email);
        logger.info("pwd received: " + password);

        if (getMemberByLogin(login.toUpperCase()) != null) {
            Member m = getMemberByLogin(login.toUpperCase());
            if (m.getEmail().toUpperCase().equals(email.toUpperCase())) {
                logger.info("email passed: " + m.getEmail());
                m.setPassword(encryptPassword(password));
                memberDAO.updateMember(m);
                return true;
            }
        } else {
            logger.info("member couldn't be found");
        }
        return false;
    }

    @Override
    public boolean checkAdmin(String token) {
        Member m = memberDAO.getMemberByToken(token);
        if (m != null) {
            return m.getRole().equals("Admin");
        }
        return false;
    }

    private String generateToken() {
        boolean tokenValid = false;
        String uuid = null;
        while (!tokenValid) {
            uuid = UUID.randomUUID().toString();
            // checks if token already in use
            if (memberDAO.getMemberByToken(uuid) == null) {
                tokenValid = true;
            }
            System.out.println("token created: " + uuid);
        }
        return uuid;
    }

}
