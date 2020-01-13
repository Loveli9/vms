package com.icss.mvp.entity;

import java.util.Date;

public class ProjectCommentsListInfo {

	private String id;//主键id
	private String no;//项目编号
	private String version;//项目编号
	private String question;//问题
	private String imprMeasure;//改进措施
	private String progressDesc;//进展&效果描述
	private Date finishTime;//计划完成时间
	private Date actualFinishTime;//实际完成时间
	private String personLiable;//责任人
	private Integer isDeleted;//逻辑删除标识 1:已删除数据，0:正常数据
	private Integer is361;//逻辑标识 1:361评估问题，2:风险，3:指标评估问题
	private Date createTime;//创建时间
	private Date modifyTime;//修改时间
	private String state;//状态
	
	private String topic;//标题
	private String severity;//严重级别
	private String prior;//优先级
	private String probability;//发生概率
	private String speed;//进度
	private String iteId;//迭代
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getPrior() {
		return prior;
	}

	public void setPrior(String prior) {
		this.prior = prior;
	}

	public String getProbability() {
		return probability;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getIteId() {
		return iteId;
	}

	public void setIteId(String iteId) {
		this.iteId = iteId;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getActualFinishTime() {
		return actualFinishTime;
	}
	public void setActualFinishTime(Date actualFinishTime) {
		this.actualFinishTime = actualFinishTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getImprMeasure() {
		return imprMeasure;
	}
	public void setImprMeasure(String imprMeasure) {
		this.imprMeasure = imprMeasure;
	}
	public String getProgressDesc() {
		return progressDesc;
	}
	public void setProgressDesc(String progressDesc) {
		this.progressDesc = progressDesc;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public String getPersonLiable() {
		return personLiable;
	}
	public void setPersonLiable(String personLiable) {
		this.personLiable = personLiable;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Integer getIs361() {
		return is361;
	}
	public void setIs361(Integer is361) {
		this.is361 = is361;
	}
}
