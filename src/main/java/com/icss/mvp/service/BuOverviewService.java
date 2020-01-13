package com.icss.mvp.service;

import com.icss.mvp.dao.GeneralSituationDao;
import com.icss.mvp.dao.IBuOverviewDao;
import com.icss.mvp.dao.IParameterInfo;
import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IProjectKeyrolesDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectParameterValueDao;
import com.icss.mvp.dao.IProjectScheduleDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.entity.MeasureInfo;
import com.icss.mvp.entity.MonthlyManpower;
import com.icss.mvp.entity.MonthlyManpowerList;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectSchedule;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.util.AuthUtil;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author zw
 *
 */
@Service("buOverviewService")
@Transactional
public class BuOverviewService {
	@Resource
	private IBuOverviewDao buOverviewDao;
	@Resource
	private IProjectKeyrolesDao projectKeyrolesDao;
	@Resource
	private IProjectScheduleDao projectScheduleDao;
	@Resource
	private IParameterInfo parameterInfoDao;
	@Resource
	private IProjectParameterValueDao projectParameterValueDao;
	@Resource
	IUserManagerDao userManagerDao;
	@Resource
	private GeneralSituationDao generalSituationDao;
	@Resource
	private IProjectListDao projectListDao;
	@Resource
	IProjectInfoDao projectInfoDao;
	private final static Logger LOG = Logger.getLogger(BuOverviewService.class);
	// private final static String[] HEADERS = { "项目名称", "项目经理", "地域", "华为产品线",
	// "子产品线", "PDU/SPDT", "业务线", "事业部", "交付部", "计费类型", "项目状态" };
//	private final static String[] HEADERS = { "项目名称", "项目经理", "地域", "华为产品线", "子产品线", "PDU/SPDT", "计费类型", "质量", "项目状态" };
	private final static String[] HEADERS = { "项目名称", "项目经理", "地域", "华为产品线", "子产品线", "PDU/SPDT", "计费类型", "项目状态" };


//	public List<OrganizeMgmer> getBUs() {
//		return buOverviewDao.getBuOpts();
//	}
//
//	public List<ProjectSummary> getProjSummaries(String buName) {
//		return buOverviewDao.getProjectSummaries(buName);
//	}
//
//	public List<MonthSummary> getMonthSummary(String buName) {
//		return buOverviewDao.getMonthSummary(buName);
//	}
//
//	public List<ProjProductivity> getProjProductity(String buName) {
//		return buOverviewDao.getProjProductity(buName);
//	}
//
//	/**
//	 * 获取当前条件下该用户可以看到的项目编号
//	 * @param proj
//	 * @param username
//	 * @return
//	 */
//	public Set<String> getProjNos(ProjectInfo proj, String username) throws Exception{
//		Set<String> projNos = new HashSet<>();
//		Map<String, Object> map = setMap(proj, username);
//		List<MeasureInfo> gridDatas = buOverviewDao.getProjDetail(map);
//		for (MeasureInfo gridData : gridDatas) {
//			projNos.add(gridData.getNo());
//		}
//		return projNos;
//	}

