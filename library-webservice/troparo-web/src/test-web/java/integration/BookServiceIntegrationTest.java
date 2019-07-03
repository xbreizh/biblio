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
import org.troparo.entities.book.AddCopyRequestType;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.loan.GetLoanByCriteriasRequestType;
import org.troparo.entities.loan.GetLoanByIdRequestType;
import org.troparo.entities.loan.LoanCriterias;
import org.troparo.entities.loan.LoanListRequestType;
import org.troparo.services.bookservice.BookService;
import org.troparo.services.bookservice.BusinessExceptionBook;
import org.troparo.services.loanservice.BusinessExceptionLoan;
import org.troparo.web.service.BookServiceImpl;
import org.troparo.web.service.ConnectServiceImpl;
import org.troparo.web.service.LoanServiceImpl;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration("classpath:/spring-hibernate-jax-ws-test.xml")
@ExtendWith(SpringExtension.class)
@Transactional
class BookServiceIntegrationTest {
    private Logger logger = Logger.getLogger(BookServiceIntegrationTest.class.getName());
    @Inject
    private BookServiceImpl bookService;
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
    @DisplayName("should add copy")
    void addCopy() throws BusinessExceptionBook {
       AddCopyRequestType addCopyRequestType = new AddCopyRequestType();
       addCopyRequestType.setToken(token);
       addCopyRequestType.setISBN("1234567824");
       addCopyRequestType.setNbCopies(3);
       assertTrue(bookService.addCopy(addCopyRequestType).isReturn());


   }
}

