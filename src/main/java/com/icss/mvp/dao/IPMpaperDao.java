package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeMasterInfo;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.LowLoc;
import com.icss.mvp.entity.Measure;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.SysDictItem;

public interface IPMpaperDao {

	/**
	 * 查出项目名称
	 * 
	 * @return
	 */
	public String queryProName(@Param("proNo") String proNo);

	/**
	 * 查询低产出项目明细
	 * 
	 * @return
	 */
	public List<LowLoc> queryLowlocDetail();

	/**
	 * 查询项目名称
	 */
	public String getProjName(@Param("no") String no);

	/**
	 * 查询PM
	 */
	public List<String> getPMname(@Param("no") String no);

	/**
	 * 查询问题单总数
	 */
	public Integer sumDTS(@Param("no") String no);

	/**
	 * 根据严重级别分类查询问题单数
	 */
	public Map<String, Object> sumBySeverity(Map<String, String> temp);

	/**
	 * 根据重要程度分类查询各迭代需求
	 */
	public List<Map<String, Object>> needs(@Param("no") String no);

	/**
	 * 查询所有问题单状态
	 */
	public List<CodeMasterInfo> getCurentStatus();

	/**
	 * 根据状态查询问题单数
	 */
	public Integer sumByCurentStatus(@Param("status") String status, @Param("no") String no);

	/**
	 * 进行中问题单数
	 */
	public Integer runingDTS(@Param("no") String no);

	/**
	 * 获得项目信息
	 */
	public ProjectInfo getProject(@Param("no") String no);

	/**
	 * 获得配置过的指标信息
	 */
	public List<Measure> measures(@Param("no") String no);

	/**
	 * 热门指标
	 */
	public List<Map<String, Object>> hotMeasures();

	/**
	 * 所有迭代
	 */
	public List<IterationCycle> iterationCycles(@Param("no") String no);

	/**
	 * 按迭代统计问题单
	 */
	public Map<String, Object> dtsCount(@Param("no") String no, @Param("startTime") String startTime,
			@Param("endTime") String endTime);

	/**
	 * 工作状态列表
	 */
	public List<SysDictItem> storyStatus();

	/**
	 * 按工作状态分组统计每个迭代的story数
	 */
	public Integer storyByStatus(@Param("no") String no, @Param("iteId") String iteId,
			@Param("status") String status);

	/**
	 * 统计每个迭代的story数
	 */
	public Integer storyCounts(@Param("no") String no, @Param("iteId") String iteId);

	public Map<String, Object> bugSubmission(@Param("no") String no,@Param("ACCOUNT") String ACCOUNT);

	public List<Map<String, Object>> bugSubmissionAcc(@Param("no") String no);

}