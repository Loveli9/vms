package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.*;
import com.icss.mvp.entity.ParameterInfoNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectInfoVo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.MathUtils;

@Service
@SuppressWarnings("all")
public class PcbListService {
	private static Logger logger = Logger.getLogger(PcbListService.class);

	@Autowired
	private MonthMeasureDao measureDao;
	@Autowired
	private IPcbListDao pcbListDao;

	@Autowired
	private ProjectInfoVoDao projectInfoVoDao;
	
	@Resource
	private ProjectInfoService projectInfoService;

	@Resource
	private ICodeCheckDao codeCheckDao;
	@Resource
	private IJobPcbDao jobPcbDao;

	/**
	 * 获取每个月的measure_value_history里存的度量指标值
	 * @param projectInfo
	 * @param username
	 * @param ids measure的id
	 * @return
	 */
	@Deprecated
	public List<Object> getPcbTestEfficient(ProjectInfo projectInfo, String username,String[] ids) {
		Set<String> nos = projectInfoService.projectNos(projectInfo, username);
		List<String> proNos = new ArrayList<>(nos);
		List<Object> ret = new ArrayList<>();
		if (proNos.size() <= 0) {
			return ret;
		}

		for (String id : ids) {
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("proNos", proNos);
			parameter.put("measureId", id);
			parameter.put("pcbCategory", projectInfo.getPcbCategory());
			List<String> proNos1 = pcbListDao.queryPcbProjectConfigList(parameter);
			int projectNum = proNos1.size();
			ret.add(getTestEfficientByMeasure(StringUtilsLocal.listToSqlIn(proNos1), id, projectNum));
		}
		return ret;
	}

	/**
	 * 获取当前月的measure_value_history里存的度量指标值
	 * @param projectInfo
	 * @param username
	 * @param ids measure的id
	 * @return
	 */
	@Deprecated
	public List<Map<String, Object>> getPcbTestEfficientXM(ProjectInfo projectInfo, String username,String[] ids) {
		List<Map<String, Object>> ret = new ArrayList<>();
		List<ProjectInfoVo> infoVos = projectInfoService.projectInfos(projectInfo, username);
		List<String> idsList = new ArrayList<>();
		for (String id : ids) {
			idsList.add(id);
		}
		for (ProjectInfoVo infoVo : infoVos) {
			Map<String,Object> parameter =  new HashMap<>();
			parameter.put("pcbCategory", projectInfo.getPcbCategory());
			parameter.put("proNo", infoVo.getNo());
			List<String> measureIds= pcbListDao.queryPcbProjectConfigIdList(parameter);

			Map<String, Object> map = new HashMap<>();
			map.put("zpdu", infoVo.getHwzpdu());
			map.put("pdu", infoVo.getPduSpdt());
			map.put("name", infoVo.getName());
			map.put("no", infoVo.getNo());
			List<Map<String, Object>> list = measureDao.getMeasuresByProjectAndMeasure("('" + infoVo.getNo() + "')",
					"(" + StringUtilsLocal.listToSqlIn(idsList) + ")");
			map.put("value0", "0.0");
			if (list == null || list.size() <= 0) {
				map.put("id0", "0");
				map.put("id1", "0");
			} else {
				int i = 0;
				List<String> idList = new ArrayList<String>();
				for (Map<String, Object> map2 : list) {
					idList.add(StringUtilsLocal.valueOf(map2.get("ID")));
				}
				List<Map<String, Object>> lists = measureDao
						.geMonthtMeasureValueLastMon("(" + StringUtilsLocal.listToSqlIn(idList) + ")");
				for (String id : ids) {
					if(measureIds.contains(id)){
						for (Map<String, Object> map2 : lists) {
							String parameterId = StringUtilsLocal.valueOf(map2.get("MEASURE_ID"));
							if(parameterId.equals(id)) {
								map.put("id" + i, StringUtilsLocal.valueOf(map2.get("MEASURE_ID")));
								String val = StringUtilsLocal.valueOf(map2.get("value"));
								if (StringUtilsLocal.isBlank(val)) {
									val = "0.0";
								}
								map.put("value" + StringUtilsLocal.valueOf(map2.get("MEASURE_ID")), val);
							}
						}
					}
					if(StringUtilsLocal.isBlank(StringUtilsLocal.valueOf(map.get("id" + i)))) {
						map.put("id" + i, "0");
					}
					i++;
				}
				
			}
			ret.add(map);
		}
		return ret;
	}
	@Deprecated
	private List<Map<String, Object>> getTestEfficientByMeasure(String proNos, String id, int projectNum) 	{
		List<Map<String, Object>> ret = new ArrayList<>();
		String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		List<Map<String, Object>> list = measureDao.getMeasuresByProjectAndMeasure("(" + proNos + ")",
				"('" + id + "')");
		List<String> idList = new ArrayList<String>();
		String name = "";
		String upper = "";
		String lower = "";
		String target = "";
		String unit = "";
		if (list.size() > 0) {
			for (Map<String, Object> map : list) {
				idList.add(StringUtilsLocal.valueOf(map.get("ID")));
				name = StringUtilsLocal.valueOf(map.get("name"));
				upper = StringUtilsLocal.valueOf(map.get("upper"));
				lower = StringUtilsLocal.valueOf(map.get("lower"));
				target = StringUtilsLocal.valueOf(map.get("target"));
				unit = StringUtilsLocal.valueOf(map.get("UNIT"));
			}
			List<Map<String, Object>> lists = measureDao
					.geMonthtMeasureValueSum("(" + StringUtilsLocal.listToSqlIn(idList) + ")");
			for (Map<String, Object> map : lists) {
				String sum = StringUtilsLocal.valueOf(map.get("sum"));
				if (StringUtilsLocal.isBlank(sum)) {
					sum = "0";
				}
				Double count = Double.parseDouble(sum) / projectNum;
				map.put("num", StringUtilsLocal.keepTwoDecimals(count));
				map.put("name", name);
				ret.add(map);
			}
		}
		boolean falg;
		String year = DateUtils.getSystemYear();
		String mon = DateUtils.getSystemMonth();
		mon = mon.substring(mon.length() - 2);
		for (String month : months) {
			falg = true;
			for (Map<String, Object> map : ret) {
				String monthMap = StringUtilsLocal.valueOf(map.get("month"));
				if (monthMap.endsWith(month)) {
					falg = false;
					break;
				}
			}
			if (falg) {
				Map<String, Object> map = new HashMap<>();
				map.put("num", 0.0);
				map.put("month", year + "-" + month);
				map.put("name", name);
				ret.add(map);
			}
		}
		for (Map<String, Object> map : ret) {
			String monthMap = StringUtilsLocal.valueOf(map.get("month"));
			if (monthMap.endsWith(mon)) {
				map.put("nowDate", "true");
				map.put("upper", upper);
				map.put("lower", lower);
				map.put("target", target);
				map.put("unit", unit);
				map.put("projectNum", projectNum);
			}
		}
		return ret;
	}
	
	
	
