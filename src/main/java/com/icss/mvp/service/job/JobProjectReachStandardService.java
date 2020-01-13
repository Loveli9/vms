package com.icss.mvp.service.job;

import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IprojectReachStandardDAO;
import com.icss.mvp.dao.ProjectInfoVoDao;
import com.icss.mvp.entity.Dept;
import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.service.ProjectReportService;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author up
 * 
 */
@Service("jobProjectReachStandardService")
@EnableScheduling
@PropertySource("classpath:task.properties")
@SuppressWarnings("all")
@Transactional
public class JobProjectReachStandardService {

	private static final String[] MONTHS = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
	private static final SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat YEARMOUTHDAY = new SimpleDateFormat("yyyy-MM-dd");

	private static Logger logger = Logger.getLogger(JobProjectReachStandardService.class);
	@Autowired
	private IprojectReachStandardDAO projectReachStandardDAO;
	@Autowired
	private ProjectInfoVoDao projectInfoVoDao;
	@Autowired
	private ProjectReportService projectReportService;
	@Autowired
	private IProjectInfoDao projectInfoDao;
	/**
	 * 定时计算入库 每天凌晨一点执行
	 */
//	@Scheduled(cron = "${reachStandard_Task_scheduled}")
	public void jobProjectReachStandard() {
		List<ProjectInfo> projectInfos = projectInfoDao.queryEffectiveProjects();
		
		projectReachStandard(projectInfos);// 计算合格率并存库
	}

	/**
	 * 定时计算质量月报点灯入库 每天凌晨一点执行
	 */
	//@Scheduled(cron = "${quality_monthly_report_lighting}")
	public void jobProjectReport() {		
		List<ProjectInfo> projectInfos = projectInfoDao.queryEffectiveProjects();
		for (ProjectInfo projectInfo : projectInfos) {
			try {
				String month = DateUtils.getSystemMonth();
				month = month + "01";
				projectReportService.exitProjectReport(projectInfo.getNo(),month);
			} catch (Exception e) {
				logger.error("计算质量月报异常，项目编号为："+projectInfo.getNo(),e);
			}
		}
	}

	private void projectReachStandard(List<ProjectInfo> projectInfos) {

		Date todat = new Date();
		String day = YEARMOUTHDAY.format(todat);
		for (ProjectInfo projectInfo : projectInfos) {
			String no = projectInfo.getNo();
			Integer measured = projectReachStandardDAO.measured(no);// 查出该项目配置过的指标数量
			List<Map<String, Object>> resultList = projectReachStandardDAO.measureValuePerIterator(no, day);// 查出每个项目每个指标当前迭代的值
			Integer reached = 0;// 达标数
			for (Map<String, Object> map : resultList) {
				if ("196".equals(map.get("measure_id")) || "197".equals(map.get("measure_id"))
						|| "198".equals(map.get("measure_id")) || "199".equals(map.get("measure_id"))) {
					reached++;
					continue;
				}
				Double value = 0.0;
				Double up = 0.0;
				Double low = 0.0;
				if ("%".equals(map.get("unit"))) {
					value = removePercent(map.get("value"));
					up = removePercent(map.get("up"));
					low = removePercent(map.get("low"));
				} else {
					if (map.get("value") != null && !"".equals(map.get("value"))) {
						value = Double.valueOf(String.valueOf(map.get("value")));
					}
					if (map.get("up") != null && !"".equals(map.get("up"))) {
						up = Double.valueOf(String.valueOf(map.get("up")));
					}
					if (map.get("low") != null && !"".equals(map.get("low"))) {
						low = Double.valueOf(String.valueOf(map.get("low")));
					}
				}
				if (value >= low && value <= up) {
					reached++;
				}
			}
			Double reachedrate = 0.0;
			if (measured != 0) {
				reachedrate = Double.valueOf(reached) / Double.valueOf(measured) * 100;
			}
			ParameterValueNew pvn = new ParameterValueNew();
			pvn.setNo(no);
			pvn.setMonth(new Date());
			pvn.setParameterId(0);
			pvn.setValue(reachedrate);
			projectReachStandardDAO.deleteValue(no);
			projectReachStandardDAO.insertReached(pvn);
		}
	}

