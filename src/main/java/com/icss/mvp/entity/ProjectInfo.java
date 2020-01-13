package com.icss.mvp.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.icss.mvp.constant.ERatingStar;
import com.icss.mvp.util.DateUtils;

public class ProjectInfo {

    private String no;

    private String name;

    private String pm;

    private String area;

    private String hwpdu;

    private String hwzpdu;

    private String pduSpdt;

    private String pmId;
    private String areaId;

    private String clientType;//1-中软,2-华为,3-地域
    private String hwpduId;
    private String hwzpduId;
    private String pduSpdtId;
    private String buId;
    private String pduId;
    private String duId;

    private String bu;

    private String pdu;

    private String du;

    private String type;

    private String projectState;

    //startStr，endStr处理日期
    private Date   startDate;
//    private String startStr;

    private Date   endDate;
//    private String endStr;

    private Date   importDate;

    private String status;

    private String projectType;

    private String qa;

    private String month;

    private String projectSynopsis;

    private String coopType;

    private String teamName;

    private String teamId;

    private String isOffshore;

    private String year;           // 年份

    private String colorType;      // 黄、红、绿类别

    private String starType;       // 星级

    private String pcbCategory;

    private String pcbStatus;

    private String isFavorite;

    private String measureId;

    private int number;

    private double projectBudget;



	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

    public String getStartStr() {
    	if(startDate==null) {
    		return null;
    	}
    	return DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, startDate);
    }

    public String getEndStr() {
    	if(endDate==null) {
    		return null;
    	}
        return DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, endDate);
    }


	public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getHwpdu() {
        return hwpdu;
    }

    public void setHwpdu(String hwpdu) {
        this.hwpdu = hwpdu;
    }

    public String getHwzpdu() {
        return hwzpdu;
    }

    public void setHwzpdu(String hwzpdu) {
        this.hwzpdu = hwzpdu;
    }

    public String getPduSpdt() {
        return pduSpdt;
    }

    public void setPduSpdt(String pduSpdt) {
        this.pduSpdt = pduSpdt;
    }

    public String getProjectSynopsis() {
        return projectSynopsis;
    }

    public void setProjectSynopsis(String projectSynopsis) {
        this.projectSynopsis = projectSynopsis;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        String decode;
        try {
            decode = URLDecoder.decode(projectState, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decode = null;
        }
        this.projectState = StringUtils.isNotBlank(decode) ? decode : projectState;
    }

    public String getCoopType() {
        return coopType;
    }

    public void setCoopType(String coopType) {
        this.coopType = coopType;
    }

    public String getIsOffshore() {
        return isOffshore;
    }

    public void setIsOffshore(String isOffshore) {
        this.isOffshore = isOffshore;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String decode;
        try {
            decode = URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decode = null;
        }
        this.name = StringUtils.isNotBlank(decode) ? decode : name;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getDu() {
        return du;
    }

    public void setDu(String du) {
        this.du = du;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        String decode;
        try {
            decode = URLDecoder.decode(pm, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decode = null;
        }
        this.pm = StringUtils.isNotBlank(decode) ? decode : pm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPdu() {
        return pdu;
    }

    public void setPdu(String pdu) {
        this.pdu = pdu;
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

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getQa() {
        return qa;
    }

    public void setQa(String qa) {
        this.qa = qa;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColorType() {
        return colorType;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    public String getStarType() {
        ERatingStar star = ERatingStar.getByTitle(starType);
        if (star == null) {
            try {
                star = ERatingStar.values()[Integer.parseInt(starType)];
            } catch (Exception e) {
                star = ERatingStar.ZERO;
            }
        }

        return star.title;
    }

    public void setStarType(String starType) {
        this.starType = starType;
    }

    public String getPcbCategory() {
        return pcbCategory;
    }

    public void setPcbCategory(String pcbCategory) {
        String decode;
        try {
            decode = URLDecoder.decode(pcbCategory, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decode = null;
        }
        this.pcbCategory = StringUtils.isNotBlank(decode) ? decode : pcbCategory;
    }

    public String getPcbStatus() {
        return pcbStatus;
    }

    public void setPcbStatus(String pcbStatus) {
        this.pcbStatus = pcbStatus;
    }

	public String getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(String isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getHwpduId() {
		return hwpduId;
	}

	public void setHwpduId(String hwpduId) {
		this.hwpduId = hwpduId;
	}

	public String getPmId() {
		return pmId;
	}

	public void setPmId(String pmId) {
		this.pmId = pmId;
	}

	public String getHwzpduId() {
		return hwzpduId;
	}

	public void setHwzpduId(String hwzpduId) {
		this.hwzpduId = hwzpduId;
	}

	public String getPduSpdtId() {
		return pduSpdtId;
	}

	public void setPduSpdtId(String pduSpdtId) {
		this.pduSpdtId = pduSpdtId;
	}

	public String getBuId() {
		return buId;
	}

	public void setBuId(String buId) {
		this.buId = buId;
	}

	public String getPduId() {
		return pduId;
	}

	public void setPduId(String pduId) {
		this.pduId = pduId;
	}

	public String getDuId() {
		return duId;
	}

	public void setDuId(String duId) {
		this.duId = duId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public double getProjectBudget() {
		return projectBudget;
	}

	public void setProjectBudget(double projectBudget) {
		this.projectBudget = projectBudget;
	}

}
