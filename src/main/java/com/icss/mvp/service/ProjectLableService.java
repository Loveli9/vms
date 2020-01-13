package com.icss.mvp.service;

import com.icss.mvp.dao.*;
import com.icss.mvp.dao.index.InTmplDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.constant.FileTypeEnum.FileType;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目与流程、流程与指标关系接口
 * 
 * @author chengchenhui
 *
 */
@Service
@SuppressWarnings("all")
public class ProjectLableService {
	@Autowired
	private ProjectLableDao dao;
	
	@Resource
	ProjectLableDao projectLableDao;
	
	@Autowired
	private MeasureRangeDao measurerangedao;

	@Autowired
	private LabelService labelService;

	@Autowired
	private ActualValCalculateService acService;

	@Autowired
	private LabelMeasureConfigDao labelMeasureConfigDao;

	@Autowired
	private TestMeasuresService testmeasureService;

	@Resource
	private LabelMeasureConfigDao configDao;
	
	@Autowired
	private BuOverviewService buOverviewService;

	@Autowired
	private MonthMeasureDao measureDao;
	
	@Resource
	private IProjectManagersDao managersDao;
	
	@Autowired
	private MonthMeasureService measureService;
	
	@Autowired
	private ProjectLableService service;
	
	@Autowired
	private CodeGainTypeService codeGainTypeService;
	
	@Autowired
	private CodeGainTypeDao codeGainTypeDao;
	
	@Resource
	private UserManagerService userManagerService;

	@Autowired
	private InTmplDao inTmplDao;

	@Autowired
	private MeasureCommentDao measureCommentDao;

	private static Logger logger = Logger.getLogger(ProjectLableService.class);

	/**
	 * @Description: 根据项目编号获取项目流程信息
	 * @author chengchenhui
	 * @date 2018年5月7日
	 **/
	public List<Map<String, Object>> getProjectAllabs(String proNo) {
		List<Map<String, Object>> list = dao.getProjectAllabs(proNo);
		Map<String, Object> listMap = new HashMap<String, Object>();
		//获取流程配置指标信息、类目作为流程子菜单
		List<String> categorys = new ArrayList<String>();
		for(Map<String, Object> map : list) {
            String url = map.get("LAB_PATH") == null ? "" : String.valueOf(map.get("LAB_PATH"));
            if (url.startsWith("/mvp/")) {
                map.put("LAB_PATH", url.substring(4));
            }
			categorys = new ArrayList<String>();
			List<String> li =  dao.queryCatgoryByPlIds(map.get("plId").toString());
			for(String category:li) {
				categorys.add(category);
			}
			listMap.put(map.get("plId").toString(),categorys);
		}
		if(listMap.size() != 0 && listMap != null) {
			list.add(listMap);
		}
		return list;
	}
	
	public List<Map<String, Object>> getTeamLabs(String teamId) {
		List<Map<String, Object>> list = dao.getTeamAllabs(teamId);
		Map<String, Object> listMap = new HashMap<String, Object>();
		//获取流程配置指标信息、类目作为流程子菜单
		List<String> categorys = new ArrayList<String>();
		for(Map<String, Object> map : list) {
            String url = map.get("LAB_PATH") == null ? "" : String.valueOf(map.get("LAB_PATH"));
            if (url.startsWith("/mvp/")) {
                map.put("LAB_PATH", url.substring(4));
            }
			categorys = new ArrayList<String>();
			List<String> li =  dao.queryCatgoryByTeamPlIds(map.get("plId").toString());
			for(String category:li) {
				categorys.add(category);
			}
			listMap.put(map.get("plId").toString(),categorys);
		}
		if(listMap.size() != 0 && listMap != null) {
			list.add(listMap);
		}
		return list;
	}

	/**
	 * @Description: 根据流程编号获取项目指标信息
	 * @author chengchenhui
	 * @date 2018年5月7日
	 **/
	public List<Map<String, Object>> getLabMeasureByProject(String labId, String version, String ite, String proNo,
			String flag) {
		List<Map<String, Object>> list = dao.getLabMeasureByProject(labId, version, ite, proNo);
		// 根据计算公式计算指标实际值
		//acService.haveConfigMeasure(list, proNo);
		list = dao.getLabMeasureByProject(labId, version, ite, proNo);
		Map<String, Double> mapAc = exploreMeasureCalculate(proNo, flag);
		for (Map<String, Object> map : list) {
			String a = String.valueOf(mapAc.get(String.valueOf(map.get("name")).trim()));
			if (StringUtils.isNotEmpty(a) && !"null".equals(a)) {
				double val = Double.valueOf(a);
				if (val > 0.0) {
					dao.saveActualValMeasureConfig(map.get("ID").toString(), a);
				} else {
					dao.saveActualValMeasureConfig(map.get("ID").toString(), "");
				}
			}
		}

		return dao.getLabMeasureByProject(labId, version, ite, proNo);
	}

	/**
	 * TMSS自动抓取时将手动输入的指标改为自动采集，手工输入时将自动采集的指标改为手动输入
	 */
	public void changemodel(String labId, String proNo, String flag) {
		if ("toAuto".equals(flag.trim())) {
			labelMeasureConfigDao.changeToAuto();
		} else if ("toManual".equals(flag.trim())) {
			labelMeasureConfigDao.changeToManual();
		}
	}

