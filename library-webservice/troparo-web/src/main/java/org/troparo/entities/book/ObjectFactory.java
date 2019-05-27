
package org.troparo.entities.book;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.troparo.entities.book package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Token_QNAME = new QName("http://troparo.org/entities/book", "Token");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.troparo.entities.book
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IsAvailableRequestType }
     * 
     */
    public IsAvailableRequestType createIsAvailableRequestType() {
        return new IsAvailableRequestType();
    }

    /**
     * Create an instance of {@link IsAvailableResponseType }
     * 
     */
    public IsAvailableResponseType createIsAvailableResponseType() {
        return new IsAvailableResponseType();
    }

    /**
     * Create an instance of {@link AddBookRequestType }
     * 
     */
    public AddBookRequestType createAddBookRequestType() {
        return new AddBookRequestType();
    }

    /**
     * Create an instance of {@link BookTypeIn }
     * 
     */
    public BookTypeIn createBookTypeIn() {
        return new BookTypeIn();
    }

    /**
     * Create an instance of {@link AddBookResponseType }
     * 
     */
    public AddBookResponseType createAddBookResponseType() {
        return new AddBookResponseType();
    }

    /**
     * Create an instance of {@link BookListRequestType }
     * 
     */
    public BookListRequestType createBookListRequestType() {
        return new BookListRequestType();
    }

    /**
     * Create an instance of {@link BookListResponseType }
     * 
     */
    public BookListResponseType createBookListResponseType() {
        return new BookListResponseType();
    }

    /**
     * Create an instance of {@link BookListType }
     * 
     */
    public BookListType createBookListType() {
        return new BookListType();
    }

    /**
     * Create an instance of {@link BookTypeOut }
     * 
     */
    public BookTypeOut createBookTypeOut() {
        return new BookTypeOut();
    }

    /**
     * Create an instance of {@link GetBookByIdRequestType }
     * 
     */
    public GetBookByIdRequestType createGetBookByIdRequestType() {
        return new GetBookByIdRequestType();
    }

    /**
     * Create an instance of {@link GetBookByIdResponseType }
     * 
     */
    public GetBookByIdResponseType createGetBookByIdResponseType() {
        return new GetBookByIdResponseType();
    }

    /**
     * Create an instance of {@link GetBookByCriteriasRequestType }
     * 
     */
    public GetBookByCriteriasRequestType createGetBookByCriteriasRequestType() {
        return new GetBookByCriteriasRequestType();
    }

    /**
     * Create an instance of {@link BookCriterias }
     * 
     */
    public BookCriterias createBookCriterias() {
        return new BookCriterias();
    }

    /**
     * Create an instance of {@link GetBookByCriteriasResponseType }
     * 
     */
    public GetBookByCriteriasResponseType createGetBookByCriteriasResponseType() {
        return new GetBookByCriteriasResponseType();
    }

    /**
     * Create an instance of {@link UpdateBookRequestType }
     * 
     */
    public UpdateBookRequestType createUpdateBookRequestType() {
        return new UpdateBookRequestType();
    }

    /**
     * Create an instance of {@link BookTypeUpdate }
     * 
     */
    public BookTypeUpdate createBookTypeUpdate() {
        return new BookTypeUpdate();
    }

    /**
     * Create an instance of {@link UpdateBookResponseType }
     * 
     */
    public UpdateBookResponseType createUpdateBookResponseType() {
        return new UpdateBookResponseType();
    }

    /**
     * Create an instance of {@link RemoveBookRequestType }
     * 
     */
    public RemoveBookRequestType createRemoveBookRequestType() {
        return new RemoveBookRequestType();
    }

    /**
     * Create an instance of {@link RemoveBookResponseType }
     * 
     */
    public RemoveBookResponseType createRemoveBookResponseType() {
        return new RemoveBookResponseType();
    }

    /**
     * Create an instance of {@link AddCopyRequestType }
     * 
     */
    public AddCopyRequestType createAddCopyRequestType() {
        return new AddCopyRequestType();
    }

    /**
     * Create an instance of {@link AddCopyResponseType }
     * 
     */
    public AddCopyResponseType createAddCopyResponseType() {
        return new AddCopyResponseType();
    }

    /**
     * Create an instance of {@link GetAvailableRequestType }
     * 
     */
    public GetAvailableRequestType createGetAvailableRequestType() {
        return new GetAvailableRequestType();
    }

    /**
     * Create an instance of {@link GetAvailableResponseType }
     * 
     */
    public GetAvailableResponseType createGetAvailableResponseType() {
        return new GetAvailableResponseType();
    }

    /**
     * Create an instance of {@link BusinessBookFaultType }
     * 
     */
    public BusinessBookFaultType createBusinessBookFaultType() {
        return new BusinessBookFaultType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/book", name = "Token")
    public JAXBElement<String> createToken(String value) {
        return new JAXBElement<String>(_Token_QNAME, String.class, null, value);
    }

}
