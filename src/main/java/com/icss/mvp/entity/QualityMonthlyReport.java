package com.icss.mvp.entity;

import com.icss.mvp.entity.common.CommonEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class QualityMonthlyReport extends CommonEntity {
	private String hwzpdu;
	private String pduSpdt;
	private String du;
	private String pdu;
	private String area;
	private String projectState;
	private Date startDate;
	private Date endDate;
	private String pm;
	private String qa;
	private String projectType;
	private String department;
	private String period;
	private String mileagePoint;
	private Long overdueDate;
	private int statisticalCycle;//统计周期
	
	private int assess;//状态评估分值
	private String statusAssessment;//状态评估
	private String comment;//点评
	private List<String> fiveQualityLamp;//近5次质量点灯
	private List<String> fiveKeyRoles;//近5次资源点灯
	private List<String> fivePlanLamp;//近5次进度点灯
	private List<String> fiveScopeLamp;//近5次变更点灯
	private List<String> fiveStatusAssessment;//近5次状态评估
	private List<String> fiveComment;//近5次点评
	private List<Map<String, Object>> targetList;
	
	private int assessment;
	private int aar;
	private int audit;
	private int quality;
	
	private int open361;
	private int closed361;
	private int delay361;
	private int added361;
	private int sum361;
	private double degree361;
	
	private int openAAR;
	private int closedAAR;
	private int delayAAR;
	private int addedAAR;
	private int sumAAR;
	private double degreeAAR;
	//否
	private int openAudit;
	private int closedAudit;
	private int delayAudit;
	private int addedAudit;
	private int sumAudit;
	private double degreeAudit;
	//是
	private int openQuality;
	private int closedQuality;
	private int delayQuality;
	private int addedQuality;
	private int sumQuality;
	private double degreeQuality;
	private int is361;
	
	private String name;
	private String no;
	private String peopleLamp;// 人员点灯
	private String planLamp;// 计划进度点灯
	private String scopeLamp;// 需求范围点灯
	private String qualityLamp;// 质量目标点灯
	private String keyRoles;// 关键角色匹配
	private String keyRolesPass;// 关键角色答辩通过率
	private int notPassedNumber; //关键角色待答辩
	private int peopleDifferNumber; //人力缺口
	private int rolesDifferNumber; //关键角色缺口
	private int redLightNumber; //红灯个数
	private int yellowLightNumber; //黄灯个数
	private int greenLightNumber; //绿灯个数
	private int demandTotal; //总需求个数
	private int demandChangeNumber; //需求变更个数
	private String developmentProgress; //开发进度
	private String demandProgress; //需求进度
	private int peopleLampTotal; //总人力诉求数
	private int keyRolesTotal; //关键角色总人数
	private int passNumber; //通过数
	private String creationDate;//质量月报创建时间

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public int getOpen361() {
		return open361;
	}

	public void setOpen361(int open361) {
		this.open361 = open361;
	}

	public int getClosed361() {
		return closed361;
	}

	public void setClosed361(int closed361) {
		this.closed361 = closed361;
	}

	public int getDelay361() {
		return delay361;
	}

	public void setDelay361(int delay361) {
		this.delay361 = delay361;
	}

	public int getAdded361() {
		return added361;
	}

	public void setAdded361(int added361) {
		this.added361 = added361;
	}

	public int getSum361() {
		return sum361;
	}

	public void setSum361(int sum361) {
		this.sum361 = sum361;
	}

	public double getDegree361() {
		return degree361;
	}

	public void setDegree361(int sum361,int closed361,double degree361) {
		if(sum361 == 0) {
			degree361 = 0;
	    }else {
	    	degree361 = (double)closed361 * 100 / (double)(sum361);
	    }
		this.degree361 = degree361;
	}

	public int getOpenAAR() {
		return openAAR;
	}

	public void setOpenAAR(int openAAR) {
		this.openAAR = openAAR;
	}

	public int getClosedAAR() {
		return closedAAR;
	}

	public void setClosedAAR(int closedAAR) {
		this.closedAAR = closedAAR;
	}

	public int getDelayAAR() {
		return delayAAR;
	}

	public void setDelayAAR(int delayAAR) {
		this.delayAAR = delayAAR;
	}

	public int getAddedAAR() {
		return addedAAR;
	}

	public void setAddedAAR(int addedAAR) {
		this.addedAAR = addedAAR;
	}

	public int getSumAAR() {
		return sumAAR;
	}

	public void setSumAAR(int sumAAR) {
		this.sumAAR = sumAAR;
	}

	public double getDegreeAAR() {
		return degreeAAR;
	}

	public void setDegreeAAR(int sumAAR,int closedAAR,double degreeAAR) {
		if(sumAAR == 0) {
			degreeAAR = 0;
	    }else {
	    	degreeAAR = (double)closedAAR * 100 / (double)(sumAAR);
	    }	
		this.degreeAAR = degreeAAR;
	}

	public int getOpenAudit() {
		return openAudit;
	}

	public void setOpenAudit(int openAudit) {
		this.openAudit = openAudit;
	}

	public int getClosedAudit() {
		return closedAudit;
	}

	public void setClosedAudit(int closedAudit) {
		this.closedAudit = closedAudit;
	}

	public int getDelayAudit() {
		return delayAudit;
	}

	public void setDelayAudit(int delayAudit) {
		this.delayAudit = delayAudit;
	}

	public int getAddedAudit() {
		return addedAudit;
	}

	public void setAddedAudit(int addedAudit) {
		this.addedAudit = addedAudit;
	}

	public int getSumAudit() {
		return sumAudit;
	}

	public void setSumAudit(int sumAudit) {
		this.sumAudit = sumAudit;
	}

	public double getDegreeAudit() {
		return degreeAudit;
	}

	public void setDegreeAudit(int sumAudit,int closedAudit,double degreeAudit) {
		if(sumAudit == 0) {
			degreeAudit = 0;
	    }else {
	    	degreeAudit = (double)closedAudit * 100 / (double)(sumAudit);
	    }	
		this.degreeAudit = degreeAudit;
	}

	public int getOpenQuality() {
		return openQuality;
	}

	public void setOpenQuality(int openQuality) {
		this.openQuality = openQuality;
	}

	public int getClosedQuality() {
		return closedQuality;
	}

	public void setClosedQuality(int closedQuality) {
		this.closedQuality = closedQuality;
	}

	public int getDelayQuality() {
		return delayQuality;
	}

	public void setDelayQuality(int delayQuality) {
		this.delayQuality = delayQuality;
	}

	public int getAddedQuality() {
		return addedQuality;
	}

	public void setAddedQuality(int addedQuality) {
		this.addedQuality = addedQuality;
	}

	public int getSumQuality() {
		return sumQuality;
	}

	public void setSumQuality(int sumQuality) {
		this.sumQuality = sumQuality;
	}

	public double getDegreeQuality() {
		return degreeQuality;
	}

	public void setDegreeQuality(int sumQuality,int closedQuality,double degreeQuality) {
		if(sumQuality == 0) {
			degreeQuality = 0;
	    }else {
	    	degreeQuality = (double)closedQuality * 100 / (double)(sumQuality);
	    }	
		this.degreeQuality = degreeQuality;
	}

	public int getIs361() {
		return is361;
	}

	public void setIs361(int is361) {
		this.is361 = is361;
	}

	public String getHwzpdu() {
		return hwzpdu;
	}

	public void setHwzpdu(String hwzpdu) {
		this.hwzpdu = hwzpdu;
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
	public String getPduSpdt() {
		return pduSpdt;
	}
	public void setPduSpdt(String pduSpdt) {
		this.pduSpdt = pduSpdt;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public String getPm() {
		return pm;
	}
	public void setPm(String pm) {
		this.pm = pm;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getPeopleLamp() {
		return peopleLamp;
	}
	public void setPeopleLamp(String peopleLamp) {
		this.peopleLamp = peopleLamp;
	}
	public String getPlanLamp() {
		return planLamp;
	}
	public void setPlanLamp(String planLamp) {
		this.planLamp = planLamp;
	}
	public String getScopeLamp() {
		return scopeLamp;
	}
	public void setScopeLamp(String scopeLamp) {
		this.scopeLamp = scopeLamp;
	}
	public String getQualityLamp() {
		return qualityLamp;
	}
	public void setQualityLamp(String qualityLamp) {
		this.qualityLamp = qualityLamp;
	}
	public String getKeyRoles() {
		return keyRoles;
	}
	public void setKeyRoles(String keyRoles) {
		this.keyRoles = keyRoles;
	}
	public String getKeyRolesPass() {
		return keyRolesPass;
	}
	public void setKeyRolesPass(String keyRolesPass) {
		this.keyRolesPass = keyRolesPass;
	}
	public int getNotPassedNumber() {
		return notPassedNumber;
	}
	public void setNotPassedNumber(int notPassedNumber) {
		this.notPassedNumber = notPassedNumber;
	}
	public int getPeopleDifferNumber() {
		return peopleDifferNumber;
	}
	public void setPeopleDifferNumber(int peopleDifferNumber) {
		this.peopleDifferNumber = peopleDifferNumber;
	}
	public int getRolesDifferNumber() {
		return rolesDifferNumber;
	}
	public void setRolesDifferNumber(int rolesDifferNumber) {
		this.rolesDifferNumber = rolesDifferNumber;
	}
	public int getRedLightNumber() {
		return redLightNumber;
	}
	public void setRedLightNumber(int redLightNumber) {
		this.redLightNumber = redLightNumber;
	}
	public int getYellowLightNumber() {
		return yellowLightNumber;
	}
	public void setYellowLightNumber(int yellowLightNumber) {
		this.yellowLightNumber = yellowLightNumber;
	}
	public int getGreenLightNumber() {
		return greenLightNumber;
	}
	public void setGreenLightNumber(int greenLightNumber) {
		this.greenLightNumber = greenLightNumber;
	}
	public int getDemandTotal() {
		return demandTotal;
	}
	public void setDemandTotal(int demandTotal) {
		this.demandTotal = demandTotal;
	}
	public int getDemandChangeNumber() {
		return demandChangeNumber;
	}
	public void setDemandChangeNumber(int demandChangeNumber) {
		this.demandChangeNumber = demandChangeNumber;
	}
	
	public String getDevelopmentProgress() {
		return developmentProgress;
	}
	public void setDevelopmentProgress(String developmentProgress) {
		this.developmentProgress = developmentProgress;
	}
	public String getDemandProgress() {
		return demandProgress;
	}
	public void setDemandProgress(String demandProgress) {
		this.demandProgress = demandProgress;
	}
	public int getPeopleLampTotal() {
		return peopleLampTotal;
	}
	public void setPeopleLampTotal(int peopleLampTotal) {
		this.peopleLampTotal = peopleLampTotal;
	}
	public int getKeyRolesTotal() {
		return keyRolesTotal;
	}
	public void setKeyRolesTotal(int keyRolesTotal) {
		this.keyRolesTotal = keyRolesTotal;
	}
	public int getPassNumber() {
		return passNumber;
	}
	public void setPassNumber(int passNumber) {
		this.passNumber = passNumber;
	}

	public int getAssessment() {
		return assessment;
	}

	public void setAssessment(int assessment) {
		this.assessment = assessment;
	}

	public int getAar() {
		return aar;
	}

	public void setAar(int aar) {
		this.aar = aar;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getMileagePoint() {
		return mileagePoint;
	}

	public void setMileagePoint(String mileagePoint) {
		this.mileagePoint = mileagePoint;
	}

	public int getAssess() {
		return assess;
	}

	public void setAssess(int assess) {
		this.assess = assess;
	}

	public String getStatusAssessment() {
		return statusAssessment;
	}

	public void setStatusAssessment(String statusAssessment) {
		this.statusAssessment = statusAssessment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<String> getFiveStatusAssessment() {
		return fiveStatusAssessment;
	}

	public void setFiveStatusAssessment(List<String> fiveStatusAssessment) {
		this.fiveStatusAssessment = fiveStatusAssessment;
	}

	public List<String> getFiveComment() {
		return fiveComment;
	}

	public void setFiveComment(List<String> fiveComment) {
		this.fiveComment = fiveComment;
	}

	public List<String> getFiveQualityLamp() {
		return fiveQualityLamp;
	}

	public void setFiveQualityLamp(List<String> fiveQualityLamp) {
		this.fiveQualityLamp = fiveQualityLamp;
	}

	public List<String> getFiveKeyRoles() {
		return fiveKeyRoles;
	}

	public void setFiveKeyRoles(List<String> fiveKeyRoles) {
		this.fiveKeyRoles = fiveKeyRoles;
	}

	public List<String> getFivePlanLamp() {
		return fivePlanLamp;
	}

	public void setFivePlanLamp(List<String> fivePlanLamp) {
		this.fivePlanLamp = fivePlanLamp;
	}

	public List<String> getFiveScopeLamp() {
		return fiveScopeLamp;
	}

	public void setFiveScopeLamp(List<String> fiveScopeLamp) {
		this.fiveScopeLamp = fiveScopeLamp;
	}

	public String getQa() {
		return qa;
	}

	public void setQa(String qa) {
		this.qa = qa;
	}

	public Long getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(Long overdueDate) {
		this.overdueDate = overdueDate;
	}

	public String getProjectState() {
		return projectState;
	}

	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

	public int getStatisticalCycle() {
		return statisticalCycle;
	}

	public void setStatisticalCycle(int statisticalCycle) {
		this.statisticalCycle = statisticalCycle;
	}

	public List<Map<String, Object>> getTargetList() {
		return targetList;
	}

	public void setTargetList(List<Map<String, Object>> targetList) {
		this.targetList = targetList;
	}
}
