package com.icss.mvp.controller;

import com.alibaba.fastjson.JSONObject;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.IterationMeasureIndex;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.IterationCycleService;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/iteration") @RestController @SuppressWarnings("all") public class IterationCycleController {

    private Logger logger = Logger.getLogger(IterationCycleController.class);

    @Autowired private IterationCycleService iterationCycleService;

    /**
     * @Description: 分页查询项目迭代信息 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    @RequestMapping(value = "/queryList", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public TableSplitResult query(
            HttpServletRequest request) {
        String code = request.getParameter("code") == null ? "" : request.getParameter("code");
        String iteName = request.getParameter("iteName") == null ? "" : request.getParameter("iteName");
        String proNo = request.getParameter("iteName") == null ? "" : request.getParameter("proNo");
        int limit =
                request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));// 每页显示条数
        int offset =
                request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));// 第几页
        String sort = TextUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");
        sort = transIntoSqlChar(sort);
        String sortOrder = TextUtils.isEmpty(request.getParameter("sortOrder")) ? "" : request.getParameter(
                "sortOrder");
        BaseResponse result = new BaseResponse();
        TableSplitResult page = new TableSplitResult();
        page.setPage(offset);
        page.setRows(limit);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("proNo",proNo);
        map.put("iteName", iteName);
        map.put("username", request.getParameter("username"));
        page.setQueryMap(map);
        page = iterationCycleService.queryIteInfoByPage(page, sort, sortOrder);
        return page;
    }

    /**
     * @Description:将表头字段映射为数据库字段 @param @param sort @param @return 参数 @return
     * String 返回类型 @throws
     */
    private String transIntoSqlChar(String sort) {
        String sortName = "";
        if ("code".equals(sort)) {
            sortName = "code";
        } else if ("iteName".equals(sort)) {
            sortName = "ite_name";
        } else if ("startDate".equals(sort)) {
            sortName = "start_date";
        } else if ("endDate".equals(sort)) {
            sortName = "end_date";
        } else if ("planStartDate".equals(sort)) {
            sortName = "plan_start_date";
        } else if ("planEndDate".equals(sort)) {
            sortName = "plan_end_date";
        }
        return sortName;
    }

    /**
     * 为项目添加迭代周期 proNo 项目编号 planStartDate 计划开始时间 planEndDate 计划结束时间 estimateWorkload
     * 估计工作量
     *
     * @param iterationCycle
     * @return
     */
    @RequestMapping(value = "/add") @ResponseBody public BaseResponse add(@RequestBody IterationCycle iterationCycle) {
        BaseResponse result = new BaseResponse();
        String mes = "";
        try {
            mes = iterationCycleService.insertIterationCycle(iterationCycle);
            result.setCode(mes);
        } catch (Exception e) {
            result.setCode(mes);
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return result;
    }

    /**
     * 更改项目迭代周期相关信息 planStartDate 计划开始时间 planEndDate 计划结束时间 estimateWorkload 估计工作量
     * startDate 实际开始时间 endDate 实际结束时间
     *
     * @param iterationCycle
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST) @ResponseBody public BaseResponse edit(
            @RequestBody IterationCycle iterationCycle) {
        BaseResponse result = new BaseResponse();
        try {
            iterationCycleService.editIterationCycle(iterationCycle);
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("failure");
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return result;
    }

    /**
     * @Description:迭代编辑页面回显 @param @param iterationCycle @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    @RequestMapping(value = "/editPage", method = RequestMethod.POST) @ResponseBody public Map<String, Object> editPage(
            String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            IterationCycle iterationCycle = iterationCycleService.queryEditPageInfo(id);
            map.put("code", "success");
            map.put("rows", iterationCycle);
        } catch (Exception e) {
            map.put("code", "failure");
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * @Description:删除当前选中的迭代管理信息 @param @param ids @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST) @ResponseBody public BaseResponse delete(
            @RequestBody String[] ids) {
        BaseResponse result = new BaseResponse();
        try {
            iterationCycleService.deleteIterationCycle(ids);
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("faliure");
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return result;
    }

    /**
     * @description: 获取迭代指标 @param proNo labId measureCate @return
     * PlainResponse @throws @author chengchenhui @date 2019/5/16
     * 15:42
     */
    @RequestMapping(value = "/measureReport", method = RequestMethod.POST) @ResponseBody public PlainResponse<JSONObject> measureReport(
            String no, String label, String category) {
        PlainResponse<JSONObject> result = new PlainResponse<>();
        try {
            JSONObject jsonObject = iterationCycleService.measureReport(no, label, category);
            result.setData(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
            return result;
        }
        return result;
    }

    @RequestMapping(value = "/iterationIndex/add", method = RequestMethod.POST) @ResponseBody public BaseResponse iterationIndexAdd(
            @RequestBody IterationMeasureIndex iterationMeasureIndex) {
        BaseResponse result = new BaseResponse();
        try {
            iterationCycleService.iterationIndexAdd(iterationMeasureIndex);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/iterationIndex/edit", method = RequestMethod.POST) @ResponseBody public BaseResponse iterationIndexEdit(
            @RequestBody IterationMeasureIndex iterationMeasureIndex, String proNo) {
        BaseResponse result = new BaseResponse();
        try {
            iterationCycleService.iterationIndexEdit(iterationMeasureIndex, proNo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return result;
    }

    /**
     * 根据项目编号查询迭代信息
     *
     * @param proNo
     * @return
     */
    @RequestMapping(value = "/getMessage", method = RequestMethod.POST) @ResponseBody public PlainResponse<List<IterationCycle>> getMessage(
            String proNo) {
        PlainResponse<List<IterationCycle>> result = new PlainResponse<>();
        try {
            result.setData(iterationCycleService.getMessage(proNo));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return result;
    }

    /**
     * @Description: 加载迭代名称下拉列表值 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    @RequestMapping("/getIteNameSelect") @ResponseBody public ListResponse<Map<String, Object>> getIteNameSelect(
            String proNo) {
        ListResponse result = new ListResponse();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> firstElement = new HashMap<>();
        firstElement.put("text", "未分配");
        firstElement.put("value", "wfp");
        firstElement.put("content", "pst");
        list.add(firstElement);
        try {
            List<Map<String, Object>> response = iterationCycleService.getIteNameSelect(proNo);
            if (response != null && !response.isEmpty()) {
                list.addAll(response);
            }
        } catch (Exception e) {
            result.setErrorMessage("faliure", e.getMessage());
            logger.error(e.getMessage(), e);
        }
        result.setData(list);
        return result;
    }

    /**
     * @Description: 校验迭代名称唯一性 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @author chengchenhui @throws
     */
    @RequestMapping("/checkIteName") @ResponseBody public BaseResponse checkIteName(String proNo, String iteName) {
        PlainResponse result = new PlainResponse();
        String flag = "";
        try {
            flag = iterationCycleService.checkIteName(proNo, iteName);
            result.setCode(flag);
        } catch (Exception e) {
            result.setCode(flag);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @Description: 保存页面截图，发邮件 @param imageDataB64 图片 @return BaseResponse
     * 返回类型 @throws
     */
    @RequestMapping("/saveImage") @ResponseBody public BaseResponse saveImage(String imageDataB64, String proNo) {
        PlainResponse result = new PlainResponse();
        String flag = "";
        try {
            flag = iterationCycleService.saveImage(imageDataB64, proNo);
            result.setCode(flag);
        } catch (Exception e) {
            result.setCode(flag);
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * @Description: 校验迭代时间顺序 @param @param iterationCycle @param @return 参数 @return
     * BaseResponse 返回类型 @author chengchenhui @throws
     */
    @RequestMapping("/checkStartTime") @ResponseBody public IterationCycle checkStartTime(String proNo) {
        IterationCycle iterationCycle = new IterationCycle();
        try {
            iterationCycle = iterationCycleService.checkStartTime(proNo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return iterationCycle;
    }

    /**
     * 查询项目名称
     *
     * @param projNo
     * @return
     */
    @RequestMapping("/getProjNoName") @ResponseBody public ProjectDetailInfo getProjNoName(String proNo) {
        return iterationCycleService.getProjNoName(proNo);
    }

    /**
     * 分页查询手工录入指标值
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/iterationIndexVlaue", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public TableSplitResult iterationIndexVlaue(
            HttpServletRequest request, String proNo) {
        String list = TextUtils.isEmpty(request.getParameter("list")) ? "" : request.getParameter("list");
        int limit =
                request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));// 每页显示条数
        int offset =
                request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));// 第几页
        TableSplitResult page = new TableSplitResult();
        page.setPage(offset);
        page.setRows(limit);
        return iterationCycleService.iterationIndexVlaue(page, proNo, list.split(","));
    }

    /**
     * @param no label category
     * @description: 可信指标
     * @author zhanghucheng
     * @date 2019/12/17
     */
    @RequestMapping(value = "/measureReport1", method = RequestMethod.GET) @ResponseBody public TableSplitResult<List<Map<String, Object>>> measureReport1(
            String no, String label, String category) {
        TableSplitResult<List<Map<String, Object>>> result = new TableSplitResult<List<Map<String, Object>>>();
        try {
            result = iterationCycleService.measureReport1(no, label, category);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

}
