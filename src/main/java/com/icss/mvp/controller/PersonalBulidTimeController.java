package com.icss.mvp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.icss.mvp.dao.IProjectSourceConfigDao;
import com.icss.mvp.dao.ISvnTaskDao;
import com.icss.mvp.dao.project.IProjectDao;
import com.icss.mvp.entity.PersonalBulidTime;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.RepositoryInfo;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.project.ProjectBaseEntity;
import com.icss.mvp.service.PersonalBulidTimeService;
import com.icss.mvp.service.TestMeasuresService;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.HttpExecuteUtils;
import com.icss.mvp.util.StringUtilsLocal;

@Controller
@RequestMapping("/personalBulidTime")
public class PersonalBulidTimeController {
	private final static Logger logger = Logger.getLogger(PersonalBulidTimeController.class);

	@Value("${personalBulidTimeUrl}")
	private String personalBulidTimeUrl;

	@Resource
	HttpServletRequest request;

	@Resource
	HttpServletResponse response;

	@Autowired
	private PersonalBulidTimeService personalBulidTimeService;

	@Autowired
	private TestMeasuresService testMeasuresService;

//	@Autowired
//	private ICodeCheckDao codeCheckDao;

    @Autowired
    private IProjectSourceConfigDao sourceDao;

	@Autowired
	ISvnTaskDao svnTaskDao;
    
	/**
	 * 采集个人构建时长
	 * 
	 * @param
	 */
	@SuppressWarnings("all")
	@RequestMapping("/insertBuildTime")
	@ResponseBody
	public PlainResponse insertBuildTime(String no,String token,String id) {
		PlainResponse result = new PlainResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		List<RepositoryInfo> infos = sourceDao.searchRepositoryByNos(no,"6",id);
		String pbiId = "";
		if (infos != null && infos.size()>0) {
			for (RepositoryInfo repositoryInfo : infos) {
				pbiId=repositoryInfo.getUrl();
			}
        }
		long startTime = System.currentTimeMillis();// 采集开始时间
		int a = caiji(pbiId, no);
		long endTime = System.currentTimeMillis();// 采集结束时间
		String mesType = "采集个人构建时长数据......";
		String zxResult = "采集成功";
		result.setCode("success");
		if (a == 0) {
			zxResult = "采集异常";
			result.setCode("fail");
		}else if (a == 2) {
			result.setCode("no");
		}
		map.put("mesType", mesType);
		map.put("zxResult", zxResult);
		map.put("workTime", StringUtilsLocal.formatTime(endTime - startTime));
		result.setData(map);
		return result;
	}

