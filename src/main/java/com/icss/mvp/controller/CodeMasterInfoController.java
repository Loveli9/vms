package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.CodeMasterInfo;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.service.CodeMasterInfoService;

@RestController
@RequestMapping("/codeMaster")
public class CodeMasterInfoController {
    
	@Resource
	private CodeMasterInfoService codeMasterInfoService;
	
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> getCodeMaster(CodeMasterInfo c){
		List<CodeMasterInfo> list = codeMasterInfoService.getList(c);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}
	
	@RequestMapping(value = "/getCodeMasterOrder",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public ListResponse<Map<String, Object>> getCodeMasterOrder(CodeMasterInfo c){
		ListResponse<Map<String, Object>> response = new ListResponse<Map<String, Object>>();
		List<Map<String, Object> > master = codeMasterInfoService.getCodeMasterOrder(c);
		try {
			response.setCode("success");
			response.setData(master);
		} catch (Exception e) {
			response.setCode("failure");
			response.setMessage("操作失败");
		}
		return response;
	}
	
	@RequestMapping("/getCodeMasterOrderByValue")
	@ResponseBody
	public Map<String, Object> getCodeMasterOrderByValue(CodeMasterInfo c){
		List<CodeMasterInfo> list = codeMasterInfoService.getCodeMasterOrderByValue(c);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}

	@RequestMapping("/getCodeMasterOrderByValues")
	@ResponseBody
	public  ListResponse<Map<String, Object>>getCodeMasterOrderByValues(CodeMasterInfo c){
		ListResponse<Map<String, Object>> response = new ListResponse<Map<String, Object>>();
		try{
			List<Map<String, Object>> data = codeMasterInfoService.getCodeMasterOrderByValues(c);
			response.setCode("success");
			response.setData(data);
		}catch (Exception e){
			response.setCode("failure");
			response.setMessage("操作失败");
		}
		return response;
	}

	// 成员状态
	@RequestMapping("/getMemberStatusValue")
	@ResponseBody
	public Map<String, Object> getMemberStatusValue(){
		return codeMasterInfoService.getMemberStatusValue();
		
	}
	
	// 团队下所有项目的PO号
	@RequestMapping("/getProjectPOCode")
	@ResponseBody
	public List<Map<String, Object>> getProjectPOCode(String no) {
		return codeMasterInfoService.getProjectPOCode(no);

	}
	
	// 成员SVN/GIT账号
	@RequestMapping("/getMemberSVN")
	@ResponseBody
	public Map<String, Object> getMemberSVN(String no) {
		return codeMasterInfoService.getMemberSVN(no);

	}
	
	// 成员职级
	@RequestMapping("/getMemberRankValue")
@ResponseBody
public Map<String, Object> getMemberRankValue(){
	return codeMasterInfoService.getMemberRankValue();

}

	@RequestMapping("/getMemberRankValues")
	@ResponseBody
	public ListResponse<Map<String, Object>> getMemberRankValues(){
		ListResponse<Map<String, Object>> response = new ListResponse<Map<String, Object>>();
		try{
			List<Map<String, Object>> data = codeMasterInfoService.getMemberRankValues();
			response.setCode("success");
			response.setData(data);
		}catch(Exception e){
			response.setCode("failure");
			response.setMessage("操作失败");
		}

        return response;
	}



}


