package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ParameterInfo;
import com.icss.mvp.entity.ProjectParameter;



public interface IProjectParameter {
	List<ProjectParameter> queryProjectParameter(@Param("projNo")String projNo);
	List<Map<String,Object>> queryInfo(@Param("projNo")String projNo,@Param("sort") String sort, @Param("order")String order);
	int insertParamInfo(List<Object> paramList);
	List<ProjectParameter> queryIsExits(@Param("projNo")String projNo,@Param("id")int id);
	
	int delProjectParameter(@Param("projNo")String projNo,@Param("id")int id);
	
	int updateProjectParameter(ProjectParameter projParam);
	
}
