package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookManagerImplTest {

    private BookManager bookManager;
    private BookDAO bookDAO;
    BookManagerImpl bookManager2 = new BookManagerImpl();


    @BeforeEach
    void init() {
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
    @DisplayName("should return error if isbn not 10 or 13 characters")
    void checkInsertion() {
        Book book = new Book();
        book.setIsbn("1234d");
        assertEquals("ISBN must be 10 or 13 characters: 1234d", bookManager2.checkInsertion(book));
    }

    @Test
    @DisplayName("should return false if param less than 2 or more than 200 characters")
    void checkBookParamLength() {
        Book book = new Book();
        book.setTitle("m");
        assertEquals(false, bookManager2.checkBookParamLength(book, book.getTitle()));
    }

    @Test
    @DisplayName("should return false if param is null")
    void checkBookParamLength1() {
        Book book = new Book();
        assertEquals(false, bookManager2.checkBookParamLength(book, book.getAuthor()));
    }

    @Test
    @DisplayName("should return true if param between 2 and 200 characters")
    void checkBookParamLength2() {
        Book book = new Book();
        book.setEdition("momo");
        assertEquals(true, bookManager2.checkBookParamLength(book, book.getEdition()));
    }


    @Test
    @DisplayName("should return books from DAO")
    void getBooks() {
        List<Book> bookList = new ArrayList<>();
        when(bookDAO.getBooks()).thenReturn(bookList);
        assertEquals(bookList, bookManager.getBooks());
    }

    @Test
    @DisplayName("should remove whitespaces from keywords")
    void cleanKeywords() {
        String keywords = "the, cafe, sucre, pollen, miel, abeille        ";
        assertEquals("the  cafe  sucre  pollen  miel  abeille        ", bookManager2.replaceSeparatorWithWhiteSpace(keywords));
    }

    @Test
    @DisplayName("should return a book")
    void getBookById() {
        Book book = new Book();
        when(bookDAO.getBookById(anyInt())).thenReturn(book);
        assertEquals(book, bookManager.getBookById(12));
    }

    @Test
    @DisplayName("should return null")
    void getBookById1() {
        Book book = new Book();
        when(bookDAO.getBookById(anyInt())).thenReturn(null);
        assertEquals(null, bookManager.getBookById(12));
    }

    @Test
    @DisplayName("should return a list of books")
    void getBooksByCriterias() {
        List<Book> bookList = new ArrayList<>();
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("Test", "Test");
        when(bookDAO.getBooksByCriterias(criterias)).thenReturn(bookList);
        assertEquals(bookList, bookManager.getBooksByCriterias(criterias));
    }


    @Test
    @DisplayName("should remove invalid entries from criterias Hashmap")
    void removeInvalidEntriesFromCriterias() {
        HashMap<String, String> criterias = new HashMap<>();
        HashMap<String, String> criteriaResults = new HashMap<>();

        criterias.put("Test", "Test");
        criterias.put("Author", "");
        criterias.put("ISBN", "123");
        criterias.put("", "");
        criterias.put("", "?");

        criteriaResults.put("ISBN", "123");

        assertEquals(criteriaResults, bookManager2.removeInvalidEntriesFromCriterias(criterias));
    }

    @Test
    void updateBook() {
        fail();
    }

    @Test
    @DisplayName("should return false if book ISBN is null")
    void checksThatBookHasAnISBN() {
        Book book = new Book();
        assertFalse(bookManager2.checksThatBookHasAnISBN(book));

    }

    @Test
    @DisplayName("should return false if book ISBN is empty ")
    void checksThatBookHasAnISBN1() {
        Book book = new Book();
        book.setIsbn("");
        assertFalse(bookManager2.checksThatBookHasAnISBN(book));

    }

    @Test
    @DisplayName("should return false if book ISBN equals to \"?\" ")
    void checksThatBookHasAnISBN2() {
        Book book = new Book();
        book.setIsbn("?");
        assertFalse(bookManager2.checksThatBookHasAnISBN(book));
    }

    @Test
    void getBookByIsbn() {
        Book book = new Book();
        when(bookDAO.getBookByIsbn("ISBN22")).thenReturn(book);
        assertEquals(book, bookManager.getBookByIsbn("isbn22"));
    }


    @Test
    @DisplayName("should ignore empty values from parameters")
    void ignoreEmptyCriteriasFromBook(){
        Book book = new Book();
        List<Book> bookList= new ArrayList<>();
        book.setIsbn("");
        fail();

    }
    @Test
    @DisplayName("should return exception if no book found")
    void remove() {
        when(bookDAO.getBookById(anyInt())).thenReturn(null);
        assertEquals("No item found", bookManager.remove(12));
    }

    @Test
    @DisplayName("should remove book if found")
    void remove1() {
        when(bookDAO.getBookById(anyInt())).thenReturn(new Book());
        assertEquals("", bookManager.remove(12));
    }

    @Test
    @DisplayName("should return number of books available")
    void getNbAvailable() {
        when(bookDAO.getAvailable(anyString())).thenReturn(3);
        assertEquals(3, bookManager.getNbAvailable("isbn123"));
    }

    @Test
    @DisplayName("should return a String error")
    void addCopy() {
        when(bookDAO.existingISBN(anyString())).thenReturn(false);
        assertEquals("No record found with that ISBN", bookManager.addCopy("ISBN123", 3));
    }


    @Test
    @DisplayName("should return a String error if error while adding the book in dao")
    void addCopy1() {

        when(bookDAO.getBookByIsbn(anyString())).thenReturn(new Book());
        when(bookDAO.existingISBN(anyString())).thenReturn(true);
        when(bookDAO.addBook(any(Book.class))).thenReturn(false);
        assertEquals("Issue while adding copies for: ISBN123", bookManager.addCopy("ISBN123", 3));
    }

    @Test
    @DisplayName("should add the book")
    void addCopy2() {

        when(bookDAO.getBookByIsbn(anyString())).thenReturn(new Book());
        when(bookDAO.existingISBN(anyString())).thenReturn(true);
        when(bookDAO.addBook(any(Book.class))).thenReturn(true);
        assertEquals("", bookManager.addCopy("ISBN123", 3));
    }

    @Test
    void isAvailable() {
        when(bookDAO.isAvailable(anyInt())).thenReturn(true);
        assertTrue(bookManager.isAvailable(12));
    }
}