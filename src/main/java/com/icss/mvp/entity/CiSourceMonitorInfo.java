package com.icss.mvp.entity;

public class CiSourceMonitorInfo {

	private int id;
	
    private String name;
	
	private int statement;
	
	private int method;
	
	private int file;
	
	private int maxDepth;
	
	private int maxComplexity;
	
	private double avgMethod;
	
	private double avgStatement;
	
	private double mI;
	
	private double  sort;

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

	public int getStatement() {
		return statement;
	}

	public void setStatement(int statement) {
		this.statement = statement;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public int getFile() {
		return file;
	}

	public void setFile(int file) {
		this.file = file;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getMaxComplexity() {
		return maxComplexity;
	}

	public void setMaxComplexity(int maxComplexity) {
		this.maxComplexity = maxComplexity;
	}

	public double getAvgMethod() {
		return avgMethod;
	}

	public void setAvgMethod(double avgMethod) {
		this.avgMethod = avgMethod;
	}

	public double getAvgStatement() {
		return avgStatement;
	}

	public void setAvgStatement(double avgStatement) {
		this.avgStatement = avgStatement;
	}

	public double getmI() {
		return mI;
	}

	public void setmI(double mI) {
		this.mI = mI;
	}

	public double getSort() {
		return sort;
	}

	public void setSort(double sort) {
		this.sort = sort;
	}


	
}
