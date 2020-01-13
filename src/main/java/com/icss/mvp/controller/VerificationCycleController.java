package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.service.VerificationCycleService;
import com.icss.mvp.util.DateUtils;

/**
 * 回归周期
 * @author up
 *
 */
@Controller
@RequestMapping("/verificationCycle")
public class VerificationCycleController {
	private final static Logger LOG = Logger.getLogger(VerificationCycleController.class);

	@Autowired
	private VerificationCycleService VerificationCycleService;

	/**
	 * 回归验证周期
	 * @param projNo 项目编号
	 */
	@RequestMapping("/ReturnValidateCycle")
	@ResponseBody
	public Map<String, Object> returnValidateCycle(String projNo,String yearNow) {
		String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		Map<String, Object> results = VerificationCycleService.returnValidateCycle(projNo,months,yearNow);
		return results;
	}
	/**
	 * 回归验证周期 项目删选信息，获取多个项目编号
	 * @param proj 
	 */
	@RequestMapping("/returnValidateCycleByNos")
	@ResponseBody
	public Map<String, Object> returnValidateCycleByNos(ProjectInfo proj) {
		String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		Map<String, Object> results = VerificationCycleService.returnValidateCycleByNos(proj,months);
		return results;
	}
	
	/**
	 * 回归验证周期
	 * 取一个月的，当前月的前一月
	 * @param projNo 项目编号
	 */
	@RequestMapping("/ReturnValidateCycleToday")
	@ResponseBody
	public Map<String, Object> returnValidateCycleToday(String projNo,String yearNow) {
		String month = DateUtils.getSystemFewMonth(0);
		int len = month.length();
		month = month.substring(len-2, len);
		Map<String, Object> results = VerificationCycleService.returnValidateCycle(projNo,month,yearNow);
		return results;
	}
	
	/**
	 * 全量功能验证周期/解决方案验证周期    项目删选信息，获取多个项目编号
	 * @param projNo 项目编号
	 */
	@RequestMapping("/totalValidateCycle")
	@ResponseBody
	public Map<String, Object> totalValidateCycle(String projNo,String yearNow) {
		String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		Map<String, Object> results = VerificationCycleService.totalValidateCycle(projNo,months,yearNow);
		return results;
	}
	/**
	 * 全量功能验证周期/解决方案验证周期
	 * @param projNo 项目编号
	 */
	@RequestMapping("/totalValidateCycleNos")
	@ResponseBody
	public Map<String, Object> totalValidateCycleNos(ProjectInfo proj) {
		String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		Map<String, Object> results = VerificationCycleService.totalValidateCycleNos(proj,months);
		return results;
	}
	
	/**
	 * 全量功能验证周期/解决方案验证周期
	 * 取一个月的，当前月的前一月
	 * @param projNo 项目编号
	 */
	@RequestMapping("/totalValidateCycleToday")
	@ResponseBody
	public Map<String, Object> totalValidateCycleToday(String projNo,String yearNow) {
		String month = DateUtils.getSystemFewMonth(0);
		int len = month.length();
		month = month.substring(len-2, len);
		Map<String, Object> results = VerificationCycleService.totalValidateCycle(projNo,month,yearNow);
		return results;
	}

}