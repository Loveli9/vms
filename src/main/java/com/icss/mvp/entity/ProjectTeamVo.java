package com.icss.mvp.entity;

public class ProjectTeamVo {
	private int	id;
	private String teamName;
	private String teamId;
//	private String teamNo;
	private String tm;
	private String tmId;
	private int teamSize;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
//	public String getTeamNo() {
//		return teamNo;
//	}
//	public void setTeamNo(String teamNo) {
//		this.teamNo = teamNo;
//	}
	public String getTm() {
		return tm;
	}
	public void setTm(String tm) {
		this.tm = tm;
	}
	public String getTmId() {
		return tmId;
	}
	public void setTmId(String tmId) {
		this.tmId = tmId;
	}
	public int getTeamSize() {
		return teamSize;
	}
	public void setTeamSize(int teamSize) {
		this.teamSize = teamSize;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}