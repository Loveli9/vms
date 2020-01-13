package com.icss.mvp.entity;

public class CiSimianInfo {

	private int id;
	
    private String name;
	
	private int SimiThreshold;
	
	private int dupLine;
	
	private int dupBlock;
	
	private int fileDup;
	
	private int file;
	
	private int significantLine;
	
	private int totalRawLine;
	
	private double percentDup;
	
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

	public int getSimiThreshold() {
		return SimiThreshold;
	}

	public void setSimiThreshold(int simiThreshold) {
		SimiThreshold = simiThreshold;
	}

	public int getDupLine() {
		return dupLine;
	}

	public void setDupLine(int dupLine) {
		this.dupLine = dupLine;
	}

	public int getDupBlock() {
		return dupBlock;
	}

	public void setDupBlock(int dupBlock) {
		this.dupBlock = dupBlock;
	}

	public int getFileDup() {
		return fileDup;
	}

	public void setFileDup(int fileDup) {
		this.fileDup = fileDup;
	}

	public int getFile() {
		return file;
	}

	public void setFile(int file) {
		this.file = file;
	}

	public int getSignificantLine() {
		return significantLine;
	}

	public void setSignificantLine(int significantLine) {
		this.significantLine = significantLine;
	}

	public int getTotalRawLine() {
		return totalRawLine;
	}

	public void setTotalRawLine(int totalRawLine) {
		this.totalRawLine = totalRawLine;
	}

	public double getPercentDup() {
		return percentDup;
	}

	public void setPercentDup(double percentDup) {
		this.percentDup = percentDup;
	}

	public double getSort() {
		return sort;
	}

	public void setSort(double sort) {
		this.sort = sort;
	}

	

}
