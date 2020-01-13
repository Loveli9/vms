package com.icss.mvp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.LogModifyNumDao;

import com.icss.mvp.entity.LogModifyNum;
@Service
public class LogModifyNumService {
	@Autowired 
	private LogModifyNumDao dao;
	public int insertcommitrecord(List<LogModifyNum> logmodelList) {
		return dao.insertcommitrecord(logmodelList);
	}
	public int queryCodeNum(String no) {
		return dao.queryCodeNum(no);
	}
	public void delcommitrecord(Map<String, Object> maps) {
		dao.delcommitrecord(maps);
		
	}

}
