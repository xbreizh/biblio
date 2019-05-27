package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Loan;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("loanDAO")
public class LoanDAOImpl implements LoanDAO {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Class cl = Loan.class;
    private String request;

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public List<Loan> getLoans() {
        logger.info("getting in dao");
        try {
            return sessionFactory.getCurrentSession().createQuery("From Loan", cl).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addLoan(Loan loan) {
        logger.info("Loan from dao: " + loan);
        try {
            sessionFactory.getCurrentSession().persist(loan);
        } catch (Exception e) {
            System.err.println("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean updateLoan(Loan loan) {
        logger.info("Loan from dao: " + loan);
        try {
            sessionFactory.getCurrentSession().update(loan);
        } catch (Exception e) {
            System.err.println("error while persisting: " + e.getMessage());
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
    public Loan getLoanByIsbn(String isbn) {
        logger.info("in the dao: " + isbn);
        request = "From Loan where book.isbn = :isbn";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("isbn", isbn);
        try {
            return (Loan) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Loan getLoanByLogin(String login) {
        logger.info("in the dao: " + login);
        request = "From Loan where borrower.login = :login";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("login", login);
        try {
            return (Loan) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Loan> getLoansByCriterias(HashMap<String, String> map) {
        logger.info("map received in DAO: " + map);
        String criterias = "";
        String status = "";
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getKey().toUpperCase().equals("STATUS")) {
                if (!criterias.equals("")) {
                    criterias += " and ";
                } else {
                    criterias += "where ";
                }
                criterias += entry.getKey() + " like :";
                if (entry.getKey().toUpperCase().contains("BOOK")) {
                    criterias += "BOOKID";
                }
                if (entry.getKey().toUpperCase().contains("LOGIN")) {
                    criterias += "LOGIN";
                }
                if (entry.getKey().toUpperCase().contains("STATUS")) {
                    criterias += "STATUS";
                }
            } else {
                status = entry.getValue();
                logger.info("status has been passed: " + status);
            }

        }

        request = "From Loan ";
        request += criterias;

        addStatusToRequest(status, map.size());
        logger.info("request: " + request);
        Query query = sessionFactory.getCurrentSession().createQuery(request, Loan.class);
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getKey().toUpperCase().equals("STATUS")) {
                logger.info("criteria: " + entry.getValue());
                if (entry.getKey().toUpperCase().contains("ISBN")) {
                    query.setParameter("ISBN", "%" + entry.getValue() + "%");
                }
                if (entry.getKey().toUpperCase().contains("LOGIN")) {
                    query.setParameter("LOGIN", "%" + entry.getValue() + "%");
                }
            }
        }
        try {
            logger.info("list with criterias size: " + query.getResultList().size());
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    private void addStatusToRequest(String status, int i) {
        if(i > 1){
            request += " and";
        }else{
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
