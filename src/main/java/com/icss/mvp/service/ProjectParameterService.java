package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.IParameterInfo;
import com.icss.mvp.dao.IProjectParameter;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterInfo;
import com.icss.mvp.entity.ProjectParameter;

@Service
public class ProjectParameterService {
	@Autowired
	private IProjectParameter dao;
	@Autowired
	private IParameterInfo parameterInfoDAO;
//	@Autowired
//	private ICodeMasterDao codeMasterDao;
	private final String DATASOURCE_KEY = "paramSource";

	public Map<String, Object> queryProjectParameter(String projNo,Page page) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> allInfo = new ArrayList<Map<String, Object>>();
		/*List<ProjectParameter> proParam = dao.queryProjectParameter(projNo); 
		Map<String, Object> oneInfo = null;	
		if (proParam.size() == 0) {
			List<ParameterInfo> paramInfo = paraService.queryParameterInfo();
			 List<Object> paramList=new ArrayList<Object>();
			 for (ParameterInfo parameterInfo : paramInfo) {
			    int isDisplay=1;
			    Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("no", projNo);
				paramMap.put("id", parameterInfo.getId());
				paramMap.put("name", parameterInfo.getName());
				paramMap.put("unit", parameterInfo.getUnit());
				paramMap.put("source_value", parameterInfo.getSource_value());
				paramMap.put("big_type_value", parameterInfo.getBig_type_value());
				paramMap.put("small_type_value", parameterInfo.getSmall_type_value());
				paramMap.put("parameter", parameterInfo.getParameter());
				paramMap.put("isDisplay",isDisplay );
				paramMap.put("create_date",parameterInfo.getCreate_date());
				paramMap.put("creator", parameterInfo.getCreator());
				paramMap.put("update_date", parameterInfo.getUpdate_date());
				paramMap.put("update_user", parameterInfo.getUpdate_user()); 
				paramList.add(paramMap);
			 } 
			  int row=dao.insertParamInfo(paramList);
	 }*/
		
		 allInfo=dao.queryInfo(projNo,page.getSort(),page.getOrder());
		 resultMap.put("rows", allInfo);
	     resultMap.put("total", allInfo.size());
	     return resultMap;
		
	
	}
		

//	public Map<String, Object> queryDataSource() {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		List<CodeMasterInfo> codeInfoList = codeMasterDao
//				.getCodeNameListbyKey(DATASOURCE_KEY);
//		resultMap.put("rows", codeInfoList);
//		resultMap.put("total", codeInfoList.size());
//		return resultMap;
//	}

	/*public String addProjectParameter(String projNo, ParameterInfo paramInfo,
			int isDisplay) {
		int id = paramInfo.getId();
		ParameterInfo parmInfo = paraService.queryParamId(id);
		parmInfo.setUnit(paramInfo.getUnit());
		parmInfo.setSource_value(paramInfo.getSource_value());
		parmInfo.setParameter(paramInfo.getParameter());
		List<ProjectParameter> reslt = dao.queryIsExits(projNo, id);
		if (null == reslt||reslt.isEmpty()) {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("no", projNo);
			paramMap.put("id", parmInfo.getId());
			paramMap.put("unit", parmInfo.getUnit());
			paramMap.put("source_value", parmInfo.getSource_value());
			paramMap.put("parameter", parmInfo.getParameter());
			paramMap.put("isDisplay",isDisplay );
			paramMap.put("create_date",parmInfo.getCreate_date());
			paramMap.put("creator", parmInfo.getCreator());
			paramMap.put("update_date", parmInfo.getUpdate_date());
			paramMap.put("update_user", parmInfo.getUpdate_user());
			int row = dao.addProjectParameter(paramMap);
			if (row > 0) {
				return "保存成功!";

			} else {
				return "保存失败!";
			}
		} else {
			int row = updateProjectParameter(projNo, parmInfo, isDisplay);
			if (row > 0) {
				return "更新成功!";
			} else {
				return "更新失败!";
			}
		}
	}*/

	public String delProjectParameter(String projNo, ParameterInfo paramInfo) {
		int id = paramInfo.getId();
		int row = dao.delProjectParameter(projNo, id);
		if (row > 0) {
			return "删除成功!";
		} else {
			return "删除失败!";
		}
	}

	public String updateProjectParameter(ProjectParameter projParam) {
		int row=dao.updateProjectParameter(projParam);
		if(row>0){
			return "保存成功!";
		}else {
			return "保存失败!";
		}
	}
	
	/**
	 * 工程能力折线图
	 * @param paraName
	 * @param paraName 
	 * @return
	 */
	public List<Map<String, Object>> processCapability(String projNo, String paraName) {
		Integer id=queryid(paraName);
		List<Map<String, Object>> list=parameterInfoDAO.processCapability(projNo,id);
		return list;
	}
	
	public Integer queryid(String paraName) {
		return parameterInfoDAO.queryid(paraName);
	}


	public List<Map<String, Object>> getProcessCapability(String projNo, String parameters) {
		List<Map<String, Object>> projectParameterValues = parameterInfoDAO.queryProcessCapability(projNo,parameters);
		List<Map<String, Object>> parameterInfos = parameterInfoDAO.queryByIds(parameters);
		for (Map<String, Object> projectParameterValue : projectParameterValues) {
			int i = (int) projectParameterValue.get("PARAMETER_ID");
			for (Map<String, Object> parameterInfo : parameterInfos) {
				int j = (int) parameterInfo.get("PARAMETER_ID");
				if(i==j) {
					projectParameterValue.put("UNIT", parameterInfo.get("UNIT"));
				}
			}
		}
		return projectParameterValues;
	}

}
