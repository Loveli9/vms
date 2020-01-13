package com.icss.mvp.service;

import com.icss.mvp.dao.*;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author fanpeng
 * 
 */
@Service("qualityModelService")
@EnableScheduling
@PropertySource("classpath:task.properties")
@Transactional
public class QualityModelService {
	@Resource
	private IQualityModelDao qualityModelDao;
	
	@Resource
	private LabelMeasureConfigDao configDao;
	
	@Resource
	private ActualValCalculateDao actualdao;
	
	@Resource
	private TestMeasuresService measuresService;
	
	@Resource
	private DevelopPageService developPageService;
	
	@Resource
	private LogModifyNumService logService;
	
	@Resource
	private ProjectInfoService projectInfoService;
	
	@Resource
	private ProjectLableService projectLableService;
	
	@Resource
	private IProjectParameterValueDao projectParameterValueDao;
	
	@Autowired
	private IProjectInfoDao projectInfoDao;
	private static Logger logger = Logger.getLogger(QualityModelService.class);
	
	public PageResponse<Map<String, Object>> queryProject(ProjectInfo projectInfo,String username,String[] measure_Ids, PageRequest pageRequest){
		PageResponse<Map<String, Object>> data = new PageResponse<>();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(pageRequest.getPageSize());
		pageInfo.setCurrentPage(pageRequest.getPageNumber()/pageRequest.getPageSize()+1);
		List<ProjectInfoVo> projectInfos=projectInfoService.projectInfos(projectInfo, username, pageInfo);		
		List<Map<String, Object>> proObjects =new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("MM");
		if (!CollectionUtils.isEmpty(projectInfos)) {
			int i=pageRequest.getPageNumber();
			for (ProjectInfoVo pro : projectInfos) {
				double value = 0.00;
				double value1 = 0.00;
				i++;
				for (String id : measure_Ids) {	
					List<ParameterValueNew> parameterValueNews=this.queryEfficiency(pro.getNo());
					Map<String, Object> val = new HashMap<>();
					val.put("id", i);
					val.put("no", pro.getNo());
					val.put("pduSpdt", pro.getPduSpdt());
					val.put("name", pro.getName());
					val.put("area", pro.getArea());
					val.put("pm", pro.getPm());
					val.put("hwpdu", pro.getHwpdu());
					val.put("hwzpdu", pro.getHwzpdu());
					if("144".equals(id)){
						val.put("measure", "代码量(LOC)");	
					}else if("145".equals(id)){
						val.put("measure", "投入人天");
					}else if("146".equals(id)){
						val.put("measure", "效率(LOC/人天)");
					}
					if (!CollectionUtils.isEmpty(parameterValueNews)) {
						for (ParameterValueNew parameterValueNew : parameterValueNews) {
							String ParameterId = parameterValueNew.getParameterId() + "";						
							if(id.equals(ParameterId)){
								val.put(format.format(parameterValueNew.getMonth()), parameterValueNew.getValue());
								if("144".equals(id)){
									value += parameterValueNew.getValue();	
								}
								if("145".equals(id)){
									value1 += parameterValueNew.getValue();
								}
							}			
						}		
						if("144".equals(id)){
							val.put("measureValue", StringUtilsLocal.keepTwoDecimals(value));
						}else if("145".equals(id)){
							val.put("measureValue", StringUtilsLocal.keepTwoDecimals(value1));
						}else if("146".equals(id)){
							val.put("measureValue", StringUtilsLocal.keepTwoDecimals(
                                    value1 == 0.00 ? 0.00 : (value / value1)));
						}				
					}	
					proObjects.add(val);
				}
			}
			data.setTotalCount(pageInfo.getTotalRecord());
			data.setData(proObjects);
		}
		return data;
	}
	
	public Integer searchTotalRecord(ProjectInfo projectInfo) {
		int i= qualityModelDao.searchTotalRecord(projectInfo);
		return i;
	}
	
