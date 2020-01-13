package com.icss.mvp.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReportQualityEntity{
	private String proNo;
	private String name; 
	private String hwzpdu;
	private String pduSpdt;
	private String pdu;
	private String du;
	private String pm;
	private Date startDate;
	//收藏
	private String collection; 
	//类型
	private String projectType;
	//指标值
	@JsonIgnore
    private List<MeasureVo> measureVoList = new ArrayList<>();
	public String getProNo() {
		return proNo;
	}
	public void setProNo(String proNo) {
		this.proNo = proNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHwzpdu() {
		return hwzpdu;
	}
	public void setHwzpdu(String hwzpdu) {
		this.hwzpdu = hwzpdu;
	}
	public String getPduSpdt() {
		return pduSpdt;
	}
	public void setPduSpdt(String pduSpdt) {
		this.pduSpdt = pduSpdt;
	}
	public String getPdu() {
		return pdu;
	}
	public void setPdu(String pdu) {
		this.pdu = pdu;
	}
	public String getDu() {
		return du;
	}
	public void setDu(String du) {
		this.du = du;
	}
	public String getPm() {
		return pm;
	}
	public void setPm(String pm) {
		this.pm = pm;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public List<MeasureVo> getMeasureVoList() {
		return measureVoList;
	}
	public void setMeasureVoList(List<MeasureVo> measureVoList) {
		this.measureVoList = measureVoList;
	}
	
}
