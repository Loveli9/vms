package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.icss.mvp.dao.IPMpaperDao;
import com.icss.mvp.dao.IterativeWorkManageDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.IterativeWorkManage;
import com.icss.mvp.entity.Measure;
import com.icss.mvp.entity.SysDictItem;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.util.DateUtils;

@Service
public class PMpaperService {
	private final static Logger LOG = Logger.getLogger(PMpaperService.class);

	@Autowired
	private IPMpaperDao pmDao;

	@Autowired
	private IterativeWorkManageDao iterativeWorkManageDao;

	/**
	 * 导出PM周报
	 * 
	 * @return
	 */
	public byte[] exportLowloc(String no) {
		InputStream is = this.getClass().getResourceAsStream("/excel/PM-Paper-template.xlsx");
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("read file failed!" + e.getMessage());
				}
			}
		} catch (IOException e) {
			LOG.error("read file failed!", e);
			return null;
		}
		writeSheet1(no, wb);
		writeSheet2(no, wb);
		writeSheet3(no, wb);
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			try {
				wb.write(os);
			} catch (IOException e) {
				LOG.error("export excel failed!", e);
			}
			return os.toByteArray();
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					LOG.error("read file failed!" + e.getMessage());
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							LOG.error("Output file failed!" + e.getMessage());
						}
					}
				}
			}
		}
	}

	/**
	 * 01-DTS问题单汇总
	 */
	private void writeSheet1(String no, Workbook wb) {
		try {
			Sheet sheet = wb.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			String projName = pmDao.getProjName(no);// 查询项目名称
			List<String> pms = pmDao.getPMname(no);
			String pm = "";
			if (pms != null && pms.size() != 0) {
				StringBuffer bf = new StringBuffer();
				for (String p : pms) {
					bf.append(p + ",");
				}
				pm = bf.substring(0, bf.length() - 1);// 查询PM
			}
			Map<String, String> temp = new HashMap<>();
			temp.put("no", no);
			temp.put("all", "all");
			Map<String, Object> severity = pmDao.sumBySeverity(temp);// 遗留问题单
			Integer alls = Integer.valueOf(String.valueOf(severity.get("alls")));
			Integer critical = Integer.valueOf(String.valueOf(severity.get("critical")));
			Integer major = Integer.valueOf(String.valueOf(severity.get("major")));
			Integer minor = Integer.valueOf(String.valueOf(severity.get("minor")));
			Integer suggestion = Integer.valueOf(String.valueOf(severity.get("suggestion")));
			Double di = (double) critical * 10 + (double) major * 3 + (double) minor * 1 + (double) suggestion * 0.1;// 遗留DI
			row = sheet.getRow(3);
			cell = row.getCell(0);
			cell.setCellValue(no == null ? "" : no);
			cell = row.getCell(1);
			cell.setCellValue(projName == null ? "" : projName);
			cell = row.getCell(2);
			cell.setCellValue(pm == null ? "" : pm);
			cell = row.getCell(3);
			cell.setCellValue(alls == null ? 0 : alls);
			cell = row.getCell(4);
			cell.setCellValue(di == null ? 0.0 : di);
			cell = row.getCell(5);
			cell.setCellValue(critical == null ? 0 : critical);
			cell = row.getCell(6);
			cell.setCellValue(major == null ? 0 : major);
			cell = row.getCell(7);
			cell.setCellValue(minor == null ? 0 : minor);
			cell = row.getCell(8);
			cell.setCellValue(suggestion == null ? 0 : suggestion);
			temp.replace("all", null);
			severity = pmDao.sumBySeverity(temp);// 累计遗留问题单
			alls = Integer.valueOf(String.valueOf(severity.get("alls")));
			critical = Integer.valueOf(String.valueOf(severity.get("critical")));
			major = Integer.valueOf(String.valueOf(severity.get("major")));
			minor = Integer.valueOf(String.valueOf(severity.get("minor")));
			suggestion = Integer.valueOf(String.valueOf(severity.get("suggestion")));
			di = (double) critical * 10 + (double) major * 3 + (double) minor * 1 + (double) suggestion * 0.1;// 遗留DI
			cell = row.getCell(9);
			cell.setCellValue(alls == null ? 0 : alls);
			cell = row.getCell(10);
			cell.setCellValue(di == null ? 0.0 : di);
			cell = row.getCell(11);
			cell.setCellValue(critical == null ? 0 : critical);
			cell = row.getCell(12);
			cell.setCellValue(major == null ? 0 : major);
			cell = row.getCell(13);
			cell.setCellValue(minor == null ? 0 : minor);
			cell = row.getCell(14);
			cell.setCellValue(suggestion == null ? 0 : suggestion);
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	}

	/**
	 * 02-质量指标
	 */
	@SuppressWarnings("deprecation")
	private void writeSheet2(String no, Workbook wb) {
		try {
			Sheet sheet = wb.getSheetAt(1);
			Row row = null;
			Cell cell = null;
			CellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			Font font = wb.createFont();
			font.setColor(HSSFColor.RED.index);
			style.setFont(font);
			List<Measure> measures = pmDao.measures(no);
			for (int i = 0; i < measures.size(); i++) {
				Measure measure = measures.get(i);
				row = sheet.getRow(i + 2);
				cell = row.getCell(0);
				cell.setCellValue(measure.getName() == null ? "" : measure.getName());
				cell = row.getCell(1);
				cell.setCellValue(measure.getUnit() == null ? "" : measure.getUnit());
				cell = row.getCell(2);
				cell.setCellValue(measure.getUpper() == null ? "" : measure.getUpper());
				cell = row.getCell(3);
				cell.setCellValue(measure.getLower() == null ? "" : measure.getLower());
				cell = row.getCell(4);
				cell.setCellValue(measure.getTarget() == null ? "" : measure.getTarget());
				cell = row.getCell(5);
				if (measure.getId() != 196L && measure.getId() != 197L && measure.getId() != 198L
						&& measure.getId() != 199L) {
					if (Double.valueOf(measure.getValue()) < Double.valueOf(measure.getLower())
							|| Double.valueOf(measure.getValue()) > Double.valueOf(measure.getUpper())) {
						cell.setCellStyle(style);
					}
				}
				cell.setCellValue(measure.getValue() == null ? "" : measure.getValue());
			}
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	}

	/**
	 * 03-需求
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void writeSheet3(String no, Workbook wb) {
		try {
			TableSplitResult tableSplitResult = new TableSplitResult<>();
			tableSplitResult.setQueryMap(new HashMap<>());
			List<IterativeWorkManage> needs = iterativeWorkManageDao.queryIteWorkManageInfo(tableSplitResult, no, null,
					null);
			Sheet sheet = wb.getSheetAt(2);
			Row row = null;
			Cell cell = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < needs.size(); i++) {
				IterativeWorkManage need = needs.get(i);
				row = sheet.getRow(i + 2);
				cell = row.getCell(0);
				cell.setCellValue(need.getIteType() == null ? "" : need.getIteType());
				cell = row.getCell(1);
				cell.setCellValue(need.getTopic() == null ? "" : need.getTopic());
				cell = row.getCell(2);
				cell.setCellValue(need.getIteName() == null ? "" : need.getIteName());
				cell = row.getCell(3);
				cell.setCellValue(need.getUserName() == null ? "" : need.getUserName());
				cell = row.getCell(4);
				cell.setCellValue(need.getPrior() == null ? "" : need.getPrior());
				cell = row.getCell(5);
				cell.setCellValue(need.getImportance() == null ? "" : need.getImportance());
				cell = row.getCell(6);
				cell.setCellValue(need.getStatus() == null ? "" : need.getStatus());
				cell = row.getCell(7);
				cell.setCellValue(need.getExpectHours() == null ? "" : need.getExpectHours());
				cell = row.getCell(8);
				cell.setCellValue(need.getActualHours() == null ? "" : need.getActualHours());
				cell = row.getCell(9);
				cell.setCellValue(need.getWrField() == null ? "" : need.getWrField());
				cell = row.getCell(10);
				cell.setCellValue(need.getVersion() == null ? "" : need.getVersion());
				cell = row.getCell(11);
				if (need.getCreateTime() != null) {
					cell.setCellValue(sdf.format(need.getCreateTime()));
				} else {
					cell.setCellValue("");
				}
				cell = row.getCell(12);
				if (need.getUpdateTime() != null) {
					cell.setCellValue(sdf.format(need.getUpdateTime()));
				} else {
					cell.setCellValue("");
				}
				cell = row.getCell(13);
				if (need.getEndTime() != null) {
					cell.setCellValue(sdf.format(need.getEndTime()));
				} else {
					cell.setCellValue("");
				}
			}
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	}

	/**
	 * 热门指标
	 */
	public void hotMeasures() {
		List<Map<String, Object>> hotMeasures = pmDao.hotMeasures();
		List<Map<String, Object>> result = new ArrayList<>();
		for (int i = 0; i < hotMeasures.size(); i++) {
			if (i >= 10) {
				if (Integer.valueOf(String.valueOf(hotMeasures.get(i).get("num"))) == Integer
						.valueOf(String.valueOf(result.get(i - 1).get("num")))) {
					result.add(hotMeasures.get(i));
				} else {
					break;
				}
			} else {
				result.add(hotMeasures.get(i));
			}
		}
	}

	/**
	 * 按迭代统计bug
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableSplitResult dtsCount(String no, String sort, String sortOrder) {
		List<Map<String, Object>> result = new ArrayList<>();
		TableSplitResult data = new TableSplitResult();
		List<IterationCycle> iterationCycles = pmDao.iterationCycles(no);
		for (IterationCycle iterationCycle : iterationCycles) {
			String startTime = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getStartDate());
			String endTime = DateUtils.SHORT_FORMAT_GENERAL.format(iterationCycle.getEndDate());
			Map<String, Object> dts = pmDao.dtsCount(no, startTime, endTime);
			Integer alls = Integer.valueOf(
					String.valueOf((dts.get("alls") == null || "".equals(dts.get("alls"))) ? "0" : dts.get("alls")));
			Integer critical = Integer.valueOf(String.valueOf(
					(dts.get("critical") == null || "".equals(dts.get("critical"))) ? "0" : dts.get("critical")));
			Integer major = Integer.valueOf(
					String.valueOf((dts.get("major") == null || "".equals(dts.get("major"))) ? "0" : dts.get("major")));
			Integer minor = Integer.valueOf(
					String.valueOf((dts.get("minor") == null || "".equals(dts.get("minor"))) ? "0" : dts.get("minor")));
			Integer suggestion = Integer.valueOf(String.valueOf(
					(dts.get("suggestion") == null || "".equals(dts.get("suggestion"))) ? "0" : dts.get("suggestion")));
			Map<String, Object> map = new HashMap<>();
			map.put("iteName", iterationCycle.getIteName());
			map.put("critical", critical);
			map.put("major", major);
			map.put("minor", minor);
			map.put("suggestion", suggestion);
			map.put("alls", alls);
			map.put("di", Double.valueOf(critical) * 10 + Double.valueOf(major) * 3 + Double.valueOf(minor) * 1
					+ Double.valueOf(suggestion) * 0.1);
			result.add(map);
		}
		if (!StringUtilsLocal.isBlank(sort) && !StringUtilsLocal.isBlank(sortOrder)) {
			CollectionUtilsLocal.listSort(result, sort, sortOrder);
		}
		data.setRows(result);
		return data;
	}

	/**
	 * 按迭代统计story
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableSplitResult story(String no, String sort, String sortOrder) {
		List<Map<String, Object>> result = new ArrayList<>();
		TableSplitResult data = new TableSplitResult();
		List<IterationCycle> iterationCycles = pmDao.iterationCycles(no);
		List<SysDictItem> sysDictItems = pmDao.storyStatus();
		for (IterationCycle iterationCycle : iterationCycles) {
			Map<String, Object> map = new HashMap<>();
			map.put("iteName", iterationCycle.getIteName());
			Integer alls = pmDao.storyCounts(no, iterationCycle.getId());
			map.put("alls", alls);
			for (SysDictItem sysDictItem : sysDictItems) {
				Integer num = pmDao.storyByStatus(no, iterationCycle.getId(), sysDictItem.getVal());
				map.put("status" + sysDictItem.getVal(), num);
			}
			result.add(map);
		}
		if (!StringUtilsLocal.isBlank(sort) && !StringUtilsLocal.isBlank(sortOrder)) {
			CollectionUtilsLocal.listSort(result, sort, sortOrder);
		}
		data.setRows(result);
		return data;
	}
	/**
	 * 按提交人统计bug
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TableSplitResult bugSubmission(String no, String sort, String sortOrder) {
		List<Map<String, Object>> result = new ArrayList<>();
		TableSplitResult data = new TableSplitResult();
		List<Map<String, Object>> account = pmDao.bugSubmissionAcc(no);
		for (Map<String, Object> acc : account) {
			String ACCOUNT = String.valueOf(
					(acc.get("ACCOUNT") == null || "".equals(acc.get("ACCOUNT"))) ? "0" : acc.get("ACCOUNT"));
		Map<String, Object> list = pmDao.bugSubmission(no,ACCOUNT);
		String creator = String.valueOf(
				(list.get("creator") == null || "".equals(list.get("creator"))) ? "0" : list.get("creator"));
		Integer critical = Integer.valueOf(String.valueOf(
				(list.get("critical") == null || "".equals(list.get("critical"))) ? "0" : list.get("critical")));
		Integer major = Integer.valueOf(
				String.valueOf((list.get("major") == null || "".equals(list.get("major"))) ? "0" : list.get("major")));
		Integer minor = Integer.valueOf(
				String.valueOf((list.get("minor") == null || "".equals(list.get("minor"))) ? "0" : list.get("minor")));
		Integer suggestion = Integer.valueOf(String.valueOf(
				(list.get("suggestion") == null || "".equals(list.get("suggestion"))) ? "0" : list.get("suggestion")));
		Integer alls = Integer.valueOf(
				String.valueOf((list.get("alls") == null || "".equals(list.get("alls"))) ? "0" : list.get("alls")));
		Map<String, Object> resultList = new HashMap<>();
		resultList.put("creator", creator);
		resultList.put("critical", critical);
		resultList.put("major", major);
		resultList.put("minor", minor);
		resultList.put("suggestion", suggestion);
		resultList.put("alls", alls);
		result.add(resultList);
		}
		if (!StringUtilsLocal.isBlank(sort) && !StringUtilsLocal.isBlank(sortOrder)) {
			CollectionUtilsLocal.listSort(result, sort, sortOrder);
		}
		data.setRows(result);
		return data;
	}
}