	/**
	 * @Description: 根据项目编号计算项目开发流程指标实际值
	 * @author
	 * @date
	 **/
	private Map<String, Double> exploreMeasureCalculate(String proNo, String flag) {
		
		// 总投入工作量
		List<Map<String, Object>> OMPUsers = managersDao.queryOMPUserSelected(proNo);
		List<String> listAuthor = new ArrayList<String>();
		for (Map<String, Object> map : OMPUsers) {
			String account = StringUtilsLocal.valueOf(map.get("ZR_ACCOUNT"));
			if (StringUtilsLocal.isBlank(account)) {
				account = StringUtilsLocal.valueOf(map.get("account"));
			}
			if (StringUtilsLocal.isBlank(account)) {
				continue;
			}
			listAuthor.add(account);
		}
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM");
		String yearMonth = sFormat.format(new Date());
		String hs = measureDao.getHoursByYearMonth(yearMonth,
				"(" + StringUtilsLocal.listToSqlIn(listAuthor) + ")");
		Double count = StringUtils.isEmpty(hs) ? 0 : Double.valueOf(hs);// 该月份总工时和
		if(count==0) {
			count = 21.5*8*listAuthor.size();
		}
		count = count / 8;
		
		// 测试用例执行数
		Integer zxyls = labelMeasureConfigDao.startTestCaseNum(proNo);
		if (zxyls == null) {
			zxyls = 0;
		}
		// 测试用例总数
		Integer zyls = labelMeasureConfigDao.testCaseCount(proNo);
		if (zyls == null) {
			zyls = 0;
		}
		// 自动化用例数
		Integer zdhyls = labelMeasureConfigDao.autoTestCaseNum(proNo);
		if (zdhyls == null) {
			zdhyls = 0;
		}
		// 测试发现问题单总数 关闭类型仅为（Closure After Correction）和状态不为Cancel
		Integer wtdzs = labelMeasureConfigDao.dtsCount(proNo);
		if (wtdzs == null) {
			wtdzs = 0;
		}
		// 已解决问题总数
		Integer yjjwtzs = labelMeasureConfigDao.solvedDtsCount(proNo);
		if (yjjwtzs == null) {
			yjjwtzs = 0;
		}
		// 手工用例执行次数
		Integer sgzxylzs = labelMeasureConfigDao.manualStartTestCaseCount(proNo);
		if (sgzxylzs == null) {
			sgzxylzs = 0;
		}
		// 自动化用例执行成功数
		Integer zdhylzxcgs = labelMeasureConfigDao.autoTestCaseStartPassedCount(proNo);
		if (zdhylzxcgs == null) {
			zdhylzxcgs = 0;
		}
		// 新增修改代码量
		Integer xzxgdml = labelMeasureConfigDao.newLoc(proNo);
		if (xzxgdml == null) {
			xzxgdml = 0;
		}
		Integer xzxgdmlWx = labelMeasureConfigDao.newLocWx(proNo);
		if (xzxgdmlWx != null) {
			xzxgdml += xzxgdmlWx;
		}
		
		// 上月新增修改代码量
		Integer dyxzxgdml = labelMeasureConfigDao.dynewLoc(proNo);
		if (dyxzxgdml == null) {
			dyxzxgdml = 0;
		}
		Integer dyxzxgdmlWx = labelMeasureConfigDao.dynewLocWx(proNo);
		if (dyxzxgdmlWx != null) {
			dyxzxgdml += dyxzxgdmlWx;
		}
		
		// 新增测试用例数
		Integer xzcsyls = labelMeasureConfigDao.newTestCaseNum(proNo);
		if (xzcsyls == null) {
			xzcsyls = 0;
		}
		// 测试自动化代码量
		Integer cszdhdml = labelMeasureConfigDao.testCaseAutoLoc(proNo);
		if (cszdhdml == null) {
			cszdhdml = 0;
		}
		Integer cszdhdmlWx = labelMeasureConfigDao.testCaseAutoLocWx(proNo);
		if (cszdhdmlWx != null) {
			cszdhdml += cszdhdmlWx;
		}
		
		// 问题解决投入工作量
		String wt = labelMeasureConfigDao.solveHours("问题单修改投入", proNo);
		Double wtjjbfb = 0.0;
		if (wt == null || "".equals(wt.trim())) {
			wtjjbfb = 0.0;
		} else {
			wtjjbfb = Double.valueOf(wt);
		}
		wtjjbfb = wtjjbfb / 100;
		Double wtjjgzl = count * wtjjbfb;
		// 测试执行工作量
		String cs = labelMeasureConfigDao.solveHours("测试执行投入", proNo);
		Double cszxbfb = 0.0;
		if (cs == null || "".equals(cs.trim())) {
			cszxbfb = 0.0;
		} else {
			cszxbfb = Double.valueOf(cs);
		}
		cszxbfb = cszxbfb / 100;
		Double cszxgzl = count * cszxbfb;
		// 自动化用例写作工作量
		String zdh = labelMeasureConfigDao.solveHours("自动化用例写作", proNo);
		Double zdhylxzbfb = 0.0;
		if (zdh == null || "".equals(zdh.trim())) {
			zdhylxzbfb = 0.0;
		} else {
			zdhylxzbfb = Double.valueOf(zdh);
		}
		zdhylxzbfb = zdhylxzbfb / 100;
		Double zdhylxzgzl = count * zdhylxzbfb;
		// 自动化用例通过率=自动化用例执行成功数/自动化用例数
		Double autoTestCasePassedRate = Double.valueOf(zdhylzxcgs) / Double.valueOf(zdhyls);
		if (zdhyls == 0) {
			autoTestCasePassedRate = 0.0;
		}
		autoTestCasePassedRate = testmeasureService.keepTwoDecimals(autoTestCasePassedRate);
		// 用例自动化率=自动化用例数/测试用例总数
		Double testCaseAutoRate = Double.valueOf(zdhyls) / Double.valueOf(zyls);
		if (zyls == 0) {
			testCaseAutoRate = 0.0;
		}
		testCaseAutoRate = testmeasureService.keepTwoDecimals(testCaseAutoRate);
		// 用例执行率=测试用例执行数/测试用例总数
		Double testCaseStartRate = Double.valueOf(zxyls) / Double.valueOf(zyls);
		if (zyls == 0) {
			testCaseStartRate = 0.0;
		}
		testCaseStartRate = testmeasureService.keepTwoDecimals(testCaseStartRate);
		// 缺陷密度=测试发现问题单总数/新增修改代码量
		Double defectDensity = Double.valueOf(wtdzs) / (Double.valueOf(xzxgdml) / 1000);
		if (xzxgdml == 0) {
			defectDensity = 0.0;
		}
		defectDensity = testmeasureService.keepTwoDecimals(defectDensity);
		// 用例密度=新增测试用例数/新增修改代码量
		Double testCaseDensity = Double.valueOf(xzcsyls) / (Double.valueOf(xzxgdml) / 1000);
		if (xzxgdml == 0) {
			testCaseDensity = 0.0;
		}
		testCaseDensity = testmeasureService.keepTwoDecimals(testCaseDensity);
		// 问题解决效率=已解决问题总数/问题解决投入工作量
		Double solvedRate = Double.valueOf(yjjwtzs) / Double.valueOf(wtjjgzl);
		if (wtjjgzl == 0) {
			solvedRate = 0.0;
		}
		solvedRate = testmeasureService.keepTwoDecimals(solvedRate);
		// 手工测试执行效率=手工用例执行次数/测试执行工作量
		Double manualTestStartRate = Double.valueOf(sgzxylzs) / Double.valueOf(cszxgzl);
		if (cszxgzl == 0) {
			manualTestStartRate = 0.0;
		}
		manualTestStartRate = testmeasureService.keepTwoDecimals(manualTestStartRate);
		// 自动化用例写作效率=测试自动化代码量/自动化用例写作工作量
		Double autoTestCaseWriteRate = Double.valueOf(cszdhdml) / Double.valueOf(zdhylxzgzl);
		if (zdhylxzgzl == 0) {
			autoTestCaseWriteRate = 0.0;
		}
		autoTestCaseWriteRate = testmeasureService.keepTwoDecimals(autoTestCaseWriteRate);
		
		
		//测试用例设计效率 = 设计测试用例总数 /用例设计投入工作量
		Double csylzs = labelMeasureConfigDao.queryCXylzs(proNo);
	    String csyl = labelMeasureConfigDao.solveHours("测试用例设计投入",proNo);
	    Double csylgzl = 0.0;
	    Double csylsjxl = 0.0;
	    if (csyl != null && !"".equals(csyl.trim())) {
	    	csylgzl = Double.valueOf(csyl)/100*count;
		} 
	    if(csylgzl > 0 ) {
	    	csylsjxl = testmeasureService.keepTwoDecimals(Double.valueOf(csylzs)/csylgzl);
	    }
	    
		//人均月代码量
	    String kftr = labelMeasureConfigDao.solveHours("开发投入",proNo);
	    Double kftrgzl = 0.0;
	    Double rjydml = 0.0;
	    if(kftr != null && !"".equals(kftr.trim())) {
	    	kftrgzl = testmeasureService.keepTwoDecimals(Double.valueOf(kftr)/100*count);
	    }
	    if(kftrgzl>0) {
	    	rjydml = testmeasureService.keepTwoDecimals(Double.valueOf(dyxzxgdml)/kftrgzl*22.5);
	    }
		//E2E代码生产率
	    Double E2Edml = 0.0;
	    if(count>0) {
	    	E2Edml = testmeasureService.keepTwoDecimals(Double.valueOf(dyxzxgdml)/count);
	    }
		//代码开发效率
	    Double dmkfxl = 0.0;
	    if(kftrgzl>0) {
	    	dmkfxl = testmeasureService.keepTwoDecimals(Double.valueOf(dyxzxgdml)/kftrgzl);
	    }
		
		//C++语言代码量
		String codeType = FileType.getFileTypes("C");
		Double Cnum = 0.0;
		List<Map<String, Object>> maps = managersDao.queryOMPUserSelected(proNo);
		List<String> kfList = new ArrayList<String>();
		List<String> sgList = new ArrayList<String>();
		for (Map<String, Object> map : maps) {
			if("开发工程师".equals(map.get("ROLE").toString())) {
				String svnGitNo = StringUtilsLocal.valueOf(map.get("svn_git_no"));
	    		String author = StringUtilsLocal.valueOf(map.get("HW_ACCOUNT"));
	    		
	    		if(StringUtilsLocal.isBlank(author)) {
	    			continue;
	    		}
	    		if(StringUtilsLocal.isBlank(svnGitNo)) {
	    			kfList.add(author);//华为工号
				}else {
					sgList.add(svnGitNo);//华为工号
				}
				
			}
		}
		Double cNum1 = 0.0;
		Double cNum2 = 0.0;
		if(kfList.size()>0) {
			cNum1 = labelMeasureConfigDao.queryCodeNumByc(codeType,"("+ StringUtilsLocal.listToSqlIn(kfList)+")");
		}
		if(sgList.size()>0) {
			cNum2 = labelMeasureConfigDao.queryCodeNumBycWX(codeType,"("+ StringUtilsLocal.listToSqlIn(sgList)+")");
		}
		Double cNum = cNum1+cNum2;
		//获取当前项目
		
		Map<String, Double> result = new HashMap<>();
		if ("toAuto".equals(flag.trim())) {
			result.put("自动化用例通过率", autoTestCasePassedRate);
			result.put("用例自动化率", testCaseAutoRate);
		}else {
			result.put("手工测试执行效率", manualTestStartRate);
		}
		result.put("用例执行率", testCaseStartRate);
		result.put("测试缺陷密度", defectDensity);
		result.put("测试用例密度", testCaseDensity);
		result.put("问题解决效率", solvedRate);
		result.put("自动化用例写作效率", autoTestCaseWriteRate);
		result.put("测试用例设计效率", csylsjxl);
		result.put("人均月代码量", rjydml);
		result.put("E2E代码生产率", E2Edml);
		result.put("代码开发效率", dmkfxl);
		result.put("C++语言代码量",cNum);
		//result.put("", value);
		return result;
	}

