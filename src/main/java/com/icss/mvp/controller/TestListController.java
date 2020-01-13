package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.IndexService;

/**
 * 月指标计算
 * 
 * @author chenweipu
 *
 */
@Controller
@RequestMapping("/testList")
public class TestListController {
	private static Logger logger = Logger.getLogger(TestListController.class);

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private IndexService pcbListService;

	@RequestMapping("/demoImage")
	@ResponseBody
	public PlainResponse<List<Map<String, Object>>> demoImage(String proNo) {
		PlainResponse<List<Map<String, Object>>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1000", "1001","1002","1003","1004","1005","1006" };
			result.setData(pcbListService.getParameterValuesByMouthImage(proNo, ids));
			result.setMessage("返回成功");
			result.setCode("success");
		} catch (Exception e) {
			logger.error("交易失败", e);
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	
	/**
	 * @Description:测试漏测指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getTestMissing")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getDevelopIterative(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1023", "1024","1025","1026","1027" };
			result.setData(pcbListService.getParameterValuesByMouth(proNo, ids));
			result.setMessage("返回成功");
			result.setCode("success");
		} catch (Exception e) {
			logger.error("交易失败", e);
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	/**
	 * @Description:测试缺陷密度指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getTestDefectDensity")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getAutoIterative(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1028", "1029", "1030" };
			result.setData(pcbListService.getParameterValuesByMouth(proNo, ids));
			result.setMessage("返回成功");
			result.setCode("success");
		} catch (Exception e) {
			logger.error("交易失败", e);
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	/**
	 * @Description:用例发现缺陷比例指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getTestcaseDefectRate")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getDevelopPdspIterative(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1031", "1032","1033","1034","1035" };
			result.setData(pcbListService.getParameterValuesByMouth(proNo, ids));
			result.setMessage("返回成功");
			result.setCode("success");
		} catch (Exception e) {
			logger.error("交易失败", e);
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}

}
