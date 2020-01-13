package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.MeasureInfo;
import com.icss.mvp.entity.MonthSummary;
import com.icss.mvp.entity.OrganizeMgmer;
import com.icss.mvp.entity.ProjProductivity;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectSummary;

public interface IBuOverviewDao
{
	public List<OrganizeMgmer> getBuOpts();

	public List<ProjectSummary> getProjectSummaries(@Param("bu") String buName);

	public List<MonthSummary> getMonthSummary(@Param("bu") String buName);

	public List<ProjProductivity> getProjProductity(String buName);

	public List<MeasureInfo> getProjDetail(Map<String, Object> map);
	
//	public List<MeasureInfo> getProjDetail(@Param("proj") ProjectInfo proj);

//	public List<MeasureInfo> getProjDetail(@Param("buName") String buName,
//			@Param("sort") String sort, @Param("order") String order,
//			@Param("proj") ProjProductivity proj);
	
	public List<ProjProductivity> getProjIndicator(
			@Param("buName") String buName, @Param("col") String col);

	public List<ProjProductivity> getSpecProjChartData(@Param("no") String no);

	public List<ProjectInfo> getProjCategory(@Param("bu") String bu,
			@Param("pdu") String pdu);

	public List<String> getBus();

	public List<String> getPus(String bu);

	public List<MeasureInfo> getMeasureResult(@Param("projNo") String no,
			@Param("bigType") String bigType, @Param("smallType")String smallType);
	
	public List<MeasureInfo> getProjOverview(String no);
	
	public List<MeasureInfo> getGridTitles();
	
	public List<String> getAreas();
	
	public List<Map<String, Object>> getZhongruanYWX();
	
	public List<Map<String, Object>> getZhongruanSYB(Map<String, Object> map);

	public List<Map<String, Object>> getZhongruanJFB(Map<String, Object> map);
	
	public List<Map<String, Object>> getHWCPX();
	
	public List<Map<String, Object>> getZCPX(Map<String, Object> map);
	
	public List<Map<String, Object>> getPDUorSPDT(Map<String, Object> map);

    String getAuthOpDepartment(@Param("username") String username);

    List<Map<String, Object>> getHwdeptBylevel(@Param("level") String level,@Param("list") Set<String> deptIds);

	List<Object> getBm(Map<String, Object> map);

	public List<String> getDoubleCycleNO(Map<String, Object> map);
}
