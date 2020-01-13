package com.icss.mvp.controller;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.ProjectLableService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Description：流程指标配置接口
 * @author chengchenhiu
 *
 */
@RestController
@RequestMapping("/projectlable")
public class ProjectLableController {
	@Autowired
	private ProjectLableService service;

	private static Logger logger = Logger.getLogger(ProjectLableController.class);

	/**
	 * @Description: 查询当前项目配置的所有流程信息     以及项目流程所配置的指标类目
	 * @author Administrator
	 * @date 2018年5月8日
	 */
	@RequestMapping("/getProjectLabs")
	@ResponseBody
	public ListResponse<Map<String, Object>> getProjectLabs(String projNo) {
		ListResponse<Map<String, Object>> result = new ListResponse<Map<String, Object>>();
		// 给当前项目配置默认流程
		try {
			List<Map<String, Object>> list = service.getProjectAllabs(projNo);
			if(list.size()==0) {
//				service.deployDefaultLabs(projNo);
			}
			list = service.getProjectAllabs(projNo);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}

	/**
　　* @description: 获取项目配置流程信息
　　* @param projNo
　　* @return ListResponse
　　* @throws
　　* @author chengchenhui
　　* @date 2019/5/15 16:15
　　*/
	@RequestMapping("/loadLabels")
	@ResponseBody
	public PlainResponse loadLabels(String projNo) {
		PlainResponse result = new PlainResponse();
		try {
			result.setData(service.loadLabels(projNo));
		}catch (Exception e){
			logger.error("项目流程记载错误："+e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/getTeamLabs")
	@ResponseBody
	public ListResponse<Map<String, Object>> getTeamLabs(String teamId) {
		ListResponse<Map<String, Object>> result = new ListResponse<Map<String, Object>>();
		// 给当前团队配置默认流程
		try {
			List<Map<String, Object>> list = service.getTeamLabs(teamId);
			if(list.size()==0) {
				service.deployTeamDefaultLabs(teamId);
			}
			list = service.getTeamLabs(teamId);
			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}
	
	

	/**
	 * @Description: 获取流程所有类目
	 * @author Administrator
	 * @date 2018年5月9日
	 */
	@RequestMapping("/getAllLabCategory")
	@ResponseBody
	public PlainResponse<Map<String, Object>> getAllLabCategory(String proNo) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<Map<String, Object>>();
		try {
			Map<String, Object> map = service.getAllLabCategory(proNo);
			result.setData(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}
	@RequestMapping("/getAllLabCategoryByTeamId")
	@ResponseBody
	public PlainResponse<Map<String, Object>> getAllLabCategoryByTeamId(String teamId) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<Map<String, Object>>();
		try {
			Map<String, Object> map = service.getAllLabCategoryByTeamId(teamId);
			result.setData(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}
	
	/**
	* @Description: 判断当前项目是否配置了其他模板
	* @author Administrator  
	* @date 2018年6月13日  
	 */
	@RequestMapping("/deleteMeasureConfig")
	@ResponseBody
	public BaseResponse deleteMeasureConfig(String proNo,String labs) {
		BaseResponse result = service.deleteMeasureConfig(proNo,labs);
		return result;
	}
	@RequestMapping("/deleteMeasureConfigByTeamId")
	@ResponseBody
	public BaseResponse deleteMeasureConfigByTeamId(String teamId,String labs) {
		BaseResponse result = service.deleteMeasureConfigByTeamId(teamId,labs);
		return result;
	}

	/**
	 * @Description: 判断当前项目是否配置了其他模板
	 * @author Administrator
	 * @date 2018年6月13日
	 */
	@RequestMapping("/queryMeasureConfigNum")
	@ResponseBody
	public BaseResponse queryMeasureConfigNum(String proNo,String labs) {
		BaseResponse result = new BaseResponse();
		boolean flag = service.queryMeasureConfigNum(proNo,labs);
		if(flag) {
			result.setCode("success");
		}else {
			result.setCode("fail");
		}
		return result;
	}
	@RequestMapping("/queryMeasureConfigByTeamId")
	@ResponseBody
	public BaseResponse queryMeasureConfigByTeamId(String teamId,String labs) {
		BaseResponse result = new BaseResponse();
		boolean flag = service.queryMeasureConfigByTeamId(teamId,labs);
		if(flag) {
			result.setCode("success");
		}else {
			result.setCode("fail");
		}
		return result;
	}

	/**
	 * @Description: 根据项目获取迭代版本列表 (缺少关系对应关系表)
	 * @author Administrator
	 * @date 2018年5月9日
	 */
	public Map<String, Object> getVersionAndIteByProNO(String proNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// List<String> list = service.getVersionAndIteByProNO();
			// map.put("data", list);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("service.getVersionAndIteByProNO exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	/**
	 * @Description: 根据项目、版本号、迭代、流程节点加载指标信息
	 * @author Administrator
	 * @date 2018年5月8日
	 * @param labId:流程id
	 *            version:版本 ite:迭代 proNo：项目id
	 */
	@RequestMapping("/getLabMeasureByProject")
	@ResponseBody
	public ListResponse<Map<String, Object>> getLabMeasureByProject(String labId, String version, String ite,
			String proNo, String flag) {
		// Map<String, Object> map = new HashMap<String, Object>();
		ListResponse<Map<String, Object>> result = new ListResponse<Map<String, Object>>();
		try {
			List<Map<String, Object>> list = service.getLabMeasureByProject(labId, version, ite, proNo, flag);

			result.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}

	/**
	 * @Description: 获取流程指标配置页面信息
	 * @author Administrator
	 * @date 2018年5月8日
	 * @version V1.0
	 */
	@RequestMapping("/getMeasureConfigPageInfo")
	@ResponseBody
	public ListResponse<ProjectLabelMeasure> getMeasureConfigPageInfo(String selCategory, String proNo, String version,
			String ite) {
		ListResponse<ProjectLabelMeasure> result = new ListResponse<ProjectLabelMeasure>();
		try {
			List<ProjectLabelMeasure> list = service.getMeasureConfigPageInfo(selCategory, proNo, version, ite);
			result.setData(list);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}
	@RequestMapping("/getMeasureConfigPageInfoByTeamId")
	@ResponseBody
	public ListResponse<TeamLabelMeasure> getMeasureConfigPageInfoByTeamId(String selCategory, String teamId, String version,
			String ite) {
		ListResponse<TeamLabelMeasure> result = new ListResponse<TeamLabelMeasure>();
		try {
			List<TeamLabelMeasure> list = service.getMeasureConfigPageInfoByTeamId(selCategory, teamId, version, ite);
			result.setData(list);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}
	
	/**
	 * @Description: 修改流程指标配置信息
	 * @author Administrator
	 * @date 2018年5月8日
	 * @version V1.0
	 */
	@RequestMapping("/updateMeasureConfig")
	@ResponseBody
	public BaseResponse updateMeasureConfig(@RequestBody ProjectLabelMeasureList projectLabelMeasureList) {
		BaseResponse result = new BaseResponse();
		try {
			 service.updateMeasureConfig(projectLabelMeasureList);
			 result.setCode("success");
			 result.setMessage("操作成功");
		} catch (Exception e) {
			logger.error("service.updateMeasureConfig exception, error: "+e.getMessage());
			result.setMessage("操作失败");
			result.setCode("fail");
		}
		return result;
	}
	
	/**
	 * @Description: 保存流程指标配置历史信息
	 * @author Administrator
	 * @date 2018年5月8日
	 * @version V1.0
	 */
	@RequestMapping("/saveMeasureConfig")
	@ResponseBody
	public BaseResponse saveMeasureConfig(@RequestBody ProjectInfo ProjectInfo) {
		BaseResponse result = new BaseResponse();
		try {
			 service.saveMeasureConfig(ProjectInfo.getNo());
			 result.setCode("success");
			 result.setMessage("操作成功");
		} catch (Exception e) {
			logger.error("service.saveMeasureConfig exception, errro: "+e.getMessage());
			result.setMessage("操作失败");
			result.setCode("fail");
		}
		return result;
	}
	
	@RequestMapping("/updateMeasureConfigByTeamId")
	@ResponseBody
	public BaseResponse updateMeasureConfigByTeamId(@RequestBody TeamLabelMeasureList teamLabelMeasureList) {
		BaseResponse result = new BaseResponse();
		try {
			 service.updateMeasureConfigByTeamId(teamLabelMeasureList);
			 result.setCode("success");
			 result.setMessage("操作成功");
		} catch (Exception e) {
			logger.error("service.updateMeasureConfigByTeamId exception, error: "+e.getMessage());
			result.setMessage("操作失败");
			result.setCode("fail");
		}
		return result;
	}

	/**
	 * @Description: 更新指标配置实际值
	 * @author Administrator
	 * @date 2018年5月15日
	 */
	@RequestMapping("/saveActualVal")
	@ResponseBody
	public BaseResponse saveActualValMeasureConfig(String id, String actualVal) {
		BaseResponse result = new BaseResponse();
		try {
			service.saveActualValMeasureConfig(id, actualVal);
			result.setMessage("保存成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return result;
	}

	/**
	 * @Description: 记录每月的指标记录实际值
	 * @author chengchenuhi
	 * @date 2018年5月21日
	 */
	@RequestMapping("/updateMeasureMonth")
	@ResponseBody
	public void updateMeasureMonth(String proNo) {
		try {
			service.updateMeasureMonth(proNo);
		} catch (Exception e) {
			logger.info("service.updateMeasureMonth exception, error: "+e.getMessage());
		}
	}

	/**
	 * TMSS自动抓取时将手动输入的指标改为自动获取，手工输入时将自动采集的指标改为手动输入
	 */
	@RequestMapping("/changemodel")
	@ResponseBody
	public void changemodel(String labId, String proNo, String flag) {
		try {
			service.changemodel(labId, proNo, flag);
		} catch (Exception e) {
			logger.info("service.changemodel exception, error: "+e.getMessage());
		}
	}
	
	/**
	 * 获取配置指标及指标迭代值
	 */
	@RequestMapping("/queryMetricIndex")
	@ResponseBody
	public ListResponse<LinkedHashMap<String, Object>> queryMetricIndex(String plId,String category,String proNo) {
		ListResponse<LinkedHashMap<String, Object>> result = new ListResponse<>();
		List<LinkedHashMap<String, Object>> map = new ArrayList<LinkedHashMap<String, Object>>();
		try {
			map = service.queryMetricIndex(plId,category,proNo);
			result.setData(map);
		} catch (Exception e) {
			logger.info("service.queryMetricIndex exception, error: "+e.getMessage());
		}
		return result;
	}
	
	
	/**
　　* @description: 更新measure_range
　　* @param mr
　　* @return BaseResponse
　　* @throws
　　* @author chengchenhui
　　* @date 2019/6/19 17:52
　　*/
	@RequestMapping("/update")
	@ResponseBody
	public BaseResponse update(@RequestBody MeasureRange mr){
		BaseResponse  result = new  BaseResponse();
		try {
			service.updateMeasureRange(mr);
		} catch (Exception e) {
			logger.error("更新指标上下限失败"+e.getMessage());
		}
		return result;
	}


//	@RequestMapping("/updateByTeamId")
//	@ResponseBody
//	public void updateByTeamId(@RequestBody TeamMeasureRange mr){
//		try {
//			service.updateMeasureRangeByTeamId(mr);
//		} catch (Exception e) {
//			logger.error("更新指标上下限失败"+e.getMessage());
//		}
//	}

}
