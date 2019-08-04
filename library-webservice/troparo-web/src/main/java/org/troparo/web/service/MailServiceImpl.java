package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.MailManager;
import org.troparo.entities.mail.*;
import org.troparo.model.Mail;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;
import org.troparo.web.service.helper.DateConvertedHelper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jws.WebService;
import java.util.List;

@Named
@WebService(serviceName = "MailService", endpointInterface = "org.troparo.services.mailservice.IMailService",
        targetNamespace = "http://troparo.org/services/MailService/", portName = "MailServicePort", name = "MailServiceImpl")
public class MailServiceImpl implements IMailService {
    private Logger logger = Logger.getLogger(MailServiceImpl.class);

    @Inject
    private MailManager mailManager;


    @Inject
    private ConnectServiceImpl authentication;
    @Inject
    private DateConvertedHelper dateConvertedHelper;

    void setDateConvertedHelper(DateConvertedHelper dateConvertedHelper) {
        this.dateConvertedHelper = dateConvertedHelper;
    }

    @Override
    public GetPasswordResetListResponse getPasswordResetList(GetPasswordResetListRequest parameters) {
        GetPasswordResetListResponse ar = new GetPasswordResetListResponse();
        List<Mail> mailList = mailManager.getPasswordResetList(parameters.getToken());
        ar.setPasswordResetListType(convertMailListIntoPasswordResetListType(mailList));

        return ar;
    }

    @Override
    public GetOverdueMailListResponse getOverdueMailList(GetOverdueMailListRequest parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        GetOverdueMailListResponse ar = new GetOverdueMailListResponse();
        List<Mail> mailList = mailManager.getOverdueEmailList();
        MailListType mailListType = convertMailListIntoMailListType(mailList);
        ar.setMailListType(mailListType);
        return ar;
    }


    @Override
    public GetLoanReadyResponse getLoanReady(GetLoanReadyRequest parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        GetLoanReadyResponse ar = new GetLoanReadyResponse();
        List<Mail> mailList = mailManager.getLoansReadyForStart(parameters.getToken());
        MailListType mailListType = convertMailListIntoMailListType(mailList);
        ar.setMailListType(mailListType);
        return ar;
    }

    @Override
    public GetReminderMailListResponse getReminderMailList(GetReminderMailListRequest parameters) throws BusinessExceptionMail {
        checkAuthentication(parameters.getToken());
        GetReminderMailListResponse ar = new GetReminderMailListResponse();
        List<Mail> mailList = mailManager.getLoansReminder(parameters.getToken());
        MailListType mailListType = convertMailListIntoMailListType(mailList);
        ar.setMailListType(mailListType);
        logger.info("returning Reminder list");
        return ar;
    }

    PasswordResetListType convertMailListIntoPasswordResetListType(List<Mail> mailList) {
        PasswordResetListType passwordResetListType = new PasswordResetListType();
        for (Mail mail : mailList) {
            PasswordResetTypeOut passwordResetTypeOut = new PasswordResetTypeOut();
            passwordResetTypeOut.setEmail(mail.getEmail());
            passwordResetTypeOut.setLogin(mail.getLogin());
            passwordResetTypeOut.setToken(mail.getToken());

            passwordResetListType.getPasswordResetTypeOut().add(passwordResetTypeOut);
        }
        return passwordResetListType;
    }


    MailListType convertMailListIntoMailListType(List<Mail> mailList) {
        MailListType mlt = new MailListType();
        logger.info("converting mailList into MailTypeOut list");

        for (Mail mail : mailList) {
            MailTypeOut mailTypeOut = new MailTypeOut();
            mailTypeOut.setEmail(mail.getEmail());
            mailTypeOut.setFirstName(mail.getFirstName());
            mailTypeOut.setLastName(mail.getLastName());
            logger.info("converting due date");
            mailTypeOut.setDueDate(dateConvertedHelper.convertDateIntoXmlDate(mail.getDueDate()));
            mailTypeOut.setDiffDays(mail.getDiffDays());
            logger.info("converting endAvailable date");
            mailTypeOut.setEndAvailableDate(dateConvertedHelper.convertDateIntoXmlDate(mail.getEndAvailableDate()));
            mailTypeOut.setIsbn(mail.getIsbn());
            mailTypeOut.setTitle(mail.getTitle());
            mailTypeOut.setAuthor(mail.getAuthor());
            mailTypeOut.setEdition(mail.getEdition());
            mlt.getMailTypeOut().add(mailTypeOut);

        }

        return mlt;
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
