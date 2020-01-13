package com.icss.mvp.util;

import java.math.BigDecimal;

public class BigDecUtils {
		private BigDecUtils() {
		}
	 
		/**
		 * 说明：
		 * 提供精确的加法运算
		 * 创建人: 李林君 邮箱： 
		 * 创建日期: 2013-9-28
		 * 
		 * @param v1 被加数
		 * @param v2 加数
		 * @return 两个参数的和
		 */
		public static double add(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(Double.toString(v1));// 建议写string类型的参数，下同
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.add(b2).doubleValue();
		}
	 
		/**
		 * 
		 * @param v1
		 * @param v2
		 * @return
		 */
		public static double sub(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.subtract(b2).doubleValue();
		}
	 
		/**
		 * 说明：
		 * 提供精确的乘法运算 
		 * 创建日期: 2013-9-28
		 * 
		 * @param v1
		 * @param v2
		 * @return
		 */
		public static double mul(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.multiply(b2).doubleValue();
		}
	 
	 
		/**
		 * 
		 * @param v1
		 * @param v2
		 * @param scale
		 * @return
		 */
		public static double div(double v1, double v2, Integer scale) {
			double result = 0;
			if (scale < 0) {
				throw new IllegalArgumentException(" the scale must be a positive integer or zero");
			}
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			try {
				result = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();// scale 后的四舍五入
			} catch (Exception e) {
				result = 0;
			}
			return result;
		}
	/** 
	    *  提供精确的四舍五入运算
	    * @param v :浮点数
	    * @param scale :精度
	    * @return double     
	    */
	    public static double round(double v, int scale) {
	        if (scale < 0) {
	            throw new IllegalArgumentException("The scale must be a positive integer or zero");
	        }
	        BigDecimal b = new BigDecimal(Double.toString(v));
	        BigDecimal one = new BigDecimal("1");
	        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	    }
	    /** 
		    *  两数相除四舍五入保存小数精度
		    * @param v :浮点数
		    * @param scale :精度
		    * @return double     
		    */
	public static double division(double v, double divisor,int scale) {
		double val = 0;
		if (divisor > 0) {
			val = v/divisor;
		}
		return round(val,2);
	}
}
