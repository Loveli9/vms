package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.Dept;
import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectInfoVo;

public interface IprojectReachStandardDAO {

	/**
	 * 查出该项目配置过的指标数量
	 */
	public Integer measured(@Param("no") String no);

	/**
	 * 查出每个项目每个指标每个迭代的值
	 */
	public List<Map<String, Object>> measureValuePerIterator(@Param("no") String no, @Param("day") String day);

	/**
	 * 删除之前算好的值
	 */
	public void deleteValue(@Param("no") String no);

	/**
	 * 添加项目合格率值
	 */
	public void insertReached(ParameterValueNew pvn);

	/**
	 * 查询合格率
	 */
	public List<Map<String, Object>> queryReach(Map<String, Object> map);

	/**
	 * 所有PDU
	 * 
	 * @return
	 */
	public List<Dept> getAllPDU(Map<String, Object> map);

	/**
	 * 所有DU
	 * 
	 * @return
	 */
	public List<Dept> getAllDU(Map<String, Object> map);

	/**
	 * 所有HWZPDU
	 * 
	 * @return
	 */
	public List<Dept> getAllHWZPDU(Map<String, Object> map);

	/**
	 * 所有PDU_SPDT
	 * 
	 * @return
	 */
	public List<Dept> getAllPDUSPDT(Map<String, Object> map);

	/**
	 * 所有地域
	 * 
	 * @return
	 */
	public List<Dept> getAllAreas(Map<String, Object> map);

	/**
	 * 查询指标名称
	 * 
	 * @return
	 */
	public String getMeasureName(@Param("id") String id);

	/**
	 * 查询项目名称
	 * 
	 * @return
	 */
	public ProjectInfoVo getProjName(@Param("no") String no);

	/**
	 * 加载选中的不合格项目的项目编号和合格率
	 */
	List<Map<String, Object>> queryProjNoAndValueByHWPDU(@Param("projectType") String projectType,
			@Param("pageInfo") PageInfo pageInfo);

	List<Map<String, Object>> queryProjNoAndValueByHWZPDU(@Param("projectType") String projectType,
			@Param("pageInfo") PageInfo pageInfo);

	List<Map<String, Object>> queryProjNoAndValueByBU(@Param("projectType") String projectType,
			@Param("pageInfo") PageInfo pageInfo);

	List<Map<String, Object>> queryProjNoAndValueByPDU(@Param("projectType") String projectType,
			@Param("pageInfo") PageInfo pageInfo);

	List<Map<String, Object>> queryProjNoAndValueByAREA(@Param("projectType") String projectType,
			@Param("pageInfo") PageInfo pageInfo);

	public List<String> reachedAllProjNoByPDU(@Param("id") String id);

	public List<String> reachedAllProjNoByDU(@Param("id") String id);

	public List<String> reachedAllProjNoByHWZPDU(@Param("id") String id);

	public List<String> reachedAllProjNoByPDUSPDT(@Param("id") String id);

	public List<String> reachedAllProjNoByArea(@Param("id") String id);
}