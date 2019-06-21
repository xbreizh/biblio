package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.consumer.contract.LoanDAO;
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
    LoanDAO loanDAO;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String exception = "";

    @Override
    public String addBook(Book book) {
        exception = "";
        // checking if already existing
        checksThatBookHasAnISBN(book);
        book.setIsbn(book.getIsbn().toUpperCase());
        if (bookDAO.existingISBN(book.getIsbn())) {
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
        // adding insertion date
        book.setInsert_date(new Date());
        book.setIsbn(book.getIsbn().toUpperCase());
        bookDAO.addBook(book);
        logger.info("exception: " + exception);
        return exception;
    }


    String checkInsertion(Book book) {
        if (checkIsbnLength(book)) return exception = "ISBN must be 10 or 13 characters: " + book.getIsbn();
        if (!checkBookParamLength(book.getTitle()))
            return exception = "Title should have between 2 and 200 characters: " + book.getTitle();
        if (!checkBookParamLength(book.getAuthor()))
            return exception = "Author should have between 2 and 200 characters: " + book.getAuthor();
        if (!checkBookParamLength(book.getEdition()))
            return exception = "Edition should have between 2 and 200 characters: " + book.getEdition();
        if (book.getPublicationYear() < 1455 || book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            return exception = "Publication year should be between 1455 and current: " + book.getPublicationYear();
        }
        if (book.getNbPages() < 1 || book.getNbPages() > 9999) {
            return exception = "NbPages should be between 1 and 9 999, please recheck: " + book.getNbPages();
        }
        if (checkBookParamLength(book.getKeywords()))
            return exception = "Keyword list should be between 2 and 200 characters: " + book.getKeywords();
        String keywords = replaceSeparatorWithWhiteSpace(book.getKeywords());
        book.setKeywords(keywords);

        return exception;
    }

    boolean checkBookParamLength(String param) {
        if (param != null) {
            return param.length() > 2 && param.length() < 200;

        }
        return false;
    }

    private boolean checkIsbnLength(Book book) {

        return book.getIsbn().length() != 10 && book.getIsbn().length() != 13;
    }

    String replaceSeparatorWithWhiteSpace(String string) {
        logger.info("trying to replace: " + string);
        String[] separators = {";", ",", "/", "\\",};
        for (String sep : separators
        ) {
            if (string.contains(sep)) {
                string = string.replace(sep, " ");
            }
        }
        return string;
    }


    String checkRequiredValuesNotNull(Book book) {
        if(book==null)return "book is null";

        if (checkValidParamString(book.getIsbn())) return "isbn should be filled";
        if (checkValidParamString(book.getTitle())) return "Title should be filled";
        if (checkValidParamString(book.getAuthor())) return "Author should be filled";
        if (checkValidParamString(book.getKeywords())) return "keywords should be filled";
        if (book.getPublicationYear() == 0) {
            return "Publication should be filled";
        }
        if (checkValidParamString(book.getEdition())) return "Edition should be filled";
        if (book.getNbPages() == 0) {
            return "NbPages should be filled";
        }
        return "";
    }

    private boolean checkValidParamString(String isbn) {

        return isbn == null || isbn.equals("") || isbn.equals("?");
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
    public List<Book> getBooksByCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = removeInvalidEntriesFromCriterias(map);

        logger.info("criterias: " + criterias);
        return bookDAO.getBooksByCriterias(criterias);
    }

    protected HashMap<String, String> removeInvalidEntriesFromCriterias(HashMap<String, String> map) {
        HashMap<String, String> criterias = new HashMap<>();
        String[] possibleCriterias = {"Author", "Title", "ISBN"};
        List<String> possibleCriteriasList = Arrays.asList(possibleCriterias);
        for (HashMap.Entry<String, String> entry : map.entrySet()
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


        HashMap<String, String> map = new HashMap<>();
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
        if (book.getKeywords() != null && !book.getKeywords().equals("") && !book.getKeywords().equals("?")) {
            b.setKeywords(book.getKeywords());
        }

        return b.getKeywords();
    }

    int transferNbPagesToSimilarBooks(Book book, Book b) {
        if (book.getNbPages() != 0) {
            b.setNbPages(book.getNbPages());
        }
        return b.getNbPages();
    }

    int transferPublicationYearToSimilarBooks(Book book, Book b) {
        if (book.getPublicationYear() != 0) {
            b.setPublicationYear(book.getPublicationYear());
        }
        return b.getPublicationYear();
    }

    String transferEditionToSimilarBooks(Book book, Book b) {
        if (book != null && b!=null ) {
            if (book.getEdition() != null && !book.getEdition().equals("") && !book.getEdition().equals("?")) {
                logger.info("got you");
                b.setEdition(book.getEdition());
            }

            return b.getEdition();
        }return null;
    }

    String transferAuthorToSimilarBooks(Book book, Book b) {
        if (book.getAuthor() != null && !book.getAuthor().equals("") && !book.getAuthor().equals("?")) {
            b.setAuthor(book.getAuthor());
        }

        return b.getAuthor();
    }

    String transferTitleToSimilarBooks(Book book, Book b) {
        if (book != null && b!=null ) {
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
                    exception = "Issue while adding copies for: " + isbn;
                    logger.info("exception: " + exception);
                }
                i++;
            }
        }
        return exception;
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
