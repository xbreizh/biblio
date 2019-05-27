
package org.troparo.entities.loan;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://troparo.org/entities/loan}Token"/&gt;
 *         &lt;element ref="{http://troparo.org/entities/loan}LoanCriterias"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "token",
    "loanCriterias"
})
@XmlRootElement(name = "getLoanByCriteriasRequestType")
public class GetLoanByCriteriasRequestType {

    @XmlElement(name = "Token", required = true)
    protected String token;
    @XmlElement(name = "LoanCriterias", required = true)
    protected LoanCriterias loanCriterias;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the loanCriterias property.
     * 
     * @return
     *     possible object is
     *     {@link LoanCriterias }
     *     
     */
    public LoanCriterias getLoanCriterias() {
        return loanCriterias;
    }

    /**
     * Sets the value of the loanCriterias property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanCriterias }
     *     
     */
    public void setLoanCriterias(LoanCriterias value) {
        this.loanCriterias = value;
    }

}