	public Map<String, Object> getProjDetail(ProjectInfo proj, String username, Map<String, Object> lightUpMap) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> titles = new LinkedList<Map<String, String>>();
		Map<String, String> title = null;
		MeasureInfo titleInfo = null;
		for (String header : HEADERS) {
			titleInfo = new MeasureInfo();
			titleInfo.setName(header);
			title = new HashMap<String, String>();
			title.put("field", header);
			title.put("title", header);
			titles.add(title);
		}
		result.put("gridTitles", titles);
		Map<String, Object> map = setMap(proj, username);
		List<MeasureInfo> gridDatas = buOverviewDao.getProjDetail(map);
		List<Map<String, String>> gridDataRecords = new LinkedList<Map<String, String>>();
//		Map<String, LightUp> proLightUpMap = this.projectLightUpColor(lightUpMap);
		for (MeasureInfo gridData : gridDatas) {
			Map<String, String> record = new HashMap<String, String>();
			record.put("项目名称", gridData.getProjectName());
			record.put("项目经理", gridData.getPm());
			record.put("地域", gridData.getArea());
			record.put("华为产品线", gridData.getHwpdu());
			record.put("子产品线", gridData.getHwzpdu());
			record.put("PDU/SPDT", gridData.getPduSpdt());
			record.put("计费类型", gridData.getType());
//			LightUp lightUp = proLightUpMap.get(gridData.getProjectName());
//			if (null == lightUp){
//				lightUp = LightUp.NO;
//			}
//			record.put("质量", lightUp.getColor());
			record.put("项目状态", gridData.getProjectState());
			record.put("项目编码", gridData.getNo());
			gridDataRecords.add(record);
		}
		Map<String, Object> tableRslts = new HashMap<String, Object>();
		tableRslts.put("rows", gridDataRecords);
		tableRslts.put("total", gridDataRecords.size());
		result.put("gridDatas", tableRslts);
		return result;
	}

	private Map<String, Object> setMap(ProjectInfo proj, String username) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String roleAuth = buOverviewDao.getAuthOpDepartment(username);
		Map<String, Object> authMap = AuthUtil.authMap(roleAuth);
		if (proj.getName() != null && !"".equals(proj.getName())) {
			map.put("name", proj.getName().trim());
		}
		if (proj.getPm() != null && !"".equals(proj.getPm())) {
			map.put("pm", proj.getPm().trim());
		}

		if (StringUtils.isNotBlank(proj.getArea())) {
			String[] split = proj.getArea().split(",");
			map.put("area", Arrays.asList(split));
		} else {
			map.put("area", authMap.get(AuthUtil.AREA));
		}
		Map<String, Object> hwPduMap = (Map<String, Object>) authMap.get(AuthUtil.HW_DEPT);
		String[] hwTitle = {"hwpdu", "hwzpdu", "pduSpdt"};
		String[] csTitle = {"bu", "pdu", "du"};
		if (StringUtils.isNotBlank(proj.getHwpdu()) || StringUtils.isNotBlank(proj.getHwzpdu())
				|| StringUtils.isNotBlank(proj.getPduSpdt())) {
			this.setProjectInfo(map, hwPduMap, proj.getHwpdu(), proj.getHwzpdu(), proj.getPduSpdt(), hwTitle);
		}
		if (StringUtils.isBlank(proj.getHwpdu()) && StringUtils.isBlank(proj.getHwzpdu())
				&& StringUtils.isBlank(proj.getPduSpdt())) {
			setParam(hwPduMap, map, hwTitle);
		}
		Map<String, Object> buMap = (Map<String, Object>) authMap.get(AuthUtil.OPT_DEPT);
		if (StringUtils.isNotBlank(proj.getBu()) || StringUtils.isNotBlank(proj.getPdu())
				|| StringUtils.isNotBlank(proj.getDu())) {
			this.setProjectInfo(map, buMap, proj.getBu(), proj.getPdu(), proj.getDu(), csTitle);
		}
		if (StringUtils.isBlank(proj.getBu()) && StringUtils.isBlank(proj.getPdu())
				&& StringUtils.isBlank(proj.getDu())) {
			setParam(buMap, map, csTitle);
		}

		if (proj.getProjectState() != null && !"".equals(proj.getProjectState())) {
			List<String> list = new ArrayList<>();
			String[] projectStates = proj.getProjectState().split(",");
			for (String projectState : projectStates) {
				list.add(projectState);
			}
			map.put("projectState", list);
		} else {
			map.put("projectState", new HashMap<>());
		}
		return map;
	}

	private void setParam(Map<String, Object> pduMap, Map<String, Object> paraMap, String[] title){
		Set<String> set0 = new HashSet<>();
		Set<String> set1 = new HashSet<>();
		Set<String> set2 = new HashSet<>();
		set0.addAll(pduMap.keySet());
		for (Map.Entry<String, Object> hwPduEntity : pduMap.entrySet()) {
			Map<String, Object> hwZpduMap = (Map<String, Object>) hwPduEntity.getValue();
			set1.addAll(hwZpduMap.keySet());
			for (Map.Entry<String, Object> pduSpdtMap : hwZpduMap.entrySet()) {
				List<String> pduSpdts = (List<String>) pduSpdtMap.getValue();
				set2.addAll(pduSpdts);
			}
		}
		paraMap.put(title[0], set0);
		paraMap.put(title[1], set1);
		paraMap.put(title[2], set2);
	}
	private void setProjectInfo(Map<String, Object> map,
								  Map<String, Object> hwPduMap,
								  String hwpdu,
								  String hwzpdu,
								  String pduSpdt,
								  String[] title) {
		if (StringUtils.isNotBlank(pduSpdt)) {
			map.put(title[0], Arrays.asList(hwpdu.split(",")));
			map.put(title[1], Arrays.asList(hwzpdu.split(",")));
			map.put(title[2], Arrays.asList(pduSpdt.split(",")));
			return;
		}

		if (StringUtils.isNotBlank(hwzpdu)) {
			String[] hwpdus = hwpdu.split(",");
			String[] hwzpdus = hwzpdu.split(",");
			map.put(title[0], Arrays.asList(hwpdus));
			map.put(title[1], Arrays.asList(hwzpdus));
			Set<String> pduSpdtSet = new HashSet<>();
			for (String hwpduNo : hwpdus) {
				Map<String, Object> hwzpduMap = (Map<String, Object>) hwPduMap.get(hwpduNo);
				for (String hwzpduNo : hwzpdus) {
					List<String> list = (List<String>) hwzpduMap.get(hwzpduNo);
					pduSpdtSet.addAll(list);
				}
			}
			map.put(title[2], new ArrayList<>(pduSpdtSet));
			return;
		}

		if (StringUtils.isNotBlank(hwpdu)) {
			String[] hwpdus = hwpdu.split(",");
			map.put(title[0], Arrays.asList(hwpdus));
			Set<String> hwzpduSet = new HashSet<>();
			Set<String> pduSpdtSet = new HashSet<>();
			for (String hwpduNo : hwpdus) {
				Map<String, Object> hwzpduMap = (Map<String, Object>) hwPduMap.get(hwpduNo);
				if (null == hwzpduMap) {
                    map.put(title[1], new ArrayList<>(hwzpduSet));
                    map.put(title[2], new ArrayList<>(pduSpdtSet));
                    return;
                }
				hwzpduSet.addAll(hwzpduMap.keySet());
				for (String hwzpduNo : hwzpduMap.keySet()) {
					pduSpdtSet.addAll((List<String>) hwzpduMap.get(hwzpduNo));
				}
			}
			map.put(title[1], new ArrayList<>(hwzpduSet));
			map.put(title[2], new ArrayList<>(pduSpdtSet));
			return;
		}

	}


	public byte[] exportExcel(ProjectInfo proj, String username, Map<String, Object> lightUpMap) throws Exception{
		Map<String, Object> gridDatas = getProjDetail(proj, username, lightUpMap);
		// Map<String, Object> gridDatas = getProjDetail(buName, new Page(), proj);
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(0);
		Cell cell;
		List<Map<String, String>> titles = (List<Map<String, String>>) gridDatas.get("gridTitles");
		for (int i = 0; i < titles.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(titles.get(i).get("title"));
		}
		Map<String, Object> tableRslts = (Map<String, Object>) gridDatas.get("gridDatas");
		List<Map<String, String>> gridDataRecords = (List<Map<String, String>>) (tableRslts.get("rows"));
		for (int i = 0; i < gridDataRecords.size(); i++) {
			row = sheet.createRow(i + 1);
			for (int j = 0; j < titles.size(); j++) {
				cell = row.createCell(j);
				cell.setCellValue(gridDataRecords.get(i).get(titles.get(j).get("field")));
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			wb.write(os);
		} catch (IOException e) {
			LOG.error("export excel failed!", e);
		}
		return os.toByteArray();
	}

//	public Map<String, Object> getProjIndicators(ProjProductivity proj) {
//		String buName = proj.getBu();
//		List<ProjProductivity> wtdsList = buOverviewDao.getProjIndicator(buName, proj.getWtdhgbtggs());
//		List<ProjProductivity> lltList = buOverviewDao.getProjIndicator(buName, proj.getBbzcssbcs());
//		List<ProjProductivity> ddqxsList = buOverviewDao.getProjIndicator(buName, proj.getWswts());
//		List<ProjProductivity> xmdmlList = buOverviewDao.getProjIndicator(buName, proj.getDml());
//		List<ProjProductivity> zxylsList = buOverviewDao.getProjIndicator(buName, proj.getSdylzxxl());
//		List<ProjProductivity> ddqxxgxlList = buOverviewDao.getProjIndicator(buName, proj.getDdckwtjjl());
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("wtds", wtdsList);
//		result.put("llt", lltList);
//		result.put("ddqxs", ddqxsList);
//		result.put("xmdml", xmdmlList);
//		result.put("zxyls", zxylsList);
//		result.put("ddqxxgxl", ddqxxgxlList);
//		return result;
//	}
//
//	public List<ProjProductivity> getSpecProjChartData(String no) {
//		return buOverviewDao.getSpecProjChartData(no);
//	}

//	public Map<String, Map<String, Map<String, List<ProjectInfo>>>> getProjCategory() {
//		List<OrganizeMgmer> bus = buOverviewDao.getBuOpts();
//		Map<String, Map<String, Map<String, List<ProjectInfo>>>> result = new HashMap<String, Map<String, Map<String, List<ProjectInfo>>>>();
//		for (OrganizeMgmer bu : bus) {
//			List<String> pus = buOverviewDao.getPus(bu.getName());
//			Map<String, Map<String, List<ProjectInfo>>> map = new HashMap<String, Map<String, List<ProjectInfo>>>();
//			for (String pu : pus) {
//				List<ProjectInfo> puList = buOverviewDao.getProjCategory(bu.getName(), pu);
//				Map<String, List<ProjectInfo>> monMap = new HashMap<String, List<ProjectInfo>>();
//				for (ProjectInfo info : puList) {
//					List<ProjectInfo> monList = new LinkedList<ProjectInfo>();
//					if (null == info.getMonth() || StringUtils.isEmpty(info.getMonth())) {
//						info.setMonth(DateUtils.getMonth());
//					}
//					if (monMap.containsKey(info.getMonth())) {
//						monList = monMap.get(info.getMonth());
//					}
//					monList.add(info);
//					monMap.put(info.getMonth(), monList);
//				}
//				map.put(pu, monMap);
//
//			}
//			result.put(bu.getName(), map);
//		}
//
//		return result;
//	}

	/**
	 * 获取展示页面各表格中的表头及数据
	 * 
	 * @param no
	 * @param bigType
	 * @param smallType
	 * @return
	 */
	public Map<String, Object> getMeasureResult(String no, String bigType, String smallType) {
		List<MeasureInfo> measureRslts = buOverviewDao.getMeasureResult(no, bigType, smallType);
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> paramNames = new TreeSet<String>();
		Set<String> monthSet = new TreeSet<String>();
		Map<String, String> units = new TreeMap<String, String>();
		List<Map<String, String>> titles = new LinkedList<Map<String, String>>();
		Map<String, String> title = new HashMap<String, String>();
		title.put("field", "month");
		title.put("title", "统计月份");
		titles.add(title);
		for (MeasureInfo measureInfo : measureRslts) {
			if (StringUtils.isNotEmpty(measureInfo.getName())) {
				paramNames.add(measureInfo.getName());
				if (!units.containsKey(measureInfo.getName())) {
					title = new HashMap<String, String>();
					title.put("field", measureInfo.getName());
					title.put("title", measureInfo.getName());
					titles.add(title);
					units.put(measureInfo.getName(), measureInfo.getUnit());
				}
			}
			if (StringUtils.isNotEmpty(measureInfo.getMonth())) {
				monthSet.add(measureInfo.getMonth());
			}
		}
		result.put("title", titles);
		result.put("units", units);
		List<Map<String, String>> monthRecords = generateDatasForGrid(measureRslts, paramNames, monthSet);
		Map<String, Object> tableRslts = new HashMap<String, Object>();
		tableRslts.put("rows", monthRecords);
		tableRslts.put("total", monthRecords.size());
		result.put("gridDatas", tableRslts);
		return result;
	}

	/**
	 * 根据数据库中查出的列表组装datagrid需要的数据格式
	 * 
	 * @param measureRslts
	 * @param paramNames
	 * @param monthSet
	 * @return
	 */
	private List<Map<String, String>> generateDatasForGrid(List<MeasureInfo> measureRslts, Set<String> paramNames,
			Set<String> monthSet) {
		List<Map<String, String>> monthRecords = new LinkedList<Map<String, String>>();
		for (String mon : monthSet) {
			Map<String, String> record = new HashMap<String, String>();
			for (String name : paramNames) {
				for (MeasureInfo measureRslt : measureRslts) {
					if (mon.equals(measureRslt.getMonth()) && name.equals(measureRslt.getName())) {
						record.put(name, measureRslt.getValue());
					}
				}
				if (null == record.get(name)) {
					record.put(name, "");
				}
			}
			record.put("month", mon);
			monthRecords.add(record);
		}
		return monthRecords;
	}

	public Map<String, Object> getProjOverveiwData(String no) {
		Map<String, Object> result = new HashMap<String, Object>();
		ProjectInfo projectInfo = projectListDao.getProjInfoByNo(no);
		result.put("no", projectInfo.getNo());
		result.put("name", projectInfo.getName());
		result.put("bu", projectInfo.getBu());
		result.put("pdu", projectInfo.getPdu());
		result.put("du", projectInfo.getDu());
		result.put("area", projectInfo.getArea());
		result.put("type", projectInfo.getType());
		result.put("startDate", projectInfo.getStartDate());
		result.put("endDate", projectInfo.getEndDate());
		result.put("projectType", projectInfo.getProjectType());
		result.put("import_date", projectInfo.getImportDate());
		result.put("pm", projectInfo.getPm());
		result.put("qa", projectInfo.getQa());
		result.put("hwpdu", projectInfo.getHwpdu());
		result.put("hwzpdu", projectInfo.getHwzpdu());
		result.put("pduSpdt", projectInfo.getPduSpdt());
		result.put("projectSynopsis", projectInfo.getProjectSynopsis());
		result.put("projectState", projectInfo.getProjectState());
		result.put("coopType", projectInfo.getCoopType());
		result.put("teamName", projectInfo.getTeamName());
		result.put("isOffshore", projectInfo.getIsOffshore());
		result.put("projectBudget", String.format("%.2f", projectInfo.getProjectBudget()));

		return result;
	}

	public Map<String, Object> getProjOverveiwDatas(String reprname) {
		Map<String, Object> result = new HashMap<String, Object>();
		String no = projectInfoDao.getNoByName(reprname);
		ProjectInfo projectInfo = projectListDao.getProjInfoByNo(no);
		result.put("no", projectInfo.getNo());
		result.put("name", projectInfo.getName());
		result.put("bu", projectInfo.getBu());
		result.put("pdu", projectInfo.getPdu());
		result.put("du", projectInfo.getDu());
		result.put("area", projectInfo.getArea());
		result.put("type", projectInfo.getType());
		result.put("startDate", projectInfo.getStartDate());
		result.put("endDate", projectInfo.getEndDate());
		result.put("projectType", projectInfo.getProjectType());
		result.put("import_date", projectInfo.getImportDate());
		result.put("pm", projectInfo.getPm());
		result.put("qa", projectInfo.getQa());
		result.put("hwpdu", projectInfo.getHwpdu());
		result.put("hwzpdu", projectInfo.getHwzpdu());
		result.put("pduSpdt", projectInfo.getPduSpdt());
		result.put("projectSynopsis", projectInfo.getProjectSynopsis());
		result.put("projectState", projectInfo.getProjectState());
		result.put("coopType", projectInfo.getCoopType());
		result.put("teamName", projectInfo.getTeamName());
		result.put("isOffshore", projectInfo.getIsOffshore());
		result.put("projectBudget", String.format("%.2f", projectInfo.getProjectBudget()));

		return result;
	}

	public Map<String, Object> getProjOverveiw(String no) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<MeasureInfo> gridTitleUnits = buOverviewDao.getGridTitles();
		List<MeasureInfo> gridDatas = buOverviewDao.getProjOverview(no);
		Map<String, String> units = new TreeMap<String, String>();
		List<Map<String, String>> titles = new LinkedList<Map<String, String>>();
		Map<String, String> title = new HashMap<String, String>();
		Set<String> paramNames = new TreeSet<String>();
		Set<String> monthSet = new TreeSet<String>();

		title.put("field", "month");
		title.put("title", "统计月份");
		titles.add(title);
		for (MeasureInfo gridTitleUnit : gridTitleUnits) {
			title = new HashMap<String, String>();
			title.put("field", gridTitleUnit.getName());
			title.put("title", gridTitleUnit.getName());
			titles.add(title);
			units.put(gridTitleUnit.getName(), gridTitleUnit.getUnit());
			paramNames.add(gridTitleUnit.getName());
		}
		for (MeasureInfo gridData : gridDatas) {
			if (null != gridData && StringUtils.isNotEmpty(gridData.getMonth())) {
				monthSet.add(gridData.getMonth());
			}
		}
		result.put("title", titles);
		result.put("units", units);
		List<Map<String, String>> monthRecords = generateDatasForGrid(gridDatas, paramNames, monthSet);
		Map<String, Object> tableRslts = new HashMap<String, Object>();
		tableRslts.put("rows", monthRecords);
		tableRslts.put("total", monthRecords.size());
		result.put("gridDatas", tableRslts);
		return result;
	}

	public List<ProjectKeyroles> getProjectKeyrolesNo(String userid) {
		List<ProjectKeyroles>proKeyRoles = new ArrayList<>();
		UserInfo user = userManagerDao.getUserInfoByName(userid);
		String pmid ="";
		//	String proNos ="";
		if ("2".equals(user.getUsertype())){
			pmid = generalSituationDao.getPMZRAccountByHW(user.getUSERID());
			if (pmid==null || "".equals(pmid)){
				return proKeyRoles;
			}
		}else if ("1".equals(user.getUsertype())){
			pmid = StringUtilsLocal.formatMakeUp(StringUtilsLocal.clearSpaceAndLine(user.getUSERID()), "0", 10);
			if (pmid==null || "".equals(pmid)){
				return proKeyRoles;
			}
		}
		List<ProjectKeyroles> keyroles = projectKeyrolesDao.queryProjectKeyrolesNo(pmid);
		for (ProjectKeyroles projectKeyroles : keyroles) {
			if (StringUtilsLocal.isBlank(projectKeyroles.getHwAccount())
					&& !StringUtilsLocal.isBlank(projectKeyroles.getAuthor())) {
				projectKeyroles.setHwAccount(projectKeyroles.getAuthor());
			}
		}
		return keyroles;
	}

	public Map<String, Object> insertInfo(List<ProjectKeyroles> projectKeyroles) {
		Map<String, Object> res = new HashMap<>();
		if (projectKeyroles != null && projectKeyroles.get(0) != null) {
			projectKeyrolesDao.batchDeleteByNo(projectKeyroles.get(0).getNo());
		}
		int num = projectKeyrolesDao.insertInfos(projectKeyroles);
		if (num > 0) {
			res.put("res", "successful");
		} else {
			res.put("res", "fail");
		}
		return res;
	}

