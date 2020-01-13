package com.icss.mvp.service;

import com.github.pagehelper.PageHelper;
import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IQmsMaturityDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.io.ExportService;
import com.icss.mvp.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QmsMaturityService {

	private final static Logger LOG = Logger.getLogger(QmsMaturityService.class);

	@Autowired
	private IQmsMaturityDao qmsMaturityDao;
	
	@Resource
	private HttpServletRequest request;
	
	@Autowired
	private ExportService exportService;
	
	@Autowired
	private IProjectInfoDao projectInfoMapper;

	@Autowired
	private ProjectInfoService projectInfoService;

	/**
	 * 查询QMS成熟度列表
	 * 
	 * @param no
	 * @param source
	 * @return
	 */
	public List<Qmsresult> queryQMSresults(String no, String source, String select) {
		String better = null;
		if("问题跟踪".equals(source)) {
			source = null;
			better = "1";
		}else if("质量控制".equals(source)) {
			better = "2";
		}
		List<String> selects = null;
		List<Qmsresult> qmsresults = null;
		try {
			if(!(StringUtils.isBlank(select) || "null".equals(select))) {
				selects = Arrays.asList(URLDecoder.decode(select,"UTF-8").split(","));
			}
			qmsresults = qmsMaturityDao.queryQMSresults(no, source,better,selects);
			for (Qmsresult qmsresult : qmsresults) {
				qmsresult.setNo(no);
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		return qmsresults;
	}

	/**
	 * 查询指定QMS成熟度
	 * 
	 * @param no
	 * @param id
	 * @return
	 */
	public Qmsresult queryQMS(String no, String id) {
		Qmsresult qmsresults = qmsMaturityDao.queryQMS(no, id);
		return qmsresults;
	}

	/**
	 * 修改后保存
	 * 
	 * @param qmsresult
	 */
	public void replaceQMSresult(Qmsresult qmsresult) {
		try {
			if ("Closed".equals(qmsresult.getState())) {
				qmsresult.setActualClosedTime(new Date());
			}
			qmsresult.setModifyTime(new Date());
			qmsMaturityDao.replaceQMSresult(qmsresult);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
	}

	/**
	 * 范围判断
	 * 
	 * @param qmsId
	 * @return
	 */
	public List<Integer> qmsRanges(Integer qmsId) {
		return qmsMaturityDao.qmsRanges(qmsId);
	}

	/** 
	 * 获取项目中每个标签页的符合度
	 * @param no
	 * @param source
	 * @return
	 */
	public Map<String, Object> sumConform(String no,String source) {
		if("问题跟踪".equals(source)) {
			return null;
		}
		String involve = qmsMaturityDao.sumConform(no,source);
		Map<String, Object> map = new HashMap<>();
		String conform = NumberUtil.stringToDouble(involve,6.00);
		map.put("conform", conform);
		return map;
	}
	
	public List<Qmsproblem> qmsProblemType(String source) {
		return qmsMaturityDao.qmsProblemType(source);
	}

	/**
	 * QMS报表
	 */
	public TableSplitResult<List<Qmsproblem>> qmsReport(ProjectInfo projectInfo) {
		TableSplitResult<List<Qmsproblem>> ret = new TableSplitResult<List<Qmsproblem>>();
		List<Qmsproblem> data = new ArrayList<>();
		String source = "";
		Integer order = 0;
		Integer orders = 0;
		try {
			String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
			Set<String> nos = projectInfoService.collectedProjectNos(projectInfo, username);
			List<String> proNos = new ArrayList<>(nos);
			String proNo = "(" + StringUtilsLocal.listToSqlIn(proNos) + ")";
			String type = "3";
			String month = null;
			if(!projectInfo.getMonth().equals(DateUtils.getNowCurrentWeek())) {
				month = projectInfo.getMonth();
				type = "0";
			}
			Integer reports = qmsMaturityDao.getQmsReports(proNo,month,type);
			List<Qmsproblem> result = qmsMaturityDao.qmsReport(proNo,type,month);
			//获取每个维度所占的数量以及项目编号
			for (Qmsproblem qmsproblem : result) {
				if(!source.equals(qmsproblem.getSource())) {
					source = qmsproblem.getSource();
					Qmsproblem qms = new Qmsproblem();
					qms.setTypeLevel("0");
					qms.setOrder(source);
					data.add(qms);
					order += 1;
					orders = 0;
				}
				orders+=1;
				qmsproblem.setOrder(order+"."+orders);
				qmsproblem.setTypeLevel("1");
				qmsproblem.setNumber(qmsMaturityDao.qmsReportNos(proNo,(qmsproblem.getPid()).toString(),type,month));
				qmsproblem.setProportion(NumberUtil.stringDivision(String.valueOf(qmsproblem.getNumber()*100), reports)+"%");
				qmsproblem.setType(type);
				if("3".equals(type) && qmsproblem.getCreationTime() == null) {
					qmsproblem.setCreationTime(new Date());
				}
				data.add(qmsproblem);
			}
			ret.setRows(data);
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		return ret;
	}

	public List<String> multipleMenu(String source) {
		List<String> list = new ArrayList<>();
		try {
			if("问题跟踪".equals(source)) {
				list = qmsMaturityDao.multipleMenuSource(source);
			}else if("质量控制".equals(source)) {
				list = qmsMaturityDao.multipleMenuLabel(source);
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
		return list;
	}

	public List<Map<String, Object>> getSelectQuestion(String source) {
		List<Map<String, Object>> data = qmsMaturityDao.getSelectQuestion(source);
		return data;
	}

	public Map<String, Object> qmsSector(ProjectInfo projectInfo) {
		Map<String, Object> data = new HashMap<>();
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		Set<String> nos = projectInfoService.collectedProjectNos(projectInfo, username);
		List<String> proNos = new ArrayList<>(nos);
		String proNo = "(" + StringUtilsLocal.listToSqlIn(proNos) + ")";
		String month = null;
		String type = "3";
		if(!projectInfo.getMonth().equals(DateUtils.getNowCurrentWeek())) {
			month = projectInfo.getMonth();
			type = "0";
		}
		List<Qmsproblem> list = qmsMaturityDao.qmsSector(proNo,month,type);
		List<String> sources = new ArrayList<>();
		List<Map<String, Object>> values = new ArrayList<>();
		for (Qmsproblem qmsproblem : list) {
			sources.add(qmsproblem.getSource());
			Map<String, Object> map = new HashMap<>();
			map.put("value", qmsproblem.getNumber());
			map.put("name", qmsproblem.getSource());
			values.add(map);
		}
		data.put("sources", sources);
		data.put("values", values);
		return data;
	}

	public Map<String, Object> qmsHistogram(ProjectInfo projectInfo) {
		Map<String, Object> data = new HashMap<>();
		String name = projectInfo.getName();
		projectInfo.setName("");
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		Set<String> nos = projectInfoService.collectedProjectNos(projectInfo, username);
		List<String> proNos = new ArrayList<>(nos);
		String proNo = "(" + StringUtilsLocal.listToSqlIn(proNos) + ")";
		String month = null;
		String type = "3";
		if(!projectInfo.getMonth().equals(DateUtils.getNowCurrentWeek())) {
			month = projectInfo.getMonth();
			type = "0";
		}
		if(name.isEmpty()) {
			name = qmsMaturityDao.qmsSource(proNo,month,type);
		}
		List<String> names = new ArrayList<>();
		List<Integer> value = new ArrayList<>();
		List<Qmsproblem> list = qmsMaturityDao.qmsHistogram(proNo,name,month,type);
		for (Qmsproblem qmsproblem : list) {
			names.add(qmsproblem.getQuestion());
			value.add(qmsproblem.getNumber());
		}
		data.put("name", name);
		data.put("names", names);
		data.put("value", value);
		return data;
	}

	public Map<String, Object> importQMS(MultipartFile file) {
		Map<String, Object> result = new HashMap<String, Object>();
		int sucNum = 0;
		InputStream is = null;
		Workbook workbook = null;
		Row row = null;
		try {
			is = file.getInputStream();
			workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			int rowSize = sheet.getLastRowNum();
			if (rowSize < 1) {
				result.put("STATUS", "FAILED");
				result.put("MESSAGE", "没有要导入的数据！");
				return result;
			}
			List<Qmsresult> list = new ArrayList<>();
			List<String> ass = new ArrayList<>();
			Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
			List<Qmslist>  data = qmsMaturityDao.getQmsidBy();
			List<Qmsproblem>  problems = qmsMaturityDao.getQmsListProblem();
			for (int i = 2; i <= rowSize+2; i++) {
				String dis = getCellFormatValue(ExcelUtils.getCell(map, row, "ID"));
				try {
					row = sheet.getRow(i);
					String name = getCellFormatValue(ExcelUtils.getCell(map, row, "项目名称"));
					if (StringUtils.isEmpty(name)) {
						continue;
					}
					String content = getCellFormatValue(ExcelUtils.getCell(map, row, "主要评估项及内容")).replaceAll("\n", "");
					String problem = getCellFormatValue(ExcelUtils.getCell(map, row, "问题二次细分")).replaceAll("\n", "");
					Qmsresult qms = new Qmsresult();
					for (Qmslist qmslist : data) {
						if(levenshtein(content,qmslist.getContent().replaceAll("\n", "")) > 0.85) {
							qms.setQmsId(qmslist.getId());
							break;
						}
					}
					String no = projectInfoMapper.getNoByName(name);
					if (StringUtils.isEmpty(no) || qms==null) {
						ass.add(dis);
						continue;
					}
					for (Qmsproblem qmsproblem : problems) {
						if(levenshtein(problem,qmsproblem.getQuestion().replaceAll("\n", "")) > 0.85) {
							qms.setProblemType(qmsproblem.getPid().toString());
							break;
						}
					}
					if(qms.getProblemType() == null) {
						ass.add("无类别："+dis);
					}
					qms.setNo(no);
					qms.setEvidence(getCellFormatValue(ExcelUtils.getCell(map, row, "问题描述")));
					qms.setInvolve("否");
					qms.setImprovementMeasure(getCellFormatValue(ExcelUtils.getCell(map, row, "改进措施")));
					qms.setDutyPerson(getCellFormatValue(ExcelUtils.getCell(map, row, "责任人")));
					String planClosedTime = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "计划关闭时间")))?
							getCellFormatValue(ExcelUtils.getCell(map, row, "计划关闭时间")).split("\\.")[0]:null;
					if(StringUtils.isNumeric(planClosedTime)) {
						qms.setPlanClosedTime(DateUtils.transformDate(planClosedTime));
					}
					qms.setState(getCellFormatValue(ExcelUtils.getCell(map, row, "状态")));
					qms.setProgress(getCellFormatValue(ExcelUtils.getCell(map, row, "进展")));
					qms.setModifyTime(new Date());
					sucNum++;
					list.add(qms);
				} catch (Exception e) {
					LOG.error(e);
					ass.add(dis);
				}
			}
			System.out.println(ass);
			qmsMaturityDao.importQMS(list);
		} catch (Exception e) {
			LOG.error(e);
			result.put("STATUS", "FAILED");
			result.put("MESSAGE", "读取文件失败！");
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					LOG.error("read file failed!" + e.getMessage());
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("read file failed!" + e.getMessage());
				}
			}
		}
		result.put("STATUS", "SUCCESS");
		result.put("sucNum", sucNum);
		return result;
	}
	
//	@Scheduled(cron = "${QmsSaveHistorical}")
	public void QmsSaveHistorical() {
		try {
			Date month = DateUtils.SHORT_FORMAT_GENERAL.parse(DateUtils.getCurrentWeek());
			List<Qmsproblem> list = qmsMaturityDao.qmsWholeProject();
			for (Qmsproblem qmsproblem : list) {
				qmsproblem.setType("0");
				qmsproblem.setCreationTime(month);
			}
			if(list.size() > 0) {
				qmsMaturityDao.qmsReportSaves(list);
			}
			List<Qmsresult> data = qmsMaturityDao.queryQMShistorical();
			for (Qmsresult qmsresult : data) {
				qmsresult.setType("0");
				qmsresult.setCreationTime(month);
			}
			if(data.size()>0) {
				qmsMaturityDao.saveQMShistorical(data);
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
	}
	
	private String getCellFormatValue(Cell cell) {
		String strCell = null;
		if (cell == null) {
			return strCell;
		}
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		default:
			strCell = null;
			break;
		}
		return strCell;
	}
	
	/**
    *计算两个字段相似度
    * @param str1
    * @param str2
    */
   public static float levenshtein(String str1,String str2) {
       //计算两个字符串的长度。
       int len1 = str1.length();
       int len2 = str2.length();
       //建立上面说的数组，比字符长度大一个空间
       int[][] dif = new int[len1 + 1][len2 + 1];
       //赋初值，步骤B。
       for (int a = 0; a <= len1; a++) {
           dif[a][0] = a;
       }
       for (int a = 0; a <= len2; a++) {
           dif[0][a] = a;
       }
       //计算两个字符是否一样，计算左上的值
       int temp;
       for (int i = 1; i <= len1; i++) {
           for (int j = 1; j <= len2; j++) {
               if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                   temp = 0;
               } else {
                   temp = 1;
               }
               //取三个值中最小的
               dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                       dif[i - 1][j] + 1);
           }
       }
       //计算相似度
       float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
       return similarity;
   }

   /**
    * 获取最小值
    * @param is
    * @return
    */
   private static int min(int... is) {
       int min = Integer.MAX_VALUE;
       for (int i : is) {
           if (min > i) {
               min = i;
           }
       }
       return min;
   }

public byte[] exportQMS(String date) {
	InputStream is = this.getClass().getResourceAsStream("/excel/export-qmsSummary.xlsx");
	Workbook wb = null;
	try {
		wb = new XSSFWorkbook(is);
	} catch (Exception e) {
		LOG.error("导出失败", e);
	}
	Sheet sheet = wb.getSheetAt(0);
	Row row;
	CellStyle cellStyle = exportService.getCellStyle(wb);
	SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
	/************************ 获取表格头 ***************************/
	int num = 2;
	/********************* 获取项目表格信息 ************************/
	String month = null;
	String type = "3";
	if(!date.equals(DateUtils.getNowCurrentWeek())) {
		month = date;
		type = "0";
	}
	List<Qmsresult> list = qmsMaturityDao.exportQMS(type,month);
	if (list == null || list.size() <= 0) {
		return exportService.returnWb2Byte(wb);
	}
	for (Qmsresult qms : list) {
		row = sheet.createRow(num);
		exportService.setCellValue(0, String.valueOf(num-1), row, cellStyle);
		exportService.setCellValue(1, qms.getName(), row, cellStyle);
		exportService.setCellValue(2, qms.getArea(), row, cellStyle);
		exportService.setCellValue(3, qms.getSpdt(), row, cellStyle);
		exportService.setCellValue(4, qms.getPm(), row, cellStyle);
		exportService.setCellValue(5, qms.getQa() == null ? "" : qms.getQa(), row, cellStyle);
		exportService.setCellValue(6, qms.getType(), row, cellStyle);
		exportService.setCellValue(7, qms.getSource(), row, cellStyle);
		exportService.setCellValue(8, qms.getContent(), row, cellStyle);
		exportService.setCellValue(9, qms.getEvidence(), row, cellStyle);
		exportService.setCellValue(10, qms.getProblemType(), row, cellStyle);
		exportService.setCellValue(11, qms.getMajorProblem() == null ? "" : qms.getMajorProblem(), row, cellStyle);
		exportService.setCellValue(12, qms.getImprovementMeasure() == null ? "" : qms.getImprovementMeasure(), row, cellStyle);
		exportService.setCellValue(13, qms.getDutyPerson() == null ? "" : qms.getDutyPerson(), row, cellStyle);
		exportService.setCellValue(14, qms.getPlanClosedTime() == null ? "" : dt.format(qms.getPlanClosedTime()), row, cellStyle);
		exportService.setCellValue(15, qms.getState() == null ? "" : qms.getState(), row, cellStyle);
		exportService.setCellValue(16, qms.getProgress() == null ? "" : qms.getProgress(), row, cellStyle);
		num++;
	}
	return exportService.returnWb2Byte(wb);
}

public TableSplitResult<List<QualityMonthlyReport>> qmsDimension(ProjectInfo projectInfo, PageRequest pageRequest) {
	TableSplitResult<List<QualityMonthlyReport>> ret = new TableSplitResult<>();
	try {
		Set<String> nos = projectInfoService.projectNos(projectInfo, null);
		List<String> proNos = new ArrayList<>(nos);
		String proNo = "(" + StringUtilsLocal.listToSqlIn(proNos) + ")";
		String type = "3";
		String month = null;
		if(!projectInfo.getMonth().equals(DateUtils.getNowCurrentWeek())) {
			month = projectInfo.getMonth();
			type = "0";
		}
		String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
		//openAAR：项目管理，closedAAR：质量策划，delayAAR：质量控制，addedAAR：流程要求，sumAAR：知识管理,comment收藏
		com.github.pagehelper.Page page = PageHelper.startPage((pageRequest.getPageNumber() == null ? 0 : (pageRequest.getPageNumber() - 1)) + 1,pageRequest.getPageSize());
		List<QualityMonthlyReport> list = qmsMaturityDao.qmsDimension(proNo,type,month,username,projectInfo.getCoopType());
		ret.setTotal((int) page.getTotal());
		for (QualityMonthlyReport qms : list) {
			if("0".equals(projectInfo.getClientType())) {
				qms.setDepartment(qms.getHwzpdu()+"/"+qms.getPduSpdt());
			}else if("1".equals(projectInfo.getClientType())) {
				qms.setDepartment(qms.getDu()+"/"+qms.getPdu());
			}
			Date startTime = qms.getStartDate();
			Date endTime = qms.getEndDate();
			Date date = DateUtils.SHORT_FORMAT_GENERAL.parse(DateUtils.getThisClcyeStart(projectInfo.getMonth()));
			if(endTime.after(date)) {
				endTime = DateUtils.SHORT_FORMAT_GENERAL.parse(projectInfo.getMonth());
			}
			qms.setStatisticalCycle(DateUtils.StatisticalCycle(startTime, endTime));
			Integer fou = qms.getOpenAAR()+qms.getClosedAAR()+qms.getDelayAAR()+qms.getAddedAAR()+qms.getSumAAR();
			qms.setDemandProgress(NumberUtil.stringDivision(String.valueOf(qms.getQuality()*100), fou)+"%");
			qms.setPeopleLamp(NumberUtil.stringToDouble(qms.getPeopleLamp() == null ? "0":qms.getPeopleLamp(),6.00));
			qms.setPlanLamp(NumberUtil.stringToDouble(qms.getPlanLamp() == null ? "0":qms.getPlanLamp(),6.00));
			qms.setScopeLamp(NumberUtil.stringToDouble(qms.getScopeLamp() == null ? "0":qms.getScopeLamp(),6.00));
			qms.setQualityLamp(NumberUtil.stringToDouble(qms.getQualityLamp() == null ? "0":qms.getQualityLamp(),6.00));
			qms.setKeyRoles(NumberUtil.stringToDouble(qms.getKeyRoles() == null ? "0":qms.getKeyRoles(),6.00));
			qms.setKeyRolesPass(NumberUtil.stringToDouble(qms.getKeyRolesPass() == null ? "0":qms.getKeyRolesPass(),6.00));
		}
		ret.setRows(list);
	} catch (Exception e) {
		LOG.error("查询失败"+e.getMessage());
	}
	return ret;
}

public List<QualityMonthlyReport> qmsSelfchecking(String month) {
	List<QualityMonthlyReport> list = qmsMaturityDao.qmsSelfchecking(month);
	//peopleLamp:汇总,planLamp:项目管理,scopeLamp:质量策划,qualityLamp:质量控制,keyRoles:流程要求,keyRolesPass:知识管理
	for (QualityMonthlyReport qms : list) {
		qms.setPeopleLamp(NumberUtil.stringToDouble(qms.getPeopleLamp() == null ? "0":qms.getPeopleLamp(),6.00));
		qms.setPlanLamp(NumberUtil.stringToDouble(qms.getPlanLamp() == null ? "0":qms.getPlanLamp(),6.00));
		qms.setScopeLamp(NumberUtil.stringToDouble(qms.getScopeLamp() == null ? "0":qms.getScopeLamp(),6.00));
		qms.setQualityLamp(NumberUtil.stringToDouble(qms.getQualityLamp() == null ? "0":qms.getQualityLamp(),6.00));
		qms.setKeyRoles(NumberUtil.stringToDouble(qms.getKeyRoles() == null ? "0":qms.getKeyRoles(),6.00));
		qms.setKeyRolesPass(NumberUtil.stringToDouble(qms.getKeyRolesPass() == null ? "0":qms.getKeyRolesPass(),6.00));
	}
	return list;
}
}
