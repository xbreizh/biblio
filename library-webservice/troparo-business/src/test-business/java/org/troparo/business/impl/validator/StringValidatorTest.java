package org.troparo.business.impl.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("classpath:/application-context-test.xml")
@ExtendWith(SpringExtension.class)
class StringValidatorTest {
    @Inject
    private StringValidator stringValidator;




    @Test
    @DisplayName("should not validate Login")
    void invalidateLogin() {
        String[] wrongLoginList = {null, "?", "", "2", "ed.", "frfr@fr", "/e3e3e3e3e3e3e3dfgdffgfgf", "?", "ba"};
        for (String login : wrongLoginList
        ) {
            System.out.println("Login: " + login);
            assertFalse(stringValidator.validateExpression("login", login));
        }
    }


    @Test
    @DisplayName("should validate Login")
    void validateLogin() {
        String[] validLoginList = {"bastien34", "rokoko", "M2345"};
        for (String login : validLoginList
        ) {
            System.out.println("Login: " + login);
            assertTrue(stringValidator.validateExpression("login", login));
        }
    }

    @Test
    @DisplayName("should not validate Password")
    void invalidatePassword() {
        String[] passwordList = {null, "?", "", "2", "ed.", "frfr@fr", "/e3e3e3e3e3e3e3dfgdffgfgf", "?", "ba", "Gee44", "bastien34$", "dededAd33$5"};
        for (String password : passwordList
        ) {
            System.out.println("password: " + password);
            assertFalse(stringValidator.validateExpression("password", password));
        }
    }


    @Test
    @DisplayName("should validate Password")
    void validatePassword() {
        String[] passwordList = {"bAstien34$", "rokoko%2Q", "M234hu$5", "4322222y%T"};
        for (String password : passwordList
        ) {
            System.out.println("Password: " + password);
            assertTrue(stringValidator.validateExpression("password", password));
        }
    }

    @Test
    @DisplayName("should not validate ShortStandard")
    void invalidateShortStandard() {
        String[] shortStandardList = {null, "?", "", "/e3e3e3e3e3e3e3dfgdfw"};
        for (String shortStandard : shortStandardList
        ) {
            System.out.println("shortStandard: " + shortStandard);
            assertFalse(stringValidator.validateExpression("short", shortStandard));
        }
    }


    @Test
    @DisplayName("should validate ShortStandard")
    void validateShortStandard() {
        String[] shortStandardList = {"bAstien34", "rokoko", "M2345", "Jean-fleur", "marko polo", "Wee4", "22", "ed.", "frfr@fr", "/3e3e3e3e3e3e3dfgd", "ba", "bastien34$"};
        for (String shortStandard : shortStandardList
        ) {
            System.out.println("LongStandard: " + shortStandard);
            assertTrue(stringValidator.validateExpression("short", shortStandard));
        }
    }


    @Test
    @DisplayName("should not validate LongStandard")
    void invalidateLongStandard() {
        String[] longStandardList = {null, "?", "", "dedd  - $£34434-_ddddsdnkjnn4jkln498fnlkrn4lknr48*&^^lknlkn43lknlkncd98ylkne3lkn389jnslkfs089u8&**()\"@OM@kmpiohjfsdma;lkmsd dklmnlfksjdfyjn4j4890s9fd090&&**(*\"()\")jkjh4388&&7jh;fsldjloi4jh53k5jfsdnkjne"};
        for (String longStandard : longStandardList
        ) {
            System.out.println("longStandard: " + longStandard);
            assertFalse(stringValidator.validateExpression("longStandard", longStandard));
        }
    }


    @Test
    @DisplayName("should validate LongStandard")
    void validateLongStandard() {
        String[] longStandardList = {"bAstien34", "rokoko", "M2345", "Jean-fleur", "marko polo", "Wee4", "22", "ed.", "frfr@fr", "/3e3e3erererererererereeredfederfre3%%^e3e3e3e3dfgd", "ba", "bastien34$"};
        for (String longStandard : longStandardList
        ) {
            System.out.println("Standard: " + longStandard);
            assertTrue(stringValidator.validateExpression("longStandard", longStandard));
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
                "sw.ddd@dede.fr",
                "loki@loki.lokii"
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


    @Test
    @DisplayName("should return false if login empty, null or ?")
    void validateForUpdateMember() {
        assertAll(
                () -> assertFalse(stringValidator.validateForUpdateMember("login", "")),
                () -> assertFalse(stringValidator.validateForUpdateMember("login", "?")),
                () -> assertFalse(stringValidator.validateForUpdateMember("login", null))
        );

    }

    @Test
    @DisplayName("should return true if value respects regex")
    void validateForUpdateMember1() {
        assertAll(
                () -> assertTrue(stringValidator.validateForUpdateMember("password", "1248Er$")),
                () -> assertTrue(stringValidator.validateForUpdateMember("firstName", "Corentin")),
                () -> assertTrue(stringValidator.validateForUpdateMember("login", "Momo56"))

        );

    }

    @Test
    @DisplayName("should return true if value null, empty ? and type != login")
    void validateForUpdateMember2() {
        assertAll(
                () -> assertTrue(stringValidator.validateForUpdateMember("password", "?")),
                () -> assertTrue(stringValidator.validateForUpdateMember("firstName", "")),
                () -> assertTrue(stringValidator.validateForUpdateMember("lastName", null))

        );

    }


    @Test
    void getException() {
        assertAll(
                ()-> assertEquals("Login must be between 5 or 10 characters: ", stringValidator.getException("login")),
                ()-> assertEquals("FirstName must be between 2 or 20 characters: ", stringValidator.getException("firstName")),
                ()-> assertEquals("LastName must be between 2 or 20 characters: ", stringValidator.getException("lastName")),
                ()-> assertEquals("Invalid Email: ", stringValidator.getException("email")),
                ()-> assertEquals("Password should have between 2 and 10 characters, have at least a lower case, an upper case, a special character and a number", stringValidator.getException("password")),
                ()-> assertEquals("Invalid entry", stringValidator.getException("ploc"))
        );
    }
}