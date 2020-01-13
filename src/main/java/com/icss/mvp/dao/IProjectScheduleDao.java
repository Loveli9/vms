package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectSchedule;

public interface IProjectScheduleDao {
	List<ProjectSchedule> queryProjectScheduleNo(@Param("no") String no);
	int insertInfos(@Param("proj") List<ProjectSchedule> proj);
	int batchDeleteByNo(@Param("no") String no);
	List<Map<String, Object>> projectScheduleList(@Param("list") Set<String> proNos, @Param("step") int step);
}
