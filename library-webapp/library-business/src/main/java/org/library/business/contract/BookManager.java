package org.library.business.contract;


import org.library.model.Book;
import org.troparo.services.bookservice.BusinessExceptionBook;

import java.util.List;
import java.util.Map;

public interface BookManager {

    List<Book> searchBooks(String token, Map<String, String> criterias) throws BusinessExceptionBook;

    Book getBookByISBN(String token, String isbn) throws BusinessExceptionBook;
}
