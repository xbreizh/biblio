package org.troparo.services.mailservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.2.7
 * 2019-05-27T11:06:02.160+02:00
 * Generated source version: 3.2.7
 *
 */
@WebService(targetNamespace = "http://troparo.org/services/MailService/", name = "IMailService")
@XmlSeeAlso({org.troparo.entities.mail.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface IMailService {

    @WebMethod
    @WebResult(name = "getOverdueMailListResponse", targetNamespace = "http://troparo.org/entities/mail", partName = "parameters")
    public org.troparo.entities.mail.GetOverdueMailListResponse getOverdueMailList(
        @WebParam(partName = "parameters", name = "getOverdueMailListRequest", targetNamespace = "http://troparo.org/entities/mail")
        org.troparo.entities.mail.GetOverdueMailListRequest parameters
    ) throws BusinessExceptionMail;
}
