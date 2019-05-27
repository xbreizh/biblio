
package org.troparo.entities.book;

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
 *         &lt;element ref="{http://troparo.org/entities/book}BookTypeOut"/&gt;
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
    "bookTypeOut"
})
@XmlRootElement(name = "getBookByIdResponseType")
public class GetBookByIdResponseType {

    @XmlElement(name = "BookTypeOut", required = true)
    protected BookTypeOut bookTypeOut;

    /**
     * Gets the value of the bookTypeOut property.
     * 
     * @return
     *     possible object is
     *     {@link BookTypeOut }
     *     
     */
    public BookTypeOut getBookTypeOut() {
        return bookTypeOut;
    }

    /**
     * Sets the value of the bookTypeOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link BookTypeOut }
     *     
     */
    public void setBookTypeOut(BookTypeOut value) {
        this.bookTypeOut = value;
    }

}
