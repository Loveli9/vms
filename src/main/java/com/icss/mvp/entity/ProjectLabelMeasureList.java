package com.icss.mvp.entity;

import java.util.List;

/**
 * 流程指标配置信息封装类入参
 * @author chengchenhui
 *
 */
public class ProjectLabelMeasureList {
	private String proNo; //项目编号
	private List<ProjectLabelMeasure> projectLabelMeasures;//该流程指标all
	public String getProNo() {
		return proNo;
	}
	public void setProNo(String proNo) {
		this.proNo = proNo;
	}
	public List<ProjectLabelMeasure> getProjectLabelMeasures() {
		return projectLabelMeasures;
	}
	public void setProjectLabelMeasures(List<ProjectLabelMeasure> projectLabelMeasures) {
		this.projectLabelMeasures = projectLabelMeasures;
	}
	
	
}