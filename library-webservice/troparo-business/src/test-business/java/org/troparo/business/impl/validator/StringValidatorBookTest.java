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
class StringValidatorBookTest {

    @Inject
    private StringValidatorBook stringValidatorBook;

    @Test
    @DisplayName("should not validate nbPages")
    void invalidateNbPages() {
        String[] nbPagesList = {null, "?", "0", "12345"};
        for (String nbPages : nbPagesList
        ) {
            assertFalse(stringValidatorBook.validateExpression("nbPages", nbPages));
        }
    }

    @Test
    @DisplayName("should validate nbPages")
    void validateNbPages() {
        String[] nbPagesList = {"1", "289", "22", "9999"};
        for (String nbPages : nbPagesList
        ) {
            assertTrue(stringValidatorBook.validateExpression("nbPages", nbPages));
        }
    }

    @Test
    @DisplayName("should not validate ISBN")
    void invalidateIsbn() {
        String[] isbnList = {null, "?", "15d48fr52we", "EDEEtytui776h8", "1", "2025"};
        for (String isbn : isbnList
        ) {
            assertFalse(stringValidatorBook.validateExpression("isbn", isbn));
        }
    }

    @Test
    @DisplayName("should not validate ISBN")
    void validateIsbn() {
        String[] isbnList = {"EDEEtytui776h", "123RttgebH781", "aaaaaaaaaa", "6767676376373", "DRDFHIOJKA"};
        for (String isbn : isbnList
        ) {
            assertTrue(stringValidatorBook.validateExpression("isbn", isbn));
        }
    }

    @Test
    @DisplayName("should not validate publicationYear")
    void invalidatePublicationYear() {
        String[] publicationYearList = {null, "?", "", "454", "1", "2025"};
        for (String publicationYear : publicationYearList
        ) {
            assertFalse(stringValidatorBook.validateExpression("publicationYear", publicationYear));
        }
    }


    @Test
    @DisplayName("should validate publicationYear")
    void validatePublicationYear1() {
        String[] publicationYearList = {"455", "1986", "1857", "2019"};
        for (String publicationYear : publicationYearList
        ) {
            assertTrue(stringValidatorBook.validateExpression("publicationYear", publicationYear));
        }
    }

    @Test
    @DisplayName("should not validate ShortStandard")
    void invalidateShortStandard() {
        String[] shortStandardList = {null, "?", "", "/e3e3e3e3e3e3e3dfgdfw"};
        for (String shortStandard : shortStandardList
        ) {
            assertFalse(stringValidatorBook.validateExpression("short", shortStandard));
        }
    }


    @Test
    @DisplayName("should validate ShortStandard")
    void validateShortStandard() {
        String[] shortStandardList = {"bAstien34", "rokoko", "M2345", "Jean-fleur", "marko polo", "Wee4", "22", "ed.", "frfr@fr", "/3e3e3e3e3e3e3dfgd", "ba", "bastien34$"};
        for (String shortStandard : shortStandardList
        ) {
            assertTrue(stringValidatorBook.validateExpression("short", shortStandard));
        }
    }

    @Test
    @DisplayName("should validate Name")
    void validateName() {
        String[] nameList = {"bAstien34$", "rokoko%2Q", "M234hu$5", "4322222y%T"};
        for (String name : nameList
        ) {
            assertTrue(stringValidatorBook.validateExpression("name", name));
        }
    }

    @Test
    @DisplayName("should not validate Name")
    void invalidateName() {
        String[] nameList = {null, "?", "", "marvin2", "j", "jen_marcel"};
        for (String name : nameList
        ) {
            assertFalse(stringValidatorBook.validateExpression("name", name));
        }
    }


    @Test
    @DisplayName("should not validate LongStandard")
    void invalidateLongStandard() {
        String[] longStandardList = {null, "?", "", "dedd  - $£34434-_ddddsdnkjnn4jkln498fnlkrn4lknr48*&^^lknlkn43lknlkncd98ylkne3lkn389jnslkfs089u8&**()\"@OM@kmpiohjfsdma;lkmsd dklmnlfksjdfyjn4j4890s9fd090&&**(*\"()\")jkjh4388&&7jh;fsldjloi4jh53k5jfsdnkjne"};
        for (String longStandard : longStandardList
        ) {
            assertFalse(stringValidatorBook.validateExpression("longStandard", longStandard));
        }
    }


    @Test
    @DisplayName("should validate LongStandard")
    void validateLongStandard() {
        String[] longStandardList = {"bAstien34", "rokoko", "M2345", "Jean-fleur", "marko polo", "Wee4", "22", "ed.", "frfr@fr", "/3e3e3erererererererereeredfederfre3%%^e3e3e3e3dfgd", "ba", "bastien34$"};
        for (String longStandard : longStandardList
        ) {
            assertTrue(stringValidatorBook.validateExpression("longStandard", longStandard));
        }
    }

    @Test
    void getException() {
        assertAll(
                () -> assertEquals("ISBN must be 10 or 13 characters: ", stringValidatorBook.getException("isbn")),
                () -> assertEquals("Author should have between 2 and 200 characters: ", stringValidatorBook.getException("author")),
                () -> assertEquals("Title should have between 2 and 200 characters: ", stringValidatorBook.getException("title")),
                () -> assertEquals("Edition should have between 2 and 200 characters: ", stringValidatorBook.getException("edition")),
                () -> assertEquals("NbPages should be between 1 and 9 999, please recheck: ", stringValidatorBook.getException("nbPages")),
                () -> assertEquals("Publication year should be between 455 and current: ", stringValidatorBook.getException("publicationYear")),
                () -> assertEquals("Invalid entry", stringValidatorBook.getException("ploc"))
        );
    }
}