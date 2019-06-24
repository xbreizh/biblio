package org.troparo.business.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.MailManager;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class MailManagerImplIntegrationTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    @Inject
    private MailManager mailManager;

    @Sql({"classpath:resetDb.sql"})
    @BeforeEach
    void reset() {
        logger.info("reset db");
    }


    @Test
    @DisplayName("should return overdue emails from database")
    void getOverdueEmailList() {
        assertNotNull(mailManager.getOverdueEmailList());
    }


}