	private Double removePercent(Object temp) {
		Double result = 0.0;
		if (temp != null) {
			String val = String.valueOf(temp);
			if ("".equals(val.trim())) {
				result = 0.0;
			} else if ("%".equals(val.substring(val.length() - 1))) {
				val = val.substring(0, val.lastIndexOf("%"));
				result = Double.valueOf(val);
			} else if (!"%".equals(val.substring(val.length() - 1))) {
				result = Double.valueOf(val);
			}
		}
		return result;
	}

	/**
	 * 查询合格率
	 */
	public List<List<Map<String, Object>>> queryReach(ProjectInfo proj) {
		List<List<Map<String, Object>>> resultList = new ArrayList<>();
		Map<String, Object> tempmap = setMap(proj);
		List<String> bu = (List<String>) tempmap.get("bu");
		List<String> pdu = (List<String>) tempmap.get("pdu");
		List<String> hwpdu = (List<String>) tempmap.get("hwpdu");
		List<String> hwzpdu = (List<String>) tempmap.get("hwzpdu");
		List<String> area = (List<String>) tempmap.get("area");
		String month = String.valueOf(tempmap.get("month"));
		// all reached warning unreached
		List<Map<String, Object>> all = new ArrayList<>();// 总数
		List<Map<String, Object>> reached = new ArrayList<>();// 合格>=80%
		List<Map<String, Object>> warning = new ArrayList<>();// 警告<80%&&>=50%
		List<Map<String, Object>> unreached = new ArrayList<>();// 不合格<50%
		if (bu.size() != 0 && bu != null) {
			if (pdu.size() == 0 || pdu == null) {
				List<Dept> pdus = projectReachStandardDAO.getAllPDU(tempmap);// id
				for (Dept p : pdus) {
					Map<String, Object> tempAll = new HashMap<>();
					Map<String, Object> tempReached = new HashMap<>();
					Map<String, Object> tempWarning = new HashMap<>();
					Map<String, Object> tempUnreached = new HashMap<>();
					Integer countAll = 0;
					Integer countReached = 0;
					Integer countWarning = 0;
					Integer countUnreached = 0;
					List<String> values = projectReachStandardDAO.reachedAllProjNoByPDU(p.getDeptId());
					countAll = values.size();
					for (String value : values) {
						Double rate = StringUtilsLocal.keepTwoDecimals(Double.valueOf(value));
						Integer judge = judge(rate);
						if (judge == 1) {
							countReached++;
						} else if (judge == 0) {
							countWarning++;
						} else if (judge == -1) {
							countUnreached++;
						}
					}
					tempAll.put("pdu", p.getDeptName() + "/" + p.getDeptId());
					tempAll.put("name", "项目总数");
					tempAll.put("num", countAll);
					all.add(tempAll);
					tempReached.put("pdu", p.getDeptName() + "/" + p.getDeptId());
					tempReached.put("name", "合格项目数");
					tempReached.put("num", countReached);
					reached.add(tempReached);
					tempWarning.put("pdu", p.getDeptName() + "/" + p.getDeptId());
					tempWarning.put("name", "警告项目数");
					tempWarning.put("num", countWarning);
					warning.add(tempWarning);
					tempUnreached.put("pdu", p.getDeptName() + "/" + p.getDeptId());
					tempUnreached.put("name", "不合格项目数");
					tempUnreached.put("num", countUnreached);
					unreached.add(tempUnreached);
				}
			} else if (pdu.size() != 0 && pdu != null) {
				List<Dept> dus = projectReachStandardDAO.getAllDU(tempmap);// id
				for (Dept d : dus) {
					Map<String, Object> tempAll = new HashMap<>();
					Map<String, Object> tempReached = new HashMap<>();
					Map<String, Object> tempWarning = new HashMap<>();
					Map<String, Object> tempUnreached = new HashMap<>();
					Integer countAll = 0;
					Integer countReached = 0;
					Integer countWarning = 0;
					Integer countUnreached = 0;
					List<String> values = projectReachStandardDAO.reachedAllProjNoByDU(d.getDeptId());
					countAll = values.size();
					for (String value : values) {
						Double rate = StringUtilsLocal.keepTwoDecimals(Double.valueOf(value));
						Integer judge = judge(rate);
						if (judge == 1) {
							countReached++;
						} else if (judge == 0) {
							countWarning++;
						} else if (judge == -1) {
							countUnreached++;
						}
					}
					tempAll.put("pdu", d.getDeptName() + "/" + d.getDeptId());
					tempAll.put("name", "项目总数");
					tempAll.put("num", countAll);
					all.add(tempAll);
					tempReached.put("pdu", d.getDeptName() + "/" + d.getDeptId());
					tempReached.put("name", "合格项目数");
					tempReached.put("num", countReached);
					reached.add(tempReached);
					tempWarning.put("pdu", d.getDeptName() + "/" + d.getDeptId());
					tempWarning.put("name", "警告项目数");
					tempWarning.put("num", countWarning);
					warning.add(tempWarning);
					tempUnreached.put("pdu", d.getDeptName() + "/" + d.getDeptId());
					tempUnreached.put("name", "不合格项目数");
					tempUnreached.put("num", countUnreached);
					unreached.add(tempUnreached);
				}
			}
		}
		if (hwpdu.size() != 0 && hwpdu != null) {
			if (hwzpdu.size() == 0 || hwpdu == null) {
				List<Dept> hwzs = projectReachStandardDAO.getAllHWZPDU(tempmap);// id
				for (Dept hwz : hwzs) {
					Map<String, Object> tempAll = new HashMap<>();
					Map<String, Object> tempReached = new HashMap<>();
					Map<String, Object> tempWarning = new HashMap<>();
					Map<String, Object> tempUnreached = new HashMap<>();
					Integer countAll = 0;
					Integer countReached = 0;
					Integer countWarning = 0;
					Integer countUnreached = 0;
					List<String> values = projectReachStandardDAO.reachedAllProjNoByHWZPDU(hwz.getDeptId());
					countAll = values.size();
					for (String value : values) {
						Double rate = StringUtilsLocal.keepTwoDecimals(Double.valueOf(value));
						Integer judge = judge(rate);
						if (judge == 1) {
							countReached++;
						} else if (judge == 0) {
							countWarning++;
						} else if (judge == -1) {
							countUnreached++;
						}
					}
					tempAll.put("pdu", hwz.getDeptName() + "/" + hwz.getDeptId());
					tempAll.put("name", "项目总数");
					tempAll.put("num", countAll);
					all.add(tempAll);
					tempReached.put("pdu", hwz.getDeptName() + "/" + hwz.getDeptId());
					tempReached.put("name", "合格项目数");
					tempReached.put("num", countReached);
					reached.add(tempReached);
					tempWarning.put("pdu", hwz.getDeptName() + "/" + hwz.getDeptId());
					tempWarning.put("name", "警告项目数");
					tempWarning.put("num", countWarning);
					warning.add(tempWarning);
					tempUnreached.put("pdu", hwz.getDeptName() + "/" + hwz.getDeptId());
					tempUnreached.put("name", "不合格项目数");
					tempUnreached.put("num", countUnreached);
					unreached.add(tempUnreached);
				}
			} else if (hwzpdu.size() != 0 && hwpdu != null) {
				List<Dept> pduspsts = projectReachStandardDAO.getAllPDUSPDT(tempmap);// id
				for (Dept pduspst : pduspsts) {
					Map<String, Object> tempAll = new HashMap<>();
					Map<String, Object> tempReached = new HashMap<>();
					Map<String, Object> tempWarning = new HashMap<>();
					Map<String, Object> tempUnreached = new HashMap<>();
					Integer countAll = 0;
					Integer countReached = 0;
					Integer countWarning = 0;
					Integer countUnreached = 0;
					List<String> values = projectReachStandardDAO.reachedAllProjNoByPDUSPDT(pduspst.getDeptId());
					countAll = values.size();
					for (String value : values) {
						Double rate = StringUtilsLocal.keepTwoDecimals(Double.valueOf(value));
						Integer judge = judge(rate);
						if (judge == 1) {
							countReached++;
						} else if (judge == 0) {
							countWarning++;
						} else if (judge == -1) {
							countUnreached++;
						}
					}
					tempAll.put("pdu", pduspst.getDeptName() + "/" + pduspst.getDeptId());
					tempAll.put("name", "项目总数");
					tempAll.put("num", countAll);
					all.add(tempAll);
					tempReached.put("pdu", pduspst.getDeptName() + "/" + pduspst.getDeptId());
					tempReached.put("name", "合格项目数");
					tempReached.put("num", countReached);
					reached.add(tempReached);
					tempWarning.put("pdu", pduspst.getDeptName() + "/" + pduspst.getDeptId());
					tempWarning.put("name", "警告项目数");
					tempWarning.put("num", countWarning);
					warning.add(tempWarning);
					tempUnreached.put("pdu", pduspst.getDeptName() + "/" + pduspst.getDeptId());
					tempUnreached.put("name", "不合格项目数");
					tempUnreached.put("num", countUnreached);
					unreached.add(tempUnreached);
				}
			}
		}
		if (area.size() != 0 && area != null) {
			List<Dept> areas = projectReachStandardDAO.getAllAreas(tempmap);// id
			for (Dept ar : areas) {
				Map<String, Object> tempAll = new HashMap<>();
				Map<String, Object> tempReached = new HashMap<>();
				Map<String, Object> tempWarning = new HashMap<>();
				Map<String, Object> tempUnreached = new HashMap<>();
				Integer countAll = 0;
				Integer countReached = 0;
				Integer countWarning = 0;
				Integer countUnreached = 0;
				List<String> values = projectReachStandardDAO.reachedAllProjNoByArea(ar.getDeptId());
				countAll = values.size();
				for (String value : values) {
					Double rate = StringUtilsLocal.keepTwoDecimals(Double.valueOf(value));
					Integer judge = judge(rate);
					if (judge == 1) {
						countReached++;
					} else if (judge == 0) {
						countWarning++;
					} else if (judge == -1) {
						countUnreached++;
					}
				}
				tempAll.put("pdu", ar.getDeptName() + "/" + ar.getDeptId());
				tempAll.put("name", "项目总数");
				tempAll.put("num", countAll);
				all.add(tempAll);
				tempReached.put("pdu", ar.getDeptName() + "/" + ar.getDeptId());
				tempReached.put("name", "合格项目数");
				tempReached.put("num", countReached);
				reached.add(tempReached);
				tempWarning.put("pdu", ar.getDeptName() + "/" + ar.getDeptId());
				tempWarning.put("name", "警告项目数");
				tempWarning.put("num", countWarning);
				warning.add(tempWarning);
				tempUnreached.put("pdu", ar.getDeptName() + "/" + ar.getDeptId());
				tempUnreached.put("name", "不合格项目数");
				tempUnreached.put("num", countUnreached);
				unreached.add(tempUnreached);
			}
		}
		resultList.add(all);
		resultList.add(reached);
		resultList.add(warning);
		resultList.add(unreached);
		return resultList;
	}

