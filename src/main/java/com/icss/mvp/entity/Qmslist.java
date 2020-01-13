package com.icss.mvp.entity;

import com.icss.mvp.entity.common.request.BaseRequest;

@SuppressWarnings("serial")
public class Qmslist extends BaseRequest {
	private Integer id;//
	private String source;// 来源
	private String label;// 维度
	private String category;// 评估要素
	private String type;// 分类                在qms_result表中，快照(0)/发布(1)/当前(3)
	private String content;// 主要评估项及内容
	private String reference;// 查证参考
	private String interviewee;// 访谈对象
	private String name;// 项目名称
	private String area;// 地域
	private String spdt;// 交付部
	private String pm;
	private String qa;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSpdt() {
		return spdt;
	}

	public void setSpdt(String spdt) {
		this.spdt = spdt;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getQa() {
		return qa;
	}

	public void setQa(String qa) {
		this.qa = qa;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getInterviewee() {
		return interviewee;
	}

	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}
}
