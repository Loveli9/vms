package com.icss.mvp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.log4j.Logger;

import com.icss.mvp.entity.MeasureComment;

public class MeasureUtils {
	private static Logger logger = Logger.getLogger(MeasureUtils.class);

	/**
	 * 质量点灯
	 */
	public static String light(MeasureComment measureComment) {
		Double measureValue = null;
		if (measureComment.getMeasureId() == 314) {// CI全量回归
			measureValue = dealHour(measureComment.getMeasureValue());
		} else {
			measureValue = deal(measureComment.getMeasureValue());
		}

		if (measureValue == null) {
			return null;
		}
		Double upper = deal(measureComment.getUpper());
		Double lower = deal(measureComment.getLower());
		Double target = deal(measureComment.getTarget());
        Double challenge = deal(measureComment.getChallenge());
        String light = "";
		String type = measureComment.getComputeRule();
		if ("13".equals(type)) {// 下限优先
			if (lower == null) {
				return null;
			}

			if (null != fuhao(measureComment.getLower())) {
				light = signLight(fuhao(measureComment.getLower()), measureValue, lower);
			} else {
				if (target == null) {
					light = measureValue <= lower ? "green" : "red";
				} else {
					if (upper != null) {
						if (measureValue <= target && measureValue >= lower) {
							light = "green";
						} else if (measureValue > target && measureValue <= upper) {
							light = "yellow";
						} else {
							light = "red";
						}
					} else {
						light = (measureValue <= target && measureValue >= lower) ? "green" : "red";
					}
				}
			}

		} else if ("12".equals(type)) {// 上限优先
			if (upper == null) {
				return null;
			}
			if (null != fuhao(measureComment.getUpper())) {
				light = signLight(fuhao(measureComment.getUpper()), measureValue, upper);
			} else {
				if (target == null) {
					light = measureValue >= upper ? "green" : "red";
				} else {
					if (lower != null) {
						if (measureValue >= target && measureValue <= upper) {
							light = "green";
						} else if (measureValue < target && measureValue >= lower) {
							light = "yellow";
						} else {
							light = "red";
						}
					} else {
						light = (measureValue >= target && measureValue <= upper) ? "green" : "red";
					}
				}
			}
		} else {// 目标优先
			if (target == null) {
				return null;
			} else if (challenge != null) {
				// 有挑战值
				if (measureValue >= target * 0.95) {
					light = "green";
				} else {
					if (lower != null) {
						light = (measureValue < target && measureValue >= lower) ? "yellow" : "red";
					} else {
						light = "red";
					}
				} 
			} else if (null != fuhao(measureComment.getTarget())) {
				light = signLight(fuhao(measureComment.getTarget()), measureValue, target);
			} else {

				if (measureValue <= target * 1.05 && measureValue >= target * 0.95) {// green
					light = "green";
				} else {
					if (upper == null && lower != null) {
						light = measureValue >= lower ? "yellow" : "red";
					} else if (lower == null && upper != null) {
						light = measureValue <= upper ? "yellow" : "red";
					} else if (upper != null && lower != null) {
						light = (measureValue <= upper && measureValue >= lower) ? "yellow" : "red";
					} else {
						light = "red";
					}
				}
			}
		}
		return light;
	}

	private static String signLight(String sign, double measureValue, double compareValue) {
		String ligth = "";
		switch (sign) {
		case "=":
			ligth = measureValue == compareValue ? "green" : "red";
			break;
		case "≥":
			ligth = measureValue >= compareValue ? "green" : "red";
			break;
		case ">":
			ligth = measureValue > compareValue ? "green" : "red";
			break;
		case "≤":
			ligth = measureValue <= compareValue ? "green" : "red";
			break;
		case "<":
			ligth = measureValue < compareValue ? "green" : "red";
			break;
		}
		return ligth;
	}

	// 获得上下限目标值的符号
	private static String fuhao(String value) {
		if (value == null || "".equals(value) || "-".equals(value)) {
			return null;
		} else {
			String fuhao = null;
			int c1 = value.charAt(0);
			if (c1 < 48 || c1 > 57) {// 非数字
				fuhao = value.substring(0, 1);
				return fuhao;
			} else {
				return null;
			}
		}
	}

	// 处理hh:mm:ss值转换成数字
	public static Double dealHour(String value) {
		try {
			if (value == null || "".equals(value) || "-".equals(value)) {
				return null;
			} else {
				String[] values = value.split(":");
				BigDecimal num = new BigDecimal(60);

				BigDecimal m = new BigDecimal(values[1]);
				BigDecimal s = new BigDecimal(values[2]);
				BigDecimal second = m.multiply(num).add(s);
				BigDecimal minutue = second.divide(num, 2, RoundingMode.HALF_UP);
				BigDecimal h = minutue.divide(num, 2, RoundingMode.HALF_UP).add(new BigDecimal(values[0]));
				return h.doubleValue();
			}
		} catch (NumberFormatException e) {
			logger.error("数据转换异常：" + value);
			return null;
		}
	}

	// 处理上下限目标值
	public static Double deal(String value) {
		try {
			if (value == null || "".equals(value.trim()) || "-".equals(value)) {
				return null;
			} else {  
				int c1 = value.charAt(0);
				if (c1 < 48 || c1 > 57) {// 非数字
					value = value.substring(1);
				}
				if (value.indexOf("%") != -1) {
					value = value.substring(0, value.indexOf("%"));
				}
				BigDecimal b = new BigDecimal(value);
				return b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		} catch (NumberFormatException e) {
			logger.error("数据转换异常：" + value);
			return null;
		}
	}
}
