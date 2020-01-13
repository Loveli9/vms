package com.icss.mvp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Deprecated
public class MonthUtil {

	public static String getMonth() {
		Calendar calendar=Calendar.getInstance();
		String month = calendar.get(Calendar.MONTH) + 1 + "";
		System.out.println(month);
		return null;
	}

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Date day = new Date();
		System.out.println(Integer.parseInt(sdf.format(day)));
	}
}