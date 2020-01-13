package com.icss.mvp.entity;

import java.util.Date;

/**
 * 项目月指标实体
 * @author Administrator
 *
 */
public class MonthMeasure {
	private Integer id;
	private String labMeasureConfigId;
	private String value;
	private String copyDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getLabMeasureConfigId() {
		return labMeasureConfigId;
	}
	public void setLabMeasureConfigId(String labMeasureConfigId) {
		this.labMeasureConfigId = labMeasureConfigId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCopyDate() {
		return copyDate;
	}
	public void setCopyDate(String copyDate) {
		this.copyDate = copyDate;
	}
	
}
