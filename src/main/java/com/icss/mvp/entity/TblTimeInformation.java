package com.icss.mvp.entity;

import java.util.Date;

public class TblTimeInformation {
	
	private String zrAccount;
	private String name;
	private String standardParticipation;
	private String actualParticipation;
	private String standardLaborHour;
	private String actualLaborHour;
	private Date statisticalTime;
	public String getZrAccount() {
		return zrAccount;
	}
	public void setZrAccount(String zrAccount) {
		this.zrAccount = zrAccount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStandardParticipation() {
		return standardParticipation;
	}
	public void setStandardParticipation(String standardParticipation) {
		this.standardParticipation = standardParticipation;
	}
	public String getActualParticipation() {
		return actualParticipation;
	}
	public void setActualParticipation(String actualParticipation) {
		this.actualParticipation = actualParticipation;
	}
	public String getStandardLaborHour() {
		return standardLaborHour;
	}
	public void setStandardLaborHour(String standardLaborHour) {
		this.standardLaborHour = standardLaborHour;
	}
	public String getActualLaborHour() {
		return actualLaborHour;
	}
	public void setActualLaborHour(String actualLaborHour) {
		this.actualLaborHour = actualLaborHour;
	}
	public Date getStatisticalTime() {
		return statisticalTime;
	}
	public void setStatisticalTime(Date statisticalTime) {
		this.statisticalTime = statisticalTime;
	}
	
	
}
