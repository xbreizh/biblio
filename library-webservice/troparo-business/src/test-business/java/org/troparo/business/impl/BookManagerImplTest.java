package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookManagerImplTest {

    private BookManager bookManager;
    private BookDAO bookDAO;


    @BeforeEach
    void init(){
        bookDAO = mock(BookDAO.class);
        bookManager = new BookManagerImpl();
        bookManager.setBookDAO(bookDAO);
    }

    @Test
    @DisplayName("should say that the ISBN already exist")
    void addBook() {
        Book book = new Book();
        book.setIsbn("isbn234");
        when(bookDAO.existingISBN("ISBN234")).thenReturn(true);
        assertEquals("ISBN already existing", bookManager.addBook(book));

    }

    @Test
    @DisplayName("should say that the ISBN already exist")
    void addBook1() {
        Book book = new Book();
        book.setIsbn("isbn234");
        when(bookDAO.existingISBN("ISBN234")).thenReturn(true);
        assertEquals("ISBN already existing", bookManager.addBook(book));

    }

    @Test
    @DisplayName("should return books from DAO")
    void getBooks() {
        List<Book> bookList = new ArrayList<>();
        when(bookDAO.getBooks()).thenReturn(bookList);
        assertEquals(bookList, bookManager.getBooks());
    }

    @Test
    void getBookById() {
    }

    @Test
    void getBooksByCriterias() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void getBookByIsbn() {
        Book book = new Book();
        when(bookDAO.getBookByIsbn("ISBN22")).thenReturn(book);
        assertEquals(book,bookManager.getBookByIsbn("isbn22"));
    }

    @Test
    void remove() {
    }

    @Test
    void getNbAvailable() {
    }

    @Test
    void addCopy() {
    }

    @Test
    void isAvailable() {
    }
}