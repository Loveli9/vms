package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeMasterInfo;

public interface ICodeMasterInfoDao {

	public List<CodeMasterInfo> getList(@Param("codeM")CodeMasterInfo codeM);
	
	public List<CodeMasterInfo> getCodeMasterOrderByValue(@Param("codeM")CodeMasterInfo codeM);

	public List<Map<String, Object>> getCodeMasterOrderByValues(@Param("codeM")CodeMasterInfo codeM);

	public List<Map<String, Object>> getMemberStatusValue();

	public String getTeamId(@Param("no") String no);
	
	public List<Map<String, Object>> getProjectPOCodeByTeam(@Param("teamId") String teamId);

	public List<Map<String, Object>> getProjectPOCodeByProject(@Param("no") String no);

	public List<Map<String, Object>> getMemberSVN(@Param("no") String no);

	public List<Map<String, Object>> getMemberRankValue();

	public List<Map<String, Object>> getMemberRankValues();
	public String  getkeyByvalue(@Param("value") String role);
	
}

