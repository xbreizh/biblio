package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.entities.book.AddBookRequestType;
import org.troparo.entities.book.BookTypeIn;
import org.troparo.services.bookservice.BookService;
import org.troparo.services.bookservice.BusinessExceptionBook;

import static org.junit.jupiter.api.Assertions.*;
@ContextConfiguration("classpath:/application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional
class BookServiceImplTest {

    private BookService bookService;

    @BeforeEach
    void init(){
        bookService = new BookService();
    }

  /*  @Test
    void addBook() throws BusinessExceptionBook {
        AddBookRequestType parameters = new AddBookRequestType();
        BookTypeIn bookTypeIn = new BookTypeIn();
        bookTypeIn.setAuthor("Molko");
        bookTypeIn.setEdition("Durochelle");
        bookTypeIn.setISBN("ISBN123");
        bookTypeIn.setKeywords("pluie, vent, nuage");
        bookTypeIn.setPublicationYear(1233);
        bookTypeIn.setNbPages(2323);
        bookTypeIn.setTitle("mariko");
        parameters.setBookTypeIn(bookTypeIn);
        parameters.setToken("token123");
        System.out.println(bookService.getBookServicePort().addBook(parameters));
    }*/

/*    @Test
    void updateBook() {
        fail();
    }

    @Test
    void addCopy() {
        fail();
    }

    @Test
    void getBookById() {
        fail();
    }

    @Test
    void getAllBooks() {
        fail();
    }

    @Test
    void isAvailable() {
        fail();
    }

    @Test
    void getBookByCriterias() {
        fail();
    }

    @Test
    void removeBook() {
        fail();
    }

    @Test
    void getAvailable() {
        fail();
    }*/
}