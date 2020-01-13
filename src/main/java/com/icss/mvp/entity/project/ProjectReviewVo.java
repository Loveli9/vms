package com.icss.mvp.entity.project;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ProjectReviewVo{

    private String  id;
    private String  proNo;       //项目编号
    private String  projectStatus; //项目状态
    private String  projectReview; //项目点评&趋势
    private Date    statisticalTime; //统计时间
    //指标数据
    private String	demandCompletion; //需求完成率
    private String  progressCompletion;//进度完成率
    private String  progressDeviation;//进度(偏差率)
    
    private int  memberDiffer;//总人力缺口
    private String  memberArrival;//总人力到位率
    private int  keyRolesDiffer;//关键角色缺口
    private String  keyRolesArrival;//关键角色到位率
    private String  keyRolesPass;//关键角色答辩通过率
    private int  keyRolesfail;//待答辩人数
    private String  resources;//资源（满足度&匹配度）
    
    private int  changeNumber;//变更次数
    private String  money;//金额（万元）
    private String  workload;//工作量（KLOC）
    private String  changes;//变更（变更率）
    
    private int  greenLight;//绿灯个数
    private int  redLight;//红灯个数
    private int  yellowLight;//黄灯个数
    
    private String	resourcesLamp;//资源点灯
    private String  progressLamp;//进度点灯
    private String	qualityLamp;//质量目标点灯
    private String  changesLamp;//变更点灯
    
    private List<String> fiveQualityLamp;//近5次质量点灯
	private List<String> fiveKeyRoles;//近5次资源点灯
	private List<String> fivePlanLamp;//近5次进度点灯
	private List<String> fiveScopeLamp;//近5次变更点灯
	private List<String> fiveStatusAssessment;//近5次状态评估
	private List<String> fiveComment;//近5次点评
    
    private String measureList;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}


    public String getProjectReview() {
        return projectReview;
    }

    public void setProjectReview(String projectReview) {
        this.projectReview = projectReview;
    }

    public Date getStatisticalTime() {
        return statisticalTime;
    }

    public void setStatisticalTime(Date statisticalTime) {
        this.statisticalTime = statisticalTime;
    }

	public String getDemandCompletion() {
		return demandCompletion;
	}

	public void setDemandCompletion(String demandCompletion) {
		this.demandCompletion = demandCompletion;
	}

	public String getProgressCompletion() {
		return progressCompletion;
	}

	public void setProgressCompletion(String progressCompletion) {
		this.progressCompletion = progressCompletion;
	}

	public String getProgressDeviation() {
		return progressDeviation;
	}

	public void setProgressDeviation(String progressDeviation) {
		this.progressDeviation = progressDeviation;
	}

	public int getMemberDiffer() {
		return memberDiffer;
	}

	public void setMemberDiffer(int memberDiffer) {
		this.memberDiffer = memberDiffer;
	}

	public String getMemberArrival() {
		return memberArrival;
	}

	public void setMemberArrival(String memberArrival) {
		this.memberArrival = memberArrival;
	}

	public int getKeyRolesDiffer() {
		return keyRolesDiffer;
	}

	public void setKeyRolesDiffer(int keyRolesDiffer) {
		this.keyRolesDiffer = keyRolesDiffer;
	}

	public String getKeyRolesArrival() {
		return keyRolesArrival;
	}

	public void setKeyRolesArrival(String keyRolesArrival) {
		this.keyRolesArrival = keyRolesArrival;
	}

	public String getKeyRolesPass() {
		return keyRolesPass;
	}

	public void setKeyRolesPass(String keyRolesPass) {
		this.keyRolesPass = keyRolesPass;
	}

	public int getKeyRolesfail() {
		return keyRolesfail;
	}

	public void setKeyRolesfail(int keyRolesfail) {
		this.keyRolesfail = keyRolesfail;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public int getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(int changeNumber) {
		this.changeNumber = changeNumber;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getWorkload() {
		return workload;
	}

	public void setWorkload(String workload) {
		this.workload = workload;
	}

	public String getChanges() {
		return changes;
	}

	public void setChanges(String changes) {
		this.changes = changes;
	}

	public int getGreenLight() {
		return greenLight;
	}

	public void setGreenLight(int greenLight) {
		this.greenLight = greenLight;
	}

	public int getRedLight() {
		return redLight;
	}

	public void setRedLight(int redLight) {
		this.redLight = redLight;
	}

	public int getYellowLight() {
		return yellowLight;
	}

	public void setYellowLight(int yellowLight) {
		this.yellowLight = yellowLight;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getResourcesLamp() {
		return resourcesLamp;
	}

	public void setResourcesLamp(String resourcesLamp) {
		this.resourcesLamp = resourcesLamp;
	}

	public String getProgressLamp() {
		return progressLamp;
	}

	public void setProgressLamp(String progressLamp) {
		this.progressLamp = progressLamp;
	}

	public String getQualityLamp() {
		return qualityLamp;
	}

	public void setQualityLamp(String qualityLamp) {
		this.qualityLamp = qualityLamp;
	}

	public String getChangesLamp() {
		return changesLamp;
	}

	public void setChangesLamp(String changesLamp) {
		this.changesLamp = changesLamp;
	}

	public String getMeasureList() {
		return measureList;
	}

	public void setMeasureList(String measureList) {
		this.measureList = measureList;
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
    
}
