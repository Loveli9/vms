package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.icss.mvp.entity.Label;

public interface ILabelDAO {
	
	public List<Map<String, Object>> queryLabel(@Param("selCategory") String selCategory);
	
	public void updateLabel(@Param("label") Label label);
	
	public void updateDeleteLabel(@Param("id") int id);
	
	public void insertLabel(@Param("label") Label label);
	
}