package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named("loanDAO")
public class LoanDAOImpl implements LoanDAO {
    private static Logger logger = Logger.getLogger(LoanDAOImpl.class.getName());
    private static String status = "status";
    private static String isbn = "isbn";
    private static String bookId = "bookId";
    private Class cl = Loan.class;
    private String request;
    //private static String login = "login";
    @Inject
    private SessionFactory sessionFactory;


    @Override
    public List<Loan> getLoans() {
        logger.info("getting in dao");
        List<Loan> loanList = new ArrayList<>();
        try {
            return sessionFactory.getCurrentSession().createQuery("From Loan", cl).getResultList();
        } catch (Exception e) {
            return loanList;
        }
    }

    @Override
    public boolean addLoan(Loan loan) {
        try {
            sessionFactory.getCurrentSession().persist(loan);
        } catch (Exception e) {
            logger.error("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean updateLoan(Loan loan) {
        try {
            sessionFactory.getCurrentSession().update(loan);
        } catch (Exception e) {
            logger.error("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Loan getLoanById(int id) {
        logger.info("in the dao: " + id);
        request = "From Loan where id = :id";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("id", id);
        try {
            return (Loan) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Loan> getLoanByIsbn(String isbn) {
        List<Loan> loanList = new ArrayList<>();
        if (isbn == null) return null;
        isbn = isbn.toUpperCase();
        logger.info("in the dao: " + isbn);
        logger.info(isbn);
        request = "From Loan where book.isbn = :isbn";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter(LoanDAOImpl.isbn.toLowerCase(), isbn);
        try {
            return query.getResultList();
        } catch (Exception e) {
            return loanList;
        }
    }

    @Override
    public List<Loan> getLoanByLogin(String login) {
        List<Loan> loanList = new ArrayList<>();
        logger.info("in the dao: " + login);
        login = login.toUpperCase();
        request = "From Loan where borrower.login = :login";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("login", login);
        try {
            List<Loan> list = query.getResultList();
            logger.info("size of list: " + list.size());
            return query.getResultList();
        } catch (Exception e) {
            return loanList;
        }
    }

    @Override
    public List<Loan> getLoansByCriterias(HashMap<String, String> map) {
        System.out.println("in the dao");
        List<Loan> loanList = new ArrayList<>();
        if (map == null || map.isEmpty()) return new ArrayList<>();
        map = cleanInvaliMapEntries(map);
        if (map.size() == 0) return new ArrayList<>();
        logger.info("map received in DAO: " + map);
        String criteria = "";
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getKey().equalsIgnoreCase(LoanDAOImpl.status)) {
                String field="";
                if (!criteria.equals("")) {
                    criteria += " and ";
                } else {
                    criteria += "where ";
                }
                if(entry.getKey().equalsIgnoreCase("login")){
                    field = "borrower.login";
                }
                criteria += field + " = :";
                if (entry.getKey().equalsIgnoreCase("BOOK_ID")) {
                    criteria += "BOOK_ID";
                }
                if (entry.getKey().equalsIgnoreCase("LOGIN")) {
                    criteria += "LOGIN";
                }
                if (entry.getKey().equalsIgnoreCase(LoanDAOImpl.status)) {
                    criteria += "STATUS";
                }
            } else {
                status = entry.getValue();
                logger.info("status has been passed: " + status);
            }

        }

        request = "From Loan ";
        request += criteria;
        logger.info("criteria: " + criteria);
        addStatusToRequest(status, map.size());
        logger.info("request: " + request);
        System.out.println("session: "+sessionFactory.getCurrentSession());
       // String request1 = "FROM Loan where borrower.login= :LOGIN";
        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        logger.info("map again: " + map);
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getKey().equalsIgnoreCase(LoanDAOImpl.status)) {
                logger.info("criteria: " + entry.getValue());
                if (entry.getKey().toUpperCase().contains(isbn)) {
                    query.setParameter(isbn, "%" + entry.getValue().toUpperCase() + "%");
                }
                System.out.println("boko: "+entry.getKey());
                if (entry.getKey().toUpperCase().contains("LOGIN")) {
                    query.setParameter("LOGIN", "%" + entry.getValue().toUpperCase() + "%");

                }
                if (entry.getKey().toUpperCase().contains("BOOK_ID")) {
                    query.setParameter("BOOK_ID", +Integer.parseInt(entry.getValue()));
                }
            }
        }
       query.setParameter("LOGIN", "JPOLINO");
        System.out.println("query: " + query.getQueryString());
        logger.info("map: " + request);
        try {
            logger.info("list with criteria size: " + query.getResultList().size());
            return query.getResultList();
        } catch (Exception e) {
            logger.info("bam l erreur");
            return loanList;
        }

    }

    private HashMap<String, String> cleanInvaliMapEntries(HashMap<String, String> map) {
        String[] authorizedCriterias = {status, "book_id", "login"};
        List<String> list = Arrays.asList(authorizedCriterias);
        System.out.println("map size: "+map.size());
        System.out.println(map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!list.contains(entry.getKey())) {
                map.remove(entry.getKey());
            }
        }
        logger.info("map truc: " + map);
        System.out.println("map now: "+map);
        return map;
    }

    private void addStatusToRequest(String status, int i) {
        logger.info("size: " + i);
        if (i > 1) {
            request += " and";
        } else if (i == 0) {
            request += " where";
        }
        if (!status.equals("")) {
            switch (status) {
                case "PROGRESS":
                    request += "  endDate is null";
                    break;
                case "TERMINATED":
                    request += "  endDate is not null";
                    break;
                case "OVERDUE":
                    request += "  endDate is null and plannedEndDate < current_date";
                    break;
                default:
                    logger.info("nothing to add");
                    break;
            }
        }
    }


}
