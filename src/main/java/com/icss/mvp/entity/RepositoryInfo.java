package com.icss.mvp.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class RepositoryInfo {
	
	private Integer proConfId;//ProjectConfigInfo主键id
	private Integer id;//主键
	private Integer isDeleted;//逻辑删除标识 1:已删除数据，0:正常数据
	private Integer type; //对接平台类型1:DTS,2:SVN,3:Git,4:TMSS,5:ci,6:iso
	private String url;//代码仓库地址
	private String branch;//代码分支 git可指定branch，为空即master分支 svn可指定directory
	
	private Integer otherAccount;//是否使用其他账号：0使用，1不使用
	
	private String scope;//实际统计的代码路径
	
	private String baseLineVersion; //基线版本号
	private String excludeRevision; //剔出指定版本
	private String excludePath; //剔出指定路径下
	
	private Date createTime;//创建时间
	private Date modifyTime;//修改时间
	
	private String no;//项目编号
	
	
	
	public String getBaseLineVersion() {
		return baseLineVersion;
	}
	public void setBaseLineVersion(String baseLineVersion) {
		this.baseLineVersion = baseLineVersion;
	}
	public String getExcludeRevision() {
		return excludeRevision;
	}
	public void setExcludeRevision(String excludeRevision) {
		this.excludeRevision = excludeRevision;
	}
	public String getExcludePath() {
		return excludePath;
	}
	public void setExcludePath(String excludePath) {
		this.excludePath = excludePath;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
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
	public Integer getOtherAccount() {
		return otherAccount;
	}
	public void setOtherAccount(Integer otherAccount) {
		this.otherAccount = otherAccount;
	}
	public Integer getProConfId() {
		return proConfId;
	}
	public void setProConfId(Integer proConfId) {
		this.proConfId = proConfId;
	}
	
	public void exclude2Scope(){
		Map<String,Object> scopes = new HashMap<>();
		if(this.excludeRevision!=null) {
			this.excludeRevision = this.excludeRevision.replace("；", ";");
			String[] exRevisons = this.excludeRevision.split(";");
			scopes.put("excludeRevision", exRevisons);
		}
		if(this.excludePath!=null) {
			this.excludePath = this.excludePath.replace("；", ";");
			String[] exPaths = this.excludePath.split(";");
			scopes.put("excludePath", exPaths);
		}
		if (this.baseLineVersion!=null) {
			String baseLineVersion = this.baseLineVersion;
			scopes.put("baseLineVersion", baseLineVersion);
		}
		scope = JSON.toJSONString(scopes);
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	
}
