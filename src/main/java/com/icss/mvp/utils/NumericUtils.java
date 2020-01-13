package com.icss.mvp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumericUtils {
    private NumericUtils() {
    }

    public static boolean isInteger(String val) {
        if (val == null) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[\\s]*[+-]{0,1}[0-9]+[\\s]*");
            Matcher isNum = pattern.matcher(val);
            return isNum.matches();
        }
    }

    public static boolean isDouble(String val) {
        if (val == null) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[\\s]*[+-]?[\\d]+(\\.[\\d]*)?[\\s]*");
            Matcher isNum = pattern.matcher(val);
            return isNum.matches();
        }
    }

    public static int parseInt(String val) {
        return Integer.parseInt(val.trim());
    }

    public static double parseDouble(String val) {
        return Double.parseDouble(val.trim());
    }

    public static <T> Integer parseIntegerMute(T args, Integer... defaultValue) {
        Integer result = defaultValue.length > 0 ? defaultValue[0] : null;

        String value = args == null ? null : args.toString();
        if (isInteger(value)) {
            result = Integer.parseInt(value);
        }

        return result;
    }

    public static <T> Double parseDoubleMute(T args, Double... defaultValue) {
        Double result = defaultValue.length > 0 ? defaultValue[0] : null;

        String value = args == null ? null : args.toString();
        if (isDouble(value) || isInteger(value)) {
            result = Double.parseDouble(value);
        }

        return result;
    }
}
