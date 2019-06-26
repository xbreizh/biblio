package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.entities.book.*;
import org.troparo.model.Book;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.bookservice.IBookService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@WebService(serviceName = "BookService", endpointInterface = "org.troparo.services.bookservice.IBookService",
        targetNamespace = "http://troparo.org/services/BookService/", portName = "BookServicePort", name = "BookServiceImpl")
public class BookServiceImpl implements IBookService {
    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Inject
    private BookManager bookManager;

    @Inject
    private ConnectServiceImpl authentication;

    private String exception = "";
    private List<Book> bookList = new ArrayList<>();
    private BookTypeOut bookTypeOut = null;
    private BookTypeIn bookTypeIn = null;
    private BookListType bookListType = new BookListType();
    private Book book = null;

    // Create
    @Override
    public AddBookResponseType addBook(AddBookRequestType parameters) throws BusinessExceptionBook {
        AddBookResponseType ar = new AddBookResponseType();
        ar.setReturn(true);
        checkAuthentication(parameters.getToken());
        if (!checkIfBookHasLegitArguments(parameters.getBookTypeIn())) {
            logger.info(exception);
            throw new BusinessExceptionBook(exception);
        }

        bookTypeIn = parameters.getBookTypeIn();
        this.book = convertBookTypeInIntoBook(bookTypeIn);
        logger.info("bookManager: " + bookManager);
        exception = bookManager.addBook(book);
        if (!exception.equals("")) {
            logger.info(exception);
            throw new BusinessExceptionBook(exception);
        }

        return ar;
    }

    private boolean checkIfBookHasLegitArguments(BookTypeIn bookTypeIn) {
        String isbn = bookTypeIn.getISBN();
        String title = bookTypeIn.getTitle();
        String author = bookTypeIn.getAuthor();
        String edition = bookTypeIn.getEdition();
        String keywords = bookTypeIn.getKeywords();
        int nbPages = bookTypeIn.getNbPages();
        int publicationYear = bookTypeIn.getPublicationYear();
        if (isbn == null || title == null || author == null || edition == null || keywords == null) return false;
        if (nbPages == 0 || nbPages == -1 || publicationYear == 0 || publicationYear == -1) return false;
        String[] attributeArray = {isbn, title, author, edition, keywords};

        for (String str : attributeArray
        ) {
            if (str.equals("") || str.equals("?")) return false;
        }
        return true;

    }

    // Converts Input into Book for business
    private Book convertBookTypeInIntoBook(BookTypeIn bookTypeIn) {
        book = new Book();
        book.setIsbn(bookTypeIn.getISBN().toUpperCase());
        book.setTitle(bookTypeIn.getTitle().toUpperCase());
        book.setAuthor(bookTypeIn.getAuthor().toUpperCase());
        logger.info(bookTypeIn.getPublicationYear());
        book.setPublicationYear(bookTypeIn.getPublicationYear());
        book.setEdition(bookTypeIn.getEdition().toUpperCase());
        book.setNbPages(bookTypeIn.getNbPages());
        book.setKeywords(bookTypeIn.getKeywords().toUpperCase());
        logger.info("pub date: " + book.getPublicationYear());
        return book;
    }

    // Converts Input into Book for business
    private void convertBookTypeUpdateIntoBook(BookTypeUpdate bookTypeUpdate) {
        book = new Book();
        book.setIsbn(bookTypeUpdate.getISBN().toUpperCase());
        book.setTitle(bookTypeUpdate.getTitle().toUpperCase());
        book.setAuthor(bookTypeUpdate.getAuthor().toUpperCase());
        logger.info(bookTypeUpdate.getPublicationYear());
        book.setPublicationYear(bookTypeUpdate.getPublicationYear());
        book.setEdition(bookTypeUpdate.getEdition().toUpperCase());
        book.setNbPages(bookTypeUpdate.getNbPages());
        book.setKeywords(bookTypeUpdate.getKeywords().toUpperCase());
        logger.info("pub date: " + book.getPublicationYear());
    }

    // Update
    @Override
    public UpdateBookResponseType updateBook(UpdateBookRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        org.troparo.entities.book.UpdateBookResponseType ar = new org.troparo.entities.book.UpdateBookResponseType();
        ar.setReturn(true);
        org.troparo.entities.book.BookTypeUpdate bookTypeUpdate = parameters.getBookTypeUpdate();
        logger.info("received: " + bookTypeUpdate);
        // update
        convertBookTypeUpdateIntoBook(bookTypeUpdate);
        logger.info("bookManager: " + bookManager);
        exception = bookManager.updateBook(book);
        if (!exception.equals("")) {
            throw new BusinessExceptionBook(exception);
        }

        return ar;
    }

    @Override
    public AddCopyResponseType addCopy(AddCopyRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        AddCopyResponseType ar = new AddCopyResponseType();
        ar.setReturn(true);
        String isbn = parameters.getISBN().toUpperCase();
        int copies = parameters.getNbCopies();

        logger.info("bookManager: " + bookManager);
        exception = bookManager.addCopy(isbn, copies);
        if (!exception.equals("")) {
            throw new BusinessExceptionBook(exception);
        }

        return ar;
    }

