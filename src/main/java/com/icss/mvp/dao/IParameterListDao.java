package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ParameterInfo;

public interface IParameterListDao 
{
    public List<ParameterInfo> getList(@Param("para")ParameterInfo para, @Param("sort") String sort, @Param("order")String order);
    
    public int addParameter(@Param("para")ParameterInfo para);
    
    public int updateParameter(@Param("para")ParameterInfo para);
    
    public int deleteParameter(@Param("para")ParameterInfo para);
    
}
