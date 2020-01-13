package com.icss.mvp.entity;

public class ProjectStatus extends ProjectInfo {
	
	private String id;
	
	private String businessUnit;
	
	private String department;
	
	private String businessId;
	
	private String causeId;
	
	private String deliverId;
	
	private Integer statusReview;
	
	private String review;

	private double progressLamp;
	
	private double resourcesLamp;
	
	private double qualityLamp;
	
	private double statusAssessment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCauseId() {
		return causeId;
	}

	public void setCauseId(String causeId) {
		this.causeId = causeId;
	}

	public String getDeliverId() {
		return deliverId;
	}

	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}

	public Integer getStatusReview() {
		return statusReview;
	}

	public void setStatusReview(Integer statusReview) {
		this.statusReview = statusReview;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public double getProgressLamp() {
		return progressLamp;
	}

	public void setProgressLamp(double progressLamp) {
		this.progressLamp = progressLamp;
	}

	public double getResourcesLamp() {
		return resourcesLamp;
	}

	public void setResourcesLamp(double resourcesLamp) {
		this.resourcesLamp = resourcesLamp;
	}

	public double getQualityLamp() {
		return qualityLamp;
	}

	public void setQualityLamp(double qualityLamp) {
		this.qualityLamp = qualityLamp;
	}

	public double getStatusAssessment() {
		return statusAssessment;
	}

	public void setStatusAssessment(double statusAssessment) {
		this.statusAssessment = statusAssessment;
	}
}
