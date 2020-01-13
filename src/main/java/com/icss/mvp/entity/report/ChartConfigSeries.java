package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

@TableName(value = "chart_config_series")
public class ChartConfigSeries implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    //Serics类型默认：bar=柱状，line=折线、area=区域、scatter=散点；
    // 勾3D时：bar3d=柱状3D；
    // 勾饼图时：pie=饼图，pie3d=饼图3D;
    // 两个全勾时：pie3d=饼图3D;
    private String type;
    @TableField(value = "x_field")
    private String xField;
    @TableField(value = "y_fields")
    private String yFields;
    private String axis;
    private Boolean stacked = false;
    private Boolean percent = false;
    private Boolean fill = false;
    private Integer idx = 0;
    @TableField(value = "chart_config_id")
    private Integer chartConfigId;

    @Override
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

    public Integer getChartConfigId() {
        return chartConfigId;
    }

    public void setChartConfigId(Integer chartConfigId) {
        this.chartConfigId = chartConfigId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getxField() {
        return xField;
    }

    public void setxField(String xField) {
        this.xField = xField;
    }

    public String getyFields() {
        return yFields;
    }

    public void setyFields(String yFields) {
        this.yFields = yFields;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public Boolean getStacked() {
        return stacked;
    }

    public void setStacked(Boolean stacked) {
        this.stacked = stacked;
    }

    public Boolean getFill() {
        return fill;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Boolean getPercent() {
        return percent;
    }

    public void setPercent(Boolean percent) {
        this.percent = percent;
    }
}