	/**
	 * 查询历史项目
	 * @param projectInfo
	 * @param username
	 * @param no
	 * @param projectState
	 * @return
	 */
	public List<QualityStatistical> queryOldProjectInfo(ProjectInfo projectInfo,String username,String no,String projectState){
		List<ProjectInfoVo> oldProjectInfo=projectInfoService.queryOldProjectInfo(projectInfo, username, no, projectState);
		List<QualityStatistical> list = new ArrayList<QualityStatistical>();
		if(!CollectionUtils.isEmpty(oldProjectInfo)) {
			for (int i = 0; i < oldProjectInfo.size(); i++) {
				QualityStatistical qualityStatistical = qualityStatistic(oldProjectInfo.get(i));
				qualityStatistical.setId(Integer.toString(i+1));
				list.add(qualityStatistical);		
			}		
		}		
		return list;
	}

	/**
	 * 质量统计查询
	 * @param projectInfo
	 * @param username
	 * @param pageRequest
	 * @return
	 */
	public PageResponse<QualityStatistical> queryTable(ProjectInfo projectInfo,String username,PageRequest pageRequest){
		PageResponse<QualityStatistical> data = new PageResponse<>();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(pageRequest.getPageSize());
		pageInfo.setCurrentPage(pageRequest.getPageNumber()/pageRequest.getPageSize()+1);
		List<ProjectInfoVo> projectInfos=projectInfoService.projectInfos(projectInfo, username, pageInfo);
		List<QualityStatistical> list = new ArrayList<QualityStatistical>();
		if(!CollectionUtils.isEmpty(projectInfos)) {
			for (int i = 0; i < projectInfos.size(); i++) {
				QualityStatistical qualityStatistical = qualityStatistic(projectInfos.get(i));
				qualityStatistical.setId(Integer.toString(i+1));
				list.add(qualityStatistical);		
			}		
		}		
		data.setTotalCount(pageInfo.getTotalRecord());
		data.setData(list);	
		return data;
	}
	
	public PageResponse<ProjectInfo> queryProjectByTeam(String projectId, String username,PageRequest pageRequest){
		PageResponse<ProjectInfo> data = new PageResponse<>();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(pageRequest.getPageSize());
		pageInfo.setCurrentPage(pageRequest.getPageNumber());
		List<ProjectInfo> projectInfos=projectInfoService.getProjectInfos(projectId, username, pageInfo);
			
		data.setTotalCount(pageInfo.getTotalRecord());
		data.setData(projectInfos);	
		return data;
	}
	
