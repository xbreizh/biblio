package org.troparo.business.contract;

import org.troparo.consumer.contract.BookDAO;
import org.troparo.model.Book;

import java.util.List;
import java.util.Map;


public interface BookManager {

    String addBook(Book book);

    List<Book> getBooks();

    Book getBookById(int id);

    List<Book> getBooksByCriterias(Map<String, String> map);

    String updateBook(Book book);

    Book getBookByIsbn(String isbn);

    String remove(int id);

    int getNbAvailable(String isbn);

    String addCopy(String isbn, int copies);

    boolean isAvailable(int id);

    void setBookDAO(BookDAO bookDAO);
}