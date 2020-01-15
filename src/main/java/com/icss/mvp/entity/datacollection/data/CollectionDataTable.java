package com.icss.mvp.entity.datacollection.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icss.mvp.supports.entity.IEntity;
import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.List;

@TableName()
public class CollectionDataTable implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String projectId;
    private String interfaceType;
    private List<CollectionDataRow> rows;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    @JsonIgnore
    public List<CollectionDataRow> getRows() {
        return rows;
    }

    @JsonIgnore
    public List<CollectionDataRow> getPeriodRows() {
        List<CollectionDataRow> periods = new ArrayList<>(0);
        if (rows != null) {
            for (CollectionDataRow row : rows) {
                if (!DateUtils.isSameDay(row.getStartDate(), row.getEndDate())) {
                    periods.add(row);
                }
            }
        }
        return rows;
    }

    @JsonIgnore
    public List<CollectionDataRow> getDailyRows() {
        List<CollectionDataRow> periods = new ArrayList<>(0);
        if (rows != null) {
            for (CollectionDataRow row : rows) {
                if (DateUtils.isSameDay(row.getStartDate(), row.getEndDate())) {
                    periods.add(row);
                }
            }
        }
        return rows;
    }

    public void setRows(List<CollectionDataRow> rows) {
        this.rows = rows;
    }
}
