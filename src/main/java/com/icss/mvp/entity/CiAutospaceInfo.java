package com.icss.mvp.entity;

public class CiAutospaceInfo {
    
	private int id;
	
	private String name;
	
	private int totalCase;
	
	private String passedPercent;
	
	private int passed;
	
	private int failed;
	
	private int investigated;
	
	private int unavailable;
	
	private int blocked;
	
	private int none;
	
	private String sort;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalCase() {
		return totalCase;
	}

	public void setTotalCase(int totalCase) {
		this.totalCase = totalCase;
	}

	public String getPassedPercent() {
		return passedPercent;
	}

	public void setPassedPercent(String passedPercent) {
		this.passedPercent = passedPercent;
	}

	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getInvestigated() {
		return investigated;
	}

	public void setInvestigated(int investigated) {
		this.investigated = investigated;
	}

	public int getUnavailable() {
		return unavailable;
	}

	public void setUnavailable(int unavailable) {
		this.unavailable = unavailable;
	}

	public int getBlocked() {
		return blocked;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}

	public int getNone() {
		return none;
	}

	public void setNone(int none) {
		this.none = none;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	
}
