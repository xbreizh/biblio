package org.library.business.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.library.business.contract.LoanManager;
import org.troparo.entities.book.*;
import org.troparo.services.bookservice.BookService;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.bookservice.IBookService;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookManagerImplTest {

    private static Logger logger = Logger.getLogger(BookManagerImpl.class);
    private BookManagerImpl bookManager;
    private BookService bookService;
    private IBookService iBookService;

    @BeforeEach
    void init() {
        bookManager = new BookManagerImpl();
        bookService = mock(BookService.class);
        bookManager.setBookService(bookService);
        iBookService = mock(IBookService.class);
        LoanManager loanManager1 = mock(LoanManager.class);
        MemberManagerImpl memberManager = new MemberManagerImpl(loanManager1, bookManager);
        LoanManagerImpl loanManager = mock(LoanManagerImpl.class);
        //memberManager.setLoanManager(loanManager);
    }


    @Test
    @DisplayName("should return books")
    void searchBooks() throws BusinessExceptionBook {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("AUTHOR", "moss");
        GetBookByCriteriasResponseType response = new GetBookByCriteriasResponseType();
        BookListType bookListType = new BookListType();
        BookTypeOut bookTypeOut = new BookTypeOut();
        String title = "blop";
        String isbn = "isbn123";
        bookTypeOut.setISBN(isbn);
        bookTypeOut.setTitle(title);
        bookListType.getBookTypeOut().add(bookTypeOut);
        response.setBookListType(bookListType);
        logger.info(bookService.getBookServicePort());
        GetAvailableResponseType getAvailableResponseType = new GetAvailableResponseType();
        getAvailableResponseType.setReturn(3);
        when(bookService.getBookServicePort()).thenReturn(iBookService);
        when(bookService.getBookServicePort().getAvailable(any(GetAvailableRequestType.class))).thenReturn(getAvailableResponseType);
        when(bookService.getBookServicePort().getBookByCriterias(any(GetBookByCriteriasRequestType.class))).thenReturn(response);
        assertEquals(title, bookManager.searchBooks("", criterias).get(0).getTitle());

    }


    @Test
    void getBookByISBN() throws BusinessExceptionBook {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("ISBN", "isbn123");
        GetBookByCriteriasResponseType response = new GetBookByCriteriasResponseType();
        BookListType bookListType = new BookListType();
        BookTypeOut bookTypeOut = new BookTypeOut();
        String title = "blop";
        String isbn = "isbn123";
        bookTypeOut.setISBN(isbn);
        bookTypeOut.setTitle(title);
        bookListType.getBookTypeOut().add(bookTypeOut);
        response.setBookListType(bookListType);
        GetAvailableResponseType getAvailableResponseType = new GetAvailableResponseType();
        getAvailableResponseType.setReturn(3);
        when(bookService.getBookServicePort()).thenReturn(iBookService);
        when(bookService.getBookServicePort().getAvailable(any(GetAvailableRequestType.class))).thenReturn(getAvailableResponseType);
        when(bookService.getBookServicePort().getBookByCriterias(any(GetBookByCriteriasRequestType.class))).thenReturn(response);
        assertEquals(title, bookManager.getBookByISBN("", isbn).getTitle());

    }

    @Test
    void getNbAvailable() throws BusinessExceptionBook {
        String isbn = "isbn123";
        GetAvailableResponseType getAvailableResponseType = new GetAvailableResponseType();
        getAvailableResponseType.setReturn(3);
        when(bookService.getBookServicePort()).thenReturn(iBookService);
        when(bookService.getBookServicePort().getAvailable(any(GetAvailableRequestType.class))).thenReturn(getAvailableResponseType);
        assertEquals(3, bookManager.getNbAvailable("", isbn));
    }

    @Test
    void setBookService() {
        BookService bookService = new BookService();
        bookManager.setBookService(bookService);
        assertEquals(bookService, bookManager.getBookService());
    }

    @Test
    void getBookService() {
        BookService bookService = new BookService();
        bookManager.setBookService(bookService);
        assertEquals(bookService, bookManager.getBookService());
    }
}