package com.icss.mvp.entity.rank;
import java.util.Date;
/**
 * Created by chengchenhui on 2019/7/19.
 */

/**
 * @author chengchenhui
 * @title: ProjectMonthBudget
 * @projectName mvp
 * @description: TODO
 * @date 2019/7/1910:23
 */
public class ProjectMonthBudget {
    private String id;
    private String no;
    private String name;
    private String pm;
    private String hwzpdu;
    private String pduSpdt;
    private String pdu;
    private String du;
    private String department;
    
    //总预算
    private double projectBudget;
    private String collection;
    private String startDate;
    private String endDate;
    private Date costDate;
    private double attendMoney;
    private double overMoney;
    private Date createTime;
    private Date updateTime;
    private double normalOut;
    private String time;
    private String maintenanceDate;
    
    //时间进度
    private double timeProgress;
    //产出进度
    private double outputProgress;
    //偏差与点灯
    private double deviationLight;
    //剩余预算
    private double surplusBudget;
    //剩余预算可维持
    private double budgetMaintenance;
    
    //总出勤产出
    private double oldAttendMoney;
    //总加班产出
    private double oldOverMoney;
    //总正常理论产出
    private double oldNormalOut;
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }


    public Date getCostDate() {
        return costDate;
    }

    public void setCostDate(Date costDate) {
        this.costDate = costDate;
    }

    public double getAttendMoney() {
        return attendMoney;
    }

    public void setAttendMoney(double attendMoney) {
        this.attendMoney = attendMoney;
    }

    public double getOverMoney() {
        return overMoney;
    }

    public void setOverMoney(double overMoney) {
        this.overMoney = overMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public double getNormalOut() {
        return normalOut;
    }

    public void setNormalOut(double normalOut) {
        this.normalOut = normalOut;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHwzpdu() {
		return hwzpdu;
	}

	public void setHwzpdu(String hwzpdu) {
		this.hwzpdu = hwzpdu;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getPduSpdt() {
		return pduSpdt;
	}

	public void setPduSpdt(String pduSpdt) {
		this.pduSpdt = pduSpdt;
	}

	public String getDu() {
		return du;
	}

	public void setDu(String du) {
		this.du = du;
	}

	public String getPdu() {
		return pdu;
	}

	public void setPdu(String pdu) {
		this.pdu = pdu;
	}

	public double getProjectBudget() {
		return projectBudget;
	}

	public void setProjectBudget(double projectBudget) {
		this.projectBudget = projectBudget;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public double getTimeProgress() {
		return timeProgress;
	}

	public void setTimeProgress(double timeProgress) {
		this.timeProgress = timeProgress;
	}

	public double getOutputProgress() {
		return outputProgress;
	}

	public void setOutputProgress(double outputProgress) {
		this.outputProgress = outputProgress;
	}

	public double getDeviationLight() {
		return deviationLight;
	}

	public void setDeviationLight(double deviationLight) {
		this.deviationLight = deviationLight;
	}

	public double getSurplusBudget() {
		return surplusBudget;
	}

	public void setSurplusBudget(double surplusBudget) {
		this.surplusBudget = surplusBudget;
	}

	public double getBudgetMaintenance() {
		return budgetMaintenance;
	}

	public void setBudgetMaintenance(double budgetMaintenance) {
		this.budgetMaintenance = budgetMaintenance;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMaintenanceDate() {
		return maintenanceDate;
	}

	public void setMaintenanceDate(String maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}

	public double getOldAttendMoney() {
		return oldAttendMoney;
	}

	public void setOldAttendMoney(double oldAttendMoney) {
		this.oldAttendMoney = oldAttendMoney;
	}

	public double getOldOverMoney() {
		return oldOverMoney;
	}

	public void setOldOverMoney(double oldOverMoney) {
		this.oldOverMoney = oldOverMoney;
	}

	public double getOldNormalOut() {
		return oldNormalOut;
	}

	public void setOldNormalOut(double oldNormalOut) {
		this.oldNormalOut = oldNormalOut;
	}
}
