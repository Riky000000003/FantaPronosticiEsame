package com.example.fantapronosticiesame.Model;
public class Cookie {
    private static String cookieUsername = "";
    private static String cookieId = "";
    private static String cookieNome = "";
    private static String cookieCognome = "";
    private static String cookiePassowrd = "";
    public static String getCookieUsername() {
        return cookieUsername;
    }
    public static void setCookieUsername(String username) {
        cookieUsername = username;
    }
    public static String getCookieId() {
        return cookieId;
    }
    public static void setCookieId(String cookieId) {
        Cookie.cookieId = cookieId;
    }
    public static String getCookieNome() {
        return cookieNome;
    }
    public static void setCookieNome(String cookieNome) {
        Cookie.cookieNome = cookieNome;
    }
    public static String getCookieCognome() {
        return cookieCognome;
    }
    public static void setCookieCognome(String cookieCognome) {
        Cookie.cookieCognome = cookieCognome;
    }
    public static String getCookiePassowrd() {
        return cookiePassowrd;
    }
    public static void setCookiePassowrd(String cookiePassowrd) {
        Cookie.cookiePassowrd = cookiePassowrd;
    }
}

