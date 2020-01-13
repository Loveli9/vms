package com.icss.mvp.entity;

import java.util.Date;

public class ProjectMembersLocal {
	private String no;
	private String userid;
	private String pmid;
	private String name;
	private String zrAccount;
	private String hwAccount;
	private String reprname;
	private String tmssAccount;
	private String svnGitNo;
	private String role;
	private String isKeyStaffs;
	private Date startDate;
	private String startDatestr;
	private Date endDate;
	private String endDatestr;
	private String status;
	private String rank;

	public String getStartDatestr() {
		return startDatestr;
	}

	public void setStartDatestr(String startDatestr) {
		this.startDatestr = startDatestr;
	}

	public String getEndDatestr() {
		return endDatestr;
	}

	public void setEndDatestr(String endDatestr) {
		this.endDatestr = endDatestr;
	}

	public String getReprname() {
		return reprname;
	}

	public void setReprname(String reprname) {
		this.reprname = reprname;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZrAccount() {
		return zrAccount;
	}
	public void setZrAccount(String zrAccount) {
		this.zrAccount = zrAccount;
	}
	public String getHwAccount() {
		return hwAccount;
	}
	public void setHwAccount(String hwAccount) {
		this.hwAccount = hwAccount;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getIsKeyStaffs() {
		return isKeyStaffs;
	}
	public void setIsKeyStaffs(String isKeyStaffs) {
		this.isKeyStaffs = isKeyStaffs;
	}
	public String getTmssAccount() {
		return tmssAccount;
	}
	public void setTmssAccount(String tmssAccount) {
		this.tmssAccount = tmssAccount;
	}
	public String getSvnGitNo() {
		return svnGitNo;
	}
	public void setSvnGitNo(String svnGitNo) {
		this.svnGitNo = svnGitNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	
}
