package com.icss.mvp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

public class DateUtils {
	private final static Logger LOG = Logger.getLogger(DateUtils.class);
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	public static final SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
	public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat YEAR_MONTH_FIRST = new SimpleDateFormat("yyyy-MM-01");
	public static final SimpleDateFormat YEAR_MONTH_MID = new SimpleDateFormat("yyyy-MM-15");
	public static final SimpleDateFormat YEAR_MONTH_NEXT = new SimpleDateFormat("yyyy-MM-16");

	/**
	 * yyyy-MM
	 */
	public static final SimpleDateFormat YEAR_HYPHEN_MONTH = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat YEAR_PERIOD_MONTH = new SimpleDateFormat("yyyy.MM");

	public static final SimpleDateFormat STANDARD_FORMAT_GENERAL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat SHORT_FORMAT_GENERAL = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat ISO_FORMAT_GENERAL = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	private DateUtils() {

	}

	/**
	 * 获得系统当前日期，并以YYYYMM格式返回
	 * 
	 * @return yyyyMM
	 */
	public static String getSystemMonth() {
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + 1 + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String reckonMonth = year + month;
		return reckonMonth;
	}

	/**
	 * 获得系统当前日期，并以YYYY格式返回
	 * 
	 * @return yyyyMM
	 */
	public static String getSystemYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR) + "";
	}

	/**
	 * 获得系统当前月前后n月，并以YYYYMM格式返回
	 * 
	 * @param n
	 *            1=下月 -1=上月
	 * 
	 * @return yyyyMM
	 */
	public static String getSystemFewMonth(int n) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, n);
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + 1 + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String reckonMonth = year + month;
		return reckonMonth;
	}

	/**
	 * 获得系统当前日期，并以YYYY-MM格式返回
	 * 
	 * @return yyyyMM
	 */
	public static String getMonth() {
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + 1 + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String reckonMonth = year + "-" + month;
		return reckonMonth;
	}

	/**
	 * 获得系统当前日期，并以YYYYMM格式返回
	 * 
	 * @return yyyyMM
	 */
	public static String getMonths() {
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + 1 + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String reckonMonth = year + month;
		return reckonMonth;
	}

	/**
	 * 获得系统当前日期的上一个月，并以YYYY-MM格式返回
	 * 
	 * @return yyyyMM
	 */
	public static String getPreMonth() {
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		if ("00".equals(month)) {
			year = String.valueOf(Integer.valueOf(year) - 1);
			month = "12";
		}
		String reckonMonth = year + "-" + month;
		return reckonMonth;
	}

	/**
	 * 获得系统当前日期，并以YYYYMMDD格式返回
	 * 
	 * @return yyyyMMdd
	 */
	public static String getToday() {
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + 1 + "";
		String day = c.get(Calendar.DAY_OF_MONTH) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}
		String reckon = year + month + day;
		return reckon;
	}

	/**
	 * 获得str日期当前月前后n天，并以yyyyMMdd格式返回
	 * 
	 * @param str
	 *            日期
	 * @param n
	 *            1=明天 -1=昨天
	 * 
	 * @return yyyyMMdd
	 */
	public static String getSystemFewDay(String str, int n) {
		Date date;
		try {
			date = format.parse(str);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, n);
			String dateStr = format.format(calendar.getTime());
			return dateStr;
		} catch (ParseException e) {
			LOG.error("日期格式错误");
			return null;
		}

	}

	/**
	 * 获取上月月底的时间
	 */
	public static String getLastMonthEnd() {
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 0);
		String lastDay = SHORT_FORMAT_GENERAL.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取上月周期月底的时间
	 */
	public static String lastCycleMonthEnd() {
		Calendar cale = Calendar.getInstance();
		Integer day = cale.get(Calendar.DATE);
		if (day > 15) {
			cale.set(Calendar.DAY_OF_MONTH, 0);
		} else {
			cale.add(Calendar.MONTH, -1);
			cale.set(Calendar.DAY_OF_MONTH, 0);
		}
		String lastDay = SHORT_FORMAT_GENERAL.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取当月月底的时间
	 */
	public static String getMonthEnd() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String end = SHORT_FORMAT_GENERAL.format(ca.getTime());
		return end;
	}

	/**
	 * 获取当月月中日期
	 */
	public static String getMidMonth() {
		Date date = new Date();
		String month = YEAR_MONTH_MID.format(date.getTime());
		return month;
	}

	/**
	 * 获取当月时间
	 */
	public static String getThisMonth() {
		Date date = new Date();
		String month = YEAR_HYPHEN_MONTH.format(date.getTime());
		return month;
	}

	/**
	 * 获取前几月的月份 传入当前年份，月份
	 * 
	 * @throws ParseException
	 */
	public static List<String> getLastMonth(String currentDate, int i) throws ParseException {
		Date date = SHORT_FORMAT_GENERAL.parse(currentDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.MONTH, -i);
		List<String> months = new ArrayList<>();
		if (day > 15) {
			months.add(YEAR_MONTH_NEXT.format(calendar.getTime()));
			months.add(SHORT_FORMAT_GENERAL.format(calendar.getTime()) + "-"
					+ calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			months.add(YEAR_MONTH_FIRST.format(calendar.getTime()));
			months.add(YEAR_MONTH_MID.format(calendar.getTime()));
		}
		return months;
	}

	/**
	 * 获取该周期的开始时间
	 */
	public static String getThisClcyeStart(String currentDate) {
		String month = null;
		try {
			Date date = SHORT_FORMAT_GENERAL.parse(currentDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			month = "";
			if (day > 15) {
				month = YEAR_MONTH_NEXT.format(calendar.getTime());
			} else {
				month = YEAR_MONTH_FIRST.format(calendar.getTime());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOG.error("YEAR_MONTH_FIRST.format exception, error: "+e.getMessage());
		}
		return month;
	}

	/**
	 * 获取该年的开始时间
	 */
	public static String getThisYearStart(String currentDate) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Date date = format.parse(currentDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int last = cal.getActualMinimum(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.DAY_OF_YEAR, last);
		String yearStr = format.format(cal.getTime());// 获取该年第一天
		return yearStr;
	}

	/**
	 * 获取上月月中日期
	 */
	public static String getLastMonthMid() {
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 0);
		String lastDay = YEAR_MONTH_MID.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取上月时间
	 */
	public static String getLastMonth() {
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 0);
		String lastDay = YEAR_HYPHEN_MONTH.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取上月第一天
	 */
	public static String getLastMonthOne() {
		Calendar cale = Calendar.getInstance();
		cale.set(Calendar.DAY_OF_MONTH, 0);
		String lastDay = YEAR_MONTH_FIRST.format(cale.getTime());
		return lastDay;
	}

	/**
	 * 获取双周报已完结的上个周期时间
	 */
	public static String getCurrentWeek() {
		Calendar cale = Calendar.getInstance();
		Integer day = cale.get(Calendar.DATE);
		String current = "";
		if (day > 15) {
			current = getMidMonth();
		} else {
			current = getLastMonthEnd();
		}
		return current;
	}

	/**
	 * 获取双周报当前的时间
	 */
	public static String getNowCurrentWeek() {
		Calendar cale = Calendar.getInstance();
		Integer day = cale.get(Calendar.DATE);
		String current = "";
		if (day > 15) {
			current = getMonthEnd();
		} else {
			current = getMidMonth();
		}
		return current;
	}

	/**
	 * 获取双周报上周的时间 传入当前日期的day
	 */
	public static String getLastWeekEnd() {
		Calendar cale = Calendar.getInstance();
		Integer day = cale.get(Calendar.DATE);
		String lastWeek = "";
		if (day > 15) {
			lastWeek = getLastMonthEnd();
		} else {
			lastWeek = getLastMonthMid();
		}
		return lastWeek;
	}

	/**
	 * 前几月月底 传入数字为回退月数
	 */
	public static String getWindMonthEnd(int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -month);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		String end = SHORT_FORMAT_GENERAL.format(c.getTime());
		return end;
	}

	/**
	 * 前几月月中 传入数字为回退月数
	 */
	public static String getWindMonthMid(int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -month);
		String mid = YEAR_MONTH_MID.format(c.getTime());
		return mid;
	}

	/**
	 * 获得str日期当前月前后n天，并以yyyyMMdd格式返回
	 * 
	 * @param str
	 *            日期
	 * @param n
	 *            1=明天 -1=昨天
	 * 
	 * @return yyyyMMdd
	 */
	public static String getDateFewDay(Date str, int n) {
		Date date = str;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, n);
		String dateStr = format.format(calendar.getTime());
		return dateStr;
	}

	/**
	 * 获得str日期当前月前后n月，并以yyyyMMdd格式返回
	 * 
	 * @param str
	 *            日期
	 * @param n
	 *            1=明天 -1=昨天
	 * 
	 * @return yyyyMMdd
	 */
	public static String getDateFewMonth(Date str, int n) {
		Date date = str;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, n);
		String dateStr = format.format(calendar.getTime());
		return dateStr;
	}

	/**
	 * 获得str日期当前月前后n月，并以yyyyMM格式返回
	 * 
	 * @param str
	 *            日期
	 * @param n
	 *            1=明天 -1=昨天
	 * 
	 * @return yyyyMMdd
	 */
	public static String getDateFewMonth(String str, int n) {
		Date date = null;
		try {
			date = format2.parse(str);
		} catch (ParseException e) {
			LOG.error("日期格式错误");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, n);
		String dateStr = format2.format(calendar.getTime());
		return dateStr;
	}

	/**
	 * 比较两个日期的大小，类型为yyyyMMdd end》beg true
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * 
	 * @return boolean
	 */
	public static boolean comparisonDateSize(String beginTime, String endTime) {
		Date bt;
		Date et;
		try {
			bt = format.parse(beginTime);
			et = format.parse(endTime);
			if (bt.before(et)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return false;
		}
	}

	/**
	 * 比较两个日期的大小，类型为yyyyMM end》beg true
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * 
	 * @return boolean
	 */
	public static boolean comparisonDateSize_yyyyMM(String beginTime, String endTime) {
		Date bt;
		Date et;
		try {
			bt = format2.parse(beginTime);
			et = format2.parse(endTime);
			if (bt.before(et)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return false;
		}
	}

	/**
	 * 获取两个时间段相差的周期数量
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Integer StatisticalCycle(Date startTime, Date endTime) {
		Calendar start = Calendar.getInstance();
		start.setTime(startTime);
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		int month = ((end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 + end.get(Calendar.MONTH)
				- start.get(Calendar.MONTH)) * 2;
		if (start.get(Calendar.DAY_OF_MONTH) > 15) {
			month += 1;
		} else {
			month += 2;
		}
		if (end.get(Calendar.DAY_OF_MONTH) <= 15) {
			month -= 1;
		}
		return Math.abs(month);
	}

	/**
	 * 比较两个日期的大小，类型为yyyyMMdd end》beg true
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * 
	 * @return boolean
	 */
	public static Integer calculationTimeDifference(String beginTime, String endTime) {
		Date bt;
		Date et;
		try {
			bt = format.parse(beginTime);
			et = format.parse(endTime);
			Long time = bt.getTime() - et.getTime();
			time = time / (1000 * 24 * 60 * 60);
			return time.intValue();
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return null;
		}
	}

	/**
	 * 获取两个日期相差月数，忽略天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Integer getMonthDiff(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);

		// 获取年的差值
		int yearInterval = year1 - year2;
		// 如果 d1的 月 小于 d2的 月 那么 yearInterval-- 这样就得到了相差的年数
		if (month1 < month2) {
			yearInterval--;
		}
		// 获取月数差值
		int monthInterval = (month1 + 12) - month2;

		monthInterval %= 12;
		int monthsDiff = Math.abs(yearInterval * 12 + monthInterval) + 1;
		return monthsDiff;
	}

	/**
	 * 比较两个日期的大小，类型为yyyyMM end》beg true
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * 
	 * @return boolean
	 */
	public static boolean comparisonDateSizeYM(Date beginTime, Date endTime) {
		Date bt;
		Date et;
		try {
			bt = format2.parse(format2.format(beginTime));
			et = format2.parse(format2.format(endTime));
			if (bt.before(et)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return false;
		}
	}

	/**
	 * 比较两个日期的大小，类型为yyyy-MM-dd HH:mm:ss end>begin true
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * 
	 * @return boolean
	 */
	public static boolean comparisonDateSizeYMdHms(String beginTime, String endTime) {
		Date bt = null;
		Date et = null;
		try {
			bt = STANDARD_FORMAT_GENERAL.parse(beginTime);
			et = STANDARD_FORMAT_GENERAL.parse(endTime);
			if (bt.getTime() <= et.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return false;
		}
	}

	/* 计算两之间段之间的天数 */
	public static double differenceTime(String beginTime, String endTime) {
		Date begin = null;
		Date end = null;
		try {
			begin = SHORT_FORMAT_GENERAL.parse(beginTime);
			end = SHORT_FORMAT_GENERAL.parse(endTime);
		} catch (ParseException e) {
			LOG.error("SHORT_FORMAT_GENERAL.parse exception, error: "+e.getMessage());
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Double.parseDouble(String.valueOf(between_days));
	}

	/**
	 * 计算两个日期的时间差，类型为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * 
	 * @return 分钟
	 */
	public static Double comparisonDateSizeYMdHmsTime(String beginTime, String endTime) {
		Date bt = null;
		Date et = null;
		Double time = 0.0;
		try {
			bt = STANDARD_FORMAT_GENERAL.parse(beginTime);
			et = STANDARD_FORMAT_GENERAL.parse(endTime);
			Long ms = et.getTime() - bt.getTime();
			time = Double.valueOf(ms) / 1000 / 60;
			return time;
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return time;
		}
	}

	public static String formatDate(SimpleDateFormat sourceFormat, SimpleDateFormat targetFormat, String dateString) {
		String result = "";
		try {
			result = targetFormat.format(sourceFormat.parse(dateString));
		} catch (ParseException e) {
			LOG.error("format date error: bad pattern, date: " + dateString);
		}
		return result;
	}

	public static String formatDate(SimpleDateFormat dateFormat, Date date) {
		return dateFormat.format(date);
	}

	// public static Date parseDate(SimpleDateFormat dateFormat, String
	// dateString) {
	// Date date = null;
	// try {
	// date = dateFormat.parse(dateString);
	// } catch (Exception e) {
	// LOG.error("parse date error, date: " + dateString);
	// }
	// return date;
	// }

	public static Date parseDate(SimpleDateFormat dateFormat, String dateString, Date... defaultValue) {
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			LOG.error("parse date error, date: " + dateString);
		}

		if (date == null && defaultValue.length > 0) {
			date = defaultValue[0];
		}

		return date;
	}

	public static Date parseISODate(String dateStr) {
		TemporalAccessor accessor = DateTimeFormatter.ISO_DATE_TIME.parse(dateStr);
		return Date.from(Instant.from(accessor));
	}

	/**
	 * 获取month月的最后一天
	 * 
	 * @param month
	 * @return
	 */
	public static Date getMonthLastDay(int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 0);
		return c.getTime();
	}

	/**
	 * 获取YYYY-MM格式月下最后一天
	 * 
	 * @param month
	 * @return
	 */
	public static Date getMonthLastDay(String month) {
		Calendar c = Calendar.getInstance();
		int y = Integer.parseInt(month.substring(0, 4));
		int m = Integer.parseInt(month.substring(5));
		c.set(Calendar.YEAR, y);
		c.set(Calendar.MONTH, m);
		c.set(Calendar.DAY_OF_MONTH, 0);
		return c.getTime();
	}

	/**
	 * 获取指定格式下的当月第一天
	 * 
	 * @param args
	 */
	public static String getFirstDayNowMonth() {
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + 1 + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		String reckonMonth = year + "/" + month + "/01+00:00:00";
		return reckonMonth;
	}

	/**
	 * 获取指定格式下的上月第一天
	 * 
	 * @param args
	 */
	public static String getFirstDayPreMonth() {
		Calendar c = Calendar.getInstance();
		String year = c.get(Calendar.YEAR) + "";
		String month = c.get(Calendar.MONTH) + "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		if ("00".equals(month)) {
			year = String.valueOf(Integer.valueOf(year) - 1);
			month = "12";
		}
		String reckonMonth = year + "/" + month + "/01+00:00:00";
		return reckonMonth;
	}

//	/**
//	 * 将传入日期(yyyy-MM-dd)以指定格式返回
//	 *
//	 * @param args
//	 */
//	public static String changeDate(String date) {
//		String year = date.substring(0, date.indexOf("-"));
//		String month = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
//		String day = date.substring(date.lastIndexOf("-") + 1);
//		String reckonMonth = year + "/" + month + "/" + day + "+00:00:00";
//		return reckonMonth;
//	}

	/**
	 * 获取传入日期的下一天,并以yyyy-MM-dd返回
	 * 
	 */
	public static String getNextDay(String now) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = SHORT_FORMAT_GENERAL.parse(now);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOG.error("SHORT_FORMAT_GENERAL.parse exception, error: "+e.getMessage());
		}
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		String dateStr = SHORT_FORMAT_GENERAL.format(c.getTime());
		return dateStr;
	}

	/**
	 * 获取当天,并以yyyy-MM-dd返回
	 * 
	 */
	public static String getTodayDay() {
		Calendar c = Calendar.getInstance();
		String dateStr = SHORT_FORMAT_GENERAL.format(c.getTime());
		return dateStr;
	}

	/**
	 * 计算两个日期相差的天数 date1 - date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int betweenDays(Date date1, Date date2) {
		int days = (int) Math.ceil((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	public static Date getFirstDayOfMonth(Date... dates) {
		return getFirstDayOfAmountMonth(0, dates);
	}

	public static Date getFirstDayOfNextMonth(Date... dates) {
		return getFirstDayOfAmountMonth(1, dates);
	}

	public static Date getFirstDayOfAmountMonth(int amount, Date... dates) {
		Date date = dates.length > 0 ? dates[0] : new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.MONTH, amount);

		return calendar.getTime();
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, String pattern) {
		String formatDate = null;
		if (pattern != null) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * 
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {

		int season = 0;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}

	/**
	 * 取得指定年月的第一天
	 * 
	 * @param yearMonthStr
	 *            年月
	 * @return : firstDay 第一天
	 * @author : youyd
	 * @version : 1.00
	 * @throws ParseException
	 * @create time : 2013-2-25 下午12:43:16
	 * @description : 取得指定年月的第一天
	 */
	public static Date getFirstDay(String yearMonthStr) throws ParseException {
		// 当日期字符串不为空或者""时，转换为Date类型
		if (yearMonthStr != null || !"".equals(yearMonthStr)) {
			Date yearMonth = parseDate(yearMonthStr, "yyyy-MM");
			// 实例化Calendar类型
			Calendar cal = Calendar.getInstance();
			// 设置年月
			cal.setTime(yearMonth);
			// 设置日期为该月第一天
			cal.set(Calendar.DATE, 1);
			// 返回指定年月的第一天
			return cal.getTime();
		} else {
			return null;
		}

	}

	/**
	 * 取得指定年月的最后一天
	 * 
	 * @param yearMonthStr
	 *            年月
	 * @return : lastDay 最后一天
	 * @author : youyd
	 * @version : 1.00
	 * @throws ParseException
	 * @create time : 2013-2-25 下午12:43:16
	 * @description : 取得指定年月的最后一天
	 */
	public static Date getLastDay(String yearMonthStr) throws ParseException {
		// 当日期字符串不为空或者""时，转换为Date类型
		if (yearMonthStr != null || !"".equals(yearMonthStr)) {
			Date yearMonth = parseDate(yearMonthStr, "yyyy-MM");
			// 实例化Calendar类型
			Calendar cal = Calendar.getInstance();
			// 设置年月
			cal.setTime(yearMonth);
			// 设置月份为下一月份
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			// 设置日期为下一月份第一天
			cal.set(Calendar.DATE, 1);
			// 设置时间为23时
			cal.set(Calendar.HOUR_OF_DAY, 23);
			// 设置时间为59分
			cal.set(Calendar.MINUTE, 59);
			// 设置时间为59秒
			cal.set(Calendar.SECOND, 59);
			// 设置时间为999毫秒
			cal.set(Calendar.MILLISECOND, 999);
			// 回滚一天 即上月份的最后一天
			cal.roll(Calendar.DATE, -1);
			// 返回指定年月的最后一天
			return cal.getTime();
		} else {
			return null;
		}

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
			LOG.error("simpleDateFormat.parse exception, error: "+e.getMessage());
		}
		return date;
	}

	// 日期转换成字符串
	public static String dateToStr(Date date, String format) {// 参数是日期字符串、格式字符串
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		return dateStr;
	}

	// 获取时间区间
	public static List<String> evaluationCycle(Integer continuousCycle, String cycle, Integer cycleCalculation) {
		List<String> time = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		try {
			List<String> cycles = Arrays.asList(cycle.split("-"));
			Integer cycleMonth = Integer.valueOf(cycles.get(0));
			Integer cycleWeek = Integer.valueOf(cycles.get(1));// 获取月和周
			Calendar calendar = Calendar.getInstance();
			Integer month = calendar.get(Calendar.MONTH) + 1;// 获取当前月
			Integer week = cycleWeek - cycleCalculation;// 计算出当前是第几周
			if (!month.equals(cycleMonth)) {
				calendar.set(Calendar.MONTH, -1);
			}
			if (week <= 0) {
				calendar.set(Calendar.DAY_OF_MONTH, 0);
				int days = calendar.get(Calendar.DAY_OF_MONTH);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				int toWeek = calendar.get(Calendar.DAY_OF_WEEK);
				double a = days + toWeek - 9;
				week = (int) (Math.ceil(a / 7.0)) + week;
			}
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			int toWeek = 9 - calendar.get(Calendar.DAY_OF_WEEK);
			int end = (week * 7 + toWeek);
			int start = end - 7 * continuousCycle;
			Integer nowMonth = calendar.get(Calendar.MONTH);
			Integer nowYear = calendar.get(Calendar.YEAR);
			calendar.set(Calendar.DAY_OF_MONTH, end);
			String endTime = format.format(calendar.getTime());
			calendar.set(nowYear, nowMonth, 1);// 避免出现跳到其它月份
			calendar.set(Calendar.DAY_OF_MONTH, start);
			String startTime = format.format(calendar.getTime());
			time.add(startTime);
			time.add(endTime);
		} catch (Exception e) {
			e.getMessage();
		}
		return time;
	}

	/**
	 * 获取最新的月份
	 * 
	 * @param num
	 *            获取数量
	 * @return
	 */
	public static String[] getLatestMonths(int num) {// 参数是日期字符串、格式字符串
		String[] str = new String[num];
		for (int i = 0; i < num; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -(num - i - 1));
			str[i] = YEAR_HYPHEN_MONTH.format(calendar.getTime());
		}
		return str;
	}

	/***
	 * 一个月分为上下半月两个周期，日期格式是yyyy-MM-dd 获取最近几个周期
	 * 
	 * @param num
	 *            周期数
	 * @param flag
	 *            是否包括当前周期
	 * @return
	 */
	public static List<String> getLatestCycles(int num, boolean flag) {
		String[] months = getLatestMonths(num / 2 + 2);
		List<String> result = new ArrayList<String>();
		for (int i = months.length - 1; i >= 0; i--) {
			result.add(SHORT_FORMAT_GENERAL.format(getMonthLastDay(months[i])));
			result.add(months[i] + "-15");
		}
		Calendar nowDate = Calendar.getInstance();
		int day = nowDate.get(Calendar.DAY_OF_MONTH);
		if (day <= 15) {
			result = result.subList(1, result.size() - 1);
		}
		if (flag) {
			result = result.subList(0, num);
		} else {
			result = result.subList(1, num + 1);
		}
		return result;
	}

	/**
	 * 获取当前周期的上一周期 传入数字为回退月数,日期
	 */
	public static String getWindMonth(int month, String time) {
		String[] date = time.split("-");// 拆分时间
		Calendar c = Calendar.getInstance();
		c.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]), 0);
		String end = YEAR_MONTH_MID.format(c.getTime());
		if (Integer.valueOf(date[2]) <= 15) {
			c.set(Integer.valueOf(date[0]), (Integer.valueOf(date[1]) - month), 0);
			end = SHORT_FORMAT_GENERAL.format(c.getTime());
		}
		return end;
	}

	public static String getWindMonthMid(int month, String time) {
		String[] date = time.split("-");// 拆分时间
		Calendar c = Calendar.getInstance();
		c.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - month, 0);
		String end = YEAR_MONTH_MID.format(c.getTime());
		return end;
	}

	public static String getWindMonthEnd(int month, String time) {
		String[] date = time.split("-");// 拆分时间
		Calendar c = Calendar.getInstance();
		c.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - month, 0);
		String end = SHORT_FORMAT_GENERAL.format(c.getTime());
		return end;
	}

	/***
	 * 一个月分为上下半月两个周期，日期格式是yyyy-MM-dd 获取最近几个周期
	 * 
	 * @param num
	 *            周期数
	 * @param flag
	 *            是否包括当前周期 传入固定时间 time
	 * @return
	 */
	public static List<String> getLatestWeek(int num, boolean flag, String time) {
		String[] date = time.split("-");// 拆分时间
		Calendar calendar = Calendar.getInstance();
		int n = num / 2 + 2;
		int j = n;
		String[] months = new String[n];
		for (; n > 0; n--) {
			calendar.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]), 0);
			calendar.add(Calendar.MONTH, -(n - 1));
			months[j - n] = YEAR_HYPHEN_MONTH.format(calendar.getTime());
		}
		List<String> result = new ArrayList<String>();
		for (int i = months.length - 1; i >= 0; i--) {
			result.add(SHORT_FORMAT_GENERAL.format(getMonthLastDay(months[i])));
			result.add(months[i] + "-15");
		}
		if (Integer.valueOf(date[2]) <= 15) {
			result = result.subList(1, result.size() - 1);
		}
		if (flag) {
			result = result.subList(0, num);
		} else {
			result = result.subList(1, num + 1);
		}
		return result;
	}

	/***
	 * 一个月分为上下半月两个周期，日期格式是yyyy-MM-dd 获取两个时间段内的周期
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static List<String> getInHoursCycles(String startDate, String endDate) {
		List<String> result = new ArrayList<String>();
		try {
			int num = getMonthDiff(SHORT_FORMAT_GENERAL.parse(startDate), SHORT_FORMAT_GENERAL.parse(endDate)) + 1;
			String[] months = new String[num];
			for (int i = 0; i < num; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(SHORT_FORMAT_GENERAL.parse(startDate));
				calendar.add(Calendar.MONTH, (num - i - 1));
				months[i] = YEAR_HYPHEN_MONTH.format(calendar.getTime());
			}
			for (int i = months.length - 1; i >= 0; i--) {
				String mouthMid = months[i] + "-15";
				String monthLast = SHORT_FORMAT_GENERAL.format(getMonthLastDay(months[i]));

				if (comparisonDateSize(startDate.replace("-", ""), mouthMid.replace("-", ""))
						&& comparisonDateSize(mouthMid.replace("-", ""), endDate.replace("-", ""))) {
					result.add(mouthMid);
				}
				if (comparisonDateSize(startDate.replace("-", ""), monthLast.replace("-", ""))
						&& comparisonDateSize(monthLast.replace("-", ""), endDate.replace("-", ""))) {
					result.add(monthLast);
				}
			}
		} catch (ParseException e) {
			LOG.error("类型转换异常：", e);
		}

		return result;
	}

	public static String[] getInMonths(Date startDate, Date endDate) {
		int num = getMonthDiff(startDate, endDate);
		String[] months = new String[num];
		for (int i = 0; i < num; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, (num - i - 1));
			months[i] = YEAR_HYPHEN_MONTH.format(calendar.getTime());
		}
		return months;
	}

	public static void main(String[] args) throws ParseException {
		// System.out.println(getSystemFewDay("20180413", 1));
		// System.out.println(comparisonDateSize("20180413", "20180413"));
		// System.out.println(getFirstDayNowMonth());
		// System.out.println(getFirstDayPreMonth());
		// System.out.println(getMonth());
		// System.out.println(getPreMonth());
		// System.out.println(getNextDay("2016-02-05"));
		// System.out.println(getTodayDay());
		// System.out.println(changeDate("2018-05-16"));
		// System.out.println(getInHoursCycles("2019-04-15","2019-05-31").toString());
	}

	/**
	 * 获取传入周期的下一周期
	 * 
	 * @param time
	 * @return
	 */
	public static String getNextCycle(String time) {
		Calendar cale = Calendar.getInstance();
		String[] date = time.split("-");// 拆分时间
		String nextDate = "";
		try {
			cale.setTime(SHORT_FORMAT_GENERAL.parse(time));
		} catch (ParseException e) {
			LOG.error("获取传入周期的下一周期失败！");
		}
		if (Integer.valueOf(date[2]) <= 15) {
			cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
			nextDate = SHORT_FORMAT_GENERAL.format(cale.getTime());
		} else {
			cale.add(Calendar.MONTH, 1);
			nextDate = YEAR_MONTH_MID.format(cale.getTime());
		}
		return nextDate;
	}

	/**
	 * 传入周期的天数小于等于15，将天数改为15， 否则改为传入周期对应月份的最后一天
	 * 
	 * @param time
	 * @return
	 */
	public static String getModifyDay(String time) {
		Calendar cale = Calendar.getInstance();
		String[] date = time.split("-");// 拆分时间
		String resDate = "";
		try {
			cale.setTime(SHORT_FORMAT_GENERAL.parse(time));
		} catch (ParseException e) {
			LOG.error("传入周期的天数小于等于15，将天数改为15， 否则改为传入周期对应月份的最后一天！");
		}
		if (Integer.valueOf(date[2]) <= 15) {
			resDate = YEAR_MONTH_MID.format(cale.getTime());
		} else {
			cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
			resDate = SHORT_FORMAT_GENERAL.format(cale.getTime());
		}
		return resDate;
	}

	/**
	 * 字符串转Date
	 * 
	 * @param val
	 * @return
	 */
	public static Date transformDate(String val) {
		Date date = null;
		SimpleDateFormat sdf = null;
		if (val != null || val.length() != 0) {
			date = new Date();
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				int intVal = Integer.parseInt(val);
				Calendar calendar = new GregorianCalendar(1900, 0, -1);
				calendar.add(Calendar.DATE, intVal);
				date = calendar.getTime();
			} catch (Exception e) {
				LOG.error("number transform date failed: " + e.getMessage());
			}
		}
		return date;
	}

	/**
	 * 判断该日期是否是该月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取传入周期前后n天,日期格式是yyyy-MM-dd
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static String getAroundDay(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, n);
		String dateStr = SHORT_FORMAT_GENERAL.format(calendar.getTime());
		return dateStr;
	}

	/***
	 * 一个月分每周一个周期，日期格式是yyyy-MM-dd 获取最近几个周期
	 * 
	 * @param num
	 *            周期数
	 * @param flag
	 *            是否包括当前周期
	 * @return
	 */
	public static List<String> getCrreateTime(int num, boolean flag) {
		List<String> result = new ArrayList<String>();
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DATE, 6 - weekday);
		Date date = c.getTime();
		c.setTime(date);
		for (int i = 0; i < num; i++) {
			c.add(Calendar.DATE, -7);
			Date date1 = c.getTime();
			result.add(DateUtils.SHORT_FORMAT_GENERAL.format(date1));

		}
		return result;
	}

	/**
	 * 获取当前月份以及前n个月份
	 * @param n
	 * @return
	 */
	public static List<String> getRecentMonths(int n) {
		List<String> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 2);

		for (int i = 0; i < n; i++) {
			cal.add(Calendar.MONTH, -1);
			String month = cal.get(Calendar.MONTH) + "";
			String year = cal.get(Calendar.YEAR) + "";
			if (month.length() == 1 && !"0".equals(month)) {
				month = "0" + month;
			}
			if ("0".equals(month)) {
				month = "12";
				int a = cal.get(Calendar.YEAR);
				year = a - 1 + "";
			}
			list.add(year + "-" + month);
		}
		return list;
	}
	
	/**
	 * 获取传入时间所属的周日以及前n个周日
	 * @param num
	 * @return
	 */
	public static List<String> getEverySunday(Date selectDate, int num) {
		List<String> result = new ArrayList<String>();
//		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(selectDate);
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DATE, 8 - weekday);
		Date date = c.getTime();
		result.add(DateUtils.SHORT_FORMAT_GENERAL.format(date));
		c.setTime(date);
		for (int i = 0; i < num - 1; i++) {
			c.add(Calendar.DATE, -7);
			Date date1 = c.getTime();
			result.add(DateUtils.SHORT_FORMAT_GENERAL.format(date1));
		}
		return result;
	}
	
	/**
	 * 获取传入周期所属月份的第一天
	 * @return
	 * @throws ParseException 
	 */
	public static String getFirstDayOfMonth(String date) throws ParseException{
		Calendar cale = Calendar.getInstance();
		
		cale.setTime(DateUtils.SHORT_FORMAT_GENERAL.parse(date));
		cale.set(Calendar.DAY_OF_MONTH, 1);
		
		return YEAR_MONTH_FIRST.format(cale.getTime());
	}
	
	/**
	 * 获取传入周期是每月第几周
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getWeekOfMonth(String date) throws ParseException {
		String[] dat = date.split("-");
		Date date2 = DateUtils.SHORT_FORMAT_GENERAL.parse(date);
		Calendar cale = Calendar.getInstance();
		cale.setFirstDayOfWeek(cale.MONDAY);
		cale.setTime(date2);
		int weekOfMonth = cale.get(Calendar.WEEK_OF_MONTH);
		return dat[0] + "年" + dat[1] + "月" + "第" + weekOfMonth + "周";
	}
	
	/**
	 * 获取传入周期所属月份的最后一天
	 * @param date
	 * @return
	 */
	public static String getEndOfMonth(String date) {
		Calendar ca = Calendar.getInstance();
		try {
			ca.setTime(SHORT_FORMAT_GENERAL.parse(date));
		} catch (ParseException e) {
			LOG.error("getEndOfMonth failed:" + e.getMessage());
		}
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String end = SHORT_FORMAT_GENERAL.format(ca.getTime());
		return end;
	}
	
	/**
	 * 判断传入时间是否为周六、日
	 * @param date
	 * @return
	 */
	public static Boolean isSaturdayAndSunday(String date) {
		Boolean flag = false;
		Calendar cal = Calendar.getInstance();
		
		try {
			cal.setTime(DateUtils.SHORT_FORMAT_GENERAL.parse(date));
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				flag = true;
			}
		} catch (ParseException e) {
			LOG.error("DateUtils isSaturdayAndSunday failed:" + e.getMessage());
		}

		return flag;
	}
	
}
