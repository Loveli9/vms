package com.icss.mvp.service;

import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.MeasureCommentDao;
import com.icss.mvp.dao.ProjectInfoVoDao;
import com.icss.mvp.dao.ProjectReportDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 安瑜东
 */
@Service("measureCommentService")
@Transactional
public class MeasureCommentService {

    @Resource
    private HttpServletRequest request;

    private static Logger logger = Logger.getLogger(MeasureCommentService.class);

    @Resource
    ProjectInfoService projectInfoService;

    @Autowired
    private MeasureCommentDao measureCommentDao;

    @Autowired
    private IterativeWorkManageService iterativeServices;

    @Resource
    IUserManagerDao userManagerDao;

    @Autowired
    private ProjectReportDao projectReportDao;

    @Autowired
    private ProjectInfoVoDao projectInfoVoDao;

    /**
     * 保存指标点评信息
     *
     * @param measureComment
     */
    public void saveMeasureComment(MeasureComment measureComment) {
        measureCommentDao.saveMeasureComment(measureComment);
    }

    /**
     * 新增指标值
     *
     * @param meaInfo
     */
    public void insert(MeasureLoadEveryInfo meaInfo) {
        measureCommentDao.insert(meaInfo);
    }

    /**
     * 查询指标点评信息列表
     *
     * @param parameter
     * @param flag      是否是当前周期
     * @return
     */
    public List<Map<String, Object>> queryCommentList(Map<String, Object> parameter, boolean flag) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<MeasureComment> measureComments = null;
        if (flag) {
            measureComments = measureCommentDao.queryCurrentCommentList(parameter);
        } else {
            String ms = String.valueOf(parameter.get("measureIds"));
            String[] measureIds = ms.substring(1, ms.length() - 1).split(",");
            Set<String> warnings = new HashSet<>();
            Set<String> others = new HashSet<>();
            for (String measureId : measureIds) {
                if ("303".equals(measureId) || "305".equals(measureId) || "306".equals(measureId)) {
                    warnings.add(measureId);
                } else {
                    others.add(measureId);
                }
            }
            parameter.put("warnings", warnings);
            parameter.put("others", others);
            measureComments = measureCommentDao.queryHistoryCommentList(parameter);
        }
        Map<String, Integer> measureCategorys = new HashMap<>();
        Set<String> parentIds = new HashSet<>();
        int num = 1;
        String tmpPID = "";
        int cnt = 1;
        String proNo = parameter.get("proNo") + "";
        String measureId = "";
        for (MeasureComment measureComment : measureComments) {
            measureId = measureComment.getMeasureId() + "";
            if (flag) {
                // 获取指标上次修改记录
                String value = measureCommentDao.getHistoryValue(proNo, measureId);
                if (StringUtils.isNotEmpty(value)) {
                    if (StringUtils.isEmpty(measureComment.getComment())) {
                        measureComment.setComment(setTips(value, measureComment.getMeasureValue()));
                    }
                }
            }
            if (!parentIds.contains(measureComment.getLabelTitle() + "")) {
                parentIds.add(measureComment.getLabelTitle() + "");
                Map<String, Object> categorys = new HashMap<>();
                categorys.put("id", measureComment.getLabelId());
                categorys.put("measureName", measureComment.getLabelTitle());
                categorys.put("measureNo", measureComment.getLabelTitle());
                categorys.put("pid", 0);
                categorys.put("level", 0);
                result.add(categorys);
            }
            if (!measureCategorys.containsKey(measureComment.getLabelTitle() + measureComment.getMeasureCategory())) {
                measureCategorys.put(measureComment.getLabelTitle() + measureComment.getMeasureCategory(), num);
                Map<String, Object> categorys = new HashMap<>();
                categorys.put("id", num);
                categorys.put("measureName", measureComment.getMeasureCategory());
                categorys.put("measureNo", measureComment.getLabelTitle());
                categorys.put("line1", measureComment.getMeasureCategory());
                categorys.put("collect", "auto");
                categorys.put("pid", 0);
                categorys.put("level", 1);
                num++;
            }
            if (!measureCategorys.get(
                    measureComment.getLabelTitle() + measureComment.getMeasureCategory()).toString().equalsIgnoreCase(
                    tmpPID)) {
                tmpPID = measureCategorys.get(
                        measureComment.getLabelTitle() + measureComment.getMeasureCategory()).toString();
                cnt = 1;
            }
            String measureNo = tmpPID + '.' + cnt;
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Object> tmpMap = new HashMap<>();

            map1.put("id", measureComment.getMeasureId());
            tmpMap.put("target",
                    StringUtilsLocal.isBlank(measureComment.getTarget()) ? "-" : measureComment.getTarget());
            tmpMap.put("lower", StringUtilsLocal.isBlank(measureComment.getLower()) ? "-" : measureComment.getLower());
            tmpMap.put("upper", StringUtilsLocal.isBlank(measureComment.getUpper()) ? "-" : measureComment.getUpper());
            tmpMap.put("unit", measureComment.getUnit());
            tmpMap.put("measureName", measureComment.getMeasureName());
            tmpMap.put("computeRule", measureComment.getComputeRule());
            map1.put("measureCategory", measureComment.getMeasureCategory());
            map1.put("line1", tmpMap);// measureComment.getOldValue()
            if (flag && null == measureComment.getMeasureValue()) {
                measureComment.setMeasureValue(measureComment.getOldValue());
            }
            Map<String, Object> realValue = new HashMap<>();
            realValue.put("measureValue", measureComment.getMeasureValue());
            realValue.put("light", lightMeasureComment(measureComment, flag));
            map1.put("realValue", realValue);
            map1.put("comment", measureComment.getComment());
            map1.put("oldValue", measureComment.getOldValue());
            map1.put("changeValue", measureComment.getChangeValue());
            map1.put("measureNo", measureNo);
            if ("手工录入".equals(measureComment.getCollectType())) {
                map1.put("collect", "manual");
            } else {
                map1.put("collect", "auto");
            }
            map1.put("pid", measureCategorys.get(measureComment.getLabelTitle() + measureComment.getMeasureCategory()));
            map1.put("level", 2);
            cnt++;
            // 获取该指标所属过程指标
            List<MeasureProcess> MeasureProcesses = measureCommentDao.queryProcessMeasure(
                    String.valueOf(measureComment.getMeasureId()));
            if (MeasureProcesses != null && MeasureProcesses.size() > 0) {
                map1.put("children", 1);
            }
            result.add(map1);
        }
        return result;
    }

    public List<MeasureComment> queryMeasureList(String proNo, String startDate, String endDate) {
        String measureIds = measureCommentDao.measureConfigurationRecord(proNo, endDate);
        List<MeasureComment> measureComments = new ArrayList<MeasureComment>();
        if (null != measureIds && !"".equals(measureIds)) {
            measureComments = measureCommentDao.queryMeasureList(proNo, measureIds.split(","), startDate, endDate);
        }
        return measureComments;
    }

    private Map<String, String> lightMeasureComment(MeasureComment measureComment, boolean flag) {
        String light = null;
        if (flag) {
            light = MeasureUtils.light(measureComment);
        } else {
            light = measureComment.getLight();
        }

        if (StringUtils.isNotBlank(light)) {
            Double measureValue = null;
            Double oldValue = null;
            Double trendValue = null;

            if (measureComment.getMeasureId() == 314) {// CI全量回归
                measureValue = MeasureUtils.dealHour(measureComment.getMeasureValue());
                oldValue = MeasureUtils.dealHour(measureComment.getOldValue()) == null ? 0.0 : MeasureUtils.dealHour(
                        measureComment.getOldValue());
            } else {
                measureValue = MeasureUtils.deal(measureComment.getMeasureValue());
                oldValue = MeasureUtils.deal(measureComment.getOldValue()) == null ? 0.0 : MeasureUtils.deal(
                        measureComment.getOldValue());
            }

            light += trend(measureValue, oldValue, flag);
            trendValue = measureValue >= oldValue ? measureValue - oldValue : oldValue - measureValue;
            Map<String, String> result = new HashMap<String, String>();
            result.put("light", light);
            result.put("value", trendValue.toString());
            return result;
        } else {
            return null;
        }
    }

    private String trend(Double measureValue, Double oldValue, boolean flag) {
        String trend = "";
        if (flag) {
            if (measureValue > oldValue) {
                trend = "upper";
            } else if (measureValue.equals(oldValue)) {
                trend = "equal";
            } else if (measureValue < oldValue) {
                trend = "lower";
            }
        } else {
            trend = "equal";
        }
        return trend;
    }

    public String setTips(String value, String measureValue) {
        String tips = "";
        Double v1 = NumberUtil.stod(null == value ? "" : value.trim());
        Double v2 = NumberUtil.stod(null == measureValue ? "" : measureValue.trim());
        if ((v2 > (v1 + v1 * 0.2)) || v2 < (v1 - v1 * 0.2)) {
            tips = "-10";
        }
        return tips;
    }

    /**
     * 保存过程数据
     *
     * @param proNo
     * @param middleId
     * @param middleValue
     * @param measures
     * @param date
     */
    public void saveMeasureProcessData(String proNo, String middleId, String middleValue,
                                       List<Map<String, String>> measures, String date) {
        Date endDate = DateUtils.parseDate(date, "yyyy-MM-dd");
        MeasureLoadEveryInfo middleInfo = new MeasureLoadEveryInfo(proNo, endDate, middleId, middleValue);
        insert(middleInfo);
        Set<String> measureIds = new HashSet<>();
        for (Map<String, String> map : measures) {
            String id = String.valueOf(map.get("measureId"));
            MeasureLoadEveryInfo processInfo = new MeasureLoadEveryInfo(proNo, endDate, id,
                    String.valueOf(map.get("measureValue")));
            insert(processInfo);
            String mis = measureCommentDao.queryMeasureId(Integer.valueOf(id));
            measureIds.addAll(CollectionUtilsLocal.splitToList(mis));
        }
        measureIds.remove(middleId);
        if (measureIds.size() > 0) {
            calculateProcess(proNo, getStartDate(date), date, measureIds);
        }
    }

    /**
     * 过程指标变更后新指标计算
     *
     * @param proNo
     * @param startDate
     * @param endDate
     * @param measureIds
     * @param
     */
    private void calculateProcess(String proNo, String startDate, String endDate, Set<String> measureIds) {
        for (String m : measureIds) {
            List<String> pids = measureCommentDao.querySameProcessMeasures(m);
            Map<String, String> map = new HashMap<>();
            for (String pid : pids) {
                map.put(pid, measureCommentDao.queryNewestValue(proNo, pid, startDate, endDate));
            }
            String s = "";
            String unit = measureCommentDao.queryUnit(Integer.valueOf(m));
            if ("363".equals(m)) {// 问题单回归不通过率
                s = division(map.get("10001"), map.get("10083"), unit);
            } else if ("371".equals(m)) {// 资料测试缺陷密度
                s = division(map.get("10064"), map.get("10100"), unit);
            } else if ("366".equals(m)) {// 测试用例设计
                s = addition(
                        map.get("10068") + map.get("10069") + map.get("10070") + map.get("10071") + map.get("10072")
                                + map.get("10073"));
            } else if ("367".equals(m)) {// 手工用例执行
                s = map.get("10074");
            } else if ("356".equals(m)) {// HLT自动化率-SQL
                s = division(map.get("10049"), map.get("10096"), unit);
            } else if ("357".equals(m)) {// HLT自动化率-OM
                s = division(map.get("10050"), map.get("10097"), unit);
            } else if ("358".equals(m)) {// HLT自动化率-专项
                s = division(map.get("10051"), map.get("10098"), unit);
            } else if ("369".equals(m)) {// 下游客户发现问题加权密度
                Map<String, Double> special = new HashMap<String, Double>();
                special.put(map.get("10037"), 10.0);
                special.put(map.get("10038"), 3.0);
                special.put(map.get("10039"), 1.0);
                special.put(map.get("10040"), 0.1);
                s = division(specialAddition(special), map.get("10099"), unit);
            } else if ("275".equals(m)) {// 资料转测试前Review缺陷密度
                s = division(map.get("10017"), map.get("10095"), unit);
            } else if ("313".equals(m)) {// 代码Review缺陷密度
                s = division(map.get("10041"), map.get("10099"), unit);
            } else if ("326".equals(m)) {// TR5前测试发现缺陷密度
                s = division(map.get("10060"), map.get("10112"), unit);
            } else if ("327".equals(m)) {// TR5前测试发现缺陷比例
                s = division(map.get("10060"), map.get("10009"), unit);
            } else if ("329".equals(m)) {// 测试用例密度
                s = division(map.get("10048"), map.get("10099"), unit);
            } else if ("332".equals(m)) {// TR2/LLD设计文档检视缺陷密度
                s = division(map.get("10013"), map.get("10101"), unit);
            } else if ("341".equals(m)) {// 产品维护问题答复率
                s = division(map.get("10056"), map.get("10075"), unit);
            } else if ("331".equals(m)) {// 结项验收软件缺陷密度
                s = division(map.get("10033"), map.get("10099"), unit);
            }

            MeasureLoadEveryInfo measureLoadEveryInfo = new MeasureLoadEveryInfo(proNo, DateUtils.parseDate(endDate,
                    "yyyy-MM-dd"),
                    String.valueOf(m), s);
            insert(measureLoadEveryInfo);
            Map<String, String> measureMap = new HashMap<>();
            measureMap.put("measureId", String.valueOf(m));
            measureMap.put("measureValue", String.valueOf(s));
        }
    }

    private String specialAddition(Map<String, Double> special) {
        Double ret = 0.0;
        int num = 0;
        for (String parameter : special.keySet()) {
            if (!StringUtilsLocal.isBlank(parameter)) {
                ret += Double.valueOf(parameter) * special.get(parameter);
                num++;
            }
        }
        if (num == 0) {
            return "";
        } else {
            return ret.toString();
        }
    }

    private String addition(String... parameters) {
        Double ret = 0.0;
        int num = 0;
        for (String parameter : parameters) {
            if (!StringUtilsLocal.isBlank(parameter)) {
                ret += Double.valueOf(parameter);
                num++;
            }
        }
        if (num == 0) {
            return "";
        } else {
            return ret.toString();
        }

    }

    /**
     * @param fenzi
     * @param fenmu
     * @param unit
     * @return
     */
    private String division(String fenzi, String fenmu, String unit) {
        if (StringUtilsLocal.isBlank(fenmu) || StringUtilsLocal.isBlank(fenzi)) {
            return "";
        }
        if (Double.valueOf(fenmu).equals(0.0)) {
            return "0.0";
        }
        Double ret = Double.valueOf(fenzi) / Double.valueOf(fenmu);
        if ("%".equals(unit)) {
            ret = ret * 100;
        }
        ret = StringUtilsLocal.keepTwoDecimals(ret);
        return ret.toString();
    }

    /**
     * 导出指标点评信息列表
     *
     * @param parameter
     * @return
     */
    public byte[] qualityReviewExport(Map<String, Object> parameter) {
        InputStream is = this.getClass().getResourceAsStream("/excel/quality-report.xlsx");
        Workbook wb = null;
        try {
            wb = new XSSFWorkbook(is);
        } catch (Exception e) {
            logger.error("查询失败", e);
        }
        String proNo = (String) parameter.get("proNo");
        demandManagement(wb, proNo);
        problemRisk(wb, proNo);
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        CellStyle cellStyle = getCellStyle(wb);
        CellStyle headerStyle = getHeadStyle(wb);
        /********************** 获取数据 **********************************/
        String endDate1 = DateUtils.getLatestCycles(1, true).get(0);
        String startDate1 = getStartDate(endDate1);
        parameter.put("startDate1", startDate1);
        parameter.put("endDate1", endDate1);
        List<MeasureComment> list = measureCommentDao.queryCurrentCommentList(parameter);
        String name = measureCommentDao.queryName(proNo);
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue(name);
        cell.setCellStyle(headerStyle);
        ArrayList<Integer> arr = new ArrayList<Integer>();
        ArrayList<Integer> arrj = new ArrayList<Integer>();
        ArrayList<Integer> arri = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            MeasureComment ll = list.get(i);
            row = sheet.createRow(i + 2);
            if (i > 0 && ll.getMeasureCategory().equals(list.get(i - 1).getMeasureCategory())) {
                cell = row.createCell(0);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle);
                if (i == 1) {
                    arr.add(1);
                    arr.add(2);
                } else {
                    arr.add(i);
                }
            } else {
                if (i > 1 && arr.size() > 1) {
                    arrj.add(arr.get(0) + 1);
                    arri.add(arr.get(arr.size() - 1) + 2);
                    arr.clear();
                }
                cell = row.createCell(0);
                String measureCategory = ll.getMeasureCategory();
                cell.setCellValue(measureCategory == null ? "" : measureCategory);
                cell.setCellStyle(cellStyle);
            }

            cell = row.createCell(1);
            String measureName = ll.getMeasureName();
            cell.setCellValue(measureName == null ? "" : measureName);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            String target = ll.getTarget();
            cell.setCellValue(target == null ? "-" : target);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            String lower = ll.getLower();
            cell.setCellValue(lower == null ? "-" : lower);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            String upper = ll.getUpper();
            cell.setCellValue(upper == null ? "-" : upper);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            String measureValue = ll.getMeasureValue();
            cell.setCellValue(measureValue == null ? "-" : measureValue);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            String comment = ll.getComment();
            cell.setCellValue(comment == null ? "..." : comment);
            cell.setCellStyle(cellStyle);
        }
        for (int m = 0; m < arrj.size(); m++) {
            sheet.addMergedRegion(new CellRangeAddress(arrj.get(m), arri.get(m), 0, 0));
        }
        sheet.addMergedRegion(new CellRangeAddress(arr.get(0) + 1, arr.get(arr.size() - 1) + 2, 0, 0));
        return returnWb2Byte(wb);
    }

    private void problemRisk(Workbook wb, String proNo) {
        try {
            Sheet sheet = wb.getSheetAt(2);
            Row row;
            Cell cell;
            CellStyle cellStyle = getCellStyle(wb);
            CellStyle headerStyle = getHeadStyle(wb);
            /********************** 获取数据 **********************************/

            List<Map<String, Object>> list = measureCommentDao.queryQroblemRisk(proNo);
            // List<Map<String, Object>> list2 =
            // measureCommentDao.queryIterativeName(proNo);
            List<Map<String, Object>> list2 = iterativeServices.getProjectMebersSelect(proNo);
            String name = measureCommentDao.queryName(proNo);
            int i = 0;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue(name);
            cell.setCellStyle(headerStyle);
            for (Map<String, Object> map : list) {
                row = sheet.createRow(i + 2);
                cell = row.createCell(0);
                Object obj = map.get("question");
                String question = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(question);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(1);
                obj = map.get("impr_measure");
                String impr_measure = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(impr_measure);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                obj = map.get("progress_desc");
                String progress_desc = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(progress_desc);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                obj = map.get("is_361");
                String is_361 = obj == null ? "-" : String.valueOf(obj);
                if ("1".equals(is_361)) {
                    is_361 = "361评估问题";
                } else if ("2".equals(is_361)) {
                    is_361 = "风险";
                } else if ("3".equals(is_361)) {
                    is_361 = "指标评估问题";
                }
                cell.setCellValue(is_361);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(4);
                obj = map.get("finish_time");
                String finish_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(finish_time);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(5);
                obj = map.get("actual_finish_time");
                String actual_finish_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(actual_finish_time);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(6);
                obj = map.get("person_liable");
                String person_liable = obj == null ? "" : String.valueOf(obj);
                List<String> result = Arrays.asList(person_liable.split(" |,"));
                person_liable = person_liable.replace(" ", "");
                String name2 = "";
                if (person_liable == null || "".equals(person_liable)) {
                    cell.setCellValue("-");
                    cell.setCellStyle(cellStyle);
                } else {
                    for (Map<String, Object> map2 : list2) {
                        obj = map2.get("value");
                        String account = obj == null ? "" : String.valueOf(obj);
                        if (result.contains(account)) {
                            obj = map2.get("text");
                            String names = obj == null ? "" : String.valueOf(obj);
                            name2 += names + ",";
                        }
                    }
                    name2 = name2.substring(0, name2.length() - 1);
                    cell.setCellValue(name2);
                    cell.setCellStyle(cellStyle);
                }
                cell = row.createCell(7);
                obj = map.get("state");
                String state = obj == null ? "-" : String.valueOf(obj);
                cell.setCellValue(state);
                cell.setCellStyle(cellStyle);
                i++;
            }
        } catch (Exception e) {
            logger.error("export excel failed!", e);
        }
    }

    private void demandManagement(Workbook wb, String proNo) {
        try {
            Sheet sheet = wb.getSheetAt(1);
            Row row;
            Cell cell;
            CellStyle cellStyle = getCellStyle(wb);
            CellStyle headerStyle = getHeadStyle(wb);
            /********************** 获取数据 **********************************/

            List<Map<String, Object>> list = measureCommentDao.queryIterativeWork(proNo);
            // List<Map<String, Object>> list2 =
            // measureCommentDao.queryIterativeName(proNo);
            List<Map<String, Object>> list2 = iterativeServices.getProjectMebersSelect(proNo);
            String name = measureCommentDao.queryName(proNo);
            int i = 0;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue(name);
            cell.setCellStyle(headerStyle);
            for (Map<String, Object> map : list) {
                row = sheet.createRow(i + 2);
                cell = row.createCell(0);
                Object obj = map.get("topic");
                String topic = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(topic);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(1);
                obj = map.get("ite_name");
                String ite_name = obj == null ? "未配置" : String.valueOf(obj);
                cell.setCellValue(ite_name);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                obj = map.get("prior");
                String prior = obj == null ? "" : String.valueOf(obj);
                if ("1".equals(prior)) {
                    prior = "低";
                } else if ("2".equals(prior)) {
                    prior = "中";
                } else if ("3".equals(prior)) {
                    prior = "高";
                }
                cell.setCellValue(prior);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(3);
                obj = map.get("importance");
                String importance = obj == null ? "" : String.valueOf(obj);
                if ("1".equals(importance)) {
                    importance = "关键";
                } else if ("2".equals(importance)) {
                    importance = "重要";
                } else if ("3".equals(importance)) {
                    importance = "一般";
                } else if ("4".equals(importance)) {
                    importance = "提示";
                }
                cell.setCellValue(importance);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(4);
                obj = map.get("status");
                String status = obj == null ? "" : String.valueOf(obj);
                if ("1".equals(status)) {
                    status = "需求分析";
                } else if ("2".equals(status)) {
                    status = "需求设计";
                } else if ("3".equals(status)) {
                    status = "开发";
                } else if ("4".equals(status)) {
                    status = "测试";
                } else if ("5".equals(status)) {
                    status = "验收";
                } else if ("6".equals(status)) {
                    status = "已关闭";
                }
                cell.setCellValue(status);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(5);
                obj = map.get("finalimit");
                String finalimit = obj == null ? "0" : String.valueOf(obj);
                cell.setCellValue(finalimit);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(6);
                obj = map.get("person_liable");
                /*
                 * if(obj != null) {
                 *
                 * } List<String> strings = JSON.parseArray(obj.toString(),String.class);
                 */
                String person_liable = obj == null ? "" : String.valueOf(obj);
                List<String> result = Arrays.asList(person_liable.split(" |,"));
                person_liable = person_liable.replace(" ", "");
                String name2 = "";
                if (person_liable == null || "".equals(person_liable)) {
                    cell.setCellValue("-");
                    cell.setCellStyle(cellStyle);
                } else {
                    for (Map<String, Object> map2 : list2) {
                        obj = map2.get("value");
                        String account = obj == null ? "" : String.valueOf(obj);
                        if (result.contains(account)) {
                            obj = map2.get("text");
                            String names = obj == null ? "" : String.valueOf(obj);
                            name2 += names + ",";
                        }
                    }
                    name2 = name2.substring(0, name2.length() - 1);
                    cell.setCellValue(name2);
                    cell.setCellStyle(cellStyle);
                }
                cell = row.createCell(7);
                obj = map.get("plan_start_time");
                String plan_start_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(plan_start_time);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(8);
                obj = map.get("plan_end_time");
                String plan_end_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(plan_end_time);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(9);
                obj = map.get("start_time");
                String start_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(start_time);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(10);
                obj = map.get("end_time");
                String end_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(end_time);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(11);
                obj = map.get("creator");
                String creator = obj == null ? "-" : String.valueOf(obj);
                cell.setCellValue(creator);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(12);
                obj = map.get("expect_hours");
                String expect_hours = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(expect_hours);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(13);
                obj = map.get("actual_hours");
                String actual_hours = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(actual_hours);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(14);
                obj = map.get("code_amount");
                String code_amount = obj == null ? "-" : String.valueOf(obj);
                cell.setCellValue(code_amount);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(15);
                obj = map.get("wr_field");
                String wr_field = obj == null ? "-" : String.valueOf(obj);
                if ("1".equals(wr_field)) {
                    wr_field = "功能";
                } else if ("2".equals(wr_field)) {
                    wr_field = "可用";
                } else if ("3".equals(wr_field)) {
                    wr_field = "网络安全";
                } else if ("4".equals(wr_field)) {
                    wr_field = "可维护性";
                } else if ("5".equals(wr_field)) {
                    wr_field = "其他DFX";
                }
                cell.setCellValue(wr_field);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(16);
                obj = map.get("create_time");
                String create_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(create_time);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(17);
                obj = map.get("update_time");
                String update_time = obj == null ? "..." : String.valueOf(obj);
                cell.setCellValue(update_time);
                cell.setCellStyle(cellStyle);
                i++;
            }
        } catch (Exception e) {
            logger.error("export excel failed!", e);
        }
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

    /* ---------------------------导出表格样式-------------------------- */
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

    public String getStartDate(String endDate) {
        String startDate = "";
        if ("15".equals(endDate.substring(8))) {
            startDate = endDate.substring(0, 8) + "01";
        } else {
            startDate = endDate.substring(0, 8) + "16";
        }
        return startDate;
    }

    public void measureCommentChange(Integer changeValue, Integer measureId, String proNo, String changeDate) {
        Integer id = measureCommentDao.getMeasureChangeKey(measureId, proNo, changeDate);
        if (null != id) {
            measureCommentDao.updateMeasureCommentChange(changeValue, id);
        } else {
            measureCommentDao.insertMeasureCommentChange(changeValue, measureId, proNo, changeDate);
        }
    }

    public Integer getMeasureChange(Integer id, String proNo, String endDate) {
        return measureCommentDao.getMeasureChange(id, proNo, endDate);
    }

    public List<MeasureProcess> queryMeasureProcessData(String proNo, String measureId, String date) {
        Map<String, Object> parameter = new HashMap<>();
        List<MeasureProcess> list = null;
        parameter.put("proNo", proNo);
        parameter.put("measureId", measureId);
        if (null != date) {
            parameter.put("startDate", getStartDate(date));
            parameter.put("endDate", date);
            list = measureCommentDao.queryMeasureProcessData(parameter);
            boolean flag = true;
            for (MeasureProcess process : list) {
                if (null != process.getMeasureValues()) {
                    flag = false;
                }
            }
            if (flag) {
                List<String> dates = DateUtils.getLatestCycles(2, true);
                if (dates.get(0).equals(date)) {
                    parameter.put("startDate", getStartDate(dates.get(1)));
                    parameter.put("endDate", dates.get(1));
                    list = measureCommentDao.queryMeasureProcessData(parameter);
                }
            }

        }
        return list;
    }

    public String measureConfigurationRecord(String proNo, String endDate) {
        return measureCommentDao.measureConfigurationRecord(proNo, endDate);
    }

    public ProblemClosedLoopRate projectStatesProblem(String currentDate, String type) throws ParseException {
        ProblemClosedLoopRate data = new ProblemClosedLoopRate();
        List<String> lastMonths = new ArrayList<>();
        List<String> thisMonths = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String currentStart = DateUtils.getThisClcyeStart(currentDate);
        List<String> lastTime = DateUtils.getLastMonth(currentDate, 1);
        String lastStart = lastTime.get(0);
        String lastEnd = lastTime.get(1);
        map.put("type", type);
        map.put("startDate", currentStart);
        map.put("endDate", currentDate);
        Set<String> list = projectInfoVoDao.projectStatesProblem(map);// 本周期交付部
        for (String string : list) {
            String thisMonth = statesProblem(string, currentStart, currentDate, type);
            thisMonths.add(thisMonth);
            String lastMonth = statesProblem(string, lastStart, lastEnd, type);
            lastMonths.add(lastMonth);
        }
        data.setDu(list);
        data.setLastMonth(lastMonths);
        data.setThisMonth(thisMonths);
        return data;
    }

    public List<ProblemClosedLoopRate> StatesProblemAnalysis(String currentDate, String type) throws ParseException {
        List<ProblemClosedLoopRate> data = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        String startDate = DateUtils.getThisClcyeStart(currentDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String currentTime = format.format(time.getTime());// 获取当前时间
        String yearStr = DateUtils.getThisYearStart(currentDate);// 获取该年第一天
        map.put("startDate", startDate);
        map.put("endDate", currentDate);
        Set<String> list = projectInfoVoDao.projectStatesProblem(map);// 本周期交付部
        for (String string : list) {
            ProblemClosedLoopRate problem = new ProblemClosedLoopRate();
            problem.setSection(string);
            List<ReportProblemAnalysis> data1 = projectReportDao.statesProblem(string, startDate, currentDate, type);
            List<Integer> list1 = ProblemAnalysis(data1, currentTime);
            float num = (float) (list1.get(0) * 100) / data1.size();
            problem.setTimely(df.format(num) + "%");
            problem.setDelayMonth(list1.get(1));
            problem.setOpenMonth(list1.get(2));
            problem.setClosedMonth(list1.get(3));
            List<ReportProblemAnalysis> data2 = projectReportDao.statesProblem(string, yearStr, currentDate, type);
            List<Integer> list2 = ProblemAnalysis(data2, currentTime);
            problem.setDelayYear(list2.get(1));
            problem.setOpenYear(list2.get(2));
            problem.setClosedYear(list2.get(3));
            data.add(problem);
        }
        return data;
    }

    public List<ReportProblemAnalysis> kanbanProblemAnalysis(String currentDate, ProjectInfo projectInfo) throws ParseException {
        List<ReportProblemAnalysis> data = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String startDate = DateUtils.getThisClcyeStart(currentDate);
        String endDate = currentDate;
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        Map<String, Object> organizationMap = new HashMap<>();

        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        if ((null == organizationMap.get("hwpduId") || 0 == ((ArrayList) (organizationMap.get("hwpduId"))).size())) {
            map.put("hwpduId", new ArrayList<>(Arrays.asList("1012")));
            map.put("hwzpduId", new ArrayList<>(Arrays.asList("1069")));
        }
        Set<String> list = projectInfoVoDao.projectStatesProblem(map);// 本周期交付部

        for (String string : list) {
            organizationMap.put("du", string);
            organizationMap.put("startDate", startDate);
            organizationMap.put("endDate", endDate);
            ReportProblemAnalysis zonglan = projectReportDao.kanbanProblemAnalysis(organizationMap);
            zonglan.setDepartment(string);
            zonglan.setDelay(zonglan.getAarDelay() + zonglan.getProblemDelay() + zonglan.getAuditDelay()
                    + zonglan.getBackDelay());
            int sum = zonglan.getAar() + zonglan.getProblem() + zonglan.getAudit() + zonglan.getBack();
            double closed = 100 * (zonglan.getAarClosed() + zonglan.getAuditClosed() + zonglan.getBackClosed()
                    + zonglan.getProblemClosed());
            zonglan.setClosed(BigDecUtils.division(closed, sum, 2) + "%");
            zonglan.setAarClosed(Math.round(BigDecUtils.division(zonglan.getAarClosed() * 100, zonglan.getAar(), 2)));
            zonglan.setAuditClosed(
                    Math.round(BigDecUtils.division(zonglan.getAuditClosed() * 100, zonglan.getAudit(), 2)));
            zonglan.setBackClosed(
                    Math.round(BigDecUtils.division(zonglan.getBackClosed() * 100, zonglan.getBack(), 2)));
            zonglan.setProblemClosed(
                    Math.round(BigDecUtils.division(zonglan.getProblemClosed() * 100, zonglan.getProblem(), 2)));
            data.add(zonglan);
        }
        return data;
    }

    public List<Integer> ProblemAnalysis(List<ReportProblemAnalysis> data, String currentTime) {
        List<Integer> list = new ArrayList<>();
        int sum = 0;
        int delay = 0;
        int open = 0;
        int closed = 0;
        for (ReportProblemAnalysis map : data) {
            Date finishTime = map.getFinishTime();
            Date actualTime = map.getActualTime() == null ? new Date() : map.getActualTime();
            if (finishTime.getTime() >= actualTime.getTime()) {
                sum++;
            } else {
                delay++;
            }
            if (("Open").equals(map.getState())) {
                open++;
            } else if (("Closed").equals(map.getState())) {
                closed++;
            }
        }
        list.add(sum);
        list.add(delay);
        list.add(open);
        list.add(closed);
        return list;
    }

    public String statesProblem(String du, String start, String end, String type) {// 整理格式
        String ratio = "0";
        DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
        try {
            List<ReportProblemAnalysis> list = projectReportDao.statesProblem(du, start, end, type);
            if (!list.isEmpty()) {
                int sum = 0;
                for (ReportProblemAnalysis map : list) {
                    Date finishTime = map.getFinishTime();
                    Date actualTime = map.getActualTime() == null ? new Date() : map.getActualTime();
                    if (finishTime.getTime() >= actualTime.getTime()) {
                        sum++;
                    }
                }
                float num = (float) (sum * 100) / list.size();
                ratio = df.format(num);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ratio;
    }

    public List<String> getDateTime(String proNo, int num, boolean flag) throws ParseException {
        List<String> list = new ArrayList<String>();

        // 获取项目是否已结项，如果已结项，获取结项时间
        String date = measureCommentDao.getCloseDateByNo(proNo);

        // 已关闭的项目，显示从结项时间开始向前5个周期
        String current = StringUtils.isNotBlank(date) ? date : DateUtils.formatDate(new Date(), "yyyy-MM-dd");

        ProjectInfo projectInfo = measureCommentDao.getDateTimeByNo(proNo);

        List<String> latestCycles = DateUtils.getLatestWeek(num, flag, current);

        for (String cycle : latestCycles) {
            Date cycleDate = DateUtils.parseDate(cycle, "yyyy-MM-dd");
            if (!(projectInfo.getStartDate().after(cycleDate))) {
                list.add(cycle);
            }
        }

        return list;
    }

    @SuppressWarnings("rawtypes")
    public Map<String, List> queryMeasureValue(String proNo, String measureId, String date) {
        Map<String, List> ret = new HashMap<>();
        List<String> dateList = DateUtils.getLatestWeek(7, true, date);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String createTime = "";
        List<String> monthList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();

        getMeasureValue(proNo, measureId, dateList, list);

        if (null != list && list.size() > 0) {
            for (Map<String, Object> map : list) {
                if (null != map && map.size() > 0 && StringUtils.isNotBlank(
                        StringUtilsLocal.valueOf(map.get("createTime")))) {
                    createTime = DateUtils.getModifyDay(StringUtilsLocal.valueOf(map.get("createTime")));
                    map.put("createTime", createTime);
                }
            }

            for (int i = 0; i < list.size(); i++) {
                String statisticalTime = StringUtilsLocal.valueOf(list.get(i).get("createTime"));
                monthList.add(statisticalTime);
                valueList.add(StringUtilsLocal.valueOf(list.get(i).get("measureValue")));

                if ((i + 1) < list.size()) {
                    String statisticalTimeNext = StringUtilsLocal.valueOf(list.get(i + 1).get("createTime"));
                    double day = DateUtils.differenceTime(statisticalTime, statisticalTimeNext);
                    if (day > 20) {
                        List<String> inHoursCycles = DateUtils.getInHoursCycles(statisticalTime, statisticalTimeNext);
                        for (String inHoursCycle : inHoursCycles) {
                            monthList.add(inHoursCycle);
                            valueList.add("-");
                        }
                    }
                }
            }
        }
        ret.put("monthList", monthList);
        ret.put("valueList", valueList);
        return ret;
    }

    private void getMeasureValue(String proNo, String measureId, List<String> dateList,
                                 List<Map<String, Object>> list) {
        for (int i = 5; i >= 0; i--) {
            Map<String, Object> measureValue = measureCommentDao.getMeasureValue(proNo, measureId,
                    dateList.get(i) + " 23:59:59",
                    dateList.get(i + 1) + " 23:59:59");
            if (null != measureValue && measureValue.size() > 0) {
                list.add(measureValue);
            }
        }
    }

    public void saveMeasureList(List<Map<String, String>> list) {
        Set<String> measureIds = new HashSet<>();
        String proNo = "";
        String date = "";
        for (Map<String, String> map : list) {
            proNo = map.get("proNo");
            date = map.get("date");
            String measureId = map.get("measureId").trim();
            String measureValue = map.get("measureValue").trim();
            if (!StringUtils.isEmpty(measureValue)) {
                String level = map.get("level");
                MeasureLoadEveryInfo measureLoadEveryInfo = new MeasureLoadEveryInfo(proNo, DateUtils.parseDate(date,
                        "yyyy-MM-dd"),
                        measureId, measureValue);
                insert(measureLoadEveryInfo);
                if ("3".equals(level)) {
                    String mis = measureCommentDao.queryMeasureId(Integer.valueOf(measureId));
                    measureIds.addAll(CollectionUtilsLocal.splitToList(mis));
                    measureIds.remove(measureId);
                }
            }
        }
        if (measureIds.size() > 0) {
            calculateProcess(proNo, getStartDate(date), date, measureIds);
        }

    }

    public List<String> getCostMonth(String startDate) {
        int monthSize = 0;

        try {
            monthSize = DateUtils.getMonthDiff(DateUtils.SHORT_FORMAT_GENERAL.parse(startDate), new Date());
        } catch (ParseException e) {
            logger.error("MeasureCommentService getCostMonth failed:" + e.getMessage());
        }
        return DateUtils.getRecentMonths(monthSize);
    }

    public List<Map<String, Object>> getCostWeek(String selectDate, int num) {
        List<Map<String, Object>> list1 = new ArrayList<>();
        List<String> list = new ArrayList<>();
        Date monthDate = null;
        String[] selectDateArr = selectDate.split("-");
        boolean flag = false;

        try {
            String[] nowArr = DateUtils.SHORT_FORMAT_GENERAL.format(new Date()).split("-");

            if ((nowArr[0] + "-" + nowArr[1]).equals(selectDate)) {
                monthDate = new Date();
                flag = true;
            } else {
                monthDate = DateUtils.SHORT_FORMAT_GENERAL.parse(DateUtils.getEndOfMonth(selectDate + "-01"));
            }
            list = DateUtils.getEverySunday(monthDate, num);

            for (String s : list) {
                String[] arr = s.split("-");

                if (flag) {
                    if (!(Integer.parseInt(arr[0]) >= Integer.parseInt(selectDateArr[0]) && Integer.parseInt(arr[1]) >= Integer.parseInt(selectDateArr[1]))) {
                        continue;
                    }

                    if (new Date().before(DateUtils.SHORT_FORMAT_GENERAL.parse(
                            DateUtils.getAroundDay(DateUtils.SHORT_FORMAT_GENERAL.parse(s), -6)))) {
                        continue;
                    }
                } else {
                    if (!(arr[0] + "-" + arr[1]).equals(selectDate)) {
                        continue;
                    }
                }

                Map<String, Object> map = new HashMap<>();
                String[] day = new String[7];

                map.put("week", DateUtils.getWeekOfMonth(s));
                map.put("cycle", s);
                for (int n = 0; n < 7; n++) {
                    day[n] = DateUtils.getAroundDay(DateUtils.SHORT_FORMAT_GENERAL.parse(s), (n - 6));
                }
                map.put("day", day);

                list1.add(map);
            }
        } catch (ParseException e) {
            logger.error("MeasureCommentService getCostWeek failed:" + e.getMessage());
        }

        return list1;
    }

}
