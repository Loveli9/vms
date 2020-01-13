package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.service.job.JobProjectReachStandardService;

@Controller
@RequestMapping("/projectReachStandard")
public class ProjectReachStandardController {

	private final static Logger LOG = Logger.getLogger(ProjectReachStandardController.class);

	@Autowired
	private JobProjectReachStandardService projectReachStandardService;

	/**
	 * 首页加载合格率柱状图
	 * 
	 * @return
	 */
	@RequestMapping("/reachStandardPDU")
	@ResponseBody
	public List<List<Map<String, Object>>> lowlocPDU(ProjectInfo proj) {
		List<List<Map<String, Object>>> list = null;
		try {
			list = projectReachStandardService.queryReach(proj);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
		return list;
	}

	/**
	 * 加载选中的不合格项目
	 */
	@RequestMapping(value = "/unreaches")
	@ResponseBody
	public Map<String, Object> questionList(@ModelAttribute ProjectInfo proj, @ModelAttribute PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Map<String, Object>> maps = projectReachStandardService.queryProjNoAndValue(proj, pageInfo);
			String title = "[{\"field\":\"项目名称\",\"title\":\"项目名称\"},{\"field\":\"合格率\",\"title\":\"合格率\"},{\"field\":\"未达成指标\",\"title\":\"未达成指标\"},{\"field\":\"未达成指标个数\",\"title\":\"未达成指标个数\"}]";
			Object gridTitles = JSON.parse(title);
			map.put("gridTitles", gridTitles);
			JSONObject json = new JSONObject();
			json.put("total", maps.size());
			JSONArray jsonArray = new JSONArray();
			for (Map<String, Object> temp : maps) {
				JSONObject projectInfoObject = new JSONObject();
				projectInfoObject.put("项目编码", temp.get("no"));
				projectInfoObject.put("项目名称", temp.get("name"));
				projectInfoObject.put("合格率", temp.get("rate"));
				projectInfoObject.put("未达成指标个数", temp.get("num"));
				projectInfoObject.put("未达成指标", temp.get("some"));
				jsonArray.add(projectInfoObject);
			}
			json.put("rows", jsonArray);
			map.put("gridDatas", json);
		} catch (Exception e) {
			LOG.error("projectReachStandardService.queryProjNoAndValue exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

}
