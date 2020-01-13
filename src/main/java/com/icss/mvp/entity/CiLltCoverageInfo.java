package com.icss.mvp.entity;

public class CiLltCoverageInfo {

	private Integer id;

	private String name;
	
	private int error;
	
	private int failure;
	
	private int method;
	
	private String line_coverage;
	
	private double sort;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public int getFailure() {
		return failure;
	}

	public void setFailure(int failure) {
		this.failure = failure;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getLine_coverage() {
		return line_coverage;
	}

	public void setLine_coverage(String line_coverage) {
		this.line_coverage = line_coverage;
	}

	public double getSort() {
		return sort;
	}

	public void setSort(double sort) {
		this.sort = sort;
	}

	

}
