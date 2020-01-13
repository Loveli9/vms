package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.ParameterInfo;

public interface IParameterInfo {
	List<ParameterInfo> queryParameterInfo();

	ParameterInfo queryParamId(@Param("paramId") int paramId);
	
	public Integer queryid(@Param("paraName")String paraName);
	
	public List<Map<String, Object>> processCapability(@Param("projNo")String projNo, @Param("paraId")Integer paraId);

	List<Map<String, Object>> queryProcessCapability(@Param("projNo")String projNo,@Param("parameters") String parameters);

	List<Map<String, Object>> queryByIds(@Param("parameters")String parameters);

}
