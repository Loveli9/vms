package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.common.request.PageRequest;

public interface IProjectOperationDao {

	int saveProjectOperation(@Param("proNo") String proNo, @Param("userName") String userName, @Param("message") String message);
	
	int queryProjectOperationCount(@Param("proNo") String proNo);
	
	List<Map<String, String>> queryProjectOperationList(@Param("proNo") String proNo, @Param("page") PageRequest pageRequest);

	int queryProjectOperationCountClone();

	List<Map<String, String>> queryProjectOperationListClone(@Param("page") PageRequest pageRequest);
}
