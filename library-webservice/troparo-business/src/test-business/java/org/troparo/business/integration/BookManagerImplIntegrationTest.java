package org.troparo.business.integration;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.BookManager;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class BookManagerImplIntegrationTest {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Sql({"classpath:/src/main/resources/resetDb.sql"})
    @BeforeEach
    void reset() {
        logger.info("reset db");
    }


    @Inject
    private BookManager bookManager;


    @Test
    @DisplayName("should return books from database")
    void getBooks() {
        assertNotNull(bookManager.getBooks());
    }


}
