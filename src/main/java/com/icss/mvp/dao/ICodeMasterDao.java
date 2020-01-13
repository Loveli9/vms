package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeMasterInfo;


public interface ICodeMasterDao {
	public List<CodeMasterInfo> getCodeNameListbyKey(@Param("key")String key);

}
