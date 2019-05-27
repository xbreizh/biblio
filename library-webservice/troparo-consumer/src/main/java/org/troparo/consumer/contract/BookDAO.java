package org.troparo.consumer.contract;

import org.troparo.model.Book;

import java.util.HashMap;
import java.util.List;


public interface BookDAO {

    List<Book> getBooks();

    boolean addBook(Book book);

    Book getBookById(int id);

    Book getBookByIsbn(String isbn);

    boolean existingISBN(String isbn);

    List<Book> getBooksByCriterias(HashMap<String, String> map);

    boolean updateBook(Book book);

    boolean remove(Book book);

    int getAvailable(String isbn);

    boolean isAvailable(int id);

    /* boolean addCopies(String isbn, int copies);*/
}