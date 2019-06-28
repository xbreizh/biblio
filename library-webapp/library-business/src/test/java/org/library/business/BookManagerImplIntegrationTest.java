package org.library.business;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.library.business.impl.BookManagerImpl;
import org.troparo.entities.connect.GetTokenRequestType;
import org.troparo.entities.connect.ResetPasswordRequestType;
import org.troparo.services.connectservice.BusinessExceptionConnect;
import org.troparo.services.connectservice.ConnectService;

import java.util.HashMap;
import java.util.Map;

class BookManagerImplIntegrationTest {

    private BookManagerImpl bookManager;
    private String token;

    //@Inject
    private ConnectService connectService;


    @BeforeEach
    void init() throws BusinessExceptionConnect {
        connectService = new ConnectService();
        ResetPasswordRequestType resetPasswordRequestType = new ResetPasswordRequestType();
        resetPasswordRequestType.setLogin("lokii");
        resetPasswordRequestType.setPassword("123");
        resetPasswordRequestType.setEmail("LOKI@LOKI.LOKII");


        GetTokenRequestType getTokenRequestType = new GetTokenRequestType();
        getTokenRequestType.setLogin("lokii");
        getTokenRequestType.setPassword("123");
        token = connectService.getConnectServicePort().getToken(getTokenRequestType).getReturn();


    }

    @Test
    @DisplayName("should return books")
    void searchBooks() {
        bookManager = new BookManagerImpl();
        HashMap<String, String> criterias = new HashMap<>();
        criterias.put("author", "moss");
        System.out.println(bookManager.searchBooks(token, criterias).size());

    }

}