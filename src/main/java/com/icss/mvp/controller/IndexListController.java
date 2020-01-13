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
@RequestMapping("/IndexList")
public class IndexListController {
	private static Logger logger = Logger.getLogger(IndexListController.class);

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
	 * @Description:开发迭代验收指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getDevelopIterative")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getDevelopIterative(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1000", "1001","1002","1003","1004","1005","1006" };
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
	 * @Description:自动化迭代验收指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getAutoIterative")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getAutoIterative(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1007", "1008", "1009" };
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
	 * @Description:开发结项验收指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getDevelopPdspIterative")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getDevelopPdspIterative(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1010", "1011","1012","1013","1014","1015", "1016"};
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
	 * @Description:开发遗留验收指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getDevelopLeaveIterative")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getDevelopLeaveIterative(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1017", "1018", "1019", "1020", "1021" };
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
	 * @Description:测试结项验收指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getTestPdspIteration")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getTestPdspIteration(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1022" };
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
	 * @Description:测试结项验收指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getAutomation")
	@ResponseBody
	public PlainResponse<Map<String,Object>> getAutomation(String proNo) {
		PlainResponse<Map<String,Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "24", "1036", "1037", "1038", "1039", "1040", "1041",
					"1042", "1043", "1044", "1045", "1046", "1047", "1048", "1049", "1050", 
					"1051", "1052"};
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
