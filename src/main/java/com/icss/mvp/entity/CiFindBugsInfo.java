package com.icss.mvp.entity;

public class CiFindBugsInfo {
 
	private int id;
	
	private String name;
	
	private int hPRIbug;
	
	private int mPRIbug;
	
	private int lPRIbug;
	
	private int bugTotal;
	
	private double hPRIdensity;
	
	private double mPRIdensity;
	
	private double lPRIdensity;
	
	private double densityTotal;
	
	private double sort;

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

	public int gethPRIbug() {
		return hPRIbug;
	}

	public void sethPRIbug(int hPRIbug) {
		this.hPRIbug = hPRIbug;
	}

	public int getmPRIbug() {
		return mPRIbug;
	}

	public void setmPRIbug(int mPRIbug) {
		this.mPRIbug = mPRIbug;
	}

	public int getlPRIbug() {
		return lPRIbug;
	}

	public void setlPRIbug(int lPRIbug) {
		this.lPRIbug = lPRIbug;
	}

	public int getBugTotal() {
		return bugTotal;
	}

	public void setBugTotal(int bugTotal) {
		this.bugTotal = bugTotal;
	}

	public double gethPRIdensity() {
		return hPRIdensity;
	}

	public void sethPRIdensity(double hPRIdensity) {
		this.hPRIdensity = hPRIdensity;
	}

	public double getmPRIdensity() {
		return mPRIdensity;
	}

	public void setmPRIdensity(double mPRIdensity) {
		this.mPRIdensity = mPRIdensity;
	}

	public double getlPRIdensity() {
		return lPRIdensity;
	}

	public void setlPRIdensity(double lPRIdensity) {
		this.lPRIdensity = lPRIdensity;
	}

	public double getDensityTotal() {
		return densityTotal;
	}

	public void setDensityTotal(double densityTotal) {
		this.densityTotal = densityTotal;
	}

	public double getSort() {
		return sort;
	}

	public void setSort(double sort) {
		this.sort = sort;
	}

}
