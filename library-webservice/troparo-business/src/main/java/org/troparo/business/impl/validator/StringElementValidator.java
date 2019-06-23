package org.troparo.business.impl.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class StringElementValidator {
    @Value("${LOGIN_PATTERN}")
    private String LOGIN_PATTERN;
    @Value("${FIRSTNAME_PATTERN}")
    private String FIRSTNAME_PATTERN;
    @Value("${LASTNAME_PATTERN}")
    private String LASTNAME_PATTERN;
    @Value("${PASSWORD_PATTERN}")
    private String PASSWORD_PATTERN;
    @Value("${EMAIL_PATTERN}")
    private String EMAIL_PATTERN;
    private Pattern pattern;

    public StringElementValidator() {

    }

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validateLogin(final String hex) {
        if (hex == null) {
            return false;
        }

        Matcher matcher = Pattern.compile("^[A-z0-9_-]{5,20}$").matcher(hex);
        return matcher.matches();

    }


    public boolean validate(final String hex, String type) {

        if (hex == null) {
            return false;
        }
        switch (type) {

            case "login":
                pattern = Pattern.compile(LOGIN_PATTERN);
                break;
            case "firstName":
                pattern = Pattern.compile(FIRSTNAME_PATTERN);
                break;
            case "lastName":
                pattern = Pattern.compile(LASTNAME_PATTERN);
                break;
            case "email":
                pattern = Pattern.compile(EMAIL_PATTERN);
                break;
            case "password":
                pattern = Pattern.compile(PASSWORD_PATTERN);
                break;
            default:

        }
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }


}