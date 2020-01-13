package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.ActualValCalculateDao;
import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.ProjectLableDao;

/**
 * 指标实际值计算
 * @author Administrator
 *
 */
@Service
@SuppressWarnings("all")
public class ActualValCalculateService {

	@Autowired
	private ProjectLableDao dao;

	@Autowired
	private ActualValCalculateDao acDao;

	@Resource
	private TestMeasuresService testmeasureService;

	@Resource
	private IProjectManagersDao managersDao;

	public void haveConfigMeasure(List<Map<String, Object>> list, String proNo) {
		//项目投入总人力
		List<Map<String, Object>> maps = managersDao.queryOMPUserSelected(proNo);
		List<String> List = new ArrayList<String>();
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
			String account = "".equals(StringUtilsLocal.valueOf(map.get("account"))) ? StringUtilsLocal.valueOf(
                    map.get("ZR_ACCOUNT")): StringUtilsLocal.valueOf(map.get("account"));
			if("".equals(account)) {
				continue;
			}
			List.add(account);//项目投入总人力-中软工号
		}
		//统计开发人员代码量
		double count = 0;
		double kfNum = 0;
		
		String addCode = acDao.getCodeNumWx(proNo,"("+ StringUtilsLocal.listToSqlIn(kfList)+")");
		String addCodesg = acDao.getCodeNum(proNo,"("+ StringUtilsLocal.listToSqlIn(sgList)+")");
		if(StringUtils.isEmpty(addCode)) {
			kfNum = 0;
		}else {
			kfNum = Double.valueOf(addCode);
		}
		if(StringUtils.isEmpty(addCodesg)) {
			kfNum += 0;
		}else {
			kfNum += Double.valueOf(addCodesg);
		}
		//统计总工时
		String Hours = acDao.queryHoursAll("("+ StringUtilsLocal.listToSqlIn(List)+")");
		if(StringUtils.isNotEmpty(Hours)) {
			count = Double.valueOf(Hours);
		}else {
			count = 0;
		}
		
		for (Map<String, Object> map : list) {
			Double val = calculateVal(map.get("name").toString(), proNo,count,kfNum);
			if(val == null) {
				continue;
			}else {
				if(val > 0.0) {
					dao.saveActualValMeasureConfig(map.get("ID").toString(), String.valueOf(val));
				}else {
					dao.saveActualValMeasureConfig(map.get("ID").toString(), "");
				}
			}
		}
	}
	//计算指标实际值
	private Double calculateVal(String measureName, String proNo,double count,double kfNum) {
		double result = 0.0;
		String percent = "";
		double num = 0.0;
		if ("测试用例设计效率".equals(measureName.trim())) {// 测试用例设计效率
			String csNum = acDao.testCaseDesignEffec(proNo);
			if(csNum != null) {
				num = Double.valueOf(csNum);
			}
			percent = acDao.getWorkPercent(proNo, "测试用例设计投入");
			if (StringUtils.isNotEmpty(percent)) {
				if (Double.valueOf(percent) * count > 0) {
					result = testmeasureService.keepTwoDecimals(num / (Double.valueOf(percent)/100 * (count/8)));
				}
			}
		} 
		else if ("人均月代码量".equals(measureName.trim())) {
			percent = acDao.getWorkPercent(proNo, "开发投入");
			if (StringUtils.isNotEmpty(percent)) {
				if (Double.valueOf(percent) * count > 0) {
					result = testmeasureService.keepTwoDecimals(kfNum / (Double.valueOf(percent)/100 * (count/8))*22.5);
				}
			}
		} 
		else if ("E2E代码生产率".equals(measureName.trim())) {
			if(count > 0) {
				result = testmeasureService.keepTwoDecimals(kfNum / (count/8));
			}
		} 
		else if ("代码开发效率".equals(measureName.trim())) { 
			percent = acDao.getWorkPercent(proNo, "开发投入");
			if (StringUtils.isNotEmpty(percent)) {
				if (Double.valueOf(percent) * count > 0) {
					result = testmeasureService.keepTwoDecimals(kfNum / (Double.valueOf(percent)/100 * (count/8)));
				}
			}
		}else if("代码行数".equals(measureName.trim())) {
			result = testmeasureService.keepTwoDecimals(kfNum);
		}else if("问题数".equals(measureName.trim())) {
			// 测试发现问题单总数 关闭类型仅为（Closure After Correction）和状态不为Cancel
			Integer wtdzs = acDao.dtsCount(proNo);
			if (wtdzs == null) {
				wtdzs = 0;
			}
			result = wtdzs;
		}
		else {
			return null;
		}
		return result;
	}
	
}
