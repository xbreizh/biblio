
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
 *         &lt;element ref="{http://troparo.org/entities/loan}LoanTypeOut"/&gt;
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
    "loanTypeOut"
})
@XmlRootElement(name = "getLoanByIdResponseType")
public class GetLoanByIdResponseType {

    @XmlElement(name = "LoanTypeOut", required = true)
    protected LoanTypeOut loanTypeOut;

    /**
     * Gets the value of the loanTypeOut property.
     * 
     * @return
     *     possible object is
     *     {@link LoanTypeOut }
     *     
     */
    public LoanTypeOut getLoanTypeOut() {
        return loanTypeOut;
    }

    /**
     * Sets the value of the loanTypeOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanTypeOut }
     *     
     */
    public void setLoanTypeOut(LoanTypeOut value) {
        this.loanTypeOut = value;
    }

}
