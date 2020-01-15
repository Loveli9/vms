package com.icss.mvp.entity.datacollection.data;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.supports.entity.IEntity;

@TableName()
public class CollectionDataItem implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String property;
    private String value;
    private Integer collectionDataRowId;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCollectionDataRowId() {
        return collectionDataRowId;
    }

    public void setCollectionDataRowId(Integer collectionDataRowId) {
        this.collectionDataRowId = collectionDataRowId;
    }
}
