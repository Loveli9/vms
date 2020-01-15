package com.icss.mvp.entity;

public class PmdataTotal {
	private Integer id;
	private String userid;
	private String pmid;
	private String proNo;
	private Integer rowsRoleCount;
	private Integer rowsMembercount;
	public Integer getRowsRoleCount() {
		return rowsRoleCount;
	}

	public void setRowsRoleCount(Integer rowsRoleCount) {
		this.rowsRoleCount = rowsRoleCount;
	}

	public Integer getRowsMembercount() {
		return rowsMembercount;
	}

	public void setRowsMembercount(Integer rowsMembercount) {
		this.rowsMembercount = rowsMembercount;
	}
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

}