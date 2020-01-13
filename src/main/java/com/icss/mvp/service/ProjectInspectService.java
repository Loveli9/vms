package com.icss.mvp.service;

import com.icss.mvp.dao.DeliverDao;
import com.icss.mvp.dao.MeasureAchievementStatusDao;
import com.icss.mvp.entity.DeliverResult;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service("ProjectInspectService")
public class ProjectInspectService {

	private static Logger logger = Logger.getLogger(ProjectInspectService.class);

	@Autowired
	DeliverDao deliverDao;


	/**
	 * 需求完成情况
	 * 
	 * @param projectInfo
	 * @param pageRequest
	 * @param date
	 * @return
	 */
	public TableSplitResult<List<Map<String,String>>> queryDemandFinish(ProjectInfo projectInfo,
																		  PageRequest pageRequest, String date) {

		TableSplitResult<List<Map<String,String>>> result = new TableSplitResult<>();
		try {
			List<Map<String,String>> list = new ArrayList<>();
			for(int i=0; i<5; i++){
				Map<String,String> map =  new HashMap<>(0);
				map.put("demandNumber",""+i);
				map.put("demandName",""+i);
				map.put("state",""+i);
				list.add(map);
			}
			if (list.size() <= 0) {
				result.setErr(new ArrayList<>(), 1);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception e) {
			logger.error("queryResourceReport exception, error:", e);
		}


		return result;
	}

	/**
	 * 遗留DI
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param date
	 * @return
	 */
	public TableSplitResult<List<Map<String,String>>> queryLeftOver(ProjectInfo projectInfo,
																		PageRequest pageRequest, String date) {

		TableSplitResult<List<Map<String,String>>> result = new TableSplitResult<>();
		try {
			List<Map<String,String>> list = new ArrayList<>();
			String[] arr ={"遗留DI值","遗留问题数（致命）","遗留问题数（严重）","遗留问题数（一般）","遗留问题数（提示）","遗留问题总数","新增代码规模","新增自动化脚本用例数","结项验收漏测数"};
			for(int i=0; i<arr.length; i++){
				Map<String,String> map =  new HashMap<>(0);
				map.put("acceptanceIndicators",arr[i]);
				list.add(map);
			}
			if (list.size() <= 0) {
				result.setErr(new ArrayList<>(), 1);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception e) {
			logger.error("queryResourceReport exception, error:", e);
		}


		return result;
	}

	/**
	 * 验收评价
	 *
	 * @param projectInfo
	 * @param pageRequest
	 * @param date
	 * @return
	 */
	public TableSplitResult<List<Map<String,String>>> queryEvaluation(ProjectInfo projectInfo,
																		PageRequest pageRequest, String date) {

		TableSplitResult<List<Map<String,String>>> result = new TableSplitResult<>();
		try {
			List<Map<String,String>> list = new ArrayList<>();
			String[] arr ={"骨干稳定度","交付及时性","过程交付质量","验收质量","关键黑事件","总分"};
			String[] arr1 ={"20%","20%","20%","40%","扣分项","100%"};
			for(int i=0; i<arr.length; i++){
				Map<String,String> map =  new HashMap<>(0);
				map.put("evaluateItem",""+arr[i]);
				map.put("weight",""+arr1[i]);
				list.add(map);
			}
			if (list.size() <= 0) {
				result.setErr(new ArrayList<>(), 1);
			}
			result.setRows(list);
			result.setTotal(list.size());
		} catch (Exception e) {
			logger.error("queryResourceReport exception, error:", e);
		}


		return result;
	}
}
