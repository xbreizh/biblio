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
    private static  Logger logger = Logger.getLogger(BookManagerImpl.class);

    private BookService bookService;


    @Override
    public List<Book> searchBooks(String token, Map<String, String> criteria) throws BusinessExceptionBook {
        List<Book> result;
        GetBookByCriteriasRequestType requestType = new GetBookByCriteriasRequestType();
        requestType.setToken(token);
        requestType.setBookCriterias(convertCriteriasIntoCriteriasRequest(criteria));
        GetBookByCriteriasResponseType responseType;
        logger.info(requestType.getBookCriterias().getAuthor());
        logger.info(requestType.getToken());
        responseType = getBookServicePort().getBookByCriterias(requestType);

        result = convertBookTypeOutListIntoBookList(token, responseType.getBookListType().getBookTypeOut());
        logger.info("result: " + result);
        return result;
    }

    IBookService getBookServicePort() {
        if (bookService == null) bookService = new BookService();
        return bookService.getBookServicePort();
    }

    List<Book> convertBookTypeOutListIntoBookList(String token, List<BookTypeOut> bookTypeOutList) throws BusinessExceptionBook {
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
            book.setNbAvailable(getNbAvailable(token, book.getIsbn()));
            bookList.add(book);
        }
        return bookList;
    }

    public int getNbAvailable(String token, String isbn) throws BusinessExceptionBook {
        GetAvailableRequestType requestType = new GetAvailableRequestType();
        requestType.setISBN(isbn.toUpperCase());
        requestType.setToken(token);
        GetAvailableResponseType responseType;
        responseType = getBookServicePort().getAvailable(requestType);
        logger.info("getting: " + responseType.getReturn());


        return responseType.getReturn();
    }

    BookCriterias convertCriteriasIntoCriteriasRequest(Map<String, String> criterias) {
        logger.info("criterias: " + criterias);
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
