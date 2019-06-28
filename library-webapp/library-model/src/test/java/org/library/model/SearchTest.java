package org.library.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SearchTest {

    private Search search;

    @BeforeEach
    void init() {
        search = new Search();
    }


    @Test
    void getIsbn() {
        String isbn = "ISBN124";
        search.setIsbn(isbn);
        assertEquals(isbn, search.getIsbn());
    }

    @Test
    void setIsbn() {
        String isbn = "ISBN124";
        search.setIsbn(isbn);
        assertEquals(isbn, search.getIsbn());
    }

    @Test
    void getTitle() {
        String title = "Title33";
        search.setTitle(title);
        assertEquals(title, search.getTitle());
    }

    @Test
    void setTitle() {
        String title = "Title33";
        search.setTitle(title);
        assertEquals(title, search.getTitle());
    }

    @Test
    void getAuthor() {
        String author = "Author242";
        search.setAuthor(author);
        assertEquals(author, search.getAuthor());
    }

    @Test
    void setAuthor() {
        String author = "Author242";
        search.setAuthor(author);
        assertEquals(author, search.getAuthor());
    }

    @Test
    void toString1() {
        search.setIsbn("ISBN322");
        search.setAuthor("author 4423");
        search.setTitle("Title 282");
        assertEquals("Search{isbn='ISBN322', title='Title 282', author='author 4423'}", search.toString());
    }
}
