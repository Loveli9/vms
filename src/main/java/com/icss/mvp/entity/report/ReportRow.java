package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 审核状态：0=待提审，1=待审核，2=审核通过，3=审核不过
 */
@TableName(value = "report_row")
public class ReportRow implements IEntity {
    public static final String PERIOD_TYPE_PROJECT = "1";
    public static final String PERIOD_TYPE_PERIOD = "2";

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("period")
    private String period;

    @TableField("period_id")
    private String periodId;

    @TableField("period_name")
    private String periodName;

    @TableField("report_id")
    private Integer reportId;
    //关联KPI列表
    @TableField(exist = false)
    List<ReportKpi> kpis;

    @TableField("period_start_date")
    private Date periodStartDate;
    //审核状态
    @TableField(exist = false)
    private Integer reportCheckId;

    //审核状态
    @TableField(exist = false)
    private Integer checkStatus;

    //审核描述
    @TableField(exist = false)
    private String checkDescription;

    @TableField(value = "period_end_date")
    private Date periodEndDate;

    //工号或任务编号（根据报表类型决定）
    @TableField("code")
    private String code;

    //姓名或任务名（根据报表类型决定）
    @TableField("name")
    private String name;

    public List<ReportKpi> getKpis() {
        return kpis;
    }

    public boolean addKpi(ReportKpi kpi) {
        if (this.kpis == null) {
            this.kpis = new ArrayList<>();
        }
        return this.kpis.add(kpi);
    }

    public void setKpis(List<ReportKpi> kpis) {
        this.kpis = kpis;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public Integer getReportCheckId() {
        return reportCheckId;
    }

    public void setReportCheckId(Integer reportCheckId) {
        this.reportCheckId = reportCheckId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }


    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public Date getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckDescription() {
        return checkDescription;
    }

    public void setCheckDescription(String checkDescription) {
        this.checkDescription = checkDescription;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
