
package org.troparo.services.mailservice;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-27T11:06:02.155+02:00
 * Generated source version: 3.2.7
 */

@WebFault(name = "BusinessMailFaultType", targetNamespace = "http://troparo.org/entities/mail")
public class BusinessExceptionMail extends Exception {

    private org.troparo.entities.mail.BusinessMailFaultType businessMailFaultType;

    public BusinessExceptionMail() {
        super();
    }

    public BusinessExceptionMail(String message) {
        super(message);
    }

    public BusinessExceptionMail(String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public BusinessExceptionMail(String message, org.troparo.entities.mail.BusinessMailFaultType businessMailFaultType) {
        super(message);
        this.businessMailFaultType = businessMailFaultType;
    }

    public BusinessExceptionMail(String message, org.troparo.entities.mail.BusinessMailFaultType businessMailFaultType, java.lang.Throwable cause) {
        super(message, cause);
        this.businessMailFaultType = businessMailFaultType;
    }

    public org.troparo.entities.mail.BusinessMailFaultType getFaultInfo() {
        return this.businessMailFaultType;
    }
}
