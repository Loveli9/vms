package com.icss.mvp.entity;

import java.util.Date;

public class ProjectAssessInfo {

	private String name;
	private String hwzpdu;
	private String pduSpdt;
	private String du;
	private String pdu;
	private String area;
	private Date startDate;
	private Date endDate;
	private String pm;

	private String no;
	private String projectCycle;
	private String shaftStability;
	private String timelinessDelivery;
	private String deliveryQuality;
	private String acceptanceQuality;
	private String blackEvent;
	private String totalPoints;
	private String question;
	private String singleAnalysis;
	private String imprMeasure;
	private String progressDesc;
	private String state;// 状态标识 (Close, Open)
	
	private int assessment;
	private int aar;
	private int audit;
	private int quality;
	private double degree;
	
	public double getDegree() {
		return degree;
	}

	public void setDegree(double degree) {
		this.degree = degree;
	}

	private Integer id;
	private Integer flag;//仪表盘定制标识 1:已定制数据，0:未定制数据 
	private String version;
	private Date finishTime;
	private Date actualFinishTime;// 实际完成时间
	private String personLiable;
	private Integer isDeleted;// 逻辑删除标识 1:已删除数据，0:正常数据
	private Integer is361;// 逻辑标识 1:361评估问题，2:风险，3:指标评估问题
	private Date createTime;// 创建时间
	private Date modifyTime;// 修改时间
	
	private String topic;//标题
	private String severity;//严重级别
	private String prior;//优先级
	private String probability;//发生概率
	private String speed;//进度
	private String iteId;//迭代

	private String is361str;
	private int limit;
	private int offset;
	
	public String getIs361str() {
		return is361str;
	}
	public void setIs361str(String is361str) {
		this.is361str = is361str;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
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

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
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

	public Integer getIs361() {
		return is361;
	}

	public void setIs361(Integer is361) {
		this.is361 = is361;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getProjectCycle() {
		return projectCycle;
	}

	public void setProjectCycle(String projectCycle) {
		this.projectCycle = projectCycle;
	}

	public String getShaftStability() {
		return shaftStability;
	}

	public void setShaftStability(String shaftStability) {
		this.shaftStability = shaftStability;
	}

	public String getTimelinessDelivery() {
		return timelinessDelivery;
	}

	public void setTimelinessDelivery(String timelinessDelivery) {
		this.timelinessDelivery = timelinessDelivery;
	}

	public String getDeliveryQuality() {
		return deliveryQuality;
	}

	public void setDeliveryQuality(String deliveryQuality) {
		this.deliveryQuality = deliveryQuality;
	}

	public String getAcceptanceQuality() {
		return acceptanceQuality;
	}

	public void setAcceptanceQuality(String acceptanceQuality) {
		this.acceptanceQuality = acceptanceQuality;
	}

	public String getBlackEvent() {
		return blackEvent;
	}

	public void setBlackEvent(String blackEvent) {
		this.blackEvent = blackEvent;
	}

	public String getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(String totalPoints) {
		this.totalPoints = totalPoints;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getSingleAnalysis() {
		return singleAnalysis;
	}

	public void setSingleAnalysis(String singleAnalysis) {
		this.singleAnalysis = singleAnalysis;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
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
    
}