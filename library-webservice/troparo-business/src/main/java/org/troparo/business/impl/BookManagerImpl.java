package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Named
public class BookManagerImpl implements BookManager {
    @Inject
    BookDAO bookDAO;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String exception = "";

    @Override
    public String addBook(Book book) {
        exception = "";
        // checking if already existing
        if (bookDAO.existingISBN(book.getIsbn().toUpperCase())) {
            exception = "ISBN already existing";
            return exception;
        }
        // checking that all values are provided
        exception = checkRequiredValuesNotNull(book);
        if (!exception.equals("")) {
            return exception;
        }

        // checking that all values are valid
        exception = checkInsertion(book);
        if (!exception.equals("")) {
            return exception;
        }
        logger.info("in the ");
        // adding insertion date
        book.setInsert_date(new Date());
        book.setIsbn(book.getIsbn().toUpperCase());
        bookDAO.addBook(book);
        logger.info("exception: " + exception);
        return exception;
    }


    private String checkInsertion(Book book) {
        if (book.getIsbn().length() != 10 && book.getIsbn().length() != 13) {
            return exception = "ISBN must be 10 or 13 characters: " + book.getIsbn();
        }
        if (book.getTitle().length() < 2 || book.getTitle().length() > 200) {
            return exception = "Title should have between 2 and 200 characters: " + book.getTitle();
        }
        if (book.getAuthor().length() < 2 || book.getAuthor().length() > 200) {
            return exception = "Author should have between 2 and 200 characters: " + book.getAuthor();
        }
        if (book.getEdition().length() < 2 || book.getEdition().length() > 200) {
            return exception = "Edition should have between 2 and 200 characters: " + book.getEdition();
        }
        if (book.getPublicationYear() < 1455 || book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            return exception = "Publication year should be between 1455 and current: " + book.getPublicationYear();
        }
        if (book.getNbPages() < 1 || book.getNbPages() > 9999) {
            return exception = "NbPages should be between 1 and 9 999, please recheck: " + book.getNbPages();
        }
        if (book.getKeywords().length() < 2 || book.getKeywords().length() > 200) {
            return exception = "Keyword list should be between 2 and 200 characters: " + book.getKeywords();
        }
        book.setKeywords(cleanKeywords(book.getKeywords()));

        return exception;
    }

    private String cleanKeywords(String string) {
        System.out.println("trying to replace: " + string);
        if (string.contains(",")) {
            System.out.println("replacing ,");
            string = string.replace(",", " ");
        }
        if (string.contains(";")) {
            System.out.println("replacing ;");
            string = string.replace(";", " ");
        }
        System.out.println("string wen sortie: " + string);
        return string;
    }

    private String checkRequiredValuesNotNull(Book book) {

        if (book.getIsbn().equals("") || book.getIsbn().equals("?")) {
            return "isbn should be filled";
        }
        if (book.getTitle().equals("") || book.getTitle().equals("?")) {
            return "Title should be filled";
        }
        if (book.getAuthor().equals("") || book.getAuthor().equals("?")) {
            return "Author should be filled";
        }
        if (book.getPublicationYear() == 0) {
            return "Publication should be filled";
        }
        if (book.getEdition().equals("") || book.getEdition().equals("?")) {
            return "Edition should be filled";
        }
        if (book.getNbPages() == 0) {
            return "NbPages should be filled";
        }
        if (book.getKeywords().equals("") || book.getKeywords().equals("?")) {
            return "keywords should be filled";
        }
        return "";
    }

    @Override
    public List<Book> getBooks() {
        return bookDAO.getBooks();
    }

    @Override
    public Book getBookById(int id) {
        logger.info("getting id (from business): " + id);
        Book book = bookDAO.getBookById(id);
        if (book != null) {
            logger.info("book");
            return book;
        } else {
            logger.info("book is probably null");
            return null;
        }
    }

    @Override
    public List<Book> getBooksByCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = new HashMap<>();
        for (HashMap.Entry<String, String> entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("?") && !entry.getValue().equals("")) {
                criterias.put(entry.getKey(), entry.getValue());
            }
        }
        logger.info("criterias: " + criterias);
        return bookDAO.getBooksByCriterias(criterias);
    }

    @Override
    public String updateBook(Book book) {
        exception = "";
        if (book.getIsbn().equals("") || book.getIsbn().equals("?")) {
            return "you must provide an ISBN";
        } else {
            logger.info(book.getTitle());
            logger.info(book.getAuthor());
        }
        List<Book> bookList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("ISBN", book.getIsbn().toUpperCase());
        bookList = bookDAO.getBooksByCriterias(map);
        if (bookList.size() == 0) {
            return "No Item found with that ISBN";
        }
        logger.info("getting list: " + bookList.size());
        for (Book b : bookList
        ) {
            if (!book.getTitle().equals("") && !book.getTitle().equals("?")) {
                b.setTitle(book.getTitle());
            }
            if (!book.getAuthor().equals("") && !book.getAuthor().equals("?")) {
                b.setAuthor(book.getAuthor());
            }
            if (!book.getEdition().equals("") && !book.getEdition().equals("?")) {
                b.setEdition(book.getEdition());
            }
            if (book.getPublicationYear() != 0) {
                b.setPublicationYear(book.getPublicationYear());
            }
            if (book.getNbPages() != 0) {
                b.setNbPages(book.getNbPages());
            }
            if (!book.getKeywords().equals("") && !book.getKeywords().equals("?")) {
                b.setKeywords(book.getKeywords());
            }
            logger.info(b.getAuthor());
            logger.info(b.getTitle());
            bookDAO.updateBook(b);
            logger.info("updated: " + b.getId());
        }

        return exception;
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookDAO.getBookByIsbn(isbn.toUpperCase());
    }

    @Override
    public String remove(int id) {
        Book book = bookDAO.getBookById(id);

        if (book == null) {
            return exception = "No item found";
        } else {
            bookDAO.remove(book);
        }
        return exception;
    }

    @Override
    public int getNbAvailable(String isbn) {
        return bookDAO.getAvailable(isbn);
    }

    @Override
    public String addCopy(String isbn, int copies) {
        exception = "";
        if (!bookDAO.existingISBN(isbn.toUpperCase())) {
            return "No record found with that ISBN";
        } else {
            Book b = bookDAO.getBookByIsbn(isbn.toUpperCase());
            logger.info("record found: " + b);
            int i = 0;
            while (i < copies) {
                Book b2 = new Book();
                // duplicating record
                b2.setIsbn(b.getIsbn());
                b2.setTitle(b.getTitle());
                b2.setAuthor(b.getAuthor());
                b2.setPublicationYear(b.getPublicationYear());
                b2.setNbPages(b.getNbPages());
                b2.setEdition(b.getEdition());
                b2.setKeywords(b.getKeywords());
                b2.setInsert_date(new Date());
                logger.info("new Book: " + b2);
                if (!bookDAO.addBook(b2)) {
                    exception = "issue while adding copies for: " + isbn;
                    logger.info("exception: " + exception);
                }
                i++;
            }
        }
        return exception;
    }

    @Override
    public boolean isAvailable(int id) {
        //check if exists
        if (bookDAO.getBookById(id) != null) {
            return bookDAO.isAvailable(id);
        } else {
            return false;
        }
    }
}
