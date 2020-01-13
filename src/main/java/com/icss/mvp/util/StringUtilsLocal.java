package com.icss.mvp.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author chengchenhui
 */
@SuppressWarnings("all")
public class StringUtilsLocal {

    private static Logger logger = Logger.getLogger(StringUtilsLocal.class);

    /**
     * 处理过长文本
     *
     * @param str 原文本
     * @param limit 需要显示的字数
     * @return 文本...
     */
    @Deprecated
    private static String subTextByLimit(String str, int limit) {
        byte[] datas = str.getBytes();
        int english = 0;
        for (byte b : datas) {
            if (b > 0 && b < 127) {
                english++;
            }
        }
        int validLength = str.length() - english / 2;

        if (limit < validLength) {
            int endIndex = 0;
            int chinese = 0;
            for (int i = 0; i < datas.length; i++) {
                if (datas[i] < 0) {
                    chinese++;
                }
                if (i + 1 == limit * 2) {
                    if (chinese % 2 == 0) {
                        endIndex = i + 1 - chinese / 2;
                    } else {
                        endIndex = i + 1 - (chinese + 1) / 2;
                    }
                    break;
                }
            }

            str = str.substring(0, endIndex) + "...";
        }
        return str;
    }

    /**
     * 描述: 判断字符串是否为空
     * 
     * @param str
     * @return
     * @since Ver 1.1
     * @see StringUtils.isBlank(String str)
     */
    @Deprecated
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
        // return (null == str || "".equals(str.trim()) || "NULL".equalsIgnoreCase(str.trim()));
    }

    /**
     * <pre>
     * 描述：将List<String> 转换为 字符串
     * 
     * @param stringList
     * @return returnType：String
     */
    @Deprecated
    public static String listToString(List<String> collection) {
        return CollectionUtilsLocal.joinIgnoreEmpty(collection);
        // if (stringList == null || stringList.size() == 0) {
        // return "";
        // }
    }

    /**
     * <pre>
     * 描述：将List<String> 转换为sql In的参数 字符串
     * 
     * @param stringList
     * @return returnType：String
     */
    @Deprecated
    public static String listToSqlIn(List<String> collection) {
        if (collection == null || collection.size() == 0) {
            return "'未找到'";
        }

        StringBuilder result = new StringBuilder();
        result.append("'").append(CollectionUtilsLocal.joinUniqueIgnoreEmpty(collection, "','")).append("'");

        return result.toString();
    }

    /**
     * <pre>
     * 描述：校验字符串是否包含中文
     * 
     * @param str
     * @return returnType：boolean
     */
    private static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * object转String 为空的转换为""
     * 
     * @param object
     * @return
     */
    public static String valueOf(Object object) {
        return object == null ? "" : object.toString();
        // if (object != null) {
        // return object.toString() == "null" ? "" : object.toString();
        // }
        // return "";
    }

    /**
     * object转Double 为空的转换为0.0
     * 
     * @param object
     * @return
     */
    public static Double parseDouble(Object object) {
        String value = valueOf(object);
        // TODO:try catch illegal format string
        // try {
        // Double.parseDouble(value);
        // } catch (NumberFormatException e) {
        // logger.error("Double.parseDouble exception, error: "+e.getMessage());
        // }
        return "".equals(value) ? 0.0D : Double.parseDouble(value);

        // String obj = (object == "") ? "0.0" : String.valueOf(object);
        // String value = (obj == "null") ? "0.0" : obj;
        // return Double.parseDouble(value);
    }

    /*
     * 毫秒转化为时分秒
     */
    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; // 天
        String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
        String strSecond = second < 10 ? "0" + second : "" + second;// 秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;// 毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strHour + ":" + strMinute + ":" + strSecond;
    }

    public static String deleteCharAtLastOne(StringBuilder rid) {
        String str = "";
        if (rid.length() > 0) {
            str = rid.deleteCharAt(rid.length() - 1).toString();
        }
        return str;
    }

    // 将中文符号转换为英文
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    // 去除回车和空格
    public static String clearSpaceAndLine(String mess) {
        String str = "";
        if (StringUtils.isNotEmpty(mess)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(mess);
            str = m.replaceAll("");
            str = ToDBC(str).toLowerCase();
        }
        return str;
    }

    /**
     * @Description:根据message获取提交人hw_account
     * @author Administrator
     * @date 2018年5月29日
     */
    public static String getSubmitAuthor(String commitMessage, String author) {
        String mes = clearSpaceAndLine(commitMessage);
        // 中英文冒号、回车、空格区分
        if (StringUtils.isNotEmpty(mes)) {
            if (mes.contains("author")) {// Author:
                String rgex = "author:(.*?):";
                String msg = getSubUtilSimple(mes, rgex);
                if (msg.contains(author)) {
                    return msg;
                }
            } else if (mes.contains("[author]")) {// [Author]:
                String rgex = "[author]:(.*?):";
                String msg = getSubUtilSimple(mes, rgex);
                if (msg.contains(author)) {
                    return msg;
                }
            } else if (mes.contains("提交人")) {// 提交人:
                String rgex = "提交人:(.*?):";
                String msg = getSubUtilSimple(mes, rgex);
                if (msg.contains(author)) {
                    return msg;
                }
            } else if (mes.contains("modification-person")) {// modification-person:
                String rgex = "modification-person:(.*?):";
                String msg = getSubUtilSimple(mes, rgex);
                if (msg.contains(author)) {
                    return msg;
                }
            }
        }
        return author;
    }

    /**
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
     * 
     * @param soap
     * @param rgex
     * @return
     */
    public static String getSubUtilSimple(String soap, String rgex) {
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    /**
     * 保留2位小数
     * 
     * @param d1
     * @return
     */
    public static Double keepTwoDecimals(double d1) {
        if (Double.isNaN(d1) || Double.isInfinite(d1)) {
            return 0.0;
        }
        if (d1 == 0.0) {
            return d1;
        }
        BigDecimal b = new BigDecimal(d1);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 给oldRet补齐i位，用value补位
     * 
     * @param oldRet
     * @param value
     * @param i
     * @return
     */
    public static String formatMakeUp(String oldRet, String value, int i) {
        if (oldRet.length() > i) {
            return oldRet;
        }
        StringBuilder ret = new StringBuilder();
        i = i - oldRet.length();
        for (int j = 0; j < i; j++) {
            ret.append(value);
        }
        ret.append(oldRet);
        return ret.toString();
    }

    /**
     * 向左补位
     * 
     * @param origin 字符串或整数
     * @param size 补位后字符串总长度
     * @param padStr 补位字符
     * @return
     */
    public static String leftPad(Object origin, int size, String padStr) {
        if (origin != null && origin instanceof Integer) {
            /**
             * <pre>
             *     %s   字符串类型
             *     %c   字符类型
             *     %b   布尔类型
             *     %d   整数类型（十进制）
             *     %x   整数类型（十六进制）
             *     %o   整数类型（八进制）
             *     %f   浮点类型
             *     %a   十六进制浮点类型
             *     %e   指数类型
             *     %g   通用浮点类型（f和e类型中较短的）
             *     %h   散列码
             *     %%   百分比类型
             *     %n   换行符
             *     %tx  日期与时间类型（x代表不同的日期与时间转换符
             * </pre>
             */
            String fromat = String.format("%%%s%dd", padStr, size);
            return String.format(fromat, origin);
        }

        String str = origin == null ? "" : origin.toString();
        return org.apache.commons.lang.StringUtils.leftPad(str, size, padStr);
    }

    /**
     * 获取登录客户端IP
     * 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    logger.error("InetAddress.getLocalHost exception, error: " + e.getMessage());
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * String保留两位小数
     * 
     * @param value
     * @return
     */
    @Deprecated
    public static String formatValue(String value) {
        int num = value.indexOf(".");
        if (num > 0 && num < value.length() - 3) {
            value = value.substring(0, num + 3);
        }
        return value;
    }

    /**
     * 字符串前面补零
     * 
     * @param code
     * @param num
     * @return
     */
    public static String zeroFill(String code, int num) {
        String res = "";
        if (StringUtils.isNotBlank(code)) {
            res = String.format("%0" + num + "d", Integer.parseInt(code));
        }
        return res;
    }
}