	private Integer judge(Double rate) {
		if (rate >= 80.00) {
			return 1;
		} else if (rate >= 50.00 && rate < 80.00) {
			return 0;
		} else {
			return -1;
		}
	}

	private Map<String, Object> setMap(ProjectInfo proj) {
		Map<String, Object> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		if (proj.getArea() != null && !"".equals(proj.getArea())) {
			String[] arr = proj.getArea().split(",");
			map.put("area", Arrays.asList(arr));
		} else {
			map.put("area", list);
		}
		if (proj.getHwpdu() != null && !"".equals(proj.getHwpdu())) {
			String[] arr = proj.getHwpdu().split(",");
			map.put("hwpdu", Arrays.asList(arr));
		} else {
			map.put("hwpdu", list);
		}
		if (proj.getHwzpdu() != null && !"".equals(proj.getHwzpdu())) {
			String[] arr = proj.getHwzpdu().split(",");
			map.put("hwzpdu", Arrays.asList(arr));
		} else {
			map.put("hwzpdu", list);
		}
		if (proj.getPduSpdt() != null && !"".equals(proj.getPduSpdt())) {
			String[] arr = proj.getPduSpdt().split(",");
			map.put("pduSpdt", Arrays.asList(arr));
		} else {
			map.put("pduSpdt", list);
		}
		if (proj.getBu() != null && !"".equals(proj.getBu())) {
			String[] arr = proj.getBu().split(",");
			map.put("bu", Arrays.asList(arr));
		} else {
			map.put("bu", list);
		}
		if (proj.getPdu() != null && !"".equals(proj.getPdu())) {
			String[] arr = proj.getPdu().split(",");
			map.put("pdu", Arrays.asList(arr));
		} else {
			map.put("pdu", list);
		}
		if (proj.getDu() != null && !"".equals(proj.getDu())) {
			String[] arr = proj.getDu().split(",");
			map.put("du", Arrays.asList(arr));
		} else {
			map.put("du", list);
		}
		if (proj.getMonth() != null && !"".equals(proj.getMonth().trim())) {
			map.put("month", proj.getMonth());
		} else {
			map.put("month", null);
		}
		if (proj.getProjectType() != null && !"".equals(proj.getProjectType().trim())) {
			map.put("projectType", proj.getProjectType());
		} else {
			map.put("projectType", null);
		}
		return map;
	}

