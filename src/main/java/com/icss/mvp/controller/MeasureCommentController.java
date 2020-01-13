
package com.icss.mvp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.icss.mvp.entity.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.MeasureCommentService;
import com.icss.mvp.service.ProjectOperationService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DateUtils;

@RestController
@RequestMapping("/measureComment")
public class MeasureCommentController extends BaseController {

    private static Logger logger = Logger.getLogger(MeasureCommentController.class);
    @Autowired
    private MeasureCommentService measureCommentService;

    @Autowired
    private ProjectOperationService projectOperationService;

    /**
     * 导出指标点评信息列表
     *
     * @param response
     * @param proNo
     */
    @RequestMapping("/qualityReviewExport")
    @ResponseBody
    public void qualityReviewExport(HttpServletResponse response, String proNo) {
        try {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("proNo", proNo);
            byte[] fileContents = measureCommentService.qualityReviewExport(parameter);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "质量点评" + sf.format(new Date()).toString() + ".xlsx";
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
            response.getOutputStream().write(fileContents);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("查询失败", e);
        }
    }

    /**
     * 查询指标点评信息列表
     *
     * @param proNo
     * @param queryDate
     * @return
     */
    @RequestMapping("/queryMeasureCommentList")
    @ResponseBody
    public ListResponse<Map<String, Object>> queryMeasureCommentList(String proNo, String queryDate) {
        ListResponse<Map<String, Object>> result = new ListResponse<Map<String, Object>>();
        try {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("proNo", proNo);
            if (null == queryDate) {
                result.setData(null);
                result.setMessage("返回成功");
                result.setCode("success");
                return result;
            }
            String endDate = "";
            if (queryDate.indexOf(",") > 0) {
                String date1 = queryDate.substring(0, queryDate.indexOf(","));
                String date2 = queryDate.substring(queryDate.indexOf(",") + 1);
                parameter.put("startDate1", measureCommentService.getStartDate(date1));
                parameter.put("endDate1", date1);
                endDate = date1;
                parameter.put("startDate2", measureCommentService.getStartDate(date2));
                parameter.put("endDate2", date2);
            } else {
                parameter.put("startDate1", measureCommentService.getStartDate(queryDate));
                parameter.put("endDate1", queryDate);
                endDate = queryDate;
            }
            String measureIds = measureCommentService.measureConfigurationRecord(proNo, endDate);
            if (null != measureIds && !"".equals(measureIds)) {
                measureIds = "(" + measureIds + ")";
                parameter.put("measureIds", measureIds);
                boolean flag = false;
                if (endDate.equals(DateUtils.getLatestCycles(1, true).get(0))) {
                    flag = true;
                }
                result.setData(measureCommentService.queryCommentList(parameter, flag));
            }
            result.setMessage("返回成功");
            result.setCode("success");
        } catch (Exception e) {
            logger.error("交易失败", e);
            result.setMessage("返回失败");
            result.setCode("failure");
        }
        return result;
    }

    /**
     * 查询过程数据
     *
     * @param proNo
     * @param measureId
     * @param date
     * @return
     */
    @RequestMapping("/queryMeasureProcessData")
    @ResponseBody
    public List<MeasureProcess> queryMeasureProcessData(String proNo, String measureId, String date) {
        List<MeasureProcess> data = null;
        try {
            data = measureCommentService.queryMeasureProcessData(proNo, measureId, date);
        } catch (Exception e) {
            logger.error("查询失败", e);
        }
        return data;
    }

