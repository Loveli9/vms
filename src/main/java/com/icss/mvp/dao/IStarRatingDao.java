package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.StarRating;

public interface IStarRatingDao {
	
	public List<Map<String, Object>> initStarRating(Map<String, Object> map);
	
	public void insertStarRating(@Param("stars")List<StarRating> stars);

	public List<Map<String, Object>> loadSatrByTypes(@Param("strSql")String strSql);
	
}