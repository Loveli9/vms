package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;


import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.IcpConfigInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.RepositoryInfo;
import com.icss.mvp.entity.VersionStrucInfo;


public interface VersionStrucDao {
	/**
	* @Description: 查询版本级构建指标 
	* @author Administrator  
	* @date 2018年6月20日  
	 */
	List<Map<String, Object>> queryVersionStrucInfo(@Param("no") String no);
	
	/**
	* @Description: 保存版本构建信息
	* @author Administrator  
	* @date 2018年6月21日  
	 */
	void saveVersionBuildInfo(@Param("list") List<VersionStrucInfo> list);
	
	/**
	* @Description: 计算当月的总构建时长（截止当前时间）  
	* @author Administrator  
	* @date 2018年6月22日  
	 */
	Double queryBuildTimes(@Param("ym")String ym, @Param("no")String no);
	
	/**
	* @Description: 查询当月截止目前的总构建次数  
	* @author Administrator  
	* @date 2018年6月22日  
	 */
	Double queryBuildCounts(@Param("ym")String ym, @Param("no")String no);

	/**
	* @Description: 保存ftp icp-ci 配置
	* @author Administrator  
	* @date 2018年6月26日  
	 */
	Integer saveIcpCiConfig(@Param("info")IcpConfigInfo info);
	
	/**
	* @Description: 查询当前项目配置的FTP获取参数
	* @date 2018年6月26日  
	 */
	List<RepositoryInfo> queryIcpCiConfig();
	
	/**
	* @Description:查询所有ICP-CI配置信息  
	* @author Administrator  
	* @date 2018年6月27日  
	* @version V1.0
	 */
	List<IcpConfigInfo> queryIcpConfig();
	
	/**
	 * 
	* @Title: VersionStrucDao.java  
	* @Package com.icss.mvp.dao  
	* @Description: TODO(用一句话描述该文件做什么)  
	* @author Administrator  
	* @date 2018年7月3日  
	* @version V1.0
	 * @return 
	 */
	List<ProjectInfo> querySelectedNos(Map<String, Object> map);
	
	/**
	* @Description:查询多个项目版本构建时长平均值
	* @author Administrator  
	* @date 2018年7月3日  
	 */
	Double queryBuildTimesByNos(@Param("yearMonth")String yearMonth, @Param("sqlStr")String sqlStr);
	
	/**
	* @Description:查询多个项目版本构建次数
	* @author Administrator  
	* @date 2018年7月3日  
	 */
	Double queryBuildCountsByNos(@Param("yearMonth")String yearMonth, @Param("sqlStr")String sqlStr);
	
	
	
}
