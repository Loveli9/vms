package com.icss.mvp.controller.project;

import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.project.ProjectSchedulePlanEntity;
import com.icss.mvp.entity.project.SchedulePlanEntity;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.project.ProjectPlanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by up on 2019/2/14.
 */
@Controller
@RequestMapping("/projectPlan")
public class ProjectPlanController extends BaseController {

    private static Logger  logger = Logger.getLogger(ProjectPlanController.class);

    @Autowired
    private ProjectPlanService projectPlanService;

	/**
	 * 查询全部项目计划
	 * @param no
	 * @return
	 */
	@RequestMapping("/queryProjectSchedulePlan")
	@ResponseBody
	public ListResponse<ProjectSchedulePlanEntity> queryProjectSchedulePlan(String no){
		ListResponse<ProjectSchedulePlanEntity> result = new ListResponse<>();
		try {
			List<ProjectSchedulePlanEntity> data = projectPlanService.queryProjectSchedulePlan(no);
			result.setCode("0");
			result.setMessage("查询项目计划成功");
			result.setData(data);
			result.setTotalCount(data.size());
		} catch (Exception e) {
			logger.error("queryProjectSchedulePlan exception, error:" ,e);
			result.setCode("1");
			result.setMessage("查询项目计划失败");
			result.setData(new ArrayList<>());
			result.setTotalCount(0);
		}
		return result;
	}

	/**
	 * 根据id查询项目计划信息
	 * @param projectScheduleId
	 * @return
	 */
	@RequestMapping("/querySchedulePlan")
	@ResponseBody
	public ListResponse<SchedulePlanEntity> querySchedulePlan(String projectScheduleId,String proNo){
		ListResponse<SchedulePlanEntity> result = new ListResponse<>();
		try {
			List<SchedulePlanEntity> data = projectPlanService.querySchedulePlan(projectScheduleId,proNo);
			result.setCode("0");
			result.setMessage("查询项目计划节点信息成功");
			result.setData(data);
			result.setTotalCount(data.size());
		} catch (Exception e) {
			logger.error("querySchedulePlan exception, error:" ,e);
			result.setCode("1");
			result.setMessage("查询项目计划节点信息失败");
			result.setData(new ArrayList<>());
			result.setTotalCount(0);
		}
		return result;
	}

	/**
	 * 添加项目计划
	 * @param projectSchedulePlanEntity
	 * @return
	 */
	@RequestMapping("/addProjectSchedulePlan")
	@ResponseBody
	public PlainResponse<ProjectSchedulePlanEntity> addProjectSchedulePlan(@RequestBody ProjectSchedulePlanEntity projectSchedulePlanEntity){
		PlainResponse<ProjectSchedulePlanEntity> result = new PlainResponse<>();
		try {
			ProjectSchedulePlanEntity data = projectPlanService.addProjectSchedulePlan(projectSchedulePlanEntity);
			result.setCode("0");
			result.setMessage("添加项目计划成功");
			result.setData(data);
		} catch (Exception e) {
			logger.error("addProjectSchedulePlan exception, error:" ,e);
			result.setCode("1");
			result.setMessage("添加项目计划失败");
		}
		return result;
	}


	/**
	 * 添加项目计划节点
	 * @param schedulePlanEntity
	 * @return
	 */
	@RequestMapping("/addSchedulePlan")
	@ResponseBody
	public PlainResponse<SchedulePlanEntity> addSchedulePlan(@RequestBody SchedulePlanEntity schedulePlanEntity){
		PlainResponse<SchedulePlanEntity> result = new PlainResponse<>();
		try {
			SchedulePlanEntity data = projectPlanService.addSchedulePlan(schedulePlanEntity);
			result.setCode("0");
			result.setMessage("添加项目计划节点成功");
			result.setData(data);
		} catch (Exception e) {
			logger.error("addSchedulePlan exception, error:" ,e);
			result.setCode("1");
			result.setMessage("添加项目计划节点失败");
		}
		return result;
	}

	/**
	 * 编辑项目计划节点
	 * @param schedulePlanEntity
	 * @return
	 */
	@RequestMapping("/editSchedulePlan")
	@ResponseBody
	public BaseResponse editSchedulePlan(@RequestBody SchedulePlanEntity schedulePlanEntity){
		BaseResponse result = new BaseResponse();
		try {
			int ret = projectPlanService.editSchedulePlan(schedulePlanEntity);
			result.setCode("0");
			result.setMessage("编辑项目计划节点成功");
		} catch (Exception e) {
			logger.error("editSchedulePlan exception, error:" ,e);
			result.setCode("1");
			result.setMessage("编辑项目计划节点失败");
		}
		return result;
	}

	/**
	 * 删除项目计划节点
	 * @param id
	 * @return
	 */
	@RequestMapping("/delSchedulePlan")
	@ResponseBody
	public BaseResponse delSchedulePlan(String id){
		BaseResponse result = new BaseResponse();
		try {
			int ret = projectPlanService.delSchedulePlan(id);
			if(ret>0){
				result.setCode("0");
				result.setMessage("删除项目计划节点成功");
			}else{
				result.setCode("1");
				result.setMessage("删除项目计划节点失败");
			}

		} catch (Exception e) {
			logger.error("delSchedulePlan exception, error:" ,e);
			result.setCode("1");
			result.setMessage("删除项目计划节点失败");
		}
		return result;
	}

	/**
	 * 删除项目计划
	 * @param id
	 * @return
	 */
	@RequestMapping("/delProjectSchedulePlan")
	@ResponseBody
	public BaseResponse delProjectSchedulePlan(String id){
		BaseResponse result = new BaseResponse();
		try {
			int ret = projectPlanService.delProjectSchedulePlan(id);
			if(ret>0){
				result.setCode("0");
				result.setMessage("删除项目计划成功");
			}else{
				result.setCode("1");
				result.setMessage("删除项目计划失败");
			}

		} catch (Exception e) {
			logger.error("delProjectSchedulePlan exception, error:" ,e);
			result.setCode("1");
			result.setMessage("删除项目计划失败");
		}
		return result;
	}
}
