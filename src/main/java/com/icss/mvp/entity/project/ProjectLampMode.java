package com.icss.mvp.entity.project;

import com.icss.mvp.util.DateUtils;

public class ProjectLampMode {
	private String id;
	private String proNo; // 项目编号
	private int lampMode; // 点灯模式
	private String field; // 点灯字段
	private String statisticalTime; // 统计时间
	private String autoValue;// 自动值
	private String manualValue;// 手动值

	private String value;

	public String getValue() {
		if (lampMode == 0) {
			return  autoValue;
		} else if (lampMode == 1) {
			return  manualValue;
		} else {
			return value;
		}
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}

	public int getLampMode() {
		return lampMode;
	}

	public void setLampMode(int lampMode) {
		this.lampMode = lampMode;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getStatisticalTime() {	
		return statisticalTime;
	}

	public void setStatisticalTime(String statisticalTime) {
		this.statisticalTime = statisticalTime;
	}

	public String getAutoValue() {
		return autoValue;
	}

	public void setAutoValue(String autoValue) {
		this.autoValue = autoValue;
	}

	public String getManualValue() {
		return manualValue;
	}

	public void setManualValue(String manualValue) {
		this.manualValue = manualValue;
	}
	
}
