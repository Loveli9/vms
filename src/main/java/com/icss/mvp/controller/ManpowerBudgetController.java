package com.icss.mvp.controller;

import com.icss.mvp.entity.ManpowerBudget;
import com.icss.mvp.service.ManpowerBudgetService;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 人力预算
 */
@RestController
@RequestMapping("/manpowerBudget")
public class ManpowerBudgetController {
	private static Logger logger = Logger.getLogger(ManpowerBudgetController.class);

	@Resource
	private ManpowerBudgetService manpowerBudgetService;
	@Resource
	private HttpServletRequest request;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(ManpowerBudget manpowerBudget) {
		Map<String, Object> map = new HashMap<>();
		try {
			manpowerBudgetService.insert(manpowerBudget);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("ManpowerBudgetController exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping(value = "/del/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> del(@PathVariable int id) {
		Map<String, Object> map = new HashMap<>();
		try {
			manpowerBudgetService.deleteByPrimaryKey(id);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("manpowerBudgetService.deleteByPrimaryKey exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@ResponseBody
	public Map<String, Object> edit(@RequestBody ManpowerBudget manpowerBudget) {
		Map<String, Object> map = new HashMap<>();
		try {
			manpowerBudgetService.updateByPrimaryKeySelective(manpowerBudget);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("manpowerBudgetService.updateByPrimaryKeySelective exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping(value = "/getManpowerBudgetByPmid")
	@ResponseBody
	public Map<String, Object> getManpowerBudgetByProNo(String userid) {
		Map<String, Object> map = new HashMap<>();
		try {
			ManpowerBudget manpowerBudget = manpowerBudgetService.getManpowerBudgetByPmid(userid);
			map.put("data", manpowerBudget);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("manpowerBudgetService.getManpowerBudgetByProNo exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
	@RequestMapping(value = "/getManpowerBudgetByNo")
	@ResponseBody
	public Map<String, Object> getManpowerBudgetByNo(String proNo) {
		Map<String, Object> map = new HashMap<>();
		try {
			ManpowerBudget manpowerBudget = manpowerBudgetService.getManpowerBudgetByNo(proNo);
			map.put("data", manpowerBudget);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			logger.error("manpowerBudgetService.getManpowerBudgetByNo exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
}