	public QualityStatistical qualityStatistic(ProjectInfoVo projectInfoVo) {
		QualityStatistical qualityStatistical=new QualityStatistical();
		qualityStatistical.setPduSpdt(projectInfoVo.getPduSpdt());
		qualityStatistical.setName(projectInfoVo.getName());
		qualityStatistical.setSupplier("中软国际"); //  供应商
		qualityStatistical.setArea(projectInfoVo.getArea());
		qualityStatistical.setPm(projectInfoVo.getPm());
		qualityStatistical.setNo(projectInfoVo.getNo());
		qualityStatistical.setHwpdu(projectInfoVo.getHwpdu());//华为产品线
		qualityStatistical.setHwzpdu(projectInfoVo.getHwzpdu()); //华为子产品线
		
		List<ParameterValueNew> ParameterValueNews=qualityModelDao.searchStatistical(projectInfoVo.getNo());
		List<ParameterValueNew> ParameterValueNewsXml=qualityModelDao.searchStatisticalXml(projectInfoVo.getNo());
		ParameterValueNews.addAll(ParameterValueNewsXml);
		if(!CollectionUtils.isEmpty(ParameterValueNews)) {
			for (ParameterValueNew parameterValueNew : ParameterValueNews) {
				Double value =parameterValueNew.getValue();
				int parameterId=parameterValueNew.getParameterId();
				if(parameterId==150) {//当前人力
					qualityStatistical.setNowHuman(String.valueOf(value));
				}else if(parameterId==151) {//累计发现用例个数
					qualityStatistical.setAccDevCase(String.valueOf(value));
				}else if(parameterId==152) {//累计发现问题单数
					qualityStatistical.setAccFindPro(String.valueOf(value));
				}else if(parameterId==153) {//累计开发代码规模
					qualityStatistical.setAccDevCode(String.valueOf(value));
				}else if(parameterId==154) {//累计处理问题数
					qualityStatistical.setAccDealPro(String.valueOf(value));
				}else if(parameterId==155) {//累计投入人月
					qualityStatistical.setAccPutHuman(String.valueOf(value));
				}else if(parameterId==156) {//代码累计生产率(loc/人天)
					qualityStatistical.setAccCodePdt(String.valueOf(value));
				}else if(parameterId==157) {//用例累计生产率(个/人天)
					qualityStatistical.setAccCasePdt(String.valueOf(value));		
				}else if(parameterId==158) {//测试发现有效问题效率(个/人天)
					qualityStatistical.setTestFindEffPro(String.valueOf(value));	
				}else if(parameterId==159) {//处理问题效率(个/人天)
					qualityStatistical.setDealProEff(String.valueOf(value));
				}else if(parameterId==2){ //162 代码重复率 改为2
					qualityStatistical.setCodeRepeat(String.valueOf(StringUtilsLocal.keepTwoDecimals(value)));
				}else if(parameterId==163){ //163 代码最大圈复杂度
					qualityStatistical.setMaxComplex(String.valueOf(value));
				}else if(parameterId==3){ //164 平均圈复杂度  改为3
					qualityStatistical.setAverageComplex(String.valueOf(StringUtilsLocal.keepTwoDecimals(value)));
				}else if(parameterId==165){ //165 静态检查Pc-lintError\warning级别清零
					qualityStatistical.setPcLintErrorReset(String.valueOf(value));
				}else if(parameterId==1){ //166静态检查FindBugsHigh级别清零 改为1
					qualityStatistical.setLineErrorReset(String.valueOf(StringUtilsLocal.keepTwoDecimals(value)));
				}
			}
		}
		List<Map<String, String>> listMap=qualityModelDao.queryMeasure(projectInfoVo.getNo());
		if(!CollectionUtils.isEmpty(listMap)) {
			for (Map<String, String> map : listMap) {
//				if(String.valueOf(map.get("measureId")).equals("27")) {
//					qualityStatistical.setDealProEff(map.get("actualVal"));//处理问题效率(个/人天)
//				}else
//					if (String.valueOf(map.get("measureId")).equals("46")) {
//					qualityStatistical.setAccCodePdt(map.get("actualVal"));//代码累计生产率(loc/人天)
//				}else 
//					if (String.valueOf(map.get("measureId")).equals("57")) {
//					qualityStatistical.setLLTcover(map.get("actualVal"));//LLT覆盖率  57
//				}else if (String.valueOf(map.get("measureId")).equals("58")) {
//					qualityStatistical.setCodeRepeat(map.get("actualVal"));//代码重复率
//				}else if (String.valueOf(map.get("measureId")).equals("59")) {
//					qualityStatistical.setAverageComplex(map.get("actualVal"));//平均圈复杂度
//				}else if (String.valueOf(map.get("measureId")).equals("60")) {
//					qualityStatistical.setMaxComplex(map.get("actualVal"));//代码最大圈复杂度
//				}else 
				if ("67".equals(String.valueOf(map.get("measureId")))) {
					qualityStatistical.setTestDefects(map.get("actualVal"));//测试缺陷密度(个/Kloc)
				}else if ("68".equals(String.valueOf(map.get("measureId")))) {
					qualityStatistical.setTestCase(map.get("actualVal"));//测试用例密度(个/Kloc)
				}
			}
		}
		List<Map<String, String>> automaticMap=qualityModelDao.queryAutomaticMeasure(projectInfoVo.getNo());
		if(!CollectionUtils.isEmpty(automaticMap)) {
			for (Map<String, String> map : automaticMap) {
				if ("178".equals(map.get("measure_id"))) {
					qualityStatistical.setCompleteAutomaticCase(map.get("measure_value"));//已完成自动化用例个数
				}else if ("179".equals(map.get("measure_id"))) {
					qualityStatistical.setProceedAutomaticCase(map.get("measure_value"));//计划进行自动化的用例个数
				}else if ("183".equals(map.get("measure_id"))) {
					qualityStatistical.setAutomaticCaseExcute(map.get("measure_value"));//自动化用例执行率
				}else if ("184".equals(map.get("measure_id"))) {
					qualityStatistical.setAutomaticFactoryExcute(map.get("measure_value"));//自动化工厂执行用例数
				}else if ("185".equals(map.get("measure_id"))) {
					qualityStatistical.setAutomaticFactoryTotal(map.get("measure_value"));//自动化工厂总用例数
				}else if ("189".equals(map.get("measure_id"))) {
					qualityStatistical.setTotalAutomaticScript(map.get("measure_value"));//全量自动化脚本连跑通过率
				}else if ("190".equals(map.get("measure_id"))) {
					qualityStatistical.setOnceAutomaticExcuteSuccess(map.get("measure_value"));//一次自动化连跑执行成功总用例数
				}else if ("191".equals(map.get("measure_id"))) {
					qualityStatistical.setOnceAutomaticExcuteCase(map.get("measure_value"));//一次自动化连跑执行总用例数
				}
			}
		}
		return qualityStatistical;
	}
	/**
	 * 效率定时计算入库
	 * 每天凌晨一点执行
	 */
//	@Scheduled(cron = "${efficiency_Task_scheduled}")
	public void saveEfficiency() {
		//低产出
		List<ProjectInfo> projectInfos=projectInfoDao.queryEffectiveProjects();
		String type="";//不分代码类型
		String codeType="all";//所有代码量
		String roleType="all";
		String actualFlag="0";//实际工时
		if (!CollectionUtils.isEmpty(projectInfos)) {
			logger.info("效率看板定时计算开始------------->");
			for (ProjectInfo projectInfo : projectInfos) {
				List<GerenCode> gerenCodes=developPageService.developSearch(projectInfo.getNo(), type, codeType, roleType);
				if (!CollectionUtils.isEmpty(gerenCodes)) {
					List<ParameterValueNew> list = calculate(gerenCodes,null,projectInfo.getNo());
					projectParameterValueDao.insertParams(list);
				}
				String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
				List<MonthlyManpower> monthlyManpowers=measuresService.getMonthlyManpower(projectInfo.getNo(),actualFlag,months,true);
				if (!CollectionUtils.isEmpty(monthlyManpowers)) {
					List<ParameterValueNew> list=calculate(null,monthlyManpowers,projectInfo.getNo());
					projectParameterValueDao.insertParams(list);
				}
				if (!CollectionUtils.isEmpty(gerenCodes)&&!CollectionUtils.isEmpty(monthlyManpowers)) {
					List<ParameterValueNew> list=calculate(gerenCodes,monthlyManpowers,projectInfo.getNo());
					projectParameterValueDao.insertParams(list);
				}
			}
			logger.info("效率看板定时计算结束------------->");
		}
		
	}
	