    /**
     * 保存指标点评信息
     *
     * @param proNo
     * @param id
     * @param value
     * @param comment
     * @param date
     * @param field
     * @return
     */
    @RequestMapping(value = "/saveMeasureComment", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse saveMeasureComment(String proNo, Integer id, String value, String comment, String date, String field) {
        BaseResponse result = new BaseResponse();
        try {
            Date endDate = DateUtils.SHORT_FORMAT_GENERAL.parse(date);
            String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
            if (id <= 10000) {
                if ("realValue".equals(field)) {// 修改指标值
                    MeasureLoadEveryInfo measureLoadEveryInfo = new MeasureLoadEveryInfo(proNo, endDate,
                            String.valueOf(id), value);
                    measureCommentService.insert(measureLoadEveryInfo);
                    projectOperationService.saveProjectOperation(proNo, userName, "录入指标数据");
                } else if ("comment".equals(field)) {// 修改点评
                    MeasureComment measureComment = new MeasureComment();
                    measureComment.setProNo(proNo);
                    measureComment.setId(id);
                    measureComment.setComment(comment);
                    measureComment.setEndDate(endDate);
                    measureCommentService.saveMeasureComment(measureComment);
                    projectOperationService.saveProjectOperation(proNo, userName, "录入指标点评信息");
                }
            }
            result.setCode("success");
        } catch (Exception e) {
            logger.error("保存失败：", e);
            result.setCode("failure");
            result.setMessage("保存失败");
        }
        return result;
    }

    /**
     * 批量保存指标值
     *
     * @param list
     * @return
     */
    @RequestMapping(value = "/saveMeasureList")
    @ResponseBody
    public BaseResponse saveMeasureList(@RequestBody List<Map<String, String>> list) {
        BaseResponse result = new BaseResponse();
        try {
            if (null != list && list.size() > 0) {
                measureCommentService.saveMeasureList(list);
                String proNo = list.get(0).get("proNo");
                String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
                projectOperationService.saveProjectOperation(proNo, userName, "整体保存指标数据");
                result.setMessage("返回成功");
                result.setCode("success");
            }

        } catch (Exception e) {
            logger.error("批量保存失败：", e);
            result.setCode("failure");
            result.setMessage("批量保存失败");
        }
        return result;
    }

    /**
     * 质量点评过程数据保存
     *
     * @param proNo
     * @param middleId
     * @param middleValue
     * @param date
     * @param measure
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveMeasureProcessData", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse saveMeasureProcessData(String proNo, String middleId, String middleValue, String date, String measure) {
        BaseResponse result = new BaseResponse();
        List<Map<String, String>> measures = JSONArray.parseObject(measure, List.class);
        try {
            measureCommentService.saveMeasureProcessData(proNo, middleId, middleValue, measures, date);
            String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
            projectOperationService.saveProjectOperation(proNo, userName, "录入指标过程数据");
            result.setMessage("返回成功");
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("failure");
            result.setMessage("保存失败");
            logger.error("保存失败：", e);
        }
        return result;
    }

    @RequestMapping("/measureCommentChange")
    @ResponseBody
    public boolean measureCommentChange(Integer changeValue, Integer measureId, String proNo, String changeDate) {
        measureCommentService.measureCommentChange(changeValue, measureId, proNo, changeDate);
        String userName = CookieUtils.value(request, CookieUtils.USER_NAME);
        projectOperationService.saveProjectOperation(proNo, userName, "修改指标点灯状态");
        return true;
    }

    @RequestMapping("/getTime")
    @ResponseBody
    public PlainResponse<List<String>> getTime(int num, boolean flag) {
        PlainResponse<List<String>> result = new PlainResponse<List<String>>();
        try {
            result.setData(DateUtils.getLatestCycles(num, flag));
            result.setCode("success");
        } catch (Exception e) {
            logger.error("queryProjectCycle exception, error:", e);
            result.setCode("fail");
            result.setErrorMessage("error", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/projectStatesProblem")
    @ResponseBody
    public ProblemClosedLoopRate projectStatesProblem(String currentDate, String type) {
        ProblemClosedLoopRate data = null;
        try {
            data = measureCommentService.projectStatesProblem(currentDate, type);
        } catch (Exception e) {
            logger.error(e);
        }
        return data;
    }

    @RequestMapping("/StatesProblemAnalysis")
    @ResponseBody
    public List<ProblemClosedLoopRate> StatesProblemAnalysis(String currentDate, String problemType) {
        List<ProblemClosedLoopRate> data = new ArrayList<>();
        try {
            data = measureCommentService.StatesProblemAnalysis(currentDate, problemType);
        } catch (Exception e) {
            logger.error(e);
        }
        return data;
    }

    @RequestMapping("/kanbanProblemAnalysis")
    @ResponseBody
    public List<ReportProblemAnalysis> kanbanProblemAnalysis(String currentDate, ProjectInfo projectInfo) {
        List<ReportProblemAnalysis> data = new ArrayList<>();
        try {
            data = measureCommentService.kanbanProblemAnalysis(currentDate, projectInfo);
        } catch (Exception e) {
            logger.error(e);
        }
        return data;
    }

    @RequestMapping("/getTimeQ")
    @ResponseBody
    public PlainResponse<List<String>> getTimeQ(String proNo, int num, boolean flag) {
        PlainResponse<List<String>> result = new PlainResponse<List<String>>();
        try {

            result.setData(measureCommentService.getDateTime(proNo, num, flag));
//			result.setCode("success");
        } catch (Exception e) {
            logger.error("queryProjectCycle exception, error:", e);
//			result.setCode("fail");
            result.setErrorMessage("error", e.getMessage());
        }
        return result;
    }

    /**
     * 质量点评指趋势
     *
     * @param proNo
     * @param measureId
     * @param date
     * @return
     */
    @RequestMapping("/queryMeasureValue")
    @ResponseBody
    public PlainResponse<Map> queryMeasureValue(String proNo, String measureId, String date) {
        PlainResponse<Map> result = new PlainResponse<>();
        try {
            Map<String, List> data = measureCommentService.queryMeasureValue(proNo, measureId, date);
            result.setMessage("查询指标趋势成功");
            result.setData(data);
        } catch (Exception e) {
            logger.error("queryMeasureValue exception, error:", e);
            result.setCode("500");
            result.setMessage("查询指标趋势失败");
            result.setData(new HashMap());
        }
        return result;
    }

    @RequestMapping("/getCrreateTime")
    @ResponseBody
    public PlainResponse<List<String>> getCrreateTime(int num, boolean flag) {
        PlainResponse<List<String>> result = new PlainResponse<List<String>>();
        try {
            result.setData(DateUtils.getCrreateTime(num, flag));
            result.setCode("success");
        } catch (Exception e) {
            logger.error("queryProjectCycle exception, error:", e);
            result.setCode("fail");
            result.setErrorMessage("error", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getCostMonth")
    @ResponseBody
    public PlainResponse<List<String>> getCostMonth(String startDate) {
        PlainResponse<List<String>> result = new PlainResponse<List<String>>();
        try {
            result.setData(measureCommentService.getCostMonth(startDate));
            result.setCode("success");
        } catch (Exception e) {
            logger.error("DateUtils.getRecentMonths, error:", e);
            result.setCode("fail");
            result.setErrorMessage("error", e.getMessage());
        }
        return result;
    }

    @RequestMapping("/getCostWeek")
    @ResponseBody
    public PlainResponse<List<Map<String, Object>>> getCostWeek(String selectDate, int num) {
        PlainResponse<List<Map<String, Object>>> result = new PlainResponse<List<Map<String, Object>>>();
        try {
            result.setData(measureCommentService.getCostWeek(selectDate, num));
            result.setCode("success");
        } catch (Exception e) {
            logger.error("DateUtils.getRecentMonths, error:", e);
            result.setCode("fail");
            result.setErrorMessage("error", e.getMessage());
        }
        return result;
    }
}
