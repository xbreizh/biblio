
package org.troparo.entities.member;

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
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element ref="{http://troparo.org/entities/member}MemberTypeUpdate"/&gt;
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
    "memberTypeUpdate"
})
@XmlRootElement(name = "updateMemberRequestType")
public class UpdateMemberRequestType {

    @XmlElement(name = "Token", required = true)
    protected String token;
    @XmlElement(name = "MemberTypeUpdate", required = true)
    protected MemberTypeUpdate memberTypeUpdate;

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
     * Gets the value of the memberTypeUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link MemberTypeUpdate }
     *     
     */
    public MemberTypeUpdate getMemberTypeUpdate() {
        return memberTypeUpdate;
    }

    /**
     * Sets the value of the memberTypeUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link MemberTypeUpdate }
     *     
     */
    public void setMemberTypeUpdate(MemberTypeUpdate value) {
        this.memberTypeUpdate = value;
    }

}
