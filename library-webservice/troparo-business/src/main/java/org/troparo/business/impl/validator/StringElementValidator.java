package org.troparo.business.impl.validator;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class StringElementValidator {
    /*@Value("${LOGIN_PATTERN}")
    private String LOGIN_PATTERN;
    @Value("${FIRSTNAME_PATTERN}")
    private String FIRSTNAME_PATTERN;
    @Value("${LASTNAME_PATTERN}")
    private String LASTNAME_PATTERN;
    @Value("${PASSWORD_PATTERN}")
    private String PASSWORD_PATTERN;
    @Value("${EMAIL_PATTERN}")
    private String EMAIL_PATTERN;*/
    private Pattern pattern;
    public enum VerifExpression
    {
        LOGIN("^[A-z0-9_-]{5,20}$"),
        PASSWORD("^[A-z0-9_-]{5,20}$"),
        FIRSTNAME("^[A-z0-9_-]{5,20}$"),
        LASTNAME("^[A-z0-9_-]{5,20}$"),
        EMAIL("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
        private final Pattern  pattern;

        VerifExpression(final String regex){
            this.pattern=Pattern.compile(regex);
        }

        public Pattern getPattern(){
            return this.pattern;
        }
    }


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
                pattern = VerifExpression.LOGIN.getPattern();
                break;
            case "firstName":
                pattern = VerifExpression.FIRSTNAME.getPattern();
                break;
            case "lastName":
                pattern = VerifExpression.LASTNAME.getPattern();
                break;
            case "email":
                pattern = VerifExpression.EMAIL.getPattern();
                break;
            case "password":
                pattern = VerifExpression.PASSWORD.getPattern();
                break;
            default:

        }
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }


}