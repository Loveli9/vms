package com.icss.mvp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.IParameterListDao;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterInfo;

@Service("parameterListService")
@Transactional
public class ParameterListService
{
	@Resource
	private IParameterListDao parameterListDao;
	
	public Map<String, Object> getList(ParameterInfo para, Page page)
	{
		String unit = para.getUnit();

		if (StringUtils.isEmpty(unit))
		{
			para.setUnit(unit);
		} else if (unit.contains("%"))
		{
			unit = unit.replaceAll("%", "/%");
		} else if (unit.contains("/"))
		{
			unit = unit.replaceAll("/", "//");
		}
		para.setUnit(unit);
		List<ParameterInfo> paraList = parameterListDao.getList(para, page.getSort(), page.getOrder());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", paraList);
		result.put("total", paraList.size());
		return result;
	}
	
	public int addParameterInfo(ParameterInfo para)
	{
		return parameterListDao.addParameter(para);
	}
	
	public int updateParameterInfo(ParameterInfo para)
	{
		return parameterListDao.updateParameter(para);
	}
	
	public int deleteParameterInfo(ParameterInfo para)
	{
		return parameterListDao.deleteParameter(para);
	}
}