package org.troparo.business.impl.validator;

import javax.inject.Named;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class StringElementValidator {
    private static final String LOGIN_PATTERN = "^[A-z0-9_-]{5,20}$";
    private static final String FIRSTNAME_PATTERN = "^[A-z0-9_-]{5,20}$";
    private static final String LASTNAME_PATTERN = "^[A-z0-9_-]{5,20}$";
    private static final String PASSWORD_PATTERN = "^[A-z0-9_-]{5,20}$";
    private static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
            "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\" +
            "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
    private Pattern pattern;

    public StringElementValidator() {

    }

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String hex, String type) {
        System.out.println("doing stuff");
        if(hex==null) {
            return false;
        }
        switch (type) {

            case "login":
                System.out.println("cheking "+type);
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