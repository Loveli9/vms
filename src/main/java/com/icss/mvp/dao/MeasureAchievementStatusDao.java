package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.MeasureComment;

public interface MeasureAchievementStatusDao {

	int insertMeasureQualify(Map<String, Object> map);

	int saveProjectParameterRecord(@Param("date1") String date1, @Param("date2") String date2);

	List<String> queryGaoSiNo(@Param("endDate") String endDate, @Param("measureMark") List<String> measureMark);
	
	List<String> queryKeXinNo(@Param("endDate") String endDate, @Param("measureMark") List<String> measureMark);

	List<MeasureComment> queryMeasureStatus(@Param("endDate") String endDate, @Param("startDate") String startDate,
			@Param("measureIds") List<String> measureIds, @Param("no") String no);

	List<String> queryCredibleIndex(@Param("processMark") String processMark);

	Map<String, Object> getProjectLimit(@Param("no") String no, @Param("limitMark") String limitMark);


	List<String> queryZongLanNos(@Param("nextCycleDate") String nextCycleDate,
			@Param("measureMark") String measureMark);

	int getCodeGainTypeNum(@Param("no") String no, @Param("offlineMark") String offlineMark);
	
	int getProjectParameterRecordNum(@Param("no") String no, @Param("offlineMark") String offlineMark,
			@Param("offlineDate") String offlineDate);

	void updateCodeGainType(Map<String, Object> map);

	void addCodeGainType(Map<String, Object> map);

	void updateProjectParameterRecord(Map<String, Object> map);

	void addProjectParameterRecord(Map<String, Object> map);

	int initNetworkSecurity(@Param("now") String now, @Param("old") String old);

	MeasureComment queryKexinMeasure(@Param("no") String no, @Param("measureId") String measureId, @Param("date") String date);
}
