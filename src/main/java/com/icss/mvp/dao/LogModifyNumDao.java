package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.LogModifyNum;

public interface LogModifyNumDao {

	int insertcommitrecord(List<LogModifyNum> logmodelList);

	int queryCodeNum(String no);

	void delcommitrecord(@Param(value = "map") Map<String, Object> maps);

}
