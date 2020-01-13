package com.icss.mvp.entity;

public class ProjectCommentsInfo extends ProjectAssessInfo{

	private Integer open;
	
	private Integer closed;
	
	private Integer delay;
	
	private Integer added;
	
	private Integer sum;
	
	private double degree;
	
	
	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Integer getAdded() {
		return added;
	}

	public void setAdded(Integer added) {
		this.added = added;
	}

	public double getDegree() {
		return degree;
	}

	public void setDegree(double degree) {
		this.degree = degree;
	}
}
