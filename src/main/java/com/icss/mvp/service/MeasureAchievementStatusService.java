package com.icss.mvp.service;

import com.icss.mvp.dao.MeasureAchievementStatusDao;
import com.icss.mvp.entity.MeasureComment;
import com.icss.mvp.service.project.ProjectReviewService;
import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.MeasureUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@EnableScheduling
@SuppressWarnings("all")
@PropertySource("classpath:task.properties")
public class MeasureAchievementStatusService {
	private static Logger logger = Logger.getLogger(MeasureAchievementStatusService.class);

	@Resource
	ProjectInfoService projectInfoService;
	
	@Autowired
	private MeasureAchievementStatusDao dao;
	
	@Resource
	private MeasureCommentService measureCommentService;
	
	@Autowired
  private ProjectReviewService projectReviewService;
	
//	@Scheduled(cron = "${measure_achievement_status}")
	public void insertMeasureAchievementStatus() {
	  List<String> dates = DateUtils.getLatestCycles(3, true);
		insertMeasureAchievementStatusMiddle(dates);
		String now = DateUtils.SHORT_FORMAT_GENERAL.format(DateUtils.getMonthLastDay(dates.get(0).substring(0,7)));
		dao.initNetworkSecurity(now, dates.get(1));
	}
	
//	@Scheduled(cron = "${measure_achievement_status_middle}")
  public void insertMeasureAchievementStatusMiddle() {
	  List<String> dates = DateUtils.getLatestCycles(3, true);
    insertMeasureAchievementStatusMiddle(dates);
	}
	
	public void updateProjectOfflineStatus(String no, String offlineDate, String offline, String offlineMark) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("createTime", new Date());
		map.put("modifyTime", new Date());
		map.put("offline", offline);
		map.put("offlineMark", offlineMark);
		map.put("no", no);
		map.put("offlineDate", offlineDate);
		
		int codeGTCount = dao.getCodeGainTypeNum(no, offlineMark);
		if (codeGTCount > 0) {
			dao.updateCodeGainType(map);
		} else {
			dao.addCodeGainType(map);
		}
		
		int projectPRCount = dao.getProjectParameterRecordNum(no, offlineMark, offlineDate);
		if (projectPRCount > 0) {
			dao.updateProjectParameterRecord(map);
		} else {
			dao.addProjectParameterRecord(map);
		}
	}
	
	private void saveProjectParameterRecord(List<String> dates) {
		dao.saveProjectParameterRecord(dates.get(0), dates.get(1));
	}
	public void insertMeasureAchievementStatusMiddle(List<String> dateList) {
		logger.info("更新项目上线、质量、可信状态");
		saveProjectParameterRecord(dateList);
		
		projectReviewService.publishAllProjectReview(dateList.get(1));
		
		logger.info("计算项目指标点灯状态并存储");
		String measureColor = "";
		List<String> zongLanNos = dao.queryZongLanNos(dateList.get(0), "162");
		List<String> nos = dao.queryGaoSiNo(dateList.get(1), Arrays.asList("162"));
		/*List<String> keXinNos = dao.queryKeXinNo(dateList.get(1), Arrays.asList("164", "162"));

		HashSet<String> tempNos = new HashSet<String>();
		for (String no : gaoSiNos) {
			tempNos.add(no);
		}
		for (String no : keXinNos) {
			tempNos.add(no);
		}
		List<String> nos = new ArrayList<>(tempNos);*/
		
		String qualityIndex = "223,311,309,337,385,307,308,387";
		List<String> credible = dao.queryCredibleIndex("630");
		String credibleIndex = String.join(",", credible);
		
		Map<String, Object> map = new HashMap<>();
		for(String no : nos){
			List<String> measureIds = new ArrayList<>();
			String measures = measureCommentService.measureConfigurationRecord(no, dateList.get(1));
			
			if (StringUtils.isNotBlank(measures)) {
				measures = measures.replaceAll(" ", "");
				measureIds = CollectionUtilsLocal.splitToList(measures);
				if (null != measureIds && measureIds.size() > 0) {
					List<MeasureComment> measureComments = dao.queryMeasureStatus(dateList.get(1), dateList.get(2),
							measureIds, no);
					for (MeasureComment measure : measureComments) {
						int measureQualify = -1;
						if (measure.getMeasureId() != 0 && StringUtils.isNotBlank(measure.getMeasureValue())) {
							if (measure.getChangeValue() != null && measure.getChangeValue() != 4 && measure.getChangeValue() != 0) {
								measureQualify = measure.getChangeValue() == 1 ? 0 : 1;
								if (measure.getChangeValue() == 2) {
									measureColor = "yellow";
								} else if (measure.getChangeValue() == 3) {
									measureColor = "red";
								} else {
									measureColor = "green";
								}
							} else {
								measureColor = MeasureUtils.light(measure);
								if (StringUtils.isNotBlank(measureColor)) {
									measureQualify = "green".equals(measureColor) ? 0 : 1;
								}
							}
							String measureId = String.valueOf(measure.getMeasureId());
							if("303".equals(measureId) || "305".equals(measureId) || "306".equals(measureId)){
								measureId = "223";
							}
							String mark = "-1";
							if(qualityIndex.contains(measureId)){
								mark = "163";
							}else if(credibleIndex.contains(measureId)){
								mark = "164";
							}
							map.put("measureId", measureId);
							map.put("no", no);
							map.put("nowDate", dateList.get(1));
							map.put("measureQualify", String.valueOf(measureQualify));
							map.put("measureColor", measureColor);
							map.put("measureValue", measure.getMeasureValue());
							map.put("upper", measure.getUpper());
							map.put("lower", measure.getLower());
							map.put("measureMark", mark);
							map.put("challenge", measure.getChallenge());
							map.put("target", measure.getTarget());
							map.put("collectType", measure.getCollectType());
							map.put("computeRule", measure.getComputeRule());

							dao.insertMeasureQualify(map);
						}
					}
				} 
			}
		}
		for (String no : zongLanNos) {
			// 1-项目结项
			Map<String, Object> projectLimit = dao.getProjectLimit(no, "1");
			if (null != projectLimit && !projectLimit.isEmpty()) {
				String projectLimitMark = StringUtilsLocal.valueOf(projectLimit.get("is_close"));
				String projectLimitDate = StringUtilsLocal.valueOf(projectLimit.get("statistical_time"));
				if ("1".equals(projectLimitMark) && StringUtils.isNotBlank(projectLimitDate)) {
					String offlineDate = DateUtils.getNextCycle(projectLimitDate);
					// 1-项目下线
					updateProjectOfflineStatus(no, offlineDate, "1", "162");
					updateProjectOfflineStatus(no, offlineDate, "1", "163");
					updateProjectOfflineStatus(no, offlineDate, "1", "164");
				}
			}
		}
	}

}
