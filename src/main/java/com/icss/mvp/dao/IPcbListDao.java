package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface IPcbListDao
{
	List<Map<String, Object>> getParameterByProjectAndIds(@Param("proNos") String proNos,@Param("ids") String ids);
	List<Map<String, Object>> getParameterByProjectAndIdsSum(@Param("proNos") String proNos,@Param("ids") String ids);
	/**
	 * 保存pcb样本项目配置
	 * @param proNo
	 * @param pcbCategory
	 * @return
	 */
	int editPcbProjectConfig(List<Map<String,Object>> list);
	
	int insertPcbProjectConfig(List<Map<String, Object>> list);
	/**
	 * 查询pcb样本项目配置列表
	 * @param parameter
	 * @return
	 */
	List<String> queryPcbProjectConfigList(Map<String, Object> parameter);
	/**
	 * 查询pcb样本项目配置数量
	 * @param proNos
	 * @param pcbCategory
	 * @return
	 */
	int queryPcbProjectConfigCount(@Param("proNos")List<String> proNos,@Param("pcbCategory")String pcbCategory);

	List<Map<String,Object>> queryPcbProjectConfigListByNo(Map<String, Object> retMap);

	List<String> queryPcbProjectConfigIdList(Map<String, Object> parameter);
	List<Map<String, Object>> exportResearch(@Param("no") String no,@Param("category") String category);
	
}
