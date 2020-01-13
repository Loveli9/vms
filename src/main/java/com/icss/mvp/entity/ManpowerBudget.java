package com.icss.mvp.entity;

public class ManpowerBudget {
	private Integer id;
	private String userid;
	private String pmid;
	private String proNo;
	private Integer keyRoleCount;
	private Integer headcount;
	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo == null ? null : proNo.trim();
	}

	public Integer getHeadcount() {
		return headcount;
	}

	public void setHeadcount(Integer headcount) {
		this.headcount = headcount;
	}

	public Integer getKeyRoleCount() {
		return keyRoleCount;
	}

	public void setKeyRoleCount(Integer keyRoleCount) {
		this.keyRoleCount = keyRoleCount;
	}
}