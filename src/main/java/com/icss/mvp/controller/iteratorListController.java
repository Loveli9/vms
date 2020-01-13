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
@RequestMapping("/iteratorList")
public class iteratorListController {
	private static Logger logger = Logger.getLogger(iteratorListController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private IndexService pcbListService;

	@RequestMapping("/demoImage")
	@ResponseBody
	public PlainResponse<List<Map<String, Object>>> demoImage(String proNo) {
		PlainResponse<List<Map<String, Object>>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1000", "1001", "1002", "1003", "1004", "1005", "1006" };
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
	 * @Description:迭代进度偏差指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/iteratorProgressDeviation")
	@ResponseBody
	public PlainResponse<Map<String, Object>> iteratorProgressDeviation(String proNo) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1053", "1054", "1055", "1056", "1057" };
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
	 * @Description:迭代工作量偏差指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/iteratorWorkloadDeviation")
	@ResponseBody
	public PlainResponse<Map<String, Object>> iteratorWorkloadDeviation(String proNo) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1058", "1059", "1060", "1061" };
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
	 * @Description:迭代问题解决指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/iteratorProblemSolving")
	@ResponseBody
	public PlainResponse<Map<String, Object>> iteratorProblemSolving(String proNo) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1062", "1063", "1064", "1065", "1066", "1067", "1068", "1069" };
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
	 * @Description:测试报告指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/iteratorTestReport")
	@ResponseBody
	public PlainResponse<Map<String, Object>> iteratorTestReport(String proNo) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1070", "1071", "1072" };
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
	 * @Description:遗留DI指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/iteratorLegacyDI")
	@ResponseBody
	public PlainResponse<Map<String, Object>> iteratorLegacyDI(String proNo) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<>();
		try {
			String[] ids = new String[] { "1073", "1074", "1075", "1076", "1077", "1078", "1079" };
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
