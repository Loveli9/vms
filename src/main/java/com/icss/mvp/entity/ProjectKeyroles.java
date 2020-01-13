package com.icss.mvp.entity;

import java.io.Serializable;

public class ProjectKeyroles implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -3611021770114047777L;
	private String no;//项目编号
	private String reproject; //角色待入/所属项目名称
	private String userid;//登录用户id
	private String pmid ;//项目经理id
    private String name;//姓名
    private String zrAccount;//中软工号
    private String hwAccount;//华为工号
    private String position;//职位
    private String role;//角色
    private String rdpmExam;//RDPM考试（通过/不通过）
    private String replyResults;//答辩结果（通过/不通过）
    private String proCompetence;//胜任度
    private String status;//状态（在职/储备/离职）
    private String author;//华为工号--关联查询得到
    private String superior;//上级领导
    private String startDate;
    private String endDate;
    private String rank;

	public String getReproject() {
		return reproject;
	}

	public void setReproject(String reproject) {
		this.reproject = reproject;
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

	public String getSuperior() {
		return superior;
	}
	public void setSuperior(String superior) {
		this.superior = superior;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRdpmExam() {
		return rdpmExam;
	}
	public void setRdpmExam(String rdpmExam) {
		this.rdpmExam = rdpmExam;
	}
	public String getReplyResults() {
		return replyResults;
	}
	public void setReplyResults(String replyResults) {
		this.replyResults = replyResults;
	}
	public String getProCompetence() {
		return proCompetence;
	}
	public void setProCompetence(String proCompetence) {
		this.proCompetence = proCompetence;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
    
}
