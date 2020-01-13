package com.icss.mvp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
@SuppressWarnings("all")
public class TeamInfoVo {
    private String no;

    private String name;

    private String tm;

    private String tmid;

    private String hwpdu;
    
    private String hwpduid;

    private String hwzpdu;
    
    private String hwzpduid;

    private String pduSpdt;
    
    private String pduSpdtid;

    private Long hwdeptno;

    private String bu;
    
    private String buid;
	
	private String pdu;
	
	private String pduid;
	
	private String du;
	
	private String duid;
	
    private String optdeptno;

    private String area;
    
    private String areaid;

    private String areacode;

    private String cooptype;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date importdate;

    private String importuser;


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

    public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getPdu() {
		return pdu;
	}

	public void setPdu(String pdu) {
		this.pdu = pdu;
	}

	public String getDu() {
		return du;
	}

	public void setDu(String du) {
		this.du = du;
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

    public String getCooptype() {
        return cooptype;
    }

    public void setCooptype(String cooptype) {
        this.cooptype = cooptype == null ? null : cooptype.trim();
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm == null ? null : tm.trim();
    }

    public String getTmid() {
        return tmid;
    }

    public void setTmid(String tmid) {
        this.tmid = tmid == null ? null : tmid.trim();
    }

    public Date getImportDate() {
        return importdate;
    }

    public void setImportDate(Date importdate) {
        this.importdate = importdate;
    }

	public String getImportuser() {
		return importuser;
	}

	public void setImportuser(String importuser) {
		this.importuser = importuser;
	}

	public String getHwpduid() {
		return hwpduid;
	}

	public void setHwpduid(String hwpduid) {
		this.hwpduid = hwpduid;
	}

	public String getHwzpduid() {
		return hwzpduid;
	}

	public void setHwzpduid(String hwzpduid) {
		this.hwzpduid = hwzpduid;
	}

	public String getPduSpdtid() {
		return pduSpdtid;
	}

	public void setPduSpdtid(String pduSpdtid) {
		this.pduSpdtid = pduSpdtid;
	}

	public String getBuid() {
		return buid;
	}

	public void setBuid(String buid) {
		this.buid = buid;
	}

	public String getPduid() {
		return pduid;
	}

	public void setPduid(String pduid) {
		this.pduid = pduid;
	}

	public String getDuid() {
		return duid;
	}

	public void setDuid(String duid) {
		this.duid = duid;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
}