	/**
	 * 计算总代码量 总工时以及效率
	 * @param gerenCodes
	 * @param monthlyManpowers
	 * @return
	 */
	private List<ParameterValueNew> calculate(List<GerenCode> gerenCodes,List<MonthlyManpower> monthlyManpowers,String no){
		double sum =0.0;//合计
		double january=0.0; //1月
		double february=0.0; //2月 
		double march=0.0; //3月 
		double april=0.0; //4月 
		double may=0.0; //5月 
		double june=0.0; //6月 
		double july=0.0; //7月 
		double august=0.0; //8月 
		double september=0.0; //9月 
		double october=0.0; //10月 
		double november=0.0; //11月 
		double december=0.0; //12月 
		double sumCode =0.0;//合计
		double januaryCode=0.0; //1月
		double februaryCode=0.0; //2月 
		double marchCode=0.0; //3月 
		double aprilCode=0.0; //4月 
		double mayCode=0.0; //5月 
		double juneCode=0.0; //6月 
		double julyCode=0.0; //7月 
		double augustCode=0.0; //8月 
		double septemberCode=0.0; //9月 
		double octoberCode=0.0; //10月 
		double novemberCode=0.0; //11月 
		double decemberCode=0.0; //12月 
		if (!CollectionUtils.isEmpty(gerenCodes)) {
			for (GerenCode gerenCode : gerenCodes) {
				januaryCode+=gerenCode.getJanuary();
				februaryCode+=gerenCode.getFebruary();
				marchCode +=gerenCode.getMarch();
				aprilCode+=gerenCode.getApril();
				mayCode+=gerenCode.getMay();
				juneCode+=gerenCode.getJune();
				julyCode+=gerenCode.getJuly();
				augustCode+=gerenCode.getAugust();
				septemberCode+=gerenCode.getAugust();
				octoberCode+=gerenCode.getOctober();
				novemberCode+=gerenCode.getNovember();
				decemberCode+=gerenCode.getDecember();
			}
			sumCode=januaryCode+februaryCode+marchCode+aprilCode+mayCode+juneCode+julyCode+augustCode+septemberCode+octoberCode+novemberCode+decemberCode;
		}
		if (!CollectionUtils.isEmpty(monthlyManpowers)) {
			for (MonthlyManpower monthlyManpower : monthlyManpowers) {
				january+=monthlyManpower.getJanuary();
				february+=monthlyManpower.getFebruary();
				march +=monthlyManpower.getMarch();
				april+=monthlyManpower.getApril();
				may+=monthlyManpower.getMay();
				june+=monthlyManpower.getJune();
				july+=monthlyManpower.getJuly();
				august+=monthlyManpower.getAugust();
				september+=monthlyManpower.getAugust();
				october+=monthlyManpower.getOctober();
				november+=monthlyManpower.getNovember();
				december+=monthlyManpower.getDecember();
			}
			sum=january+february+march+april+may+june+july+august+september+october+november+december;
		}
		if(!CollectionUtils.isEmpty(gerenCodes) && CollectionUtils.isEmpty(monthlyManpowers)) {
			Calendar calendar=Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date="";
			List<ParameterValueNew> listPara=new ArrayList<>();
			for (int i = 1; i <=13; i++) {
				ParameterValueNew parameter =new ParameterValueNew();
				parameter.setParameterId(144);
				parameter.setNo(no);
				switch (i) {
				case 1:
					parameter.setValue(januaryCode);
					date=year+"-01-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 2:
					parameter.setValue(februaryCode);
					date=year+"-02-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 3:
					parameter.setValue(marchCode);
					date=year+"-03-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 4:
					parameter.setValue(aprilCode);
					date=year+"-04-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 5:
					parameter.setValue(mayCode);
					date=year+"-05-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 6:
					parameter.setValue(juneCode);
					date=year+"-06-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 7:
					parameter.setValue(julyCode);
					date=year+"-07-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 8:
					parameter.setValue(augustCode);
					date=year+"-08-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 9:
					parameter.setValue(septemberCode);
					date=year+"-09-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 10:
					parameter.setValue(octoberCode);
					date=year+"-10-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 11:
					parameter.setValue(novemberCode);
					date=year+"-11-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 12:
					parameter.setValue(decemberCode);
					date=year+"-12-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 13:
					date=year+"-01-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					parameter.setValue(sumCode);
					parameter.setParameterId(147);
					break;
				default:
					break;
				}
				listPara.add(parameter);
			}
			return listPara;
		}else if (CollectionUtils.isEmpty(gerenCodes) && !CollectionUtils.isEmpty(monthlyManpowers)) {
			Calendar calendar=Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date="";
			List<ParameterValueNew> listPara=new ArrayList<>();
			for (int i = 1; i <=13; i++) {
				ParameterValueNew parameter =new ParameterValueNew();
				parameter.setParameterId(145);
				parameter.setNo(no);
				switch (i) {
				case 1:
					parameter.setValue(january);
					date=year+"-01-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 2:
					parameter.setValue(february);
					date=year+"-02-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 3:
					parameter.setValue(march);
					date=year+"-03-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 4:
					parameter.setValue(april);
					date=year+"-04-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 5:
					parameter.setValue(may);
					date=year+"-05-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 6:
					parameter.setValue(june);
					date=year+"-06-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 7:
					parameter.setValue(july);
					date=year+"-07-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 8:
					parameter.setValue(august);
					date=year+"-08-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 9:
					parameter.setValue(september);
					date=year+"-09-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 10:
					parameter.setValue(october);
					date=year+"-10-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 11:
					parameter.setValue(november);
					date=year+"-11-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 12:
					parameter.setValue(december);
					date=year+"-12-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 13:
					date=year+"-01-01";
					
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					parameter.setValue(sum);
					parameter.setParameterId(148);
				default:
					break;
				}
				listPara.add(parameter);
			}
			return listPara;
		}else if (!CollectionUtils.isEmpty(gerenCodes) && !CollectionUtils.isEmpty(monthlyManpowers)) {
			Calendar calendar=Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date="";
			List<ParameterValueNew> listPara=new ArrayList<>();
			for (int i = 1; i <=13; i++) {
				ParameterValueNew parameter =new ParameterValueNew();
				parameter.setParameterId(146);
				parameter.setNo(no);
				switch (i) {
				case 1:
					parameter.setValue(parseAndCalc(januaryCode,january));
					date=year+"-01-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 2:
					parameter.setValue(parseAndCalc(februaryCode,february));
					date=year+"-02-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 3:
					parameter.setValue(parseAndCalc(marchCode,march));
					date=year+"-03-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 4:
					parameter.setValue(parseAndCalc(aprilCode,april));
					date=year+"-04-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 5:
					parameter.setValue(parseAndCalc(mayCode,may));
					date=year+"-05-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 6:
					parameter.setValue(parseAndCalc(juneCode,june));
					date=year+"-06-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 7:
					parameter.setValue(parseAndCalc(julyCode,july));
					date=year+"-07-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 8:
					parameter.setValue(parseAndCalc(augustCode,august));
					date=year+"-08-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 9:
					parameter.setValue(parseAndCalc(septemberCode,september));
					date=year+"-09-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 10:
					parameter.setValue(parseAndCalc(octoberCode,october));
					date=year+"-10-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 11:
					parameter.setValue(parseAndCalc(novemberCode,november));
					date=year+"-11-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 12:
					parameter.setValue(parseAndCalc(decemberCode,december));
					date=year+"-12-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					break;
				case 13:
					parameter.setValue(parseAndCalc(sumCode,sum));
					date=year+"-01-01";
					try {
						parameter.setMonth(sdf.parse(date));
					} catch (ParseException e) {
						logger.error("日期转换异常"+e.getMessage());
					}
					parameter.setParameterId(149);
				default:
					break;
				}
				listPara.add(parameter);
			}
			return listPara;
		}
		return null;
	}
	
