package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@TableName(value = "chart_config")
public class ChartConfig implements IEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private Double height;
    private Integer width;
    private Boolean pie = false;
    private Boolean stereoscopic = false;
    private String theme = "default";
    private String description;
    private Integer idx = 0;

    @TableField(value = "report_config_id")
    private Integer reportConfigId;

    @TableField(exist = false)
    private List<ChartConfigAxis> axes = new ArrayList<ChartConfigAxis>(0);

    @TableField(exist = false)
    private List<ChartConfigSeries> series = new ArrayList<ChartConfigSeries>(0);

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportConfigId() {
        return reportConfigId;
    }

    public void setReportConfigId(Integer reportConfigId) {
        this.reportConfigId = reportConfigId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getPie() {
        return pie;
    }

    public void setPie(Boolean pie) {
        this.pie = pie;
    }

    public Boolean getStereoscopic() {
        return stereoscopic;
    }

    public void setStereoscopic(Boolean stereoscopic) {
        this.stereoscopic = stereoscopic;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public List<ChartConfigAxis> getAxes() {
        return axes;
    }

    public void setAxes(List<ChartConfigAxis> axes) {
        this.axes = axes;
    }

    public List<ChartConfigSeries> getSeries() {
        return series;
    }

    public void setSeries(List<ChartConfigSeries> series) {
        this.series = series;
    }
}
