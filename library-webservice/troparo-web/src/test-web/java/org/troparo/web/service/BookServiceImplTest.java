package org.troparo.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.troparo.business.impl.BookManagerImpl;
import org.troparo.entities.book.*;
import org.troparo.model.Book;
import org.troparo.services.bookservice.BusinessExceptionBook;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/*@ContextConfiguration("classpath:/application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional*/
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private BookServiceImpl bookService;
    @Mock
    private BookManagerImpl bookManager;
    @Mock
    private ConnectServiceImpl connectService;

    @BeforeEach
    void init() {
        bookService = new BookServiceImpl();
        bookService.setBookManager(bookManager);
        bookService.setAuthentication(connectService);
    }

    @Test
    @DisplayName("should return an empty list")
    void getAllBooks() throws BusinessExceptionBook {
        BookListRequestType parameters = new BookListRequestType();
        parameters.setToken("tok123");
        List<Book> books = new ArrayList<>();
        when(bookManager.getBooks()).thenReturn(books);
        assertEquals(0, bookService.getAllBooks(parameters).getBookListType().getBookTypeOut().size());

    }

    @Test
    @DisplayName("should throw exception if missing bookTypeIn attribute")
    void addBook()  {
        AddBookRequestType parameters = new AddBookRequestType();
        parameters.setToken("tok123");
        BookTypeIn bookTypeIn = new BookTypeIn();
        parameters.setBookTypeIn(bookTypeIn);
        assertThrows(BusinessExceptionBook.class, ()->bookService.addBook(parameters));
    }

    @Test
    @DisplayName("should not throw exception is bookTypeIn is legit")
    void addBook1() throws BusinessExceptionBook {
        AddBookRequestType parameters = new AddBookRequestType();
        parameters.setToken("tok123");
        BookTypeIn bookTypeIn = new BookTypeIn();
        String title="title";
        String author = "author";
        int nbPages = 123;
        int publicationYear=1982;
        String edition ="edition";
        String keywords = "ocean, beach, sea";
        String isbn = "isbn233";
        bookTypeIn.setTitle(title);
        bookTypeIn.setAuthor(author);
        bookTypeIn.setNbPages(nbPages);
        bookTypeIn.setPublicationYear(publicationYear);
        bookTypeIn.setISBN(isbn);
        bookTypeIn.setEdition(edition);
        bookTypeIn.setKeywords(keywords);
        parameters.setBookTypeIn(bookTypeIn);
        when(bookManager.addBook(any(Book.class))).thenReturn("");
        assertDoesNotThrow( ()->bookService.addBook(parameters));
    }

    @Test
    @DisplayName("should throw exception is bookManager sends exception")
    void addBook2() {
        AddBookRequestType parameters = new AddBookRequestType();
        parameters.setToken("tok123");
        BookTypeIn bookTypeIn = new BookTypeIn();
        String title="title";
        String author = "author";
        int nbPages = 123;
        int publicationYear=1982;
        String edition ="edition";
        String keywords = "ocean, beach, sea";
        String isbn = "isbn233";
        bookTypeIn.setTitle(title);
        bookTypeIn.setAuthor(author);
        bookTypeIn.setNbPages(nbPages);
        bookTypeIn.setPublicationYear(publicationYear);
        bookTypeIn.setISBN(isbn);
        bookTypeIn.setEdition(edition);
        bookTypeIn.setKeywords(keywords);
        parameters.setBookTypeIn(bookTypeIn);
        when(bookManager.addBook(any(Book.class))).thenReturn("exception");
        assertThrows(BusinessExceptionBook.class, ()->bookService.addBook(parameters));
    }

    @Test
    void updateBook() {
        fail();
    }

    @Test
    void addCopy() {
        fail();
    }

    @Test
    @DisplayName("should return a book")
    void getBookById() throws BusinessExceptionBook {
        GetBookByIdRequestType parameters = new GetBookByIdRequestType();
        parameters.setToken("tok23");
        parameters.setId(3);
        when(bookManager.getBookById(anyInt())).thenReturn(new Book());
        assertNotNull(bookService.getBookById(parameters));
    }

    @Test
    @DisplayName("should return a book details")
    void getBookById1() throws BusinessExceptionBook {
        GetBookByIdRequestType parameters = new GetBookByIdRequestType();
        parameters.setToken("tok23");
        parameters.setId(3);
        Book book = new Book();
        String title="title";
        String author = "author";
        int nbPages = 123;
        int publicationYear=1982;
        int id = 45;
        String edition ="edition";
        String keywords = "ocean, beach, sea";
        String isbn = "isbn233";
        book.setTitle(title);
        book.setAuthor(author);
        book.setNbPages(nbPages);
        book.setPublicationYear(publicationYear);
        book.setIsbn(isbn);
        book.setEdition(edition);
        book.setKeywords(keywords);
        book.setId(id);
        when(bookManager.getBookById(anyInt())).thenReturn(book);
        BookTypeOut bookTypeOut = bookService.getBookById(parameters).getBookTypeOut();
        //sout
        assertAll(
                ()->assertEquals(isbn, bookTypeOut.getISBN()),
                ()->assertEquals(id, bookTypeOut.getId()),
                ()->assertEquals(author, bookTypeOut.getAuthor()),
                ()->assertEquals(title, bookTypeOut.getTitle()),
                ()->assertEquals(edition, bookTypeOut.getEdition()),
                ()->assertEquals(nbPages, bookTypeOut.getNbPages()),
                ()->assertEquals(keywords, bookTypeOut.getKeywords())
        );
    }

    @Test
    @DisplayName("should return true if manager returns true")
    void isAvailable() throws BusinessExceptionBook {
        IsAvailableRequestType parameters = new IsAvailableRequestType();
        parameters.setToken("tok123");
        parameters.setId(33);
        when(bookManager.isAvailable(anyInt())).thenReturn(true);
        assertTrue(bookService.isAvailable(parameters).isReturn());
    }

    @Test
    @DisplayName("should return false if manager returns false")
    void isAvailable1() throws BusinessExceptionBook {
        IsAvailableRequestType parameters = new IsAvailableRequestType();
        parameters.setToken("tok123");
        parameters.setId(33);
        when(bookManager.isAvailable(anyInt())).thenReturn(false);
        assertFalse(bookService.isAvailable(parameters).isReturn());
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
    }
}