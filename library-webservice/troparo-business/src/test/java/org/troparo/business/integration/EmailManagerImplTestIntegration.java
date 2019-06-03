package org.troparo.business.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.EmailManager;
import org.troparo.business.contract.MemberManager;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration("classpath:/application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional
public class EmailManagerImplTestIntegration {


    @Inject
    private EmailManager emailManager;


    @Test
    @DisplayName("should return overdue emails from database")
    void getOverdueEmailList() {
        assertNotNull(emailManager.getOverdueEmailList());
    }


}
