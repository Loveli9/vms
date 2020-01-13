package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ScripInfo;

public interface ScripInfoDao {

	/**
	* @Description: 新增一条记录
	* @author Administrator  
	* @date 2018年5月17日  
	 */
	Integer insertErrorMessage(@Param("info")ScripInfo info);
	/**
	* @Description: 更新失效时间
	* @author Administrator  
	* @date 2018年5月17日  
	 */
	Integer updateErrorMessage(@Param("id")String id);
	
	/**
	* @Description: 获取错误消息日志 
	* @author Administrator  
	* @date 2018年5月20日  
	* @version V1.0
	 */
	List<Map<String, Object>> getMessage(@Param("proNo")String proNo,@Param("token")String token);
	/**
	 * 获取已完成采集任务数
	 * @param proNo
	 * @param token
	 * @return
	 */
	Map<String, Object> getCompleteCount(@Param("proNo")String proNo,@Param("token")String token);

}
