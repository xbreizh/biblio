package org.library.helper;

import javax.inject.Named;

@Named
public class PasswordCheckerImpl implements PasswordChecker{

    private int minLength = 3;
    private int maxLength = 12;

    @Override
    public String checkValidity(String password, String confirmPassword){
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";

        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;
        boolean numberPresent = false;
        boolean specialCharacterPresent = false;
        // check Equals
        if(!password.equals(confirmPassword)) {
            return "password mismatch";
        }

        if (checkIfLengthIsCorrect(password)) return "password must have between 3 and 12 characters";


        if (checkIfNumberPresent(password, numberPresent)) return "There should be at least a number";

        if (checkIfUpperCasePresent(password, upperCasePresent)) return "There should be at an Uppercase";

        if (checkIfLowerCasePresent(password, lowerCasePresent)) return "There should be at a Lowercase";

        if (checkIfSpecialCharacterPresent(password, specialChars, specialCharacterPresent))
            return "There should be at a Special character";

        return "";
    }

    private boolean checkIfLengthIsCorrect(String password) {
        return password.length() < minLength || password.length() > maxLength;
    }

    private boolean checkIfNumberPresent(String password, boolean numberPresent) {
        char currentCharacter;
        for (int i = 0; i < password.length(); i++) {
            currentCharacter = password.charAt(i);
            if (Character.isDigit(currentCharacter)) {
                numberPresent = true;
                break;
            }
        }

        if (!numberPresent) return true;
        return false;
    }

    private boolean checkIfUpperCasePresent(String password, boolean upperCasePresent) {
        char currentCharacter;
        for (int i = 0; i < password.length(); i++) {
            currentCharacter = password.charAt(i);
            if (Character.isUpperCase(currentCharacter)) {
                upperCasePresent = true;
                break;
            }
        }
        if (!upperCasePresent) return true;
        return false;
    }

    private boolean checkIfLowerCasePresent(String password, boolean lowerCasePresent) {
        char currentCharacter;
        for (int i = 0; i < password.length(); i++) {
            currentCharacter = password.charAt(i);
            if (Character.isLowerCase(currentCharacter)) {
                lowerCasePresent = true;
                break;
            }
        }
        if (!lowerCasePresent) return true;
        return false;
    }

    private boolean checkIfSpecialCharacterPresent(String password, String specialChars, boolean specialCharacterPresent) {
        char currentCharacter;
        for (int i = 0; i < password.length(); i++) {
            currentCharacter = password.charAt(i);
            if (specialChars.contains(String.valueOf(currentCharacter))) {
                specialCharacterPresent = true;
                break;
            }
        }
        if (!specialCharacterPresent) return true;
        return false;
    }
}
