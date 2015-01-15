package fr.utt.if26.istarve.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to validates the user credentials
 */
public class LoginValidationsUtils {
    /**
     * Check if the email is valid
     * @param email
     *  email to be checked
     * @return Boolean match
     */
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Check if the password complexity is valid ( 8 chars, 1 MAJ, 1 number, 1 special char)
     * @param password
     *  password to be checked
     * @return Boolean match
     */
    public static boolean isPasswordValid(String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

//        return password.length() >= 8;
    }

    /**
     * Check if the password match its confirmation
     * @param password
     *  password to be checked
     * @param password_confirmation
     *  confirmation of the password
     * @return Boolean match
     */
    public static boolean passwordMatchConfirmation(String password, String password_confirmation){ return password.equals(password_confirmation); }

}
