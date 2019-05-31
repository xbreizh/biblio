package org.troparo.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidatorTest {

    private EmailValidator emailValidator;

    @BeforeEach
    void init() {
        emailValidator = new EmailValidator();
    }

    @Test
    @DisplayName("should return true when email valide")
    void validate() {
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
                "just”not”right@example.com",
                "this\" is\"really\"not\\allowed@example.com"
        };

        List<String> list = Arrays.asList(mailListValid);
        for (String mail : list
        ) {
            assertTrue(emailValidator.validate(mail));
        }
    }

    @Test
    @DisplayName("should return false when email invalide")
    void validate1() {
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

        List<String> list = Arrays.asList(mailListInvalid);
        for (String mail : list
        ) {
            System.out.println(mail);
            assertFalse(emailValidator.validate(mail));
        }
    }
}