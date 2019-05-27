package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

@Named("bookDAO")
public class BookDAOImpl implements BookDAO {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Class cl = Book.class;
    private String request;

    @Inject
    private SessionFactory sessionFactory;


    @Override
    public List<Book> getBooks() {
        logger.info("getting in dao");
        try {
            return sessionFactory.getCurrentSession().createQuery("from Book", cl).getResultList();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public boolean addBook(Book book) {
        logger.info("Book from dao: " + book);
        try {
            sessionFactory.getCurrentSession().persist(book);
        } catch (Exception e) {
            System.err.println("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Book getBookById(int id) {
        logger.info("in the dao: " + id);
        request = "From Book where id = :id";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("id", id);
        try {
            return (Book) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public boolean existingISBN(String isbn) {
        logger.info("in the dao: " + isbn);
        request = "From Book where isbn = :isbn";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("isbn", isbn);
        if (query.getResultList().size() != 0) {
            logger.info("records found: " + query.getResultList().size());
            return true;
        } else {
            logger.info("no record found for that isbn: " + isbn);
            return false;
        }
    }

    @Override
    public List<Book> getBooksByCriterias(HashMap<String, String> map) {
        logger.info("map received in DAO: " + map);
        String criterias = "";
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!criterias.equals("")) {
                criterias += " and ";
            } else {
                criterias += "where ";
            }
            criterias += entry.getKey() + " like :" + entry.getKey();
        }
        request = "SELECT DISTINCT ON (isbn ) *  From Book ";
        request += criterias;
        /*request += "group by ISBN";*/
        logger.info("request: " + request);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        Query query = sessionFactory.getCurrentSession().createNativeQuery(request, Book.class);
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            logger.info("criteria: " + entry.getValue());
            query.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
        }
        try {
            logger.info("list with criterias size: " + query.getResultList().size());
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean updateBook(Book book) {
        logger.info("Book from dao: " + book.getTitle());
        logger.info("Book from dao: " + book.getAuthor());
        try {
            sessionFactory.getCurrentSession().update(book);
        } catch (Exception e) {
            System.err.println("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Book book) {
        logger.info("Trying to delete" + book);
        try {
            sessionFactory.getCurrentSession().delete(book);
        } catch (Exception e) {
            System.err.println("error while updating: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public int getAvailable(String isbn) {
        logger.info("isbn passed: " + isbn);
        String request = "select count(*) from Book where isbn = :isbn and id not in(select book.id from Loan where endDate is null)";
        Query query = sessionFactory.getCurrentSession().createQuery(request);
        query.setParameter("isbn", isbn);
        long count = (long) query.getSingleResult();
        logger.info("result found: " + count);
        int i = toIntExact(count);
        logger.info("count: " + i);
        return i;
    }

    @Override
    public boolean isAvailable(int id) {
        logger.info("id passed: " + id);

        // checking if currently borrowed
        String request1 = "select book.id from Loan where endDate is null and book.id = :id";
        Query query1 = sessionFactory.getCurrentSession().createQuery(request1);
        query1.setParameter("id", id);
        return query1.getResultList().size() <= 0;// if not currently borrowed, then available
        /*
        String request2 = "From Book where id = :id and id not in(select book.id from Loan where endDate is not null)";
        logger.info("request: " + request);
        Query query = sessionFactory.getCurrentSession().createQuery(request);
        query.setParameter("id", id);
        logger.info("is available: "+query.getSingleResult());

        return query.getSingleResult() != null;*/
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        List<Book> list = new ArrayList<>();
        request = "From Book where isbn = :isbn";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("isbn", isbn);
        try {
            return (Book) query.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

}
