package com.icss.mvp.dao;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.SchedulePlan;
import java.util.List;
import java.util.Map;

public interface IProjectOverviewDao {

	List<SchedulePlan> getPeriodName(@Param("no") String no);

	Map<String, Object> getStartStopTime(@Param("no") String no);

	List<String> queryFirstFiveWeekDate(@Param("statisticalTime") String statisticalTime);

	Map<String, Object> queryProInfo(@Param("no") String no);
	
	List<Map<String, Object>> queryKeyRoles(@Param("no") String no);
	
	List<Integer> getKXversion();

	String getKnotProjectDate(@Param("no") String no, @Param("knotMark") int knotMark);
	
}
