package org.troparo.business.impl.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
class StringValidatorTest {
    @Inject
    private StringValidator stringValidator;


    @Test
    @DisplayName("should not validateExpression")
    void validate() {
        String[] wrongLoginList = {null, "?", "", "2", "ed.", "frfr@fr", "/e3e3e3e3e3e3e3dfgdffgfgf", "?", "ba"};
        for (String login : wrongLoginList
        ) {
            System.out.println("Login: " + login);
            assertFalse(stringValidator.validateExpression("login", login));
        }
    }


    @Test
    @DisplayName("should validateExpression")
    void validate1() {
        String[] validLoginList = {"bastien34", "rokoko", "M2345"};
        for (String login : validLoginList
        ) {
            System.out.println("Login: " + login);
            assertTrue(stringValidator.validateExpression("login", login));
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
            assertTrue(stringValidator.validateExpression("email", mail));
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
                "dede@fr",
                "Abc..123@example.com"};

        for (String mail : mailListInvalid
        ) {

            assertFalse(stringValidator.validateExpression("email", mail));
        }
    }
}