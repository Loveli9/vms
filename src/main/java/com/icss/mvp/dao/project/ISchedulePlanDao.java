package com.icss.mvp.dao.project;

import com.icss.mvp.entity.project.ProjectSchedulePlanEntity;
import com.icss.mvp.entity.project.SchedulePlanEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by up on 2019/2/14.
 */
public interface ISchedulePlanDao {

    /**
     * 获取项目里程碑计划
     * 
     * @param no 项目编号
     * @return
     */
    List<ProjectSchedulePlanEntity> getProjectSchedulePlanList(@Param("no") String no);

    /**
     * 获取项目里程碑详细计划
     * @param parameter
     * @return
     */
    List<SchedulePlanEntity> querySchedulePlan(@Param("no") String no,@Param("name") String name);
    
    /**
     * 添加项目计划
     * @param repInfo
     * @return
     */
    int insertProjectSchedulePlan(@Param("repInfo") ProjectSchedulePlanEntity repInfo);

    /**
     * 添加项目计划节点
     * @param repInfo
     * @return
     */
    int insertSchedulePlan(@Param("repInfo") SchedulePlanEntity repInfo);

    List<SchedulePlanEntity> getSchedulePlanList(@Param("projectScheduleId") String projectScheduleId);

    int delSchedulePlan(@Param("id")String id);

    int editSchedulePlan(@Param("data") SchedulePlanEntity schedulePlanEntity);

    int delProjectSchedulePlan(@Param("id") String id);

    int delProjectSchedulePlanByProjectScheduleId(@Param("id") String id);
}
