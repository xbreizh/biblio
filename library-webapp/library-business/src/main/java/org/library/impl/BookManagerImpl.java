package org.library.impl;

import org.apache.log4j.Logger;
import org.library.contract.BookManager;
import org.library.model.Book;
import org.springframework.security.core.context.SecurityContext;
import org.troparo.entities.book.*;
import org.troparo.services.bookservice.BookService;
import org.troparo.services.bookservice.BusinessExceptionBook;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Named
public class BookManagerImpl implements BookManager {
    private static final Logger logger = Logger.getLogger(BookManagerImpl.class);
    /*private String token="";
    private String login="";
    private Book book=null;*/


    @Override
    public List<Book> searchBooks(String token, HashMap criterias) {
        List<Book> result = new ArrayList<>();
        BookService bookService = new BookService();
        GetBookByCriteriasRequestType requestType = new GetBookByCriteriasRequestType();
        requestType.setToken(token);
        requestType.setBookCriterias(convertCriteriasIntoCriteriasRequest(criterias));
        GetBookByCriteriasResponseType responseType = new GetBookByCriteriasResponseType();
        try {
            logger.info(requestType.getBookCriterias().getAuthor());
            logger.info(requestType.getToken());
            responseType = bookService.getBookServicePort().getBookByCriterias(requestType);
        } catch (BusinessExceptionBook businessExceptionBook) {
            logger.error("error trying to get the result");
            logger.error(businessExceptionBook.getMessage());
        }

        logger.info("result: "+responseType.getBookListType().getBookTypeOut().size());
        result = convertBookTypeOutListIntoBookList(token, responseType.getBookListType().getBookTypeOut());
        logger.info("result: "+result);
        return result;
    }

    private List<Book> convertBookTypeOutListIntoBookList(String token, List<BookTypeOut> bookTypeOutList) {
        List<Book> bookList = new ArrayList<>();
        for (BookTypeOut bookTypeOut: bookTypeOutList
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

    private int settingNbAvailable(String token, String isbn) {
        int available = 0;
        BookService bookService = new BookService();
        GetAvailableRequestType requestType = new GetAvailableRequestType();
        requestType.setISBN(isbn.toUpperCase());
        requestType.setToken(token);
        try {
            GetAvailableResponseType responseType = new GetAvailableResponseType();
            responseType = bookService.getBookServicePort().getAvailable(requestType);
            logger.info("getting: "+responseType.getReturn());
            available = responseType.getReturn();
        } catch (BusinessExceptionBook businessExceptionBook) {
            logger.error(businessExceptionBook.getMessage());
        }

        return available;
    }

    private BookCriterias convertCriteriasIntoCriteriasRequest(HashMap<String, String> criterias) {
        BookCriterias bookCriterias = new BookCriterias();
            bookCriterias.setISBN(criterias.get("ISBN"));
            logger.info("added isbn: ");

            bookCriterias.setTitle(criterias.get("TITLE"));
            logger.info("added title: ");

            bookCriterias.setAuthor(criterias.get("AUTHOR"));
            logger.info("added author: ");


        logger.info("author passed: "+bookCriterias.getAuthor());
        logger.info("title passed: "+bookCriterias.getTitle());
        logger.info("isbn passed: "+bookCriterias.getISBN());
        
        return bookCriterias;
    }


    private Book convertBookTypeOutIntoBook(BookTypeOut bookTypeOut) {
        logger.info("trying to convert book");
        Book book = new Book();
        book.setId(bookTypeOut.getId());
        book.setIsbn(bookTypeOut.getISBN());
        book.setTitle(bookTypeOut.getTitle());
        book.setAuthor(bookTypeOut.getAuthor());
        book.setEdition(bookTypeOut.getEdition());
        book.setPublicationYear(bookTypeOut.getPublicationYear());
        book.setNbPages(bookTypeOut.getNbPages());
        book.setKeywords(bookTypeOut.getKeywords());
        logger.info("book converted: "+book);
        return book;
    }

}
