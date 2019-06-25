package org.troparo.business.impl;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.business.impl.validator.StringValidatorBook;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.consumer.contract.LoanDAO;
import org.troparo.model.Book;
import org.troparo.model.Member;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Named
public class BookManagerImpl implements BookManager {
    @Inject
    BookDAO bookDAO;

    public void setStringValidatorBook(StringValidatorBook stringValidatorBook) {
        this.stringValidatorBook = stringValidatorBook;
    }

    @Inject
    StringValidatorBook stringValidatorBook;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String exception = "";

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


      /*  // checking if already existing
        checksThatBookHasAnISBN(book);
        book.setIsbn(book.getIsbn().toUpperCase());
        if (bookDAO.existingISBN(book.getIsbn())) {
            exception = "ISBN already existing";
            return exception;
        }*/
     /*   // checking that all values are provided
        exception = checkRequiredValuesNotNull(book);
        if (!exception.equals("")) {
            return exception;
        }

        // checking that all values are valid
        exception = checkInsertion(book);
        if (!exception.equals("")) {
            return exception;
        }*/
        // adding insertion date
        book.setInsertDate(new Date());
        book.setIsbn(book.getIsbn().toUpperCase());
        bookDAO.addBook(book);
        logger.info("exception: " + exception);
        return exception;
    }

    /*public String checkValidityOfParametersForInserBook(Book book) {
        if (book == null) return "no book provided";

        String[][] memberParameters = {{"login", member.getLogin()},
                {"firstName", member.getFirstName()},
                {"lastName", member.getLastName()},
                {"password", member.getPassword()},
                {"email", member.getEmail()}};

        for (String[] param : memberParameters) {
            if (!stringValidator.validateExpression(param[0], param[1])) {
                return stringValidator.getException(param[0]) + param[1];
            }
        }

        return "";
    }*/


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

        for (String[] param : bookParameters) {
            System.out.println("param: "+param[0]+" / "+param[1]);
            if (!stringValidatorBook.validateExpression(param[0], param[1])) {
                return stringValidatorBook.getException(param[0]) + param[1];
            }
        }

        return "";
    }


   /* String checkInsertion(Book book) {
        if (checkIsbnLength(book)) return "ISBN must be 10 or 13 characters: " + book.getIsbn();
        if (!checkBookParamLength(book.getTitle()))
            return "Title should have between 2 and 200 characters: " + book.getTitle();
        if (!checkBookParamLength(book.getAuthor()))
            return "Author should have between 2 and 200 characters: " + book.getAuthor();
        if (!checkBookParamLength(book.getEdition()))
            return "Edition should have between 2 and 200 characters: " + book.getEdition();
        if (book.getPublicationYear() < 1455 || book.getPublicationYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            return "Publication year should be between 1455 and current: " + book.getPublicationYear();
        }
        if (book.getNbPages() < 1 || book.getNbPages() > 9999) {
            return "NbPages should be between 1 and 9 999, please recheck: " + book.getNbPages();
        }
        if (checkBookParamLength(book.getKeywords()))
            return "Keyword list should be between 2 and 200 characters: " + book.getKeywords();
        String keywords = replaceSeparatorWithWhiteSpace(book.getKeywords());
        book.setKeywords(keywords);

        return "";
    }

    boolean checkBookParamLength(String param) {
        if (param != null) {
            return param.length() > 2 && param.length() < 200;

        }
        return false;
    }

    private boolean checkIsbnLength(Book book) {

        return book.getIsbn().length() != 10 && book.getIsbn().length() != 13;
    }*/

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


   /* String checkRequiredValuesNotNull(Book book) {
        if (book == null) return "book is null";

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
    }*/

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
    public List<Book> getBooksByCriterias(Map<String, String> map) {
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
        if (book != null && b != null) {
            if (book.getEdition() != null && !book.getEdition().equals("") && !book.getEdition().equals("?")) {
                logger.info("got you");
                b.setEdition(book.getEdition());
            }

            return b.getEdition();
        }
        return null;
    }

    String transferAuthorToSimilarBooks(Book book, Book b) {
        if (book.getAuthor() != null && !book.getAuthor().equals("") && !book.getAuthor().equals("?")) {
            b.setAuthor(book.getAuthor());
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
                b2.setInsertDate(new Date());
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
