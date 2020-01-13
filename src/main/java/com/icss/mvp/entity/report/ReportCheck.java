package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectInfoVo;
import com.icss.mvp.supports.entity.IEntity;
import com.icss.mvp.util.NumberUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 报表审核实体
 */
@TableName(value = "report_check")
public class ReportCheck implements IEntity {

    public static final Integer CHECK_STATUS_WAIT_SUBMIT_CHECK = 0;
    public static final Integer CHECK_STATUS_WAIT_CHECK = 1;
    public static final Integer CHECK_STATUS_CHECK_PASS = 2;
    public static final Integer CHECK_STATUS_CHECK_FAILED = 3;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField()
    private String qa;

    @TableField(value = "project_id")
    private String projectId;

    @TableField(value = "report_id")
    private Integer reportId;

    @TableField(value = "report_row_ids")
    private String reportRowIds;

    private String submitor;

    @TableField(value = "submit_date")
    private Date submitDate;

    private Boolean checked;

    @TableField(value = "check_status")
    private Integer checkStatus;

    @TableField(value = "check_description")
    private String checkDescription;


    @TableField(exist = false)
    private List<ReportCheckKpi> kpis;

    @TableField(exist = false)
    private ProjectInfo project;

    @TableField(exist = false)
    private Report report;


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQa() {
        return qa;
    }

    public void setQa(String qa) {
        this.qa = qa;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportRowIds() {
        return reportRowIds;
    }

    public void setReportRowIds(String reportRowIds) {
        this.reportRowIds = reportRowIds;
    }

    public List<Integer> getReportRowIdList() {
        List<Integer> ids = new ArrayList<>();
        if (StringUtils.isNotEmpty(this.reportRowIds)) {
            String[] arr = this.reportRowIds.replaceAll("[\\s]", "").split("[,]");
            for (String item : arr) {
                ids.add(NumberUtil.stringToInteger(item));
            }
        }
        return ids;
    }

    public String getSubmitor() {
        return submitor;
    }

    public void setSubmitor(String submitor) {
        this.submitor = submitor;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
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

    public List<ReportCheckKpi> getKpis() {
        return kpis;
    }

    public void setKpis(List<ReportCheckKpi> kpis) {
        this.kpis = kpis;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}
