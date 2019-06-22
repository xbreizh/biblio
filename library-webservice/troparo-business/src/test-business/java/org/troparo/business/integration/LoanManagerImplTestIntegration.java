package org.troparo.business.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.business.contract.LoanManager;

import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration("classpath:/application-context-test.xml")
@TestPropertySource("classpath:config.properties")
@ExtendWith(SpringExtension.class)
@Transactional
class LoanManagerImplTestIntegration {


    @Inject
    private LoanManager loanManager;


    @Test
    @DisplayName("should return loans from database")
    void getLoans() {
        assertEquals(5, loanManager.getLoans().size());
    }

    @Test
    @DisplayName("should return 4")
    void getLoansByCriterias1() {
        HashMap<String, String> map = new HashMap<>();
        map.put("login", "JPOLINO");
        assertEquals(4, loanManager.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return 1")
    void getLoansByCriterias2() {
        HashMap<String, String> map = new HashMap<>();
        map.put("book_id", "5");
        assertEquals(1, loanManager.getLoansByCriterias(map).size());
    }

    @Test
    @DisplayName("should return an empty list when wrong status")
    void getLoansByCriterias3() {
        HashMap<String, String> map = new HashMap<>();
        map.put("login", "pol");
        map.put("status", "termindeated");
        assertEquals(0, loanManager.getLoansByCriterias(map).size());
    }

}
