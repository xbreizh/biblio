
package org.troparo.entities.member;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.troparo.entities.member package. 
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

    private final static QName _Token_QNAME = new QName("http://troparo.org/entities/member", "Token");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.troparo.entities.member
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddMemberRequestType }
     * 
     */
    public AddMemberRequestType createAddMemberRequestType() {
        return new AddMemberRequestType();
    }

    /**
     * Create an instance of {@link MemberTypeIn }
     * 
     */
    public MemberTypeIn createMemberTypeIn() {
        return new MemberTypeIn();
    }

    /**
     * Create an instance of {@link AddMemberResponseType }
     * 
     */
    public AddMemberResponseType createAddMemberResponseType() {
        return new AddMemberResponseType();
    }

    /**
     * Create an instance of {@link MemberListRequestType }
     * 
     */
    public MemberListRequestType createMemberListRequestType() {
        return new MemberListRequestType();
    }

    /**
     * Create an instance of {@link MemberListResponseType }
     * 
     */
    public MemberListResponseType createMemberListResponseType() {
        return new MemberListResponseType();
    }

    /**
     * Create an instance of {@link MemberListType }
     * 
     */
    public MemberListType createMemberListType() {
        return new MemberListType();
    }

    /**
     * Create an instance of {@link MemberTypeOut }
     * 
     */
    public MemberTypeOut createMemberTypeOut() {
        return new MemberTypeOut();
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
     * Create an instance of {@link BookTypeOut }
     * 
     */
    public BookTypeOut createBookTypeOut() {
        return new BookTypeOut();
    }

    /**
     * Create an instance of {@link GetMemberByIdRequestType }
     * 
     */
    public GetMemberByIdRequestType createGetMemberByIdRequestType() {
        return new GetMemberByIdRequestType();
    }

    /**
     * Create an instance of {@link GetMemberByIdResponseType }
     * 
     */
    public GetMemberByIdResponseType createGetMemberByIdResponseType() {
        return new GetMemberByIdResponseType();
    }

    /**
     * Create an instance of {@link GetMemberByLoginRequestType }
     * 
     */
    public GetMemberByLoginRequestType createGetMemberByLoginRequestType() {
        return new GetMemberByLoginRequestType();
    }

    /**
     * Create an instance of {@link GetMemberByLoginResponseType }
     * 
     */
    public GetMemberByLoginResponseType createGetMemberByLoginResponseType() {
        return new GetMemberByLoginResponseType();
    }

    /**
     * Create an instance of {@link GetMemberByCriteriasRequestType }
     * 
     */
    public GetMemberByCriteriasRequestType createGetMemberByCriteriasRequestType() {
        return new GetMemberByCriteriasRequestType();
    }

    /**
     * Create an instance of {@link MemberCriterias }
     * 
     */
    public MemberCriterias createMemberCriterias() {
        return new MemberCriterias();
    }

    /**
     * Create an instance of {@link GetMemberByCriteriasResponseType }
     * 
     */
    public GetMemberByCriteriasResponseType createGetMemberByCriteriasResponseType() {
        return new GetMemberByCriteriasResponseType();
    }

    /**
     * Create an instance of {@link UpdateMemberRequestType }
     * 
     */
    public UpdateMemberRequestType createUpdateMemberRequestType() {
        return new UpdateMemberRequestType();
    }

    /**
     * Create an instance of {@link MemberTypeUpdate }
     * 
     */
    public MemberTypeUpdate createMemberTypeUpdate() {
        return new MemberTypeUpdate();
    }

    /**
     * Create an instance of {@link UpdateMemberResponseType }
     * 
     */
    public UpdateMemberResponseType createUpdateMemberResponseType() {
        return new UpdateMemberResponseType();
    }

    /**
     * Create an instance of {@link RemoveMemberRequestType }
     * 
     */
    public RemoveMemberRequestType createRemoveMemberRequestType() {
        return new RemoveMemberRequestType();
    }

    /**
     * Create an instance of {@link RemoveMemberResponseType }
     * 
     */
    public RemoveMemberResponseType createRemoveMemberResponseType() {
        return new RemoveMemberResponseType();
    }

    /**
     * Create an instance of {@link BusinessMemberFaultType }
     * 
     */
    public BusinessMemberFaultType createBusinessMemberFaultType() {
        return new BusinessMemberFaultType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://troparo.org/entities/member", name = "Token")
    public JAXBElement<String> createToken(String value) {
        return new JAXBElement<String>(_Token_QNAME, String.class, null, value);
    }

}
