package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

@TableName(value = "chart_config_axis")
public class ChartConfigAxis implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 类型 X轴默认 category
     * Y轴为 numeric
     */
    private String type;

    /**
     * 图的显示位置
     */
    private String position;

    /**
     * 字段，配置页面不显示
     */
    private String fields;

    private String title;

    private Boolean percent = false;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getPercent() {
        return percent;
    }

    public void setPercent(Boolean percent) {
        this.percent = percent;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }
}
