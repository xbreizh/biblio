package org.library.business.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.contract.LoanManager;
import org.library.business.impl.DateConvertedHelper;
import org.library.business.impl.LoanManagerImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.troparo.services.bookservice.BusinessExceptionBook;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LoanManagerImpl.class)
class LoanManagerImplIntegrationTest {

    //@Inject
    private LoanManagerImpl loanManager;

    //private ConnectManagerImpl connectManager;
    String token = "81bcec45-55b5-4cb1-a0fd-1599a670f54f";

    @BeforeEach
    void init(){
        loanManager = new LoanManagerImpl();

    }


    @Test
    @DisplayName("should return error")
    void reserve()  {
        loanManager = new LoanManagerImpl();
        assertEquals("s", loanManager.reserve(token, "1234567824"));

    }


}