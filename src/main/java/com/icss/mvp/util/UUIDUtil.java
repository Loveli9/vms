package com.icss.mvp.util;

import java.util.UUID;

public class UUIDUtil {
	/**
	 * 
	 * <pre>
	 * <b>描述：获取一个新的UUID值</b>
	 * <b>任务编号：</b>
	 * <b>作者：张鹏飞</b> 
	 * <b>创建日期： 2017年5月15日 下午4:49:26</b>
	 *  &#64;return
	 * </pre>
	 */
	public static String getNew() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void main(String[] args) {
		System.out.println(UUIDUtil.getNew());
	}
}