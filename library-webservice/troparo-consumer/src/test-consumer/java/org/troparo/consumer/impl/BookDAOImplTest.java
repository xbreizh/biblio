package org.troparo.consumer.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class BookDAOImplTest {

    @Inject
    private BookDAO bookDAO;

    @Sql({"classpath:/resetDb.sql"})
    @BeforeEach
    void reset(){
        System.out.println("reset db");
    }

    @Test
    @DisplayName("should return the books from db (checking the number)")
    void getBooks() {
        assertEquals(5, bookDAO.getBooks().size());
    }

    @Test
    @DisplayName("")
    void addBook() {
        Book book = new Book();
        assertEquals(5, bookDAO.getBooks().size());
        bookDAO.addBook(book);
        assertEquals(6, bookDAO.getBooks().size());
    }

    @Test
    @DisplayName("should return book if id valid")
    void getBookById() {
        Book book = bookDAO.getBookById(3);
        System.out.println(book);
        assertAll(
                () -> assertEquals("MAURICE MOSS", book.getAuthor()),
                () -> assertEquals("GALLIMARD", book.getEdition()),
                () -> assertEquals("IT  MOON  ADVENTURE", book.getKeywords())
        );
    }

    @Test
    @DisplayName("should return null if id invalid")
    void getBookById1() {
        assertNull(bookDAO.getBookById(33));
    }

    @Test
    @DisplayName("should return true if existing ISBN")
    void existingISBN() {
        assertTrue(bookDAO.existingISBN("12345678Ok"));
    }

    @Test
    @DisplayName("should return false if none-existing ISBN")
    void existingISBN1() {
        assertFalse(bookDAO.existingISBN("12345678Ow"));
    }

    @Test
    @DisplayName("should ignore invalid keys ")
    void getBooksByCriterias() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Titlde", "jpolinfo");
        assertEquals(0, bookDAO.getBooksByCriterias(map).size());

    }

    @Test
    @DisplayName("")
    void getBooksByCriterias1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Title", "jpolinfo");
        assertEquals(0, bookDAO.getBooksByCriterias(map).size());

    }

    @Test
    @DisplayName("should return empty list if map is null")
    void getBooksByCriterias2() {

        assertEquals(0, bookDAO.getBooksByCriterias(null).size());

    }

    @Test
    @DisplayName("should update book author")
    void updateBook() {
        Book book = bookDAO.getBookById(2);
        String newAuthor = "Jean Pialat";
        assertEquals("TEST", book.getAuthor());
        book.setAuthor(newAuthor);
        bookDAO.updateBook(book);
        assertEquals(newAuthor, bookDAO.getBookById(2).getAuthor());
    }

    @Test
    @DisplayName("should remove a book if existing")
    void remove() {
        Book book = bookDAO.getBookById(5);
        assertNotNull(book);
        bookDAO.remove(book);
        assertNull(bookDAO.getBookById(5));
    }

    @Test
    @DisplayName("should return true if trying to remove")
    void remove1() {
        assertTrue(bookDAO.remove(new Book()));
    }

    @Test
    @DisplayName("should return false if book not available")
    void getAvailable() {
        assertFalse(bookDAO.isAvailable(5));
    }

    @Test
    @DisplayName("should return true if book available")
    void isAvailable() {
        assertTrue(bookDAO.isAvailable(9));
    }

    @Test
    @DisplayName("should return not null if ISBN valid")
    void getBookByIsbn() {
        assertNotNull(bookDAO.getBookByIsbn("12345678oK"));
    }

    @Test
    @DisplayName("should return null if ISBN invalid")
    void getBookByIsbn1() {
        assertNull(bookDAO.getBookByIsbn("12345678pK"));
    }
}