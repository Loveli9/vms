package com.icss.mvp.entity;

public class SvnConfig {
	private String username;//账号
	private String password;//密码
	private String svnUrl;//svn http路径
	private String no;//项目编号
//	private String branch;//git分支
	private String startDate;//采集开始日期
	private String endDate;//采集结束日期 
	private String scope;//svn指定版本目录不进行统计
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSvnUrl() {
		return svnUrl;
	}
	public void setSvnUrl(String svnUrl) {
		this.svnUrl = svnUrl;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
