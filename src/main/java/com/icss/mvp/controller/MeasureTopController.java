/**
 * 
 */
package com.icss.mvp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.Measure;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.service.MeasureService;
import com.icss.mvp.service.MeasureTopService;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.util.CookieUtils;

/**
 * @author user
 *
 */
@RestController
@RequestMapping("/measureTop")
public class MeasureTopController {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MeasureService measureService;

	@Autowired
	private MeasureTopService measureTopService;

	@Autowired
	private ProjectInfoService projectInfoService;

	private static Logger logger = Logger.getLogger(MeasureTopController.class);

	@RequestMapping("/getTopMeasure")
	@ResponseBody
	/**
	 * 
	 * @param projectInfo
	 * @return
	 */
	public Map<String, Object> getTopMeasure(@ModelAttribute ProjectInfo projectInfo) {
		
		Map<String, Object> result = new HashMap<>();
		String username = CookieUtils.value(request, CookieUtils.USER_NAME);
		
		try {
			Set<String> projNos = projectInfoService.projectNos(projectInfo, username);

			if (projNos.size() <= 0) {
				result.put("status", "0");
				result.put("msg", "无在行项目");
				logger.info("No project exists!");
				return result;
			}
			List<Measure> currentRanking = measureTopService.getCurrentMonthTopMeasure(projNos);
			currentRanking.sort(((o1, o2) -> o1.getTimes() > o2.getTimes() ? -1 : 1));
			
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			List<String> types = new ArrayList<String>();
			List<Long> idList = currentRanking.stream().limit(5).map(Measure::getId).collect(Collectors.toList());
			for (Long id : idList) {
				Measure m = measureService.getMeasureById(id.toString());
				List<Measure> yearList = measureTopService.statisticsByID(projNos, id);
				types.add(m.getName());
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("name", m.getName());
				tmpMap.put("value", yearList.stream().limit(12).map(Measure::getTimes).collect(Collectors.toList()));
				dataList.add(tmpMap);

			}

			result.put("status", "0");
			result.put("msg", "返回成功");
			result.put("dataList", dataList);
			result.put("types", types);
			result.put("months", generateMonth());
			
		} catch (Exception e) {
			logger.error("measureTopService.getCurrentMonthTopMeasure exception, error: "+e.getMessage());
			result.put("status", "1");
			result.put("msg", "返回失败");
		}

		return result;
	}

	private List<String> generateMonth() {
		List<String> months = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		for (int i = 1; i <= 12; i++) {
			months.add(year + "-" + i);
		}
		return months;

	}

}
