package org.mail.contract;

import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.mailservice.BusinessExceptionMail;

import javax.mail.MessagingException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;

public interface EmailManager {

    void sendPasswordResetEmail()  throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail;

    void sendOverdueMail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException;

    void sendReadyEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException;

    void sendReminderEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException;

}
