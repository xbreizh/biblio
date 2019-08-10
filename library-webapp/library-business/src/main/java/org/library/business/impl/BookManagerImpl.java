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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class BookManagerImpl implements BookManager {
    private static final int MAX_BOOK_LIST = 30;
    private static Logger logger = Logger.getLogger(BookManagerImpl.class);
    private BookService bookService;

    @Override
    public BookService getBookService() {
        return bookService;
    }

    @Override
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public List<Book> searchBooks(String token, Map<String, String> criteria) throws BusinessExceptionBook {
        List<Book> result;
        GetBookByCriteriasRequestType requestType = new GetBookByCriteriasRequestType();
        requestType.setToken(token);
        requestType.setBookCriterias(convertCriteriaIntoCriteriaRequest(criteria));
        GetBookByCriteriasResponseType responseType;
        logger.info(requestType.getBookCriterias().getAuthor());
        logger.info(requestType.getToken());
        responseType = getBookServicePort().getBookByCriterias(requestType);

        result = convertBookTypeOutListIntoBookList(token, responseType.getBookListType().getBookTypeOut());
        logger.info("result: " + result);
        if (result.size() >= MAX_BOOK_LIST) {
            return result.subList(0, MAX_BOOK_LIST);
        }
    return result;
    }

    @Override
    public Book getBookByISBN(String token, String isbn) throws BusinessExceptionBook {
        logger.info("getting book by isbn: " + isbn);
        Map<String, String> criteria = new HashMap<>();
        criteria.put("ISBN", isbn);
        List<Book> books = searchBooks(token, criteria);
        logger.info("map passed: " + criteria);

        if (!books.isEmpty()) {
            Book book = books.get(0);
            logger.info("Book found: " + book.getTitle());
            return book;
        }
        logger.info("no book found");
        return null;
    }

    private IBookService getBookServicePort() {
        if (bookService == null) bookService = new BookService();
        return bookService.getBookServicePort();
    }

    private List<Book> convertBookTypeOutListIntoBookList(String token, List<BookTypeOut> bookTypeOutList) throws BusinessExceptionBook {
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

    int getNbAvailable(String token, String isbn) throws BusinessExceptionBook {
        GetAvailableRequestType requestType = new GetAvailableRequestType();
        requestType.setISBN(isbn.toUpperCase());
        requestType.setToken(token);
        GetAvailableResponseType responseType;
        responseType = getBookServicePort().getAvailable(requestType);
        logger.info("getting: " + responseType.getReturn());


        return responseType.getReturn();
    }

    private BookCriterias convertCriteriaIntoCriteriaRequest(Map<String, String> criterias) {
        logger.info("criteria: " + criterias);
        BookCriterias bookCriterias = new BookCriterias();
        bookCriterias.setISBN(criterias.get("ISBN"));
        logger.info("added isbn: ");

        bookCriterias.setTitle(criterias.get("TITLE"));
        logger.info("added title: ");

        bookCriterias.setAuthor(criterias.get("AUTHOR"));
        logger.info("added author: ");

        logger.info("elements received: isbn " + bookCriterias.getISBN() + " / title " + bookCriterias.getTitle() + " / author: " + bookCriterias.getAuthor());

        return bookCriterias;
    }


}
