package org.troparo.business.impl.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
class StringElementValidatorTest {
    @Inject
    private StringElementValidator stringElementValidator;



    @Test
    @DisplayName("should not validate")
    void validate() {
        String[] wrongLoginList= {null,"?", "","2", "ed.","frfr@fr", "/e3e3e3e3e3e3e3dfgdffgfgf", "?"};
        for (String login: wrongLoginList
        ) {
            System.out.println("Login: "+login);
            assertFalse(stringElementValidator.validateLogin(login));
        }
    }


    @Test
    @DisplayName("should validate")
    void validate1() {
        String[] validLoginList= {"bastien34", "rokoko", "M2345"};
        for (String login: validLoginList
             ) {
            System.out.println("Login: "+login);
            assertTrue(stringElementValidator.validate(login, "login"));
        }
    }

    @Test
    @DisplayName("should return true when email valide")
    void validateEmail() {
        String[] mailListValid = {
                "email@example.com",
                "firstname.lastname@example.com",
                "email@subdomain.example.com",
                "firstname+lastname@example.com",
                "email@123.123.123.123",
                "email@[123.123.123.123]",
                "email@example.com",
                "1234567890@example.com",
                "email@example-one.com",
                "_______@example.com",
                "email@example.name",
                "email@example.museum",
                "email@example.co.jp",
                "email@111.222.333.44444",
                "firstname-lastname@example.com",
                "sw.ddd@dede.fr"
        };
        for (String mail : mailListValid
        ) {
            assertTrue(stringElementValidator.validate(mail, "email"));
        }
    }

    @Test
    @DisplayName("should return false when email invalide")
    void validateEmail1() {
        String[] mailListInvalid = {
                "plainaddress",
                "#@%^%#$@#$@#.com",
                "@example.com",
                "Joe Smith <email@example.com>",
                "email.example.com",
                "email@example@example.com",
                ".email@example.com",
                "email.@example.com",
                "email..email@example.com",
                "あいうえお@example.com",
                "email@example.com (Joe Smith)",
                "email@example",
                "email@-example.com",
                "email@example..com",
                "Abc..123@example.com"};

        for (String mail : mailListInvalid
        ) {

            assertFalse(stringElementValidator.validate(mail, "email"));
        }
    }
}