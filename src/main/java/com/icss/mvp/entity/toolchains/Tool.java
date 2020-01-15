package com.icss.mvp.entity.toolchains;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.istack.internal.NotNull;

@TableName(value = "tool")
public class Tool {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    private String version;

    @TableField("support_process")
    private String supportProcess;

    @TableField("business_property")
    private String businessProperty;

    @TableField("manage_property")
    private String manageProperty;

    @TableField("provide_interface")
    private Boolean provideInterface;

    @TableField("prj_amount")
    private Integer prjAmount;

    @TableField("report_method")
    private String reportMethod;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSupportProcess() {
        return supportProcess;
    }

    public void setSupportProcess(String supportProcess) {
        this.supportProcess = supportProcess;
    }

    public String getBusinessProperty() {
        return businessProperty;
    }

    public void setBusinessProperty(String businessProperty) {
        this.businessProperty = businessProperty;
    }

    public String getManageProperty() {
        return manageProperty;
    }

    public void setManageProperty(String manageProperty) {
        this.manageProperty = manageProperty;
    }

    public Boolean getProvideInterface() {
        return provideInterface;
    }

    public void setProvideInterface(Boolean provideInterface) {
        this.provideInterface = provideInterface;
    }

    public Integer getPrjAmount() {
        return prjAmount;
    }

    public void setPrjAmount(Integer prjAmount) {
        this.prjAmount = prjAmount;
    }

    public String getReportMethod() {
        return reportMethod;
    }

    public void setReportMethod(String reportMethod) {
        this.reportMethod = reportMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
