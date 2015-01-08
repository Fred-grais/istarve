package fr.utt.if26.istarve.utils;

public class LoginValidationsUtils {

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    public static boolean passwordMatchConfirmation(String password, String password_confirmation){ return password.equals(password_confirmation); }

}
