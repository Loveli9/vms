package com.icss.mvp.entity;

import java.util.Date;

public class ProjectParameterValue {
	private String no;
	private Date yearMonth;
	private Integer id;
	private String name;
	private String big_type_value;
	private String small_type_value;
	private String source;
	private String unit;
	private String isDisplay;
	private Double value;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Date getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(Date yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBig_type_value() {
		return big_type_value;
	}
	public void setBig_type_value(String big_type_value) {
		this.big_type_value = big_type_value;
	}
	public String getSmall_type_value() {
		return small_type_value;
	}
	public void setSmall_type_value(String small_type_value) {
		this.small_type_value = small_type_value;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}

}
