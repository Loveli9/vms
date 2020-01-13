package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.util.List;

@TableName(value = "report_kpi_config")
public class ReportKpiConfig implements IEntity {
    public static final String TYPE_PROJECT = "1";
    public static final String TYPE_PERSONNEL = "2";
    public static final String TYPE_DEMAND = "3";
    public static final String TYPE_PROJECT_BASIC = "10";
    public static final String TYPE_ITERATION_BASIC = "9";
    public static final String TYPE_PERSONNEL_BASIC = "8";
    public static final String TYPE_DEMAND_BASIC = "7";
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //管理分组：如质量，效率等
    @TableField(value = "manage_group")
    private String manageGroup;
    //字段名称
    @TableField(value = "kpi_name")
    private String kpiName;
    //计算表达式
    @TableField(value = "expression")
    private String expression;
    //数据类型
    @TableField(value = "data_type")
    private String dataType = "string";
    //日期格式
    //格式化表达式，与formatter互斥
    // 根据表达式使用不同的格式化方式，比如yyyy-MM-dd使用日期格式化，###.00使用数字格式化

    @TableField(value = "format_pattern")
    private String formatPattern;
    //格式化方式
    //格式化方法，与formatPattern互斥，与预置的格式化函数进行绑定，并可指定表达式
    //例如：scale(2,'ROUND_HALF_UP') 表示保留两位小数四舍五入，具体参见BigDecimal
    @TableField(value = "formatter")
    private String formatter;
    //指标类型：项目，人员，需求
    @TableField(value = "kpi_type")
    private String kpiType;
    ///描述
    @TableField(value = "description")
    private String description;
    //指标列宽
    @TableField(value = "width")
    private Double width;
    @TableField(value = "read_only")
    private Boolean readOnly;
    //亮灯规则相关
    @TableField(value = "max_value")
    private Double maxValue;
    @TableField(value = "min_value")
    private Double minValue;
    @TableField(value = "target_value")
    private Double targetValue;
    //上下限优先
    @TableField(value = "light_up_rule")
    private String lightUpRule;

    @TableField(exist = false)
    private List<MetricsItemConfig> metricsItemConfigs;

    public List<MetricsItemConfig> getMetricsItemConfigs() {
        return metricsItemConfigs;
    }

    public void setMetricsItemConfigs(List<MetricsItemConfig> metricsItemConfigs) {
        this.metricsItemConfigs = metricsItemConfigs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFormatPattern() {
        return formatPattern;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getKpiType() {
        return kpiType;
    }

    public void setKpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public String getLightUpRule() {
        return lightUpRule;
    }

    public void setLightUpRule(String lightUpRule) {
        this.lightUpRule = lightUpRule;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public String getManageGroup() {
        return manageGroup;
    }

    public void setManageGroup(String manageGroup) {
        this.manageGroup = manageGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ReportKpiConfig{" +
                "id=" + id +
                ", manageGroup='" + manageGroup + '\'' +
                ", kpiName='" + kpiName + '\'' +
                ", expression='" + expression + '\'' +
                ", dataType='" + dataType + '\'' +
                ", formatPattern='" + formatPattern + '\'' +
                ", formatter='" + formatter + '\'' +
                ", kpiType='" + kpiType + '\'' +
                ", description='" + description + '\'' +
                ", width=" + width +
                ", readOnly=" + readOnly +
                ", maxValue=" + maxValue +
                ", minValue=" + minValue +
                ", targetValue=" + targetValue +
                ", lightUpRule='" + lightUpRule + '\'' +
                ", metricsItemConfigs=" + metricsItemConfigs +
                '}';
    }
}
