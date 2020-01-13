package com.icss.mvp.service;

import java.util.List;

import com.icss.mvp.dao.IParameterInfo;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParameterInfoService {
	 @Autowired
	   private IParameterInfo dao;
	 public List<ParameterInfo> queryParameterInfo()
	    {
	        return dao.queryParameterInfo();
	    }
	 
	 public ParameterInfo queryParamId(int paramId)
	    {
	        return dao.queryParamId(paramId);
	    } 
	 
	/* public ParameterInfo queryId(String name,String unit,String bigValue,String smallValue,String sourceValue,String parameter)
	 {
	 return dao.queryId(name,unit,bigValue,smallValue,sourceValue,parameter);
}*/
}