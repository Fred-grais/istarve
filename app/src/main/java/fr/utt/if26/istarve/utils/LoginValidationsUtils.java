package fr.utt.if26.istarve.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginValidationsUtils {

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

//        return password.length() >= 8;
    }

    public static boolean passwordMatchConfirmation(String password, String password_confirmation){ return password.equals(password_confirmation); }

}
