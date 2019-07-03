package integration;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.loan.*;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.web.service.ConnectServiceImpl;
import org.troparo.web.service.LoanServiceImpl;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration("classpath:/spring-hibernate-jax-ws-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class LoanServiceIntegrationTest {
    private Logger logger = Logger.getLogger(LoanServiceIntegrationTest.class.getName());
    @Inject
    private LoanServiceImpl loanService;
    @Inject
    private ConnectServiceImpl connectService;
    private String token = "";

    @BeforeEach
    @Sql(scripts = "classpath:resetDb.sql")
    void getToken() {
        GetTokenRequestType parameters = new GetTokenRequestType();
        parameters.setLogin("LOKII");
        parameters.setPassword("123");
        token = connectService.getToken(parameters).getReturn();

    }

    @Test
    @DisplayName("should return list of Loans")
    void getLoans() throws BusinessExceptionLoan {
        LoanListRequestType loanListRequestType = new LoanListRequestType();
        loanListRequestType.setToken(token);
        System.out.println(loanService.getAllLoans(loanListRequestType).getLoanListType().getLoanTypeOut());
        assertEquals(5, loanService.getAllLoans(loanListRequestType).getLoanListType().getLoanTypeOut().size());
    }

    @Test
    @DisplayName("should return list of Loans")
    void getLoansById() throws BusinessExceptionLoan {
        GetLoanByIdRequestType loanListRequestType = new GetLoanByIdRequestType();
        loanListRequestType.setToken(token);
        loanListRequestType.setId(11);
        assertEquals("JPOLINO", loanService.getLoanById(loanListRequestType).getLoanTypeOut().getLogin());
    }

    @Test
    @DisplayName("should return loans for a member")
    void getLoansByCriterias() throws BusinessExceptionLoan {
        GetLoanByCriteriasRequestType loanByCriteriasRequestType = new GetLoanByCriteriasRequestType();
        loanByCriteriasRequestType.setToken(token);
        LoanCriterias loanCriterias = new LoanCriterias();
        loanCriterias.setLogin("jpolino");
        loanByCriteriasRequestType.setLoanCriterias(loanCriterias);
        System.out.println("size: " + loanService.getLoanByCriterias(loanByCriteriasRequestType).getLoanListType().getLoanTypeOut().size());
        assertEquals(4, loanService.getLoanByCriterias(loanByCriteriasRequestType).getLoanListType().getLoanTypeOut().size());
    }
}

