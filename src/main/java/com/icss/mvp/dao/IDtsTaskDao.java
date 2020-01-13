package com.icss.mvp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.DtsLogs;

public interface IDtsTaskDao
{
	public void insert(@Param("list") List<Map<String, String>> dtsList);

	public List<HashMap<String, Object>> query();

	public List<HashMap<String, Object>> queryDI();

	public List<HashMap<String, Object>> queryTr5DI();

	public List<HashMap<String, Object>> queryTr6DI();

    public List<HashMap<String,Object>> queryDensity();

	public HashMap<String,Object> queryServerity(@Param("no")String no);
 
	public List<Map<String, Object>> queryDts(@Param("no")String no, @Param("date") String date);

	public List<HashMap<String, Object>> queryServerityByVersion(@Param("no") String no);

	public List<HashMap<String, Object>> queryServerityByEven(Map<String, Object> map);

	public List<Map<String, Object>> queryDtsBVersion(@Param("no") String no);
	
	public List<DtsLogs> dtsDownload(Map<String, Object> map);

	public Map<String, Object> getDtsSeverityToday(@Param("no")String no);
	
	Integer dtsIterTotal(@Param("no")String no, @Param("startTime")String startTime, @Param("endTime")String endTime);

	Map<String, Object> dtsIterTotalNotClo(@Param("no")String no, @Param("startTime")String startTime, @Param("endTime")String endTime);
	Map<String, Object> dtsIterOverDueTotal(@Param("no")String no, @Param("startTime")String startTime, @Param("endTime")String endTime);
}
