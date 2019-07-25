package org.troparo.consumer.contract;

import org.troparo.model.Book;

import java.util.List;
import java.util.Map;


public interface BookDAO {

    List<Book> getBooks();

    boolean addBook(Book book);

    Book getBookById(int id);

    Book getBookByIsbn(String isbn);

    boolean existingISBN(String isbn);

    List<Book> getBooksByCriteria(Map<String, String> map);

    boolean updateBook(Book book);

    boolean remove(Book book);

    int getNbAvailable(String isbn);

    boolean isAvailable(int id);

}