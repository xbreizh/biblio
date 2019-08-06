package org.mail.contract;

import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.mailservice.BusinessExceptionMail;

import javax.mail.MessagingException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;

public interface EmailManager {

    boolean sendPasswordResetEmail()  throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail;

    boolean sendOverdueMail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException;

    boolean sendReadyEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException;

    boolean sendReminderEmail() throws BusinessExceptionConnect, MessagingException, IOException, BusinessExceptionMail, DatatypeConfigurationException;

}
