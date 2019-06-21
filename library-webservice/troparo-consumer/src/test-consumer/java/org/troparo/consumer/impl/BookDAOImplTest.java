package org.troparo.consumer.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
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
/*import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;*/

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class BookDAOImplTest {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private BookDAO bookDAO;

    @Sql({"classpath:/src/main/resources/resetDb.sql"})
    @BeforeEach
    void reset() {
        logger.info("reset db");
    }

    @Test
    @DisplayName("should return the books from db (checking the number)")
    void getBooks() {
        assertEquals(5, bookDAO.getBooks().size());
    }

    @Test
    @DisplayName("should return an exception is sessionFactory is null")
    void getBooks1() {
        BookDAOImpl bookDAO1 = new BookDAOImpl();
        bookDAO1.setSessionFactory(null);
        assertEquals(0, bookDAO1.getBooks().size());
    }

    @Test
    @DisplayName("should return true")
    void addBook() {
        Book book = new Book();
        assertAll(
                () -> assertEquals(5, bookDAO.getBooks().size()),
                () -> assertTrue(bookDAO.addBook(book)),
                () -> assertEquals(6, bookDAO.getBooks().size())
        );


    }

    @Test
    @DisplayName("should return false")
    void addBook1() {
        Book book = new Book();
        BookDAOImpl bookDAO1 = new BookDAOImpl();
        bookDAO1.setSessionFactory(null);
       // assertEquals(5, bookDAO1.getBooks().size());
        assertFalse(bookDAO1.addBook(book));
    }

    @Test
    @DisplayName("should return book if id valid")
    void getBookById() {
        Book book = bookDAO.getBookById(3);
        logger.info(book);
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
    @DisplayName("should ignore cases")
    void getBooksByCriterias3() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Title", "grande");
        assertEquals(1, bookDAO.getBooksByCriterias(map).size());

    }

    @Test
    @DisplayName("should return empty list if no criteria")
    void getBooksByCriterias4() {
        HashMap<String, String> map = new HashMap<>();
        assertEquals(0, bookDAO.getBooksByCriterias(map).size());

    }

    @Test
    @DisplayName("should return empty list if any issue")
    void getBooksByCriterias5(){
        BookDAOImpl bookDAO1 = new BookDAOImpl();
        bookDAO1.setSessionFactory(null);
        HashMap<String, String> map = new HashMap<>();
        map.put("Title", "grande");
        assertEquals(0, bookDAO1.getBooksByCriterias(map).size());
    }

    @Test
    @DisplayName("should return empty list if any issue")
    void getBooksByCriterias6(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Title", "e");
        map.put("author", "e");
        assertEquals(2, bookDAO.getBooksByCriterias(map).size());
    }

    @Test
    @DisplayName("should update book author")
    void updateBook() {
        Book book = bookDAO.getBookById(2);

        assertEquals("TEST", book.getAuthor());
        String newAuthor = "Jean Pialat";
        book.setAuthor(newAuthor);
                assertAll(
                ()->assertTrue(bookDAO.updateBook(book)),
                ()->assertEquals(newAuthor, bookDAO.getBookById(2).getAuthor())
        );

    }

    @Test
    @DisplayName("should return an exception")
    void updateBook1(){
        Book book = new Book();
        BookDAOImpl bookDAO1 = new BookDAOImpl();
        bookDAO1.setSessionFactory(null);
        assertFalse(bookDAO1.updateBook(book));
    }

    @Test
    @DisplayName("should remove a book if existing")
    void remove() {
        Book book = bookDAO.getBookById(5);
        assertAll(
                ()-> assertNotNull(book),
                ()->assertTrue(bookDAO.remove(book)),
                ()->assertNull(bookDAO.getBookById(5))
        );

    }

    @Test
    @DisplayName("should return true if trying to remove")
    void remove1() {
        assertTrue(bookDAO.remove(new Book()));
    }

    @Test
    @DisplayName("should return false when removing book")
    void remove2(){
        Book book = new Book();
        BookDAOImpl bookDAO1 = new BookDAOImpl();
        bookDAO1.setSessionFactory(null);
        // assertEquals(5, bookDAO1.getBooks().size());
        assertFalse(bookDAO1.remove(book));
    }

    @Test
    @DisplayName("should return the number of books available")
    void getAvailable1(){
        assertAll(
                ()-> assertEquals(0, bookDAO.getAvailable("1234567824")),
                ()-> assertEquals(3, bookDAO.getAvailable("12345678OK")),
                ()-> assertEquals(0, bookDAO.getAvailable("fr"))

        );

    }

    @Test
    @DisplayName("should return false if book not available")
    void isAvailable() {
        assertFalse(bookDAO.isAvailable(5));
    }

    @Test
    @DisplayName("should return true if book available")
    void isAvailable1() {
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