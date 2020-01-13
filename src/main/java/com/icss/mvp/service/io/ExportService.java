package com.icss.mvp.service.io;

import com.icss.mvp.dao.*;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.CommonService;
import com.icss.mvp.service.GeneralSituationService;
import com.icss.mvp.service.NetworkSecurityService;
import com.icss.mvp.service.ProjectOverviewService;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by up on 2018/10/9.
 */
@Service("ExportService")
public class ExportService extends CommonService {

	private static Logger logger = Logger.getLogger(ExportService.class);
	@Autowired
	private ProjectInfoVoDao projectInfoVoDao;
	@Autowired
	private IterationCycleDao iterationCycleDao;
	@Autowired
	private IQmsMaturityDao qmsMaturityDao;
	@Autowired
	private IterationMeasureIndexDao iterationMeasureIndexDao;
	@Autowired
	private IQualityModelDao qualityModelDao;
	@Autowired
	private ISvnTaskDao svnTaskDao;
	@Autowired
	private ILowLocDao lowLocDao;
	@Autowired
	private ProjectLableDao ProjectLableDao;
	@Autowired
	private ProjectOverviewService projectOverviewService;
	@Autowired
	private IProjectOverviewDao projectOverviewDao;
	@Autowired
	private NetworkSecurityService networkSecurityService;
	
	@Autowired 
	private GeneralSituationService generalSituationService;
	
