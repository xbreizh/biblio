package org.troparo.consumer.impl;


import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.consumer.enums.LoanStatus;
import org.troparo.model.Book;
import org.troparo.model.Loan;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;


@Named("loanDAO")
public class LoanDAOImpl implements LoanDAO {
    private static final String STATUS = "status";
    private static final String ISBN = "isbn";
    private static final String BOOK_ID = "book_id";
    private static final String LOGIN = "login";
    private static final String QUERY ="query: ";
    private static Logger logger = Logger.getLogger(LoanDAOImpl.class);
    @Inject
    private SessionFactory sessionFactory;
    private static final Class cl = Loan.class;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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
        logger.info("trying to add loan");
        try {
            sessionFactory.getCurrentSession().flush();
            sessionFactory.getCurrentSession().persist(loan);
        } catch (Exception e) {
            logger.error("error while persisting: " + e.getMessage());
            return false;
        }
        logger.info("loan added successfully");
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
    public Loan getPendingReservation(String isbn) {
        logger.info("Isbn received: " + isbn);
        List<Loan> loanList;
        String request = "From Loan where isbn = :isbn and reservationDate is not null and startDate is null and book.id is null order by reservationDate asc";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
            query.setParameter(ISBN, isbn);
            loanList = query.getResultList();

            if (!loanList.isEmpty()) return loanList.get(0);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("no pending reservation for: " + isbn);
        return null;
    }

