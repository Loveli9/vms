package com.icss.mvp.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.CodeGainType;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.CodeGainTypeService;

@Controller
@SuppressWarnings("all")
@RequestMapping("/codeType")
public class CodeGainTypeController {
	 private static Logger logger = Logger.getLogger(CodeGainTypeController.class);
	@Autowired
	private CodeGainTypeService service;
	
	@RequestMapping("/saveCodeType")
	@ResponseBody
	public BaseResponse saveCodeGainType(String no,String type,String textMeanType, String online, String trusted, String po) {
		BaseResponse result = new BaseResponse();
		try {
			service.saveCodeGainType(type, no,textMeanType, online, trusted, po);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("service.saveCodeGainType exception, error: "+e.getMessage());
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/codeTypeConfig")
	@ResponseBody
	public PlainResponse getCodeTypeByNo(String no) {
		PlainResponse result = new PlainResponse();
		try {
			List<CodeGainType> gainType =  service.getCodeTypeByNo(no);
			result.setCode("success");
			result.setData(gainType);
		} catch (Exception e) {
			logger.error("service.getCodeTypeByNo exception, error: "+e.getMessage());
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
		
	}
	
	@RequestMapping("/emailConfig")
	@ResponseBody
	public PlainResponse emailConfig(String no,String emailUrl,String emailOnOff) {
		PlainResponse result = new PlainResponse();
		try {
			result.setData(service.emailConfig(no,emailUrl,emailOnOff));
			result.setCode("success");
		} catch (Exception e) {
			logger.error("service.emailConfig exception, error: "+e.getMessage());
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
		
	}
	
	@RequestMapping("/getEmailConfig")
	@ResponseBody
	public PlainResponse getEmailConfig(String no) {
		PlainResponse result = new PlainResponse();
		try {
			result.setData(service.getEmailConfig(no));
			result.setCode("success");
		} catch (Exception e) {
			logger.error("service.getEmailConfig exception, error: "+e.getMessage());
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
		
	}
}
