package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.icss.mvp.dao.ILowLocDao;
import com.icss.mvp.entity.Dept;
import com.icss.mvp.entity.LowDetails;
import com.icss.mvp.entity.LowLoc;
import com.icss.mvp.entity.LowOutputEntity;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.SaveLowLoc;
import com.icss.mvp.util.DateUtils;

@Service
public class LowLocService {
	private final static Logger LOG = Logger.getLogger(LowLocService.class);

	@Autowired
	private ILowLocDao lowLocDao;

	@Autowired
	private TestMeasuresService testMeasuresService;

	/**
	 * 查询所有低产出原因
	 * 
	 * @return
	 */
	
	public Map<String, List<String>> queryLowLocReasons() {
		Map<String, List<String>> map = new HashMap<>();
		map.put("noSel", lowLocDao.queryNoLocReasons());
		map.put("lowSel", lowLocDao.queryLowLocReasons());
		return map;
	}

	/**
	 * 查询所有低产出人员
	 * 
	 * @return
	 */
	public List<LowLoc> queryLowLocPerson(String proNo, String month, String standard) {
		Integer locStandard = 0;
		try {
			locStandard = Integer.valueOf(standard);
		} catch (Exception e) {
			LOG.info(e);
			return null;
		}
		List<LowLoc> list = new ArrayList<>();
		List<Map<String, Object>> map = lowLocDao.queryMember(proNo);
		month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		Integer type = 0;// 默认commit统计
		type = lowLocDao.queryTongjiType(proNo);
		if (map != null && map.size() != 0) {
			for (Map<String, Object> one : map) {
				LowLoc lowloc = null;
				if (one.get("account") != null) {
					lowloc = lowLocDao.queryFromLowloc(proNo, (String) one.get("account"), month, locStandard);
					if (lowloc != null) {
						list.add(lowloc);
						continue;
					}
				}
				lowloc = new LowLoc();
				lowloc.setProjNo(proNo);
				String pdu = lowLocDao.queryPDU(proNo);
				if (pdu == null) {
					lowloc.setPdu("");
				} else {
					lowloc.setPdu(pdu);
				}
				if (one.get("NAME") == null) {
					lowloc.setName("");
				} else {
					lowloc.setName((String) one.get("NAME"));
				}
				if (one.get("account") == null) {
					lowloc.setAccount("");
				} else {
					lowloc.setAccount((String) one.get("account"));
				}
				Integer loc = 0;
				if (type == 0) {// 0为commit统计
					if (one.get("svngit") == null || "".equals(String.valueOf(one.get("svngit")))) {
						loc = lowLocDao.queryLocWx(lowloc.getAccount(), month, proNo);
					} else {
						loc = lowLocDao.queryLoc(String.valueOf(one.get("svngit")), month, proNo);
					}
				} else if (type == 1) {// 1为message统计
					if (one.get("svngit") == null || "".equals(String.valueOf(one.get("svngit")))) {
						loc = lowLocDao.queryLocByMessage(lowloc.getAccount(), month, proNo);
					} else {
						loc = lowLocDao.queryLocByMessage(String.valueOf(one.get("svngit")), month, proNo);
					}
				}
				if (loc == null) {
					lowloc.setLoc(0);
				} else {
					lowloc.setLoc(loc);
				}
				if (lowloc.getLoc() >= locStandard) {
					continue;
				}
				if (lowloc.getLoc() == 0) {
					lowloc.setSureLowloc("无产出人员");
				} else {
					lowloc.setSureLowloc("低产出人员");
				}
				lowloc.setLowLocReason("");
				lowloc.setRemark("");
				lowloc.setMonth("");
				lowloc.setStandard("");
				list.add(lowloc);
			}
			return list;
		}
		return null;
	}

	/**
	 * 保存所有低产出人员
	 * 
	 * @return
	 */
	public String saveLowLoc(SaveLowLoc allLowLoc) {
		List<LowLoc> list = allLowLoc.getAllLowLoc();
		for (int i = 0; i < list.size(); i++) {
			try {
				LowLoc ll = new LowLoc();
				ll = list.get(i);
				String month = ll.getMonth();
				month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
				if (i == 0) {
					lowLocDao.deleteHavedLowLoc(ll.getProjNo(), month);
				}
				ll.setMonth(ll.getMonth() + "-01");
				lowLocDao.saveLowLoc(ll);
			} catch (Exception e) {
				LOG.info(e);
				return "NO";
			}
		}
		return "OK";
	}

