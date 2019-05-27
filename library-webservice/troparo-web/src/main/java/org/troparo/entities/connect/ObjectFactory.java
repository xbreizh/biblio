
package org.troparo.entities.connect;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.troparo.entities.connect package. 
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

    private final static QName _Token_QNAME = new QName("http://troparo.org/entities/connect", "Token");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.troparo.entities.connect
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CheckTokenRequestType }
     * 
     */
    public CheckTokenRequestType createCheckTokenRequestType() {
        return new CheckTokenRequestType();
    }

    /**
     * Create an instance of {@link CheckTokenResponseType }
     * 
     */
    public CheckTokenResponseType createCheckTokenResponseType() {
        return new CheckTokenResponseType();
    }

    /**
     * Create an instance of {@link InvalidateTokenRequestType }
     * 
     */
    public InvalidateTokenRequestType createInvalidateTokenRequestType() {
        return new InvalidateTokenRequestType();
    }

    /**
     * Create an instance of {@link InvalidateTokenResponseType }
     * 
     */
    public InvalidateTokenResponseType createInvalidateTokenResponseType() {
        return new InvalidateTokenResponseType();
    }

    /**
     * Create an instance of {@link GetTokenRequestType }
     * 
     */
    public GetTokenRequestType createGetTokenRequestType() {
        return new GetTokenRequestType();
    }

    /**
     * Create an instance of {@link GetTokenResponseType }
     * 
     */
    public GetTokenResponseType createGetTokenResponseType() {
        return new GetTokenResponseType();
    }

    /**
     * Create an instance of {@link ResetPasswordRequestType }
     * 
     */
    public ResetPasswordRequestType createResetPasswordRequestType() {
        return new ResetPasswordRequestType();
    }

    /**
     * Create an instance of {@link ResetPasswordResponseType }
     * 
     */
    public ResetPasswordResponseType createResetPasswordResponseType() {
        return new ResetPasswordResponseType();
    }

    /**
     * Create an instance of {@link BusinessConnectFaultType }
     * 
     */
    public BusinessConnectFaultType createBusinessConnectFaultType() {
        return new BusinessConnectFaultType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/connect", name = "Token")
    public JAXBElement<String> createToken(String value) {
        return new JAXBElement<String>(_Token_QNAME, String.class, null, value);
    }

}
