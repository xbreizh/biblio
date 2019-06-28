package org.library.business.contract;


import org.library.model.Book;

import java.util.List;
import java.util.Map;

public interface BookManager {

    List<Book> searchBooks(String token, Map<String, String> criterias);
}