	/**
	 * 导出所有低产出人员
	 * @param no 
	 * @param month 
	 * 
	 * @return
	 */
	public byte[] exportLowloc(List<String> no, String month) {
		month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		InputStream is = this.getClass().getResourceAsStream("/excel/lowloc-template.xlsx");
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
		writeSheet1(wb,no,month);
		writeSheet2(wb,no,month);
		writeSheet3(wb,no,month);
		writeSheet4(wb,no,month);
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
	 * 01-外包人员产出分析（汇总）
	 * @param no 
	 * @param month 
	 */
	private void writeSheet1(Workbook wb, List<String> no, String month){
		try {
			Sheet sheet = wb.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			List<LowOutputEntity> resultList = new ArrayList<>();
			Map<String, LowOutputEntity> outputs = new HashMap<>();
			List<Map<String, Object>> list = lowLocDao.queryCountnoAndCount("("+ StringUtilsLocal.listToSqlIn(no)+")",month);
				for(Map<String, Object> element:list) {
					String department = element.get("spdu")==null?"":String.valueOf(element.get("spdu"));
					if(department == null) {
						continue;
					}	
					LowOutputEntity outputEntity = outputs.get(department) == null ? new LowOutputEntity()
							: outputs.get(department);
					outputEntity.setDepartment(department);
					String type = element.get("sureLowloc")==null?"":String.valueOf(element.get("sureLowloc"));
					int total = element.get("num")==null? 0:Integer.parseInt(String.valueOf(element.get("num")));
					if(total<=0 || StringUtils.isBlank(type)) {
						continue;
					}
					int count = element.get("counts")==null? 0:Integer.parseInt(String.valueOf(element.get("counts")));
					//判断是否低产出
					if("低产出人员".equals(type)) {
						outputEntity.setLowMemberCount(count);
					}else{
						outputEntity.setZeroMemberCount(count);
					}
					//判断总人数是否有误
					if(total<(outputEntity.getLowMemberCount()+outputEntity.getZeroMemberCount())) {
						total = outputEntity.getLowMemberCount()+outputEntity.getZeroMemberCount();
					}
					outputEntity.setTotalMemberCount(total);
					String lowRate = String.format("%.2f", (double)((double)outputEntity.getLowMemberCount()*100 /(double)total));
					outputEntity.setLowRate(lowRate);
					String zeroRate = String.format("%.2f", (double)((double)outputEntity.getZeroMemberCount()*100 /(double)total));
					outputEntity.setZeroRate(zeroRate);
					outputs.put(department,outputEntity);
				}
		resultList =  new ArrayList<LowOutputEntity>(outputs.values());
			for (int i = 0; i < resultList.size(); i++) {
			  row = sheet.getRow(i + 3);
				cell = row.getCell(0);
				Object obj = resultList.get(i).getDepartment();
				String pdu = obj == null ? "" : String.valueOf(obj);
				cell.setCellValue(pdu);
				cell = row.getCell(1);
				obj = resultList.get(i).getTotalMemberCount();
				String num = obj == null ? "" : String.valueOf(obj);
				cell.setCellValue(num);
				cell = row.getCell(2);
				obj = resultList.get(i).getZeroMemberCount();
				String Zero = obj == null ? "" : String.valueOf(obj);
				cell.setCellValue(Zero);
				cell = row.getCell(3);
				obj = resultList.get(i).getZeroRate();
				String Zeror = obj == null ? "" : String.valueOf(obj);
				cell.setCellValue(Zeror);
				cell = row.getCell(4);
				obj = resultList.get(i).getLowMemberCount();
				String Low = obj == null ? "" : String.valueOf(obj);
				cell.setCellValue(Low);
				cell = row.getCell(5);
				obj = resultList.get(i).getLowRate();
				String Lowl = obj == null ? "" : String.valueOf(obj);
				cell.setCellValue(Lowl);
			}
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	}

	/**
	 * 02-无产出汇总
	 * @param month 
	 * @param no 
	 */
	private void writeSheet2(Workbook wb, List<String> no, String month) {
		try {
			Sheet sheet = wb.getSheetAt(1);
			Row row = null;
			Cell cell = null;
			int i = 0;
			List<Map<String, Object>> list = lowLocDao.queryPDUfromLowloc("("+ StringUtilsLocal.listToSqlIn(no)+")",month);
			for (Map<String, Object> map : list) {
				Integer sum = 0;//无产出总人数
				Integer reason1 = 0;Integer reason2 = 0;Integer reason3 = 0;Integer reason4 = 0;
				Integer reason5 = 0;Integer reason6 = 0;Integer reason7 = 0;Integer reason8 = 0;
				Integer reason9 = 0;Integer reason10 = 0;Integer reason11 = 0;Integer reason12 = 0;
				Integer reason13 = 0;Integer reason14 = 0;Integer reason15 = 0;Integer reason16 = 0;
				Integer reason17 = 0;Integer reason18 = 0;
				Object obj = map.get("spdu");//部门
				String pdu = obj == null ? "" : String.valueOf(obj);	
				obj = map.get("reason");//原因
				String reason = obj == null ? "" : String.valueOf(obj);
				obj = map.get("counts");//每个原因对应人数
				String counts = obj == null ? "" : String.valueOf(obj);
				//审核原因并计算总数
				if("测试工程师".equals(reason)) {
					String  reasons= counts;
					reason1 = Integer.valueOf(reasons);
					sum +=reason1;
				}else if("测试协调员".equals(reason)) {
					String  reasons= counts;
					reason2 = Integer.valueOf(reasons);
					sum +=reason2;
				}else if("开发工程师".equals(reason)) {
					String  reasons= counts;
					reason3 = Integer.valueOf(reasons);
					sum +=reason3;
				}else if("美工".equals(reason)) {
					String  reasons= counts;
					reason4 = Integer.valueOf(reasons);
					sum +=reason4;
				}else if("模块设计工程师".equals(reason)) {
					String  reasons= counts;
					reason5 = Integer.valueOf(reasons);
					sum +=reason5;
				}else if("维护工程师".equals(reason)) {
					String  reasons= counts;
					reason6 = Integer.valueOf(reasons);
					sum +=reason6;
				}else if("系统测试工程师".equals(reason)) {
					String  reasons= counts;
					reason7 = Integer.valueOf(reasons);
					sum +=reason7;
				}else if("系统设计工程师".equals(reason)) {
					String  reasons= counts;
					reason8 = Integer.valueOf(reasons);
					sum +=reason8;
				}else if("项目经理".equals(reason)) {
					String  reasons= counts;
					reason9 = Integer.valueOf(reasons);
					sum +=reason9;
				}else if("质量工程师".equals(reason)) {
					String  reasons= counts;
					reason10 = Integer.valueOf(reasons);
					sum +=reason10;
				}else if("资料工程师".equals(reason)) {
					String  reasons= counts;
					reason11 = Integer.valueOf(reasons);
					sum +=reason11;
				}else if("实验室操作员".equals(reason)) {
					String  reasons= counts;
					reason12 = Integer.valueOf(reasons);
					sum +=reason12;
				}else if("新员工".equals(reason)) {
					String  reasons= counts;
					reason13 = Integer.valueOf(reasons);
					sum +=reason13;
				}else if("开发语言统计不到".equals(reason)) {
					String  reasons= counts;
					reason14 = Integer.valueOf(reasons);
					sum +=reason14;
				}else if("配置库遗漏".equals(reason)) {
					String  reasons= counts;
					reason15 = Integer.valueOf(reasons);
					sum +=reason15;
				}else if("安全编码审计".equals(reason)) {
					String  reasons= counts;
					reason16 = Integer.valueOf(reasons);
					sum +=reason16;
				}else if("离职".equals(reason)) {
					String  reasons= counts;
					reason17 = Integer.valueOf(reasons);
					sum +=reason17;
				}else if("统计有误".equals(reason)) {
					String  reasons= counts;
					reason18 = Integer.valueOf(reasons);
					sum +=reason18;
				}
					row = sheet.getRow(i + 2);
					cell = row.getCell(0);
					cell.setCellValue(pdu);
					cell = row.getCell(1);
					cell.setCellValue(sum);
					cell = row.getCell(2);
					cell.setCellValue(reason1);
					cell = row.getCell(3);
					cell.setCellValue(reason2);
					cell = row.getCell(4);
					cell.setCellValue(reason3);
					cell = row.getCell(5);
					cell.setCellValue(reason4);
					cell = row.getCell(6);
					cell.setCellValue(reason5);
					cell = row.getCell(7);
					cell.setCellValue(reason6);
					cell = row.getCell(8);
					cell.setCellValue(reason7);
					cell = row.getCell(9);
					cell.setCellValue(reason8);
					cell = row.getCell(10);
					cell.setCellValue(reason9);
					cell = row.getCell(11);
					cell.setCellValue(reason10);
					cell = row.getCell(12);
					cell.setCellValue(reason11);
					cell = row.getCell(13);
					cell.setCellValue(reason12);
					cell = row.getCell(14);
					cell.setCellValue(reason13);
					cell = row.getCell(15);
					cell.setCellValue(reason14);
					cell = row.getCell(16);
					cell.setCellValue(reason15);
					cell = row.getCell(17);
					cell.setCellValue(reason16);
					cell = row.getCell(18);
					cell.setCellValue(reason17);
					cell = row.getCell(19);
					cell.setCellValue(reason18);
				i++;
			}
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	}

	/**
	 * 03-低产出汇总
	 */
	private void writeSheet3(Workbook wb, List<String> no, String month) {
		try {
			Sheet sheet = wb.getSheetAt(2);
			Row row = null;
			Cell cell = null;
			int i = 0;
			List<Map<String, Object>> list = lowLocDao.queryPDUfromLowloczero("("+ StringUtilsLocal.listToSqlIn(no)+")",month);
			for (Map<String, Object> map : list) {
				Integer sum = 0;//低产出总人数
				Integer reason1 = 0;Integer reason2 = 0;Integer reason3 = 0;Integer reason4 = 0;
				Integer reason5 = 0;Integer reason6 = 0;Integer reason7 = 0;Integer reason8 = 0;
				Integer reason9 = 0;Integer reason10 = 0;Integer reason11 = 0;Integer reason12 = 0;
				Object obj = map.get("spdu");//部门
				String pdu = obj == null ? "" : String.valueOf(obj);	
				obj = map.get("reason");//原因
				String reason = obj == null ? "" : String.valueOf(obj);
				obj = map.get("counts");//每个原因对应人数
				String counts = obj == null ? "" : String.valueOf(obj);
				//审核原因并计算总数
				if("业务需求量小".equals(reason)) {
					String  reasons= counts;
					reason1 = Integer.valueOf(reasons);
					sum +=reason1;
				}else if("新员工".equals(reason)) {
					String  reasons= counts;
					reason2 = Integer.valueOf(reasons);
					sum +=reason2;
				}else if("代码有专人提交".equals(reason)) {
					String  reasons= counts;
					reason3 = Integer.valueOf(reasons);
					sum +=reason3;
				}else if("配置库遗漏".equals(reason)) {
					String  reasons= counts;
					reason4 = Integer.valueOf(reasons);
					sum +=reason4;
				}else if("部分投入测试".equals(reason)) {
					String  reasons= counts;
					reason5 = Integer.valueOf(reasons);
					sum +=reason5;
				}else if("工具开发兼顾工具维护".equals(reason)) {
					String  reasons= counts;
					reason6 = Integer.valueOf(reasons);
					sum +=reason6;
				}else if("PM有部分项目管理工作".equals(reason)) {
					String  reasons= counts;
					reason7 = Integer.valueOf(reasons);
					sum +=reason7;
				}else if("部分投入技术攻关预研".equals(reason)) {
					String  reasons= counts;
					reason8 = Integer.valueOf(reasons);
					sum +=reason8;
				}else if("部分投入系统部署".equals(reason)) {
					String  reasons= counts;
					reason9 = Integer.valueOf(reasons);
					sum +=reason9;
				}else if("离职".equals(reason)) {
					String  reasons= counts;
					reason10 = Integer.valueOf(reasons);
					sum +=reason10;
				}else if("投入CI".equals(reason)) {
					String  reasons= counts;
					reason11 = Integer.valueOf(reasons);
					sum +=reason11;
				}else if("导出数据异常".equals(reason)) {
					String  reasons= counts;
					reason12 = Integer.valueOf(reasons);
					sum +=reason12;
				}
					row = sheet.getRow(i + 2);
					cell = row.getCell(0);
					cell.setCellValue(pdu);
					cell = row.getCell(1);
					cell.setCellValue(sum);
					cell = row.getCell(2);
					cell.setCellValue(reason1);
					cell = row.getCell(3);
					cell.setCellValue(reason2);
					cell = row.getCell(4);
					cell.setCellValue(reason3);
					cell = row.getCell(5);
					cell.setCellValue(reason4);
					cell = row.getCell(6);
					cell.setCellValue(reason5);
					cell = row.getCell(7);
					cell.setCellValue(reason6);
					cell = row.getCell(8);
					cell.setCellValue(reason7);
					cell = row.getCell(9);
					cell.setCellValue(reason8);
					cell = row.getCell(10);
					cell.setCellValue(reason9);
					cell = row.getCell(11);
					cell.setCellValue(reason10);
					cell = row.getCell(12);
					cell.setCellValue(reason11);
				i++;
			}
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	}

	/**
	 * 04-详细信息
	 * @param no 
	 * @param month 
	 */
	private void writeSheet4(Workbook wb, List<String> no, String month) {
		try {
			Sheet sheet = wb.getSheetAt(3);
			Row row = null;
			Cell cell = null;
			CellStyle cellStyle = getCellStyle(wb);
			List<LowLoc> list = lowLocDao.queryLowlocDetail("("+ StringUtilsLocal.listToSqlIn(no)+")",month);
			for (int i = 0; i < list.size(); i++) {
				LowLoc ll = list.get(i);
				row = sheet.getRow(i + 2);
				cell = row.getCell(0);
				String proNo = ll.getProjNo();
				cell.setCellValue(proNo == null ? "" : proNo);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(1);
				String proName = lowLocDao.queryProName(proNo);
				cell.setCellValue(proName == null ? "" : proName);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(2);
				String pdu = ll.getPdu();
				cell.setCellValue(pdu == null ? "" : pdu);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(3);
				String name = ll.getName();
				cell.setCellValue(name == null ? "" : name);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(4);
				String account = ll.getAccount();
				cell.setCellValue(account == null ? "" : account);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(5);
				Integer loc = ll.getLoc();
				cell.setCellValue(loc);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(6);
				String sureLowloc = ll.getSureLowloc();
				cell.setCellValue(sureLowloc == null ? "" : sureLowloc);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(7);
				String lowLocReason = ll.getLowLocReason();
				cell.setCellValue(lowLocReason == null ? "" : lowLocReason);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(8);
				String remark = ll.getRemark();
				cell.setCellValue(remark == null ? "" : remark);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(9);
				String month1 = ll.getMonth();
				cell.setCellValue(month1 == null ? "" : month1);
				cell.setCellStyle(cellStyle);
				cell = row.getCell(10);
				String standard = ll.getStandard();
				cell.setCellValue(standard == null ? "" : standard);
				cell.setCellStyle(cellStyle);
			}
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	}
	/**
	 * 页面加载产出分析汇总报表
	 */
	public List<LowDetails> AnalysisSummary(ProjectInfo projectInfo, String client, String month,
			String line,String column) {
		Map<String, Object> param = new HashMap<>();
		param.put("client", client);
		setValue2Param(client,param,projectInfo);
		month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		if ("1".equals(column)) {
			column = "无产出人员";
		}else {
			column = "低产出人员";
		}
		List<LowDetails> resultList = new ArrayList<>();
		param.put("month",month);
		param.put("line",line);
		param.put("column",column);
		Map<String, LowDetails> details = new HashMap<>();
	    	try {
	    		List<Map<String, Object>> list = lowLocDao.AnalysisSummary(param);
	    		List<String> java = Arrays.asList("java","xml","sql","properties","jsp","html","css","js","txt");
	    		List<String> js = Arrays.asList("html","htm","css","js","txt");
	    		List<String> c = Arrays.asList("h","hpp","hxx","cpp","cc","cxx","c","txt","c++");
	    		Integer javaNum = 0;
	    		Integer jsNum = 0;
	    		Integer ccNum = 0;
	    		Integer sum = 0;
	    		Integer num = 0;
	    		for(Map<String, Object> element:list) {
					String staff = element.get("staff")==null?"":String.valueOf(element.get("staff"));
					if(staff == null) {
						continue;
					}
					if(details.get(staff) == null) {
						javaNum = 0;
			    		jsNum = 0;
			    		ccNum = 0;
			    		sum = 0;
			    		num = 0;
					}
					LowDetails detailsAnalysis= details.get(staff) == null ? new LowDetails()
							: details.get(staff);
					detailsAnalysis.setStaff(staff);
					String account = element.get("account")==null?"":String.valueOf(element.get("account"));
					detailsAnalysis.setAccount(account);
					String department = element.get("department")==null?"":String.valueOf(element.get("department"));
					detailsAnalysis.setDepartment(department);
					String subproduct = element.get("subproduct")==null?"":String.valueOf(element.get("subproduct"));
					detailsAnalysis.setSubproduct(subproduct);
					String spdu = element.get("spdu")==null?"":String.valueOf(element.get("spdu"));
					detailsAnalysis.setSpdu(spdu);
					String project = element.get("project")==null?"":String.valueOf(element.get("project"));
					detailsAnalysis.setProject(project);
					String stype = element.get("stype")==null?"":String.valueOf(element.get("stype"));
					int count = element.get("counts")==null? 0:Integer.parseInt(String.valueOf(element.get("counts")));
					//判断代码类型并分类
					if(java.contains(stype)) {
						javaNum += count;
						detailsAnalysis.setJavaNum(javaNum);
					}
					if(js.contains(stype)) {
						jsNum += count;
						detailsAnalysis.setJsNum(jsNum);
					}
					if(c.contains(stype)) {
						ccNum += count;
						detailsAnalysis.setCcNum(ccNum);
					}
					sum += count;
					if(count > 0){num += 1;}else {num += 0;}
					detailsAnalysis.setNum(num);
					detailsAnalysis.setSum(sum);
					details.put(staff,detailsAnalysis);
				}
			} catch (Exception e) {
				LOG.error("export excel failed!", e);
			}
		    resultList =  new ArrayList<LowDetails>(details.values());	
			return resultList;
		}
	/**
	 * 页面加载产出分析汇总报表
	 */
	public List<LowDetails> zeroMember(ProjectInfo projectInfo, String client, String month,
			String line,String column) {
		Map<String, Object> param = new HashMap<>();
		param.put("client", client);
		setValue2Param(client,param,projectInfo);

		month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		if ("1".equals(column)) {column = "安全编码审计";
		}else if("2".equals(column)){column = "实验室操作员";
		}else if("3".equals(column)){column = "开发工程师";
		}else if("4".equals(column)){column = "开发语言统计不到";
		}else if("5".equals(column)){column = "新员工";
		}else if("6".equals(column)){column = "模块设计工程师";
		}else if("7".equals(column)){column = "测试协调员";
		}else if("8".equals(column)){column = "测试工程师";
		}else if("9".equals(column)){column = "离职";
		}else if("10".equals(column)){column = "系统测试工程师";
		}else if("11".equals(column)){column = "系统设计工程师";
		}else if("12".equals(column)){column = "统计有误";
		}else if("13".equals(column)){column = "维护工程师";
		}else if("14".equals(column)){column = "美工";
		}else if("15".equals(column)){column = "质量工程师";
		}else if("16".equals(column)){column = "资料工程师";
		}else if("17".equals(column)){column = "配置库遗留";
		}else if("18".equals(column)){column = "项目经理";
		}
		List<LowDetails> resultList = new ArrayList<>();
		param.put("month",month);
		param.put("line",line);
		param.put("column",column);
		Map<String, LowDetails> details = new HashMap<>();
	    	try {
	    		List<Map<String, Object>> list = lowLocDao.zeroMember(param);
	    		List<String> java = Arrays.asList("java","xml","sql","properties","jsp","html","css","js","txt");
	    		List<String> js = Arrays.asList("html","htm","css","js","txt");
	    		List<String> c = Arrays.asList("h","hpp","hxx","cpp","cc","cxx","c","txt","c++");
	    		Integer javaNum = 0;
	    		Integer jsNum = 0;
	    		Integer ccNum = 0;
	    		Integer sum = 0;
	    		Integer num = 0;
	    		for(Map<String, Object> element:list) {
					String staff = element.get("staff")==null?"":String.valueOf(element.get("staff"));
					if(staff == null) {
						continue;
					}
					if(details.get(staff) == null) {
						javaNum = 0;
			    		jsNum = 0;
			    		ccNum = 0;
			    		sum = 0;
			    		num = 0;
					}
					LowDetails detailsAnalysis= details.get(staff) == null ? new LowDetails()
							: details.get(staff);
					detailsAnalysis.setStaff(staff);
					String account = element.get("account")==null?"":String.valueOf(element.get("account"));
					detailsAnalysis.setAccount(account);
					String department = element.get("department")==null?"":String.valueOf(element.get("department"));
					detailsAnalysis.setDepartment(department);
					String subproduct = element.get("subproduct")==null?"":String.valueOf(element.get("subproduct"));
					detailsAnalysis.setSubproduct(subproduct);
					String spdu = element.get("spdu")==null?"":String.valueOf(element.get("spdu"));
					detailsAnalysis.setSpdu(spdu);
					String project = element.get("project")==null?"":String.valueOf(element.get("project"));
					detailsAnalysis.setProject(project);
					String stype = element.get("stype")==null?"":String.valueOf(element.get("stype"));
					int count = element.get("counts")==null? 0:Integer.parseInt(String.valueOf(element.get("counts")));
					//判断代码类型并分类
					if(java.contains(stype)) {
						javaNum += count;
						detailsAnalysis.setJavaNum(javaNum);
					}
					if(js.contains(stype)) {
						jsNum += count;
						detailsAnalysis.setJsNum(jsNum);
					}
					if(c.contains(stype)) {
						ccNum += count;
						detailsAnalysis.setCcNum(ccNum);
					}
					sum += count;
					if(count > 0){num += 1;}else {num += 0;}
					detailsAnalysis.setNum(num);
					detailsAnalysis.setSum(sum);
					details.put(staff,detailsAnalysis);
				}
			} catch (Exception e) {
				LOG.error("export excel failed!", e);
			}
		    resultList =  new ArrayList<LowDetails>(details.values());	
			return resultList;
		}
	/**
	 * 低产出页面详情
	 */
	public List<LowDetails> lowMember(ProjectInfo projectInfo, String client, String month,
			String line,String column) {
		Map<String, Object> param = new HashMap<>();
		param.put("client", client);
		setValue2Param(client,param,projectInfo);
		month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		if ("1".equals(column)) {column = "PM有部分项目管理工作";
		}else if("2".equals(column)){column = "业务需求量小";
		}else if("3".equals(column)){column = "代码有专人提交";
		}else if("4".equals(column)){column = "导出数据异常";
		}else if("5".equals(column)){column = "工具开发兼顾工具维护";
		}else if("6".equals(column)){column = "投入CI";
		}else if("7".equals(column)){column = "新员工";
		}else if("8".equals(column)){column = "离职";
		}else if("9".equals(column)){column = "部分投入技术攻关预研";
		}else if("10".equals(column)){column = "部分投入测试";
		}else if("11".equals(column)){column = "部分投入系统部署";
		}else if("12".equals(column)){column = "配置库遗漏";
		}
		List<LowDetails> resultList = new ArrayList<>();
		param.put("month",month);
		param.put("line",line);
		param.put("column",column);
		Map<String, LowDetails> details = new HashMap<>();
	    	try {
	    		List<Map<String, Object>> list = lowLocDao.lowMember(param);
	    		List<String> java = Arrays.asList("java","xml","sql","properties","jsp","html","css","js","txt");
	    		List<String> js = Arrays.asList("html","htm","css","js","txt");
	    		List<String> c = Arrays.asList("h","hpp","hxx","cpp","cc","cxx","c","txt","c++");
	    		Integer javaNum = 0;
	    		Integer jsNum = 0;
	    		Integer ccNum = 0;
	    		Integer sum = 0;
	    		Integer num = 0;
	    		for(Map<String, Object> element:list) {
					String staff = element.get("staff")==null?"":String.valueOf(element.get("staff"));
					if(staff == null) {
						continue;
					}
					if(details.get(staff) == null) {
						javaNum = 0;
			    		jsNum = 0;
			    		ccNum = 0;
			    		sum = 0;
			    		num = 0;
					}
					LowDetails detailsAnalysis= details.get(staff) == null ? new LowDetails()
							: details.get(staff);
					detailsAnalysis.setStaff(staff);
					String account = element.get("account")==null?"":String.valueOf(element.get("account"));
					detailsAnalysis.setAccount(account);
					String department = element.get("department")==null?"":String.valueOf(element.get("department"));
					detailsAnalysis.setDepartment(department);
					String subproduct = element.get("subproduct")==null?"":String.valueOf(element.get("subproduct"));
					detailsAnalysis.setSubproduct(subproduct);
					String spdu = element.get("spdu")==null?"":String.valueOf(element.get("spdu"));
					detailsAnalysis.setSpdu(spdu);
					String project = element.get("project")==null?"":String.valueOf(element.get("project"));
					detailsAnalysis.setProject(project);
					String stype = element.get("stype")==null?"":String.valueOf(element.get("stype"));
					int count = element.get("counts")==null? 0:Integer.parseInt(String.valueOf(element.get("counts")));
					//判断代码类型并分类
					if(java.contains(stype)) {
						javaNum += count;
						detailsAnalysis.setJavaNum(javaNum);
					}
					if(js.contains(stype)) {
						jsNum += count;
						detailsAnalysis.setJsNum(jsNum);
					}
					if(c.contains(stype)) {
						ccNum += count;
						detailsAnalysis.setCcNum(ccNum);
					}
					sum += count;
					if(count > 0){num += 1;}else {num += 0;}
					detailsAnalysis.setNum(num);
					detailsAnalysis.setSum(sum);
					details.put(staff,detailsAnalysis);
				}
			} catch (Exception e) {
				LOG.error("export excel failed!", e);
			}
		    resultList =  new ArrayList<LowDetails>(details.values());	
			return resultList;
		}
	/**
	 * 页面加载产出分析汇总报表
	 */
	public List<LowOutputEntity> loadreportFormTable1(ProjectInfo projectInfo, String client, String month) {
		Map<String, Object> param = new HashMap<>();
		param.put("client", client);
		setValue2Param(client,param,projectInfo);
		month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		List<LowOutputEntity> resultList = new ArrayList<>();
		Map<String, LowOutputEntity> outputs = new HashMap<>();
		param.put("month",month);
		try {
			List<Map<String, Object>> list = lowLocDao.queryPDUNum1(param);
			for(Map<String, Object> element:list) {
				String department = element.get("spdu")==null?"":String.valueOf(element.get("spdu"));
				if(department == null) {
					continue;
				}	
				LowOutputEntity outputEntity = outputs.get(department) == null ? new LowOutputEntity()
						: outputs.get(department);
				outputEntity.setDepartment(department);
				String no = element.get("nos")==null?"":String.valueOf(element.get("nos"));
				outputEntity.setNo(no);
				String type = element.get("sureLowloc")==null?"":String.valueOf(element.get("sureLowloc"));
				int total = element.get("num")==null? 0:Integer.parseInt(String.valueOf(element.get("num")));
				if(total<=0 || StringUtils.isBlank(type)) {
					continue;
				}
				int count = element.get("counts")==null? 0:Integer.parseInt(String.valueOf(element.get("counts")));
				//判断是否低产出
				if("低产出人员".equals(type)) {
					outputEntity.setLowMemberCount(count);
				}else{
					outputEntity.setZeroMemberCount(count);
				}
				//判断总人数是否有误
				if(total<(outputEntity.getLowMemberCount()+outputEntity.getZeroMemberCount())) {
					total = outputEntity.getLowMemberCount()+outputEntity.getZeroMemberCount();
				}
				outputEntity.setTotalMemberCount(total);
				outputs.put(department,outputEntity);
			}
		} catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
	    resultList =  new ArrayList<LowOutputEntity>(outputs.values());	
		return resultList;
	}

	/**
	 * 页面加载无产出汇总报表
	 */
public List<Map<String, Object>> loadreportFormTable2(ProjectInfo projectInfo, String client, String month) {
	    Map<String, Object> param = new HashMap<>();
	    param.put("client", client);
		setValue2Param(client,param,projectInfo);
	    month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		param.put("month",month);
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
		List<Map<String, Object>> list = lowLocDao.queryPDUfromLowloc2(param);
		for (Map<String, Object> map : list) {
			Map<String, Object> resultMap = new HashMap<>();
			Integer sum = 0;//无产出总人数
			Object obj = map.get("spdu");//部门
			String pdu = obj == null ? "" : String.valueOf(obj);	
			obj = map.get("reason");//原因
			String reason = obj == null ? "" : String.valueOf(obj);
			obj = map.get("counts");//每个原因对应人数
			String counts = obj == null ? "" : String.valueOf(obj);
			obj = map.get("nos");
			String no = obj == null ? "" : String.valueOf(obj);
			//审核原因并计算总数
			if("安全编码审计".equals(reason)) {
				String  reasons= counts;
				Integer reason1 = Integer.valueOf(reasons);
				resultMap.put("reason1", reason1);
				sum +=reason1;
			}else if("实验室操作员".equals(reason)) {
				String  reasons= counts;
				Integer reason2 = Integer.valueOf(reasons);
				resultMap.put("reason2", reason2);
				sum +=reason2;
			}else if("开发工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason3 = Integer.valueOf(reasons);
				resultMap.put("reason3", reason3);
				sum +=reason3;
			}else if("开发语言统计不到".equals(reason)) {
				String  reasons= counts;
				Integer reason4 = Integer.valueOf(reasons);
				resultMap.put("reason4", reason4);
				sum +=reason4;
			}else if("新员工".equals(reason)) {
				String  reasons= counts;
				Integer reason5 = Integer.valueOf(reasons);
				resultMap.put("reason5", reason5);
				sum +=reason5;
			}else if("模块设计工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason6 = Integer.valueOf(reasons);
				resultMap.put("reason6", reason6);
				sum +=reason6;
			}else if("测试协调员".equals(reason)) {
				String  reasons= counts;
				Integer reason7 = Integer.valueOf(reasons);
				resultMap.put("reason7", reason7);
				sum +=reason7;
			}else if("测试工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason8 = Integer.valueOf(reasons);
				resultMap.put("reason8", reason8);
				sum +=reason8;
			}else if("离职".equals(reason)) {
				String  reasons= counts;
				Integer reason9 = Integer.valueOf(reasons);
				resultMap.put("reason9", reason9);
				sum +=reason9;
			}else if("系统测试工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason10 = Integer.valueOf(reasons);
				resultMap.put("reason10", reason10);
				sum +=reason10;
			}else if("系统设计工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason11 = Integer.valueOf(reasons);
				resultMap.put("reason11", reason11);
				sum +=reason11;
			}else if("统计有误".equals(reason)) {
				String  reasons= counts;
				Integer reason12 = Integer.valueOf(reasons);
				resultMap.put("reason12", reason12);
				sum +=reason12;
			}else if("维护工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason13 = Integer.valueOf(reasons);
				resultMap.put("reason13", reason13);
				sum +=reason13;
			}else if("美工".equals(reason)) {
				String  reasons= counts;
				Integer reason14 = Integer.valueOf(reasons);
				resultMap.put("reason14", reason14);
				sum +=reason14;
			}else if("质量工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason15 = Integer.valueOf(reasons);
				resultMap.put("reason15", reason15);
				sum +=reason15;
			}else if("资料工程师".equals(reason)) {
				String  reasons= counts;
				Integer reason16 = Integer.valueOf(reasons);
				resultMap.put("reason16", reason16);
				sum +=reason16;
			}else if("配置库遗留".equals(reason)) {
				String  reasons= counts;
				Integer reason17 = Integer.valueOf(reasons);
				resultMap.put("reason17", reason17);
				sum +=reason17;
			}else if("项目经理".equals(reason)) {
				String  reasons= counts;
				Integer reason18 = Integer.valueOf(reasons);
				resultMap.put("reason18", reason18);
				sum +=reason18;
			}else{
				String  reasons= counts;
				Integer reason19 = Integer.valueOf(reasons);//预留
				}
			resultMap.put("no", no);
			resultMap.put("pdu", pdu);
			resultMap.put("noLocNum", sum);
			resultList.add(resultMap);
		}
		}catch (Exception e) {
			LOG.error("export excel failed!", e);
		}
		return resultList;
	}

	/**
	 * 页面加载低产出汇总报表
	 * @param month 
	 */
	public List<Map<String, Object>> loadreportFormTable3(ProjectInfo projectInfo, String client, String month) {
		Map<String, Object> param = new HashMap<>();
	    param.put("client", client);
		setValue2Param(client,param,projectInfo);
	    month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		List<Map<String, Object>> resultList = new ArrayList<>();
	    param.put("month", month);

		try {
			List<Map<String, Object>> list = lowLocDao.queryPDUfromLowloc3(param);
			for (Map<String, Object> map : list) {
				Map<String, Object> resultMap = new HashMap<>();
				Integer sum = 0;//低产出总人数
				Object obj = map.get("spdu");//部门
				String pdu = obj == null ? "" : String.valueOf(obj);	
				obj = map.get("reason");//原因
				String reason = obj == null ? "" : String.valueOf(obj);
				obj = map.get("counts");//每个原因对应人数
				String counts = obj == null ? "" : String.valueOf(obj);
				obj = map.get("nos");
				String no = obj == null ? "" : String.valueOf(obj);
				//审核原因并计算总数
				if("PM有部分项目管理工作".equals(reason)) {
					String  reasons= counts;
					Integer reason1 = Integer.valueOf(reasons);
					resultMap.put("reason1", reason1);
					sum +=reason1;
				}else if("业务需求量小".equals(reason)) {
					String  reasons= counts;
					Integer reason2 = Integer.valueOf(reasons);
					resultMap.put("reason2", reason2);
					sum +=reason2;
				}else if("代码有专人提交".equals(reason)) {
					String  reasons= counts;
					Integer reason3 = Integer.valueOf(reasons);
					resultMap.put("reason3", reason3);
					sum +=reason3;
				}else if("导出数据异常".equals(reason)) {
					String  reasons= counts;
					Integer reason4 = Integer.valueOf(reasons);
					resultMap.put("reason4", reason4);
					sum +=reason4;
				}else if("工具开发兼顾工具维护".equals(reason)) {
					String  reasons= counts;
					Integer reason5 = Integer.valueOf(reasons);
					resultMap.put("reason5", reason5);
					sum +=reason5;
				}else if("投入CI".equals(reason)) {
					String  reasons= counts;
					Integer reason6 = Integer.valueOf(reasons);
					resultMap.put("reason6", reason6);
					sum +=reason6;
				}else if("新员工".equals(reason)) {
					String  reasons= counts;
					Integer reason7 = Integer.valueOf(reasons);
					resultMap.put("reason7", reason7);
					sum +=reason7;
				}else if("离职".equals(reason)) {
					String  reasons= counts;
					Integer reason8 = Integer.valueOf(reasons);
					resultMap.put("reason8", reason8);
					sum +=reason8;
				}else if("部分投入技术攻关预研".equals(reason)) {
					String  reasons= counts;
					Integer reason9 = Integer.valueOf(reasons);
					resultMap.put("reason9", reason9);
					sum +=reason9;
				}else if("部分投入测试".equals(reason)) {
					String  reasons= counts;
					Integer reason10 = Integer.valueOf(reasons);
					resultMap.put("reason10", reason10);
					sum +=reason10;
				}else if("部分投入系统部署".equals(reason)) {
					String  reasons= counts;
					Integer reason11 = Integer.valueOf(reasons);
					resultMap.put("reason11", reason11);
					sum +=reason11;
				}else if("配置库遗漏".equals(reason)) {
					String  reasons= counts;
					Integer reason12 = Integer.valueOf(reasons);
					resultMap.put("reason12", reason12);
					sum +=reason12;
				}
				else{
					String  reasons= counts;
					Integer reason13 = Integer.valueOf(reasons);//预留
					}
				resultMap.put("no", no);
				resultMap.put("pdu", pdu);
				resultMap.put("noLocNum", sum);
				resultList.add(resultMap);
			}
			}catch (Exception e) {
				LOG.error("export excel failed!", e);
			}
			return resultList;
		}

	/**
	 * 未进行低产出分析
	 * @param projectInfo
	 * @param client
	 * @param month
	 */
	public List<Map<String, Object>> loadreportFormTable4(ProjectInfo projectInfo, String client, String month
			) {
		Map<String, Object> param = new HashMap<>();
	    param.put("client", client);
		setValue2Param(client,param,projectInfo);
	    month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
	    List<Map<String, Object>> resultList = new ArrayList<>();
	    param.put("month", month);
	    	try {
	    		List<Map<String, Object>> list = lowLocDao.queryCountEverymonth(param);
	    		for (Map<String, Object> map : list) {
	    			Map<String, Object> resultMap = new HashMap<>();
	    			Object obj = map.get("entryName");
	    			String name = obj == null ? "" : String.valueOf(obj);
	    			obj = map.get("spdu");
	    			String pdu = obj == null ? "" : String.valueOf(obj);
	    			obj = map.get("pm");
	    			String pm = obj == null ? "" : String.valueOf(obj);
	    			obj = map.get("num");
	    			String nums = obj == null ? "" : String.valueOf(obj);
	    			Integer num = Integer.valueOf(nums);
	    			obj = map.get("nos");
	    			String no = obj == null ? "" : String.valueOf(obj);
	    			resultMap.put("no", no);
	    			resultMap.put("name", name);
	    			resultMap.put("pdu", pdu);
	    			resultMap.put("pm", pm);
	    			resultMap.put("num", num);
	    			resultList.add(resultMap);
	    		}
			} catch (Exception e) {
				// TODO: handle exception
			}		
		return resultList;
	}

	/**
	 * 页面加载产出分析汇总饼图
	 */
	public List<List<String>> loadreportFormcharts1(@ModelAttribute ProjectInfo projectInfo,String client,String month) {
		Map<String, Object> param = new HashMap<>();
		param.put("client", client);
		setValue2Param(client,param,projectInfo);
		month = month.substring(0, month.indexOf("-")) + month.substring(month.indexOf("-") + 1);
		List<List<String>> resultList = new ArrayList<>();
		param.put("month",month);
		List<Map<String, Object>> list1 = lowLocDao.queryCountFromLowloc(param);
		List<String> templist1 = new ArrayList<>();
		List<String> templist2 = new ArrayList<>();
		for (Map<String, Object> map : list1) {
			String sure = String.valueOf(map.get("sureLowloc"));
			String alls = String.valueOf(map.get("alls"));
			Integer pre = 0;
			if (alls != null && !"".equals(alls)) {
				pre = Integer.valueOf(alls);
			}
			templist1.add(sure);
			templist2.add(String.valueOf(pre));
		}
		resultList.add(templist1);
		resultList.add(templist2);
		templist1 = new ArrayList<>();
		templist2 = new ArrayList<>();
		List<String> templist3 = new ArrayList<>();
		List<Map<String, Object>> list2 = lowLocDao.queryCountByMonth(param);
		for (Map<String, Object> map : list2) {
			String month1 = String.valueOf(map.get("month"));
			String countno = String.valueOf(map.get("countno"));
			String countlow = String.valueOf(map.get("countlow"));
			if (month1 == null) {
				month1 = "";
			} else {
				month1 = month1.substring(0, month1.lastIndexOf("-"));
			}
			if (countno == null || "".equals(countno)) {
				countno = "0";
			}
			if (countlow == null || "".equals(countlow)) {
				countlow = "0";
			}
			templist1.add(month1);
			templist2.add(countno);
			templist3.add(countlow);
		}
		resultList.add(templist1);
		resultList.add(templist2);
		resultList.add(templist3);
		return resultList;
	}

	/**
	 * 页面加载无产出汇总饼图
	 */
	public List<List<String>> loadreportFormcharts2() {
		List<List<String>> resultList = new ArrayList<>();
		List<Map<String, Object>> list = lowLocDao.queryEveryReasonForNo();
		List<String> list1 = new ArrayList<>();
		List<String> list2 = new ArrayList<>();
		for (Map<String, Object> map : list) {
			String reason = String.valueOf(map.get("lowLocReason"));
			list1.add(reason);
			String num = String.valueOf(map.get("nonum"));
			if (num == null || "".equals(num.trim())) {
				list2.add("0");
			} else {
				list2.add(num);
			}
		}
		resultList.add(list1);
		resultList.add(list2);
		return resultList;
	}

	/**
	 * 首页加载低产出折线图
	 */
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> loadZXT(ProjectInfo proj) {
		List<List<Map<String, Object>>> resultList = new ArrayList<>();
		Map<String, Object> temp = setMap(proj);
		List<Map<String, Object>> maps = new ArrayList<>();
		if ((List<String>) temp.get("bu") != null && ((List<String>) (temp.get("bu"))).size() != 0) {
			maps = lowLocDao.loadZXTfromViewOP(temp);
		} else if ((List<String>) temp.get("hwpdu") != null && ((List<String>) (temp.get("hwpdu"))).size() != 0) {
			maps = lowLocDao.loadZXTfromViewHW(temp);
		} else if ((List<String>) temp.get("area") != null && ((List<String>) (temp.get("area"))).size() != 0) {
			maps = lowLocDao.loadZXTfromViewArea(temp);
		}
		List<Map<String, Object>> listLow = new ArrayList<>();
		List<Map<String, Object>> listNo = new ArrayList<>();
		List<String> month = new LinkedList<>();
		for (Map<String, Object> map : maps) {
			Map<String, Object> temp1 = new HashMap<>();
			temp1.put("month", String.valueOf(map.get("month")).substring(0, 7));
			temp1.put("sureLowloc", "低产出人员");
			temp1.put("num", map.get("countlow"));
			listLow.add(temp1);
			Map<String, Object> temp2 = new HashMap<>();
			temp2.put("month", String.valueOf(map.get("month")).substring(0, 7));
			temp2.put("sureLowloc", "无产出人员");
			temp2.put("num", map.get("countno"));
			listNo.add(temp2);
			month.add(String.valueOf(map.get("month")).substring(5, 7));
		}
		String year = DateUtils.getSystemYear();
		for (int i = 1; i <= 12; i++) {
			String mon = i < 10 ? "0" + String.valueOf(i) : String.valueOf(i);
			boolean flag = true;
			for (String m : month) {
				if (m.equals(mon)) {
					flag = false;
					break;
				}
			}
			if (flag == true) {
				Map<String, Object> temp1 = new HashMap<>();
				temp1.put("month", year + "-" + mon);
				temp1.put("sureLowloc", "低产出人员");
				temp1.put("num", 0);
				listLow.add(temp1);
				Map<String, Object> temp2 = new HashMap<>();
				temp2.put("month", year + "-" + mon);
				temp2.put("sureLowloc", "无产出人员");
				temp2.put("num", 0);
				listNo.add(temp2);
			}
		}
		resultList.add(sort(listLow));
		resultList.add(sort(listNo));
		return resultList;
	}

	private List<Map<String, Object>> sort(List<Map<String, Object>> list) {
		List<Map<String, Object>> result = new ArrayList<>();
		String year = DateUtils.getSystemYear();
		for (int i = 1; i <= 12; i++) {
			String mon = i < 10 ? year + "-0" + String.valueOf(i) : year + "-" + String.valueOf(i);
			for (Map<String, Object> map : list) {
				if (String.valueOf(map.get("month")).equals(mon)) {
					result.add(map);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 首页加载低产出无产出各原因饼图
	 */
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> lowlocBT(ProjectInfo proj) {
		List<List<Map<String, Object>>> resultList = new ArrayList<>();
		Map<String, Object> map = setMap(proj);
		// List<Map<String, Object>> list1 = lowLocDao.lowlocBT(map);
		// List<Map<String, Object>> list2 = lowLocDao.nolocBT(map);
		if ((List<String>) map.get("bu") != null && ((List<String>) (map.get("bu"))).size() != 0) {
			List<Map<String, Object>> list1 = lowLocDao.lowlocBTfromViewOP(map);
			List<Map<String, Object>> list2 = lowLocDao.nolocBTfromViewOP(map);
			resultList.add(list1);
			resultList.add(list2);
		} else if ((List<String>) map.get("hwpdu") != null && ((List<String>) (map.get("hwpdu"))).size() != 0) {
			List<Map<String, Object>> list1 = lowLocDao.lowlocBTfromViewHW(map);
			List<Map<String, Object>> list2 = lowLocDao.nolocBTfromViewHW(map);
			resultList.add(list1);
			resultList.add(list2);
		} else if ((List<String>) map.get("area") != null && ((List<String>) (map.get("area"))).size() != 0) {
			List<Map<String, Object>> list1 = lowLocDao.lowlocBTfromViewArea(map);
			List<Map<String, Object>> list2 = lowLocDao.nolocBTfromViewArea(map);
			resultList.add(list1);
			resultList.add(list2);
		}
		return resultList;
	}

	/**
	 * 首页加载低产出各PDU柱状图
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<List<Map<String, Object>>> lowlocPDU(ProjectInfo proj) {
		List<List<Map<String, Object>>> resultList = new ArrayList<>();
		Map<String, Object> tempmap = setMap(proj);
		List<String> bu = (List<String>) tempmap.get("bu");
		List<String> pdu = (List<String>) tempmap.get("pdu");
		List<String> hwpdu = (List<String>) tempmap.get("hwpdu");
		List<String> hwzpdu = (List<String>) tempmap.get("hwzpdu");
		List<String> area = (List<String>) tempmap.get("area");
		String month = String.valueOf(tempmap.get("month"));
		// low no
		List<Map<String, Object>> listLow = new ArrayList<>();
		List<Map<String, Object>> listNo = new ArrayList<>();
		// some
		List<Map<String, Object>> listSome = new ArrayList<>();
		if (bu.size() != 0) {
			if (pdu.size() == 0) {
				List<Dept> pdus = lowLocDao.getAllPDU(tempmap);// id
				for (Dept p : pdus) {
					Map<String, Object> tempLow = new HashMap<>();
					Map<String, Object> tempNo = new HashMap<>();
					Map<String, Object> tempSome = new HashMap<>();
					Integer countlow = 0;
					Integer countno = 0;
					Integer some = 0;
					List<String> projNos = lowLocDao.lowlocAllProjNofromOPbyPDU(p.getDeptId());
					for (String id : projNos) {
						Map<String, Object> num = lowLocDao.lowlocPDU(id, month);
						if (num != null) {
							countlow += Integer.valueOf(String.valueOf(num.get("countlow")));
							countno += Integer.valueOf(String.valueOf(num.get("countno")));
						}
						Integer alls = lowLocDao.getAllMembers(id);
						some += alls;
						Map<String, Object> lownos = lowLocDao.getLowlocMembers(month, id);
						if (lownos != null) {
							some = alls - Integer.valueOf(String.valueOf(lownos.get("countlow")))
									- Integer.valueOf(String.valueOf(lownos.get("countno")));
						}
						if (some < 0) {
							some = 0;
						}
					}
					tempLow.put("pdu", p.getDeptName());
					tempLow.put("sureLowloc", "低产出人员");
					tempLow.put("num", countlow);
					listLow.add(tempLow);
					tempNo.put("pdu", p.getDeptName());
					tempNo.put("sureLowloc", "无产出人员");
					tempNo.put("num", countno);
					listNo.add(tempNo);
					tempSome.put("pdu", p.getDeptName());
					tempSome.put("sureLowloc", "有产出人员");
					tempSome.put("num", some);
					listSome.add(tempSome);
				}
			} else if (pdu.size() != 0) {
				List<Dept> dus = lowLocDao.getAllDU(tempmap);
				for (Dept d : dus) {
					Map<String, Object> tempLow = new HashMap<>();
					Map<String, Object> tempNo = new HashMap<>();
					Map<String, Object> tempSome = new HashMap<>();
					Integer countlow = 0;
					Integer countno = 0;
					Integer some = 0;
					List<String> projNos = lowLocDao.lowlocAllProjNofromOPbyDU(d.getDeptId());
					for (String id : projNos) {
						Map<String, Object> num = lowLocDao.lowlocPDU(id, month);
						if (num != null) {
							countlow += Integer.valueOf(String.valueOf(num.get("countlow")));
							countno += Integer.valueOf(String.valueOf(num.get("countno")));
						}
						Integer alls = lowLocDao.getAllMembers(id);
						Map<String, Object> lownos = lowLocDao.getLowlocMembers(month, id);
						some += alls;
						if (lownos != null) {
							some = alls - Integer.valueOf(String.valueOf(lownos.get("countlow")))
									- Integer.valueOf(String.valueOf(lownos.get("countno")));
						}
						if (some < 0) {
							some = 0;
						}
					}
					tempLow.put("pdu", d.getDeptName());
					tempLow.put("sureLowloc", "低产出人员");
					tempLow.put("num", countlow);
					listLow.add(tempLow);
					tempNo.put("pdu", d.getDeptName());
					tempNo.put("sureLowloc", "无产出人员");
					tempNo.put("num", countno);
					listNo.add(tempNo);
					tempSome.put("pdu", d.getDeptName());
					tempSome.put("sureLowloc", "有产出人员");
					tempSome.put("num", some);
					listSome.add(tempSome);
				}
			}
		}
		if (hwpdu.size() != 0) {
			if (hwzpdu.size() == 0) {
				List<Dept> hwzpdus = lowLocDao.getAllHWZPDU(tempmap);
				for (Dept hw : hwzpdus) {
					Map<String, Object> tempLow = new HashMap<>();
					Map<String, Object> tempNo = new HashMap<>();
					Map<String, Object> tempSome = new HashMap<>();
					Integer countlow = 0;
					Integer countno = 0;
					Integer some = 0;
					List<String> projNos = lowLocDao.lowlocAllProjNofromHWbyHWZPDU(hw.getDeptId());
					for (String id : projNos) {
						Map<String, Object> num = lowLocDao.lowlocPDU(id, month);
						if (num != null) {
							countlow += Integer.valueOf(String.valueOf(num.get("countlow")));
							countno += Integer.valueOf(String.valueOf(num.get("countno")));
						}
						Integer alls = lowLocDao.getAllMembers(id);
						Map<String, Object> lownos = lowLocDao.getLowlocMembers(month, id);
						some += alls;
						if (lownos != null) {
							some = alls - Integer.valueOf(String.valueOf(lownos.get("countlow")))
									- Integer.valueOf(String.valueOf(lownos.get("countno")));
						}
						if (some < 0) {
							some = 0;
						}
					}
					tempLow.put("pdu", hw.getDeptName());
					tempLow.put("sureLowloc", "低产出人员");
					tempLow.put("num", countlow);
					listLow.add(tempLow);
					tempNo.put("pdu", hw.getDeptName());
					tempNo.put("sureLowloc", "无产出人员");
					tempNo.put("num", countno);
					listNo.add(tempNo);
					tempSome.put("pdu", hw.getDeptName());
					tempSome.put("sureLowloc", "有产出人员");
					tempSome.put("num", some);
					listSome.add(tempSome);
				}
			} else if (hwzpdu.size() != 0) {
				List<Dept> pduspdts = lowLocDao.getAllPDUSPDT(tempmap);
				for (Dept pp : pduspdts) {
					Map<String, Object> tempLow = new HashMap<>();
					Map<String, Object> tempNo = new HashMap<>();
					Map<String, Object> tempSome = new HashMap<>();
					Integer countlow = 0;
					Integer countno = 0;
					Integer some = 0;
					List<String> projNos = lowLocDao.lowlocAllProjNofromHWbyPDUSPDT(pp.getDeptId());
					for (String id : projNos) {
						Map<String, Object> num = lowLocDao.lowlocPDU(id, month);
						if (num != null) {
							countlow += Integer.valueOf(String.valueOf(num.get("countlow")));
							countno += Integer.valueOf(String.valueOf(num.get("countno")));
						}
						Integer alls = lowLocDao.getAllMembers(id);
						Map<String, Object> lownos = lowLocDao.getLowlocMembers(month, id);
						some += alls;
						if (lownos != null) {
							some = alls - Integer.valueOf(String.valueOf(lownos.get("countlow")))
									- Integer.valueOf(String.valueOf(lownos.get("countno")));
						}
						if (some < 0) {
							some = 0;
						}
					}
					tempLow.put("pdu", pp.getDeptName());
					tempLow.put("sureLowloc", "低产出人员");
					tempLow.put("num", countlow);
					listLow.add(tempLow);
					tempNo.put("pdu", pp.getDeptName());
					tempNo.put("sureLowloc", "无产出人员");
					tempNo.put("num", countno);
					listNo.add(tempNo);
					tempSome.put("pdu", pp.getDeptName());
					tempSome.put("sureLowloc", "有产出人员");
					tempSome.put("num", some);
					listSome.add(tempSome);
				}
			}
		}
		if (area.size() != 0) {
			List<Dept> areas = lowLocDao.getAllAreas(tempmap);
			for (Dept ar : areas) {
				Map<String, Object> tempLow = new HashMap<>();
				Map<String, Object> tempNo = new HashMap<>();
				Map<String, Object> tempSome = new HashMap<>();
				Integer countlow = 0;
				Integer countno = 0;
				Integer some = 0;
				List<String> projNos = lowLocDao.lowlocAllProjNofromArea(ar.getDeptId());
				for (String id : projNos) {
					Map<String, Object> num = lowLocDao.lowlocPDU(id, month);
					if (num != null) {
						countlow += Integer.valueOf(String.valueOf(num.get("countlow")));
						countno += Integer.valueOf(String.valueOf(num.get("countno")));
					}
					Integer alls = lowLocDao.getAllMembers(id);
					Map<String, Object> lownos = lowLocDao.getLowlocMembers(month, id);
					some += alls;
					if (lownos != null) {
						some = alls - Integer.valueOf(String.valueOf(lownos.get("countlow")))
								- Integer.valueOf(String.valueOf(lownos.get("countno")));
					}
					if (some < 0) {
						some = 0;
					}
				}
				tempLow.put("pdu", ar.getDeptName());
				tempLow.put("sureLowloc", "低产出人员");
				tempLow.put("num", countlow);
				listLow.add(tempLow);
				tempNo.put("pdu", ar.getDeptName());
				tempNo.put("sureLowloc", "无产出人员");
				tempNo.put("num", countno);
				listNo.add(tempNo);
				tempSome.put("pdu", ar.getDeptName());
				tempSome.put("sureLowloc", "有产出人员");
				tempSome.put("num", some);
				listSome.add(tempSome);
			}
		}
		resultList.add(listLow);
		resultList.add(listNo);
		resultList.add(listSome);
		return resultList;
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
		return map;
	}

	/**
	 * 加载选中项目组的开发总人数
	 * 
	 * @return
	 */
	public Integer kaifaNum(ProjectInfo proj) {
		Map<String, Object> map = setMap(proj);
		return lowLocDao.kaifaNum(map);
	}
	/*  ---------------------------导出表格样式--------------------------*/
	public CellStyle getCellStyle(Workbook wb){
        // 单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //设置边框
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        //设置自动换行
        cellStyle.setWrapText(true);
        //设置字体
        Font cellFont = wb.createFont();
        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }


    private void setValue2Param(String client,Map<String,Object> param,ProjectInfo projectInfo){
		if("2".equals(client)){//华为
			List<String> hwpduId = CollectionUtilsLocal.splitToList(projectInfo.getHwpdu());
			List<String> hwzpduId = CollectionUtilsLocal.splitToList(projectInfo.getHwzpdu());
			List<String> pduSpdtId = CollectionUtilsLocal.splitToList(projectInfo.getPduSpdt());
			param.put("hwpduId", hwpduId);
			param.put("hwzpduId", hwzpduId);
			param.put("pduSpdtId", pduSpdtId);
		}else if ("1".equals(client)) {//中软
			List<String> buId = CollectionUtilsLocal.splitToList(projectInfo.getBu());
			List<String> pduId = CollectionUtilsLocal.splitToList(projectInfo.getPdu());
			List<String> duId = CollectionUtilsLocal.splitToList(projectInfo.getDu());
			param.put("buId", buId);
			param.put("pduId", pduId);
			param.put("duId", duId);
		}else if ("3".equals(client)) {//地域
			List<String> areaId = CollectionUtilsLocal.splitToList(projectInfo.getArea());
			param.put("areaId", areaId);
		}
	}
}
