package com.icss.mvp.entity.project;

import java.util.List;

/**
 * Created by up on 2019/2/14.
 */
public class ProjectReviewEntity {

	private Integer id;
	private String proNo; // 项目编号
	private String projectStatus; // 项目状态
	private String projectReview; // 项目点评&趋势
	private String statisticalTime; // 统计时间
	// 指标数据
	private String demandCompletion; // 需求完成率
	private String progressCompletion;// 进度完成率
	private String progressDeviation;// 进度(偏差率)

	private int memberDiffer;// 总人力缺口
	private String memberArrival;// 总人力到位率
	private int keyRolesDiffer;// 关键角色缺口
	private String keyRolesArrival;// 关键角色到位率
	private String keyRolesPass;// 关键角色答辩通过率
	private int keyRolesfail;// 待答辩人数
	private String resources;// 资源（满足度&匹配度）

	private String changeNumber;// 变更工作量
	private String demandNumber;// 总需工作量
	private String money;// 金额（万元）
	private String workload;// 工作量（KLOC）
	private String changes;// 变更（变更率）

	private int greenLight;// 绿灯个数
	private int redLight;// 红灯个数
	private int yellowLight;// 黄灯个数

	private String resourcesLamp;// 资源点灯
	private String progressLamp;// 进度点灯
	private String qualityLamp;// 质量目标点灯
	private String changesLamp;// 变更点灯

	private String measureList;
	private int isClose; // 结项标识:"0"未结项,"1"已结项

	private String type; // 商业模式以及配置历史记录是否为空的判断
	private List<String> monthList;// 一个指标所以的时间集合
	private List<String> valueList;// 一个指标不同时间数据的集合

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public List<String> getValueList() {
		return valueList;
	}

	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIsClose() {
		return isClose;
	}

	public void setIsClose(int isClose) {
		this.isClose = isClose;
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
		this.proNo = proNo;
	}

	public String getProjectReview() {
		return projectReview;
	}

	public void setProjectReview(String projectReview) {
		this.projectReview = projectReview;
	}

	public String getStatisticalTime() {
		return statisticalTime;
	}

	public void setStatisticalTime(String statisticalTime) {
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

	public String getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(String changeNumber) {
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

	public String getDemandNumber() {
		return demandNumber;
	}

	public void setDemandNumber(String demandNumber) {
		this.demandNumber = demandNumber;
	}

}