    @Override
    public Book getNextAvailableBook(String isbn) {
        List<Book> bookList;
        if (isbn == null) return null;
        isbn = "'" + isbn.toUpperCase() + "'";
        logger.info("ISBN received: " + isbn);
        String request =
                "select * from Book where isbn = " + isbn + " and exists(" +
                        "select book_id from loan where isbn = " + isbn + " and end_date is not null)";
        try {
            logger.info(QUERY+request);
            Query query = sessionFactory.getCurrentSession().createNativeQuery(request).addEntity(Book.class);
            bookList = query.getResultList();
            if (!bookList.isEmpty()) return bookList.get(0);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean cleanupExpiredReservation(int expiration) {
        StringBuilder sb = new StringBuilder();
        int beforeCleanup = cleanupExpiredReservationCount(expiration);
        logger.info("number reservations to clean: "+beforeCleanup);
        try {
            sb.append("update loan set end_date = current_date where available_date < (now() - interval ");
            sb.append( "'"+expiration+" day'");
            sb.append(") and start_date is null and end_date is null");


            Query query = sessionFactory.getCurrentSession().createNativeQuery(
                    sb.toString());

            query.executeUpdate();
            int afterCleanup = cleanupExpiredReservationCount(expiration);
            logger.info("number reservations to clean: "+afterCleanup);
            if(afterCleanup==0) return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    public int cleanupExpiredReservationCount(int expiration) {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("select * from  Loan where available_date < (now() - ");
            sb.append("interval '"+expiration+" day'");
            sb.append(") and start_date is null and end_date is null");
            Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
            logger.info(QUERY+sb.toString());
            if( query.getResultList().isEmpty()) {
                logger.info("no expired reservation found");
                return 0;
            }
            else{
                logger.info(query.getFetchSize());
            return query.getResultList().size();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return 999;
        }

    }

    @Override
    public List<Loan> getAllPendingReservationWithNoBook() {

        Query query = sessionFactory.getCurrentSession().createQuery("From Loan where book.id is null and endDate is null");
        return query.getResultList();
    }

    @Override
    public List<Loan> getLoansReadyForStart() {
        String request = "From Loan where book.id is not null and availableDate is not null and startDate is null and endDate is null";
        Query query = sessionFactory.getCurrentSession().createQuery(request);
        return query.getResultList();
    }

    @Override
    public List<Loan> getReminderLoans(int daysReminder) {
        // we calculate the number of days between today and planned_end_date
        // if that number is positive (negative would be overdue)
        // and inferior or equals to daysReminder
        // we consider it

        StringBuilder sb = new StringBuilder();
        sb.append("select * from loan where id in (");
        sb.append("select id from (");
        sb.append("select * from ("+
                "SELECT current_date, id, reservation_date, available_date, start_date, planned_end_date, end_date, isbn, book_id, borrower_id,"+
                "  EXTRACT(DAY FROM planned_end_date - current_date) as diff "+
                "FROM loan ) as a where a.diff >=0 and ");
        sb.append("a.diff <= "+daysReminder);
        sb.append(" and borrower_id in (select id from member where reminder = true) and end_date is null and start_date is not null and planned_end_date is not null"
                );
        sb.append(") b)");
        logger.info(QUERY+sb.toString());
        Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString()).addEntity(Loan.class);
        return query.getResultList();
    }


    Date getTodayDate() {
        return new Date();
    }



    @Override
    public boolean removeLoan(Loan loan) {
        logger.info("trying to remove loan");
        try {
            sessionFactory.getCurrentSession().remove(loan);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Loan> getLoansByCriteria(Map<String, String> map) {

        StringBuilder request = new StringBuilder();
        List<Loan> loanList = new ArrayList<>();
        if (map == null) return loanList;
        if (map.isEmpty()) return loanList;
        if (!checkValidMapEntries(map)) return loanList;
        logger.info("map received in DAO: " + map);
        request.append("From Loan ");
        request.append(createRequestFromMap(map));

        logger.info("request: " + request);
        if (map.containsKey(STATUS)) {
            request.append(addStatusToRequest(map));
        }
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(request.toString(), cl);
            addingParametersToCriteriaQuery(map, query);
            logger.info("map again: " + map);

            logger.info("map: " + request);

            logger.info("list with criteria size: " + query.getResultList().size());
            return query.getResultList();
        } catch (Exception e) {
            logger.info("bam l erreur");
            return loanList;
        }

    }


    private void addingParametersToCriteriaQuery(Map<String, String> map, Query query) {
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
                if (entry.getKey().toLowerCase().contains(ISBN)) {
                    query.setParameter(ISBN, entry.getValue().toUpperCase());
                }

            }
        }
    }

    String createRequestFromMap(Map<String, String> map) {
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
                if (entry.getKey().equalsIgnoreCase(ISBN)) {
                    criteria.append("book.isbn");
                    criteria.append(" = :");
                    criteria.append(entry.getKey());
                }

            } else {
                logger.info("status has been passed: " + entry.getValue());
            }

        }
        return criteria.toString();
    }

    private boolean checkValidMapEntries(Map<String, String> map) {
        String[] authorizedCriteria = {STATUS, BOOK_ID, LOGIN, ISBN};
        List<String> list = Arrays.asList(authorizedCriteria);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!list.contains(entry.getKey().toLowerCase())) {
                return false;
            } else {
                if (entry.getKey().equalsIgnoreCase(STATUS) && !checkValidStatus(entry.getValue())) return false;

            }

        }

        return true;
    }

    boolean checkValidStatus(String status) {
        return EnumUtils.isValidEnum(LoanStatus.class, status.toUpperCase());
    }

    String addStatusToRequest(Map<String, String> map) {

        String status = extractStatusFromMap(map);

        String request = "";
        logger.info("size: " + map.size());
        if (!checkValidStatus(status)) {
            return request;
        }
        if (map.size() - 1 >= 1) {
            request += " and";
        } else {
            request += " where";
        }


        switch (status) {
            case "TERMINATED":
                request += " endDate is not null";
                break;
            case "OVERDUE":
                request += " endDate is null and plannedEndDate < current_date";
                break;
            default: //progress
                request += " endDate is null";
                break;

        }

        return request;
    }

    String extractStatusFromMap(Map<String, String> map) {
        if (map == null) return "";
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(STATUS)) {
                    return entry.getValue().toUpperCase();
                }

            }
        }
        return "";
    }

}
