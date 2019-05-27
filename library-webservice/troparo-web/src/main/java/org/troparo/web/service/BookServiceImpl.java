package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.BookManager;
import org.troparo.entities.book.*;
import org.troparo.model.Book;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.bookservice.IBookService;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebService(serviceName = "BookService", endpointInterface = "org.troparo.services.bookservice.IBookService",
        targetNamespace = "http://troparo.org/services/BookService/", portName = "BookServicePort", name = "BookServiceImpl")
public class BookServiceImpl implements IBookService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private BookManager bookManager;

    @Inject
    private ConnectServiceImpl authentication;

    private String exception = "";
    private List<Book> bookList = new ArrayList<>();
    private org.troparo.entities.book.BookTypeOut bookTypeOut = null;
    private org.troparo.entities.book.BookTypeIn bookTypeIn = null;
    private org.troparo.entities.book.BookListType bookListType = new org.troparo.entities.book.BookListType();
    private Book book = null;

    // Create
    @Override
    public AddBookResponseType addBook(org.troparo.entities.book.AddBookRequestType parameters) throws BusinessExceptionBook {
        AddBookResponseType ar = new AddBookResponseType();
        ar.setReturn(true);

        checkAuthentication(parameters.getToken());

        bookTypeIn = parameters.getBookTypeIn();
        convertBookTypeInIntoBook();
        logger.info("bookManager: " + bookManager);
        exception = bookManager.addBook(book);
        if (!exception.equals("")) {
            logger.info(exception);
            throw new BusinessExceptionBook(exception);
        }

        return ar;
    }

    // Converts Input into Book for business
    private void convertBookTypeInIntoBook() {
        book = new Book();
        book.setIsbn(bookTypeIn.getISBN().toUpperCase());
        book.setTitle(bookTypeIn.getTitle().toUpperCase());
        book.setAuthor(bookTypeIn.getAuthor().toUpperCase());
        logger.info(bookTypeIn.getPublicationYear());
        book.setPublicationYear(bookTypeIn.getPublicationYear());
        book.setEdition(bookTypeIn.getEdition().toUpperCase());
        book.setNbPages(bookTypeIn.getNbPages());
        book.setKeywords(bookTypeIn.getKeywords().toUpperCase());
        logger.info("pub date: " + book.getPublicationYear());
    }

    // Converts Input into Book for business
    private void convertBookTypeUpdateIntoBook(org.troparo.entities.book.BookTypeUpdate bookTypeUpdate) {
        book = new Book();
        book.setIsbn(bookTypeUpdate.getISBN().toUpperCase());
        book.setTitle(bookTypeUpdate.getTitle().toUpperCase());
        book.setAuthor(bookTypeUpdate.getAuthor().toUpperCase());
        logger.info(bookTypeUpdate.getPublicationYear());
        book.setPublicationYear(bookTypeUpdate.getPublicationYear());
        book.setEdition(bookTypeUpdate.getEdition().toUpperCase());
        book.setNbPages(bookTypeUpdate.getNbPages());
        book.setKeywords(bookTypeUpdate.getKeywords().toUpperCase());
        logger.info("pub date: " + book.getPublicationYear());
    }

    // Update
    @Override
    public UpdateBookResponseType updateBook(UpdateBookRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        org.troparo.entities.book.UpdateBookResponseType ar = new org.troparo.entities.book.UpdateBookResponseType();
        ar.setReturn(true);
        org.troparo.entities.book.BookTypeUpdate bookTypeUpdate = parameters.getBookTypeUpdate();
        logger.info("received: " + bookTypeUpdate);
        // update
        convertBookTypeUpdateIntoBook(bookTypeUpdate);
        logger.info("bookManager: " + bookManager);
        exception = bookManager.updateBook(book);
        if (!exception.equals("")) {
            throw new BusinessExceptionBook(exception);
        }

        return ar;
    }

    @Override
    public AddCopyResponseType addCopy(AddCopyRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        AddCopyResponseType ar = new AddCopyResponseType();
        ar.setReturn(true);
        String isbn = parameters.getISBN().toUpperCase();
        int copies = parameters.getNbCopies();

        logger.info("bookManager: " + bookManager);
        exception = bookManager.addCopy(isbn, copies);
        if (!exception.equals("")) {
            throw new BusinessExceptionBook(exception);
        }

        return ar;
    }

    // Get One
    @Override
    public GetBookByIdResponseType getBookById(GetBookByIdRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        logger.info("new method added");
        GetBookByIdResponseType rep = new GetBookByIdResponseType();
        BookTypeOut bt = new org.troparo.entities.book.BookTypeOut();
        Book book = bookManager.getBookById(parameters.getReturn());
        if (book == null) {
            throw new BusinessExceptionBook("no book found with that id");
        } else {
            bt.setId(book.getId());
            bt.setISBN(book.getIsbn());
            bt.setTitle(book.getTitle());
            bt.setAuthor(book.getAuthor());
            bt.setEdition(book.getEdition());
            bt.setNbPages(book.getNbPages());
            bt.setKeywords(book.getIsbn());
            rep.setBookTypeOut(bt);
        }
        return rep;
    }

    // Get All


    @Override
    public BookListResponseType getAllBooks(BookListRequestType parameters) throws BusinessExceptionBook {

        checkAuthentication(parameters.getToken());
        bookList = bookManager.getBooks();
        logger.info("size list: " + bookList.size());

        BookListResponseType bookListResponseType = new BookListResponseType();

        convertBookIntoBookTypeOut();
        // add bookType to the movieListType

        bookListResponseType.setBookListType(bookListType);
        return bookListResponseType;
    }

    @Override
    public IsAvailableResponseType isAvailable(IsAvailableRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());
        IsAvailableResponseType ar = new IsAvailableResponseType();
        ar.setReturn(bookManager.isAvailable(parameters.getId()));
        return ar;
    }

    private void checkAuthentication(String token) throws BusinessExceptionBook {
        try {
            authentication.checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessExceptionBook("invalid token");
        }
    }


    // Get List By Criterias
    @Override
    public GetBookByCriteriasResponseType getBookByCriterias(GetBookByCriteriasRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        HashMap<String, String> map = new HashMap<>();
        BookCriterias criterias = parameters.getBookCriterias();
        map.put("ISBN", criterias.getISBN().toUpperCase());
        map.put("Title", criterias.getTitle().toUpperCase());
        map.put("Author", criterias.getAuthor().toUpperCase());
        logger.info("map: " + map);
        /*bookListType.getBookTypeOut().clear();*/
        bookList = bookManager.getBooksByCriterias(map);
        GetBookByCriteriasResponseType brt = new GetBookByCriteriasResponseType();
        logger.info("bookListType beg: " + bookListType.getBookTypeOut().size());

        convertBookIntoBookTypeOut();
        /*bookListType.getBookTypeOut().add(bookTypeOut); // add bookType to the movieListType*/
        logger.info("bookListType end: " + bookListType.getBookTypeOut().size());
        brt.setBookListType(bookListType);
       /* brt = removeDuplicates(brt);*/
        return brt;
    }
