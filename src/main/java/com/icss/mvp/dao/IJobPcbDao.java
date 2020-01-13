package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ParameterInfoNew;
import com.icss.mvp.entity.SvnLogFileModifyNum;

public interface IJobPcbDao{
	
	/**
	 * 总用例数
	 * @param projNo
	 * @return
	 */
	List<Map<String, Object>> testCaseCount(@Param("proNos")String proNos,@Param("date")String date);
	/**
	 * 单个项目的总用例数
	 * @param projNo
	 * @return
	 */
	Integer countCaseByIteration(@Param("no")String no,@Param("startDate")String startDate, @Param("endDate")String endDate);
	/**
	 * 总自动化用例数
	 * @param projNo
	 * @return
	 */
	List<Map<String, Object>> autoTestCaseNum(@Param("proNos")String proNos,@Param("date")String date);
	/**
	 * 单个项目总自动化用例数
	 * @param projNo
	 * @return
	 */
	Integer numCaseByIteration(@Param("no")String no,@Param("startDate")String startDate, @Param("endDate")String endDate);
	/**
	 * 代码检视发现缺陷个数（一般+严重）
	 * @param 
	 * @return
	 */
	Integer codecheckDefectNum(@Param("date")String date,@Param("no")String no);
	/**
	 * 根据项目，id，获取当前月前一月的参数
	 * @param proNo
	 * @param parameterIds
	 * @return
	 */
	List<Map<String, Object>> getProjectParameterValue(@Param("proNo")String proNo,@Param("parameterIds")String parameterIds);
	/**
	 * 根据项目，id，获取每月的参数
	 * @param proNo
	 * @param parameterIds
	 * @return
	 */
	List<Map<String, Object>> getParameterValueGroupMouth(@Param("proNos")String proNos,@Param("parameterId")String parameterId);
	
	/**
	 * 根据项目，id，获取每月的参数
	 */
	List<Map<String, Object>> getParameterValueGroup(@Param("proNos")String proNos,@Param("parameterId")String parameterId,
			@Param("startDate")String startDate, @Param("endDate")String endDate);
	/**
	 * 根据项目，id，获取每月的参数
	 * @param proNo
	 * @param parameterIds
	 * @return
	 */
	List<Map<String, Object>> getParameterValueGroupMouthByNo(@Param("proNo")String proNo,@Param("parameterId")String parameterId);
	
	/**
	 * 获取参数信息
	 * @param id
	 * @return
	 */
	Map<String, Object> getParameterInfo(@Param("id")String id);
	/**
	 * 获取参数信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getParameterInfoIds(@Param("ids")String ids);
	/*
	 * 获取java端到端代码量
	 */
	//Double queryCodeDTD(@Param("pList")String pList,@Param("month")String month,@Param("CodeType")String CodeType,@Param("proNo")String proNo);
	
	/*
	 * 获取java/c++端到端代码量
	 */
	Double queryCodeDTD(@Param("month")String month,@Param("CodeType")String CodeType,@Param("proNo")String proNo);
	/**
	 * 根据项目，id，开始日期，结束日期，获取每月的参数
	 * @param proNo
	 * @param parameterIds
	 * @return
	 */
	List<Map<String, Object>> getParameterValueByNoId(@Param("proNo")String proNo, @Param("parameterId")String parameterId, @Param("startDate")String startDate, @Param("endDate")String endDate);

	Map<String, Object> getMeasureInfo(@Param("id")String id);

	List<Map<String, Object>> getPcbValuesByIteration(@Param("proNos")String proNos,@Param("measureId")String measureId);

	List<Map<String, Object>> getPcbValuesByItera(@Param("proNos")String proNos,@Param("measureId")String measureId,
			@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	List<Map<String, Object>> getProjectMeasure(@Param("proNo")String no,@Param("measureIds") String measureIds);
	
	// 根据Id修改指标上下限、目标值
	Integer updateParameterInfoById(ParameterInfoNew parameterInfoNew);
}