package org.troparo.business.impl.validator;

import javax.inject.Named;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
public class StringValidator {

    private static final String LOGIN="login";
    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */


    public boolean validateExpression(String type, String hex) {
        Pattern pattern;

        if (hex == null) {
            return false;
        }
        switch (type) {

            case LOGIN:
                pattern = RegularExpression.LOGIN.getPattern();
                break;
            case "email":
                pattern = RegularExpression.EMAIL.getPattern();
                break;
            case "password":
                pattern = RegularExpression.PASSWORD.getPattern();
                break;
            default:
                pattern = RegularExpression.STANDARD.getPattern();

        }
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    public boolean validateForUpdateMember(String type, String hex) {
        if (type.equals(LOGIN) && hex == null) return false;
        if (hex != null ) {
            if (!hex.equals("?") && !hex.equals("")) {
                return validateExpression(type, hex);
            }
        }

        return true;

    }

    public String getException(String param) {
        switch (param) {
            case LOGIN:
                return "Login must be between 5 or 10 characters: ";
            case "firstName":
                return "FirstName must be between 5 or 20 characters: ";
            case "lastName":
                return "LastName must be between 5 or 20 characters: ";
            case "email":
                return "Invalid Email: ";
            case "password":
                return "Password should have between 2 and 10 characters, have at least a lower case, an upper case, a special character and a number";
            default:
                return "";
        }

    }


}