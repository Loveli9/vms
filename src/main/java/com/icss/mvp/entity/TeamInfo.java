package com.icss.mvp.entity;

import java.util.Date;

public class TeamInfo {
	private String no;

	private String name;

	private String tm;
	
	private String tmid;
	
	private String area;
	
	private String clientType;
	private String hwpdu;

	private String hwzpdu;
	
	private String pduSpdt;
	
	private String bu;
	
	private String pdu;
	
	private String du;

	// 合作类型
	private String coopType;

	// 团队说明
	private String desc;

	// 操作时间
	private Date operateTime;

	// 操作人员
	private String operateUser;


	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
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

	public String getTm() {
		return tm;
	}

	public void setTm(String tm) {
		this.tm = tm;
	}

	public String getTmid() {
		return tmid;
	}

	public void setTmid(String tmid) {
		this.tmid = tmid;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getHwpdu() {
		return hwpdu;
	}

	public void setHwpdu(String hwpdu) {
		this.hwpdu = hwpdu;
	}

	public String getHwzpdu() {
		return hwzpdu;
	}

	public void setHwzpdu(String hwzpdu) {
		this.hwzpdu = hwzpdu;
	}

	public String getPduSpdt() {
		return pduSpdt;
	}

	public void setPduSpdt(String pduSpdt) {
		this.pduSpdt = pduSpdt;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getPdu() {
		return pdu;
	}

	public void setPdu(String pdu) {
		this.pdu = pdu;
	}

	public String getDu() {
		return du;
	}

	public void setDu(String du) {
		this.du = du;
	}

	public String getCoopType() {
		return coopType;
	}

	public void setCoopType(String coopType) {
		this.coopType = coopType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateUser() {
		return operateUser;
	}

	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}
}