	/**
	 * 加载选中的不合格项目
	 */
	public List<Map<String, Object>> queryProjNoAndValue(ProjectInfo proj, PageInfo pageInfo) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> tempmap = setMap(proj);
		List<Map<String, Object>> list = null;
		List<String> bu = (List<String>) tempmap.get("bu");
		List<String> pdu = (List<String>) tempmap.get("pdu");
		List<String> hwpdu = (List<String>) tempmap.get("hwpdu");
		List<String> hwzpdu = (List<String>) tempmap.get("hwzpdu");
		List<String> area = (List<String>) tempmap.get("area");
		String projectType = String.valueOf(tempmap.get("projectType"));
		if (bu.size() != 0 && bu != null) {
			if (pdu.size() == 0 || pdu == null) {
				list = projectReachStandardDAO.queryProjNoAndValueByBU(projectType, pageInfo);
			} else if (pdu.size() != 0 && pdu != null) {
				list = projectReachStandardDAO.queryProjNoAndValueByPDU(projectType, pageInfo);
			}
		}
		if (hwpdu.size() != 0 && hwpdu != null) {
			if (hwzpdu.size() == 0 || hwpdu == null) {
				list = projectReachStandardDAO.queryProjNoAndValueByHWPDU(projectType, pageInfo);
			} else if (hwzpdu.size() != 0 && hwpdu != null) {
				list = projectReachStandardDAO.queryProjNoAndValueByHWZPDU(projectType, pageInfo);
			}
		}
		if (area.size() != 0 && area != null) {
			list = projectReachStandardDAO.queryProjNoAndValueByAREA(projectType, pageInfo);
		}
		Date todat = new Date();
		String day = YEARMOUTHDAY.format(todat);
		if (list != null && list.size() != 0) {
			for (Map<String, Object> map : list) {
				if (Double.valueOf(String.valueOf(map.get("VALUE"))) < 50.0) {
					Map<String, Object> resultMap = new HashMap<>();
					List<Map<String, Object>> temp = projectReachStandardDAO
							.measureValuePerIterator(String.valueOf(map.get("NO")), day);// 查出每个项目每个指标当前迭代的值
					StringBuffer bf = new StringBuffer();
					Integer reached = 0;// 达标数
					for (Map<String, Object> m : temp) {
						if ("196".equals(m.get("measure_id")) || "197".equals(m.get("measure_id"))
								|| "198".equals(m.get("measure_id")) || "199".equals(m.get("measure_id"))) {
							continue;
						}
						Double value = 0.0;
						Double up = 0.0;
						Double low = 0.0;
						if ("%".equals(m.get("unit"))) {
							value = removePercent(m.get("value"));
							up = removePercent(m.get("up"));
							low = removePercent(m.get("low"));
						} else {
							if (m.get("value") != null && !"".equals(m.get("value"))) {
								value = Double.valueOf(String.valueOf(m.get("value")));
							}
							if (m.get("up") != null && !"".equals(m.get("up"))) {
								up = Double.valueOf(String.valueOf(m.get("up")));
							}
							if (m.get("low") != null && !"".equals(m.get("low"))) {
								low = Double.valueOf(String.valueOf(m.get("low")));
							}
						}
						if (value < low || value > up) {
							bf.append(
									projectReachStandardDAO.getMeasureName(String.valueOf(m.get("measure_id"))) + ",");
							reached++;
						}
					}
					resultMap.put("no", projectReachStandardDAO.getProjName(String.valueOf(map.get("NO"))).getNo());
					resultMap.put("name", projectReachStandardDAO.getProjName(String.valueOf(map.get("NO"))).getName());
					resultMap.put("rate", String.valueOf(map.get("VALUE")));
					resultMap.put("num", reached);
					resultMap.put("some", bf.length() == 0 ? bf : bf.substring(0, bf.length() - 1));
					resultList.add(resultMap);
				}
			}
		}
		return resultList;
	}
}