	@SuppressWarnings("unchecked")
	private int caiji(String pbiId, String projNo) {
		if(StringUtilsLocal.isBlank(pbiId)) {
			return 2;
		}
		// 如果没有该项目数据，就第一次采集
		Integer count = personalBulidTimeService.queryAllBuilds(projNo);
		// 最先查询当月已经采集到的构建信息条数
		Integer num = personalBulidTimeService.queryNowBulid(DateUtils.getMonth(), projNo);
		if (num == 0) {
			// num=0，说明是当月第一次采集数据，应先把上个月的重新采集一遍，确保完整
			// 上个月的数据先删后新增
			personalBulidTimeService.deleteNowBulid(DateUtils.getPreMonth(), projNo);
		}
		// 删除当月已经获取到的构建信息重新采集
		personalBulidTimeService.deleteNowBulid(DateUtils.getMonth(), projNo);
		// 重新获得当月的构建信息，确保刷新
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, Object> para = new HashMap<>();
		para.put("pbi_id", pbiId);
		para.put("page_size", 1);
		if (count == 0) {
			para.put("start_time", getStartDate(projNo));// 从项目起始月开始查询
		} else if (num == 0) {
			para.put("start_time", DateUtils.getFirstDayPreMonth());// 从上个月开始查询
		} else {
			para.put("start_time", DateUtils.getFirstDayNowMonth());// 从当前月开始查询
		}
		List<PersonalBulidTime> listInsert = new ArrayList<>();
		try {
			for (int i = 1; true; i++) {
				para.remove("page_index");
				para.put("page_index", i);
				String result = HttpExecuteUtils.httpGet(personalBulidTimeUrl, para);
				resultMap = JSON.parseObject(result, Map.class);
				Map<String, Object> mapTemp = (Map<String, Object>) (resultMap.get("result"));
				if (mapTemp.get("reason") != null) {
					break;
				}
				List<Map<String, Object>> list = (List<Map<String, Object>>) (mapTemp.get("build_list"));
				for (Map<String, Object> map : list) {
					// 排除异常情况，即开始时间大于结束时间的情况
					if (!DateUtils.comparisonDateSizeYMdHms(String.valueOf(map.get("start_time")),
							String.valueOf(map.get("end_time")))) {
						continue;
					}
					if (!"success".equals(String.valueOf(map.get("build_result")))) {
						continue;
					}
					PersonalBulidTime pbt = new PersonalBulidTime();
					pbt.setProId(projNo);
					pbt.setPbiId(String.valueOf(map.get("pbi_id")));
					pbt.setPbiName(String.valueOf(map.get("pbi_name")));
					pbt.setStartTime(String.valueOf(map.get("start_time")));
					pbt.setEndTime(String.valueOf(map.get("end_time")));
					pbt.setBulidType(String.valueOf(map.get("build_type")));
					pbt.setBulidResult(String.valueOf(map.get("build_result")));
					listInsert.add(pbt);
				}
			}
			if (listInsert != null && listInsert.size() > 0) {
				personalBulidTimeService.insertBulid(listInsert);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("NO", projNo);
			map.put("lasttime", new Date());
			map.put("id", pbiId);
			svnTaskDao.insertlasttime(map);
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("svnTaskDao.insertlasttime exception, error: "+e.getMessage());
			return 0;
		}
	}

	/**
	 * 绘制个人构建折线图
	 * 
	 * @param
	 */
	@RequestMapping("/getPersonalBulidTime")
	@ResponseBody
	public Map<String, Object> personalBulidTime(String projNo, String year) {
		Map<String, Object> results = new HashMap<>();
		// 计算每个月的构建时长
		String mon = year;
		for (int i = 1; i <= 12; i++) {
			if (i < 10) {
				year = mon + "-0" + String.valueOf(i);
			} else {
				year = mon + "-" + String.valueOf(i);
			}
			List<Map<String, Object>> buildList = personalBulidTimeService.buildPerMonth(year, projNo);
			Double time = 0.0;
			if (buildList != null && buildList.size() != 0) {
				for (Map<String, Object> map : buildList) {
					Double minutes = DateUtils.comparisonDateSizeYMdHmsTime(String.valueOf(map.get("start_time")),
							String.valueOf(map.get("end_time")));
					time = time + minutes;
				}
				time = testMeasuresService.keepTwoDecimals(time / buildList.size());
				results.put("mon" + String.valueOf(i), time);
			} else {
				results.put("mon" + String.valueOf(i), null);
			}
		}
		return results;

	}

	@RequestMapping("/savePbiId")
	@ResponseBody
	public String savePbiId(String projNo, String pbiId, String pbiName) {
		String result = personalBulidTimeService.savePbiId(projNo, pbiId, pbiName);
		return result;
	}

	@RequestMapping("/readPbiId")
	@ResponseBody
	public String readPbiId(String projNo) {
		return personalBulidTimeService.queryPbiId(projNo);
	}

	@RequestMapping("/readPbiName")
	@ResponseBody
	public String readPbiName(String projNo) {
		return personalBulidTimeService.queryPbiName(projNo);
	}

	/**
	 * @Description: 获取最新个人构建时长(气泡项目级)
	 * @author Administrator
	 * @date 2018年6月29日
	 */
	@RequestMapping("/queryBuildTimeNew")
	@ResponseBody
	public Map<String, Object> queryBuildTimeNew(String no) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Double value = 0.0;
		Double time = 0.0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		String month = dateFormat.format(new Date());
		List<Map<String, Object>> buildList = personalBulidTimeService.buildPerMonth(month, no);
		for (Map<String, Object> map : buildList) {
			Double minutes = DateUtils.comparisonDateSizeYMdHmsTime(String.valueOf(map.get("start_time")),
					String.valueOf(map.get("end_time")));
			time = time + minutes;
		}
		if (time * buildList.size() > 0) {
			value = testMeasuresService.keepTwoDecimals(time / buildList.size());
		}
		resultMap.put("result", value);
		return resultMap;
	}

	/**
	 * 获取部门级别个人构建时长(部门级别图表) 首页图表
	 * 
	 * @param
	 */
	@RequestMapping("/getPersonalBulidTimeByNos")
	@ResponseBody
	public Map<String, Object> getPersonalBulidTimeByNos(ProjectInfo proj) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = personalBulidTimeService.getPersonalBulidTimeByNos(proj);
			map.put("code", 200);
		} catch (Exception e) {
			logger.error("personalBulidTimeService.getPersonalBulidTimeByNos exception, error: "+e.getMessage());
			map.put("code", 400);
		}
		return map;
	}
	
	/**
	 * 判断项目是否进行了smartIDE配置
	 * 
	 * @param
	 */
	@RequestMapping("/isHaveConfigIde")
	@ResponseBody
	public BaseResponse isHaveConfigIde(String proNo) {
		BaseResponse result = new BaseResponse();
		int a = personalBulidTimeService.isHaveConfigIde(proNo);
		if(a>0) {
			result.setCode("200");
		}else {
			result.setCode("400");
		}
		return result;
	}

    @Autowired
    IProjectDao projectDao;

    private String getStartDate(String projectId) {
        String result = "";

        ProjectBaseEntity project = null;
        try {
            List<ProjectBaseEntity> projects = projectDao.getProjectList(Collections.singleton(projectId));
            if (projects != null && !projects.isEmpty()) {
                project = projects.get(0);
            }
        } catch (Exception e) {
            logger.error("projectDao.getProjectList exception, error: " + e.getMessage());
        }

        if (project != null && project.getBeginDate() != null) {
            result = DateUtils.formatDate(project.getBeginDate(), "yyyy-MM-dd 00:00:00");
        }

        return result;
    }

}
