package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

@Named("bookDAO")
public class BookDAOImpl implements BookDAO {
    private static Logger logger = Logger.getLogger(BookDAOImpl.class.getName());
    private Class cl = Book.class;
    @Inject
    private SessionFactory sessionFactory;

    static void createRequestFromMap(Map<String, String> map, StringBuilder criteria) {
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (!criteria.toString().equals("")) {
                criteria.append(" and ");
            } else {
                criteria.append("where ");
            }
            criteria.append(entry.getKey());
            criteria.append(" like :");
            criteria.append(entry.getKey());
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Book> getBooks() {
        List<Book> bookList = new ArrayList<>();
        try {
            return sessionFactory.getCurrentSession().createQuery("From Book", cl).getResultList();
        } catch (Exception e) {
            return bookList;
        }

    }

    @Override
    public boolean addBook(Book book) {
        logger.info("Book: " + book);
        try {
            sessionFactory.getCurrentSession().flush();
            sessionFactory.getCurrentSession().persist(book);
        } catch (Exception e) {
            logger.error("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Book getBookById(int id) {
        String request;
        logger.info("id: " + id);
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
        logger.info("ISBN: " + isbn);
        String request;
        isbn = isbn.toUpperCase();
        request = "From Book where isbn = :isbn";

        Query query = sessionFactory.getCurrentSession().createQuery(request, cl);
        query.setParameter("isbn", isbn);
        if (!query.getResultList().isEmpty()) {
            logger.info("records found: " + query.getResultList().size());
            return true;
        } else {
            logger.info("no record found for that isbn: " + isbn);
            return false;
        }
    }

    @Override
    public List<Book> getBooksByCriterias(Map<String, String> map) {
        String request;
        StringBuilder criteria = new StringBuilder();
        logger.info("map: " + map);
        List<Book> bookList = new ArrayList<>();
        if (map == null || map.isEmpty()) return bookList;
        int mapSizeBeforeCleaning = map.size();
        cleanInvaliMapEntries(map);
        int mapSizeAfterCleaning = map.size();
        if (mapSizeBeforeCleaning != mapSizeAfterCleaning) return bookList;
        createRequestFromMap(map, criteria);
        request = "SELECT DISTINCT ON (isbn ) *  From Book ";
        request += criteria;
        Query query;
        try {
            logger.info("request: " + request);
            sessionFactory.getCurrentSession().clear();
            query = sessionFactory.getCurrentSession().createNativeQuery(request, Book.class);
            for (Map.Entry<String, String> entry : map.entrySet()
            ) {
                logger.info("criteria: " + entry.getValue());
                query.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
            }


            logger.info("list with criteria size: " + query.getResultList().size());
        } catch (Exception e) {
            return bookList;
        }
        return query.getResultList();
    }

    private Map<String, String> cleanInvaliMapEntries(Map<String, String> map) {
        String[] authorizedCriteria = {"isbn", "author", "title"};
        List<String> list = Arrays.asList(authorizedCriteria);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!list.contains(entry.getKey().toLowerCase())) {
                map.remove(entry.getKey());
            }
        }
        logger.info("map truc: " + map);
        return map;
    }

    @Override
    public boolean updateBook(Book book) {
        logger.info("Book title: " + book.getTitle() +
                "\nBook author: " + book.getAuthor());
        try {
            sessionFactory.getCurrentSession().update(book);
        } catch (Exception e) {
            logger.error("error while updating: " + e.getMessage());
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
            logger.error("error while removing: " + e.getMessage());
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
        String request1 = "select book.id from Loan where  book.id = :id";
        Query query1 = sessionFactory.getCurrentSession().createQuery(request1);
        query1.setParameter("id", id);
        logger.info("size: "+query1.getResultList().size());
        return query1.getResultList().isEmpty();// if not currently borrowed, then available

    }

    @Override
    public Book getBookByIsbn(String isbn) {
        String request;
        isbn = isbn.toUpperCase();
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