	/**
	 * 保存pcb样本项目配置 
	 * @param list
	 */
	public void editPcbProjectConfig(List<Map<String, Object>> list){
		pcbListDao.editPcbProjectConfig(list);					
	}	
	public void insertPcbProjectConfig(List<Map<String, Object>> list) {
		pcbListDao.insertPcbProjectConfig(list);
	}
	/**
	 * 根据parameter_info_new的id和项目编号及pcbCategory，获取project_parameter_value_new对应的每个月的数据汇总，返回
	 * @param projectInfo
	 * @param username
	 * @param ids
	 * @return
	 */
	public List<Object> getPcbValuesByMouth(ProjectInfo projectInfo, String username, String[] ids) {
		Set<String> nos = projectInfoService.projectNos(projectInfo, username);
		List<String> proNos = new ArrayList<>(nos);
		List<Object> ret = new ArrayList<>();
		if (proNos.size() <= 0) {
			return ret;
		}

		for (String id : ids) {
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("proNos", proNos);
			parameter.put("measureId", id);
			parameter.put("pcbCategory", projectInfo.getPcbCategory());
			List<String> proNos1 = pcbListDao.queryPcbProjectConfigList(parameter);
			int projectNum = proNos1.size();
			ret.add(getPcbValues(StringUtilsLocal.listToSqlIn(proNos1), id, projectNum));
		}
		return ret;
	}
	
