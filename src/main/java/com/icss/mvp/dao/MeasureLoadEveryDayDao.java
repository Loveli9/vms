package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.IterationMeasureIndex;
import com.icss.mvp.entity.MeasureLoadEveryInfo;
import com.icss.mvp.entity.ProjectInfo;


public interface MeasureLoadEveryDayDao {
	
	/**
	 * 新增指标值
	 * @param meaInfo
	 */
	void insert(@Param("meaInfo")List<MeasureLoadEveryInfo> meaInfo);
	
	/**
	 * 获取在行项目列表
	 * @return
	 */
	List<ProjectInfo> getPorjectList();
	
	/**
	 * 获取项目配置流程类目
	 * @param proNo
	 * @return
	 */
	String getProjectConfigCategory(@Param("proNo") String proNo);
	
	/**
	 * 查询所有手工录入指标并更新到measure_history_detail
	 * @param proNo
	 * @param iteId
	 * @return
	 */
	void insertIteMeasureIndexsManual(@Param("proNo") String proNo,@Param("iteId")String iteId,
			@Param("category")String category);
	
	/**
	 * 返回手工录入指标值
	 * @param proNo
	 * @param iteId
	 * @return
	 */
//	List<IterationMeasureIndex> ManualMeasureValues(@Param("proNo") String proNo, @Param("startDate")String startDate,
//			@Param("endDate") String endDate, @Param("category")String category);
	List<IterationMeasureIndex> ManualMeasureValues(@Param("proNo") String proNo, @Param("startDate")String startDate,
													@Param("endDate") String endDate);
	/**
	 * 获取迭代内新增代码量
	 * @param iteId
	 * @param no
	 * @return
	 */
	Double queryCodeNum(@Param("proNo") String proNo,
			@Param("startDate")String startDate,@Param("endDate") String endDate);
	
	/**
	 * 查询计划执行的用例数、已执行用例数
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map<String, Integer> queryTestCaseImpAndPlanNum(String no, String startDate, String endDate);
	
	/**
	 * java/c迭代生产率
	 * @param no
	 * @param javaFiles
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Double getJavaOrcProduct(@Param("no")String no, @Param("javaFiles")String javaFiles,
			@Param("startDate")String startDate, @Param("endDate")String endDate);

	
	/**
	 * 迭代内测试用例设计相关效率
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Map<String, Long> queryCase(@Param("no")String no,
			@Param("startDate")String startDate, @Param("endDate")String endDate);

	/**
	 * 判断当前项目是否是第一次计算指标值
	 * @param no
	 * @return
	 */
	Integer isFirstCalculate(@Param("no")String no);
	
	/**
	 * 插入非配置化指标
	 * @param no
	 */
	void insertNotIterationMeasure(@Param("no")String no);
	
	/**
	 * 获取项目开发人员人数
	 * @param no
	 * @return
	 */
	Integer getDevelopPerson(@Param("no")String no);
	
	/**
	 * 获取迭代内手工用例执行数量
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Integer getManualExcute(@Param("no")String no,
			@Param("startDate")String startDate, @Param("endDate")String endDate);
	/**
	 * 获取项目开始日期
	 * @param no
	 * @return
	 */
	String getProjectStartDate(@Param("no")String no);



	/**
	 * 获取自动化脚本数
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 * xiaofeizhe
	 */
	Integer automationScriptsNumber(@Param("no")String no,
			@Param("startDate")String startDate, @Param("endDate")String endDate);
	/**
	 * 执行测试用例数
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 * xiaofeizhe
	 */
	Integer numberOfExecutionCases(@Param("proNo") String proNo,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);
	
	/**
	 * 手工执行测试用例总数
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 * xiaofeizhe
	 */
	Integer getManualUseCases(@Param("proNo") String proNo,
                              @Param("startDate") String startDate,
                              @Param("endDate") String endDate);
	
	/**
	 * 设计测试用例数
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 * xiaofeizhe
	 */
	Integer getNumberOfDesignCases(@Param("proNo") String proNo,
                                   @Param("startDate") String startDate,
                                   @Param("endDate") String endDate);
	/**
	 * 获取代码行值
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 * xiaofeizhe
	 */
	Integer getlineOfCode(@Param("proNo") String proNo,
                          @Param("startDate") String startDate,
                          @Param("endDate") String endDate);
	/**
	 * 自动化用例数
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 * xiaofeizhe
	 */
	Integer getAutomationUseCases(@Param("proNo") String proNo,
                          @Param("startDate") String startDate,
                          @Param("endDate") String endDate);

	/**
	 * 用例总数
	 * @param no
	 * @param startDate
	 * @param endDate
	 * @return
	 * xiaofeizhe
	 */
	Integer getUseCases(@Param("proNo") String proNo,
                          @Param("startDate") String startDate,
                          @Param("endDate") String endDate);

	Double getProjectWorkHours(@Param("proNo") String proNo,
                          	 @Param("startDate") String startDate,
                             @Param("endDate") String endDate);
}
