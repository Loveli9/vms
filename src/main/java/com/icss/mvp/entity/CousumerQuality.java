package com.icss.mvp.entity;

/**
 * 消费者业务线指标值 
 * @author ZR-133006
 *
 */
public class CousumerQuality {
	
	private int measureId;
	private String measureName;
	private String measureValue;
	private String upper;
	private String lower;
	private String target;
	
	
	public int getMeasureId() {
		return measureId;
	}
	public void setMeasureId(int measureId) {
		this.measureId = measureId;
	}
	public String getMeasureName() {
		return measureName;
	}
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	public String getMeasureValue() {
		return measureValue;
	}
	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

}