	/**
	 * @Description: 查询所有指标信息
	 * @author chenchenhui
	 * @date 2018年5月8日
	 * @version V1.0
	 */
	public List<Map<String, Object>> getMeasures() {
		return dao.getMeasures();
	}

	/**
	 * @Description: 获取流程指标配置页面信息
	 * @author Administrator
	 * @date 2018年5月8日
	 * @version V1.0
	 */
	public List<ProjectLabelMeasure> getMeasureConfigPageInfo(String selCategory, String proNo, String version,
			String ite) {
		// 1、获取所有流程信息
		List<Map<String, Object>> allLabList = labelService.queryLabel(selCategory);
		

		// 2、获取当前项目配置流程信息(初次默认查询通用流程数据)
		List<Map<String, Object>> labList = dao.getProjectLabs(proNo, selCategory);
		List<String> labIds = compareToContains(labList);


		List<ShowMeasure> mList = null;

		List<ProjectLabelMeasure> pList = new ArrayList<ProjectLabelMeasure>();
		// 流程信息封装
		for (Map<String, Object> lab : allLabList) {
			// 3、 根据项目号、版本、迭代 查询当前流程配置的指标信息
			List<Map<String, Object>> measureList = dao.getLabMeareListByProNo(lab.get("ID").toString(),proNo, version, ite);
			List<String> measureIds = compareToContains(measureList);
			// 封装流程
			ProjectLabelMeasure pm = new ProjectLabelMeasure();
			pm.setId(Integer.valueOf(lab.get("ID").toString()));
			pm.setLabelCategory(lab.get("CATEGORY").toString());
			pm.setLabelName(lab.get("TITLE").toString());
			// pm.setLabelOrder(lab.get("order").toString());
			pm.setIsSelect("0");
			if (labIds.contains(lab.get("ID").toString())) {
				pm.setIsSelect("1");// 选中

			}
			// 根据流程ID查询所有指标
			List<Map<String, Object>> measures = dao.getAllCategorysByLabId(lab.get("ID").toString());

			List<ShowMeasureCat> catList = new ArrayList<ShowMeasureCat>();
			Map<String, String> catMap = new HashMap<String, String>();// 记录类目是否已存在

			mList = new ArrayList<ShowMeasure>();// 存所有指标

			for (Map<String, Object> measure : measures) {

				// 统计类目
				if (!catMap.containsKey(measure.get("CATEGORY").toString())) {
					ShowMeasureCat model = new ShowMeasureCat();
					model.setLabelId(measure.get("LABLE_ID").toString());// 流程下的类目
					model.setMeasureCategory(measure.get("CATEGORY").toString());
					model.setIsSelect("0");// 默认不选中类目

					catList.add(model);

					catMap.put(measure.get("CATEGORY").toString(), measure.get("CATEGORY").toString());
				}

				ShowMeasure sm = new ShowMeasure();
				sm.setMeasureId(Integer.valueOf((measure.get("ID").toString())));// 流程配置id
				sm.setMeasureCategory(measure.get("CATEGORY").toString());// 指标分类
				//遍历指标，判断值是否已更新过
				MeasureRange measureRange = measurerangedao.queryMeasureRange(proNo,measure.get("ID").toString());
				if(measureRange == null){
					sm.setUpper(measure.get("UPPER")==null?"":measure.get("UPPER").toString());
					sm.setChallenge(measure.get("CHALLENGE")==null?"":measure.get("CHALLENGE").toString());
					sm.setTarget(measure.get("TARGET")==null?"":measure.get("TARGET").toString());
					sm.setLower(measure.get("LOWER")==null?"":measure.get("LOWER").toString());
					sm.setCollectType(measure.get("COLLECT_TYPE")==null?"":measure.get("COLLECT_TYPE").toString());
					String rule = null == measure.get("COMPUTE_RULE")?"":measure.get("COMPUTE_RULE").toString();
					if(!"12".equals(rule) && !"13".equals(rule)){
						rule = "11";
					}
					sm.setComputeRule(rule);
				}else{
					sm.setUpper(measureRange.getUpper()==null?"":measureRange.getUpper());
					sm.setChallenge(measureRange.getChallenge()==null?"":measureRange.getChallenge());
					sm.setTarget(measureRange.getTarget()==null?"":measureRange.getTarget());
					sm.setLower(measureRange.getLower()==null?"":measureRange.getLower());
					sm.setCollectType(StringUtils.isEmpty(measureRange.getCollectType())?"":measureRange.getCollectType());
					sm.setComputeRule(StringUtils.isEmpty(measureRange.getComputeRule())?"11":measureRange.getComputeRule());
				}
				sm.setUnit(measure.get("UNIT")==null?"":measure.get("UNIT").toString());
				sm.setMeasureName(measure.get("name")==null?"":measure.get("name").toString());// 指标名称
				// sm.setMeasureOrder(measure.get("order").toString());// 指标排序
				sm.setIsSelect("0");// 是否选中
				if (measureIds.contains(measure.get("ID").toString())) {
					sm.setIsSelect("1");// 选中
				}
				mList.add(sm);
			}

			// 遍历类目
			for (ShowMeasureCat cat : catList) {
				List<ShowMeasure> meaList = new ArrayList<ShowMeasure>();

				for (ShowMeasure showMeasure : mList) { // 标识过的指标
					if (cat.getMeasureCategory().equals(showMeasure.getMeasureCategory())) {

						if ("1".equals(showMeasure.getIsSelect())) {
							cat.setIsSelect("1");
						}
						meaList.add(showMeasure);
					}
				}
				cat.setMeasureList(meaList);
			}
			pm.setMeasureCatList(catList);
			pList.add(pm);
		}
		return pList;
	}
	public List<TeamLabelMeasure> getMeasureConfigPageInfoByTeamId(String selCategory, String teamId, String version,
			String ite) {
		// 1、获取所有流程信息
		List<Map<String, Object>> allLabList = labelService.queryLabel(selCategory);
		

		// 2、获取当前项目配置流程信息(初次默认查询通用流程数据)
		List<Map<String, Object>> labList = dao.getProjectLabsByTeamId(teamId, selCategory);
		List<String> labIds = compareToContains(labList);


		List<ShowMeasure> mList = null;

		List<TeamLabelMeasure> pList = new ArrayList<TeamLabelMeasure>();
		// 流程信息封装
		for (Map<String, Object> lab : allLabList) {
			// 3、 根据项目号、版本、迭代 查询当前流程配置的指标信息
			List<Map<String, Object>> measureList = dao.getLabMeareListByTeamId(lab.get("ID").toString(),teamId, version, ite);
			List<String> measureIds = compareToContains(measureList);
			// 封装流程
			TeamLabelMeasure pm = new TeamLabelMeasure();
			pm.setId(Integer.valueOf(lab.get("ID").toString()));
			pm.setLabelCategory(lab.get("CATEGORY").toString());
			pm.setLabelName(lab.get("TITLE").toString());
			// pm.setLabelOrder(lab.get("order").toString());
			pm.setIsSelect("0");
			if (labIds.contains(lab.get("ID").toString())) {
				pm.setIsSelect("1");// 选中
			}
			// 根据流程ID查询所有指标
			List<Map<String, Object>> measures = dao.getAllCategorysByLabId(lab.get("ID").toString());

			List<TeamShowMeasureCat> catList = new ArrayList<TeamShowMeasureCat>();
			Map<String, String> catMap = new HashMap<String, String>();// 记录类目是否已存在

			mList = new ArrayList<ShowMeasure>();// 存所有指标

			for (Map<String, Object> measure : measures) {

				// 统计类目
				if (!catMap.containsKey(measure.get("CATEGORY").toString())) {
					TeamShowMeasureCat model = new TeamShowMeasureCat();
					model.setLabelId(measure.get("LABLE_ID").toString());// 流程下的类目
					model.setMeasureCategory(measure.get("CATEGORY").toString());
					model.setIsSelect("0");// 默认不选中类目

					catList.add(model);

					catMap.put(measure.get("CATEGORY").toString(), measure.get("CATEGORY").toString());
				}

				ShowMeasure sm = new ShowMeasure();
				sm.setMeasureId(Integer.valueOf((measure.get("ID").toString())));// 流程配置id
				sm.setMeasureCategory(measure.get("CATEGORY").toString());// 指标分类
				//遍历指标，判断值是否已更新过
				TeamMeasureRange teamMeasureRange = measurerangedao.queryMeasureRangeByTeamId(teamId,measure.get("ID").toString());
				if(teamMeasureRange == null){
					sm.setUpper(measure.get("UPPER")==null?"":measure.get("UPPER").toString());
					sm.setChallenge(measure.get("CHALLENGE")==null?"":measure.get("CHALLENGE").toString());
					sm.setTarget(measure.get("TARGET")==null?"":measure.get("TARGET").toString());
					sm.setLower(measure.get("LOWER")==null?"":measure.get("LOWER").toString());
					sm.setCollectType(measure.get("COLLECT_TYPE")==null?"":measure.get("COLLECT_TYPE").toString());
					String rule = measure.get("COMPUTE_RULE").toString();
					if(!"12".equals(rule) && !"13".equals(rule)){
						rule = "11";
					}
					sm.setComputeRule(rule);
				}else{
					sm.setUpper(teamMeasureRange.getUpper()==null?"":teamMeasureRange.getUpper());
					sm.setChallenge(teamMeasureRange.getChallenge()==null?"":teamMeasureRange.getChallenge());
					sm.setTarget(teamMeasureRange.getTarget()==null?"":teamMeasureRange.getTarget());
					sm.setLower(teamMeasureRange.getLower()==null?"":teamMeasureRange.getLower());
					sm.setCollectType(teamMeasureRange.getCollectType()==null?"":teamMeasureRange.getCollectType());
					sm.setComputeRule(StringUtils.isEmpty(teamMeasureRange.getComputeRule())?"11":teamMeasureRange.getComputeRule());
				}
				sm.setUnit(measure.get("UNIT")==null?"":measure.get("UNIT").toString());
				sm.setMeasureName(measure.get("name")==null?"":measure.get("name").toString());// 指标名称
				// sm.setMeasureOrder(measure.get("order").toString());// 指标排序
				sm.setIsSelect("0");// 是否选中
				if (measureIds.contains(measure.get("ID").toString())) {
					sm.setIsSelect("1");// 选中
				}
				mList.add(sm);
			}

			// 遍历类目
			for (TeamShowMeasureCat cat : catList) {
				List<ShowMeasure> meaList = new ArrayList<ShowMeasure>();

				for (ShowMeasure showMeasure : mList) { // 标识过的指标
					if (cat.getMeasureCategory().equals(showMeasure.getMeasureCategory())) {

						if ("1".equals(showMeasure.getIsSelect())) {
							cat.setIsSelect("1");
						}
						meaList.add(showMeasure);
					}
				}
				cat.setMeasureList(meaList);
			}
			pm.setMeasureCatList(catList);
			pList.add(pm);
		}
		return pList;
	}

