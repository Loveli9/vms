package com.icss.mvp.dao.project;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.project.ProjectBaseEntity;

/**
 * Created by Ray on 2019/1/11.
 */
public interface IProjectDao {

    /**
     * 获取项目的起止时间
     *
     * @param projectId 项目编号
     * @return
     */
    Map getProjectTimescale(@Param("projectId") String projectId);

    /**
     * 获取项目基本信息
     * 
     * @param projectId 项目编号
     * @return
     */
    List<ProjectBaseEntity> getProjectList(@Param("projectIds") Set<String> projectId);
}
