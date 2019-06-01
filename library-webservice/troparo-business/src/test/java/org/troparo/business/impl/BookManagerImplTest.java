package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.troparo.business.contract.BookManager;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import java.util.ArrayList;
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
        criterias.put("Moko", "");
        criterias.put("", "?");

        criteriaResults.put("ISBN", "123");

        assertEquals(criteriaResults, bookManager2.removeInvalidEntriesFromCriterias(criterias));
    }


    @Test
    void test(){
        Book b1 = new Book();
        Book b2 = b1;
        bookManager2.transferNbPagesToSimilarBooks(b1, b2);
    }

    @Test
    @DisplayName("should return a Null pointer exception if dao returns null")
    void updateBook() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ISBN", "ABC");
        when(bookDAO.getBooksByCriterias(map)).thenReturn(null);
        Book book = new Book();
        book.setIsbn("ABC");
        assertEquals("No Item found with that ISBN", bookManager.updateBook(book));
    }

    @Test
    @DisplayName("should return \"no book provided\" if no book has been provided")
    void updateBook1() {
        assertEquals("No book provided!", bookManager2.updateBook(null));
    }

    @Test
    @DisplayName("should return \"No book to update\" if dao returns an error while updating")
    void updateBook2() {
        HashMap<String, String> map = new HashMap<>();
        List<Book> bookList = new ArrayList<>();
        Book book = new Book();
        book.setIsbn("ABC123");
        when(bookDAO.getBooksByCriterias(map)).thenReturn(bookList);
        assertEquals("No book to update", bookManager.updateBook(book));
    }


    @Test
    void testMock(){
        BookManagerImpl bmanager = mock(BookManagerImpl.class);
        Book book = new Book();
        when(bmanager.checksThatBookHasAnISBN(book)).thenReturn("");
    }

    @Test
    @DisplayName("should return \"You must provide an ISBN\" if ISBN not provided")
    void updateBook3() {
        when(bookDAO.updateBook(any(Book.class))).thenReturn(false);
        assertEquals("You must provide an ISBN", bookManager2.updateBook(new Book()));
    }

    @Test
    @DisplayName("should return \"Issue while updating\" if dao returns an error while updating")
    void updateBook4() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ISBN", "ABC123");
        List<Book> bookList = new ArrayList<>();
        Book book = new Book();
        book.setIsbn("ABC123");
        bookList.add(book);
        when(bookDAO.getBooksByCriterias(map)).thenReturn(bookList);
        when(bookDAO.updateBook(book)).thenReturn(false);
        assertEquals("Issue while updating", bookManager.updateBook(book));
    }

    @Test
    @DisplayName("should return empty string if no error while updating")
    void updateBook5() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ISBN", "ABC123");
        List<Book> bookList = new ArrayList<>();
        Book book = new Book();
        book.setIsbn("ABC123");
        bookList.add(book);
        when(bookDAO.getBooksByCriterias(map)).thenReturn(bookList);
        when(bookDAO.updateBook(book)).thenReturn(true);
        assertEquals("", bookManager.updateBook(book));
    }


    @Test
    @DisplayName("should return empty string if book ISBN is not empty")
    void checksThatBookHasAnISBN0() {
        Book book = new Book();
        book.setIsbn("ABC");
        assertEquals("", bookManager2.checksThatBookHasAnISBN(book));

    }

    @Test
    @DisplayName("should return false if book ISBN is null")
    void checksThatBookHasAnISBN() {
        Book book = new Book();
        assertEquals("You must provide an ISBN", bookManager2.checksThatBookHasAnISBN(book));

    }

    @Test
    @DisplayName("should return false if book ISBN is empty ")
    void checksThatBookHasAnISBN1() {
        Book book = new Book();
        book.setIsbn("");
        assertEquals("You must provide an ISBN", bookManager2.checksThatBookHasAnISBN(book));

    }

    @Test
    @DisplayName("should return false if book ISBN equals to \"?\" ")
    void checksThatBookHasAnISBN2() {
        Book book = new Book();
        book.setIsbn("?");
        assertEquals("You must provide an ISBN", bookManager2.checksThatBookHasAnISBN(book));
    }

    @Test
    void getBookByIsbn() {
        Book book = new Book();
        when(bookDAO.getBookByIsbn("ISBN22")).thenReturn(book);
        assertEquals(book, bookManager.getBookByIsbn("isbn22"));
    }


    @Test
    @DisplayName("should transfer Title")
    void transferTitleToSimilarBooks() {
        String title = "Nemo";
        Book book = new Book();
        Book book2 = new Book();
        book.setTitle(title);
        assertEquals(title, bookManager2.transferTitleToSimilarBooks(book, book2));

    }
    @Test
    @DisplayName("should transfer Author")
    void transferAuthorToSimilarBooks() {
        String author = "Paul Jackson";
        Book book = new Book();
        Book book2 = new Book();
        book.setAuthor(author);
        assertEquals(author, bookManager2.transferAuthorToSimilarBooks(book, book2));

    }
    @Test
    @DisplayName("should transfer Keywords")
    void transferKeywordsToSimilarBooks() {
        String keywords = "Mer, Ocean, Poissons";
        Book book = new Book();
        Book book2 = new Book();
        book.setKeywords(keywords);
        assertEquals(keywords, bookManager2.transferKeywordsToSimilarBooks(book, book2));

    }
    @Test
    @DisplayName("should transfer NbPages")
    void transferNbPagesToSimilarBooks() {
        int nbPages = 126;
        Book book = new Book();
        Book book2 = new Book();
        book.setNbPages(nbPages);
        assertEquals(nbPages, bookManager2.transferNbPagesToSimilarBooks(book, book2));

    }
    @Test
    @DisplayName("should transfer Publication Year")
    void transferPublicationYearToSimilarBooks() {
        int publicationYear = 2017;
        Book book = new Book();
        Book book2 = new Book();
        book.setPublicationYear(publicationYear);
        assertEquals(publicationYear, bookManager2.transferPublicationYearToSimilarBooks(book, book2));

    }

    @Test
    @DisplayName("should transfer Edition")
    void transferPublicationEditionToSimilarBooks() {
        String edition = "Gaumont";
        Book book = new Book();
        Book book2 = new Book();
        book2.setEdition(edition);
        book.setEdition("?");
        assertEquals(edition, bookManager2.transferEditionToSimilarBooks(book, book2));

    }

    @Test
    @DisplayName("shouls transfert data from a book to another if not empty")
    void transferValuesToSimilarBooks(){
        Book book2 = new Book();
        book2.setEdition("Maroni");
        book2.setNbPages(143);

        Book book = new Book();
        book.setTitle("Nemo");
        book.setEdition("?");
        book.setAuthor("Roger Marc");
        book.setPublicationYear(1233);
        book.setNbPages(0);

        assertAll(
                () -> assertEquals("Roger Marc", bookManager2.transferValuesToSimilarBooks(book, book2).getAuthor()),
                () -> assertEquals("Maroni", bookManager2.transferValuesToSimilarBooks(book, book2).getEdition()),
                () -> assertEquals(1233, bookManager2.transferValuesToSimilarBooks(book, book2).getPublicationYear()),
                () -> assertEquals(143,  bookManager2.transferValuesToSimilarBooks(book, book2).getNbPages()),
                () -> assertEquals("Nemo",  bookManager2.transferValuesToSimilarBooks(book, book2).getTitle())
        );

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