package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.QualityPlanModule;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.ProjectManagementProcessService;

@Controller
@RequestMapping("/projectManagement")
public class ProjectManagementProcessController {

	private final static Logger LOG = Logger.getLogger(ProjectManagementProcessController.class);

	@Autowired
	private ProjectManagementProcessService projectManagementProcessService;

	/**
	 * 结果质量指标
	 * 
	 * @return
	 */
	@RequestMapping("/resultQuality")
	@ResponseBody
	public PlainResponse<Map<String, Object>> resultQuality(String proNo) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<>();
		try {
			result.setData(projectManagementProcessService.resultQuality(proNo));
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
		return result;
	}

	/**
	 * 指标分类个数
	 */
	@RequestMapping("/categorys")
	@ResponseBody
	public List<Map<String, Object>> categorys() {
		return projectManagementProcessService.categorys();
	}

	/**
	 * 质量策划添加新模块
	 */
	@RequestMapping("/saveModule")
	@ResponseBody
	public BaseResponse saveModule(@RequestBody QualityPlanModule qualityPlanModule) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			if (queryModule(qualityPlanModule.getNo(), qualityPlanModule.getModule()) == null) {
				projectManagementProcessService.saveModule(qualityPlanModule);
				baseResponse.setMessage(
						queryModule(qualityPlanModule.getNo(), qualityPlanModule.getModule()).getModuleName());
			} else {
				baseResponse.setCode("404");
			}
		} catch (Exception e) {
			// TODO: handle exception
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

	/**
	 * 质量策划查询某模块
	 */
	@RequestMapping("/queryModule")
	@ResponseBody
	public QualityPlanModule queryModule(String no, String module) {
		return projectManagementProcessService.queryModule(no, module);
	}

	/**
	 * 质量策划查询所有模块
	 */
	@RequestMapping("/queryModules")
	@ResponseBody
	public PlainResponse<List<QualityPlanModule>> queryModules(String no) {
		PlainResponse<List<QualityPlanModule>> result = new PlainResponse<>();
		try {
			result.setData(projectManagementProcessService.queryModules(no));
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
		return result;
	}

	/**
	 * 质量策划添加新模块
	 */
	@RequestMapping("/sureDeletemodule")
	@ResponseBody
	public BaseResponse sureDeletemodule(@RequestBody QualityPlanModule qualityPlanModule) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			projectManagementProcessService.sureDeletemodule(qualityPlanModule);
		} catch (Exception e) {
			// TODO: handle exception
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

}
