package org.troparo.business.integration;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.LoanManager;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
@Sql("classpath:resetDb.sql")
@Transactional
class LoanManagerImplIntegrationTest {
    private Logger logger = Logger.getLogger(this.getClass().getName());


    @Inject
    private LoanManager loanManager;


    @BeforeEach
    void reset() {
        System.out.println("db size loans : " + loanManager.getLoans().size());
        logger.info("reset db");
    }


    @Test
    @DisplayName("should return loans from database")
    void getLoans() {
        assertEquals(5, loanManager.getLoans().size());
    }

    @Test
    @DisplayName("should return 4")
    void getLoansByCriterias1() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "JPOLINO");
        assertEquals(4, loanManager.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return 1")
    void getLoansByCriterias2() {
        Map<String, String> map = new HashMap<>();
        map.put("book_id", "5");
        assertEquals(1, loanManager.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return an empty list when wrong status")
    void getLoansByCriterias3() {
        Map<String, String> map = new HashMap<>();
        map.put("login", "pol");
        map.put("status", "termindeated");
        assertEquals(0, loanManager.getLoansByCriterias(map).size());
    }

}
