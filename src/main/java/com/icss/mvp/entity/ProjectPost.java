package com.icss.mvp.entity;

public class ProjectPost {
	
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPmid() {
		return pmid;
	}

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}

	private String pmid ;
	
    private String position;
    
    private String name;
    
    private Integer demand;
    
    private Integer onDuty;
    
    private String superior;
    
    private String superiorSequence;

	private String dictItemId;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDemand() {
		return demand;
	}

	public void setDemand(Integer demand) {
		this.demand = demand;
	}

	public Integer getOnDuty() {
		return onDuty;
	}

	public void setOnDuty(Integer onDuty) {
		this.onDuty = onDuty;
	}

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	public String getSuperiorSequence() {
		return superiorSequence;
	}

	public void setSuperiorSequence(String superiorSequence) {
		this.superiorSequence = superiorSequence;
	}

	public String getDictItemId() {
		return dictItemId;
	}

	public void setDictItemId(String dictItemId) {
		this.dictItemId = dictItemId;
	}
    
}
