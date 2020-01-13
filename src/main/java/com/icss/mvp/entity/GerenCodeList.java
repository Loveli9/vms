package com.icss.mvp.entity;

import java.util.List;

/**
 * 
 * @author chenweipu
 *
 */
public class GerenCodeList
{
	private String proNo; //项目编号
	private Integer type; //类型
	private List<GerenCode> gerenCodes;//每月投入比例
	public String getProNo() {
		return proNo;
	}
	public void setProNo(String proNo) {
		this.proNo = proNo;
	}
	public List<GerenCode> getGerenCodes() {
		return gerenCodes;
	}
	public void setGerenCodes(List<GerenCode> gerenCodes) {
		this.gerenCodes = gerenCodes;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
