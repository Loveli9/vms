package com.icss.mvp.entity;

import java.util.List;

/**
 * 每月投入比例封装类入参
 * @author chenweipu
 *
 */
public class MonthlyManpowerList {
	private String proNo; //项目编号
	private String type; //类型
	private List<MonthlyManpower> manpowers;//每月投入比例
	public String getProNo() {
		return proNo;
	}
	public void setProNo(String proNo) {
		this.proNo = proNo;
	}
	public List<MonthlyManpower> getManpowers() {
		return manpowers;
	}
	public void setManpowers(List<MonthlyManpower> manpowers) {
		this.manpowers = manpowers;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}