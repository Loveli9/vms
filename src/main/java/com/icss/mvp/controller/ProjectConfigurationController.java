package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.CollectionJobInfo;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ProjectDataSourceInfo;
import com.icss.mvp.entity.RepositoryInfo;
import com.icss.mvp.service.ProjectConfigurationService;

/**
 * 
 * @author liuwenyan
 * @see 项目度量信息配置
 * 
 */
@RestController
@RequestMapping("/project")
public class ProjectConfigurationController {
	@Autowired
	private ProjectConfigurationService projConfService;

//	@RequestMapping("/queryprojinfo")
//	@ResponseBody
//	public Map<String, Object> queryProjInfo(String projNo) {
//
//		return projConfService.queryProjInfo(projNo);
//
//	}

//	@RequestMapping("/queryprojcmfords")
//	@ResponseBody
//	public Map<String, Object> queryProjCMForDS() {
//
//		return projConfService.queryProjCMForDS();
//	}

	@RequestMapping("/queryprojdsinfo")
	@ResponseBody
	public Map<String, Object> queryProjDataSrcInfo(String projNo, Page page) {

		return projConfService.queryProjDataSrcInfo(projNo, page);
	}

	@RequestMapping("/insertdatasource")
	public Map<String, Object> insertDataSourceForProj(ProjectDataSourceInfo projDSInfo) {

		return projConfService.insertDataSourceForProj(projDSInfo);

	}

	@RequestMapping("/udpatedatasource")
	@ResponseBody
	public Map<String, Object> updateDataSourceForProj(ProjectDataSourceInfo projDSInfo) {

		return projConfService.updateDataSourceForProj(projDSInfo);

	}

	@RequestMapping("/deldatasource")
	@ResponseBody
	public Map<String, Object> delDataSourceForProj(ProjectDataSourceInfo projDSInfo) {
		return projConfService.delDataSourceForProj(projDSInfo);
	}

	@RequestMapping("/searchprojdsinfo")
	@ResponseBody
	public ProjectDataSourceInfo searchProjDataSrcInfo(String no) {

		return projConfService.searchProjDataSrcInfo(no);
	}

	@RequestMapping("/searchConfigInfo")
	@ResponseBody
	public List<RepositoryInfo> searchConfigInfo(String no) {
		return projConfService.searchConfigInfo(no);
	}

	@RequestMapping("/searchConfigInfoOtherAcc")
	@ResponseBody
	public List<RepositoryInfo> searchConfigInfoOtherAcc(String no) {
		return projConfService.searchConfigInfoOtherAcc(no);
	}
	//获取新项目的采集配置
	/*@RequestMapping("/getConfigJob")
	@ResponseBody
	public List<CollectionJobInfo> getConfigJob(String projectId) {
		return projConfService.searchConfigJob(projectId);
	}*/
	@RequestMapping("/searchConfigJob")
	@ResponseBody
	public List<CollectionJobInfo> searchConfigJob(String projectId) {
		return projConfService.searchConfigJob(projectId);
	}
	/*@RequestMapping("/searchConfigJobByTeam")
	@ResponseBody
	public List<CollectionJobInfo> searchConfigJobByTeam(String projectId) {
		return projConfService.searchConfigJobByTeam(projectId);
	}*/
	@RequestMapping("/getProjNameByNo")
	@ResponseBody
	public String getProjNameByNo(String projectId) {
		return projConfService.getProjNameByNo(projectId);
	}
	
	@RequestMapping("/searchConfigJobById")
	@ResponseBody
	public List<Map<String, Object>> searchConfigJobById(String id) {
		return projConfService.searchConfigJobById(id);
	}
	
	@RequestMapping("/saveConfigJob")
	@ResponseBody
	public Map<String,Object> saveConfigJob(String id,String no,String ids,String selectedIds,String types,String name) {
		return projConfService.saveConfigJob(id,no,ids,selectedIds,types,name);
	}
	
	@RequestMapping("/searchConfigInfoByJob")
	@ResponseBody
	public List<RepositoryInfo> searchConfigInfoByJob(String ids) {
		return projConfService.searchConfigInfoByJob(ids);
	}
	@RequestMapping("/searchConfigReNum")
	@ResponseBody
	public void searchConfigReNum(String id,String num,String progressLast) {
		projConfService.searchConfigReNum(id,num,progressLast);
	}
	@RequestMapping("/copyJob")
	@ResponseBody
	public Map<String, Object> copyJob(String id,String ids) {
		return projConfService.copyJob(id,ids);
	}
	@RequestMapping("/delJob")
	@ResponseBody
	public Map<String, Object> delJob(String id,String ids,String no) {
		return projConfService.delJob(id,ids,no);
	}
}
