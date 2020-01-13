package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @NAME: 度量项配置
 * @Author: wwx550362
 * @Date: 2019/12/10 14:59
 * @Version 1.0
 */
public class MetricsItemConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("field_name")
    private String fieldName;

    @TableField("field_alias")
    private String fieldAlias;

    @TableField("data_type")
    private String dataType;

    @TableField("read_only")
    private Boolean readOnly;

    @TableField("idx")
    private Integer idx = -1;

    @TableField("metrics_table_config_id")
    private Integer metricsTableConfigId;

    public Integer getMetricsTableConfigId() {
        return metricsTableConfigId;
    }

    public void setMetricsTableConfigId(Integer metricsTableConfigId) {
        this.metricsTableConfigId = metricsTableConfigId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }
}