	private List<Map<String, Object>> getPcbValues(String proNos, String id, int projectNum) {
		List<Map<String, Object>> ret = new ArrayList<>();
		List<Map<String, Object>> listSelect = new ArrayList<>();
		
		//Java获取当前月份的前12个月，获取最近的12个月
		String dateString = null;
		String startDate = null;
		String endDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		dateString = sdf.format(cal.getTime());
		List<String> rqList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			dateString = sdf.format(cal.getTime());
			System.out.print(dateString + " ");
			rqList.add(dateString.substring(0, 7));
			//xfzeList.add(xfze);
			cal.add(Calendar.MONTH, -1);
		}
		// 倒序
		Collections.reverse(rqList);
		for(int i=0;i<rqList.size();i++){
			System.out.print(rqList.get(i) + " ");
		}
		//存入list集合
		ArrayList<Object> list1 = new ArrayList<>();
		list1.add(rqList.get(0));
		list1.add(rqList.get(1));
		list1.add(rqList.get(2));
		list1.add(rqList.get(3));
		list1.add(rqList.get(4));
		list1.add(rqList.get(5));
		list1.add(rqList.get(6));
		list1.add(rqList.get(7));
		list1.add(rqList.get(8));
		list1.add(rqList.get(9));
		list1.add(rqList.get(10));
		list1.add(rqList.get(11));
		//Java获取当前月份的前12个月的startDate月份值
		startDate = (String) list1.get(0);
		endDate = (String) list1.get(11);
		
		String[] months = {(String) list1.get(0),(String) list1.get(1),(String) list1.get(2),(String) list1.get(3),(String) list1.get(4),(String) list1.get(5),
				(String) list1.get(6),(String) list1.get(7),(String) list1.get(8),(String) list1.get(9),(String) list1.get(10),(String) list1.get(11)};
		
