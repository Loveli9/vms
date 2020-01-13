package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.QualityStatistical;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.service.ProjectLableService;
import com.icss.mvp.service.QualityModelService;
import com.icss.mvp.util.CookieUtils;


@Controller
@RequestMapping("/qualityModel")
public class QualityModelController {
	
	private static Logger logger = Logger.getLogger(QualityModelController.class);

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ProjectLableService service;
	
	@Resource
	private QualityModelService qualityModelService;

	/**
	 * 分页查询项目
	 * @param projectInfo
	 * @param pageRequest
	 * @return
	 */
	@RequestMapping("/queryNO")
	@ResponseBody
	public PageResponse<Map<String, Object>> queryProject(@ModelAttribute ProjectInfo projectInfo, PageRequest pageRequest) {
		PageResponse<Map<String, Object>> result = new PageResponse<>();
		try {
			String[] measureIds = new String[]{"144","145","146"};
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectInfo.setProjectState(URLDecoder.decode(projectInfo.getProjectState(), "UTF-8"));
			result = qualityModelService.queryProject(projectInfo, username, measureIds, pageRequest);
			result.setMessage("返回成功");
			result.setCode("success");
		} catch (Exception e) {
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	
	/**
	 * 查询总条数
	 * @param projectInfo
	 * @return
	 */
	@RequestMapping("/searchTotalRecord")
	@ResponseBody
	public Integer searchTotalRecord(ProjectInfo projectInfo)
	{
		return qualityModelService.searchTotalRecord(projectInfo);
	}
	
	/**
	 * 查询历史项目
	 * @param projectInfo
	 * @param no
	 * @param projectState
	 * @return
	 */
	@RequestMapping("/queryOldProjectInfo")
	@ResponseBody
	public List<QualityStatistical> queryOldProjectInfo(@ModelAttribute ProjectInfo projectInfo,String no,String projectState)
	{
		List<QualityStatistical> result = new ArrayList();
		try{
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectInfo.setProjectState(URLDecoder.decode(projectInfo.getProjectState(), "UTF-8"));
			result = qualityModelService.queryOldProjectInfo(projectInfo,username,no,projectState);
		}
		catch(UnsupportedEncodingException e){
			logger.error("qualityModelService.queryOldProjectInfo exception, error: "+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 质量统计
	 * @param projectInfo
	 * @return
	 */
	@RequestMapping("/queryTable")
	@ResponseBody
	public PageResponse<QualityStatistical> queryTable(@ModelAttribute ProjectInfo projectInfo,PageRequest pageRequest)
	{
		PageResponse<QualityStatistical> result = new PageResponse<>();
		try{
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectInfo.setProjectState(URLDecoder.decode(projectInfo.getProjectState(), "UTF-8"));
			result = qualityModelService.queryTable(projectInfo,username,pageRequest);
		}
		catch(UnsupportedEncodingException e){
			result.setErrorMessage("", e.getMessage());
			logger.error("qualityModelService.queryTable exception, error: "+ e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/queryProjectByTeam")
	@ResponseBody
	public PageResponse<ProjectInfo> queryProjectByTeam(String projectId, PageRequest pageRequest)
	{
		PageResponse<ProjectInfo> result = null;
		try {
			result = new PageResponse<>();
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			result = qualityModelService.queryProjectByTeam(projectId,username,pageRequest);
		} catch (Exception e) {
			logger.error("qualityModelService.queryProjectByTeam exception, error: "+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 定时任务效率看板数据存入数据库
	 * 或者刷新
	 */
	@RequestMapping("/saveEfficiency")
	@ResponseBody
	public void saveEfficiency() {
		qualityModelService.saveEfficiency();
	};
	
	@RequestMapping("/queryEfficiency")
	@ResponseBody
	public List<ParameterValueNew> queryEfficiency(String no){
		return qualityModelService.queryEfficiency(no);
	}
	
	/**
	 * 定时任务质量看板数据存入数据库
	 * 或者刷新
	 */
	@RequestMapping("/saveStatistical")
	@ResponseBody
	public void saveStatistical() {
		qualityModelService.saveStatistical();
	}
}

