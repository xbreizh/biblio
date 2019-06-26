package org.troparo.business.impl.validator;

import org.apache.log4j.Logger;

import javax.inject.Named;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class StringValidatorBook {

    private static final String ISBN = "isbn";
    private static final String SHORT = "short";
    private static final String NAME = "name";
    private static final String PUBLICATIONYEAR = "publicationYear";
    private static final String NB_PAGES = "nbPages";
    private Logger logger = Logger.getLogger(StringValidatorBook.class);

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */


    public boolean validateExpression(String type, String hex) {
        Pattern pattern;
        String[] names = {"author"};
        String[] shortStandard = {"edition", SHORT};
        logger.info("Type: " + type + " / hex: " + hex);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        if (Arrays.asList(names).contains(type)) type = NAME;
        if (Arrays.asList(shortStandard).contains(type)) type = SHORT;
        if (hex == null) {
            return false;
        }

        switch (type) {

            case ISBN:
                pattern = RegularExpression.ISBN.getPattern();
                break;
            case PUBLICATIONYEAR:
                pattern = Pattern.compile("(45[5-9]|4[6-9][0-9]|[5-9][0-9]{2}|1[0-9]{3}|20[01][0-9]|" + thisYear + ")");
                break;
            case NB_PAGES:
                pattern = RegularExpression.NB_PAGES.getPattern();
                break;
            case NAME:
                pattern = RegularExpression.NAME.getPattern();
                break;
            case SHORT:
                pattern = RegularExpression.SHORT.getPattern();
                break;
            default:
                pattern = RegularExpression.LONG.getPattern();

        }
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }


    public String getException(String param) {
        switch (param) {
            case ISBN:
                return "ISBN must be 10 or 13 characters: ";
            case "author":
                return "Author should have between 2 and 200 characters: ";
            case "title":
                return "Title should have between 2 and 200 characters: ";
            case "edition":
                return "Edition should have between 2 and 200 characters: ";
            case NB_PAGES:
                return "NbPages should be between 1 and 9 999, please recheck: ";
            case PUBLICATIONYEAR:
                return "Publication year should be between 455 and current: ";
            default:
                return "Invalid entry";
        }

    }


}