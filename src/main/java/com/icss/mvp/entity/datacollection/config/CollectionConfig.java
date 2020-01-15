package com.icss.mvp.entity.datacollection.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.supports.entity.IEntity;

import java.util.ArrayList;
import java.util.List;

@TableName(value = "collection_config")
public class CollectionConfig implements IEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String projectId;

    @TableField(exist = false)
    private ProjectInfo project;

    @TableField(exist = false)
    private List<CollectionGroup> collectionGroups = new ArrayList<>(0);


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

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public List<CollectionGroup> getCollectionGroups() {
        return collectionGroups;
    }

    public void setCollectionGroups(List<CollectionGroup> collectionGroups) {
        this.collectionGroups = collectionGroups;
    }
}
