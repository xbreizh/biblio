package org.library.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.impl.BookManagerImpl;
import org.library.model.Book;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.entities.book.BookTypeOut;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*@ExtendWith(SpringExtension.class)
@Transactional*/
class BookManagerImplTest {
    //@Inject
    /*private BookManagerImpl bookManager;

    @BeforeEach
    void init() {
        bookManager = new BookManagerImpl();
    }

  *//*  @Test
    void searchBooks() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Title", "e");
        assertNotNull(bookManager.searchBooks("token123", map));
    }*//*

    @Test
    void convertBookTypeOutListIntoBookList() {
        List<BookTypeOut> bookTypeOutList = new ArrayList<>();
        String token = "tok23";
        String title = "Title343";
        String author = "Author22";
        String isbn = "ISBN2334";
        String keywords = "sea, ocean, beach";
        int id = 44;
        int publicationYear = 1983;
        int nbPages = 33;
        BookTypeOut booktypeOut = new BookTypeOut();
        booktypeOut.setAuthor(author);
        booktypeOut.setTitle(title);
        booktypeOut.setId(id);
        booktypeOut.setISBN(isbn);
        booktypeOut.setNbPages(nbPages);
        booktypeOut.setPublicationYear(publicationYear);
        booktypeOut.setKeywords(keywords);
        bookTypeOutList.add(booktypeOut);
        Book book = bookManager.convertBookTypeOutListIntoBookList(token, bookTypeOutList).get(0);

        assertAll(
                () -> assertEquals(book.getAuthor(), booktypeOut.getAuthor())
        );
    }*/
}