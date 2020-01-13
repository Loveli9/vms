package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.Label;
import com.icss.mvp.entity.LabelMeasureConfig;
import com.icss.mvp.entity.Measure;
import com.icss.mvp.entity.MeasureRange;
import com.icss.mvp.entity.MonthMeasure;
import com.icss.mvp.entity.ProjectLabelMeasure;
import com.icss.mvp.entity.TeamLabel;
import com.icss.mvp.entity.TeamMeasureConfig;
import com.icss.mvp.entity.ProjectLabelConfig;
import com.icss.mvp.entity.teamLabelConfig;

public interface ProjectLableDao {
	
	/**
	* @Description: 根据项目id获取当前项目配置的所有流程
	* @author Administrator  
	* @date 2018年5月9日  
	 */
	List<Map<String, Object>> getProjectAllabs(@Param("proNo") String proNo);
	List<Map<String, Object>> getTeamAllabs(@Param("teamId") String teamId);
	
	/**
	 * @Description: 根据流程目录、项目id获取当前项目配置流程信息
	 * @author chengchenhui
	 * @date 2018年5月7日
	 */
	List<Map<String, Object>> getProjectLabs(@Param("proNo") String proNo,@Param("category") String category);
	List<Map<String, Object>> getProjectLabsByTeamId(@Param("teamId") String teamId,@Param("category") String category);
	
	/**
	 * 
	 * @Description: 根据项目、版本号、迭代、流程节点加载指标信息
	 * @author Administrator
	 * @date 2018年5月7日
	 */
	List<Map<String, Object>> getLabMeasureByProject(@Param("labId") String labId, @Param("version") String version,
			@Param("ite") String ite, @Param("proNo") String proNo);
	List<Map<String, Object>> getLabMeasureByTeam(@Param("labId") String labId, @Param("version") String version,
			@Param("ite") String ite, @Param("teamId") String teamId);
	
	/**
	 * @Description: 查询当前流程配置的指标信息
	 * @date 2018年5月8日
	 */
	List<Map<String, Object>> getLabMeareListByProNo(@Param("labId") String labId,@Param("proNo") String proNo, @Param("version") String version,@Param("ite") String ite);
	List<Map<String, Object>> getLabMeareListByTeamId(@Param("labId") String labId,@Param("teamId") String teamId, @Param("version") String version,@Param("ite") String ite);
	
	/**
	 * 
	* @Description: 查询所有指标记录
	* @author cch  
	* @date 2018年5月8日  
	 */
	public List<Map<String, Object>> getMeasures();
	
	/**
	* @Description: 获取流程所有类目  
	* @author Administrator  
	* @date 2018年5月9日  
	* @version V1.0
	 */
	List<String> getAllLabCategory();
	
	/**
	* @Description: 根据流程id查询对应的所有指标类目 
	* @author Administrator  
	* @date 2018年5月9日  
	* @version V1.0
	 */
	List<Map<String, Object>> getAllCategorysByLabId(@Param("labId") String labId);
	
	/**
	* @Description: 更新当前项目配置的流程状态 
	* @author chengchenhui  
	* @date 2018年5月10日  
	 */
	int updateProjectLabConfig(@Param("labId") String labId, @Param("proNo") String proNo, @Param("status") String status);
	int updateTeamLabConfig(@Param("labId") String labId, @Param("teamId") String teamId, @Param("status") String status);
	
	/**
	* @Description: 查询当前项目是否配置了该流程
	* @author chengchenhui  
	* @date 2018年5月10日  
	* @version V1.0
	 */
	ProjectLabelConfig getProjectLabConfigByKey(@Param("labId") String labId,@Param("proNo") String proNo);
	List<teamLabelConfig> getTeamLabNumByTeamId(@Param("labId") String labId,@Param("teamId") String teamId);
	
	/**
	* @Description: 判断当前流程是否配置过该指标
	* @author Administrator  
	* @date 2018年5月10日  
	* @version V1.0
	 */
	LabelMeasureConfig getMeasureConfig(@Param("measureId") String measureId,@Param("projectLabId") String projectLabId);
	List<TeamMeasureConfig> getMeasureConfigByTeam(@Param("measureId") String measureId,@Param("teamLabId") String teamLabId);
	
	/**
	* @Description: 更新当前指标状态  
	* @author Administrator  
	* @date 2018年5月10日  
	* @version V1.0
	 */
	int updateConfigMeasure(@Param("measureId") String measureId,@Param("projectLabId") String projectLabId,@Param("status") String status);
	int updateConfigMeasureByTeam(@Param("measureId") String measureId,@Param("teamLabId") String teamLabId,@Param("status") String status);
	
	/**
	* @Description: 新增一条流程配置记录  
	* @author Administrator  
	* @date 2018年5月10日  
	* @version V1.0
	 */
	int insertProjectLableConfig(@Param("labId") String labId,@Param("proNo") String proNo);
	int insertTeamLableConfigByTeamId(@Param("labId") String labId,@Param("teamId") String teamId);
	
