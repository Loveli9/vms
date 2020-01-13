package com.icss.mvp.entity;

import java.util.Date;

/**
 * 
 * @ClassName: IterativeWorkManage
 * @Description: 需求管理实体类
 * @author chengchenhui
 * @date 2018年8月3日
 *
 */
public class IterativeWorkManage {
	private String id;
	private String iteType;// 类型 story bug task
	private String topic;// 标题
	private String creator;// 创建人
	private String solvePerson;// 处理人
	private String prior;// 优先级
	private String importance;// 重要程度
	private String status;// 状态
	private String change;// 变更
	private String version;// 发布版本号
	private Date createTime;// 创建时间
	private Date updateTime;// 更新时间
	private String iteId;// 迭代id
	private String proNo;// 项目编号
	private Date startTime;// 开始时间
	private Date endTime;// 结束时间
	private String describeInfo;// 描述
	private int isDeleted;// 删除
	private String iteName;// 迭代名称
	private String expectHours;// 预计工作量
	private String actualHours;// 实际工作量
	private String wrField;// 领域
	private String userName;// 创建人
	private Date planStartTime;// 计划开始日期
	private Date planEndTime;// 计划结束日期
	private String codeAmount;// 代码量
	private String personLiable;// 责任人
	private int finalimit;// 完成度

	private String demandCode;// 需求编码
	private String document;// 软件需求规格文档（基线号）
	private String demandType;// 需求类型
	private String alertContent;// 变更内容
	private String design;// 设计文档(基线号/章节号)
	private String luNumber;// LLT/UT用例编号(基线号)
	private String testNumber;// 测试用例编号(基线号)
	private String checkResult;// 验收结果
	private String remarks;// 备注

	/*
	 * private String topic1;//标题1 public String getTopic1() { return topic1; }
	 * public void setTopic1(String topic1) { this.topic1 = topic1; }
	 */

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getDemandCode() {
		return demandCode;
	}

	public void setDemandCode(String demandCode) {
		this.demandCode = demandCode;
	}

	public String getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(String testNumber) {
		this.testNumber = testNumber;
	}

	public String getLuNumber() {
		return luNumber;
	}

	public void setLuNumber(String luNumber) {
		this.luNumber = luNumber;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIteType() {
		return iteType;
	}

	public void setIteType(String iteType) {
		this.iteType = iteType;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSolvePerson() {
		return solvePerson;
	}

	public void setSolvePerson(String solvePerson) {
		this.solvePerson = solvePerson;
	}

	public String getPrior() {
		return prior;
	}

	public void setPrior(String prior) {
		this.prior = prior;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIteId() {
		return iteId;
	}

	public void setIteId(String iteId) {
		this.iteId = iteId;
	}

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDescribeInfo() {
		return describeInfo;
	}

	public void setDescribeInfo(String describeInfo) {
		this.describeInfo = describeInfo;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIteName() {
		return iteName;
	}

	public void setIteName(String iteName) {
		this.iteName = iteName;
	}

	public String getExpectHours() {
		return expectHours;
	}

	public void setExpectHours(String expectHours) {
		this.expectHours = expectHours;
	}

	public String getActualHours() {
		return actualHours;
	}

	public void setActualHours(String actualHours) {
		this.actualHours = actualHours;
	}

	public String getWrField() {
		return wrField;
	}

	public void setWrField(String wrField) {
		this.wrField = wrField;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	public String getCodeAmount() {
		return codeAmount;
	}

	public void setCodeAmount(String codeAmount) {
		this.codeAmount = codeAmount;
	}

	public String getPersonLiable() {
		return personLiable;
	}

	public void setPersonLiable(String personLiable) {
		this.personLiable = personLiable;
	}

	public int getFinalimit() {
		return finalimit;
	}

	public void setFinalimit(int finalimit) {
		this.finalimit = finalimit;
	}

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public String getAlertContent() {
		return alertContent;
	}

	public void setAlertContent(String alertContent) {
		this.alertContent = alertContent;
	}

}
