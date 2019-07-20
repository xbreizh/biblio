package org.troparo.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.impl.validator.StringValidatorBook;
import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class BookManagerImplTest {

    private BookManagerImpl bookManager2 = new BookManagerImpl();
    private BookManagerImpl bookManager;
    private BookDAO bookDAO;
    private StringValidatorBook stringValidatorBook;

    @BeforeEach
    void init() {
        bookDAO = mock(BookDAO.class);
        bookManager = new BookManagerImpl();
        stringValidatorBook = mock(StringValidatorBook.class);
        bookManager.setBookDAO(bookDAO);
        bookManager.setStringValidatorBook(stringValidatorBook);
    }

    @Test
    @DisplayName("should say that the ISBN already exist")
    void addBook() {
        Book book = new Book();
        book.setIsbn("isbn234");
        book.setAuthor("author");
        book.setTitle("title");
        book.setNbPages(12);
        book.setEdition("edition");
        book.setPublicationYear(1983);
        book.setKeywords("frfr, frfr");
        when(stringValidatorBook.validateExpression(anyString(), anyString())).thenReturn(true);
        when(bookDAO.existingISBN("isbn234")).thenReturn(true);
        assertEquals("ISBN already existing", bookManager.addBook(book));

    }

    @Test
    @DisplayName("should return empty string when insertion ok")
    void addBook1() {
        Book book = new Book();
        book.setIsbn("isnhyehrj1");
        book.setTitle("title");
        book.setAuthor("author");
        book.setInsertDate(new Date());
        book.setPublicationYear(1983);
        book.setEdition("edition");
        book.setNbPages(123);
        book.setKeywords("un");
        when(stringValidatorBook.validateExpression(anyString(), anyString())).thenReturn(true);
        when(bookDAO.existingISBN("ISBN234")).thenReturn(false);
        when(bookDAO.addBook(book)).thenReturn(true);
        assertEquals("", bookManager.addBook(book));

    }

    @Test
    @DisplayName("should return \\there was an issue while inserting the book\\ when insertion ko")
    void addBook2() {
        Book book = new Book();
        book.setIsbn("isnhyehrj1");
        book.setTitle("title");
        book.setAuthor("author");
        book.setInsertDate(new Date());
        book.setPublicationYear(1983);
        book.setEdition("edition");
        book.setNbPages(123);
        book.setKeywords("un");
        when(stringValidatorBook.validateExpression(anyString(), anyString())).thenReturn(true);
        when(bookDAO.existingISBN("ISBN234")).thenReturn(false);
        when(bookDAO.addBook(book)).thenReturn(false);
        assertEquals("there was an issue while inserting the book", bookManager.addBook(book));

    }

    @Test
    @DisplayName("should return exception")
    void addBook3() {
        bookManager.setStringValidatorBook(new StringValidatorBook());
        Book book = new Book();
        assertEquals("ISBN must be 10 or 13 characters: null", bookManager.addBook(book));

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
        when(bookDAO.getBookById(anyInt())).thenReturn(null);
        assertNull(bookManager.getBookById(12));
    }

    @Test
    @DisplayName("should return a list of books")
    void getBooksByCriterias() {
        List<Book> bookList = new ArrayList<>();
        Map<String, String> criterias = new HashMap<>();
        criterias.put("Test", "Test");
        when(bookDAO.getBooksByCriterias(criterias)).thenReturn(bookList);
        assertEquals(bookList, bookManager.getBooksByCriteria(criterias));
    }


    @Test
    @DisplayName("should remove invalid entries from criterias Map")
    void removeInvalidEntriesFromCriterias() {
        Map<String, String> criterias = new HashMap<>();
        Map<String, String> criteriaResults = new HashMap<>();

        criterias.put("Test", "Test");
        criterias.put("Author", "");
        criterias.put("ISBN", "123");
        criterias.put("Title", "?");
        criterias.put("Moko", "");
        criterias.put("", "?");

        criteriaResults.put("ISBN", "123");

        assertEquals(criteriaResults, bookManager2.removeInvalidEntriesFromCriterias(criterias));
    }


    @Test
    @DisplayName("should return a Null pointer exception if dao returns null")
    void updateBook() {
        Map<String, String> map = new HashMap<>();
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
        Map<String, String> map = new HashMap<>();
        List<Book> bookList = new ArrayList<>();
        Book book = new Book();
        book.setIsbn("ABC123");
        when(bookDAO.getBooksByCriterias(map)).thenReturn(bookList);
        assertEquals("No book to update", bookManager.updateBook(book));
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
        Map<String, String> map = new HashMap<>();
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
        Map<String, String> map = new HashMap<>();
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
    @DisplayName("should return false if book ISBN equals to \"?\" ")
    void checksThatBookHasAnISBN3() {
        //Book book = new Book();
        // book.setIsbn("?");
        assertEquals("You must provide an ISBN", bookManager2.checksThatBookHasAnISBN(null));
    }

    @Test
    @DisplayName("should return book by ISBN")
    void getBookByIsbn() {
        Book book = new Book();
        when(bookDAO.getBookByIsbn("ISBN22")).thenReturn(book);
        assertEquals(book, bookManager.getBookByIsbn("isbn22"));
    }


    @Test
    @DisplayName("should return null if book is null")
    void transferTitleToSimilarBooks() {
        assertAll(
                () -> assertNull(bookManager2.transferTitleToSimilarBooks(null, new Book())),
                () -> assertNull(bookManager2.transferTitleToSimilarBooks(new Book(), null))
        );


    }


    @Test
    @DisplayName("should return null if title is null")
    void transferTitleToSimilarBooks1() {
        Book book = new Book();
        String[] titles = {null, "", "?"};
        for (String s : titles
        ) {
            book.setTitle(s);
            assertNull(bookManager2.transferTitleToSimilarBooks(book, new Book()));
        }


    }


    @Test
    @DisplayName("should transfer Title")
    void transferTitleToSimilarBooks2() {
        String title = "Nemo";
        Book book = new Book();
        Book book2 = new Book();
        book.setTitle(title);
        assertEquals(title, bookManager2.transferTitleToSimilarBooks(book, book2));

    }


    @Test
    @DisplayName("should transfer Author")
    void transferAuthorToSimilarBooks() {
        BookManagerImpl bookManager = new BookManagerImpl();
        String author = "Paul Jackson";
        Book book = new Book();
        Book book2 = new Book();
        book.setAuthor(author);
        assertEquals(author, bookManager.transferAuthorToSimilarBooks(book, book2));

    }

    @Test
    @DisplayName("should return null ")
    void transferAuthorToSimilarBooks1() {
        Book book = new Book();
        String[] authors = {null, "", "?"};
        for (String s : authors
        ) {
            book.setAuthor(s);
            assertNull(bookManager2.transferAuthorToSimilarBooks(book, new Book()));
        }

    }

    @Test
    @DisplayName("should return null if book is null")
    void transferAuthorToSimilarBooks2() {
        assertAll(
                () -> assertNull(bookManager2.transferAuthorToSimilarBooks(null, new Book())),
                () -> assertNull(bookManager2.transferAuthorToSimilarBooks(new Book(), null))
        );


    }

    @Test
    @DisplayName("should transfer Keywords")
    void transferKeywordsToSimilarBooks() {
        String keywords = "Mer, Ocean, Poissons";
        Book book = new Book();
        Book book2 = new Book();
        book.setKeywords(keywords);
        bookManager2.setBookDAO(bookDAO);
        assertEquals(keywords, bookManager2.transferKeywordsToSimilarBooks(book, book2));

    }

    @Test
    @DisplayName("should should return null if book is null")
    void transferKeywordsToSimilarBooks1() {
        assertAll(
                () -> assertNull(bookManager2.transferKeywordsToSimilarBooks(null, new Book())),
                () -> assertNull(bookManager2.transferKeywordsToSimilarBooks(new Book(), null))
        );

    }

    @Test
    @DisplayName("should transfer Keywords")
    void transferKeywordsToSimilarBooks2() {
        Book book = new Book();
        String[] keywords = {null, "", "?"};
        for (String s : keywords
        ) {
            book.setKeywords(s);
            assertNull(bookManager2.transferKeywordsToSimilarBooks(book, new Book()));
        }

    }

    @Test
    @DisplayName("should return null if invalid value")
    void transferNbPagesToSimilarBooks() {
        Book book = new Book();
        assertEquals(0, bookManager.transferNbPagesToSimilarBooks(book, new Book()));


    }

    @Test
    @DisplayName("should return null if invalid value")
    void transferNbPagesToSimilarBooks1() {
        Book book = new Book();
        int nbPages = 18;
        book.setNbPages(nbPages);
        assertEquals(nbPages, bookManager.transferNbPagesToSimilarBooks(book, new Book()));

    }

    @Test
    @DisplayName("should return null if any book is null")
    void transferNbPagesToSimilarBooks2() {
        assertAll(
                () -> assertEquals(0, bookManager2.transferNbPagesToSimilarBooks(null, new Book())),
                () -> assertEquals(0, bookManager2.transferNbPagesToSimilarBooks(new Book(), null))
        );

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
    @DisplayName("should transfert edition")
    void transferEditionToSimilarBooks() {
        Book book = new Book();
        String edition = "plokko";
        book.setEdition(edition);
        assertEquals(edition, bookManager.transferEditionToSimilarBooks(book, new Book()));

    }

    @Test
    @DisplayName("should return null if invalid entry")
    void transferEditionToSimilarBooks1() {
        Book book = new Book();
        String[] editions = {null, "", "?"};
        for (String edition : editions
        ) {
            book.setEdition(edition);
            assertNull(bookManager2.transferEditionToSimilarBooks(book, new Book()));
        }

    }

    @Test
    @DisplayName("should return null if any book is null")
    void transferEditionToSimilarBooks2() {
        assertAll(
                () -> assertNull(bookManager2.transferEditionToSimilarBooks(null, new Book())),
                () -> assertNull(bookManager2.transferEditionToSimilarBooks(new Book(), null))
        );

    }

    @Test
    @DisplayName("should transfert data from a book to another if not empty")
    void transferValuesToSimilarBooks() {
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
                () -> assertEquals(143, bookManager2.transferValuesToSimilarBooks(book, book2).getNbPages()),
                () -> assertEquals("Nemo", bookManager2.transferValuesToSimilarBooks(book, book2).getTitle())
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
        when(bookDAO.getBookById(12)).thenReturn(new Book());
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
    @DisplayName("should return true if available")
    void isAvailable() {
        when(bookDAO.isAvailable(anyInt())).thenReturn(true);
        assertTrue(bookManager.isAvailable(12));
    }

    @Test
    @DisplayName("should return false if not available")
    void isAvailable1() {
        when(bookDAO.isAvailable(anyInt())).thenReturn(false);
        assertFalse(bookManager.isAvailable(12));
    }

    @Test
    @DisplayName("should return an exception")
    void checkValidityOfParametersForInsertBook() {
        Book book = new Book();
        book.setIsbn("isbn123");
        bookManager.setStringValidatorBook(new StringValidatorBook());
        assertEquals("ISBN must be 10 or 13 characters: isbn123", bookManager.checkValidityOfParametersForInsertBook(book));

    }

    @Test
    @DisplayName("should return \\no book provided\\")
    void checkValidityOfParametersForInsertBook1() {
        assertEquals("no book provided", bookManager.checkValidityOfParametersForInsertBook(null));

    }

  /*  @Test
    @DisplayName("should return error is isbn not filled")
    void checkRequiredValuesNotNull() {
        BookManagerImpl bookManager1 = new BookManagerImpl();
        Book book = new Book();
        assertEquals("isbn should be filled", bookManager1.checkRequiredValuesNotNull(book));
    }

    @Test
    @DisplayName("should return error is isbn not filled")
    void checkRequiredValuesNotNull1() {
        BookManagerImpl bookManager1 = new BookManagerImpl();
        Book book = new Book();
        book.setIsbn("");
        assertEquals("isbn should be filled", bookManager1.checkRequiredValuesNotNull(book));
    }

    @Test
    @DisplayName("should return error is isbn not filled")
    void checkRequiredValuesNotNull2() {
        BookManagerImpl bookManager1 = new BookManagerImpl();
        Book book = new Book();
        book.setIsbn("?");
        assertEquals("isbn should be filled", bookManager1.checkRequiredValuesNotNull(book));
    }*/
}