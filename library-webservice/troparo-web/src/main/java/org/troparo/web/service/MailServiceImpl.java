package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.MailManager;
import org.troparo.entities.mail.GetOverdueMailListRequest;
import org.troparo.entities.mail.GetOverdueMailListResponse;
import org.troparo.entities.mail.MailListType;
import org.troparo.entities.mail.MailTypeOut;
import org.troparo.model.Mail;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Named
@WebService(serviceName = "MailService", endpointInterface = "org.troparo.services.mailservice.IMailService",
        targetNamespace = "http://troparo.org/services/MailService/", portName = "MailServicePort", name = "MailServiceImpl")
public class MailServiceImpl implements IMailService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private MailManager mailManager;


    @Inject
    private ConnectServiceImpl authentication;


    @Override
    public GetOverdueMailListResponse getOverdueMailList(GetOverdueMailListRequest parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        GetOverdueMailListResponse ar = new GetOverdueMailListResponse();
        List<Mail> mailList = mailManager.getOverdueEmailList();
        MailListType mailListType = convertmailListIntoMailListType(mailList);
        ar.setMailListType(mailListType);
        return ar;
    }


    MailListType convertmailListIntoMailListType(List<Mail> mailList) {
        MailListType mlt = new MailListType();


        for (Mail mail : mailList) {
            MailTypeOut mout = new MailTypeOut();
            mout.setEmail(mail.getEmail());
            mout.setFirstName(mail.getFirstName());
            mout.setLastName(mail.getLastName());
            mout.setDueDate(convertDateIntoXmlDate(mail.getDueDate()));
            mout.setDiffDays(mail.getDiffDays());
            mout.setIsbn(mail.getIsbn());
            mout.setTitle(mail.getTitle());
            mout.setAuthor(mail.getAuthor());
            mout.setEdition(mail.getEdition());
            mlt.getMailTypeOut().add(mout);

        }

        return mlt;
    }

    XMLGregorianCalendar convertDateIntoXmlDate(Date date) {
        // converting Date into XML date

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage());
        }
        return xmlCalendar;
    }

    private void checkAuthentication(String token) throws BusinessExceptionMail {
        try {
            authentication.checkToken(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BusinessExceptionMail("invalid token");
        }
    }

    void setAuthentication(ConnectServiceImpl authentication) {
        this.authentication = authentication;
    }

    void setMailManager(MailManager mailManager) {
        this.mailManager = mailManager;
    }


}
