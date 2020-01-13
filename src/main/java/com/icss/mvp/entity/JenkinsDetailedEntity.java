package com.icss.mvp.entity;

import java.util.Date;
import com.icss.mvp.util.UUIDUtil;

public class JenkinsDetailedEntity {

	private String id;
	private String no;
	private Date createTime;
	private String type;
	private String identity;// 构建次数
	private Date timeStamp;// 采集时间
	private String platform;// 云龙/jenkins
	private String measure;// 指标字段
	private String value;// 指标值

	public JenkinsDetailedEntity() {
		this.id = UUIDUtil.getNew();
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMeasure() {
		return measure;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}