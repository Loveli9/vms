package com.icss.mvp.entity;

import java.io.Serializable;

/**
 * 
 *
 /**
 * 
 *
 *@author zqq
 */
public class TComputeResultPromotion implements Serializable {

	
	
	private Integer id;

	
	
	private String noId;

	
	
	private Integer indexId;

	
	
	private String computeTime;

	
	
	public String getNoId() {
		return noId;
	}
	public void setNoId(String noId) {
		this.noId = noId;
	}
	private Double promotionResult;
	public void setId(Integer id) {
		this.id=id;
	}
	public Integer getId() {
		 return this.id;
	}
	public void setIndexId(Integer indexId) {
		this.indexId=indexId;
	}
	public Integer getIndexId() {
		 return this.indexId;
	}
	public void setComputeTime(String computeTime) {
		this.computeTime=computeTime;
	}
	public String getComputeTime() {
		 return this.computeTime;
	}
	public void setPromotionResult(Double promotionResult) {
		this.promotionResult=promotionResult;
	}
	public Double getPromotionResult() {
		 return this.promotionResult;
	}
}