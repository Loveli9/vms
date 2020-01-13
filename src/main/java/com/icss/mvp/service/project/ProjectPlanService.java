package com.icss.mvp.service.project;

import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.project.IProjectReviewDao;
import com.icss.mvp.dao.project.ISchedulePlanDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.project.ProjectSchedulePlanEntity;
import com.icss.mvp.entity.project.SchedulePlanEntity;
import com.icss.mvp.util.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by up on 2019/2/14.
 */
@Service("projectPlanService")
public class ProjectPlanService {

    private static Logger logger = Logger.getLogger(ProjectPlanService.class);

    @Autowired
    private ISchedulePlanDao iSchedulePlanDao;

    @Autowired
    private IProjectListDao iProjectListDao;
    
    @Autowired
	private IProjectReviewDao projectReviewDao;

    /***
     * 查询项目计划个数和对应id等数据
     * @param no
     * @return
     */
	public List<ProjectSchedulePlanEntity> queryProjectSchedulePlan(String no){
        List<ProjectSchedulePlanEntity> projectSchedulePlanEntities= iSchedulePlanDao.getProjectSchedulePlanList(no);
        if(projectSchedulePlanEntities.size()<=0){
			// 第一次添加里程碑计划
            ProjectSchedulePlanEntity projectSchedulePlanEntity = new ProjectSchedulePlanEntity();
            projectSchedulePlanEntity.setNo(no);
            projectSchedulePlanEntity.setPlanType(1);
            projectSchedulePlanEntity.setPlanName("里程碑");
            Date newDate = new Date();
            projectSchedulePlanEntity.setCreateTime(newDate);
            projectSchedulePlanEntity.setModifyTime(newDate);
            projectSchedulePlanEntity.setIsDeleted(0);
            iSchedulePlanDao.insertProjectSchedulePlan(projectSchedulePlanEntity);

			// 添加计划默认数据，添加项目开始结束点
            ProjectDetailInfo projectDetailInfo = iProjectListDao.isExit(no);
            SchedulePlanEntity schedulePlanEntity = new SchedulePlanEntity();
            schedulePlanEntity.setCreateTime(newDate);
            schedulePlanEntity.setModifyTime(newDate);
            schedulePlanEntity.setIsDeleted(0);
            schedulePlanEntity.setProjectScheduleId(projectSchedulePlanEntity.getId());
            schedulePlanEntity.setPlannedFinishDate(projectDetailInfo.getStartDate());
            schedulePlanEntity.setName("立项");
            schedulePlanEntity.setScheduleType("0");
            schedulePlanEntity.setScheduleIcon("0");
            iSchedulePlanDao.insertSchedulePlan(schedulePlanEntity);
            schedulePlanEntity.setPlannedFinishDate(projectDetailInfo.getEndDate());
            schedulePlanEntity.setName("结项");
            schedulePlanEntity.setScheduleType("1");
            schedulePlanEntity.setScheduleIcon("0");
            iSchedulePlanDao.insertSchedulePlan(schedulePlanEntity);

            projectSchedulePlanEntities.add(projectSchedulePlanEntity);
        }

        for (ProjectSchedulePlanEntity projectSchedulePlanEntitie : projectSchedulePlanEntities) {
            List<SchedulePlanEntity> schedulePlanEntities = iSchedulePlanDao.getSchedulePlanList(projectSchedulePlanEntitie.getId());
            projectSchedulePlanEntitie.setList(schedulePlanEntities);
        }
		return projectSchedulePlanEntities;
	}

    /**
     *  查询里程碑计划节点信息
     * @param projectScheduleId
     * @return
     */
	public List<SchedulePlanEntity> querySchedulePlan(String projectScheduleId, String proNo) {

		List<SchedulePlanEntity> schedulePlanEntities = iSchedulePlanDao.getSchedulePlanList(projectScheduleId);
		String closeDate = projectReviewDao.getCloseDateByNo(proNo);
		
		if (schedulePlanEntities.size() > 0 && null != schedulePlanEntities) {
			for (SchedulePlanEntity entity : schedulePlanEntities) {
				if (("结项").equals(entity.getName())) {
					if (StringUtils.isNotBlank(closeDate)) {
						Date actualFinishDate = DateUtils.parseDate(DateUtils.SHORT_FORMAT_GENERAL, closeDate);
						entity.setActualFinishDate(actualFinishDate);
					} else {
						entity.setActualFinishDate(null);
					}
					
				}
			}
		}
		return schedulePlanEntities;
	}

    /**
     * 添加里程碑计划节点
     * @param schedulePlanEntity
     * @return
     */
    public SchedulePlanEntity addSchedulePlan(SchedulePlanEntity schedulePlanEntity) {
        Date newDate = new Date();

        schedulePlanEntity.setCreateTime(newDate);
        schedulePlanEntity.setModifyTime(newDate);
        schedulePlanEntity.setIsDeleted(0);

        iSchedulePlanDao.insertSchedulePlan(schedulePlanEntity);
	    return schedulePlanEntity;
    }

    /**
     * 添加里程碑计划
     * @param projectSchedulePlanEntity
     * @return
     */
    public ProjectSchedulePlanEntity addProjectSchedulePlan(ProjectSchedulePlanEntity projectSchedulePlanEntity) {
        Date newDate = new Date();

        projectSchedulePlanEntity.setCreateTime(newDate);
        projectSchedulePlanEntity.setModifyTime(newDate);
        projectSchedulePlanEntity.setIsDeleted(0);
        iSchedulePlanDao.insertProjectSchedulePlan(projectSchedulePlanEntity);
        return projectSchedulePlanEntity;
    }

    public int delSchedulePlan(String id) {
        return iSchedulePlanDao.delSchedulePlan(id);
    }

    public int editSchedulePlan(SchedulePlanEntity schedulePlanEntity) {
        return iSchedulePlanDao.editSchedulePlan(schedulePlanEntity);
    }

    public int delProjectSchedulePlan(String id) {
        iSchedulePlanDao.delProjectSchedulePlanByProjectScheduleId(id);
        return iSchedulePlanDao.delProjectSchedulePlan(id);
    }

}
