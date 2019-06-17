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
        exception = checkValidityOfParametersForInsertMember(member);
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


    String checkValidityOfParametersForInsertMember(Member member) {
        if(member==null)return "no member provided";

        if(member.getLogin()==null)return "No login provided";
        if (member.getLogin().equals("")||member.getLogin().equals("?"))return "No login provided";
        if (member.getLogin().length() < 5 || member.getLogin().length() > 20) {
            return  "Login must be 5 or 20 characters: " + member.getLogin();
        }
        if(member.getFirstName()==null)return "No firstname provided";
        if (member.getFirstName().equals("")||member.getFirstName().equals("?"))return "No firstname provided";
        if (member.getFirstName().length() < 2 || member.getFirstName().length() > 50) {
            return  "FirstName should have between 2 and 200 characters: " + member.getFirstName();
        }
        if(member.getLastName()==null)return "No lastname provided";
        if (member.getLastName().equals("")||member.getLastName().equals("?"))return "No lastname provided";
        if (member.getLastName().length() < 2 || member.getLastName().length() > 50) {
            return "LastName should have between 2 and 200 characters: " + member.getLastName();
        }
        if(member.getPassword()==null)return "No password provided";
        if (member.getPassword().equals("")||member.getPassword().equals("?"))return "No password provided";
        if (member.getPassword().length() < 2 || member.getPassword().length() > 200) {
            return  "Password should have between 2 and 200 characters: " + member.getPassword();
        }
        if(member.getEmail()==null)return "No email provided";
        if (member.getEmail().equals("")||member.getEmail().equals("?"))return "No email provided";
        if (!validator.validate(member.getEmail())) return  "Invalid Email: " + member.getEmail();
        //logger.info("email validation: "+member.getEmail());

        return "";
    }


    String checkValidityOfParametersForUpdateMember( Member memberNewValues) {
        if(memberNewValues==null)return "no member provided";
        int nbValuesToUpdate = 0;
        String login = memberNewValues.getLogin();
        logger.info("login: "+login);
        if(login==null)return "No login provided";
        if (login.equals("")||login.equals("?"))return "No login provided";
        if (login.length() < 5 || login.length() > 20) {
            return  "Login must be 5 or 20 characters: " +login;
        }
        String firstname = memberNewValues.getFirstName();
        if(firstname!=null){
            if(!firstname.equals("")&&!firstname.equals("?")) {
                if (firstname.length() < 2 || firstname.length() > 50) {
                    return "FirstName should have between 2 and 200 characters: " + firstname;
                }else{
                    nbValuesToUpdate++;
                }
            }
            }
        String lastname = memberNewValues.getLastName();
        if(lastname!=null) {
            if(!lastname.equals("")&&!lastname.equals("?")) {
                if (lastname.length() < 2 || lastname.length() > 50) {
                    return "LastName should have between 2 and 200 characters: " + lastname;
                }else{
                    nbValuesToUpdate++;
                }
            }
        }
        String password = memberNewValues.getPassword();
        if(password!=null) {
            if(!password.equals("")&&!password.equals("?")) {
                if (password.length() < 2 || password.length() > 200) {
                    return "Password should have between 2 and 200 characters: " + password;
                }else{
                    nbValuesToUpdate++;
                }
            }
        }
        String email = memberNewValues.getEmail();
        if(email!=null) {
            if (!email.equals("") && !email.equals("?")) {
                if (!validator.validate(email)){
                    return "Invalid Email: " + email;
                }else{
                    nbValuesToUpdate++;
                }
            }
        }
        String role = memberNewValues.getRole();
        if(role!=null){
            if(!role.equals("")&&!role.equals("?")){
                if (role.length() < 6 || role.length() > 10) {
                    return  "Role should have between 6 and 10 characters: " + role;
                }else{
                    nbValuesToUpdate++;
                }
            }
        }
        logger.info("nb values: "+nbValuesToUpdate);
        if(nbValuesToUpdate==0)return "There is no value to be updated";
        return "";
    }


    String checkRequiredValuesNotNull(Member member) {
        String login = member.getLogin();
        if (member.getLogin() != null) {
            if (!login.equals("") && !login.equals("?") ) {

            }
            else return "login should be filled";
        }else return "login should be filled";

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
        logger.info("member returned: "+member);
        if (member != null) {
            logger.info("member: "+member);
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
        exception = "";
        if(member==null)return "No member passed";
        String login = member.getLogin();

        Member memberFromDatabase = memberDAO.getMemberByLogin(login);
        if(memberFromDatabase==null)return "No member found with that login";
        logger.info("reaching here");
        exception = checkValidityOfParametersForUpdateMember(member);
        if (!exception.equals("")) {
            logger.info("ex "+exception);
            return exception;
        }
        Member memberToTestIfAnyChange = memberFromDatabase;
        memberFromDatabase = transfertUpdatedDetails(member, memberFromDatabase);
        if(memberFromDatabase==memberToTestIfAnyChange)return "nothing to update";

        memberDAO.updateMember(memberFromDatabase);
        logger.info("updated: " + memberFromDatabase.getId());
        return "";
    }

    Member transfertUpdatedDetails(Member newMember, Member memberFromDatabase) {
        String firstname = newMember.getFirstName();
        String lastname = newMember.getLastName();
        String password = newMember.getPassword();
        String email = newMember.getEmail();
        String role = newMember.getRole();
        if(firstname!=null){
            if(!firstname.equals("")&&!firstname.equals("?"))memberFromDatabase.setFirstName(firstname);
        }
        if(lastname!=null){
            if(!lastname.equals("")&&!lastname.equals("?"))memberFromDatabase.setLastName(lastname);
        }
        if(password!=null){
            if(!password.equals("")&&!password.equals("?"))memberFromDatabase.setPassword(password);
        }
        if(email!=null){
            if(!email.equals("")&&!email.equals("?"))memberFromDatabase.setEmail(email);
        }
        if(role!=null){
            if(!role.equals("")&&!role.equals("?"))memberFromDatabase.setRole(role);
        }
        return memberFromDatabase;
    }



    private boolean checkIfAnyvaluePassed(Member member) {
        String[] paramList = {member.getLogin(), member.getFirstName(),member.getLastName(), member.getEmail(),member.getPassword(),member.getRole()};
        int nbElement=0;
        for (String str: paramList
             ) {
            if (str != null) {
                if (!str.equals("") && !str.equals("?")) nbElement++;
            }
        }
        if(nbElement!=0)return true;
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
        Member m;
        try {
            logger.info("trying to get token from business");
            m = getMemberByLogin(login.toUpperCase());
            // checking password match
            logger.info("member found: "+m);
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
            }else{
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
