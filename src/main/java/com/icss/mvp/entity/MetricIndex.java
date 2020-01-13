package com.icss.mvp.entity;

import java.util.List;
import java.util.Map;

public class MetricIndex {
	
	private String name;
	
	Map<String, List<String>> uslAndTime;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, List<String>> getUslAndTime() {
		return uslAndTime;
	}
	public void setUslAndTime(Map<String, List<String>> uslAndTime) {
		this.uslAndTime = uslAndTime;
	}
	
}
