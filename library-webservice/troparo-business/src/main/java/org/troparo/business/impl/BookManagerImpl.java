package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.business.impl.validator.StringValidatorBook;
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
    @Inject
    StringValidatorBook stringValidatorBook;
    private static  Logger logger = Logger.getLogger(BookManagerImpl.class);


    public void setStringValidatorBook(StringValidatorBook stringValidatorBook) {
        this.stringValidatorBook = stringValidatorBook;
    }

    @Override
    public String addBook(Book book) {
        String exception;

        exception = checkValidityOfParametersForInsertBook(book);
        if (!exception.equals("")) {
            return exception;
        }
        if (bookDAO.existingISBN(book.getIsbn())) {
            return "ISBN already existing";
        }
        book.setInsertDate(new Date());
        book.setIsbn(book.getIsbn().toUpperCase());
        if (bookDAO.addBook(book)) return "";

        return "there was an issue while inserting the book";
    }


    public String checkValidityOfParametersForInsertBook(Book book) {
        if (book == null) return "no book provided";
        String[][] bookParameters = {
                {"isbn", book.getIsbn()},
                {"title", book.getTitle()},
                {"author", book.getAuthor()},
                {"publicationYear", Integer.toString(book.getPublicationYear())},
                {"nbPages", Integer.toString(book.getNbPages())},
                {"keywords", book.getKeywords()},
                {"edition", book.getEdition()}};
        book.setKeywords(replaceSeparatorWithWhiteSpace(book.getKeywords()));
        for (String[] param : bookParameters) {
            logger.info("param: " + param[0] + " / " + param[1]);
            if (!stringValidatorBook.validateExpression(param[0], param[1])) {
                return stringValidatorBook.getException(param[0]) + param[1];
            }
        }

        return "";
    }


    String replaceSeparatorWithWhiteSpace(String string) {
        logger.info("trying to replace: " + string);
        if (string == null) return "";
        String[] separators = {";", ",", "/", "\\",};
        for (String sep : separators
        ) {
            if (string.contains(sep)) {
                string = string.replace(sep, " ");
            }
        }
        return string;
    }


    @Override
    public List<Book> getBooks() {
        return bookDAO.getBooks();
    }

    @Override
    public Book getBookById(int id) {
        logger.info("getting id (from business): " + id);
        return bookDAO.getBookById(id);

    }

    @Override
    public List<Book> getBooksByCriteria(Map<String, String> map) {
        Map<String, String> criterias = removeInvalidEntriesFromCriterias(map);

        logger.info("criterias: " + criterias);
        return bookDAO.getBooksByCriterias(criterias);
    }

    protected Map<String, String> removeInvalidEntriesFromCriterias(Map<String, String> map) {
        Map<String, String> criterias = new HashMap<>();
        String[] possibleCriterias = {"Author", "Title", "ISBN"};
        List<String> possibleCriteriasList = Arrays.asList(possibleCriterias);
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            if (possibleCriteriasList.contains(entry.getKey()) && !entry.getValue().equals("?") && !entry.getValue().equals("")) {
                criterias.put(entry.getKey(), entry.getValue());
            }

        }
        return criterias;
    }

    @Override
    public String updateBook(Book book) {


        if (book == null) return "No book provided!";
        if (!checksThatBookHasAnISBN(book).equals("")) return checksThatBookHasAnISBN(book);
        List<Book> bookList;


        Map<String, String> map = new HashMap<>();
        map.put("ISBN", book.getIsbn().toUpperCase());
        bookList = bookDAO.getBooksByCriterias(map);
        if (bookList == null) return "No Item found with that ISBN";
        logger.info("getting list. Size: " + bookList.size());
        if (bookList.isEmpty()) return "No book to update";
        // updates each book from the list when values from book are provided
        for (Book b : bookList
        ) {
            transferValuesToSimilarBooks(book, b);
            logger.info(b.getAuthor());
            logger.info(b.getTitle());
            if (!bookDAO.updateBook(b)) return "Issue while updating";
            logger.info("updated: " + b.getId());
        }


        return "";
    }


    Book transferValuesToSimilarBooks(Book book, Book b) {
        transferTitleToSimilarBooks(book, b);
        transferAuthorToSimilarBooks(book, b);
        transferEditionToSimilarBooks(book, b);
        transferPublicationYearToSimilarBooks(book, b);
        transferNbPagesToSimilarBooks(book, b);
        transferKeywordsToSimilarBooks(book, b);
        return b;
    }

    String transferKeywordsToSimilarBooks(Book book, Book b) {
        if (book != null && b != null) {
            if (book.getKeywords() != null && !book.getKeywords().equals("") && !book.getKeywords().equals("?")) {
                b.setKeywords(book.getKeywords());
            }

            return b.getKeywords();
        } else {
            return null;
        }
    }

    int transferNbPagesToSimilarBooks(Book book, Book b) {
        if (book != null && b != null) {
            if (book.getNbPages() != 0) {
                b.setNbPages(book.getNbPages());
            }
            return b.getNbPages();
        } else {
            return 0;
        }

    }

    int transferPublicationYearToSimilarBooks(Book book, Book b) {
        if (book.getPublicationYear() != 0) {
            b.setPublicationYear(book.getPublicationYear());
        }
        return b.getPublicationYear();
    }

    String transferEditionToSimilarBooks(Book book, Book b) {
        if (book != null && b != null) {
            if (book.getEdition() != null && !book.getEdition().equals("") && !book.getEdition().equals("?")) {
                b.setEdition(book.getEdition());
            }

            return b.getEdition();
        }
        return null;
    }

    String transferAuthorToSimilarBooks(Book book, Book b) {
        if (book != null && b != null) {
            if (book.getAuthor() != null && !book.getAuthor().equals("") && !book.getAuthor().equals("?")) {
                b.setAuthor(book.getAuthor());
            }
        } else {
            return null;
        }

        return b.getAuthor();
    }

    String transferTitleToSimilarBooks(Book book, Book b) {
        if (book != null && b != null) {
            if (book.getTitle() != null && !book.getTitle().equals("") && !book.getTitle().equals("?")) {
                b.setTitle(book.getTitle());
            }
        } else {
            return null;
        }

        return b.getTitle();
    }


    protected String checksThatBookHasAnISBN(Book book) {
        String str = "You must provide an ISBN";
        if (book == null) return str;
        if (book.getIsbn() == null || book.getIsbn().equals("") || book.getIsbn().equals("?")) {
            return str;
        } else {
            logger.info(book.getTitle());
            logger.info(book.getAuthor());
        }
        return "";
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        isbn = isbn.toUpperCase();
        return bookDAO.getBookByIsbn(isbn);
    }

    @Override
    public String remove(int id) {
        Book book = bookDAO.getBookById(id);

        if (book == null) {
            return "No item found";
        } else {
            bookDAO.remove(book);
        }
        return "";
    }

    @Override
    public int getNbAvailable(String isbn) {
        return bookDAO.getAvailable(isbn);
    }

    @Override
    public String addCopy(String isbn, int copies) {
        if (!bookDAO.existingISBN(isbn.toUpperCase())) {
            return "No record found with that ISBN";
        } else {
            Book b = bookDAO.getBookByIsbn(isbn.toUpperCase());
            logger.info("record found: " + b);
            int i = 0;
            while (i < copies) {
                Book b2 = new Book();
                b2.setAuthor(b.getAuthor());
                b2.setTitle(b.getTitle());
                b2.setIsbn(b.getIsbn());
                b2.setEdition(b.getEdition());
                b2.setPublicationYear(b.getPublicationYear());
                b2.setNbPages(b.getNbPages());
                b2.setKeywords(b.getKeywords());
                b2.setInsertDate(new Date());
                logger.info("new Book: " + b2);
                if (!bookDAO.addBook(b2)) {
                    return "Issue while adding copies for: " + isbn;
                }
                i++;
            }
        }
        return "";
    }

    @Override
    public boolean isAvailable(int id) {

        return bookDAO.isAvailable(id);
    }

    @Override
    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }
}
