
package org.troparo.entities.loan;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.troparo.entities.loan package. 
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

    private final static QName _Token_QNAME = new QName("http://troparo.org/entities/loan", "Token");
    private final static QName _Login_QNAME = new QName("http://troparo.org/entities/loan", "Login");
    private final static QName _Id_QNAME = new QName("http://troparo.org/entities/loan", "Id");
    private final static QName _Status_QNAME = new QName("http://troparo.org/entities/loan", "Status");
    private final static QName _ISBN_QNAME = new QName("http://troparo.org/entities/loan", "ISBN");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.troparo.entities.loan
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddLoanRequestType }
     * 
     */
    public AddLoanRequestType createAddLoanRequestType() {
        return new AddLoanRequestType();
    }

    /**
     * Create an instance of {@link LoanTypeIn }
     * 
     */
    public LoanTypeIn createLoanTypeIn() {
        return new LoanTypeIn();
    }

    /**
     * Create an instance of {@link AddLoanResponseType }
     * 
     */
    public AddLoanResponseType createAddLoanResponseType() {
        return new AddLoanResponseType();
    }

    /**
     * Create an instance of {@link LoanListRequestType }
     * 
     */
    public LoanListRequestType createLoanListRequestType() {
        return new LoanListRequestType();
    }

    /**
     * Create an instance of {@link LoanListResponseType }
     * 
     */
    public LoanListResponseType createLoanListResponseType() {
        return new LoanListResponseType();
    }

    /**
     * Create an instance of {@link LoanListType }
     * 
     */
    public LoanListType createLoanListType() {
        return new LoanListType();
    }

    /**
     * Create an instance of {@link LoanTypeOut }
     * 
     */
    public LoanTypeOut createLoanTypeOut() {
        return new LoanTypeOut();
    }

    /**
     * Create an instance of {@link GetLoanByIdRequestType }
     * 
     */
    public GetLoanByIdRequestType createGetLoanByIdRequestType() {
        return new GetLoanByIdRequestType();
    }

    /**
     * Create an instance of {@link GetLoanByIdResponseType }
     * 
     */
    public GetLoanByIdResponseType createGetLoanByIdResponseType() {
        return new GetLoanByIdResponseType();
    }

    /**
     * Create an instance of {@link GetLoanStatusRequestType }
     * 
     */
    public GetLoanStatusRequestType createGetLoanStatusRequestType() {
        return new GetLoanStatusRequestType();
    }

    /**
     * Create an instance of {@link GetLoanStatusResponseType }
     * 
     */
    public GetLoanStatusResponseType createGetLoanStatusResponseType() {
        return new GetLoanStatusResponseType();
    }

    /**
     * Create an instance of {@link GetLoanByCriteriasRequestType }
     * 
     */
    public GetLoanByCriteriasRequestType createGetLoanByCriteriasRequestType() {
        return new GetLoanByCriteriasRequestType();
    }

    /**
     * Create an instance of {@link LoanCriterias }
     * 
     */
    public LoanCriterias createLoanCriterias() {
        return new LoanCriterias();
    }

    /**
     * Create an instance of {@link GetLoanByCriteriasResponseType }
     * 
     */
    public GetLoanByCriteriasResponseType createGetLoanByCriteriasResponseType() {
        return new GetLoanByCriteriasResponseType();
    }

    /**
     * Create an instance of {@link IsRenewableRequestType }
     * 
     */
    public IsRenewableRequestType createIsRenewableRequestType() {
        return new IsRenewableRequestType();
    }

    /**
     * Create an instance of {@link IsRenewableResponseType }
     * 
     */
    public IsRenewableResponseType createIsRenewableResponseType() {
        return new IsRenewableResponseType();
    }

    /**
     * Create an instance of {@link RenewLoanRequestType }
     * 
     */
    public RenewLoanRequestType createRenewLoanRequestType() {
        return new RenewLoanRequestType();
    }

    /**
     * Create an instance of {@link RenewLoanResponseType }
     * 
     */
    public RenewLoanResponseType createRenewLoanResponseType() {
        return new RenewLoanResponseType();
    }

    /**
     * Create an instance of {@link TerminateLoanRequestType }
     * 
     */
    public TerminateLoanRequestType createTerminateLoanRequestType() {
        return new TerminateLoanRequestType();
    }

    /**
     * Create an instance of {@link TerminateLoanResponseType }
     * 
     */
    public TerminateLoanResponseType createTerminateLoanResponseType() {
        return new TerminateLoanResponseType();
    }

    /**
     * Create an instance of {@link LoanTypeUpdate }
     * 
     */
    public LoanTypeUpdate createLoanTypeUpdate() {
        return new LoanTypeUpdate();
    }

    /**
     * Create an instance of {@link BusinessLoanFaultType }
     * 
     */
    public BusinessLoanFaultType createBusinessLoanFaultType() {
        return new BusinessLoanFaultType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/loan", name = "Token")
    public JAXBElement<String> createToken(String value) {
        return new JAXBElement<String>(_Token_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/loan", name = "Login")
    public JAXBElement<String> createLogin(String value) {
        return new JAXBElement<String>(_Login_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/loan", name = "Id")
    public JAXBElement<Integer> createId(Integer value) {
        return new JAXBElement<Integer>(_Id_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/loan", name = "Status")
    public JAXBElement<String> createStatus(String value) {
        return new JAXBElement<String>(_Status_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/loan", name = "ISBN")
    public JAXBElement<String> createISBN(String value) {
        return new JAXBElement<String>(_ISBN_QNAME, String.class, null, value);
    }

}