    // Get One
    @Override
    public GetBookByIdResponseType getBookById(GetBookByIdRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        logger.info("new method added");
        GetBookByIdResponseType rep = new GetBookByIdResponseType();
        BookTypeOut bookTypeOut = new org.troparo.entities.book.BookTypeOut();
        Book book = bookManager.getBookById(parameters.getId());
        if (book == null) {
            throw new BusinessExceptionBook("no book found with that bookId");
        } else {
            bookTypeOut.setId(book.getId());
            bookTypeOut.setISBN(book.getIsbn());
            bookTypeOut.setTitle(book.getTitle());
            bookTypeOut.setAuthor(book.getAuthor());
            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setNbPages(book.getNbPages());
            bookTypeOut.setKeywords(book.getKeywords());
            rep.setBookTypeOut(bookTypeOut);
        }
        return rep;
    }

    // Get All


    @Override
    public BookListResponseType getAllBooks(BookListRequestType parameters) throws BusinessExceptionBook {

        checkAuthentication(parameters.getToken());
        bookList = bookManager.getBooks();
        logger.info("size list: " + bookList.size());

        BookListResponseType bookListResponseType = new BookListResponseType();

        convertBookIntoBookTypeOut();
        // add bookType to the movieListType

        bookListResponseType.setBookListType(bookListType);
        return bookListResponseType;
    }

    @Override
    public IsAvailableResponseType isAvailable(IsAvailableRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());
        IsAvailableResponseType ar = new IsAvailableResponseType();
        ar.setReturn(bookManager.isAvailable(parameters.getId()));
        return ar;
    }

    private void checkAuthentication(String token) throws BusinessExceptionBook {
        try {
            authentication.checkToken(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BusinessExceptionBook("invalid token");
        }
    }


    // Get List By Criterias
    @Override
    public GetBookByCriteriasResponseType getBookByCriterias(GetBookByCriteriasRequestType parameters) throws BusinessExceptionBook {
        GetBookByCriteriasResponseType getBookByCriteriasResponseType = new GetBookByCriteriasResponseType();
        checkAuthentication(parameters.getToken());

        BookCriterias criterias = parameters.getBookCriterias();

        Map<String, String> newMap = cleanCriteriasMap(criterias);

        if (newMap.isEmpty()) {
            logger.info("map: " + newMap);
        }
        logger.info(newMap);
        if (newMap.isEmpty()) {
            getBookByCriteriasResponseType.setBookListType(bookListType);
            return getBookByCriteriasResponseType;
        }

        bookList = bookManager.getBooksByCriterias(newMap);
        logger.info("bookListType beg: " + bookListType.getBookTypeOut().size());

        convertBookIntoBookTypeOut();

        logger.info("bookListType end: " + bookListType.getBookTypeOut().size());
        getBookByCriteriasResponseType.setBookListType(bookListType);

        return getBookByCriteriasResponseType;
    }


    private Map<String, String> cleanCriteriasMap(BookCriterias criterias) {
        Map<String, String> map = new HashMap<>();
        if (criterias.getAuthor() != null) map.put("Author", criterias.getAuthor().toUpperCase());
        if (criterias.getTitle() != null) map.put("Title", criterias.getTitle().toUpperCase());
        if (criterias.getISBN() != null) map.put("ISBN", criterias.getISBN().toUpperCase());
        logger.info("map: " + map);

        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry entry : map.entrySet()
        ) {
            if (!entry.getValue().equals("") && !entry.getValue().equals("?")) {
                newMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        logger.info("newMap: " + newMap);
        return newMap;
    }

    // Delete
    @Override
    public RemoveBookResponseType removeBook(RemoveBookRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        RemoveBookResponseType ar = new RemoveBookResponseType();
        ar.setReturn(true);

        logger.info("bookManager: " + bookManager);
        exception = bookManager.remove(parameters.getId());
        if (!exception.equals("")) {
            throw new BusinessExceptionBook(exception);
        }

        return ar;

    }

    // Get number Available
    @Override
    public GetAvailableResponseType getAvailable(GetAvailableRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        GetAvailableResponseType ar = new GetAvailableResponseType();
        int i = bookManager.getNbAvailable(parameters.getISBN().toUpperCase());
        logger.info("i: " + i);
        ar.setReturn(i);

        return ar;
    }

    // Converts Book from Business into output
    private void convertBookIntoBookTypeOut() {

        bookListType.getBookTypeOut().clear();
        for (Book book : bookList) {

            // set values retrieved from DAO class
            bookTypeOut = new org.troparo.entities.book.BookTypeOut();
            bookTypeOut.setId(book.getId());
            bookTypeOut.setISBN(book.getIsbn());
            bookTypeOut.setTitle(book.getTitle());
            bookTypeOut.setAuthor(book.getAuthor());
            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setPublicationYear(book.getPublicationYear());
            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setNbPages(book.getNbPages());
            bookTypeOut.setKeywords(book.getKeywords());
            bookListType.getBookTypeOut().add(bookTypeOut);
        }
        logger.info("bookListType end: " + bookListType.getBookTypeOut().size());
    }

    void setBookManager(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    void setAuthentication(ConnectServiceImpl authentication) {
        this.authentication = authentication;
    }

}