	/**
	* @Description: 新增一条指标配置记录  
	* @author Administrator  
	* @date 2018年5月10日  
	* @version V1.0
	 */
	int insertLableMeasureConfig(@Param("measureId") String measureId,@Param("projectLabId") String projectLabId);
	int insertTeamMeasureConfig(@Param("measureId") String measureId,@Param("teamLabId") String teamLabId);
	/**
	* @Description: 根据项目编号和流程id获取配置id
	* @author Administrator  
	* @date 2018年5月10日  
	* @version V1.0
	 */
	String getProjectConfigId(@Param("labId") String labId,@Param("proNo") String proNo);
	String getTeamConfigId(@Param("labId") String labId,@Param("teamId") String teamId);
	
	/**
	* @Description: 更新指标实际值
	* @author Administrator  
	* @date 2018年5月15日  
	 */
	Integer saveActualValMeasureConfig(@Param("id") String id,@Param("actualVal") String actualVal);
	
	/**
	* @Description: 查询当前项目指标关系  
	* @author Administrator  
	* @date 2018年5月21日  
	 */
	List<Map<String, Object>> updateMeasureMonth(@Param("proNo") String proNo);
	
	/**
	* @Description:查询指标是否已经存储了月记录  
	* @author Administrator  
	* @date 2018年5月21日  
	 */
	Map<String, Object> queryMonthMeasure(@Param("lId") String lId);
	
	/**
	* @Description: 更新月指标实际值
	* @author Administrator  
	* @date 2018年5月21日  
	 */
	Integer updateValue(@Param("id")String id,@Param("acVal")String acVal);
	
	/**
	* @Description: 按月份插入一条新的指标记录
	* @author Administrator  
	* @date 2018年5月21日  
	 */
	Integer insertVal(@Param("acVal")String acVal, @Param("lId")String lId);
	
	/**
	* @Description: 判断当前项目是否已经配置了其他流程模板   
	* @author Administrator  
	* @date 2018年6月13日  
	 */
	List<ProjectLabelConfig> queryMeasureConfigNum(@Param("proNo")String proNo, @Param("labName")String labName);
	List<teamLabelConfig> queryMeasureConfigByTeamId(@Param("teamId")String teamId, @Param("labName")String labName);
	
	/**
	* @Description:更新当前流程所有指标 
	* @author Administrator  
	* @date 2018年6月14日  
	 */
	Integer updateMeasures(@Param("id")String id);
	Integer updateMeasuresByTeamId(@Param("id")String id);

	List<Measure> queryMetricIndex(@Param("plId")String plId, @Param("category")String category);

	List<MonthMeasure> queryMeasureValue(@Param("str")String str);

	/*
	 * 获取项目流程配置子菜单
	 */
	List<String> queryCatgoryByPlIds(@Param("plId")String plId);
	List<String> queryCatgoryByTeamPlIds(@Param("plId")String plId);
	
	/**
	 * 获取已选中流程类目
	 * @param proNo
	 * @return
	 */
	String getIsSelectCategory(@Param("proNo")String proNo);
	String getIsSelectCategoryByTeamId(@Param("teamId")String teamId);
	
	List<ProjectLabelConfig> getLabelConfig(@Param("proNo") String proNo);
	
	/**
	 * 查询历史项目所有流程
	 * @param proNo
	 * @return
	 */
	List<Label> queryProjectAlllabs(@Param("oldNo") String oldNo);
	/**
	 * 查询历史项目已勾选流程
	 */
	List<Label> queryProjectChecklist(@Param("oldNo") String oldNo);
	
	List<TeamLabel> queryProjectAlllabsByTeam(@Param("teamId") String teamId);
	
	/**
	 * 删除新项目所有流程
	 * @param proNo
	 * @return
	 */
	Integer deleteProjectAlllabs(@Param("proNo") String proNo);
//	Integer deleteProjectAlllabsByTeam(@Param("projNo") String projNo);
	
	/**
	 * 导入历史项目所有流程
	 * @param label
	 * @return
	 */
	void saveProjectAlllabs(@Param("label") Label label);
//	void saveProjectAlllabsByTeam(@Param("label") TeamLabel label);
	
	/**
	 * 删除新项目所有流程指标配置
	 * @param id
	 * @return
	 */
//	Integer deleteLabelMesure(@Param("id") Integer id);
	
	/**
	 * 导入历史项目所有流程指标配置
	 * @param newId
	 * @param oldId
	 * @return
	 */
	int saveLabelMesure(@Param("newId") Integer newId, @Param("oldId") Integer oldId);
	int saveLabelMesureByTeam(@Param("newId") Integer newId, @Param("oldId") Integer oldId);
	/**
	 *删除模板配置内容
	 * @param strSql
	 */
	void deleteMeasureConfig(@Param("strSql") String strSql);
	void deleteMeasureConfigByTeamId(@Param("strSql") String strSql);
	
	void deleteLableMeasureConfig(@Param("strSql") String strSql);
	void deleteLableMeasureConfigByTeamId(@Param("strSql") String strSql);

	List<ProjectLabelMeasure> getMeasureRang(@Param("proNo") String no);
	
	String getTeamId(@Param("proNo") String proNo);
	
	void replaceMeasureConfig(Map<String, String> map);

	void replaceMeasureConfigTeam(Map<String, String> measureConfigMap);
}
