package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.PersonalBulidTime;

public interface IPersonalBulidTimeDao {

	/**
	 * 最先查询当月已经采集的构建信息条数
	 * 
	 * @return
	 */
	public Integer queryNowBulid(@Param("month") String month, @Param("projNo") String projNo);

	/**
	 * 先删除当月已经保存过的构建信息
	 * 
	 * @return
	 */
	public void deleteNowBulid(@Param("month") String month, @Param("projNo") String projNo);

	/**
	 * 重新保存构建信息，确保刷新
	 * 
	 * @return
	 */
	public void insertBulid(@Param("list") List<PersonalBulidTime> list);

	/**
	 * 查询每个月的构建信息
	 * 
	 * @return
	 */
	public List<Map<String, Object>> buildPerMonth(@Param("month") String month, @Param("projNo") String projNo);

	/**
	 * 删除原smartIDE
	 * 
	 * @return
	 */
	public void deletePbiId(@Param("projNo") String projNo);

	/**
	 * 配置新smartIDE
	 * 
	 * @return
	 */
	public void savePbiId(@Param("projNo") String projNo, @Param("pbiId") String pbiId,
			@Param("pbiName") String pbiName);

	/**
	 * 读取pbi_id
	 * 
	 * @return
	 */
	public String queryPbiId(@Param("projNo") String projNo);

	/**
	 * 读取pbi_name
	 * 
	 * @return
	 */
	public String queryPbiName(@Param("projNo") String projNo);

	/**
	 * 获得项目的所有构建条数
	 * 
	 * @return
	 */
	public Integer queryAllBuilds(@Param("projNo") String projNo);

	/**
	 * 查询多个项目的总构建时长
	 */
	public Double personBuildTime(@Param("yearMonth") String yearMonth, @Param("sqlStr") String sqlStr);

	/**
	 * 查询多个项目的总构建次数
	 */
	public Double personBuildTimeCounts(@Param("yearMonth") String yearMonth, @Param("sqlStr") String sqlStr);
	
	/*
	 * 查询项目是配置了smartIde参数
	 */
	public Integer isHaveConfigIde(@Param("proNo")String proNo);

}