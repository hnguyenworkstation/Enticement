package com.greencode.enticement_android.Helpers;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hung Nguyen on 11/30/2016.
 */

public class StringChecker {
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    public static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);;
    public static Pattern passwordPattern;
    public static Matcher emailMatcher = null;

    public static boolean validateEmail(String email) {
        emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }
}
