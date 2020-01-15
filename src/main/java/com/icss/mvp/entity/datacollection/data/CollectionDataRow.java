package com.icss.mvp.entity.datacollection.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

import java.util.Date;
import java.util.List;

@TableName()
public class CollectionDataRow implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //数据开始时间
    private Date startDate;
    //数据结束时间
    private Date endDate;

    private Integer collectionDataTableId;

    @TableField(exist = false)
    private List<CollectionDataItem> dataItems;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<CollectionDataItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<CollectionDataItem> dataItems) {
        this.dataItems = dataItems;
    }

    public Integer getCollectionDataTableId() {
        return collectionDataTableId;
    }

    public void setCollectionDataTableId(Integer collectionDataTableId) {
        this.collectionDataTableId = collectionDataTableId;
    }
}
