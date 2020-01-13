package com.icss.mvp.entity.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;
import com.icss.mvp.utils.NumericUtils;

/**
 * @NAME: 度量项
 * @Author: wwx550362
 * @Date: 2019/12/10 14:51
 * @Version 1.0
 */
@TableName(value = "metrics_item")
public class MetricsItem implements IEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("collected_value")
    private String collectedValue;

    @TableField("input_value")
    private String inputValue;

    @TableField("metrics_row_id")
    private Integer metricsRowId;
    @TableField("metrics_item_config_id")
    private Integer metricsItemConfigId;

    @TableField(exist = false)
    private MetricsItemConfig metricsItemConfig;

    public Integer getMetricsItemConfigId() {
        return metricsItemConfigId;
    }

    public void setMetricsItemConfigId(Integer metricsItemConfigId) {
        this.metricsItemConfigId = metricsItemConfigId;
    }

    public Integer getMetricsRowId() {
        return metricsRowId;
    }

    public void setMetricsItemConfig(MetricsItemConfig metricsItemConfig) {
        this.metricsItemConfig = metricsItemConfig;
    }

    public void setMetricsRowId(Integer metricsRowId) {
        this.metricsRowId = metricsRowId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollectedValue() {
        return collectedValue;
    }

    public void setCollectedValue(String collectedValue) {
        this.collectedValue = collectedValue;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }


    public Object getValue() {
        if (this.metricsItemConfig != null) {
            if ("int".equals(this.metricsItemConfig.getDataType())) {
                Integer collected = NumericUtils.parseIntegerMute(collectedValue);

                Integer input = NumericUtils.parseIntegerMute(inputValue);
                if (input != null) {
                    collected = collected == null ? input : collected + input;
                }

                return collected;
            } else if ("float".equals(this.metricsItemConfig.getDataType())) {
                Double collected = NumericUtils.parseDoubleMute(collectedValue);

                Double input = NumericUtils.parseDoubleMute(inputValue);
                if (input != null) {
                    collected = collected == null ? input : collected + input;
                }

                return collected;
            } else {
                return this.getInputValue();
            }
        }
        return null;
    }
}
