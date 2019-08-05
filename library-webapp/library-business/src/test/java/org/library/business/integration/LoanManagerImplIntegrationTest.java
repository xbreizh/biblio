package org.library.business.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.impl.LoanManagerImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LoanManagerImpl.class)
class LoanManagerImplIntegrationTest {

    //@Inject
    private LoanManagerImpl loanManager;


    @BeforeEach
    void init() {
        loanManager = new LoanManagerImpl();

    }


/*    @Test
    @DisplayName("should return error")
    void reserve()  {
        assertEquals("invalid member", loanManager.reserve("dede", "1234567824"));

    }*/


}