package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.IDtsTaskDao;
import com.icss.mvp.dao.IProjectParameterValueDao;
import com.icss.mvp.entity.DtsInfo;
import com.icss.mvp.entity.DtsLogs;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.util.DateUtils;

@Service
public class DtsTaskService {
	private final static Logger LOG = Logger.getLogger(DtsTaskService.class);
	
	@Autowired
	private IDtsTaskDao dtsTaskDao;
	@Resource
	private IProjectParameterValueDao parameterValueDao;

	public Map<String, Object> getDtsSeverity(String projNo) {
		return dtsTaskDao.queryServerity(projNo);
	}

	public List<Map<String, Object>> queryDIs(String no, String date) {
		List<Map<String, Object>> hashMaps = dtsTaskDao.queryDts(no,date);
		String today = DateUtils.getToday();
		int len;
		if("00000000".equals(date)) {
			date = DateUtils.getSystemFewDay(today, -30);
			len = 30;
		}else {
			date = DateUtils.getSystemFewDay(date, 1);
			len = DateUtils.calculationTimeDifference(today,date);
		}
		List<Map<String, Object>> reList = new ArrayList<>();
		for(int i=0;i<len;i++){
			Map<String, Object> reMap = new HashMap<>();
			reMap.put("critNum", 0.0);
			reMap.put("majNum", 0.0);
			reMap.put("minNum", 0.0);
			reMap.put("sugNum", 0.0);
			for (Map<String, Object> hashMap : hashMaps) {
				String curentStatus = (String) hashMap.get("curentStatus");
				String severity = (String) hashMap.get("severity");
				String createdTime = (String) hashMap.get("createdTime");
				String lastUpdateTime = (String) hashMap.get("lastUpdateTime");
				if(Constants.CANCEL.equals(curentStatus)||Constants.CLOSE.equals(curentStatus)) {
					if(DateUtils.comparisonDateSize(createdTime,date)&&DateUtils.comparisonDateSize(date,lastUpdateTime)) {
						addNum(severity,reMap);
					}
				}else {
					if(DateUtils.comparisonDateSize(createdTime,date)) {
						addNum(severity,reMap);
					}
				}
			}
			Double dtsLeaveDI = 10*(Double)reMap.get("critNum")+
					3*(Double)reMap.get("majNum")+
					1*(Double)reMap.get("minNum")+
					0.1*(Double)reMap.get("sugNum");
			reMap.put("value", dtsLeaveDI);
			reMap.put("date", date);
			reList.add(reMap);
			date = DateUtils.getSystemFewDay(date,1);
		}
		return reList;
	}

	
	
	private void addNum(String severity, Map<String, Object> reMap) {
		if(Constants.CRITIAL.equals(severity)) {
			reMap.put("critNum", (Double)reMap.get("critNum")+1);
		}
		if(Constants.MAJOR.equals(severity)) {
			reMap.put("majNum", (Double)reMap.get("majNum")+1);
		}
		if(Constants.MINOR.equals(severity)) {
			reMap.put("minNum", (Double)reMap.get("minNum")+1);
		}
		if(Constants.SUGGESTION.equals(severity)) {
			reMap.put("sugNum", (Double)reMap.get("sugNum")+1);
		}
	}

	public List<Map<String, Object>> getDtsList(String projNo) {
		return parameterValueDao.queryDtsDiList(projNo,Constants.LEAVE_DI);
	}

	public List<Map<String, Object>> queryDINums(List<Map<String, Object>> maps, String projNo) {
		String date="00000000";
		if(maps!=null) {
			for (Map<String, Object> map : maps) {
				String newdate = (String) map.get("date");
				if(DateUtils.comparisonDateSize(date,newdate)) {
					date=newdate;
				}
			}
		}
		List<Map<String, Object>> ret = queryDIs(projNo,date);
		for (Map<String, Object> map : ret) {
			Date day =null;
			try {
				day = DateUtils.format.parse(String.valueOf(map.get("date")));
			} catch (ParseException e) {
				LOG.error("DtsLeaveDINumCollector myFormatter.parse fail!" , e);
			}
			ParameterValueNew paraValue = new ParameterValueNew();
			paraValue.setNo(projNo);
			paraValue.setParameterId(Integer.parseInt(Constants.LEAVE_DI));
			paraValue.setMonth(day);
			paraValue.setValue((Double)map.get("value"));
			parameterValueDao.insertParameterValueNew(paraValue);
		}
		for (Map<String, Object> map : ret) {
			maps.add(map);
		}
		return maps;
	}

	public List<HashMap<String, Object>> getDtsSeverityByVersion(String projNo) {
		return dtsTaskDao.queryServerityByVersion(projNo);
	}

