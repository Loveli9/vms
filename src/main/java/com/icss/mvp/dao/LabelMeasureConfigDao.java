package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.LabelMeasureConfig;

/**
 * 指标配置表表对应的MyBatis SQL XML配置文件的映射接口
 * 
 * @author Administrator
 * @time 2018-5-10 15:32:49
 */
public interface LabelMeasureConfigDao {
	/**
	 * 在数据库中新插入一条指定的指标配置表记录
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public Integer insertLabelMeasureConfig(LabelMeasureConfig labelMeasureConfig);

	/**
	 * 在数据库中修改此条指标配置表记录
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public Integer updateLabelMeasureConfig(LabelMeasureConfig labelMeasureConfig);

	/**
	 * 在数据库中通过主键id查出指定的指标配置表记录
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public LabelMeasureConfig getLabelMeasureConfigById(String id);

	/**
	 * 在数据库中通过主键id删除指定指标配置表记录
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public Integer deleteLabelMeasureConfigById(String id);

	/**
	 * 在数据库中通过主键id批量删除指定指标配置表记录
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public Integer deleteLabelMeasureConfigByIdList(List<String> list);

	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize） 和其他条件参数得到指定条数的指标配置表记录
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public List<LabelMeasureConfig> getLabelMeasureConfigForPage(Map<String, Object> map);

	/**
	 * 在数据库中通过Map中条件参数得到指定指标配置表记录的总条数
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public Integer getLabelMeasureConfigCount(Map<String, Object> map);

	/**
	 * 在数据库中查出所有指标配置表记录
	 * 
	 * @author Administrator
	 * @time 2018-5-10 15:32:49
	 */
	public List<LabelMeasureConfig> getAllLabelMeasureConfig();

	/**
	 * 执行用例数
	 */
	public Integer startTestCaseNum(@Param("proNo") String proNo);

	/**
	 * 总用例数
	 */
	public Integer testCaseCount(@Param("proNo") String proNo);

	/**
	 * 自动化用例数
	 */
	public Integer autoTestCaseNum(@Param("proNo") String proNo);

	/**
	 * 问题单总数 关闭类型仅为（Closure After Correction）
	 */
	public Integer dtsCount(@Param("proNo") String proNo);

	/**
	 * 已解决问题总数
	 */
	public Integer solvedDtsCount(@Param("proNo") String proNo);

	/**
	 * 手工执行测试用例总数
	 */
	public Integer manualStartTestCaseCount(@Param("proNo") String proNo);

	/**
	 * 自动化用例执行成功数
	 */
	public Integer autoTestCaseStartPassedCount(@Param("proNo") String proNo);

	/**
	 * 新增代码量
	 */
	public Integer newLoc(@Param("proNo") String proNo);

	/**
	 * 新增代码量
	 */
	public Integer newLocWx(@Param("proNo") String proNo);

	/**
	 * 新增测试用例数
	 */
	public Integer newTestCaseNum(@Param("proNo") String proNo);

	/**
	 * 测试自动化代码量
	 */
	public Integer testCaseAutoLoc(@Param("proNo") String proNo);

	/**
	 * 测试自动化代码量
	 */
	public Integer testCaseAutoLocWx(@Param("proNo") String proNo);

	/**
	 * 项目组所有员工华为工号
	 */
	public List<String> allPeople(@Param("proNo") String proNo);

	/**
	 * 投入工作量百分比
	 */
	public String solveHours(@Param("type") String type, @Param("proNo") String proNo);

	/**
	 * 项目组员工工时
	 */
	public String workTimes(@Param("HWid") String HWid);

	/**
	 * TMSS自动抓取时将手动输入的指标改为自动采集
	 */
	public void changeToAuto();

	/**
	 * 手工输入时将自动采集的指标改为手动输入
	 */
	public void changeToManual();
	
	/*
	 * 当月新增修改代码量
	 */
	public Integer dynewLoc(@Param("proNo") String proNo);
	public Integer dynewLocWx(@Param("proNo") String proNo);
	
	/*
	 * 查询c++代码量
	 */
	public Double queryCodeNumByc(@Param("codeType") String codeType, @Param("authors") String authors);
	/*
	 * 查询c++代码量
	 */
	public Double queryCodeNumBycWX(@Param("codeType") String codeType, @Param("authors") String authors);
	
	/*
	 * 当月设计测试用例总数
	 */
	public Double queryCXylzs(@Param("proNo") String proNo);

}