/*
    private GetBookByCriteriasResponseType removeDuplicates(GetBookByCriteriasResponseType brt) {
        List<BookTypeOut> finallist = new ArrayList<>();
        List<BookTypeOut> intermlist = new ArrayList<>();
        List<BookTypeOut> initList = brt.getBookListType().getBookTypeOut();

        for (BookTypeOut bout: initList
             ) {
            for (BookTypeOut bout2: finallist
                 ) {
                if(bout.getISBN().equals())
            }
        }*/

/*
        return brt;
    }*/


    // Delete
    @Override
    public RemoveBookResponseType removeBook(RemoveBookRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        RemoveBookResponseType ar = new RemoveBookResponseType();
        ar.setReturn(true);

        logger.info("bookManager: " + bookManager);
        exception = bookManager.remove(parameters.getId());
        if (!exception.equals("")) {
            throw new BusinessExceptionBook(exception);
        }

        return ar;

    }

    // Get number Available
    @Override
    public GetAvailableResponseType getAvailable(GetAvailableRequestType parameters) throws BusinessExceptionBook {
        checkAuthentication(parameters.getToken());

        GetAvailableResponseType ar = new GetAvailableResponseType();
        int i = bookManager.getNbAvailable(parameters.getISBN().toUpperCase());
        logger.info("i: " + i);
        ar.setReturn(i);

        return ar;
    }

    // Converts Book from Business into output
    private void convertBookIntoBookTypeOut() {

        bookListType.getBookTypeOut().clear();
        for (Book book : bookList) {

            // set values retrieved from DAO class
            bookTypeOut = new org.troparo.entities.book.BookTypeOut();
            bookTypeOut.setId(book.getId());
            bookTypeOut.setISBN(book.getIsbn());
            bookTypeOut.setTitle(book.getTitle());
            bookTypeOut.setAuthor(book.getAuthor());
            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setPublicationYear(book.getPublicationYear());
            bookTypeOut.setEdition(book.getEdition());
            bookTypeOut.setNbPages(book.getNbPages());
            bookTypeOut.setKeywords(book.getKeywords());
            bookListType.getBookTypeOut().add(bookTypeOut);
        }
        logger.info("bookListType end: " + bookListType.getBookTypeOut().size());
    }


}
