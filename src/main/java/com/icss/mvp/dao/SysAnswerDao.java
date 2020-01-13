package com.icss.mvp.dao;

import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.SysAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAnswerDao {
	int deleteByPrimaryKey(String id);

	int insert(SysAnswer record);

	int insertSelective(SysAnswer record);

	SysAnswer selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(SysAnswer record);

	int updateByPrimaryKeyWithBLOBs(SysAnswer record);

	int updateByPrimaryKey(SysAnswer record);

	int selectAnswerCount(String questionId);

	List<SysAnswer> selectAnswers(@Param("questionId") String questionId, @Param("pageInfo") PageInfo pageInfo);

	void updateSolveState(@Param("questionId") String questionId);
}