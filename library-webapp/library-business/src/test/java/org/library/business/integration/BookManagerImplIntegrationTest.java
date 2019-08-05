package org.library.business.integration;


import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.contract.BookManager;
import org.library.business.impl.BookManagerImpl;
import org.library.business.impl.LoanManagerImpl;
import org.library.business.impl.MemberManagerImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.book.*;
import org.troparo.services.bookservice.BookService;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.bookservice.IBookService;
import org.troparo.services.memberservice.IMemberService;
import org.troparo.services.memberservice.MemberService;

import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookManagerImpl.class)
class BookManagerImplIntegrationTest {


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
        MemberManagerImpl memberManager = new MemberManagerImpl();
        LoanManagerImpl loanManager = mock(LoanManagerImpl.class);
        memberManager.setLoanManager(loanManager);
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

   /* @Test
    @DisplayName("should return books")
    void searchBooks1() throws BusinessExceptionBook {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("isbn", "ok");
        assertEquals("LA GRANDE AVENTURE", bookManager.searchBooks("", criterias).get(0).getTitle());

    }

    @Test
    @DisplayName("should return books")
    void getBookByISBN() throws BusinessExceptionBook {
        String isbn = "8574596258";
        assertEquals("BOKANA", bookManager.getBookByISBN("", isbn));

    }


    @Test
    @DisplayName("should return number available")
    void getNbAvailable() throws BusinessExceptionBook {
        assertEquals(4, bookManager.getNbAvailable("", "12345678OK"));

    }*/


}