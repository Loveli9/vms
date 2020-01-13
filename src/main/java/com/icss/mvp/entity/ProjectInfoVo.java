package com.icss.mvp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectInfoVo {
    private String no;

    private String name;


    private String hwpdu;

    private String hwzpdu;

    private String pduSpdt;

    private Long hwdeptno;


    private String optdeptno;

    private String area;

    private String areacode;

    private String type;

    private String pm;

    private String pmid;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    private String projectState;
    private String isOffshore;

    private String status;
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }


    public String getHwpdu() {
        return hwpdu;
    }

    public void setHwpdu(String hwpdu) {
        this.hwpdu = hwpdu == null ? null : hwpdu.trim();
    }

    public String getHwzpdu() {
        return hwzpdu;
    }

    public void setHwzpdu(String hwzpdu) {
        this.hwzpdu = hwzpdu == null ? null : hwzpdu.trim();
    }

    public String getPduSpdt() {
        return pduSpdt;
    }

    public void setPduSpdt(String pduSpdt) {
        this.pduSpdt = pduSpdt == null ? null : pduSpdt.trim();
    }

    public Long getHwdeptno() {
        return hwdeptno;
    }

    public void setHwdeptno(Long hwdeptno) {
        this.hwdeptno = hwdeptno;
    }


    public String getOptdeptno() {
        return optdeptno;
    }

    public void setOptdeptno(String optdeptno) {
        this.optdeptno = optdeptno == null ? null : optdeptno.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode == null ? null : areacode.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm == null ? null : pm.trim();
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid == null ? null : pmid.trim();
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getIsOffshore() {
        return isOffshore;
    }

    public void setIsOffshore(String isOffshore) {
        this.isOffshore = isOffshore == null ? null : isOffshore.trim();
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}