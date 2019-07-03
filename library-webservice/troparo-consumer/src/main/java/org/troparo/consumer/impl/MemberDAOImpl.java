package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.MemberDAO;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named("memberDAO")
public class MemberDAOImpl implements MemberDAO {
    private static final String LOGIN = "login";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String ROLE = "role";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";
    private static Logger logger = Logger.getLogger(MemberDAOImpl.class.getName());
    private Class cl = Member.class;
    private String request;
    @Inject
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean addMember(Member member) {
        logger.info("Member from dao: " + member);
        try {
            sessionFactory.getCurrentSession().flush();
            sessionFactory.getCurrentSession().persist(member);
        } catch (Exception e) {
            logger.error("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }


    @Override
    public List<Member> getAllMembers() {
        List<Member> memberList = new ArrayList<>();
        logger.info("getting in dao");
        try {
            logger.info(sessionFactory);
            return sessionFactory.getCurrentSession().createQuery("from Member", cl).getResultList();
        } catch (Exception e) {
            logger.error("SessionFactory possibly not initialized!");
        }
        return memberList;

    }


    @Override
    public Member getMemberById(int id) {
        logger.info("id passed: " + id);
        request = "From Member where id = :id";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("id", id);
        try {
            return (Member) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public boolean existingLogin(String login) {
        logger.info("login passed " + login);
        login = login.toUpperCase();
        request = "From Member where login = :login";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter(LOGIN, login);
        if (!query.getResultList().isEmpty()) {
            logger.info("records found: " + query.getResultList().size());
            return true;
        } else {
            logger.info("no record found for that login: " + login);
            return false;
        }
    }

    @Override
    public List<Member> getMembersByCriterias(Map<String, String> map) {
        List<Member> memberList = new ArrayList<>();
        if (map == null) return new ArrayList<>();
        cleanInvaliMapEntries(map);
        if (map.size() == 0) return new ArrayList<>();
        logger.info("map received in DAO: " + map);
        StringBuilder criteria = new StringBuilder();
        BookDAOImpl.createRequestFromMap(map, criteria);
        request = "From Member ";
        request += criteria;
        logger.info("request: " + request);
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
            for (Map.Entry<String, String> entry : map.entrySet()
            ) {
                logger.info("criteria: " + entry.getValue());
                query.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
            }

            logger.info("list with criteria size: " + query.getResultList().size());
            return query.getResultList();
        } catch (Exception e) {
            return memberList;
        }
    }

    private Map<String, String> cleanInvaliMapEntries(Map<String, String> map) {
        String[] authorizedCriteria = {LOGIN, FIRSTNAME, LASTNAME, ROLE, EMAIL};
        List<String> list = Arrays.asList(authorizedCriteria);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!list.contains(entry.getKey().toLowerCase()))
                map.remove(entry.getKey());
        }
        return map;
    }

    @Override
    public boolean updateMember(Member member) {
        logger.info("Member from dao: " + member.getLogin());
        try {
            sessionFactory.getCurrentSession().update(member);
        } catch (Exception e) {
            logger.error("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Member member) {
        logger.info("Trying to delete" + member);
        try {
            sessionFactory.getCurrentSession().delete(member);
        } catch (Exception e) {
            logger.error("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }


    @Override
    public boolean invalidateToken(String token) {
        logger.info("token received: " + token);
        try {
            Member m = getMemberByToken(token);
            if (m == null) return false;
            m.setToken(null);
            updateMember(m);
        } catch (Exception e) {
            logger.error("issue while invalidating the token");
            return false;
        }
        return true;
    }


    @Override
    public Member getMemberByLogin(String login) {
        logger.info("login received(from DAO): " + login);
        logger.info("session: " + sessionFactory);
        request = "From Member where login = :login";
        logger.info("login received: " + login);
        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter(LOGIN, login);
        logger.info("query: " + query);
        try {
            return (Member) query.getResultList().get(0);
        } catch (Exception e) {
            logger.info("returning null");
            return null;
        }
    }

    @Override
    public Member getMemberByToken(String token) {
        logger.info("token: " + token);
        request = "From Member where token = :token";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter(TOKEN, token);
        try {
            return (Member) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
