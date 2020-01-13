package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.request.PageRequest;

/**
 * 质量看板
 * @author fanpeng
 *
 */
public interface IQualityModelDao {
	
	public List<ProjectInfo> queryProject(@Param("projectInfo")ProjectInfo projectInfo,@Param("pageRequest")PageRequest pageRequest);
	
	public Integer searchTotalRecord(@Param("projectInfo")ProjectInfo projectInfo);
	
	public List<Map<String, String>> queryAutomaticMeasure(@Param("no")String no);
	
	public List<Map<String, String>> queryMeasure(@Param("no")String no);
	
	public Integer queryNowHuman(@Param("no")String no);
	
	public List<ParameterValueNew> searchParameter(@Param("no")String no);
	
	public List<ParameterValueNew> searchStatistical(@Param("no")String no);
	
	public List<ParameterValueNew> searchStatisticalXml(@Param("no")String no);
}
