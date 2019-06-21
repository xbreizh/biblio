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
    private static final String STATUS = "status";
    private static final String ISBN = "isbn";
    private static final String BOOK_ID = "book_id";
    private static final String LOGIN = "login";

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Inject
    private SessionFactory sessionFactory;
    private Class cl = Loan.class;


    @Override
    public List<Loan> getLoans() {
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
        String request;
        logger.info("Id received: " + id);
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
        String request;
        List<Loan> loanList = new ArrayList<>();
        if (isbn == null) return loanList;
        isbn = isbn.toUpperCase();
        logger.info("ISBN received: " + isbn);
        logger.info(isbn);
        request = "From Loan where book.isbn = :isbn";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
            query.setParameter(ISBN, isbn);

            return query.getResultList();
        } catch (Exception e) {
            return loanList;
        }
    }

    @Override
    public List<Loan> getLoanByLogin(String login) {
        String request;
        List<Loan> loanList = new ArrayList<>();
        logger.info("Login received: " + login);
        login = login.toUpperCase();
        request = "From Loan where borrower.login = :login";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
            query.setParameter(LOGIN, login);

            List<Loan> list = query.getResultList();
            logger.info("size of list: " + list.size());
            return query.getResultList();
        } catch (Exception e) {
            return loanList;
        }
    }

    @Override
    public List<Loan> getLoansByCriterias(HashMap<String, String> map) {
        StringBuilder request = new StringBuilder();
        List<Loan> loanList = new ArrayList<>();
        if (map == null || map.isEmpty()) return new ArrayList<>();
        if (!checkValidMapEntries(map)) return loanList;
        System.out.println("here");
        if (map.size() == 0) return new ArrayList<>();
        logger.info("map received in DAO: " + map);
        request.append("From Loan ");
        request.append(createRequestFromMap(map));

        logger.info("request: " + request);

        if (map.containsKey(STATUS)) {
            request.append(addStatusToRequest(map));
        }
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(request.toString(), cl);
            System.out.println("getting params");
            addingParametersToCriteriasQuery(map, query);
            logger.info("map again: " + map);

            logger.info("map: " + request);

            logger.info("list with criteria size: " + query.getResultList().size());
            return query.getResultList();
        } catch (Exception e) {
            logger.info("bam l erreur");
            return loanList;
        }

    }

    private void addingParametersToCriteriasQuery(HashMap<String, String> map, Query query) {
        System.out.println("mapmap: "+map);
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getKey().equalsIgnoreCase(STATUS)) {
                logger.info("criteria: " + entry.getValue());
                if (entry.getKey().toLowerCase().contains(LOGIN)) {
                    query.setParameter(LOGIN, entry.getValue().toUpperCase());

                }
                if (entry.getKey().toLowerCase().contains(BOOK_ID)) {
                    query.setParameter(BOOK_ID, +Integer.parseInt(entry.getValue()));
                }

            }
        }
    }

    private String createRequestFromMap(HashMap<String, String> map) {
        StringBuilder criteria = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getKey().equalsIgnoreCase(STATUS)) {
                if (!criteria.toString().equals("")) {
                    criteria.append(" and ");
                } else {
                    criteria.append("where ");
                }
                if (entry.getKey().equalsIgnoreCase(LOGIN)) {
                    criteria.append("borrower.login");
                    criteria.append(" = :");
                    criteria.append(LOGIN);
                }
                if (entry.getKey().equalsIgnoreCase(BOOK_ID)) {
                    criteria.append(BOOK_ID);
                    criteria.append(" = :");
                    criteria.append(entry.getKey());
                }

            } else {
                logger.info("status has been passed: " + entry.getValue());
            }

        }
        return criteria.toString();
    }

    private boolean checkValidMapEntries(HashMap<String, String> map) {
        String[] authorizedCriteria = {STATUS, BOOK_ID, LOGIN};
        List<String> list = Arrays.asList(authorizedCriteria);
        System.out.println("map: "+map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!list.contains(entry.getKey().toLowerCase())) {
                return false;
            } else {
                if (entry.getKey().equalsIgnoreCase(STATUS) && !checkValidStatus(entry.getValue())) return false;

            }

        }

        return true;
    }

    private boolean checkValidStatus(String status) {
        String[] authorized = {"PROGRESS", "TERMINATED", "OVERDUE"};
        List<String> authorizedList = Arrays.asList(authorized);
        return authorizedList.contains(status.toUpperCase());
    }

    String addStatusToRequest(HashMap<String, String> map) {

        String status = extractStatusFromMap(map);

        String request = "";
        logger.info("size: " + map.size());
        if (!checkValidStatus(status)) return request;
        if (!status.isEmpty()) {
            if (map.size() - 1 > 1) {
                request += " and";
            } else {
                request += " where";
            }


            switch (status) {
                case "PROGRESS":
                    request += " endDate is null";
                    break;
                case "TERMINATED":
                    request += " endDate is not null";
                    break;
                case "OVERDUE":
                    request += " endDate is null and plannedEndDate < current_date";
                    break;

            }

        }

        return request;
    }

    String extractStatusFromMap(HashMap<String, String> map) {
        if(map==null )return "";
        if(!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(STATUS)) {
                    return entry.getValue().toUpperCase();
                }

            }
        }return "";
    }

}
