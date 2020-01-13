package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectCommentsListInfo;
import com.icss.mvp.entity.ProjectMaturityAssessment;
import com.icss.mvp.entity.ProjectStatus;
import com.icss.mvp.entity.ProjectStatusLighting;

public interface IProjectMaturityAssessmentDAO {
	public String getProjectNo(@Param("proName")String proName);
	
	public String getProjectName(@Param("proNo")String proNo);
	
	//public void deleteProjectMaturityAssessment();
	
	public void replaceProjectMaturityAssessment(@Param("pmas")List<ProjectMaturityAssessment> pmas);
	
	public List<Map<String, Object>> initMaturityAssessment(Map<String, Object> map);
	
	public List<Map<String, Object>> assessmentChart(Map<String, Object> map);
	
	public List<Map<String, Object>> DepartmentData(Map<String, Object> param);
	
	public Map<String, Object> everyScore(@Param("proName")String proName);
	
	public Map<String, Object> comments(@Param("proName")String proName);
	
	public List<ProjectCommentsListInfo> searchCommentsList(@Param("proNo")String proNo);
	
	public List<ProjectCommentsListInfo> searchCommentsListById(@Param("id")Integer id);
	
	public ProjectMaturityAssessment export361(@Param("no")String no);
	
	public void replaceCommentsList(@Param("data")ProjectCommentsListInfo projectCommentsListInfo);
	public void replaceCommentsLists(@Param("pmas")List<ProjectCommentsListInfo> projectCommentsListInfo);

	/*
	 * 分类查询项目列表
	 */
	public List<Map<String, Object>> loadTabsByTypes(@Param("strSql")String strSql );
	
	public List<Map<String, Object>> loadProInfoByColor(@Param("strSql")String strSql);

	public void deleteProjectMaturityComments(@Param("data")Map<String,Object> map);

	public List<Map<String, Object>> selectProjectMaturityAssessment(@Param("proNo")String proNo,@Param("start")String start1,@Param("end")String end1);

	public List<Map<String, Object>> hwDepartment(Map<String, Object> params);

	public List<Map<String, Object>> zrDepartment(Map<String, Object> params);

	public List<ProjectStatus> projectOverview(Map<String, Object> map);

	public void assessUpdata(@Param("id") String id,@Param("comment") String comment);

	public void updateProjectAssessState(@Param("id") String id,@Param("state") String state);

}