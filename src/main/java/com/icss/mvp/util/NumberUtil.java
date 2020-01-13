package com.icss.mvp.util;

import com.icss.mvp.controller.ProjectLableController;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by chengchenhui on 2019/4/22.
 */
public class NumberUtil {
    private static Logger logger = Logger.getLogger(NumberUtil.class);

    private static String[] hanArr = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static String[] unitArr = {"十", "百", "千", "万", "十", "白", "千", "亿", "十", "百", "千"};

    /**
     * String转成int的值， 若无法转换，默认返回0
     */
    public static int stoi(String string) {
        return stoi(string, 0);
    }

    public static int stoi(String string, int defaultValue) {
        if ((string == null) || ("".equalsIgnoreCase(string))) {
            return defaultValue;
        }
        int id;
        try {
            id = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            logger.error("Integer.parseInt exception, error: " + e.getMessage());
            return defaultValue;
        }
        return id;
    }

    /**
     * String转成long的值， 若无法转换，默认返回0
     */
    public static long stol(String string) {
        return stol(string, 0);
    }

    public static long stol(String string, long defaultValue) {
        if ((string == null) || ("".equalsIgnoreCase(string))) {
            return defaultValue;
        }
        long ret;
        try {
            ret = Long.parseLong(string);
        } catch (NumberFormatException e) {
            logger.error("Long.parseLong exception, error: " + e.getMessage());
            return defaultValue;
        }

        return ret;
    }

    /**
     * String转成double的值， 若无法转换，默认返回0.00
     */
    public static double stod(String string) {
        return stod(string, 0.00);
    }

    public static double stod(String string, double defaultValue) {
        if ((string == null) || ("".equalsIgnoreCase(string))) {
            return defaultValue;
        }
        double ret;
        try {
            ret = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            logger.error("Double.parseDouble exception, error: " + e.getMessage());
            return defaultValue;
        }

        return ret;
    }

    /**
     * 将整数转成中文表示
     */
    public static String toChineseNum(int number) {
        String numStr = String.valueOf(number);
        String result = "";
        int numLen = numStr.length();
        for (int i = 0; i < numLen; i++) {
            int num = numStr.charAt(i) - 48;
            if (i != numLen - 1 && num != 0) {
                result += hanArr[num] + unitArr[numLen - 2 - i];
                if (number >= 10 && number < 20) {
                    result = result.substring(1);
                }
            } else {
                if (!(number >= 10 && number % 10 == 0)) {
                    result += hanArr[num];
                }
            }
        }
        return result;
    }


    /**
     * 获取一个属于[min, max)中的随机数
     */
    public static int random(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * String乘法保留两位小数
     *
     * @param number
     * @param multiplier
     * @return
     */
    public static String stringToDouble(String number, double multiplier) {
        if ((number == null) || (number.equalsIgnoreCase(""))) {
            return "0.00";
        }
        String minlat;
        try {
            BigDecimal maxlng = new BigDecimal(number);
            BigDecimal lat = maxlng.multiply(new BigDecimal(multiplier)).setScale(2, BigDecimal.ROUND_UP);//保留2位小数
            minlat = lat.toString();
        } catch (Exception e) {
            logger.error("maxlng.multiply exception, error: " + e.getMessage());
            return "0.00";
        }
        return minlat;
    }

    /**
     * String除法保留两位小数
     *
     * @param number
     * @param division
     * @return
     */
    public static String stringDivision(String number, double division) {
        if ((number == null) || (number.equalsIgnoreCase(""))) {
            return "0.00";
        }
        String minlat;
        try {
            BigDecimal maxlng = new BigDecimal(number);
            BigDecimal lat = maxlng.divide(new BigDecimal(division), 2, BigDecimal.ROUND_HALF_UP);//保留2位小数
            minlat = lat.toString();
        } catch (Exception e) {
            logger.error("maxlng.divide exception, error: " + e.getMessage());
            return "0.00";
        }
        return minlat;
    }

    public static Integer stringToInteger(String item) {
        return Integer.parseInt(item);
    }
}
