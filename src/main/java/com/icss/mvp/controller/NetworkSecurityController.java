package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.NetworkSecurity;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.NetworkSecurityService;

@Controller
@RequestMapping("/networkSecurity")
public class NetworkSecurityController {
	private static Logger logger = Logger.getLogger(NetworkSecurityController.class);
	@Autowired
	private NetworkSecurityService networkSecurityService;
	
	@RequestMapping("/getMonths")
    @ResponseBody
    public PlainResponse<List<Map<String, String>>> queryMonths(int num) {
		PlainResponse<List<Map<String, String>>> ret = new PlainResponse<List<Map<String, String>>>();
		try {
			ret.setData(networkSecurityService.getMonths(num));
			ret.setCode("success");
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}
	
	@RequestMapping("/saveNetworkSecurity")
    @ResponseBody
    public BaseResponse saveNetworkSecurity(@RequestBody NetworkSecurity netSec) {
		BaseResponse ret = new BaseResponse();
		try {
			networkSecurityService.saveNetworkSecurity(netSec);
			ret.setCode("success");
		} catch (Exception e) {
			logger.error("保存数据异常", e);
		}
		return ret;
	}
	
	@RequestMapping("/queryNetworkSecurity")
    @ResponseBody
    public TableSplitResult<List<NetworkSecurity>> queryNetworkSecurity(NetworkSecurity netSec, String date, String type, PageRequest pageRequest) {
		TableSplitResult<List<NetworkSecurity>> ret = new TableSplitResult<List<NetworkSecurity>>();
		try {
			ret = networkSecurityService.queryNetworkSecurity(netSec, date, type, pageRequest);
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}
	
	
	@RequestMapping("getNetworkSecurityByKey")
    @ResponseBody
    public PlainResponse<NetworkSecurity> getNetworkSecurityByKey(String proNo, String date) {
		PlainResponse<NetworkSecurity> ret = new PlainResponse<NetworkSecurity>();
		try {
			ret.setData(networkSecurityService.getNetworkSecurityByKey(proNo, date));
			ret.setCode("success");
		} catch (Exception e) {
			logger.error("查询数据异常", e);
		}
		return ret;
	}
}
