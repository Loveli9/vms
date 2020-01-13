package com.icss.mvp.entity;

import java.util.List;

public class TeamLabelMeasure {
	private String labelCategory; // 流程类目 
	private Integer id; // 流程ID
	private String labelName; // 流程名称
	private String labelOrder;//流程顺序
	private String isSelect;//该项目是否选择此流程0-未选择，1-选择
	private List<TeamShowMeasureCat> measureCatList;//该流程下的所有指标类目及指标
	public String getLabelCategory() {
		return labelCategory;
	}
	public void setLabelCategory(String labelCategory) {
		this.labelCategory = labelCategory;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getLabelOrder() {
		return labelOrder;
	}
	public void setLabelOrder(String labelOrder) {
		this.labelOrder = labelOrder;
	}
	public String getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}
	public List<TeamShowMeasureCat> getMeasureCatList() {
		return measureCatList;
	}
	public void setMeasureCatList(List<TeamShowMeasureCat> measureCatList) {
		this.measureCatList = measureCatList;
	}

}
