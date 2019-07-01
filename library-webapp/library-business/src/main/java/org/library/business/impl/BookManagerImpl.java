package org.library.business.impl;

import org.apache.log4j.Logger;
import org.library.business.contract.BookManager;
import org.library.model.Book;
import org.troparo.entities.book.*;
import org.troparo.services.bookservice.BookService;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.bookservice.IBookService;


import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class BookManagerImpl implements BookManager {
    private static final Logger logger = Logger.getLogger(BookManagerImpl.class);

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }


    public BookService getBookService() {
        return bookService;
    }

    private BookService bookService;


    @Override
    public List<Book> searchBooks(String token, Map<String, String> criterias) {
        List<Book> result;
        bookService = new BookService();
        GetBookByCriteriasRequestType requestType = new GetBookByCriteriasRequestType();
        requestType.setToken(token);
        requestType.setBookCriterias(convertCriteriasIntoCriteriasRequest(criterias));
        GetBookByCriteriasResponseType responseType = new GetBookByCriteriasResponseType();
        try {
            logger.info(requestType.getBookCriterias().getAuthor());
            logger.info(requestType.getToken());
            responseType = getBookServicePort().getBookByCriterias(requestType);
        } catch (BusinessExceptionBook businessExceptionBook) {
            logger.error("error trying to get the result");
            logger.error(businessExceptionBook.getMessage());
        }

        logger.info("result: " + responseType.getBookListType().getBookTypeOut().size());
        result = convertBookTypeOutListIntoBookList(token, responseType.getBookListType().getBookTypeOut());
        logger.info("result: " + result);
        return result;
    }

    IBookService getBookServicePort() {
        return bookService.getBookServicePort();
    }

    List<Book> convertBookTypeOutListIntoBookList(String token, List<BookTypeOut> bookTypeOutList) {
        List<Book> bookList = new ArrayList<>();
        for (BookTypeOut bookTypeOut : bookTypeOutList
        ) {
            Book book = new Book();
            book.setId(bookTypeOut.getId());
            book.setIsbn(bookTypeOut.getISBN());
            book.setTitle(bookTypeOut.getTitle());
            book.setAuthor(bookTypeOut.getAuthor());
            book.setEdition(bookTypeOut.getEdition());
            book.setPublicationYear(bookTypeOut.getPublicationYear());
            book.setNbPages(bookTypeOut.getNbPages());
            book.setKeywords(bookTypeOut.getKeywords());
            book.setNbAvailable(settingNbAvailable(token, book.getIsbn()));
            bookList.add(book);
        }
        return bookList;
    }

    int settingNbAvailable(String token, String isbn) {
        int available = 0;
        BookService bookService = new BookService();
        GetAvailableRequestType requestType = new GetAvailableRequestType();
        requestType.setISBN(isbn.toUpperCase());
        requestType.setToken(token);
        try {
            GetAvailableResponseType responseType;
            responseType = bookService.getBookServicePort().getAvailable(requestType);
            logger.info("getting: " + responseType.getReturn());
            available = responseType.getReturn();
        } catch (BusinessExceptionBook businessExceptionBook) {
            logger.error(businessExceptionBook.getMessage());
        }

        return available;
    }

    BookCriterias convertCriteriasIntoCriteriasRequest(Map<String, String> criterias) {
        logger.info("criterias: "+criterias);
        BookCriterias bookCriterias = new BookCriterias();
        bookCriterias.setISBN(criterias.get("ISBN"));
        logger.info("added isbn: ");

        bookCriterias.setTitle(criterias.get("TITLE"));
        logger.info("added title: ");

        bookCriterias.setAuthor(criterias.get("AUTHOR"));
        logger.info("added author: ");


        logger.info("author passed: " + bookCriterias.getAuthor());
        logger.info("title passed: " + bookCriterias.getTitle());
        logger.info("isbn passed: " + bookCriterias.getISBN());

        return bookCriterias;
    }


}
