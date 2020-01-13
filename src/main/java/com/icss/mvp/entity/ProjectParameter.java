package com.icss.mvp.entity;

import java.util.Date;

public class ProjectParameter {
private String no;
private int parameterId;
private String sourceValue;
private String parameter;
private String unit;
private int isDisplay;
private Date createDate;
private String creator;
private Date UpdateDate;
private String updateUser;
public ProjectParameter() {
	super();
}
public String getNo() {
	return no;
}
public void setNo(String no) {
	this.no = no;
}

public int getParameterId() {
	return parameterId;
}
public void setParameterId(int parameterId) {
	this.parameterId = parameterId;
}
public String getSourceValue() {
	return sourceValue;
}
public void setSourceValue(String sourceValue) {
	this.sourceValue = sourceValue;
}
public String getUnit() {
	return unit;
}
public void setUnit(String unit) {
	this.unit = unit;
}
public String getParameter() {
	return parameter;
}
public void setParameter(String parameter) {
	this.parameter = parameter;
}

public int getIsDisplay() {
	return isDisplay;
}
public void setIsDisplay(int isDisplay) {
	this.isDisplay = isDisplay;
}
public Date getCreateDate() {
	return createDate;
}
public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}
public String getCreator() {
	return creator;
}
public void setCreator(String creator) {
	this.creator = creator;
}
public Date getUpdateDate() {
	return UpdateDate;
}
public void setUpdateDate(Date updateDate) {
	UpdateDate = updateDate;
}

public String getUpdateUser() {
	return updateUser;
}
public void setUpdateUser(String updateUser) {
	this.updateUser = updateUser;
}
@Override
public String toString() {
	return "ProjectParameter [no=" + no + ", ParameterId=" + parameterId
			+ ", sourceValue=" + sourceValue + ", unit=" + unit
			+ ", parameter=" + parameter + ", isDisplay=" + isDisplay
			+ ", createDate=" + createDate + ", creator=" + creator
			+ ", UpdateDate=" + UpdateDate + "]";
}

}
