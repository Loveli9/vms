package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.MonthMeasure;

public interface MonthMeasureDao {
	
	/**
	 * 查询当前指标是否是第一次计算
	 */
	List<MonthMeasure> isFirstCalculate(@Param("lmcId") String lmcId,@Param("yearMonth") String yearMonth);
	/**
	* @Description:获取所有月份指标实际值  
	* @author Administrator  
	* @date 2018年5月25日  
	 */
	List<MonthMeasure> geMonthtMeasureValue(@Param("labMeasureIds")String labMeasureIds);
	/**
	 * @Description:获取所有月份指标实际值 ，上个月的信息
	 * @author Administrator  
	 * @date 2018年5月25日  
	 */
	List<Map<String,Object>> geMonthtMeasureValueLastMon(@Param("labMeasureIds")String labMeasureIds);
	/**
	 * @Description:获取所有月份指标实际值,每月值求和 
	 * @author Administrator  
	 * @date 2018年5月25日  
	 */
	List<Map<String,Object>> geMonthtMeasureValueSum(@Param("labMeasureIds")String labMeasureIds);
	/**
	* @Description: 保存指标值
	* @author Administrator  
	* @date 2018年5月25日  
	* @version V1.0
	 */
	Integer saveMonthMeasure(@Param("monthMeasure") MonthMeasure monthMeasure);
	
	/**
	* @Description: 更新当前指标实际值
	* @author Administrator  
	* @date 2018年5月25日  
	 */
	Integer updateMonthMeasures(@Param("monthMeasure") MonthMeasure monthMeasure);
	
/*	*//**
	* @Description: 查询该月份代码量
	* @author Administrator  
	* @date 2018年5月28日  
	 *//*
	String getCodeNumByMonth(@Param("yearMonth") String yearMonth,@Param("account") String account,@Param("proNo") String proNo);
	
	String getCodeNumByMonthWX(@Param("yearMonth") String yearMonth,@Param("account") String account,@Param("proNo") String proNo);
*/
	
	/**
	* @Description: 查询该月份代码量
	* @author Administrator  
	* @date 2018年5月28日  
	 */
	Double getCodeNumByMonth(@Param("yearMonth") String yearMonth,@Param("proNo") String proNo);
	
	Double getCodeNumByMonthWX(@Param("yearMonth") String yearMonth,@Param("proNo") String proNo);
	
	/**
	* @Description: 查询当前项目所有月份投入工作占比  
	* @author Administrator  
	* @date 2018年5月28日  
	 */
	String getPercentByYearMonth(@Param("yearMonth") String yearMonth,@Param("proNo") String proNo,@Param("type") String type);
	
	/**
	* @Description: 查询项目投入月总工时
	* @author Administrator  
	* @date 2018年5月28日  
	 */
	String getHoursByYearMonth(@Param("yearMonth") String yearMonth,@Param("account") String account);
	
	/**
	* @Description: 查询项目月测试用例总数
	* @author Administrator  
	* @date 2018年5月28日  
	 */
	String getylNumByYearMonth(@Param("yearMonth") String yearMonth,@Param("proNo") String proNo);
	/**
	* @Description: 获取项目配置的所有指标 
	* @author Administrator  
	* @date 2018年5月28日  
	 */
	List<Map<String, Object>> getMeasuresByProject(@Param("proNo") String proNo);
	/**
	 * @Description: 获取项目配置的所有指标 
	 * @author Administrator  
	 * @date 2018年5月28日  
	 */
	List<Map<String, Object>> getMeasuresByProjectAndMeasure(@Param("proNos") String proNos,@Param("ids") String ids);
	
	
	
	/**
	 * 执行用例数
	 */
	public Integer startTestCaseNum(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 总用例数
	 */
	public Integer testCaseCount(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 自动化用例数
	 */
	public Integer autoTestCaseNum(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 问题单总数 关闭类型仅为（Closure After Correction）
	 */
	public Integer dtsCount(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 已解决问题总数
	 */
	public Integer solvedDtsCount(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 手工执行测试用例总数
	 */
	public Integer manualStartTestCaseCount(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 自动化用例执行成功数
	 */
	public Integer autoTestCaseStartPassedCount(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 新增代码量
	 */
	public Integer newLoc(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	public Integer newLocWx(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 新增测试用例数
	 */
	public Integer newTestCaseNum(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	/**
	 * 测试自动化代码量
	 */
	public Integer testCaseAutoLoc(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	public Integer testCaseAutoLocWx(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	
	
	/**
	 * 投入工作量百分比
	 */
	public String solveHours(@Param("type") String type,@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	/**
	 * 项目月版本数
	 */
	Integer getVersionNum(@Param("proNo") String proNo,@Param("yearMonth") String yearMonth);
	/*
	 * 查询c++代码量
	 */
	Double queryCodeNumByc(@Param("codeType")String codeType, @Param("authors")String authors, @Param("yearMonth") String yearMonth);
	Double queryCodeNumBycWX(@Param("codeType")String codeType,@Param("authors")String authors, @Param("yearMonth") String yearMonth);
	
	/*
	 * 查询当月项目投入工时
	 */
	Double workTimes(@Param("HWid")String HWid, @Param("yearMonth")String yearMonth);
}
