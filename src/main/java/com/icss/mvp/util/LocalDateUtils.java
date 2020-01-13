package com.icss.mvp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * Created by Ray on 2019/5/8.
 *
 * @author Ray
 * @date 2019/5/8 10:47
 */
public class LocalDateUtils {

    private static Logger                 logger                = Logger.getLogger(LocalDateUtils.class);

    private static final String           ERROR_MESSAGE_PATTERN = "%s exception, error: %s%s";

    /**
     *
     */
    public static final DateTimeFormatter SHORT_FORMAT_GENERAL  = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter ABBR_ENG_MONTH_FORMAT = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);

    public static final DateTimeFormatter YEAR_PERIOD_MONTH     = DateTimeFormatter.ofPattern("yyyy.MM");

    // public static final DateTimeFormatter STANDARD_FORMAT_GENERAL =
    // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // public static final DateTimeFormatter ISO_FORMAT_GENERAL =
    // DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * java.util.Date --> java.time.LocalDate
     */
    public static LocalDate parseLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        LocalDate result = null;

        try {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            result = localDateTime.toLocalDate();
        } catch (Exception e) {
            logger.error("LocalDateTime.ofInstant exeption, error: "+e.getMessage());
        }

        return result;
    }

    public static Date parseDate(LocalDate date) {
        if (date == null) {
            return null;
        }

        Date result = null;

        try {
            Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            result = Date.from(instant);
        } catch (Exception e) {
            logger.error("Date.from exception, error: "+e.getMessage());
        }

        return result;
    }

    /**
     * 把字符串转换成指定格式的日期
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            logger.error("simpleDateFormat.parse exception, error: "+e.getMessage());
        }
        return date;
    }
    
    public static Date plusDays(Date date, int days) {
        return parseDate(LocalDateUtils.parseLocalDate(date).plusDays(days));
    }

    /**
     * Creates a formatter using the specified pattern.
     *
     * <pre>
     *     字母	日期或时间元素	表示	示例
     *      G	Era 标志符	Text	AD
     *      y	年	Year	1996; 96
     *      M	年中的月份	Month	July; Jul; 07
     *      w	年中的周数	Number	27
     *      W	月份中的周数	Number	2
     *      D	年中的天数	Number	189
     *      d	月份中的天数	Number	10
     *      F	月份中的星期	Number	2
     *      E	星期中的天数	Text	Tuesday; Tue
     *      a	AM/PM 标记	Text	PM
     *      H	一天中的小时数（0-23）	Number	0
     *      k	一天中的小时数（1-24）	Number	24
     *      K	am/pm 中的小时数（0-11）	Number	0
     *      h	am/pm 中的小时数（1-12）	Number	12
     *      m	小时中的分钟数	Number	30
     *      s	分钟中的秒数	Number	55
     *      S	毫秒数	Number	978
     *      z	时区	General time zone	Pacific Standard Time; PST; GMT-08:00
     *      Z	时区	RFC 822 time zone	-0800
     * </pre>
     * 
     * @param pattern optional
     * @return default value with DateTimeFormatter.ofPattern("yyyy-MM-dd")
     * @see DateTimeFormatter#ofPattern(String)
     */
    private static DateTimeFormatter getFormatter(String... pattern) {
        DateTimeFormatter result = SHORT_FORMAT_GENERAL;
        try {
            if (pattern.length > 0) {
                result = DateTimeFormatter.ofPattern(pattern[0]);
            }
        } catch (Exception e) {
            String detail = String.format(", pattern: %s", Arrays.toString(pattern));
            logger.error(String.format(ERROR_MESSAGE_PATTERN, "DateTimeFormatter.ofPattern", e.getMessage(), detail));
        }

        return result;
    }

    public static LocalDate parseLocalDate(String text, String... pattern) {
        LocalDate result;

        try {
            result = LocalDate.parse(text, getFormatter(pattern));
        } catch (Exception e) {
            String detail = String.format("origin: %s, pattern: %s,", text, Arrays.toString(pattern));
            logger.error("parseLocalDate exception, " + detail + " error: " + e.getMessage());
            result = null;
        }

        return result;
    }

    public static String getCurrent(String pattern) {
        LocalDate current = LocalDate.now();
        return current.format(getFormatter(pattern));
    }

    public static String getCurrentYearString() {
        return getCurrent("yyyy");
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * 获取指定日期中月份的3字英文缩写
     * 
     * @param date LocalDate类型时间
     * @return 月份的3字英文缩写，入参为空对象时返回空白字符串
     */
    public static String getAbbreviatedMonth(LocalDate date) {
        return date != null ? date.format(ABBR_ENG_MONTH_FORMAT) : "";
    }

    public static String getAbbreviatedMonth(Date date) {
        return getAbbreviatedMonth(parseLocalDate(date));
    }

    public static String getCurrentAbbreviatedMonth() {
        return getAbbreviatedMonth(LocalDate.now());
    }

    /**
     * 将数字转化为月份的3字英文缩写
     * 
     * @param month 1-12的数字
     * @return 月份的3字英文缩写，无效数字返回空白字符串
     */
    public static String getAbbreviatedMonth(int month) {
        return (month > 0 && month < 13) ? getAbbreviatedMonth(LocalDate.of(2000, month, 1)) : "";
    }

    /**
     * 将字符串转化为月份的3字英文缩写
     * 
     * @param month 01 12 等可以转化为数字的字符串
     * @return 月份的3字英文缩写，转化错误则返回空白字符串
     */
    public static String getAbbreviatedMonth(String month) {
        return getAbbreviatedMonth(MathUtils.parseIntSmooth(month));
    }

}