//	public List<ProjectSchedule> getProjectScheduleNo(String no) {
//		return projectScheduleDao.queryProjectScheduleNo(no);
//	}

	public Map<String, Object> insertInfoProjectSchedule(List<ProjectSchedule> projectSchedules) {
		Map<String, Object> res = new HashMap<>();
		if (projectSchedules == null) {
			res.put("res", "fail");
			return res;
		}
		if (projectSchedules.size() > 0) {
			projectScheduleDao.batchDeleteByNo(projectSchedules.get(0).getNo());
		}
		int num = projectScheduleDao.insertInfos(projectSchedules);
		if (num > 0) {
			res.put("res", "successful");
		} else {
			res.put("res", "fail");
		}
		return res;
	}

	public List<String> getAreas() {
		return buOverviewDao.getAreas();
	}

//	public List<Map<String, Object>> getZhongruanYWX() {
//		return buOverviewDao.getZhongruanYWX();
//	}
//
//	public List<Map<String, Object>> getZhongruanSYB(String ywxval) {
//		Map<String, Object> map = new HashMap<>();
//		if (ywxval == null || "".equals(ywxval)) {
//			return new ArrayList<>();
//		} else {
//			List<String> valueList = new ArrayList<String>();
//			String[] ywxvals = ywxval.split(",");
//			for (String ywx : ywxvals) {
//				valueList.add(ywx);
//			}
//			map.put("ywxval", valueList);
//		}
//		return buOverviewDao.getZhongruanSYB(map);
//	};
//
//	public List<Map<String, Object>> getZhongruanJFB(String sybval) {
//		Map<String, Object> map = new HashMap<>();
//		if (sybval == null || "".equals(sybval)) {
//			return new ArrayList<>();
//		} else {
//			List<String> valueList = new ArrayList<String>();
//			String[] sybvals = sybval.split(",");
//			for (String syb : sybvals) {
//				valueList.add(syb);
//			}
//			map.put("sybval", valueList);
//		}
//		return buOverviewDao.getZhongruanJFB(map);
//	};

	public List<Map<String, Object>> getHWCPX() {
		return buOverviewDao.getHWCPX();
	}

	public List<Map<String, Object>> getZCPX(String hwcpxval) {
		Map<String, Object> map = new HashMap<>();
		if (hwcpxval == null || "".equals(hwcpxval)) {
			return new ArrayList<>();
		} else {
			List<String> valueList = new ArrayList<String>();
			String[] hwcpxvals = hwcpxval.split(",");
			for (String hwcpx : hwcpxvals) {
				valueList.add(hwcpx);
			}
			map.put("hwcpxval", valueList);
		}
		return buOverviewDao.getZCPX(map);
	};

	public List<Map<String, Object>> getPDUorSPDT(String zcpxval) {
		Map<String, Object> map = new HashMap<>();
		if (zcpxval == null || "".equals(zcpxval)) {
			return new ArrayList<>();
		} else {
			List<String> valueList = new ArrayList<String>();
			String[] zcpxvals = zcpxval.split(",");
			for (String zcpx : zcpxvals) {
				valueList.add(zcpx);
			}
			map.put("zcpxval", valueList);
		}
		return buOverviewDao.getPDUorSPDT(map);
	}

	public List<Map<String, Object>> getZRaccountByName(String name) {
		List<Map<String, Object>> ret = projectListDao.queryMembersByName(name);
		return ret;
	}

	public Map<String, Object> insertInvestmentProportion(MonthlyManpowerList manpowerList) {
		List<ParameterValueNew> paramList = new ArrayList<>();
		Map<String, Object> ret = new HashMap<>();
		if (manpowerList.getManpowers() == null) {
			ret.put("msg", "传入表为空");
			return ret;
		}
		String proNo = manpowerList.getProNo();
		String[] months = DateUtils.getLatestMonths(12);
		for (MonthlyManpower manpower : manpowerList.getManpowers()) {
			Integer ParameterId = parameterInfoDao.queryid(manpower.getName());
			if (ParameterId == null) {
				LOG.error("参数名称不存在：" + manpower.getName());
				continue;
			}
			for (int i = 0; i < months.length; i++) {
				String month = months[i];
				ParameterValueNew parameterValueNew = new ParameterValueNew();
				parameterValueNew.setNo(proNo);
				parameterValueNew.setParameterId(ParameterId);
				double value = 0.00;
				value = manpower.getJanuary();
				switch (i+1) {
				case 1:
					value = manpower.getFirst();
					break;
				case 2:
					value = manpower.getSecond();
					break;
				case 3:
					value = manpower.getThird();
					break;
				case 4:
					value = manpower.getFourth();
					break;
				case 5:
					value = manpower.getFifth();
					break;
				case 6:
					value = manpower.getSixth();
					break;
				case 7:
					value = manpower.getSeventh();
					break;
				case 8:
					value = manpower.getEighth();
					break;					
				case 9:
					value = manpower.getNinth();
					break;
				case 10:
					value = manpower.getTenth();
					break;
				case 11:
					value = manpower.getEleventh();
					break;
				case 12:
					value = manpower.getTwelfth();
					break;
				default:
					break;
				}
				Date date = null;
				try {
					date = DateUtils.SHORT_FORMAT_GENERAL.parse(month + "-01");
				} catch (ParseException e) {
					LOG.error("时间序列号异常：" + e.getMessage());
				}
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(value);
				paramList.add(parameterValueNew);
			}

		}
		if (paramList.size() > 0) {
			//projectParameterValueDao.deleteParameterValueById(proNo, "138,139,140,141,142,143");
			projectParameterValueDao.insertParams(paramList);
		}
		ret.put("msg", "success");
		return ret;
	}

	public void deleteInvestmentProportion(String projNo, String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		projectParameterValueDao.deleteParameterValueByName(projNo, name);
	}
	
	public Map<String, Object> getMonthlyManpowerProportion(String projNo) {
		Map<String, Object> result = new HashMap<String, Object>();
		String[] months = DateUtils.getLatestMonths(12);
		List<MonthlyManpower> body = new ArrayList<>();
		result.put("head", months);
		result.put("body", body);
		List<Map<String, Object>> list = projectParameterValueDao.queryProportionList(projNo,
				"138,139,140,141,142,143",months);
		Map<String, MonthlyManpower> monthlyManpowerMap = new HashMap<>();
		
		for (Map<String, Object> map : list) {
			String name = StringUtilsLocal.valueOf(map.get("name"));
			if (!monthlyManpowerMap.containsKey(name)) {
				MonthlyManpower manpower = new MonthlyManpower();
				manpower.setName(name);
				monthlyManpowerMap.put(name, manpower);
			}
			double timeDou = Double.parseDouble(StringUtilsLocal.valueOf(map.get("value")));
			String month = StringUtilsLocal.valueOf(map.get("months"));
			for (int i = 0; i < months.length; i++) {
				if(month.equals(months[i])) {
					switch (i+1) {
					case 1:
						monthlyManpowerMap.get(name).setFirst(timeDou);
						break;
					case 2:
						monthlyManpowerMap.get(name).setSecond(timeDou);
						break;
					case 3:
						monthlyManpowerMap.get(name).setThird(timeDou);
						break;
					case 4:
						monthlyManpowerMap.get(name).setFourth(timeDou);
						break;
					case 5:
						monthlyManpowerMap.get(name).setFifth(timeDou);
						break;
					case 6:
						monthlyManpowerMap.get(name).setSixth(timeDou);
						break;
					case 7:
						monthlyManpowerMap.get(name).setSeventh(timeDou);
						break;
					case 8:
						monthlyManpowerMap.get(name).setEighth(timeDou);
						break;					
					case 9:
						monthlyManpowerMap.get(name).setNinth(timeDou);
						break;
					case 10:
						monthlyManpowerMap.get(name).setTenth(timeDou);
						break;
					case 11:
						monthlyManpowerMap.get(name).setEleventh(timeDou);
						break;
					case 12:
						monthlyManpowerMap.get(name).setTwelfth(timeDou);
						break;
					default:
						break;
					}
				}
			}
		}
		for (MonthlyManpower value : monthlyManpowerMap.values()) {
			body.add(value);
		}
		return result;
	}
	
