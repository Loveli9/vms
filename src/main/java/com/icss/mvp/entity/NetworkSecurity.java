package com.icss.mvp.entity;

import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 *
 */
public class NetworkSecurity extends ProjectInfo {
	private Integer id;
	private String surveyDate;
	private String proNo;
	private Integer deliverStatus;
	private Integer ukStatus;
	private Integer sowStatus;
	private Integer securityStatus;
	private String securityLiable;
	private String satisfyRate;
	private Integer caseStatus;
	private String caseRate;
	private String dangerReport;
	private Integer dangerStatus;
	private Integer errorCleared;
	private Integer warningCleared;
	private String department;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSurveyDate() {
		return surveyDate;
	}
	public void setSurveyDate(String surveyDate) {
		this.surveyDate = surveyDate;
	}
	public String getProNo() {
		return proNo;
	}
	public void setProNo(String proNo) {
		this.proNo = proNo;
	}
	public Integer getDeliverStatus() {
		return deliverStatus;
	}
	public void setDeliverStatus(Integer deliverStatus) {
		this.deliverStatus = deliverStatus;
	}
	public Integer getUkStatus() {
		return ukStatus;
	}
	public void setUkStatus(Integer ukStatus) {
		this.ukStatus = ukStatus;
	}
	public Integer getSowStatus() {
		return sowStatus;
	}
	public void setSowStatus(Integer sowStatus) {
		this.sowStatus = sowStatus;
	}
	public Integer getSecurityStatus() {
		return securityStatus;
	}
	public void setSecurityStatus(Integer securityStatus) {
		this.securityStatus = securityStatus;
	}
	public String getSecurityLiable() {
		return securityLiable;
	}
	public void setSecurityLiable(String securityLiable) {
		this.securityLiable = securityLiable;
	}
	public String getSatisfyRate() {
		return satisfyRate;
	}
	public void setSatisfyRate(String satisfyRate) {
		this.satisfyRate = satisfyRate;
	}
	public String getCaseRate() {
		return caseRate;
	}
	public void setCaseRate(String caseRate) {
		this.caseRate = caseRate;
	}
	public String getDangerReport() {
		return dangerReport;
	}
	public void setDangerReport(String dangerReport) {
		this.dangerReport = dangerReport;
	}
	public Integer getDangerStatus() {
		return dangerStatus;
	}
	public void setDangerStatus(Integer dangerStatus) {
		this.dangerStatus = dangerStatus;
	}
	public Integer getErrorCleared() {
		return errorCleared;
	}
	public void setErrorCleared(Integer errorCleared) {
		this.errorCleared = errorCleared;
	}
	public Integer getWarningCleared() {
		return warningCleared;
	}
	public void setWarningCleared(Integer warningCleared) {
		this.warningCleared = warningCleared;
	}
	public Integer getCaseStatus() {
		return caseStatus;
	}
	public void setCaseStatus(Integer caseStatus) {
		this.caseStatus = caseStatus;
	}
	public String getDepartment() {
		if (StringUtils.isNotBlank(getHwzpdu())) {
			department = getHwzpdu() + "/" + getPduSpdt();
		} else {
			department = getPdu() + "/" + getDu();
		}	
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Override
	public String getPm() {
		if(StringUtils.isNotBlank(super.getPm())) {
			return super.getPm().replaceAll("[^\u4e00-\u9fa5]", "");
		}else {
			return super.getPm();
		}
		
	}
	
}