	@SuppressWarnings("deprecation")
	public byte[] exportExcel(String no) {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("指标参数表");

		// 表头样式
		CellStyle headerStyle = getHeadStyle(wb);
		CellStyle cellStyle = getCellStyle(wb);

		int border = 1;

		Row row;
		Row row1;
		Row row2;
		Row row3;
		Row row4;
		Row row5;
		Cell cell;
		// CellStyle cellStyle = wb.createCellStyle();
		// CreationHelper creationHelper = wb.getCreationHelper();
		// cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(
		// "yyyy-MM-dd"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		/********************** 获取头信息 ************************************/
		List<ProjectLabelMeasure> list = ProjectLableDao.getMeasureRang(no);// 只读导出
		// String category = dao.getProjectConfigCategory(no);
		// List<ProjectLabelMeasure> list =
		// projectLableService.getMeasureConfigPageInfo(category, no, "", "");导出时进行保存

		row = sheet.createRow(0);
		row1 = sheet.createRow(1);
		row2 = sheet.createRow(2);
		row3 = sheet.createRow(3);
		row4 = sheet.createRow(4);
		row5 = sheet.createRow(5);
		List<String> measureIds = new ArrayList<>();
		int num = 0;
		ProjectInfoVo infoVo = projectInfoVoDao.fetchProjectInfoByKey(no);
		cell = row.createCell(num);
		cell.setCellValue(infoVo.getName());
		cell.setCellStyle(headerStyle);

		sheet.autoSizeColumn(num);
		sheet.setColumnWidth(num, sheet.getColumnWidth(num) * 2);

		Cell cell1 = row1.createCell(num);
		cell1.setCellValue("类目");
		cell1.setCellStyle(headerStyle);
		Cell cell2 = row2.createCell(num);
		cell2.setCellValue("指标");
		cell2.setCellStyle(headerStyle);
		Cell upper = row3.createCell(num);
		upper.setCellValue("上限值");
		upper.setCellStyle(headerStyle);
		Cell lower = row4.createCell(num);
		lower.setCellValue("下限值");
		lower.setCellStyle(headerStyle);
		Cell target = row5.createCell(num);
		target.setCellValue("目标值");
		target.setCellStyle(headerStyle);
		num++;
		for (int i = 0; i < list.size(); i++) {
			/*
			 * if("0".equals(list.get(i).getIsSelect())){ continue; }
			 */
			int newNum0 = num;
			cell = row.createCell(num);
			cell.setCellValue(list.get(i).getLabelName());
			cell.setCellStyle(headerStyle);

			List<ShowMeasureCat> showMeasureCats = list.get(i).getMeasureCatList();
			for (int j = 0; j < showMeasureCats.size(); j++) {
				/*
				 * if("0".equals(showMeasureCats.get(j).getIsSelect())){ continue; }
				 */
				int newNum = num;
				cell1 = row1.createCell(num);
				cell1.setCellValue(showMeasureCats.get(j).getMeasureCategory());
				cell1.setCellStyle(headerStyle);

				List<ShowMeasure> showMeasures = showMeasureCats.get(j).getMeasureList();
				for (int k = 0; k < showMeasures.size(); k++) {
					/*
					 * if("0".equals(showMeasures.get(k).getIsSelect())){ continue; }
					 */
					cell2 = row2.createCell(num);
					cell2.setCellValue(showMeasures.get(k).getMeasureName());
					cell2.setCellStyle(headerStyle);

					sheet.autoSizeColumn(num);
					sheet.setColumnWidth(num, sheet.getColumnWidth(num) * 2);

					upper = row3.createCell(num);
					upper.setCellValue(showMeasures.get(k).getUpper());
					upper.setCellStyle(headerStyle);
					lower = row4.createCell(num);
					lower.setCellValue(showMeasures.get(k).getLower());
					lower.setCellStyle(headerStyle);
					target = row5.createCell(num);
					target.setCellValue(showMeasures.get(k).getTarget());
					target.setCellStyle(headerStyle);

					measureIds.add(showMeasures.get(k).getMeasureId() + "");
					num++;
				}

				if (newNum == num - 1) {
					continue;
				}
				CellRangeAddress region = new CellRangeAddress(1, 1, (short) newNum, (short) num - 1);
				sheet.addMergedRegion(region);

				RegionUtil.setBorderBottom(border, region, sheet, wb);
				RegionUtil.setBorderLeft(border, region, sheet, wb);
				RegionUtil.setBorderRight(border, region, sheet, wb);
				RegionUtil.setBorderTop(border, region, sheet, wb);
			}
			if (newNum0 == num - 1) {
				continue;
			}
			CellRangeAddress region = new CellRangeAddress(0, 0, (short) newNum0, (short) num - 1);
			sheet.addMergedRegion(region);
			RegionUtil.setBorderBottom(border, region, sheet, wb);
			RegionUtil.setBorderLeft(border, region, sheet, wb);
			RegionUtil.setBorderRight(border, region, sheet, wb);
			RegionUtil.setBorderTop(border, region, sheet, wb);
		}
		/*******************************************************************/

		int lenNum = 6;
		/********************** 获取指标信息 **********************************/
		List<IterationCycle> iterationCycles = iterationCycleDao.iterationList(no);

		for (IterationCycle iterationCycle : iterationCycles) {
			Map<String, String> iterationIds = new HashMap<>();
			List<IterationMeasureIndex> values = iterationMeasureIndexDao.measureIndexValues(iterationCycle.getId(),
					measureIds, format.format(iterationCycle.getStartDate()),
					format.format(iterationCycle.getEndDate()), iterationCycle.getProNo());
			for (IterationMeasureIndex iterationMeasureIndex : values) {
				iterationIds.put(iterationMeasureIndex.getMeasureId(), iterationMeasureIndex.getValue());
			}

			row = sheet.createRow(lenNum);
			cell = row.createCell(0);
			cell.setCellValue(new StringBuilder(iterationCycle.getIteName()).append('(')
					.append(DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getStartDate())).append("~")
					.append(DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getEndDate())).append(')')
					.toString());
			cell.setCellStyle(cellStyle);
			lenNum++;
			for (int i = 0; i < measureIds.size(); i++) {
				cell = row.createCell(i + 1);
				try {
					String val = iterationIds.get(measureIds.get(i));
					cell.setCellValue(StringUtilsLocal.isBlank(val) ? "0.0" : val);
				} catch (Exception e) {
					cell.setCellValue("数据异常");
				}
				cell.setCellStyle(cellStyle);
			}
		}
		/*******************************************************************/

		// 冻结第一列
		sheet.createFreezePane(1, 0, 1, 0);

		return returnWb2Byte(wb);
	}

	// -----------------------------------------"部分项目指标采集"----------------------------------------------------------
	@SuppressWarnings("deprecation")
	public byte[] downloadIndex() {
		InputStream is = this.getClass().getResourceAsStream("/excel/export-index.xlsx");
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
		Sheet sheet = wb.getSheetAt(0);
		Row row;
		Cell cell;
		CellStyle cellStyle = getCellStyle(wb);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/************************ 获取表格头 ***************************/
		int num = 3;
		/********************* 获取项目表格信息 ************************/
		ProjectInfo proInfo = new ProjectInfo();
		proInfo.setProjectState("在行");
		PageRequest pageRequest = new PageRequest();
		List<ProjectInfo> projectInfos = qualityModelDao.queryProject(proInfo, pageRequest);// 先根据条件查询出项目信息
		if (projectInfos == null || projectInfos.size() <= 0) {
			return returnWb2Byte(wb);
		}

		for (ProjectInfo projectInfo : projectInfos) {
			int newNum = num;
			row = sheet.createRow(num);
			/*
			 * cell = row.createCell(0); cell.setCellValue(projectInfo.getBu());
			 * cell.setCellStyle(cellStyle); cell = row.createCell(1);
			 * cell.setCellValue(projectInfo.getPdu()); cell.setCellStyle(cellStyle); cell =
			 * row.createCell(2); cell.setCellValue(projectInfo.getHwpdu());
			 * cell.setCellStyle(cellStyle); cell = row.createCell(3);
			 * cell.setCellValue(projectInfo.getHwzpdu()); cell.setCellStyle(cellStyle);
			 * cell = row.createCell(4); cell.setCellValue(projectInfo.getPduSpdt());
			 * cell.setCellStyle(cellStyle); cell = row.createCell(5);
			 * cell.setCellValue(projectInfo.getDu()); cell.setCellStyle(cellStyle); cell =
			 * row.createCell(6); cell.setCellValue(projectInfo.getName());
			 * cell.setCellStyle(cellStyle);
			 */
			setCellValue(0, projectInfo.getBu(), row, cellStyle);
			setCellValue(1, projectInfo.getPdu(), row, cellStyle);
			setCellValue(2, projectInfo.getHwpdu(), row, cellStyle);
			setCellValue(3, projectInfo.getHwzpdu(), row, cellStyle);
			setCellValue(4, projectInfo.getPduSpdt(), row, cellStyle);
			setCellValue(5, projectInfo.getDu(), row, cellStyle);
			setCellValue(6, projectInfo.getName(), row, cellStyle);

			String no = projectInfo.getNo();
			List<String> measureIds = Arrays.asList("125", "126", "127", "128", "129", "130", "131", "132", "133",
					"134", "135", "136", "137", "138", "139", "140", "141", "142", "115", "116", "117", "118", "120",
					"121", "122", "123", "124", "260", "261", "262", "263", "264", "265", "165", "166", "167", "168",
					"169", "170", "171", "172", "173", "174", "175", "176", "177", "143", "144", "145", "146", "147",
					"148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161",
					"162", "163", "164", "195", "196", "197", "198", "199", "200", "201", "202", "203", "204", "205",
					"206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219",
					"220", "221", "254", "255", "256", "257", "258", "259", "178", "179", "180", "181", "182", "183",
					"184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "266", "267");

			/********************** 获取指标信息 **********************************/
			List<IterationCycle> iterationCycles = iterationCycleDao.iterationList(no);

			if (iterationCycles.size() <= 0) {
				num++;
			}
			int j = 0;
			for (IterationCycle iterationCycle : iterationCycles) {
				if (j > 0) {
					row = sheet.createRow(num);
				}
				j++;
				Map<String, String> iterationIds = new HashMap<>();
				List<IterationMeasureIndex> values = iterationMeasureIndexDao.measureIndexValues(iterationCycle.getId(),
						measureIds, format.format(iterationCycle.getStartDate()),
						format.format(iterationCycle.getEndDate()), iterationCycle.getProNo());
				for (IterationMeasureIndex iterationMeasureIndex : values) {
					iterationIds.put(iterationMeasureIndex.getMeasureId(), iterationMeasureIndex.getValue());
				}

				cell = row.createCell(7);
				cell.setCellValue(new StringBuilder(iterationCycle.getIteName()).append('(')
						.append(DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getStartDate())).append("~")
						.append(DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getEndDate())).append(')')
						.toString());
				cell.setCellStyle(cellStyle);
				for (int i = 0; i < measureIds.size(); i++) {
					cell = row.createCell(i + 8);
					try {
						String val = iterationIds.get(measureIds.get(i));
						cell.setCellValue(StringUtilsLocal.isBlank(val) ? "0.0" : val);
					} catch (Exception e) {
						cell.setCellValue("数据异常");
					}
					cell.setCellStyle(cellStyle);
				}
				num++;
			}
			if (newNum == num - 1) {
				continue;
			}
			for (int i = 0; i < 7; i++) {
				CellRangeAddress region = new CellRangeAddress(newNum, num - 1, (short) i, (short) i);
				sheet.addMergedRegion(region);
				RegionUtil.setBorderBottom(1, region, sheet, wb);
				RegionUtil.setBorderLeft(1, region, sheet, wb);
				RegionUtil.setBorderRight(1, region, sheet, wb);
				RegionUtil.setBorderTop(1, region, sheet, wb);
			}
		}
		/*
		 * //冻结 sheet.createFreezePane( 1, 0, 1, 0 );
		 */
		return returnWb2Byte(wb);
	}

	// 项目周报---------------------------------------------------------------------------------------------------------------
	public byte[] downloadWeekly() {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("项目周报");

		CellStyle headerStyle = getHeadStyle(wb);
		CellStyle cellStyle = getCellStyle(wb);
		CellStyle cellStyleRed = getCellStyleRed(wb);

		/************************ 生成表格头 ***************************/
		int num = 0;
		Date newtime = new Date();
		Row row = sheet.createRow(num);

		setCellValue(0, "项目名称", row, headerStyle);
		setCellValue(1, "地域", row, headerStyle);
		setCellValue(2, "华为产品线", row, headerStyle);
		setCellValue(3, "子产品线", row, headerStyle);
		setCellValue(4, "PDU/SPDU", row, headerStyle);
		setCellValue(5, "业务线", row, headerStyle);
		setCellValue(6, "事业部", row, headerStyle);
		setCellValue(7, "交付部", row, headerStyle);
		setCellValue(8, "合作方经理", row, headerStyle);
		setCellValue(9, "合作方QA", row, headerStyle);
		setCellValue(10, "代码采集是否完成(是/否)", row, headerStyle);
		setCellValue(11, "tmss数据采集是否完成(是/否)", row, headerStyle);
		setCellValue(12, "dts数据采集是否完成(是/否)", row, headerStyle);
		setCellValue(13, "ICP-CI是否完成采集(是/否)", row, headerStyle);
		setCellValue(14, "代码检视(smartide)是否完成采集(是/否)", row, headerStyle);
		setCellValue(15, "是否完成月度低产出分析(是/否)", row, headerStyle);

		sheet.autoSizeColumn(0);
		sheet.setColumnWidth(0, sheet.getColumnWidth(0) * 3);
		for (int i = 1; i <= 15; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
		}

		num++;
		/************************************************************/

		/********************* 获取项目表格信息 ************************/
		ProjectInfo proInfo = new ProjectInfo();
		proInfo.setProjectState("在行");
		PageRequest pageRequest = new PageRequest();
		List<ProjectInfo> projectInfos = qualityModelDao.queryProject(proInfo, pageRequest);// 先根据条件查询出项目信息
		if (projectInfos == null || projectInfos.size() <= 0) {
			return returnWb2Byte(wb);
		}

		for (ProjectInfo projectInfo : projectInfos) {
			row = sheet.createRow(num);
			setCellValue(0, projectInfo.getName(), row, cellStyle);
			setCellValue(1, projectInfo.getArea(), row, cellStyle);
			setCellValue(2, projectInfo.getHwpdu(), row, cellStyle);
			setCellValue(3, projectInfo.getHwzpdu(), row, cellStyle);
			setCellValue(4, projectInfo.getPduSpdt(), row, cellStyle);
			setCellValue(5, projectInfo.getBu(), row, cellStyle);
			setCellValue(6, projectInfo.getPdu(), row, cellStyle);
			setCellValue(7, projectInfo.getDu(), row, cellStyle);
			setCellValue(8, projectInfo.getPm(), row, cellStyle);
			setCellValue(9, projectInfo.getQa(), row, cellStyle);

			// 采集最后时间判断是否采集
			List<Map<String, Object>> listmap = svnTaskDao.getLastTimeGroupType(projectInfo.getNo());
			setCellValue(10, "否", row, cellStyleRed);
			setCellValue(11, "否", row, cellStyleRed);
			setCellValue(12, "否", row, cellStyleRed);
			setCellValue(13, "否", row, cellStyleRed);
			setCellValue(14, "否", row, cellStyleRed);
			setCellValue(15, "否", row, cellStyleRed);
			boolean svnAndGit = false;
			for (Map<String, Object> map : listmap) {
				String type = StringUtilsLocal.valueOf(map.get("type"));
				Date date = (Date) map.get("lasttime");
				int days = DateUtils.betweenDays(newtime, date);
				// 对接平台类型1:DTS，2:SVN，3:Git，4:TMSS，5:ci，6:iso
				String val = "否";
				if (days <= 30 && days >= 0) {
					val = "是";
				}
				if ("1".equals(type)) {
					if ("否".equals(val)) {
						setCellValue(12, val, row, cellStyleRed);
					} else {
						setCellValue(12, val, row, cellStyle);
					}
				} else if ("2".equals(type) || "3".equals(type)) {
					if (svnAndGit) {
						continue;
					}
					if (days <= 30 && days >= 0) {
						svnAndGit = true;
					}
				} else if ("4".equals(type)) {
					if ("否".equals(val)) {
						setCellValue(11, val, row, cellStyleRed);
					} else {
						setCellValue(11, val, row, cellStyle);
					}
				} else if ("5".equals(type)) {
					if ("否".equals(val)) {
						setCellValue(13, val, row, cellStyleRed);
					} else {
						setCellValue(13, val, row, cellStyle);
					}
				} else if ("6".equals(type)) {
					if ("否".equals(val)) {
						setCellValue(14, val, row, cellStyleRed);
					} else {
						setCellValue(14, val, row, cellStyle);
					}
				}

			}
			setCellValue(10, svnAndGit ? "是" : "否", row, svnAndGit ? cellStyle : cellStyleRed);

			// 低产出分析
			List<Map<String, Object>> listMap = lowLocDao.queryCountMonthByNo(DateUtils.format2.format(newtime),
					projectInfo.getNo());
			String val = "否";
			if (listMap != null && listMap.size() > 0) {
				int lowLocNum = Integer.valueOf(StringUtilsLocal.valueOf(listMap.get(0).get("num")));
				if (lowLocNum > 0) {
					val = "是";
				}
			}
			if ("否".equals(val)) {
				setCellValue(15, val, row, cellStyleRed);
			} else {
				setCellValue(15, val, row, cellStyle);
			}

			for (int i = 1; i <= 7; i++) {
				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
			}
			num++;
		}

		/************************************************************/

		return returnWb2Byte(wb);

	}

	public void setCellValue(int i, String value, Row row, CellStyle cellStyle) {
		Cell cell = row.createCell(i);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
	}

	public byte[] exportKX(ProjectInfo projectInfo, PageRequest pageRequest, String date) {
		InputStream is = this.getClass().getResourceAsStream("/excel/trusted_metrics_report.xlsx");
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
//		List<Map<String, Object>> result = projectOverviewService
//				.queryProjectReliableQuality(projectInfo, pageRequest, date).getRows();
		List<Map<String, Object>> result = projectOverviewService
				.queryProjectQualityReport(projectInfo, pageRequest, date, "164",null,null,false).getRows();
		if (result == null || result.size() == 0) {
			return returnWb2Byte(wb);
		}
		for (Map<String, Object> map : result) {
			Map<String, Object> proInfo = projectOverviewDao.queryProInfo(String.valueOf(map.get("proNo")));
			List<Map<String, Object>> queryKeyRoles = projectOverviewDao
					.queryKeyRoles(String.valueOf(map.get("proNo")));
			if (proInfo != null) {
				map.put("BU", proInfo.get("BU"));
				map.put("PDU", proInfo.get("PDU"));
				String department=String.valueOf(map.get("department"));
				map.put("DU", department.lastIndexOf("/")!=-1?department.substring(department.lastIndexOf("/")+1):department);
				map.put("AREA", proInfo.get("AREA"));
				map.put("TYPE", proInfo.get("TYPE"));
				map.put("PMname", proInfo.get("PM"));
				map.put("PMaccount", proInfo.get("HR_ACCOUNT"));
			}
			if (queryKeyRoles != null && queryKeyRoles.size() > 0) {
				for (Map<String, Object> m : queryKeyRoles) {
//					if (m.get("POSITION").equals("PM")) {
//						map.put("PMname",
//								map.get("PMname") == null ? m.get("NAME") : map.get("PMname") + "," + m.get("NAME"));
//						map.put("PMaccount", map.get("PMaccount") == null ? m.get("HW_ACCOUNT")
//								: map.get("PMaccount") + "," + m.get("HW_ACCOUNT"));
//					} else 
					if ("QA".equals(m.get("POSITION"))) {
						map.put("QAname",
								map.get("QAname") == null ? m.get("NAME") : map.get("QAname") + "," + m.get("NAME"));
						map.put("QAaccount", map.get("QAaccount") == null ? m.get("HW_ACCOUNT")
								: map.get("QAaccount") + "," + m.get("HW_ACCOUNT"));
					}
				}
			}
		}
		List<Integer> kxVersion=projectOverviewDao.getKXversion();
		CellStyle cellStyle = getCellStyle(wb);
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> map = result.get(i);
			row = sheet.createRow(i + 3);
			row.setHeightInPoints((float) 30.0);
			setValue(row, 0, map.get("BU"), cellStyle);
			setValue(row, 1, map.get("PDU"), cellStyle);
			setValue(row, 2, map.get("DU"), cellStyle);
			setValue(row, 3, map.get("name"), cellStyle);
			setValue(row, 4, map.get("AREA"), cellStyle);
			setValue(row, 5, map.get("PMname"), cellStyle);
			setValue(row, 6, map.get("PMaccount"), cellStyle);
			setValue(row, 7, map.get("QAname"), cellStyle);
			setValue(row, 8, map.get("QAaccount"), cellStyle);
			setValue(row, 9, map.get("TYPE"), cellStyle);
			setValue(row, 10, null, cellStyle);
			for(int n=0;n<kxVersion.size();n++) {
				int ver=kxVersion.get(n);
				String val = null;
				if (map.get("id" + String.valueOf(ver)) == null || "".equals(map.get("id" + String.valueOf(ver)))) {
					val = "-";
				} else {
					Double temp = Double.valueOf(String.valueOf(map.get("id" + String.valueOf(ver))));
					val = temp % 1 == 0.0 ? String.valueOf(new BigDecimal(temp))
							: String.valueOf(StringUtilsLocal.keepTwoDecimals(temp));
				}
				setValue(row, n + 11, val, cellStyle);
			}
		}
		return returnWb2Byte(wb);
	}

	public byte[] exportNetSecReport(NetworkSecurity netSec, String date, String type) {
		InputStream is = this.getClass().getResourceAsStream("/excel/network_security_report.xlsx");
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
		List<NetworkSecurity> result = networkSecurityService.queryNetworkSecurityList(netSec, date, type);

		if (result == null || result.size() == 0) {
			return returnWb2Byte(wb);
		}

		CellStyle cellStyle = getCellStyle(wb);
		Sheet sheet = wb.getSheetAt(0);
		Row row = null;
		for (int i = 0, size = result.size(); i < size; i++) {
			NetworkSecurity networkSecurity = result.get(i);
			row = sheet.createRow(i + 1);
			row.setHeightInPoints((float) 30.0);
			setValue(row, 0, networkSecurity.getHwpdu(), cellStyle);
			setValue(row, 1, networkSecurity.getHwzpdu(), cellStyle);
			setValue(row, 2, networkSecurity.getPduSpdt(), cellStyle);
			setValue(row, 3, networkSecurity.getBu(), cellStyle);
			setValue(row, 4, networkSecurity.getPdu(), cellStyle);
			setValue(row, 5, networkSecurity.getDu(), cellStyle);
			setValue(row, 6, networkSecurity.getName(), cellStyle);
			setValue(row, 7, date.substring(0,7), cellStyle);
			setValue(row, 8, networkSecurity.getProjectType(), cellStyle);
			setValue(row, 9, networkSecurity.getType(), cellStyle);
			setValue(row, 10, networkSecurity.getPm(), cellStyle);
			setValue(row, 11, networkSecurity.getQa(), cellStyle);
			Integer status = networkSecurity.getSecurityStatus();
			if(null != status) {
				setValue(row, 12, status==2?"是":"否", cellStyle);
				setValue(row, 13, networkSecurity.getDeliverStatus()==2?"是":"否", cellStyle);
				setValue(row, 14, networkSecurity.getUkStatus()==2?"是":"否", cellStyle);
				String sowText = "不涉及";
				switch (networkSecurity.getSowStatus()) {
				case 2:
					sowText = "暂未明确";
					break;
				case 3:
					sowText = "确认中";
					break;
				case 4:
					sowText = "已明确";
					break;
				default:
					break;
				}
				setValue(row, 15, sowText, cellStyle);
				setValue(row, 16, networkSecurity.getErrorCleared(), cellStyle);
				setValue(row, 17, networkSecurity.getWarningCleared(), cellStyle);
				setValue(row, 18, networkSecurity.getSatisfyRate(), cellStyle);
				String caseRate = networkSecurity.getCaseRate();
				if(networkSecurity.getCaseStatus()==1) {
					caseRate = "不涉及";
				}
				setValue(row, 19, caseRate, cellStyle);
				setValue(row, 20, networkSecurity.getDangerStatus()==2?"是":"否", cellStyle);
				setValue(row, 21, networkSecurity.getDangerReport(), cellStyle);	
			}	
		}
		return returnWb2Byte(wb);
	}
		
	private String setValue(Row row, int n, Object value, CellStyle cellStyle) {
		Cell cell = row.createCell(n);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value == null || "".equals(value) ? "-" : String.valueOf(value));
		return cell.getStringCellValue();
	}

	/**
	 * 表头样式
	 * 
	 * @param wb
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public CellStyle getHeadStyle(Workbook wb) {
		// 表头样式
		CellStyle headerStyle = wb.createCellStyle();
		// 水平居中
		headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置边框
		headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		// 设置颜色
		headerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(headerFont);
		return headerStyle;
	}

	/**
	 * 单元格样式绿色
	 * 
	 * @param wb
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public CellStyle getCellStyleGreenTitle(Workbook wb) {
		// 单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置边框
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		// 设置自动换行
		cellStyle.setWrapText(true);
		// 设置颜色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置字体
		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 12);
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(cellFont);
		return cellStyle;
	}
	
	/**
	 * 单元格样式背景色
	 * 
	 * @param wb
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public CellStyle getCellStyleColor(Workbook wb,String color) {
		// 单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置边框
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		// 设置自动换行
		cellStyle.setWrapText(true);
		// 设置颜色
		switch(color){
		case "":cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index); break;
		case "red":cellStyle.setFillForegroundColor(HSSFColor.RED.index); break;
		case "green":cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index); break;
		case "yellow":cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index); break;
		}
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置字体
		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 12);
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(cellFont);
		return cellStyle;
	}
	
	/**
	 * 单元格样式
	 * 
	 * @param wb
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public CellStyle getCellStyle(Workbook wb) {
		// 单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置边框
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		// 设置自动换行
		cellStyle.setWrapText(true);
		// 设置字体
		Font cellFont = wb.createFont();
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(cellFont);
		return cellStyle;
	}

	/**
	 * 单元格样式红色
	 * 
	 * @param wb
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public CellStyle getCellStyleRed(Workbook wb) {
		// 单元格样式
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置边框
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		// 设置自动换行
		cellStyle.setWrapText(true);
		// 设置字体
		Font cellFont = wb.createFont();
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellFont.setColor(HSSFColor.RED.index);
		cellStyle.setFont(cellFont);
		return cellStyle;
	}

	public byte[] returnWb2Byte(Workbook wb) {
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			try {
				wb.write(os);
			} catch (IOException e) {
				logger.error("export excel failed!", e);
			}
			return os.toByteArray();
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					logger.error("read file failed!" + e.getMessage());
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("Output file failed!" + e.getMessage());
				}
			}
		}
	}

	public byte[] downloadProblemQMS(String no) {
		InputStream is = this.getClass().getResourceAsStream("/excel/export-qms.xlsx");
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);
		} catch (Exception e) {
			logger.error("导出失败", e);
		}
		Sheet sheet = wb.getSheetAt(0);
		Row row;
		CellStyle cellStyle = getCellStyle(wb);
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		/************************ 获取表格头 ***************************/
		int num = 2;
		/********************* 获取项目表格信息 ************************/
		ProjectInfoVo infoVo = projectInfoVoDao.fetchProjectInfoByKey(no);
		List<Qmsresult> list = qmsMaturityDao.downloadProblemQMS(no);// 先根据条件查询出项目信息
		if (list == null || list.size() <= 0) {
			return returnWb2Byte(wb);
		}
		setCellValue(0, infoVo.getName()+":审计问题跟踪", sheet.createRow(0), cellStyle);
		for (int i=0;i<list.size();i++) {
			Qmsresult qms = list.get(i);
			row = sheet.createRow(num);
			setCellValue(0, String.valueOf(i+1), row, cellStyle);
			setCellValue(1, qms.getSource(), row, cellStyle);
			setCellValue(2, qms.getContent(), row, cellStyle);
			setCellValue(3, qms.getEvidence(), row, cellStyle);
			setCellValue(4, qms.getProblemType(), row, cellStyle);
			setCellValue(5, qms.getMajorProblem(), row, cellStyle);
			setCellValue(6, qms.getImprovementMeasure(), row, cellStyle);
			setCellValue(7, qms.getDutyPerson(), row, cellStyle);
			setCellValue(8, qms.getPlanClosedTime() == null ? "" : dt.format(qms.getPlanClosedTime()), row, cellStyle);
			setCellValue(9, qms.getState(), row, cellStyle);
			setCellValue(10, qms.getProgress(), row, cellStyle);
			num++;
		}
		return returnWb2Byte(wb);
	}

	public byte[] exportXMCB(String proNo, String date, String nextDate) {
		InputStream is = this.getClass().getResourceAsStream("/excel/project-cost.xlsx");
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);
		} catch (Exception e) {
			logger.error("导出失败", e);
		}
		Sheet sheet = wb.getSheetAt(0);
		Row row;
		CellStyle cellStyle = getCellStyle(wb);
		/************************ 获取表格头 ***************************/
		int num = 2;
		/********************* 获取项目表格信息 ************************/
		List<ProjectCost> list = generalSituationService.exportXMCB(proNo, date, nextDate);
		if (list == null || list.size() <= 0) {
			return returnWb2Byte(wb);
		}
		for (ProjectCost pc : list) {
			row = sheet.createRow(num);
			setCellValue(0, String.valueOf(num - 1), row, cellStyle);
			setCellValue(1, pc.getName(), row, cellStyle);
			setCellValue(2, pc.getZrAccount(), row, cellStyle);
			setCellValue(3, pc.getHwAccount(), row, cellStyle);
			setCellValue(4, 0.0 == pc.getAttendenceFirst() ? "-" : String.valueOf(pc.getAttendenceFirst()), row,
					cellStyle);
			setCellValue(5, 0.0 == pc.getOvertimeFirst() ? "-" : String.valueOf(pc.getOvertimeFirst()), row, cellStyle);
			setCellValue(6, 0.0 == pc.getAttendenceSecond() ? "-" : String.valueOf(pc.getAttendenceSecond()), row,
					cellStyle);
			setCellValue(7, 0.0 == pc.getOvertimeSecond() ? "-" : String.valueOf(pc.getOvertimeSecond()), row,
					cellStyle);
			setCellValue(8, 0.0 == pc.getAttendenceThird() ? "-" : String.valueOf(pc.getAttendenceThird()), row,
					cellStyle);
			setCellValue(9, 0.0 == pc.getOvertimeThird() ? "-" : String.valueOf(pc.getOvertimeThird()), row, cellStyle);
			setCellValue(10, 0.0 == pc.getAttendenceFourth() ? "-" : String.valueOf(pc.getAttendenceFourth()), row,
					cellStyle);
			setCellValue(11, 0.0 == pc.getOvertimeFourth() ? "-" : String.valueOf(pc.getOvertimeFourth()), row,
					cellStyle);
			setCellValue(12, 0.0 == pc.getAttendenceFifth() ? "-" : String.valueOf(pc.getAttendenceFifth()), row,
					cellStyle);
			setCellValue(13, 0.0 == pc.getOvertimeFifth() ? "-" : String.valueOf(pc.getOvertimeFifth()), row,
					cellStyle);
			setCellValue(14, 0.0 == pc.getAttendenceSixth() ? "-" : String.valueOf(pc.getAttendenceSixth()), row,
					cellStyle);
			setCellValue(15, 0.0 == pc.getOvertimeSixth() ? "-" : String.valueOf(pc.getOvertimeSixth()), row,
					cellStyle);
			setCellValue(16, 0.0 == pc.getAttendenceSeventh() ? "-" : String.valueOf(pc.getAttendenceSeventh()), row,
					cellStyle);
			setCellValue(17, 0.0 == pc.getOvertimeSeventh() ? "-" : String.valueOf(pc.getOvertimeSeventh()), row,
					cellStyle);
			setCellValue(18, 0.0 == pc.getTotalHours() ? "-" : String.valueOf(pc.getTotalHours()), row, cellStyle);
			
			num++;
		}
		return returnWb2Byte(wb);
	}

}
