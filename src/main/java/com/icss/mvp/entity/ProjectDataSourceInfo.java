package com.icss.mvp.entity;

import java.util.Date;

public class ProjectDataSourceInfo {
	
	private String no;
	
	//private String sourceValue;
	private String source_value;
	private String url;
	
	private String user;
	
	private String password;
	
	private Date createDate;
	
	private String creator;
	
	private Date updateDate;
	
	private String updateUser;
	
	private String version;
	
	private String giturl;
	
	private String isourl;
	
	private String ciurl;
	
	private String tmurl;
	
	private String gitbranch;
	
	private String scope; //项目统计需要过滤掉的版本(多个用分号隔开)

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getGitbranch() {
		return gitbranch;
	}

	public void setGitbranch(String gitbranch) {
		this.gitbranch = gitbranch;
	}

	public String getTmurl() {
		return tmurl;
	}

	public void setTmurl(String tmurl) {
		this.tmurl = tmurl;
	}

	public String getIsourl() {
		return isourl;
	}

	public void setIsourl(String isourl) {
		this.isourl = isourl;
	}

	public String getCiurl() {
		return ciurl;
	}

	public void setCiurl(String ciurl) {
		this.ciurl = ciurl;
	}

	public String getGiturl() {
		return giturl;
	}

	public void setGiturl(String giturl) {
		this.giturl = giturl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getNo() {
		return no;
	}

	/*public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public String getSourceValue() {
		return sourceValue;
	}*/
	

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSource_value() {
		return source_value;
	}

	public void setSource_value(String source_value) {
		this.source_value = source_value;
	}

	public String getUrl() {
		return url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreator() {
		return creator;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

}
