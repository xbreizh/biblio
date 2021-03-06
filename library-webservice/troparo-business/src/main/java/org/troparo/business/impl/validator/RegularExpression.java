package org.troparo.business.impl.validator;

import java.util.regex.Pattern;

public enum RegularExpression {
    LOGIN("^[A-z0-9_-]{5,10}$"), // lower or upper case, only num and letters, 5-10 characters
    NAME("^[a-zA-Z\\- ]{2,20}$"), // lower or upper case, NO num - allowed, 2-20 characters
    SHORT("^.{2,20}$"), //any character, length 1-20
    ISBN("^[A-z0-9_-]{10}$||^[A-z0-9_-]{13}$"), //numbers and letters, length 10 or 13
    NB_PAGES("([1-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|[1-8][0-9]{3}|9[0-8][0-9]{2}|99[0-8][0-9]|999[0-9])"), //should be between 1 and 9999
    LONG("^.{2,200}$"), //any character, length 1-200
    PASSWORD("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+*=])(?=\\S+$).{5,}$"), // min 1 upper, min 1 lower, min 1 digit, min 1 special, 5-10 characters
    EMAIL("(?:[A-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Z0-9](?:[A-Z0-9-]*[A-Z0-9])?\\.)+[A-Z0-9](?:[A-Z0-9-]*[A-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    private final Pattern pattern;

    RegularExpression(final String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return this.pattern;
    }
}
