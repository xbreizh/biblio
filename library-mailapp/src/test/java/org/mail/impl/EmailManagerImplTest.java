package org.mail.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mail.contract.ConnectManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.services.mailservice.MailService;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmailManagerImpl.class, ConnectManagerImpl.class})
class EmailManagerImplTest {

    private EmailManagerImpl emailManager;
    private MailService mailService;
    @Inject
    private ConnectManagerImpl connectManager;

    @BeforeEach
    void init(){
        emailManager = spy(EmailManagerImpl.class);
        mailService = mock(MailService.class);
        emailManager.setMailService(mailService);
    }


    @Test
    @DisplayName("should set mailService")
    void getMailService(){
        MailService mailService1 = new MailService();
        emailManager.setMailService(mailService1);
        assertEquals(mailService1, emailManager.getMailService());
    }


}
