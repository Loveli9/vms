package com.icss.mvp.service;

import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.dao.*;
import com.icss.mvp.dao.index.InTmplDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.util.DateUtils;
//import com.icss.mvp.util.EmailUtils;
import com.icss.mvp.util.StringUtilsLocal;
import com.icss.mvp.util.UUIDUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("all") @Service public class IterationCycleService {

    private final static Logger logger = Logger.getLogger(IterationCycleService.class);

    @Autowired private IterationCycleDao iterationCycleDao;

    @Autowired private DeliverDao deliverDao;

    @Autowired private MeasureRangeDao measurerangedao;

    @Autowired private IterationMeasureIndexDao iterationMeasureIndexDao;

    @Autowired private IProjectListDao projectListDao;
    @Autowired private CodeGainTypeDao codeGainTypeDao;

    @Autowired private InTmplDao inTmplDao;

    @Autowired private MeasureCommentDao measureCommentDao;

    private final BigDecimal zero = new BigDecimal(0.00);

    private final String[] INT_UNIT = { "个", "页", "次" };

    public String insertIterationCycle(IterationCycle iterationCycle) throws Exception {
        // 判断当前迭代名称是否存在
        List<IterationCycle> list = iterationCycleDao.checkIteName(iterationCycle.getProNo(),
                                                                   iterationCycle.getIteName());
        if (list.size() > 0) {
            return "repeat";
        } else {
            iterationCycle.setId(UUIDUtil.getNew());
            // 新增开始时间和结束时间默认等于计划开始和结束时间
            /*
             * iterationCycle.setStartDate(iterationCycle.getPlanStartDate());
             * iterationCycle.setEndDate(iterationCycle.getPlanEndDate());
             */
            iterationCycleDao.insertSelective(iterationCycle);
        }
        return "success";
    }

    /**
     * 给导入项目配置默认迭代信息（项目起止时间）
     *
     * @param iterationCycle
     * @return
     * @throws Exception
     */
    public BaseResponse deployIteration(IterationCycle iterationCycle) {
        BaseResponse response = new BaseResponse();
        // 判断当前迭代是否存在
        List<IterationCycle> list = iterationCycleDao.isHaveIteration(iterationCycle.getProNo());
        if (list.size() > 0) {
            response.setCode("100");
            return response;
        }
        try {
            iterationCycleDao.insertSelective(iterationCycle);
            response.setCode("200");
        } catch (Exception e) {
            logger.error("默认迭代配置失败" + e.getMessage());
        }
        return response;
    }

    public void editIterationCycle(IterationCycle iterationCycle) {
        iterationCycleDao.updateByPrimaryKeySelective(iterationCycle);
    }

    private Map<String, Integer> createMeasureIdIndexMap(Map<String, String> measureNameId, String[] measures) {
        Map<String, Integer> measureIdIndexMap = new HashMap<>();
        for (int i = 0; i < measures.length; i++) {
            measureIdIndexMap.put(measureNameId.get(measures[i]), i);
        }
        return measureIdIndexMap;
    }

    /**
     * 将一个迭代周期的指标按照放入列的对应位置
     *
     * @param iterationCycle
     * @param list
     * @param measureIdIndexMap
     * @param rows
     * @param iterIndex
     */
    private void createDataRow(IterationCycle iterationCycle, List<IterationMeasureIndex> list,
                               Map<String, Integer> measureIdIndexMap, List<String[]> rows) {
        String[] row = new String[measureIdIndexMap.size() + 1];
        for (int i = 1; i < row.length; i++) {
            row[i] = "0.0";
        }
        // 第一列展示迭代周期
        row[0] = new StringBuilder(iterationCycle.getIteName()).append('(').append(
                DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getStartDate())).append("~").append(
                DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getEndDate())).append(
                ')').toString();

        for (IterationMeasureIndex iterationMeasureIndex : list) {
            Integer index = measureIdIndexMap.get(iterationMeasureIndex.getMeasureId());
            if (null != index && index < row.length - 1) {
                row[index + 1] = formatValue(iterationMeasureIndex.getValue(), iterationMeasureIndex.getUnit());
            }
        }

        rows.add(row);
    }

    private String formatValue(String value, String unit) {
        String result = "";
        if (ArrayUtils.contains(INT_UNIT, unit)) {
            BigDecimal bigValue = safeConvertToDecimal(value);
            result = String.valueOf(bigValue.intValue());
        } else if ("%".equals(unit)) {
            BigDecimal bigValue = safeConvertToDecimal(value);
            result = (bigValue.compareTo(new BigDecimal("100")) > 0) ? "100.00" : bigValue.setScale(2,
                                                                                                    RoundingMode.HALF_UP).toString();
        } else if ("KLOC".equals(unit)) {
            BigDecimal bigValue = safeConvertToDecimal(value);
            result = bigValue.setScale(1, RoundingMode.HALF_UP).toString();
        } else {
            return value;
        }
        return result;
    }

    private BigDecimal safeConvertToDecimal(String value) {
        BigDecimal bigValue;
        try {
            bigValue = new BigDecimal(value);
        } catch (NumberFormatException e) {
            bigValue = zero;
        }
        return bigValue;
    }

    public void iterationIndexAdd(IterationMeasureIndex iterationMeasureIndex) {
        iterationMeasureIndex.setId(UUIDUtil.getNew());
        iterationMeasureIndex.setCreateTime(new Date());
        iterationMeasureIndexDao.insert(iterationMeasureIndex);
    }

    public void iterationIndexEdit(IterationMeasureIndex iterationMeasureIndex, String proNo) {

        String value = formatValue(iterationMeasureIndex.getValue(), iterationMeasureIndex.getUnit());
        iterationMeasureIndex.setValue(value);

        int num = iterationMeasureIndexDao.checkIterationInfo(iterationMeasureIndex.getIterationId(),
                                                              iterationMeasureIndex.getMeasureId());

        if (num == 0) {
            iterationIndexAdd(iterationMeasureIndex);
        } else {
            iterationMeasureIndex.setCreateTime(new Date());
            iterationMeasureIndexDao.updateByPrimaryKeySelective(iterationMeasureIndex);
        }
    }

    /**
     * @Description:分页查询迭代管理信息 @param @param request 参数 @return void 返回类型 @throws
     */
    @SuppressWarnings("all") public TableSplitResult queryIteInfoByPage(TableSplitResult page, String sort,
                                                                        String sortOrder) {
        TableSplitResult data = new TableSplitResult();
        if(null != page.getQueryMap().get("proNo") && "" !=page.getQueryMap().get("proNo")){
            Integer total = iterationCycleDao.queryIterationTotals(page);
            if (total > 0) {
                List<IterationCycle> resultList = iterationCycleDao.queryIteInfoByPage(page, sort, sortOrder);
                data.setRows(resultList);
            }
            data.setTotal(total);
        }else{
            if (page.getQueryMap().get("username").equals("admin")) {
                List<IterationCycleResult> resultList = iterationCycleDao.queryAll(page, sort, sortOrder);
                data.setRows(resultList);
                //            Integer total = iterationCycleDao.queryAllTotals(page);
                data.setTotal(10);
            } else {
                String zrAccount = deliverDao.queryAccount(page.getQueryMap().get("username").toString());
                page.getQueryMap().put("zrAccount", zrAccount);
                Integer total = iterationCycleDao.queryIterationTotals(page);
                if (total > 0) {
                    List<IterationCycle> resultList = iterationCycleDao.queryIteInfoByPage(page, sort, sortOrder);
                    data.setRows(resultList);
                }
                data.setTotal(total);
            }
        }
        return data;
    }

    public List<IterationCycle> getMessage(String proNo) {
        List<IterationCycle> list = iterationCycleDao.iterationListasc(proNo);
        return list;
    }

    /**
     * @Description:删除当前选中的迭代管理信息 @param @param ids @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    public void deleteIterationCycle(String[] ids) throws Exception {
        for (String id : ids) {
            iterationCycleDao.deleteIterationCycle(id);
        }
    }

    /**
     * @Description:迭代编辑页面回显 @param @param iterationCycle @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    public IterationCycle queryEditPageInfo(String id) throws Exception {
        return iterationCycleDao.queryEditPageInfo(id);
    }

    /**
     * @Description: 加载迭代名称下拉列表值 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    public List<Map<String, Object>> getIteNameSelect(String proNo) throws Exception {
        return iterationCycleDao.getIteNameSelect(proNo);
    }

    /**
     * @Description: 校验迭代名称唯一性 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    public String checkIteName(String proNo, String iteName) throws Exception {
        List<IterationCycle> list = iterationCycleDao.checkIteName(proNo, iteName);
        if (list.size() > 0) {
            return "faliure";
        } else {
            return "success";
        }
    }

    public String saveImage(String imageDataB64, String proNo) {
        List<Map<String, Object>> list = codeGainTypeDao.getEmailInfo(proNo);
        if (list == null || list.size() <= 0) {
            return "未配置邮件";
        }
        // if(!StringUtilsLocal.valueOf(list.get(0).get("email_on_off")).equals("0")) {
        // return "配置邮件发送开关关闭";
        // }
        ProjectDetailInfo detailInfo = projectListDao.isExit(proNo);
        try {
            String path = StringUtilsLocal.valueOf(list.get(0).get("send_email"));
            if (StringUtilsLocal.isBlank(path)) {
                return "配置邮件发送邮箱为空";
            }
            //            EmailUtils.sendEmail(detailInfo.getName(), imageDataB64, path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("EmailUtils.sendEmail exception, error: " + e.getMessage());
        }
        /*
         * ImageUtils imageUtils = new ImageUtils(); imageUtils.saveImage(imageDataB64,
         * proNo+System.currentTimeMillis());
         */
        return "发送邮件成功";
    }

    /**
     * @Description: 校验迭代时间顺序 @param @param iterationCycle @param @return 参数 @return
     * BaseResponse 返回类型 @author chengchenhui @throws
     */
    public IterationCycle checkStartTime(String proNo) throws Exception {
        return iterationCycleDao.checkStartTime(proNo);
    }

    /**
     * 查询项目名称
     *
     * @param projNo
     * @return
     */
    public ProjectDetailInfo getProjNoName(String proNo) {
        ProjectDetailInfo detailInfo = projectListDao.isExit(proNo);
        return detailInfo;
    }

    /**
     * 分页查询手工录入指标值
     *
     * @param page
     * @param list
     * @return
     */
    public TableSplitResult iterationIndexVlaue(TableSplitResult page, String proNo, String[] list) {
        TableSplitResult result = new TableSplitResult<>();
        int total = iterationMeasureIndexDao.countManualEntryMeasure(proNo);
        List<Measure> measures = iterationMeasureIndexDao.queryManualEntryMeasure(page, proNo);
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        for (Measure measure : measures) {
            String unit = measure.getUnit();
            Map<String, String> map = new HashMap<>();
            map.put("measure_id", measure.getId() + "");
            map.put("measure_name", measure.getName());
            map.put("unit", unit);
            for (int i = 0; i < list.length; i++) {
                map.put(list[i], "-");
            }
            List<IterationMeasureIndex> data = iterationMeasureIndexDao.queryIterationInfo(measure.getId() + "", list);
            for (IterationMeasureIndex measureIndex : data) {
                map.put(measureIndex.getIterationId(), formatValue(measureIndex.getValue(), unit));
            }
            rows.add(map);
        }
        result.setRows(rows);
        result.setTotal(total);
        return result;
    }

    /**
     * @description: 获取迭代指标 @param proNo labId measureCate @return
     * PlainResponse @throws @author chengchenhui @date 2019/5/16
     * 15:42
     */
    public JSONObject measureReport(String proNo, String label, String measureCate) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtilsLocal.isBlank(label) || StringUtilsLocal.isBlank(measureCate)) {
            throw new Exception("参数异常");
        }
        List<Map<String, Object>> listMaps = null;
        Map<String, String> measureNameId = new HashMap<>();
        List<String> ids = inTmplDao.getConfigMeasureIds(proNo, false);
        if (ids == null || ids.size() == 0) {
            return null;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("proNo", proNo);
        param.put("ids", ids);
        param.put("flag", false);
        param.put("label", label);
        param.put("category", measureCate);
        listMaps = inTmplDao.getConfigRecords(param);

        List<String[]> rows = new ArrayList<>();
        List<String> measureIds = new ArrayList<>();
        int lenth = listMaps.size() + 1;
        String[] indexs = new String[lenth - 1];
        String[] titles = new String[lenth];
        String[] units = new String[lenth];
        String[] uppers = new String[lenth];
        String[] lowers = new String[lenth];
        String[] targets = new String[lenth];
        int i = 1;
        titles[0] = "统计周期(迭代或月)";
        uppers[0] = "上限";
        lowers[0] = "下限";
        targets[0] = "目标值";
        units[0] = "单位";
        for (Map<String, Object> map : listMaps) {
            indexs[i - 1] = map.get("name").toString() + map.get("measure_id").toString();
            // titles[i] = createHeader(map);
            titles[i] = map.get("name").toString().trim();
            units[i] = (null == map.get("unit") || "".equals(map.get("unit"))) ? "单位" : map.get("unit").toString();
            uppers[i] = null == map.get("upper") ? "" : map.get("upper").toString();
            lowers[i] = null == map.get("lower") ? "" : map.get("lower").toString();
            targets[i] = null == map.get("target") ? "" : map.get("target").toString();
            measureIds.add(map.get("measure_id").toString());
            measureNameId.put(map.get("name").toString() + map.get("measure_id").toString(),
                              map.get("measure_id").toString());
            i++;
        }
        rows.add(titles);
        rows.add(uppers);
        rows.add(lowers);
        rows.add(targets);
        rows.add(units);
        Map<String, Integer> measureIdIndexMap = createMeasureIdIndexMap(measureNameId, indexs);

        List<IterationCycle> list = iterationCycleDao.iterationList(proNo);
        List<String> iterationIds = new ArrayList<>();
        int index = list.size();
        Date date = new Date();
        int indexNow = 0;
        boolean flag = false;
        for (IterationCycle iterationCycle : list) {
            if (date.after(iterationCycle.getEndDate())) {
                indexNow += 1;
            }
            if (date.before(iterationCycle.getEndDate()) && date.after(iterationCycle.getStartDate())) {
                flag = true;
            }
            if (null != iterationCycle.getStartDate() && null != iterationCycle.getEndDate()) {
                List<IterationMeasureIndex> values = iterationMeasureIndexDao.measureIndexValues(iterationCycle.getId(),
                                                                                                 measureIds,
                                                                                                 format.format(
                                                                                                         iterationCycle.getStartDate()),
                                                                                                 format.format(
                                                                                                         iterationCycle.getEndDate()),
                                                                                                 iterationCycle.getProNo());
                iterationIds.add(iterationCycle.getId());
                createDataRow(iterationCycle, values, measureIdIndexMap, rows);
            }
            index--;
        }
        JSONObject result = new JSONObject();
        result.put("tableData", rows);
        result.put("indexNow", flag == true ? indexNow : -1);
        return result;
    }

    /**
     * @description: 根据日期获取迭代信息 @param proNo date @return string
     * PlainResponse @throws @author wangjianbin @date 2019/12/18
     */
    public IterationCycle queryIteInfo(String proNo, Date date) {
        return iterationCycleDao.queryIteInfo(proNo, date);

    }

    /**
     * @description: 根据id获取迭代信息 @param id  @return
     * PlainResponse @throws @author wangjianbin @date 2019/12/18
     */
    public IterationCycle queryIteInfoById(String id) {
        return iterationCycleDao.queryIteInfoById(id);
    }

    /**
     * @param proNo labId measureCate
     * @return PlainResponse
     * @throws
     * @description: 可信追踪
     * @author zhanghucheng
     * @date 2019/12/17
     */
    public TableSplitResult<List<Map<String, Object>>> measureReport1(String proNo, String label, String measureCate)
            throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtilsLocal.isBlank(label) || StringUtilsLocal.isBlank(measureCate)) {
            throw new Exception("参数异常");
        }
        List<Map<String, Object>> listMaps = null;
        Map<String, String> measureNameId = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        param.put("proNo", proNo);
        param.put("ids", null);
        param.put("flag", false);
        param.put("label", label);
        param.put("category", measureCate);
        listMaps = inTmplDao.getConfigRecords(param);

        List<String> measureIds = new ArrayList<>();
        int i = 1;
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> listMap1 = new HashMap<>();
        listMap1.put("name", "上限");
        for (Map<String, Object> map : listMaps) {
            listMap1.put("id" + map.get("measure_id"), null == map.get("upper") ? "" : map.get("upper").toString());
            measureIds.add(map.get("measure_id").toString());
        }
        resultList.add(listMap1);
        Map<String, Object> listMap2 = new HashMap<>();
        listMap2.put("name", "下限");
        for (Map<String, Object> map : listMaps) {
            listMap2.put("id" + map.get("measure_id"), null == map.get("lower") ? "" : map.get("lower").toString());
        }
        resultList.add(listMap2);
        Map<String, Object> listMap3 = new HashMap<>();
        listMap3.put("name", "目标值");
        for (Map<String, Object> map : listMaps) {
            listMap3.put("id" + map.get("measure_id"), null == map.get("target") ? "" : map.get("target").toString());
        }
        resultList.add(listMap3);

        List<IterationCycle> list = iterationCycleDao.iterationList(proNo);
        List<String> iterationIds = new ArrayList<>();
        int index = list.size();
        Date date = new Date();
        int indexNow = 0;
        boolean flag = false;
        for (IterationCycle iterationCycle : list) {
            if (date.after(iterationCycle.getEndDate())) {
                indexNow += 1;
            }
            if (date.before(iterationCycle.getEndDate()) && date.after(iterationCycle.getStartDate())) {
                flag = true;
            }
            if (null != iterationCycle.getStartDate() && null != iterationCycle.getEndDate()) {
                List<IterationMeasureIndex> values = iterationMeasureIndexDao.measureIndexValues(iterationCycle.getId(),
                                                                                                 measureIds,
                                                                                                 format.format(
                                                                                                         iterationCycle.getStartDate()),
                                                                                                 format.format(
                                                                                                         iterationCycle.getEndDate()),
                                                                                                 iterationCycle.getProNo());
                iterationIds.add(iterationCycle.getId());
                createDataRows(iterationCycle, values, resultList);
            }
            index--;
        }
        TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
        result.setRows(resultList);
        return result;
    }

    /**
     * 将一个迭代周期的指标按照放入列的对应位置（可信追踪）
     *
     * @param iterationCycle
     * @param list
     * @param rows
     */
    private void createDataRows(IterationCycle iterationCycle, List<IterationMeasureIndex> list,
                                List<Map<String, Object>> rows) {
        Map<String, Object> row = new HashMap<>(0);
        // 迭代周期
        String name = new StringBuilder(iterationCycle.getIteName()).append('(').append(
                DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getStartDate())).append("~").append(
                DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, iterationCycle.getEndDate())).append(
                ')').toString();
        row.put("name", name);
        for (IterationMeasureIndex iterationMeasureIndex : list) {
            row.put("id" + iterationMeasureIndex.getMeasureId(),
                    formatValue(iterationMeasureIndex.getValue(), iterationMeasureIndex.getUnit()));
        }

        rows.add(row);
    }

    public List<IterationCycle> listPastIteration(String projectNo) {
        List<IterationCycle> pasts = new ArrayList<>();
        List<IterationCycle> iterationCycles = getMessage(projectNo);
        long dt = Calendar.getInstance().getTime().getTime();
        iterationCycles.forEach(iterationCycle -> {
            Date idt = iterationCycle.getStartDate();
            if (iterationCycle.getStartDate().getTime() < dt) {
                pasts.add(iterationCycle);
            }
        });
        return pasts;
    }
}
