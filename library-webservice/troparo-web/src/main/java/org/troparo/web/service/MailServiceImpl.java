package org.troparo.web.service;


import org.apache.log4j.Logger;
import org.troparo.business.contract.MailManager;
import org.troparo.entities.mail.*;
import org.troparo.model.Mail;
import org.troparo.services.mailservice.BusinessExceptionMail;
import org.troparo.services.mailservice.IMailService;

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
        ar.setPasswordResetListType(convertmailListIntoPasswordResetlistType(mailList));

        return ar;
    }

    private PasswordResetListType convertmailListIntoPasswordResetlistType(List<Mail> mailList) {
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
            mout.setDueDate(dateConvertedHelper.convertDateIntoXmlDate(mail.getDueDate()));
            mout.setDiffDays(mail.getDiffDays());
            mout.setIsbn(mail.getIsbn());
            mout.setTitle(mail.getTitle());
            mout.setAuthor(mail.getAuthor());
            mout.setEdition(mail.getEdition());
            mlt.getMailTypeOut().add(mout);

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
