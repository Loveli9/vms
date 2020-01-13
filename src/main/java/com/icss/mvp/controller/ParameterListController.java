package com.icss.mvp.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.CodeMasterInfo;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterInfo;
import com.icss.mvp.service.CodeMasterInfoService;
import com.icss.mvp.service.ParameterListService;

/**
 * 
 * @author diaokeqi
 *
 */
@RestController
@RequestMapping("/parameter")
public class ParameterListController {

	private static Logger LOG = Logger.getLogger(ParameterListController.class);
	
	@Resource
	private ParameterListService parameterListService;
	
	@Resource
	private CodeMasterInfoService codeMasterInfoService;
	
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest request, ParameterInfo para, Page page)
	{
		if (null != para) {
			para = paraInit(para);	
		}
		Map<String, Object> result = parameterListService.getList(para, page);
		List<ParameterInfo> paraList = (List<ParameterInfo>)result.get("rows");
		if (null != paraList) {
			for (ParameterInfo parameterInfo : paraList) {
				parameterInfo = paraAfter(parameterInfo);
			}
		}
		result.put("rows", paraList);
		return result;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Map<String, Object> addParameter(ParameterInfo para)
	{
		Page page = new Page();
		page.setSort("id");
		page.setOrder("asc");
		Map<String, Object> result = parameterListService.getList(new ParameterInfo(), page);
		List<ParameterInfo> paraList = (List<ParameterInfo>)result.get("rows");
		int id = 1;
		if (null != paraList) {
			for (ParameterInfo parameterInfo : paraList) {
				if (id != Integer.valueOf(parameterInfo.getId())) {
					break;
				} else {
					id++;
				}
			}
		}
		para.setId(id);
		Date create_date = new Date();
		para.setCreate_date(create_date);
		 parameterListService.addParameterInfo(para);
		 return parameterListService.getList(new ParameterInfo(), page);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public Object updateParameter(ParameterInfo para)
	{
		if (null != para) {
			para = paraInit(para);	
		}
		Date update_date = new Date();
		para.setUpdate_date(update_date);
		int code = parameterListService.updateParameterInfo(para);
		return code;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Object deleteParameter(ParameterInfo para)
	{
		return parameterListService.deleteParameterInfo(para);
	}
	private ParameterInfo paraInit(ParameterInfo para)
	{
		CodeMasterInfo bigType = new CodeMasterInfo("BigType", null, para.getBig_type_value());
		CodeMasterInfo smallType = new CodeMasterInfo("SmallType", null, para.getSmall_type_value());
		CodeMasterInfo source = new CodeMasterInfo("Source", null, para.getSource_value());
		para.setBig_type_value(codeMasterInfoService.getCodeMaster(bigType).getValue());
		para.setSmall_type_value(codeMasterInfoService.getCodeMaster(smallType).getValue());
		para.setSource_value(codeMasterInfoService.getCodeMaster(source).getValue());
		return para;
	}
	private ParameterInfo paraAfter(ParameterInfo para)
	{
		CodeMasterInfo bigType = new CodeMasterInfo("BigType", null, para.getBig_type_value());
		CodeMasterInfo smallType = new CodeMasterInfo("SmallType", null, para.getSmall_type_value());
		CodeMasterInfo source = new CodeMasterInfo("Source", null, para.getSource_value());
		para.setBig_type_value(codeMasterInfoService.getCodeMaster(bigType).getName());
		para.setSmall_type_value(codeMasterInfoService.getCodeMaster(smallType).getName());
		para.setSource_value(codeMasterInfoService.getCodeMaster(source).getName());
		return para;
	}
	
	@RequestMapping("param")
	public String goProjPage()
	{
		return "/parameterList/parameterList";
	} 
}
