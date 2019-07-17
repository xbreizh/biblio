package org.troparo.consumer.impl;


import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
            return getCurrentSession().createQuery("From Book", cl).getResultList();
        } catch (Exception e) {
            return bookList;
        }

    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public boolean addBook(Book book) {
        logger.info("Book: " + book);
        try {
            getCurrentSession().persist(book);
        } catch (Exception e) {
            logger.error("error while persisting: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Book getBookById(int id) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Book> cr = cb.createQuery(Book.class);
        Root<Book> root = cr.from(Book.class);
        cr.select(root);
        cr.where(cb.equal(root.get("id"), id));
        Query<Book> query = getCurrentSession().createQuery(cr);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean existingISBN(String isbn) {
        logger.info("ISBN: " + isbn);
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Book> cr = cb.createQuery(Book.class);
        Root<Book> root = cr.from(Book.class);
        cr.select(root);
        cr.where(cb.equal(root.get("isbn"), isbn.toUpperCase()));
        Query<Book> query = getCurrentSession().createQuery(cr);


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
        cleanInvalidMapEntries(map);
        int mapSizeAfterCleaning = map.size();
        if (mapSizeBeforeCleaning != mapSizeAfterCleaning) return bookList;
        createRequestFromMap(map, criteria);
        request = "SELECT DISTINCT ON (isbn ) *  From Book ";
        request += criteria;
        Query query;
        try {
            logger.info("request: " + request);
            getCurrentSession().clear();
            query = getCurrentSession().createNativeQuery(request, this.cl);

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

    private Map<String, String> cleanInvalidMapEntries(Map<String, String> map) {
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
            getCurrentSession().update(book);
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
            getCurrentSession().delete(book);
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
        Query query = getCurrentSession().createQuery(request);
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
        String request = "select book.id from Loan where  book.id = ?1 and endDate is null ";
        Query query = getCurrentSession().createQuery(request);
        query.setParameter(1, id);
        logger.info("size: " + query.getResultList().size());

        return query.getResultList().isEmpty();

    }

    @Override
    public Book getBookByIsbn(String isbn) {

        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Book> cr = cb.createQuery(Book.class);
        Root<Book> root = cr.from(Book.class);
        cr.select(root);
        cr.where(cb.equal(root.get("isbn"), isbn.toUpperCase()));
        Query<Book> query = getCurrentSession().createQuery(cr);
        if (query.getResultList().isEmpty()) return null;
        return query.getResultList().get(0);
    }

}
