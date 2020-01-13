/**
 * 
 */
package com.icss.mvp.entity;

/**
 * 项目指标月度TOP
 * 
 * @author 陈丽佳
 *
 */
public class MonthTopMeasure {
	private Integer id;
	
	private Long measureId;
	
	private Integer count;
	
	private String copyDate;
	
	private String name;
	
	private String relatedProjectNo;
	
//	private String area;
//
//	private String hwpdu;
//
//	private String hwzpdu;
//
//	private String pduSpdt;
//
//	private String bu;
//
//	private String pdu;
//
//	private String du;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getMeasureId() {
		return measureId;
	}

	public void setMeasureId(Long measureId) {
		this.measureId = measureId;
	}

	public String getRelatedProjectNo() {
		return relatedProjectNo;
	}

	public void setRelatedProjectNo(String relatedProjectNo) {
		this.relatedProjectNo = relatedProjectNo;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getCopyDate() {
		return copyDate;
	}

	public void setCopyDate(String copyDate) {
		this.copyDate = copyDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
