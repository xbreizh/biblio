package org.library.business.integration;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.impl.BookManagerImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.services.bookservice.BusinessExceptionBook;

import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookManagerImpl.class)
class BookManagerImplIntegrationTest {

    @Inject
    private BookManagerImpl bookManager;

/*
    @BeforeEach
    void init() {
        BookService bookService = mock(BookService.class);

    }*/

    @Test
    @DisplayName("should return books")
    void searchBooks() throws BusinessExceptionBook {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("AUTHOR", "moss");
        assertEquals("LA GRANDE AVENTURE", bookManager.searchBooks("", criterias).get(0).getTitle());

    }

/*    @Test
    @DisplayName("should return exception")
    void searchBooks1() {
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("AUTHORW", "moss");
        assertThrows(BusinessExceptionBook.class, ()-> bookManager.searchBooks("", criterias).get(0).getTitle());

    }*/

    @Test
    @DisplayName("should return number available")
    void getNbAvailable() throws BusinessExceptionBook {
        assertEquals(4, bookManager.getNbAvailable("", "12345678OK"));

    }

   /* @Test
    @DisplayName("should return exception")
    void getNbAvailable1() throws BusinessExceptionBook {
        assertEquals(4, bookManager.getNbAvailable("", "12s"));

    }*/

}