//	public Map<String, LightUp> projectLightUpColor(Map<String, Object> lightUpMap){
//		Map<String, LightUp> map = new HashMap<>();
//		List<String> redList = (List<String>)lightUpMap.get("redList");
//		for (String s : redList) {
//			map.put(s, LightUp.RED);
//		}
//		List<String> greenList = (List<String>)lightUpMap.get("greenList");
//		for (String s : greenList) {
//			map.put(s, LightUp.GREEN);
//		}
//		List<String> yellowList = (List<String>)lightUpMap.get("yellowList");
//		for (String s : yellowList) {
//			map.put(s, LightUp.YELLOW);
//		}
//		List<String> noList = (List<String>)lightUpMap.get("noList");
//		for (String s : noList) {
//			map.put(s, LightUp.NO);
//		}
//		return map;
//	}

	public List<Map<String, Object>> getHwdept(String level, String ids, String username) {
//		String sysIds = "";
//		UserInfo userInfo = userManagerDao.getUserInfoByName(username);
//		if ("0".equals(userInfo.getUsertype()) && "1".equals(level)){
//			if("1".equals(level)){
//				sysIds = userInfo.getHwpdu()==null? "": userInfo.getHwpdu();
//			}else if ("2".equals(level)){
//				sysIds = userInfo.getHwzpdu()==null?"": userInfo.getHwzpdu();
//			}else if ("3".equals(level)){
//				sysIds = userInfo.getPduspdt()==null? "" :userInfo.getPduspdt();
//			}
//			List<Map<String,Object>> sysopDeparts = buOverviewDao.getHwdeptBylevel(level,null);
//			if(StringUtilsLocal.isBlank(sysIds)){
//				return sysopDeparts;
//			}
//			sysIds = ","+sysIds+",";
//			List<Map<String,Object>> rets = new ArrayList<>();
//			for (Map<String,Object> sysopDepart : sysopDeparts) {
//				if(sysIds.contains(","+StringUtilsLocal.valueOf(sysopDepart.get("dept_id"))+",")){
//					rets.add(sysopDepart);
//				}
//			}
//			return rets;
//		}
		if(!"1".equals(level) && StringUtilsLocal.isBlank(ids)){
			return new ArrayList<>();
		}
		UserInfo userInfo = userManagerDao.getUserInfoByName(username);
		if(StringUtilsLocal.isBlank(ids) && StringUtilsLocal.isBlank(userInfo.getHwpdu())){
			return new ArrayList<>();
		}
		String userIds = "";
		if("1".equals(level)){
			userIds = userInfo.getHwpdu();
		}else if ("2".equals(level)){
			userIds = userInfo.getHwzpdu();
		}else if ("3".equals(level)){
			userIds = userInfo.getPduspdt();
		}

		Set<String> deptIds = new HashSet<>();
		if(!StringUtilsLocal.isBlank(ids)){
			String[] deIds = ids.split(",");
			for (String id : deIds) {
				deptIds.add(id);
			}
		}
		List<Map<String,Object>> opDepartments = buOverviewDao.getHwdeptBylevel(level,deptIds);
		if(StringUtilsLocal.isBlank(userIds)){
			return opDepartments;
		}
		userIds = ","+userIds+",";
		List<Map<String,Object>> ret = new ArrayList<>();
		for (Map<String,Object> opDepartment : opDepartments) {
			if(userIds.contains(","+StringUtilsLocal.valueOf(opDepartment.get("dept_id"))+",")){
				ret.add(opDepartment);
			}
		}

		return ret;
	}

	public List<Object> getBm(Map<String, Object> map) {
		return buOverviewDao.getBm(map);
	}
}
