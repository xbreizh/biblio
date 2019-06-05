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


    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public void setValidator(EmailValidator validator) {
        this.validator = validator;
    }

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
        exception = checkValidityOfParametersForMember(member);
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


    String checkValidityOfParametersForMember(Member member) {

        if (member.getLogin().length() < 5 || member.getLogin().length() > 20) {
            return  "Login must be 5 or 20 characters: " + member.getLogin();
        }
        if (member.getFirstName().length() < 2 || member.getFirstName().length() > 50) {
            return  "FirstName should have between 2 and 200 characters: " + member.getFirstName();
        }
        if (member.getLastName().length() < 2 || member.getLastName().length() > 50) {
            return "LastName should have between 2 and 200 characters: " + member.getLastName();
        }
        if (member.getPassword().length() < 2 || member.getPassword().length() > 200) {
            return  "Password should have between 2 and 200 characters: " + member.getPassword();
        }
        if (validateEmail(member)) return  "Invalid Email: " + member.getEmail();
        System.out.println("email validation: "+member.getEmail());

        return "";
    }

    public boolean validateEmail(Member member) {
        System.out.println("mail: "+member.getEmail());
        if (!validator.validate(member.getEmail())) {
            return true;
        }
        return false;
    }

    String checkRequiredValuesNotNull(Member member) {
        System.out.println("inda");
        String login = member.getLogin();
        if (member.getLogin() != null) {
            if (!login.equals("") && !login.equals("?") ) {

            }
            else return "login should be filled";
        }else return "login should be filled";
       /* if(member.getLogin() == null) {
            if (!checkIfLoginHasBeenPassed(member.getLogin())) return "login should be filled";
        }*/
        if (member.getFirstName() != null) {
            if (member.getFirstName().equals("") || member.getFirstName().equals("?")) {
                return "FirstName should be filled";
            }
        } else {
            return "FirstName should be filled";
        }

        if (member.getLastName() != null) {
            if (member.getLastName().equals("") || member.getLastName().equals("?") || member.getLastName().isEmpty()) {
                return "LastName should be filled";
            }
        } else {
            return "LastName should be filled";
        }
        if (member.getEmail() != null) {
            if (member.getEmail().equals("") || member.getEmail().equals("?") || member.getEmail().isEmpty()) {
                return "Email should be filled";
            }
        } else {
            return "Email should be filled";
        }

        if (member.getPassword() != null) {
            if (member.getPassword().equals("") || member.getPassword().equals("?") || member.getPassword().isEmpty()) {
                return "Password should be filled";
            }
        } else {
            return "Password should be filled";
        }

       /* if (checkIfLoginHasBeenPassed(member.getPassword())) return "Password should be filled";

        if (checkIfLoginHasBeenPassed(member.getEmail())) return "Email should be filled";*/
        return "";
    }

  /*  protected boolean checkIfLoginHasBeenPassed(Member member) {
        String login = member.getLogin();
        if (member.getLogin() != null) {
            if (!login.equals("") && !login.equals("?") ) {
                return true;
            }
        }
        return false;
    }*/

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
        System.out.println("entering");
        exception = "";/*
        boolean receivedCriteria = false;*/
        // checking that all values are provided
        exception = checkRequiredValuesNotNull(member);
        if (!exception.equals("")) {
            return exception;
        }
        System.out.println("here");
       /* if (member.getLogin().equals("") || member.getLogin().equals("?")) {
            return "you must provide an Login";
        } else {
            logger.info("member received: " + member);
        }*/
        // checking that all values are valid
       // exception = checkValidityOfParametersForMember(member);
        exception = checkValidityOfParametersForMember(member);
        if (!exception.equals("")) {
            return exception;
        }
        System.out.println("there");


        List<Member> loginList = new ArrayList<>();
       /* HashMap<String, String> map = new HashMap<>();
        map.put("Login", member.getLogin());*/
        System.out.println("youmo");
        if (memberDAO.getMemberByLogin(member.getLogin()) != null) {
            return "No Item found with that Login";
        }

        logger.info("getting list: " + loginList.size());
        if(loginList.size()>1){
            logger.error("possible duplication issue in the database");
            return "the member couldn't be updated at this time. Please contact the administrator";
        }
        if(loginList.isEmpty())return "the member couldn't be updated at this time. Please contact the administrator";
        Member m =loginList.get(0);
        // checking that all values are valid

        /*for (Member m : loginList
        ) {*/
            /*if (!m.getFirstName().equals("") && !m.getFirstName().equals("?")) {
                if (m.getFirstName().length() < 2 || m.getFirstName().length() > 50) {
                    return exception = "FirstName should have between 2 and 200 characters: " + m.getFirstName();
                }
                receivedCriteria = true;
                m.setFirstName(m.getFirstName());
            }
            if (!m.getLastName().equals("") && !m.getLastName().equals("?")) {
                if (m.getLastName().length() < 2 || m.getLastName().length() > 50) {
                    return exception = "LastName should have between 2 and 200 characters: " + m.getLastName();
                }
                receivedCriteria = true;
                m.setLastName(m.getLastName());
            }
            if (!m.getPassword().equals("") && !m.getPassword().equals("?")) {
                if (m.getPassword().length() < 2 || m.getPassword().length() > 200) {
                    return exception = "Password should have between 2 and 200 characters: " + m.getPassword();
                }
                receivedCriteria = true;
                m.setPassword(encryptPassword(m.getPassword())); // encrypting password
            }
            if (!m.getEmail().equals("") && !m.getEmail().equals("?")) {
                if (validateEmail(m)) return exception = "Invalid Email: " + m.getEmail();
                receivedCriteria = true;
                m.setEmail(m.getEmail());
            }
            if (!receivedCriteria) {
                return "No criteria was passed in";
            }*/
        // checking that all values are valid
        /*    exception = checkValidityOfParametersForMember(member);
            if (!exception.equals("")) {
                return exception;
            }
            logger.info(m.getLogin());

        }*/
        memberDAO.updateMember(m);
        logger.info("updated: " + m.getId());
        return "";
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
        System.out.println("getting here");
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

/*    @Override
    public boolean disconnect(String token) {
        return false;
    }

    @Override
    public boolean connect(String login, String password) {
        return false;
    }*/

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
        System.out.println("here");
        logger.info("member received: " + login);
        logger.info("email received: " + email);
        logger.info("pwd received: " + password);
        if (login == null || email == null || password == null) return false;

        Member m = getMemberByLogin(login.toUpperCase());
        if (m != null) {
            System.out.println(m.getEmail() + " / " + email);
            if (m.getEmail().equalsIgnoreCase(email)) {
                System.out.println("member not null");
                logger.info("email passed: " + m.getEmail());
                m.setPassword(encryptPassword(password));
                if (!memberDAO.updateMember(m)) return false;
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
            if (m.getRole() != null) {
                return m.getRole().equals("Admin");
            }
        }
        return false;
    }

    String generateToken() {
        boolean tokenValid = false;
        String uuid = null;
        while (!tokenValid) {
            uuid = UUID.randomUUID().toString();
            System.out.println("generating token: " + uuid);
            // checks if token already in use
            if (memberDAO.getMemberByToken(uuid) == null) {
                System.out.println("here");
                tokenValid = true;
            }
            System.out.println("token created: " + uuid);
        }
        return uuid;
    }

}
