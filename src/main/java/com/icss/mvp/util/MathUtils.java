package com.icss.mvp.util;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class MathUtils {

	private static Logger logger = Logger.getLogger(MathUtils.class);

	/**
	 * 获取标准差
	 *
	 * @param array
	 *            参数数组
	 * @param average
	 *            平均值
	 * @return 标准差
	 */
	public static Double getSigma(List<Double> array, double average) {
		int total = 0;
		for (Double anArray : array) {
			// 求出方差，如果要计算方差的话这一步就可以了
			total += (anArray - average) * (anArray - average);
		}
		// 求出标准差
		return Math.sqrt(total / array.size());
	}

	/**
	 * 获取平均值
	 * 
	 * @param array
	 *            参数数组
	 * @return 标准差
	 */
	public static Double getAverage(List<Double> array) {
		int sum = 0;
		for (Double anArray : array) {
			// 求出数组的总和
			sum += anArray;
		}
		// 求出数组的平均数
		return (double) (sum / array.size());
	}

	/**
	 * @param args
	 * @param <T>
	 * @return
	 */
	public static <T> int parseIntSmooth(T args, int... defaultValue) {
		int result = defaultValue.length > 0 ? defaultValue[0] : 0;
		try {
			result = Integer.parseInt(String.valueOf(args));
		} catch (Exception e) {
			logger.error("Integer.parseInt exception, args: " + String.valueOf(args) + ", error: " + e.getMessage());
		}
		return result;
	}

	/**
	 * @param args
	 * @param defaultValue
	 * @param <T>
	 * @return
	 */
	public static <T> Double parseDoubleSmooth(T args, Double defaultValue) {
		Double result = defaultValue;
		try {
			result = Double.parseDouble(String.valueOf(args));
		} catch (Exception e) {
			logger.error("Double.parseDouble exception, error: "+e.getMessage());
		}
		return result;
	}

//	public static int randomInt(int range) {
//		Random r = new Random();
//		return r.nextInt(range);
//	}
//
//	public static double randomDouble(double range) {
//		Random r = new Random();
//		return r.nextDouble() * range;
//	}

}
