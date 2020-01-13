package com.icss.mvp.entity;

import java.util.List;
import java.util.Map;

/**
 * 个人代码详情
 * @author Administrator
 *
 */
public class CodeParam {
	private String no;
	private String author;
	private String month;
	private List<Map<String, Object>> types;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public List<Map<String, Object>> getTypes() {
		return types;
	}
	public void setTypes(List<Map<String, Object>> types) {
		this.types = types;
	}
	
	
}
