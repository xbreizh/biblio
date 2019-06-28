package org.library.business;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.library.business.contract.ConnectManager;
import org.library.business.impl.BookManagerImpl;
import org.library.business.impl.ConnectManagerImpl;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.ResetPasswordRequestType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookManagerImplIntegrationTest {

    private BookManagerImpl bookManager;
    private String token;

    //@Inject
    private ConnectService connectService;


    @BeforeEach
    void init() throws BusinessExceptionConnect {
        ConnectService connectService = new ConnectService();
        ResetPasswordRequestType resetPasswordRequestType = new ResetPasswordRequestType();
        resetPasswordRequestType.setLogin("Lokii");
        resetPasswordRequestType.setPassword("555");
        resetPasswordRequestType.setEmail("LOKI@LOKI.LOKII");


        GetTokenRequestType getTokenRequestType = new GetTokenRequestType();
        getTokenRequestType.setLogin("lokii");
        getTokenRequestType.setPassword("555");
        token = connectService.getConnectServicePort().getToken(getTokenRequestType).getReturn();

        bookManager = new BookManagerImpl();

    }

    @Test
    @DisplayName("should return books")
    void searchBooks() {

        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("author", "moss");
        assertNotNull( bookManager.searchBooks(token, criterias));

    }

}