package com.icss.mvp.entity;

import java.util.Date;

public class ProjectDetailInfo {
	private String no;

	private String name;

	private String hwpdu;
	private String hwpduId;

	private String hwzpdu;
	private String hwzpduId;

	private String pduSpdt;
	private String pduSpdtId;

	private String bu;
	private String buId;

	private String pdu;
	private String pduId;

	private String du;
	private String duId;

	private String area;
	private String areaId;
	private String pm;
	private String pmId;

	private String type;

	private String projectSynopsis;

	private Date startDate;

	private Date endDate;

	private Double predictMoney;

	private Double workerCount;

	private String projectType;
	private String projectState;
	private String coopType;
	private String isOffshore;
	private String po;
	private Double countOfDay;
	private Double countOfMonth;

	private String version;
	
	private int teamId;
	private String teamName;

	private String qaName;
	private String qaId;

	public String getHwpduId() {
		return hwpduId;
	}

	public void setHwpduId(String hwpduId) {
		this.hwpduId = hwpduId;
	}

	public String getHwzpduId() {
		return hwzpduId;
	}

	public void setHwzpduId(String hwzpduId) {
		this.hwzpduId = hwzpduId;
	}

	public String getPduSpdtId() {
		return pduSpdtId;
	}

	public void setPduSpdtId(String pduSpdtId) {
		this.pduSpdtId = pduSpdtId;
	}

	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	public String getPduId() {
		return pduId;
	}

	public void setPduId(String pduId) {
		this.pduId = pduId;
	}

	public String getDuId() {
		return duId;
	}

	public void setDuId(String duId) {
		this.duId = duId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getPmId() {
		return pmId;
	}

	public void setPmId(String pmId) {
		this.pmId = pmId;
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

	public String getProjectSynopsis() {
		return projectSynopsis;
	}

	public void setProjectSynopsis(String projectSynopsis) {
		this.projectSynopsis = projectSynopsis;
	}

	public String getProjectState() {
		return projectState;
	}

	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

	public String getCoopType() {
		return coopType;
	}

	public void setCoopType(String coopType) {
		this.coopType = coopType;
	}

	public String getIsOffshore() {
		return isOffshore;
	}

	public void setIsOffshore(String isOffshore) {
		this.isOffshore = isOffshore;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public Double getCountOfDay() {
		return countOfDay;
	}

	public void setCountOfDay(Double countOfDay) {
		this.countOfDay = countOfDay;
	}

	public Double getCountOfMonth() {
		return countOfMonth;
	}

	public void setCountOfMonth(Double countOfMonth) {
		this.countOfMonth = countOfMonth;
	}

	private Date importDate;

	private String importUser;

	public void setNo(String no) {
		this.no = no;
	}

	public String getNo() {
		return no;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getBu() {
		return bu;
	}

	public void setPdu(String pdu) {
		this.pdu = pdu;
	}

	public String getPdu() {
		return pdu;
	}

	public void setDu(String du) {
		this.du = du;
	}

	public String getDu() {
		return du;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getArea() {
		return area;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setPredictMoney(Double predictMoney) {
		this.predictMoney = predictMoney;
	}

	public Double getPredictMoney() {
		return predictMoney;
	}

	public void setWorkerCount(Double workerCount) {
		this.workerCount = workerCount;
	}

	public Double getWorkerCount() {
		return workerCount;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportUser(String importUser) {
		this.importUser = importUser;
	}

	public String getImportUser() {
		return importUser;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamCode) {
		this.teamId = teamCode;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getQaName() {
		return qaName;
	}

	public void setQaName(String qaName) {
		this.qaName = qaName;
	}

	public String getQaId() {
		return qaId;
	}

	public void setQaId(String qaId) {
		this.qaId = qaId;
	}

}
