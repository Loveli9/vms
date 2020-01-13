package com.icss.mvp.entity;

import java.util.List;

public class ShowMeasureCat {
    //类目
	private String labelId;//流程ID
    private String measureCategory;
	private String isSelect;//该流程是否选中此指标
	private String projectLabId;//项目流程配置id
	private List<ShowMeasure> measureList;//该类目下的指标
	
	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public List<ShowMeasure> getMeasureList() {
		return measureList;
	}

	public void setMeasureList(List<ShowMeasure> measureList) {
		this.measureList = measureList;
	}

	public String getMeasureCategory() {
		return measureCategory;
	}

	public void setMeasureCategory(String measureCategory) {
		this.measureCategory = measureCategory;
	}


	public String getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}

	public String getProjectLabId() {
		return projectLabId;
	}

	public void setProjectLabId(String projectLabId) {
		this.projectLabId = projectLabId;
	}
}