	/**
	 * @Description: 将对象唯一索引放入list
	 * @author cch
	 * @date 2018年5月8日
	 */
	public List<String> compareToContains(List<Map<String, Object>> list) {
		List<String> li = new ArrayList<String>();
		if (list.size() > 0) {
			for (Map<String, Object> lab : list) {
				li.add(lab.get("ID").toString());
			}
		}
		return li;
	}

	/**
	 * @Description: 获取流程所有类目列表
	 * @author Administrator
	 * @date 2018年5月9日
	 */
	public Map<String, Object> getAllLabCategory(String proNo) {
		List<String> categoryList = dao.getAllLabCategory();
		//当前项目配置流程类目
		String isSelectCate = dao.getIsSelectCategory(proNo);
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isEmpty(isSelectCate)){
			isSelectCate = "无指标流程";
		}
		
		for (String ct : categoryList) {
			map.put(ct, "");
			if(isSelectCate.equals(ct)){
				map.put(ct, "selected");
			}
		}
		return map;
	}
	public Map<String, Object> getAllLabCategoryByTeamId(String teamId) {
		List<String> categoryList = dao.getAllLabCategory();
		//当前项目配置流程类目
		String isSelectCate = dao.getIsSelectCategoryByTeamId(teamId);
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isEmpty(isSelectCate)){
			isSelectCate = "业务线通用";
		}
		
		for (String ct : categoryList) {
			map.put(ct, "");
			if(isSelectCate.equals(ct)){
				map.put(ct, "selected");
			}
		}
		return map;
	}

	/**
	 * @Description: 更新流程指标配置信息
	 * @author Administrator
	 * @date 2018年5月10日
	 */
	public void updateMeasureConfig(ProjectLabelMeasureList projectLabelMeasureList) {
		List<ProjectLabelMeasure> projectLabelMeasure = projectLabelMeasureList.getProjectLabelMeasures();
		String proNo = projectLabelMeasureList.getProNo();
		String status = "0";
		int result = 0;
		// 遍历model更新指标对象
		for (ProjectLabelMeasure pl : projectLabelMeasure) {
			String labId = pl.getId().toString();
			ProjectLabelConfig plc = dao.getProjectLabConfigByKey(labId, proNo);

			if ("0".equals(pl.getIsSelect()) || null == pl.getIsSelect()) {
				status = "1";
				if(null!=plc) {
					result = dao.updateProjectLabConfig(labId, proNo, status);// is_delete = 1更新流程状态
					//更新流程下所有的指标状态is_delete=1
					dao.updateMeasures(plc.getId()+"");
				}		
				
			} else {// 选择状态
				// 配置一条新的流程
				if (null == plc) {
					result = dao.insertProjectLableConfig(labId, proNo);
				} else {
					status = "0";
					result = dao.updateProjectLabConfig(labId, proNo, status);
				}
				String projectLabId = dao.getProjectConfigId(labId, proNo);
				List<ShowMeasureCat> ShowMeasureCat = pl.getMeasureCatList();// 类目集合
				// 遍历类目集合
				for (ShowMeasureCat cats : ShowMeasureCat) {
					List<ShowMeasure> ShowMeasure = cats.getMeasureList();
					for (ShowMeasure measure : ShowMeasure) {// 遍历指标
						String measureId = measure.getMeasureId().toString();
						if ("0".equals(measure.getIsSelect())) {// 指标未选择
							// 更新当前指标状态
							status = "1";
							result = dao.updateConfigMeasure(measureId, projectLabId, status);
						} else {
							// 判断当前指标是否配置过
							LabelMeasureConfig lmc = dao.getMeasureConfig(measureId, projectLabId);
							if (null != lmc) {
								status = "0";
								// 如果已配置则更新
								result = dao.updateConfigMeasure(measureId, projectLabId, status);
							} else {// 插入当前指标记录
								result = dao.insertLableMeasureConfig(measureId, projectLabId);
							}
						}
					}
				}
			}

		}
		configProjectStatus(proNo);
//		if("0".equals(codeGainTypeDao.getProIsKx(proNo))) {//仅在项目为可信项目状态下修改
//			codeGainTypeService.saveCodeGainTypeByParameterId(codeGainTypeDao.getReliableStaticMeasureCount(proNo)>0?"0":"1", proNo, 164);
//		}
	}

	private void configProjectStatus(String proNo) {
		List<CodeGainType> codeList= codeGainTypeService.getCodeTypeByNoParameterId(proNo, "162");
		if(null != codeList && codeList.size()>0) {
			if("0".equals(codeList.get(0).getType())) {//项目上线为重点项目
				codeGainTypeService.saveProjectQualityFlag(proNo);;
			}
		}
	}
	
	public void saveMeasureConfig(String projectId) {
		List<Label> list = projectLableDao.queryProjectChecklist(projectId);
		String ladelIds = "";
		List<String> measureId = new ArrayList<>();
		for (Label lab : list) {
			if("".equals(ladelIds)) {
				ladelIds = lab.getPlId();
			}else {
				ladelIds += (","+lab.getPlId());
			}
			List<Map<String, Object>> measureList = dao.getLabMeareListByProNo(lab.getPlId() , projectId, null, null);
			measureId.addAll(compareToContains(measureList));
		}
		String measureIds = String.join(",", measureId);
		Map<String,String> map = new HashMap<>();
		map.put("no", projectId);
		map.put("ladelIds", ladelIds);
		map.put("measureIds", measureIds);
		dao.replaceMeasureConfig(map);
	}
	
	public void updateMeasureConfigByTeamId(TeamLabelMeasureList teamLabelMeasureList) {
		List<TeamLabelMeasure> teamLabelMeasure = teamLabelMeasureList.getTeamLabelMeasures();
		String teamId = teamLabelMeasureList.getTeamId();
		// String proNo = "";
		String status = "0";
		int result = 0;
		// 遍历model更新指标对象
		for (TeamLabelMeasure pl : teamLabelMeasure) {
			String labId = pl.getId().toString();
			List<teamLabelConfig> list = dao.getTeamLabNumByTeamId(labId, teamId);

			if ("0".equals(pl.getIsSelect()) || null == pl.getIsSelect()) {
				status = "1";
				result = dao.updateTeamLabConfig(labId, teamId, status);// is_delete = 1更新流程状态
				//更新流程下所有的指标状态is_delete=1
				List<Map<String, Object>> pMeasuresList = dao.getLabMeasureByTeam(labId, "", "", teamId);
				if(pMeasuresList.size()>0) {
					for (int i = 0; i < pMeasuresList.size(); i++) {
						dao.updateMeasuresByTeamId(pMeasuresList.get(i).get("ID").toString()) ;
					}
				}
				
			} else {// 选择状态
				// 配置一条新的流程
				if (list.size() == 0) {
					result = dao.insertTeamLableConfigByTeamId(labId, teamId);
				} else {
					status = "0";
					result = dao.updateTeamLabConfig(labId, teamId, status);// is_delete = 1
				}
				String teamLabId = dao.getTeamConfigId(labId, teamId);
				List<TeamShowMeasureCat> teamShowMeasureCat = pl.getMeasureCatList();// 类目集合
				// 遍历类目集合
				for (TeamShowMeasureCat cats : teamShowMeasureCat) {
					// 判断类目选择状态
					if ("0".equals(cats.getIsSelect())) {// 类目未选择
						// 更新类目对应的所有配置的指标状态is_deleted = 1
						List<ShowMeasure> ShowMeasure = cats.getMeasureList();
						status = "1";
						for (ShowMeasure measure : ShowMeasure) {
							String measureId = measure.getMeasureId().toString();
							result = dao.updateConfigMeasureByTeam(measureId, teamLabId, status);
						}
					} else {// 类目选择状态
							// 遍历类目指标集合
						List<ShowMeasure> ShowMeasure = cats.getMeasureList();
						for (ShowMeasure measure : ShowMeasure) {// 遍历指标
							String measureId = measure.getMeasureId().toString();
							if ("0".equals(measure.getIsSelect())) {// 指标未选择
								// 更新当前指标状态
								status = "1";
								result = dao.updateConfigMeasureByTeam(measureId, teamLabId, status);
							} else {
								// 判断当前指标是否配置过
								List<TeamMeasureConfig> configList = dao.getMeasureConfigByTeam(measureId, teamLabId);
								if (configList.size() > 0) {
									status = "0";
									// 如果已配置则更新
									result = dao.updateConfigMeasureByTeam(measureId, teamLabId, status);
								} else {// 插入当前指标记录
									result = dao.insertTeamMeasureConfig(measureId, teamLabId);
								}
							}
						}
					}

				}
			}

		}
	}
	
	/**
	* @Description:更新当前流程所有指标 
	* @author Administrator  
	* @date 2018年6月14日  
	 */
	private Integer updateMeasures(String labId) {
		Integer result = 0;
		try {
			 result = dao.updateMeasures(labId);
		} catch (Exception e) {
			logger.error("dao.updateMeasures exception, errror: "+e.getMessage());
		}
		return result;
	}

	/**
	 * @Description：给当前项目配置默认流程(开发，测试) 
	 * @date 2018年5月14日
	 */
	public void deployDefaultLabs(String projNo) {
		try {
			String[] labIds = {"30", "31","32","33","34","35","36"};
			for (int i = 0; i < labIds.length; i++) {
				String labId = labIds[i];
				// 查询是否已经配置
				ProjectLabelConfig plc = dao.getProjectLabConfigByKey(labId, projNo);
				if (null == plc) {// 未配置过此流程
					dao.insertProjectLableConfig(labId, projNo);
					// 获取项目配置流程id
					ProjectLabelConfig config = dao.getProjectLabConfigByKey(labId, projNo);
					// 给当前流程配置对应指标（查询流程对应的指标项）
					List<Map<String, Object>> measures = dao.getAllCategorysByLabId(labIds[i]);
					if (measures.size() > 0) {
						for (Map<String, Object> measure : measures) {
							// 查询当前流程是否配置过该指标
							LabelMeasureConfig lmc = dao.getMeasureConfig(measure.get("ID").toString(),	projNo);
							if (null == lmc) {
								dao.insertLableMeasureConfig(measure.get("ID").toString(), config.getId().toString());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("dao.insertLableMeasureConfig exception, error: "+e.getMessage());
		}
	}
	
	public void deployTeamDefaultLabs(String teamId) {
		try {
			String[] labIds = {"30", "31","32","33","34","35","36"};
			for (int i = 0; i < labIds.length; i++) {
				String labId = labIds[i];
				// 查询是否已经配置
				List<teamLabelConfig> list = dao.getTeamLabNumByTeamId(labId, teamId);
				if (list.size() == 0) {// 未配置过此流程
					dao.insertTeamLableConfigByTeamId(labId, teamId);
					// 获取团队配置流程id
					List<teamLabelConfig> config = dao.getTeamLabNumByTeamId(labId, teamId);
					// 给当前流程配置对应指标（查询流程对应的指标项）
					List<Map<String, Object>> measures = dao.getAllCategorysByLabId(labIds[i]);
					if (measures.size() > 0) {
						for (Map<String, Object> measure : measures) {
							// 查询当前流程是否配置过该指标
							List<TeamMeasureConfig> lmList = dao.getMeasureConfigByTeam(measure.get("ID").toString(),
									teamId);
							if (lmList.size() == 0) {
								dao.insertTeamMeasureConfig(measure.get("ID").toString(),
										config.get(0).getId().toString());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("dao.insertTeamMeasureConfig exception, error: "+e.getMessage());
		}
	}

	/**
	 * @Description: 更新指标配置实际值
	 * @author Administrator
	 * @date 2018年5月15日
	 */
	public void saveActualValMeasureConfig(String id, String actualVal) {
		String val = "";
		if(StringUtils.isNotEmpty(actualVal)) {
			Double value = Double.valueOf(actualVal);
			val = String.format("%.2f",value);
		}
		try {
			Integer aInteger = dao.saveActualValMeasureConfig(id, actualVal);
			SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM");
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH,0);
			String yearMonth = sFormat.format(cal.getTime());
			MonthMeasure mm = new MonthMeasure();
			// 保存至历史数据库中
			if (aInteger == 1) {
				// 查询labid
					// 判断指标实际值是否已计算
					List<MonthMeasure> isFirst = measureDao.isFirstCalculate(id, yearMonth);
					if (isFirst.size() > 0) {// 更新
						mm.setLabMeasureConfigId(id);
						mm.setValue(val);
						mm.setCopyDate(yearMonth + "-01");
						measureDao.updateMonthMeasures(mm);
					} else {// 插入
						mm.setLabMeasureConfigId(id);
						mm.setValue(val);
						mm.setCopyDate(yearMonth + "-01");
						measureDao.saveMonthMeasure(mm);
					}
				}
		} catch (Exception e) {
			logger.error("measureDao.isFirstCalculate exception, error；"+e.getMessage());
		}
	}

	/**
	 * @Description: 记录每月的指标记录实际值
	 * @author Administrator
	 * @date 2018年5月21日
	 */
	public List<Map<String, Object>> updateMeasureMonth(String proNo) {
		List<Map<String, Object>> list = dao.updateMeasureMonth(proNo);
		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				// 判断指标是否已经存储了月记录
				Map<String, Object> hismap = dao.queryMonthMeasure(map.get("ID").toString());
				if (null != hismap) {
					dao.updateValue(hismap.get("id").toString(), map.get("ACTUAL_VAL").toString());
				} else {
					dao.insertVal(map.get("ACTUAL_VAL").toString(), map.get("ID").toString());
				}
			}
		}
		return list;
	}
	
	/**
	* @Description: 将当前配置模板清空
	* @author Administrator  
	* @date 2018年6月13日  
	 */
	public boolean queryMeasureConfigNum(String proNo, String labName) {
		boolean flag = false;
		List<ProjectLabelConfig> proLabs = dao.queryMeasureConfigNum(proNo, "('"+labName.trim()+"')");
		if(proLabs.size() == 0){
			flag = true;
		}
		return flag;
	}
	public boolean queryMeasureConfigByTeamId(String teamId, String labName) {
		boolean flag = false;
		List<teamLabelConfig> teamLabs = dao.queryMeasureConfigByTeamId(teamId, "('"+labName.trim()+"')");
		if(teamLabs.size() == 0){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 删除旧模板配置
	 * @param proNo
	 * @param labName
	 * @return
	 */

	public BaseResponse deleteMeasureConfig(String proNo, String labName){
		BaseResponse response = new BaseResponse();
		List<String> strSql = new ArrayList<>();
		try {
			List<ProjectLabelConfig> proLabs = dao.queryMeasureConfigNum(proNo, "('"+labName.trim()+"')");
			if(proLabs.size() > 0){
				for (ProjectLabelConfig pro : proLabs){
					strSql.add(pro.getId().toString());
				}
				dao.deleteMeasureConfig("("+ StringUtilsLocal.listToSqlIn(strSql)+")");
				dao.deleteLableMeasureConfig("("+ StringUtilsLocal.listToSqlIn(strSql)+")");
				response.setCode("success");
			}
		}catch (Exception e){
			response.setCode("fail");
			logger.error(e);
		}
		return response;
	}
	public BaseResponse deleteMeasureConfigByTeamId(String teamId, String labName){
		BaseResponse response = new BaseResponse();
		List<String> strSql = new ArrayList<>();
		try {
			List<teamLabelConfig> teamLabs = dao.queryMeasureConfigByTeamId(teamId, "('"+labName.trim()+"')");
			if(teamLabs.size() > 0){
				for (teamLabelConfig team : teamLabs){
					strSql.add(team.getId().toString());
				}
				dao.deleteMeasureConfigByTeamId("("+ StringUtilsLocal.listToSqlIn(strSql)+")");
				dao.deleteLableMeasureConfigByTeamId("("+ StringUtilsLocal.listToSqlIn(strSql)+")");
				response.setCode("success");
			}
		}catch (Exception e){
			response.setCode("fail");
			logger.error(e);
		}
		return response;
	}
	
	
	/*
	 * 数据组装
	 */
	public List<LinkedHashMap<String, Object>> queryMetricIndex(String plId, String category,String proNo) {
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM");
		        String startDate = "";// year+month
				String endDate = "";// year+month
				// 获取项目启动时间
				Map<String, Object> map = buOverviewService.getProjOverveiwData(proNo);
				if (map != null) {
					startDate = map.get("startDate") == null ? "" : sFormat.format(map.get("startDate"));
					endDate = map.get("endDate") == null ? "" : sFormat.format(map.get("endDate"));
				}
				List<String> ym = measureService.getYearMonthFromStart(startDate, endDate);
				System.out.println(ym);
		        // 获取项目阶段配置的所有指标
				List<Measure> measuresList = dao.queryMetricIndex(plId, category);

				// 存储返回值数据
				List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

				// 存所有指标名称
				List<String> columnName = new ArrayList<String>();

				// 存所有周期
				List<String> allTime = new ArrayList<String>();

				for (Measure measure : measuresList) {
					if (StringUtils.isNotBlank(measure.getName()) && !columnName.contains(measure.getName())) {// 所有指标名称
						columnName.add(measure.getName());
					}
					if (StringUtils.isNotBlank(measure.getCopyDate()) && !allTime.contains(measure.getCopyDate())) {// 项目周期
						allTime.add(measure.getCopyDate());
					}
				}
				LinkedHashMap<String, Object> mapColumn = new LinkedHashMap<String, Object>();
				LinkedHashMap<String, Object> mapColumn1 = new LinkedHashMap<String, Object>();
				LinkedHashMap<String, Object> mapColumn2 = new LinkedHashMap<String, Object>();
				LinkedHashMap<String, Object> mapColumn3 = new LinkedHashMap<String, Object>();
				LinkedHashMap<String, Object> mapColumn4 = new LinkedHashMap<String, Object>();
				LinkedHashMap<String, Object> mapColumn5 = new LinkedHashMap<String, Object>();
				for (int i = 0; i < columnName.size(); i++) {
					for (Measure measure : measuresList) {
						if (measure.getName().equals(columnName.get(i))) {
							// 第一行data
							mapColumn.put("time", "迭代(或时间)");
							if(StringUtils.isNotEmpty(measure.getUnit())) {
								mapColumn.put("col" + i, measure.getName() + "(" + measure.getUnit() + ")");
							}else {
								mapColumn.put("col" + i, measure.getName());
							}

							// 第二行upper
							mapColumn1.put("time", "上限值");
							mapColumn1.put("col" + i, measure.getUpper());

							// 第三行lower
							mapColumn2.put("time", "下限值");
							mapColumn2.put("col" + i, measure.getLower());

							// 第四行target
							mapColumn3.put("time", "目标值");
							mapColumn3.put("col" + i, measure.getTarget());
						}
					}
				}
				list.add(mapColumn);
				list.add(mapColumn1);
				list.add(mapColumn2);
				list.add(mapColumn3);
				for (int k = 0; k < ym.size(); k++) {
					mapColumn4 = new LinkedHashMap<String, Object>();
					for (int i = 0; i < columnName.size(); i++) {
						if(allTime.contains(ym.get(k))) {
							for (Measure measure : measuresList) {
								if (measure.getCopyDate() != null && measure.getName().equals(columnName.get(i))
										&& ym.get(k).equals(measure.getCopyDate())) {
									mapColumn4.put("time", ym.get(k));
									mapColumn4.put("col" + i, StringUtils.isEmpty(measure.getValue())?0:measure.getValue());
								}
							}
						}else {
							mapColumn4.put("time", ym.get(k));
							mapColumn4.put("col" + i,0);
						}
						
					}
					list.add(mapColumn4);
				}
				// 日期、折线名称、每一个折线的月份指标值(组装折线图数据)
				LinkedHashMap<String, Object> echartMap = new LinkedHashMap<String, Object>();
				echartMap.put("daysBetweenToStrArray",ym);

				echartMap.put("topFiveQYK", columnName);

				List chartList = new ArrayList();
				mapColumn5.put("time", "合计");
				for (int i = 0; i < columnName.size(); i++) {
					Double num = 0.0;
					List<LinkedHashMap<String, Object>> yList = new ArrayList<>();
					for (int j = 4; j < list.size(); j++) {
						LinkedHashMap<String, Object> yMap = new LinkedHashMap<>();
						yMap.put("QYMC", columnName.get(i));
						yMap.put("COUNTDATE", list.get(j).get("time"));
						yMap.put("NM", (list.get(j).get("col" + i) == null || "".equals(list.get(j).get("col" + i))) ? 0 : list.get(j).get("col" + i));
						yList.add(yMap);
					}
					for(int k = 0;k<yList.size();k++) {
						num+=Double.valueOf(yList.get(k).get("NM").toString());
					}
					mapColumn5.put("col"+i, StringUtilsLocal.keepTwoDecimals(num));
					chartList.add(yList);
					echartMap.put("qymcByMCs", chartList);
				}
				list.add(mapColumn5);
				list.add(echartMap);
				return list;
	}

	/**
　　* @description: 更新measure_range/team_measure_range记录
　　* @param mr
　　* @return
　　* @throws
　　* @author chengchenhui
　　* @date 2019/6/19 14:30
　　*/
	public void updateMeasureRange(MeasureRange mr) throws Exception{
		boolean flag = mr.getNo().endsWith("_team");
		Date date = new Date();
		mr.setUpdateTime(date);
		if(!flag){
			if(StringUtilsLocal.isBlank(mr.getId())){
				mr.setCreateTime(date);
				measurerangedao.insert(mr);
			}else{
				measurerangedao.upDateRangeById(mr);
			}
		}else{
			updateTeamRange(mr);
		}
	}

	/**
	 * 更新team_measure_range记录
	 * @param mr
	 */
	public void updateTeamRange(MeasureRange mr){
		Date date = new Date();
		mr.setNo(mr.getNo().substring(0,mr.getNo().indexOf("_team")));
		if(StringUtilsLocal.isBlank(mr.getId())){
			mr.setCreateTime(date);
			measurerangedao.insertByTeamId(mr);
		}else{
			measurerangedao.updateByTeamId(mr);
		}
	}



//	public void updateMeasureRangeByTeamId(TeamMeasureRange mr) throws Exception{
//		TeamMeasureRange teamMeasureRange = measurerangedao.queryMeasureRangeByTeamId(mr.getTeamId(), mr.getMesureId());
//		if("1".equals(mr.getCollectType())) {
//			mr.setCollectType("手工录入");
//		}else {
//			mr.setCollectType("自动采集");
//		}
//		if(teamMeasureRange == null){
//			mr.setUpdateTime(new Date());
//			mr.setCreateTime(new Date());
//			measurerangedao.insertByTeamId(mr);
//		}else{
//			mr.setUpdateTime(new Date());
//			measurerangedao.updateByTeamId(mr);
//		}
//	}

	public String getTeamId(String projNo) {
		return dao.getTeamId(projNo);
	}
	/**
	　　* @description: 加载项目流程
	　　* @param projNo
	　　* @return List
	　　* @throws
	　　* @author chengchenhui
	　　* @date 2019/5/15 16:30
	　　*/
    public Map<String,Object> loadLabels(String projNo) {
		Map<String,Object> resultMap = new HashMap<>();
    	List<String> labList = new ArrayList<>();

        List<String> ids = inTmplDao.getConfigMeasureIds(projNo,false);
        if(ids == null || ids.size() == 0){
            return null;
        }
        Map<String,Object> param = new HashMap<>();
        param.put("proNo",projNo);
        param.put("ids",ids);
        param.put("flag",false);
		List<Map<String,Object>> listMaps = inTmplDao.getConfigRecords(param);

		if(null != listMaps && listMaps.size() >0) {
			for (Map<String, Object> map : listMaps) {
				if ((null != map.get("label") && StringUtils.isNotEmpty(map.get("label").toString()))) {
					labList.add(map.get("label").toString());
				}
			}
			labList = new ArrayList<>(new HashSet<>(labList));
			if (labList.size() > 0) {
				for (String label : labList) {
					List<String> categoryList = new ArrayList<>();
					for (Map<String, Object> map : listMaps) {
                        if (label.equals(map.get("label").toString()) && null != map.get("category")
                            && StringUtils.isNotEmpty(map.get("category").toString())) {
                            categoryList.add(map.get("category").toString());
                        }
					}
					resultMap.put(label, new ArrayList<>(new HashSet<>(categoryList)));
				}
			}
		}
		return resultMap;
    }
}
