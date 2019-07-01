package org.library.business.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.business.impl.ConnectManagerImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConnectManagerImpl.class)
class ConnectManagerImplIntegrationTest {

    @Inject
    ConnectManagerImpl connectManager;

   /* @Test
    void authenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        connectManager.authenticate(authentication);
    }*/

    @Test
    void supports() {
    }

    @Test
    void buildUserAuthority() {
    }
}