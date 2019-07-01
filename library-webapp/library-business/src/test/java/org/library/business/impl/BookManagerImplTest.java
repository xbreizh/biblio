package org.library.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.contract.BookManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.entities.book.*;
import org.troparo.services.bookservice.BookService;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.bookservice.IBookService;

import javax.inject.Inject;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookManagerImpl.class)
class BookManagerImplTest {

    @Inject
    BookManagerImpl bookManager;

    private  BookService bookService;

    @BeforeEach
    void init(){
        bookService = mock(BookService.class);
        //bookManager.setBookService(bookService);

    }

  /*  @Test
    @DisplayName("should set BookService")
    void setBookService() {
        BookService bookService = new BookService();
        bookManager.setBookService(bookService);
        assertEquals(bookService, bookManager.getBookService());
    }*/

    @Test
    @DisplayName("should return book")
    void searchBooks() throws BusinessExceptionBook {
        GetBookByCriteriasRequestType requestType = new GetBookByCriteriasRequestType();
        requestType.setToken("");
        requestType.setBookCriterias(new BookCriterias());
        GetBookByCriteriasResponseType responseType = new GetBookByCriteriasResponseType();
        BookListType bookListType = new BookListType();
        BookTypeOut bookTypeOut = new BookTypeOut();
        String edition = "edito";
        bookTypeOut.setEdition(edition);
        bookListType.getBookTypeOut().add(bookTypeOut);
        responseType.setBookListType(bookListType);
        IBookService iBookService = mock(IBookService.class);
        //when(bookManager.getBookServicePort()).thenReturn(iBookService);
        //when(bookService.getBookServicePort().getBookByCriterias(requestType)).thenReturn(responseType);
        assertEquals(edition, responseType.getBookListType().getBookTypeOut().get(0).getEdition());


    }

 /*   @Test
    @DisplayName("should return exception")
    void searchBooks1() throws BusinessExceptionBook {
        GetBookByCriteriasRequestType requestType = new GetBookByCriteriasRequestType();
        requestType.setToken("");
        requestType.setBookCriterias(new BookCriterias());
        GetBookByCriteriasResponseType responseType = new GetBookByCriteriasResponseType();
        BookListType bookListType = new BookListType();
        BookTypeOut bookTypeOut = new BookTypeOut();
        String edition = "edito";
        bookTypeOut.setEdition(edition);
        bookListType.getBookTypeOut().add(bookTypeOut);
        responseType.setBookListType(bookListType);
        IBookService iBookService = mock(IBookService.class);
        when(bookManager.getBookServicePort().getBookByCriterias(requestType)).thenThrow(new BusinessExceptionBook());
        when(bookService.getBookServicePort().getBookByCriterias(requestType)).thenReturn(responseType);
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("AUTHOR", "moss");
        assertThrows(BusinessExceptionBook.class, ()->bookManager.searchBooks("",criterias ));


    }*/

}