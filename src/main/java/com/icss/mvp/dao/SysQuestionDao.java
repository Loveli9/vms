package com.icss.mvp.dao;

import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.SysQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysQuestionDao {
	int deleteByPrimaryKey(String id);

	int insert(SysQuestion record);

	int insertSelective(SysQuestion record);

	SysQuestion selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(SysQuestion record);

	int updateByPrimaryKeyWithBLOBs(SysQuestion record);

	int updateByPrimaryKey(SysQuestion record);

	int selectQuestionCount(SysQuestion sysQuestion);

	List<Map<String, Object>> selectQuestions(@Param("sysQuestion") SysQuestion sysQuestion,
			@Param("pageInfo") PageInfo pageInfo);

	Map<String, Object> details(@Param("questionId") String questionId);
	
	void updateSolveState(@Param("solveState") String solveState,@Param("id") String id);
}