	public List<HashMap<String, Object>> getDtsSeverityByEven(DtsInfo dtsInfo) {
		Map<String, Object> map=new HashMap<>();
		if(!setValue(map,"curentStatus",dtsInfo.getCurentStatus())) {
			return new ArrayList<>();
		}
		if(!setValue(map,"bVersions",dtsInfo.getbVersion())) {
			return new ArrayList<>();
		}
		map.put("projNo", dtsInfo.getProjNo());
		map.put("dimensionality", dtsInfo.getDimensionality());
		return dtsTaskDao.queryServerityByEven(map);
	}
	
	private boolean setValue(Map<String, Object> map,String key,String value) {
		List<String> valueList = new ArrayList<>();
		if(value!=null && !"".equals(value)) {
			String[] values = value.split(",");
			for (String value1 : values) {
				valueList.add(value1);
			}
		}
		map.put(key, valueList);
		if(valueList.size()==0) {
			return false;
		}
		return true;
	}

	public List<Map<String, Object>> getDtsVersion(String no) {
		return dtsTaskDao.queryDtsBVersion(no);
	}
	
	public byte[] dtsDownload(String no, String severity, String selected, String selectedVal, String bVersion, String curentStatus) throws ClassNotFoundException{
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(0);
		Cell cell=null;
		Class cla=Class.forName("com.icss.mvp.entity.DtsLogs");
		Field[] fields = cla.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(fields[i].getName());
		}
		Map<String, Object> map=new HashMap<>();
		map.put("no", no);
		map.put("severity", severity);
		map.put("selected", selected);
		map.put("selectedVal", selectedVal);
		List<String> tempList=new ArrayList<>();
		if(bVersion!=null && !"".equals(bVersion)) {
			String[] values = bVersion.split(",");
			for (String value : values) {
				tempList.add(value);
			}
			map.put("bVersion", tempList);
		}else {
			map.put("bVersion", tempList);
		}
		tempList=new ArrayList<>();
		if(curentStatus!=null && !"".equals(curentStatus)) {
			String[] values = curentStatus.split(",");
			for (String value : values) {
				tempList.add(value);
			}
			map.put("curentStatus", tempList);
		}else {
			map.put("curentStatus", tempList);
		}
		List<DtsLogs> dtsList=dtsTaskDao.dtsDownload(map);
		DtsLogs dts=null;
		Date date=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 1; i <= dtsList.size(); i++) {
			dts=dtsList.get(i-1);
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellValue(dts.getTicket_No());
			cell = row.createCell(1);
			cell.setCellValue(dts.getProduct_Line());
			cell = row.createCell(2);
			cell.setCellValue(dts.getProduct_Family());
			cell = row.createCell(3);
			cell.setCellValue(dts.getPdt());
			cell = row.createCell(4);
			cell.setCellValue(dts.getProduct());
			cell = row.createCell(5);
			cell.setCellValue(dts.getV_Version());
			cell = row.createCell(6);
			cell.setCellValue(dts.getR_Version());
			cell = row.createCell(7);
			cell.setCellValue(dts.getC_Version());
			cell = row.createCell(8);
			cell.setCellValue(dts.getL_Version());
			cell = row.createCell(9);
			cell.setCellValue(dts.getB_Version());
			cell = row.createCell(10);
			cell.setCellValue(dts.getFeature());
			cell = row.createCell(11);
			cell.setCellValue(dts.getSubsystem());
			cell = row.createCell(12);
			cell.setCellValue(dts.getModule());
			cell = row.createCell(13);
			cell.setCellValue(dts.getModule_Version());
			cell = row.createCell(14);
			cell.setCellValue(dts.getCreator_Lowest_level_Dept());
			cell = row.createCell(15);
			cell.setCellValue(dts.getPartner_Defect());
			cell = row.createCell(16);
			cell.setCellValue(dts.getTier_1());
			cell = row.createCell(17);
			cell.setCellValue(dts.getTier_2());
			cell = row.createCell(18);
			cell.setCellValue(dts.getTier_3());
			cell = row.createCell(19);
			cell.setCellValue(dts.getPartner_Company());
			cell = row.createCell(20);
			cell.setCellValue(dts.getPartner_Company_Dept());
			cell = row.createCell(21);
			cell.setCellValue(dts.getResponsible_Project_Team());
			cell = row.createCell(22);
			cell.setCellValue(dts.getCause_Device_Failure_Reliability_Risk());
			cell = row.createCell(23);
			cell.setCellValue(dts.getDefect_Source_No());
			cell = row.createCell(24);
			cell.setCellValue(dts.getBackground());
			cell = row.createCell(25);
			cell.setCellValue(dts.getBrief());
			cell = row.createCell(26);
			date=dts.getDefect_Found_Time();
			if(date!=null) {
				cell.setCellValue(sdf.format(date));
			}else {
				cell.setCellValue("");
			}
			cell = row.createCell(27);
			date=dts.getCreated_Time();
			if(date!=null) {
				cell.setCellValue(sdf.format(date));
			}else {
				cell.setCellValue("");
			}
			cell = row.createCell(28);
			cell.setCellValue(dts.getCreator());
			cell = row.createCell(29);
			cell.setCellValue(dts.getComments());
			cell = row.createCell(30);
			date=dts.getLast_Updated_Time();
			if(date!=null) {
				cell.setCellValue(sdf.format(date));
			}else {
				cell.setCellValue("");
			}
			cell = row.createCell(31);
			cell.setCellValue(dts.getDefect_Found_Activity());
			cell = row.createCell(32);
			cell.setCellValue(dts.getTigger());
			cell = row.createCell(33);
			cell.setCellValue(dts.getImpact());
			cell = row.createCell(34);
			cell.setCellValue(dts.getDefect_Found_Stage());
			cell = row.createCell(35);
			cell.setCellValue(dts.getSeverity());
			cell = row.createCell(36);
			cell.setCellValue(dts.getPhysical_Failure_Cause());
			cell = row.createCell(37);
			cell.setCellValue(dts.getDevice_Reliability_Defect_Type());
			cell = row.createCell(38);
			cell.setCellValue(dts.getDefect_Report_Quality());
			cell = row.createCell(39);
			cell.setCellValue(dts.getReject_Reason());
			cell = row.createCell(40);
			cell.setCellValue(dts.getDefect_Source());
			cell = row.createCell(41);
			cell.setCellValue(dts.getOmission_Reason());
			cell = row.createCell(42);
			cell.setCellValue(dts.getReoccur_Chance());
			cell = row.createCell(43);
			cell.setCellValue(dts.getColse_Type());
			cell = row.createCell(44);
			cell.setCellValue(dts.getCurent_Status());
			cell = row.createCell(45);
			cell.setCellValue(dts.getWorkflow_Status());
			cell = row.createCell(46);
			cell.setCellValue(dts.getCurrent_Handler());
			cell = row.createCell(47);
			cell.setCellValue(dts.getWorkflow_Type());
			cell = row.createCell(48);
			cell.setCellValue(dts.getDefect_Handler());
			cell = row.createCell(49);
			cell.setCellValue(dts.getFailures());
			cell = row.createCell(50);
			cell.setCellValue(dts.getOriginal_Ticket_No());
			cell = row.createCell(51);
			cell.setCellValue(dts.getTicket_Copier());
			cell = row.createCell(52);
			cell.setCellValue(dts.getBug_fix_Version());
			cell = row.createCell(53);
			cell.setCellValue(dts.getAssociated_BaseLine_Version());
			cell = row.createCell(54);
			cell.setCellValue(dts.getTest_roadmap_Version());
			cell = row.createCell(55);
			cell.setCellValue(dts.getTested_BaseLine_Version());
			cell = row.createCell(56);
			cell.setCellValue(dts.getPreviously_Downgraded());
			cell = row.createCell(57);
			cell.setCellValue(dts.getFeature_Project());
			cell = row.createCell(58);
			cell.setCellValue(dts.getrD_Project());
			cell = row.createCell(59);
			cell.setCellValue(dts.getCommon_Defect());
			cell = row.createCell(60);
			cell.setCellValue(dts.getNumber_of_Locating_people());
			cell = row.createCell(61);
			cell.setCellValue(dts.getIs_Publish());
			cell = row.createCell(62);
			cell.setCellValue(dts.getExpected_Defect_found_Activity());
			cell = row.createCell(63);
			cell.setCellValue(dts.getExpected_Defect_found_Subactivity());
			cell = row.createCell(64);
			date=dts.getImportDate();
			if(date!=null) {
				cell.setCellValue(sdf.format(date));
			}else {
				cell.setCellValue("");
			}
			cell = row.createCell(65);
			date=dts.getUpdateDate();
			if(date!=null) {
				cell.setCellValue(sdf.format(date));
			}else {
				cell.setCellValue("");
			}
			cell = row.createCell(66);
			cell.setCellValue(dts.getNo());
		}
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			try {
				wb.write(os);
			} catch (IOException e) {
				LOG.error("export excel failed!", e);
			}
			//按os流输出wb文件
			byte[] b = os.toByteArray();
			return b;
		} finally {
			if(wb!=null) {
				try {
					wb.close();
				} catch (IOException e) {
					LOG.error("read file failed!"+ e.getMessage());
				}
			}
			if(os!=null) {
				try {
					os.close();
				} catch (IOException e) {
					LOG.error("Output file failed!"+ e.getMessage());
				}
			}
		}
	}
	/**
	 * 测试获取当前数据
	 * @param projNo
	 * @return
	 */
	public Map<String, Object> getDtsSeverityToday(String projNo) {
		return dtsTaskDao.getDtsSeverityToday(projNo);
	}
	
}