	public Double parseAndCalc(Double code,Double measure) {
		DecimalFormat df = new DecimalFormat("0.00");  //保留两位小数
		Double result=0.0;
		if (measure!=0.0&& measure !=null) {
			try {
				result= Double.parseDouble(df.format(code/measure));
			} catch (NumberFormatException e) {
				logger.error("DOUBLE转化异常！"+e.getMessage());
			}
		}
		return result;
		
	}
	
	/**
	 * 效率看板查询指标数据
	 * @param no
	 * @return
	 */
	public List<ParameterValueNew> queryEfficiency(String no){
		return qualityModelDao.searchParameter(no);
	}
	
	/**
	 * 效率定时计算入库
	 * 每天凌晨两点执行
	 */
//	@Scheduled(cron = "${statistical_Task_scheduled}")
	public void saveStatistical() {
		//获取所有项目列表dx5cx
		
		List<ProjectInfo> projectInfos=projectInfoDao.queryEffectiveProjects();
		List<ParameterValueNew> list =new ArrayList<>();
		ParameterValueNew parameterValueNew =null;
		Calendar calendar=Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString=year+"-"+month+"-01";
		Date date=null;
		DecimalFormat df = new DecimalFormat("0.00");  //保留两位小数
		DecimalFormat decimalFormat = new DecimalFormat("0.000"); //保留三位小数
		try {
			date=sdf.parse(dateString);
		} catch (ParseException e) {
			logger.error("日期转换异常"+e.getMessage());
		}
		for (ProjectInfo projectInfo : projectInfos) {
			//当前人力 150
			Integer nowHuman=qualityModelDao.queryNowHuman(projectInfo.getNo());
			if(null!=nowHuman) {
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(nowHuman);
				parameterValueNew.setParameterId(150);
				list.add(parameterValueNew);
			}
			//累计发现用例个数 151
			Integer accDevCase=configDao.testCaseCount(projectInfo.getNo());
			if(null!=accDevCase) {
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(accDevCase);
				parameterValueNew.setParameterId(151);
				list.add(parameterValueNew);
			}
			//累计发现问题单数 152
			Integer accFindPro = configDao.dtsCount(projectInfo.getNo());
			if (null!=accFindPro) {
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(accFindPro);
				parameterValueNew.setParameterId(152);
				list.add(parameterValueNew);
			}
			//累计开发代码规模 153
//			Integer newloc =configDao.newLoc(projectInfo.getNo());
//			Integer newlocWX=configDao.newLocWx(projectInfo.getNo());
//			if (newloc==null) {
//				newloc=0;
//			}
//			if(newlocWX==null) {
//				newlocWX=0;
//			}
//			Integer accDevCode=newloc+newlocWX;
			Integer accDevCode = logService.queryCodeNum(projectInfo.getNo());
			if (null!=accDevCode) {
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(Double.parseDouble(decimalFormat.format((Double.parseDouble(accDevCode+""))/1000)));
				parameterValueNew.setParameterId(153);
				list.add(parameterValueNew);
			}
			//累计处理问题数 154
			Integer accDealPro=configDao.solvedDtsCount(projectInfo.getNo());
			if(null!=accDealPro) {
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(accDealPro);
				parameterValueNew.setParameterId(154);
				list.add(parameterValueNew);
			}
			
			//累计投入人月 155
			String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
			List<MonthlyManpower> monthlyManpowers=measuresService.getMonthlyManpower(projectInfo.getNo(),"0",months,true);//0-实际工时
			Double accPutHuman=0.0;
			if (!CollectionUtils.isEmpty(monthlyManpowers)) {
				for (MonthlyManpower monthlyManpower : monthlyManpowers) {
					accPutHuman+=monthlyManpower.getSum();
				}
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(accPutHuman);
				parameterValueNew.setParameterId(155);
				list.add(parameterValueNew);
			}
			//代码累计生产率(loc/人天) 156
			if(accDevCode!=null && accPutHuman!=null && accPutHuman!=0.0) {
				String accCodePdt = df.format(accDevCode/(accPutHuman/22.5));
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(Double.parseDouble(accCodePdt));
				parameterValueNew.setParameterId(156);
				list.add(parameterValueNew);
			}
			//用例累计生产率(个/人天) 157
			if(accDevCase!=null && accPutHuman!=null && accPutHuman!=0.0) {
				String accCasePdt = df.format(accDevCase/(accPutHuman/22.5));
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(Double.parseDouble(accCasePdt));
				parameterValueNew.setParameterId(157);
				list.add(parameterValueNew);				
			}
			//测试发现有效问题效率(个/人天) 158
			if(accFindPro!=null && accPutHuman!=null && accPutHuman!=0.0) {
				String testFindEffPro = df.format(accFindPro/(accPutHuman/22.5));
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(Double.parseDouble(testFindEffPro));
				parameterValueNew.setParameterId(158);
				list.add(parameterValueNew);				
			}
			//处理问题效率(个/人天) 159
			if(accDealPro!=null && accPutHuman!=null && accPutHuman!=0.0) {
				String dealProEff = df.format(accDealPro/(accPutHuman/22.5));
				parameterValueNew=new ParameterValueNew();
				parameterValueNew.setNo(projectInfo.getNo());
				parameterValueNew.setMonth(date);
				parameterValueNew.setValue(Double.parseDouble(dealProEff));
				parameterValueNew.setParameterId(159);
				list.add(parameterValueNew);			
			}
		}
		if (!CollectionUtils.isEmpty(list)) {
			projectParameterValueDao.insertParams(list);
		}
	}
}
