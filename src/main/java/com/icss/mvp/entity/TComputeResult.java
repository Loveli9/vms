package com.icss.mvp.entity;

import java.io.Serializable;

/**
 * 
 *
 * @author zqq
 */
public class TComputeResult implements Serializable {

	private Integer id;
	// 团队/po/服务id

	private String objectId;
	// 1：团队，2：po，3：服务

	private Integer type;
	// 范围

	private String scope;
	// 进度

	private String progress;
	// 过程质量

	private String processquality;
	// 效率

	private String efficiency;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	/** 团队/po/服务id */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/** 团队/po/服务id */
	public String getObjectId() {
		return this.objectId;
	}

	/** 1：团队，2：po，3：服务 */
	public void setType(Integer type) {
		this.type = type;
	}

	/** 1：团队，2：po，3：服务 */
	public Integer getType() {
		return this.type;
	}

	/** 范围 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/** 范围 */
	public String getScope() {
		return this.scope;
	}

	/** 进度 */
	public void setProgress(String progress) {
		this.progress = progress;
	}

	/** 进度 */
	public String getProgress() {
		return this.progress;
	}

	/** 过程质量 */
	public void setProcessquality(String processquality) {
		this.processquality = processquality;
	}

	/** 过程质量 */
	public String getProcessquality() {
		return this.processquality;
	}

	/** 效率 */
	public void setEfficiency(String efficiency) {
		this.efficiency = efficiency;
	}

	/** 效率 */
	public String getEfficiency() {
		return this.efficiency;
	}
}
