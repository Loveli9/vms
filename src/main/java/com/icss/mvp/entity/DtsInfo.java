package com.icss.mvp.entity;

public class DtsInfo {
	//项目ID
	private String projNo;
	//阶段
	private String workflowStatus;
	//版本
	private String bVersion;
	//状态
	private String curentStatus;
	/**维度
	1、问题单（特性）  --feature
	2、问题单（子系统）--subsystem
	3、问题单（模块）--module
	4、问题单（责任人）--current_Handler
	5、问题单（项目组）--v_Version   **/
	private String dimensionality;
	
	
	public String getDimensionality() {
		return dimensionality;
	}
	public void setDimensionality(String dimensionality) {
		this.dimensionality = dimensionality;
	}
	public String getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public String getProjNo() {
		return projNo;
	}
	public void setProjNo(String projNo) {
		this.projNo = projNo;
	}
	public String getCurentStatus() {
		return curentStatus;
	}
	public void setCurentStatus(String curentStatus) {
		this.curentStatus = curentStatus;
	}
	public String getbVersion() {
		return bVersion;
	}
	public void setbVersion(String bVersion) {
		this.bVersion = bVersion;
	}
	
}