		List<Map<String, Object>> list = jobPcbDao.getParameterValueGroup("(" + proNos + ")",id, startDate, endDate);
		Map<String, Object> parameterInfo = jobPcbDao.getMeasureInfo(id);
		List<String> idList = new ArrayList<String>();
		String name = StringUtilsLocal.valueOf(parameterInfo.get("name"));
		String upper = StringUtilsLocal.valueOf(parameterInfo.get("UPPER"));
		String lower = StringUtilsLocal.valueOf(parameterInfo.get("LOWER"));
		String target = StringUtilsLocal.valueOf(parameterInfo.get("TARGET"));
		String unit = StringUtilsLocal.valueOf(parameterInfo.get("UNIT"));
		if (list!=null && list.size() > 0) {
			for (String month : months) {
				Map<String, Object> retMap  = new HashMap<>();
				List<Double> vals = new ArrayList<>();
				for (Map<String, Object> map : list) {
					String monthMap = StringUtilsLocal.valueOf(map.get("month"));
					if (monthMap.endsWith(month)) {
						vals.add(StringUtilsLocal.parseDouble(map.get("measure_value")));
					}
				}
				int sample  = 0;
				Double sum = 0.0;
				if(vals.size()<7){
					for (Map<String, Object> map : list) {
						String monthMap = StringUtilsLocal.valueOf(map.get("month"));
						if (monthMap.endsWith(month)) {
							Double value = StringUtilsLocal.parseDouble(map.get("measure_value"));
							sum += value;
							sample++;
						}
					}
				}else{
					Double LCL = 0.0;
					Double UCL = 0.0;
					if(vals.size()>0){
						Double average = MathUtils.getAverage(vals);
						Double sigma = MathUtils.getSigma(vals,average);
						LCL = average-sigma*2;
						UCL = average+sigma*2;
						if(LCL < 0.0 && !"200".equals(id)){
							LCL = 0.0;
						}
					}

					for (Map<String, Object> map : list) {
						String monthMap = StringUtilsLocal.valueOf(map.get("month"));
						if (monthMap.endsWith(month)) {
							Double value = StringUtilsLocal.parseDouble(map.get("measure_value"));
							if(UCL >= value && LCL <= value){
								sum += value;
								sample++;
							}
						}
					}
				}


				Double count = 0.0;
				if(sample!=0){
					count = sum / sample;
				}

				retMap.put("num", StringUtilsLocal.keepTwoDecimals(count));
				retMap.put("name", name);
				retMap.put("month", month);
				retMap.put("projectNum", sample);
				listSelect.add(retMap);
			}

		}
		boolean falg;
		String mon = DateUtils.getSystemMonth();
		mon = mon.substring(mon.length() - 2);
		for (String month : months) {
			falg = true;
			for (Map<String, Object> map : listSelect) {
				String monthMap = StringUtilsLocal.valueOf(map.get("month"));
				if (monthMap.endsWith(month)) {
					ret.add(map);
					falg = false;
					break;
				}
			}
			if (falg) {
				Map<String, Object> map = new HashMap<>();
				map.put("num", 0.0);
				map.put("month", month);
				map.put("name", name);
				ret.add(map);
			}
		}
		for (Map<String, Object> map : ret) {
			String monthMap = StringUtilsLocal.valueOf(map.get("month"));
			if (monthMap.endsWith(mon)) {
				map.put("nowDate", "true");
				map.put("upper", upper);
				map.put("lower", lower);
				map.put("target", target);
				map.put("unit", unit);
				map.put("projectNum", map.get("projectNum")==null?0:map.get("projectNum"));
				map.put("id", id);
			}
		}
		return ret;
	}
	/**
	 * 根据项目编号导出研发效率信息
	 */
	public byte[] exportResearch(String no) {
		InputStream is = this.getClass().getResourceAsStream("/excel/research-efficiency.xlsx");
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
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        /************************获取表格头***************************/
		String category = "研发效率";
		List<Map<String, Object>> list = pcbListDao.exportResearch("("+no+")",category);
		String Index = null;
		int i = 0;
		ArrayList<Integer> arr=new ArrayList<Integer>();
        ArrayList<Integer> arrj=new ArrayList<Integer>();
        ArrayList<Integer> arri=new ArrayList<Integer>();
        for (Map<String, Object> map : list) {
        	row = sheet.createRow(i+1);
            Object obj = map.get("nam");
			String name = obj == null ? "" : String.valueOf(obj);
			obj = map.get("pdu");
			String pdu = obj == null ? "" : String.valueOf(obj);
			if(i>0 && name.equals(list.get(i-1).get("nam"))) {
				cell = row.createCell(0);
			    cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(1);
			    cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				if(i==1) {
					arr.add(0);
					arr.add(1);
				}else {arr.add(i);}
			}else{
				if(i>1 && arr.size() > 1) {
					arrj.add(arr.get(0)+1);
					arri.add(arr.get(arr.size()-1)+1);
					arr.clear();
				}
				cell = row.createCell(0);
			    cell.setCellValue(pdu);
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(1);
			    cell.setCellValue(name);
				cell.setCellStyle(cellStyle);
			}
			obj = map.get("ids");
			String id = obj == null ? "" : String.valueOf(obj);
			if("237".equals(id)) {
				Index = "JAVA端到端生产率";
			}else {
				Index = "C++端到端生产率";
			}
			obj = map.get("target");
			String target = obj == null ? "" : String.valueOf(obj);
			obj = map.get("upp");
			String upper = obj == null ? "" : String.valueOf(obj);
			obj = map.get("low");
			String lower = obj == null ? "" : String.valueOf(obj);
			obj = map.get("var");
			String value = obj == null ? "" : String.valueOf(obj);
			cell = row.createCell(2);
		    cell.setCellValue(Index);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(3);
		    cell.setCellValue(target);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(4);
		    cell.setCellValue(lower);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(5);
		    cell.setCellValue(upper);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(6);
		    cell.setCellValue(value);
		    cell.setCellStyle(cellStyle);
			i++;
		}
        for(int m = 0;m < arrj.size(); m++ ) {
        	sheet.addMergedRegion(new CellRangeAddress(arrj.get(m),arri.get(m), 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(arrj.get(m),arri.get(m), 1, 1));
		}
        sheet.addMergedRegion(new CellRangeAddress(arr.get(0)+1,arr.get(arr.size()-1)+1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(arr.get(0)+1,arr.get(arr.size()-1)+1, 1, 1));
        return returnWb2Byte(wb);
	}
	/**
	 * 根据项目编号导出研发效率信息
	 */
	public byte[] exportIteration(String no) {
		InputStream is = this.getClass().getResourceAsStream("/excel/research-efficiency.xlsx");
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
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        /************************获取表格头***************************/
		String category = "迭代效率";
		List<Map<String, Object>> list = pcbListDao.exportResearch("("+no+")",category);
		String Index = null;
		int i = 0;
		ArrayList<Integer> arr=new ArrayList<Integer>();
        ArrayList<Integer> arrj=new ArrayList<Integer>();
        ArrayList<Integer> arri=new ArrayList<Integer>();
        for (Map<String, Object> map : list) {
        	row = sheet.createRow(i+1);
            Object obj = map.get("nam");
			String name = obj == null ? "" : String.valueOf(obj);
			obj = map.get("pdu");
			String pdu = obj == null ? "" : String.valueOf(obj);
			if(i>0 && name.equals(list.get(i-1).get("nam"))) {
				cell = row.createCell(0);
			    cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(1);
			    cell.setCellValue("");
				cell.setCellStyle(cellStyle);
				if(i==1) {
					arr.add(0);
					arr.add(1);
				}else {arr.add(i);}
			}else{
				if(i>1 && arr.size() > 1) {
					arrj.add(arr.get(0)+1);
					arri.add(arr.get(arr.size()-1)+1);
					arr.clear();
				}
				cell = row.createCell(0);
			    cell.setCellValue(pdu);
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(1);
			    cell.setCellValue(name);
				cell.setCellStyle(cellStyle);
			}
			obj = map.get("ids");
			String id = obj == null ? "" : String.valueOf(obj);
			if("254".equals(id)) {
				Index = "JAVA迭代生产率";
			}else if("255".equals(id)){
				Index = "C/C++迭代生产率";
			}else if("256".equals(id)){
				Index = "迭代测试用例设计效率";
			}else if("257".equals(id)){
				Index = "迭代测试用例执行效率";
			}else if("258".equals(id)){
				Index = "自动化用例写作效率";
			}else if("259".equals(id)){
				Index = "自动化脚本连跑通过率";
			}
			obj = map.get("target");
			String target = obj == null ? "" : String.valueOf(obj);
			obj = map.get("upp");
			String upper = obj == null ? "" : String.valueOf(obj);
			obj = map.get("low");
			String lower = obj == null ? "" : String.valueOf(obj);
			obj = map.get("var");
			String value = obj == null ? "" : String.valueOf(obj);
			cell = row.createCell(2);
		    cell.setCellValue(Index);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(3);
		    cell.setCellValue(target);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(4);
		    cell.setCellValue(lower);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(5);
		    cell.setCellValue(upper);
			cell.setCellStyle(cellStyle);
			cell = row.createCell(6);
		    cell.setCellValue(value);
		    cell.setCellStyle(cellStyle);
			i++;
		}
        for(int m = 0;m < arrj.size(); m++ ) {
        	sheet.addMergedRegion(new CellRangeAddress(arrj.get(m),arri.get(m), 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(arrj.get(m),arri.get(m), 1, 1));
		}
        sheet.addMergedRegion(new CellRangeAddress(arr.get(0)+1,arr.get(arr.size()-1)+1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(arr.get(0)+1,arr.get(arr.size()-1)+1, 1, 1));
        return returnWb2Byte(wb);
	}
	/**
	 * 根据parameter_info_new的id和项目编号及pcbCategory，获取project_parameter_value_new对应的最新月每个项目的数据，返回
	 * @param projectInfo
	 * @param username
	 * @param ids
	 * @param page
	 * @return
	 */
	public PageResponse<Map<String, Object>> getPcbValuesToday(ProjectInfo projectInfo, String username, String[] ids, PageRequest pager) {
		if (pager == null) {
            pager = new PageRequest();
        }
		//根据页面条件查询匹配的项目No
		Set<String> nos = projectInfoService.projectNos(projectInfo, username);
		List<Map<String, Object>> ret = new ArrayList<>();
		List<String> proNos = new ArrayList<>(nos);
		
		PageResponse<Map<String, Object>> result = new PageResponse<>();
		if (proNos.size() <= 0) {
			result.setTotalCount(0);
			result.setData(ret);
			result.setPageSize(pager.getPageSize());
	        result.setPageNumber(pager.getPageNumber());
			return result;
		}else {
			int total = pcbListDao.queryPcbProjectConfigCount(proNos, projectInfo.getPcbCategory());
			if(total <= 0) {
				result.setTotalCount(0);
				result.setData(ret);
				result.setPageSize(pager.getPageSize());
		        result.setPageNumber(pager.getPageNumber());
			}else {
				Map<String,Object> parameter =  new HashMap<>();
				parameter.put("pcbCategory", projectInfo.getPcbCategory());
				parameter.put("proNos", proNos);
				parameter.put("offset", pager.getPageNumber());
	            parameter.put("limit", pager.getPageSize());
	            //根据pcb样本配置条件筛选项目
				proNos = pcbListDao.queryPcbProjectConfigList(parameter);
				List<String> idsList = new ArrayList<>();
				for (String id : ids) {
					idsList.add(id);
				}
				for (String proNo : proNos) {				
					parameter.put("proNo", proNo);
					List<String> measureIds = pcbListDao.queryPcbProjectConfigIdList(parameter);

					ProjectInfoVo infoVo =projectInfoVoDao.fetchProjectInfoByKey(proNo);
					Map<String, Object> map = new HashMap<>();
					map.put("zpdu", infoVo.getHwzpdu());
					map.put("pdu", infoVo.getPduSpdt());
					map.put("name", infoVo.getName());
					map.put("no", infoVo.getNo());
					List<Map<String, Object>> list = jobPcbDao.getProjectMeasure(infoVo.getNo(),"(" + StringUtilsLocal.listToSqlIn(
                            idsList) + ")");
					if (list == null || list.size() <= 0) {
						for (String id : ids) {
							if(!measureIds.contains(id)){
								map.put("id" + id, "");
								continue;
							}
							map.put("id"+id, "0.0");
						}
					} else {
						for (String id : ids) {
							if(!measureIds.contains(id)){
								map.put("id" + id, "");
								continue;
							}
							for (Map<String, Object> parameterValue : list) {
								String parameterId = StringUtilsLocal.valueOf(parameterValue.get("measure_id"));
								if(parameterId.equals(id)) {
									map.put("id" + id, parameterId);
									String val = StringUtilsLocal.valueOf(parameterValue.get("measure_value"));
									if (StringUtilsLocal.isBlank(val)) {
										val = "0.0";
									}
									map.put("id" + id, val);
								}
							}
							if(StringUtilsLocal.isBlank(StringUtilsLocal.valueOf(map.get("id" + id)))) {
								map.put("id" + id, "0.0");
							}

						}
					}
					ret.add(map);
				}
				result.setTotalCount(total);
				result.setData(ret);
				result.setPageSize(pager.getPageSize());
				result.setPageNumber(pager.getPageNumber());
			}
		}
		return result;
	}
	
	
	
	/**
	 * 根据parameter_info_new的id和项目编号及pcbCategory，获取project_parameter_value_new对应的每个月的数据汇总，返回
	 * @param projectInfo
	 * @param username
	 * @param ids
	 * @return
	 */
	public List<Object> getPcbValuesByIteration(ProjectInfo projectInfo, String username, String[] ids) {
		Set<String> nos = projectInfoService.projectNos(projectInfo, username);
		List<String> proNos = new ArrayList<>(nos);
		List<Object> ret = new ArrayList<>();
		if (proNos.size() <= 0) {
			return ret;
		}

		for (String id : ids) {
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("proNos", proNos);
			parameter.put("measureId", id);
			parameter.put("pcbCategory", projectInfo.getPcbCategory());
			List<String> proNos1 = pcbListDao.queryPcbProjectConfigList(parameter);
			int projectNum = proNos1.size();
			ret.add(getPcbValuesByIteration(proNos1, id, projectNum));
		}
		return ret;
	}


	private List<Map<String, Object>> getPcbValuesByIteration(List<String> proNos, String id, int projectNum) {
		List<Map<String, Object>> ret = new ArrayList<>();
		List<Map<String, Object>> listSelect = new ArrayList<>();
		
		//Java获取当前月份的前12个月，获取最近的12个月
		String dateString = null;
		String startDate = null;
		String endDate = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		dateString = sdf.format(cal.getTime());
		List<String> rqList = new ArrayList<>();
		System.out.println("倒序前日期:");
		for (int i = 0; i < 12; i++) {
			dateString = sdf.format(cal.getTime());
			System.out.print(dateString + " ");
			rqList.add(dateString.substring(0, 7));
			//xfzeList.add(xfze);
			cal.add(Calendar.MONTH, -1);
		}
		// 倒序
		Collections.reverse(rqList);
		System.out.println("倒序后日期:");
		for(int i=0;i<rqList.size();i++){
			System.out.print(rqList.get(i) + " ");
		}
		//存入list集合
		ArrayList<Object> list1 = new ArrayList<>();
		list1.add(rqList.get(0));
		list1.add(rqList.get(1));
		list1.add(rqList.get(2));
		list1.add(rqList.get(3));
		list1.add(rqList.get(4));
		list1.add(rqList.get(5));
		list1.add(rqList.get(6));
		list1.add(rqList.get(7));
		list1.add(rqList.get(8));
		list1.add(rqList.get(9));
		list1.add(rqList.get(10));
		list1.add(rqList.get(11));
		//Java获取当前月份的前12个月的startDate月份值
		startDate = (String) list1.get(0);
		endDate = (String) list1.get(11);
		
		String[] months = {(String) list1.get(0),(String) list1.get(1),(String) list1.get(2),(String) list1.get(3),(String) list1.get(4),(String) list1.get(5),
				(String) list1.get(6),(String) list1.get(7),(String) list1.get(8),(String) list1.get(9),(String) list1.get(10),(String) list1.get(11)};
		
		List<Map<String, Object>> list = jobPcbDao.getPcbValuesByItera("(" + StringUtilsLocal.listToSqlIn(proNos) + ")",id, startDate, endDate);
//		Map<String,List<Map<String, Object>>> pcbValueNos =  new HashMap<>();
//		int iterationNum = 0;

		Map<String, Object> parameterInfo = jobPcbDao.getMeasureInfo(id);
		List<String> idList = new ArrayList<String>();
		String name = StringUtilsLocal.valueOf(parameterInfo.get("name"));
		String upper = StringUtilsLocal.valueOf(parameterInfo.get("UPPER"));
		String lower = StringUtilsLocal.valueOf(parameterInfo.get("LOWER"));
		String target = StringUtilsLocal.valueOf(parameterInfo.get("TARGET"));
		String unit = StringUtilsLocal.valueOf(parameterInfo.get("UNIT"));
		
		String mon = DateUtils.getSystemMonth();
		mon = mon.substring(mon.length()-2);

		Double newSum = 0.0;
		int newSample = 0;
		if (list!=null && list.size() > 0) {
			for (String month : months) {
				Map<String, Object> retMap = new HashMap<>();
				Double sum = 0.0;
				int sample = 0;
				if(Integer.valueOf(month) == Integer.valueOf(mon)){
					List<Double> vals = new ArrayList<>();
					for (Map<String, Object> map : list) {
						if( map.get("measure_value")!=null) {
							Double value = StringUtilsLocal.parseDouble(map.get("measure_value"));
							vals.add(value);
						}
					}
					if(vals.size()<7){
						for (Map<String, Object> map : list) {
							if( map.get("measure_value")!=null){
								sum += StringUtilsLocal.parseDouble(map.get("measure_value"));
								sample++;
							}
						}
					}else{
						Double LCL = 0.0;
						Double UCL = 0.0;
						if(vals.size()>0){
							Double average = MathUtils.getAverage(vals);
							Double sigma = MathUtils.getSigma(vals,average);
							LCL = average-sigma*2;
							UCL = average+sigma*2;
							if(LCL < 0.0 && !"200".equals(id)){
								LCL = 0.0;
							}
						}

						for (Map<String, Object> map : list) {
							if( map.get("measure_value")!=null){
								Double value = StringUtilsLocal.parseDouble(map.get("measure_value"));
								if(UCL >= value && LCL <= value){
									sum += value;
									sample++;
								}
							}
						}
					}
					newSum = sum;
					newSample = sample;
				}else if(Integer.valueOf(month) > Integer.valueOf(mon)){
					sum = newSum;
					sample = newSample;
				}else{
					List<Double> vals = new ArrayList<>();
					for (Map<String, Object> map : list) {
						if( map.get("measure_value") == null){
							continue;
						}
						String date = DateUtils.format2.format((Date) map.get("plan_end_date"));
						date = date.substring(date.length() - 2);
						if (Integer.valueOf(month) >= Integer.valueOf(date)) {
							Double value = StringUtilsLocal.parseDouble(map.get("measure_value"));
							vals.add(value);
						}
					}
					if(vals.size()<7){
						for (Map<String, Object> map : list) {
							if( map.get("measure_value") == null){
								continue;
							}
							String date = DateUtils.format2.format((Date) map.get("plan_end_date"));
							date = date.substring(date.length() - 2);
							if (Integer.valueOf(month) >= Integer.valueOf(date)) {
								sum += StringUtilsLocal.parseDouble(map.get("measure_value"));
								sample++;
							}
						}
					}else {
						Double LCL = 0.0;
						Double UCL = 0.0;
						if (vals.size() > 0) {
							Double average = MathUtils.getAverage(vals);
							Double sigma = MathUtils.getSigma(vals, average);
							LCL = average - sigma * 2;
							UCL = average + sigma * 2;
							if (LCL < 0.0 && !"200".equals(id)) {
								LCL = 0.0;
							}
						}

						for (Map<String, Object> map : list) {
							if(map.get("measure_value") == null){
								continue;
							}
							String date = DateUtils.format2.format((Date) map.get("plan_end_date"));
							date = date.substring(date.length() - 2);
							if (Integer.valueOf(month) >= Integer.valueOf(date)) {
								Double value = StringUtilsLocal.parseDouble(map.get("measure_value"));
								if (UCL >= value && LCL <= value) {
									sum += value;
									sample++;
								}
							}
						}
					}
				}
				Double count = 0.0;
				if (sample != 0){
					count= sum / sample;
				}
				retMap.put("num", StringUtilsLocal.keepTwoDecimals(count));
				retMap.put("name", name);
				retMap.put("month", month);
				retMap.put("sample", sample);
				listSelect.add(retMap);
			}
		}
		boolean falg;

		for (String month : months) {
			falg = true;
			for (Map<String,Object> map : listSelect) {
				String monthMap = StringUtilsLocal.valueOf(map.get("month"));
				if(monthMap.endsWith(month)) {
					ret.add(map);
					falg = false;
					break;
				}
			}
			if(falg) {
				Map<String,Object> map = new HashMap<>();
				map.put("num", 0.0);
				map.put("month", month);
				map.put("name", name);
				ret.add(map);
			}
		}
		for (Map<String,Object> map : ret) {
			String monthMap = StringUtilsLocal.valueOf(map.get("month"));
			if(monthMap.endsWith(mon)) {
				map.put("nowDate", "true");
				map.put("upper", upper);
				map.put("lower", lower);
				map.put("target", target);
				map.put("projectNum", map.get("sample")==null?0:map.get("sample"));
				map.put("id", id);
			}
		}
		return ret;
	}

	/**
	 * 根据parameter_info_new的id和项目编号及pcbCategory，获取project_parameter_value_new对应的最新月每个项目的数据，返回
	 * @param projectInfo
	 * @param username
	 * @param ids
	 * @param pcbCategory
	 * @param page
	 * @return
	 */
	public PageResponse<Map<String, Object>> getPcbValuesTodayMeasure(ProjectInfo projectInfo, String username,String[] ids, PageRequest pager) {
		if (pager == null) {
            pager = new PageRequest();
        }
		//根据页面条件查询匹配的项目No
		Set<String> nos = projectInfoService.projectNos(projectInfo, username);
		List<Map<String, Object>> ret = new ArrayList<>();
		List<String> proNos = new ArrayList<>(nos);
		PageResponse<Map<String, Object>> result = new PageResponse<>();
		if (proNos.size() <= 0) {
			result.setTotalCount(0);
			result.setData(ret);
			result.setPageSize(pager.getPageSize());
	        result.setPageNumber(pager.getPageNumber());
			return result;
		}else {
			int total = pcbListDao.queryPcbProjectConfigCount(proNos, projectInfo.getPcbCategory());
			if(total <= 0) {
				result.setTotalCount(0);
				result.setData(ret);
				result.setPageSize(pager.getPageSize());
		        result.setPageNumber(pager.getPageNumber());
			}else {
				Map<String,Object> parameter =  new HashMap<>();
				parameter.put("pcbCategory", projectInfo.getPcbCategory());
				parameter.put("proNos", proNos);
				parameter.put("offset", pager.getPageNumber());
	            parameter.put("limit", pager.getPageSize());
	            //根据pcb样本配置条件筛选项目
				proNos = pcbListDao.queryPcbProjectConfigList(parameter);
				List<String> idsList = new ArrayList<>();
				for (String id : ids) {
					idsList.add(id);
				}
				for (String proNo : proNos) {
					parameter.put("proNo", proNo);
					List<String> measureIds= pcbListDao.queryPcbProjectConfigIdList(parameter);

					ProjectInfoVo infoVo =projectInfoVoDao.fetchProjectInfoByKey(proNo);
					Map<String, Object> map = new HashMap<>();
					map.put("zpdu", infoVo.getHwzpdu());
					map.put("pdu", infoVo.getPduSpdt());
					map.put("name", infoVo.getName());
					map.put("no", infoVo.getNo());
					List<Map<String, Object>> list = jobPcbDao.getProjectMeasure(infoVo.getNo(),"(" + StringUtilsLocal.listToSqlIn(
                            idsList) + ")");
					if (list == null || list.size() <= 0) {
						for (String id : ids) {
							if(!measureIds.contains(id)){
								map.put("id" + id, "");
								continue;
							}
							map.put("id"+id, "0.0");
						}
					} else {
						for (String id : ids) {
							if(!measureIds.contains(id)){
								map.put("id" + id, "");
								continue;
							}
							for (Map<String, Object> parameterValue : list) {
								String parameterId = StringUtilsLocal.valueOf(parameterValue.get("measure_id"));
								if(parameterId.equals(id)) {
									map.put("id" + id, parameterId);
									String val = StringUtilsLocal.valueOf(parameterValue.get("measure_value"));
									if (StringUtilsLocal.isBlank(val)) {
										val = "0.0";
									}
									map.put("id" + id, val);
								}
							}
							if(StringUtilsLocal.isBlank(StringUtilsLocal.valueOf(map.get("id" + id)))) {
								map.put("id" + id, "0.0");
							}
						}
					}
					ret.add(map);
				}
				result.setTotalCount(total);
				result.setData(ret);
				result.setPageSize(pager.getPageSize());
				result.setPageNumber(pager.getPageNumber());
			}			
		}
		return result;
	}
	
	public Integer updateParameterInfoById(ParameterInfoNew parameterInfoNew) {
		return jobPcbDao.updateParameterInfoById(parameterInfoNew);
	}
	   /**
     * 单元格样式
     * @param wb
     * @return
     */
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
    public byte[] returnWb2Byte(Workbook wb){
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

	
}
