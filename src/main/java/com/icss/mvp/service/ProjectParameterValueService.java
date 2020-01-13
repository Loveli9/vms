package com.icss.mvp.service;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectParameterValueDao;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectParameterValue;
import com.icss.mvp.util.DateUtils;

@Service("projectParameterValueService")
@Transactional
public class ProjectParameterValueService
{

	@Resource
	private IProjectParameterValueDao parameterValueDao;
	
	@Resource
	private IProjectListDao projectListDao;
	
	private static final Logger LOG = Logger
			.getLogger(ProjectParameterValueService.class);

	public Map<String, Object> getList(
			ProjectParameterValue projectParameterValue, Page page)
	{
		String unit = projectParameterValue.getUnit();

		if (StringUtils.isEmpty(unit))
		{
			projectParameterValue.setUnit(unit);
		} else if (unit.contains("%"))
		{
			unit = unit.replaceAll("%", "/%");
		} else if (unit.contains("/"))
		{
			unit = unit.replaceAll("/", "//");
		}
		projectParameterValue.setUnit(unit);
		List<ProjectParameterValue> paraValue = parameterValueDao.getList(
				projectParameterValue, page.getSort(), page.getOrder());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", paraValue);
		result.put("total", paraValue.size());
		return result;
	}

	public Map<String, Object> updateParameterValue(
			ProjectParameterValue paraValue)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		int cnt = insertParameterValue(paraValue.getNo(),paraValue.getYearMonth(), paraValue.getId(),paraValue.getValue());
		if (cnt > 0)
		{
			result.put("result", "OK");
		} else
		{
			result.put("result", "NG");
		}

		return result;
	}

	public Map<String, Object> importParams(MultipartFile file, String no)
	{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try 
		{
			InputStream is = file.getInputStream();
			ExcelReader reader = new ExcelReader(is);
			Map<String, Object> importResult = reader.read(no);
			if (Constants.FAILED.equals(importResult.get(Constants.STATUS)))
			{
				return importResult;
			}
			List<ParameterValueNew> paramList = (List<ParameterValueNew>) importResult
					.get("DATA");
			if (CollectionUtils.isNotEmpty(paramList))
			{
				// parameterValueDao.insertParams(paramList);
				for (ParameterValueNew value : paramList){
					insertParameterValue(value);
				}
			}
			resultMap.put(Constants.STATUS, Constants.SUCCESS);
		} catch (Exception e)
		{
			LOG.error("import params failed!", e);
			resultMap.put(Constants.STATUS, Constants.FAILED);
			resultMap.put(Constants.MESSAGE, "解析EXCEL出错，请检查文件内容！");
		}
		return resultMap;
	}

	public byte[] getTemplateBtyes(ProjectParameterValue paramVo)
	{
		Page page = new Page();
		List<ProjectParameterValue> paraValue = parameterValueDao.getList(
				paramVo, page.getSort(), page.getOrder());
		ExcelReader reader = new ExcelReader();
		return reader.write(paraValue);
	}
	
	public int insertParameterValue(String no,Date month, int parameterId,double value){
		ParameterValueNew paraValue = new ParameterValueNew();
		paraValue.setNo(no);
		paraValue.setMonth(month);
		paraValue.setParameterId(parameterId);
		paraValue.setValue(value);
		return insertParameterValue(paraValue);
	}
	
	private Integer convertToInt(String str){
		int result = 0;
		try{
			//若是数字，则可以转换，否则会出现异常
			result=Integer.parseInt(str);
		}catch(Exception e){
			return null;
		}
		return result;
	}
	
	public Double convertToDouble(String str){
		double result = 0;
		try{
			//若是数字，则可以转换，否则会出现异常
			result=Double.parseDouble(str);
		}catch(Exception e){
			return null;
		}
		return result;
	}
	public void inserCiParameter(List<HashMap<String, Object>> resultDatas,String paraValue,int paraNum){
		Date date = null;
		for (HashMap<String, Object> info : resultDatas) {
			try {
				date = DateUtils.format.parse(String.valueOf(info.get(Constants.STRUCT_TIME)));
			} catch (ParseException e) {
				LOG.error("parse fail!" +date);
			}
			
			Double parameterValue = convertToDouble(String.valueOf(info.get(paraValue)));
			if (null == parameterValue){
				return;
			}
			insertParameterValue(String.valueOf(info.get("no")),date,paraNum,parameterValue);
		}
	}
	public void insertParameterValueByVersion(String version, Date month, String parameterId,String value){
		Integer id = convertToInt(parameterId);
		if (id == null){
			return;
		}
		Double parameterValue = convertToDouble(value);
		if (parameterValue == null){
			return;
		}
		List<ProjectInfo> projectInfos = projectListDao.isExistVersion(version);
		if (projectInfos.size() > 0){
			for (ProjectInfo info : projectInfos){
				String no = info.getNo();
				insertParameterValue(no,month, id,parameterValue);
			}
		}
	}
	
	public double getParameterValue(String no,Date month,String parameterId){
		double resutl = 0;
		Integer id = convertToInt(parameterId);
		if (id == null){
			return resutl;
		}
		ParameterValueNew paraValue = new ParameterValueNew();
		paraValue.setNo(no);
		paraValue.setMonth(month);
		paraValue.setParameterId(id);
		List<ParameterValueNew> values = parameterValueDao.getParameterValue(paraValue);
		if (values.size() > 0){
			resutl = values.get(0).getValue();
		}
		return resutl; 
	}
	
	public int insertParameterValue(ParameterValueNew paraValue){
		Date monthTmp = paraValue.getMonth();
		monthTmp.setDate(1);
		paraValue.setMonth(monthTmp);
		List<ParameterValueNew> values = parameterValueDao.getParameterValue(paraValue);
		if (values.isEmpty()){
		}
		else{
			parameterValueDao.deleteParameterValue(paraValue);
		}
		return parameterValueDao.insertParameterValue(paraValue);
	}
}
