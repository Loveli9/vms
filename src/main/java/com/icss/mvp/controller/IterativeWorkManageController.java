package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.icss.mvp.entity.IterativeWorkManage;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.IterativeWorkManageService;

/**
 * @author chengchenhui
 * @ClassName: IterativeWorkManage
 * @Description: 需求管理
 * @date 2018年8月3日
 */
@Controller @RequestMapping("/IteManage") @SuppressWarnings("all") public class IterativeWorkManageController {

    @Autowired private IterativeWorkManageService service;

    private static Logger logger = Logger.getLogger(IterativeWorkManageController.class);

    /**
     * @Description: 分页查询需求管理信息 @param @return 参数 @return
     * PageResponse<IterativeWorkManage> 返回类型 @throws
     */
    @RequestMapping(value = "/queryByPage", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public TableSplitResult queryIteWorkManageInfo(
            HttpServletRequest request) {
        String iteId = StringUtils.isEmpty(request.getParameter("iteId")) ? "" : request.getParameter("iteId");
        String proNo = StringUtils.isEmpty(request.getParameter("proNo")) ? "" : request.getParameter("proNo");
        String status = StringUtils.isEmpty(request.getParameter("status")) ? "" : request.getParameter("status");
        String topic = StringUtils.isEmpty(request.getParameter("topic")) ? "" : request.getParameter("topic");
        String type = StringUtils.isEmpty(request.getParameter("type")) ? "" : request.getParameter("type");
        String hwAccount = StringUtils.isEmpty(request.getParameter("hwAccount")) ? "" : request.getParameter(
                "hwAccount");
        // if (!"ready".equals(type)) {
        // hwAccount = null;
        // }
        try {
            topic = new String(topic.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.debug(e);
            logger.error("topic.getBytes exception, error: " + e.getMessage());
        }
        int limit =
                request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));// 每页显示条数
        int offset =
                request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));// 第几页
        String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");// 排序字段
        sort = transIntoSqlChar(sort);
        String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder")) ? "" : request.getParameter(
                "sortOrder");// 排序方式
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("iteId", iteId);
        map.put("topic", topic);
        map.put("status", status);
        TableSplitResult page = new TableSplitResult();
        page.setPage(offset);
        page.setRows(limit);
        page.setQueryMap(map);
        page = service.queryIteWorkManageInfo(page, proNo, type, hwAccount, sort, sortOrder);
        return page;
    }

    /**
     * @Description:将表头字段映射为数据库字段 @param @param sort @param @return 参数 @return
     * String 返回类型 @throws
     */
    private String transIntoSqlChar(String sort) {
        String sortName = "";
        if ("prior".equals(sort)) {
            sortName = "prior";
        } else if ("status".equals(sort)) {
            sortName = "status";
        } else if ("change".equals(sort)) {
            sortName = "change";
        } else if ("iteType".equals(sort)) {
            sortName = "ite_type";
        } else if ("importance".equals(sort)) {
            sortName = "importance";
        } else if ("expectHours".equals(sort)) {
            sortName = "expect_hours";
        } else if ("actualHours".equals(sort)) {
            sortName = "actual_hours";
        } else if ("wrField".equals(sort)) {
            sortName = "wr_field";
        } else if ("createTime".equals(sort)) {
            sortName = "create_time";
        } else if ("updateTime".equals(sort)) {
            sortName = "update_time";
        } else if ("iteId".equals(sort)) {
            sortName = "plan_start_date";
        } else if ("topic".equals(sort)) {
            sortName = "topic";
        } else if ("creator".equals(sort)) {
            sortName = "creator";
        } else if ("solvePerson".equals(sort)) {
            sortName = "solve_person";
        } else if ("finalimit".equals(sort)) {
            sortName = "finalimit";
        } else if ("version".equals(sort)) {
            sortName = "version";
        } else if ("proNo".equals(sort)) {
            sortName = "pro_no";
        } else if ("startTime".equals(sort)) {
            sortName = "start_time";
        } else if ("endTime".equals(sort)) {
            sortName = "end_time";
        } else if ("describeInfo".equals(sort)) {
            sortName = "describe_info";
        } else if ("isDeleted".equals(sort)) {
            sortName = "is_deleted";
        } else if ("planStartTime".equals(sort)) {
            sortName = "plan_start_time";
        } else if ("planEndTime".equals(sort)) {
            sortName = "plan_end_time";
        } else if ("codeAmount".equals(sort)) {
            sortName = "code_amount";
        } else if ("personLiable".equals(sort)) {
            sortName = "person_liable";
        } else if ("id".equals(sort)) {
            sortName = "id";
        } else if ("document".equals(sort)) {
            sortName = "document";
        } else if ("demandCode".equals(sort)) {
            sortName = "demand_code";
        } else if ("demandType".equals(sort)) {
            sortName = "demand_type";
        } else if ("alertContent".equals(sort)) {
            sortName = "alert_content";
        } else if ("design".equals(sort)) {
            sortName = "design";
        } else if ("luNumber".equals(sort)) {
            sortName = "lu_number";
        } else if ("testNumber".equals(sort)) {
            sortName = "test_number";
        } else if ("checkResult".equals(sort)) {
            sortName = "check_result";
        } else if ("remarks".equals(sort)) {
            sortName = "remarks";
        }
        return sortName;
    }

    /**
     * @Description:新增需求管理信息 @param @param iteInfo @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    @RequestMapping("/add") @ResponseBody public BaseResponse addIteWorkManageInfo(
            @RequestBody IterativeWorkManage iteInfo, HttpServletRequest request) {
        BaseResponse result = new BaseResponse();
        int count = service.topicCount(iteInfo.getProNo(), iteInfo.getTopic());
        if (count < 1) {
            try {
                service.addIteWorkManageInfo(iteInfo, request);
            } catch (Exception e) {
                logger.error("新增失败：", e);
            }
            result.setCode("success");
        } else {
            // 如果数据库中存在相同topic，不允许添加
            result.setCode("failure");
        }
        return result;
    }

    /**
     * @Description:编辑需求管理信息 @param @return 参数 @return BaseResponse 返回类型 @throws
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST) @ResponseBody public BaseResponse editIteWorkManageInfo(
            @RequestBody IterativeWorkManage iterativeWorkManage) {
        BaseResponse result = new BaseResponse();
        try {
            // service.editIteWorkManageInfo(iterativeWorkManage);
            String mess = service.editIteWorkManageEdit(iterativeWorkManage);
            result.setCode(mess);
            // if ("error".equals(mess)) {
            // result.setCode(mess);
            // }
        } catch (Exception e) {
            logger.error("编辑失败：", e);
            result.setErrorMessage("failure", "操作失败");
        }
        return result;
    }

    /**
     * 显示待审核内容
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/compareCheck") @ResponseBody public Map<String, Object> compareCheck(String proNo,
                                                                                                   String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<String> report = service.compareCheck(proNo, id);
            map.put("code", "success");
            map.put("data", report);
            map.put("checkId", id);
        } catch (Exception e) {
            map.put("code", "failure");
            logger.error("service.compareCheck exception, error: " + e.getMessage());
        }
        return map;
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/checkResult") @ResponseBody public BaseResponse checkResult(String result, String id,
                                                                                          String person, String no) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            service.checkResult(result, id, person, no);
            baseResponse.setCode("200");
        } catch (Exception e) {
            logger.error("审核失败" + e.getMessage());
            baseResponse.setCode("failure");
        }
        return baseResponse;
    }

    /**
     * 撤销审核
     */
    @RequestMapping(value = "/revocation") @ResponseBody public BaseResponse revocation(String id) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            service.revocation(id);
            baseResponse.setCode("200");
        } catch (Exception e) {
            logger.error("撤销失败" + e.getMessage());
            baseResponse.setCode("failure");
        }
        return baseResponse;
    }

    /**
     * 项目组成员
     */
    @RequestMapping(value = "/members") @ResponseBody public List<ProjectMembersLocal> members(String proNo,
                                                                                               String hwAccount) {
        List<ProjectMembersLocal> list = null;
        try {
            list = service.members(proNo, hwAccount);
            if (list.size() == 0) {
                list = null;
            }
        } catch (Exception e) {
            logger.error("service.members exception, error: " + e.getMessage());
        }
        return list;
    }

    /**
     * @Description: 删除需求管理信息 @param @return 参数 @return BaseResponse 返回类型 @throws
     */
    @RequestMapping("/delete") @ResponseBody public Map<String, Object> deleteIteWorkManageInfo(
            @RequestBody String[] ids) {
        // BaseResponse result = new BaseResponse();
        Map<String, Object> map = null;
        try {
            map = service.deleteIteWorkManageInfo(ids);
            map.put("code", "success");
            // result.setCode("success");
        } catch (Exception e) {
            logger.error("删除失败：", e);
            map.put("code", "failure");
            // result.setCode("failure");
            // result.setMessage("操作失败");
        }
        return map;
    }

    /**
     * @Description: 删除需求管理信息 @param @return 参数 @return BaseResponse 返回类型 @throws
     */
    @RequestMapping(value = "/deleteIte") @ResponseBody public Map<String, Object> delete(@RequestBody String[] ids) {
        Map<String, Object> map = null;
        try {
            String id = ids[0];
            map = service.delete(id);
        } catch (Exception e) {
            logger.error("删除失败：", e);
        }
        return map;
    }

    /**
     * @Description: excel导入需求管理内容 @param 参数 @return void 返回类型 @throws
     */
    @RequestMapping("/import") @ResponseBody public Map<String, Object> importIteWorkInfo(
            @RequestParam(value = "file") MultipartFile file, @RequestParam(value = "proNo") String proNo,
            @RequestParam(value = "hwAccount") String hwAccount, HttpServletRequest request) {
        Map<String, Object> result = service.importIteWorkInfo(file, proNo, hwAccount, request);
        return result;
    }

    /**
     * @Description: 导出excel需求管理工作内容 @param 参数 @return void 返回类型 @throws
     */
    @RequestMapping("/export") @ResponseBody public void export(HttpServletResponse response,
                                                                HttpServletRequest request) {
        //        String proNo = TextUtils.isEmpty(request.getParameter("proNo1")) ? "" : request.getParameter("proNo1");
        String proNo = "HWHZP5FF1606F03X11";

        try {
            byte[] fileContents = service.export(proNo);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "需求管理信息" + sf.format(new Date()).toString() + ".xlsx";
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                               "attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
            response.getOutputStream().write(fileContents);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("导出失败", e);
        }
    }

    /**
     * @Description: 获取燃尽图 @param iteId 迭代id @return PlainResponse<List<Map<String,
     * Object>>> 返回类型 @throws
     */
    @RequestMapping("/burnoutFigure") @ResponseBody public PlainResponse<Map<String, List<String>>> burnoutFigure(
            String iteId) {
        PlainResponse result = new PlainResponse();
        try {
            result.setData(service.burnoutFigure(iteId));
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("faliure");
            logger.error("service.burnoutFigure exception, error: " + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/editCompletion") @ResponseBody public BaseResponse editPcbProjectConfig(
            @RequestBody List<Map<String, Object>> list) {
        BaseResponse result = new BaseResponse();
        try {
            service.editCompletion(list);
            result.setCode("success");
        } catch (Exception e) {
            logger.error("配置失败：", e);
            result.setErrorMessage("failure", "配置失败");
        }
        return result;
    }

    /**
     * @Description: 燃尽图任务进度 @param iteId 迭代id @throws
     */
    @RequestMapping("/completion") @ResponseBody public List<Map<String, Object>> completion(String iteId,
                                                                                             String proNo) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            result = service.completion(iteId, proNo);
        } catch (Exception e) {
            logger.error("service.completion exception, error: " + e.getMessage());
        }
        return result;
    }

    /**
     * @Description: 获取story @param iteId 迭代id @return
     * PlainResponse<List<Map<String, Object>>> 返回类型 @throws
     */
    @RequestMapping("/getStoryData") @ResponseBody public PlainResponse<Map<String, Object>> getStoryData(
            String iteId) {
        PlainResponse result = new PlainResponse();
        try {
            result.setData(service.getStoryData(iteId));
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("faliure");
            logger.error("service.getStoryData exception, error: " + e.getMessage());
        }
        return result;
    }

    /**
     * @Description: 获取story @param iteId 迭代id @return
     * PlainResponse<List<Map<String, Object>>> 返回类型 @throws
     */
    @RequestMapping("/getBugData") @ResponseBody public PlainResponse<Map<String, Object>> getBugData(String iteId) {
        PlainResponse result = new PlainResponse();
        try {
            result.setData(service.getBugData(iteId));
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("faliure");
            logger.error("service.getBugData exception, error: " + e.getMessage());
        }
        return result;
    }

    /**
     * @Description: bug提交 @throws
     */
    @RequestMapping("/bugSubmission") @ResponseBody public PlainResponse<Map<String, Object>> bugSubmission(String no) {
        PlainResponse result = new PlainResponse();
        try {
            result.setData(service.bugSubmission(no));
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("faliure");
            logger.error("service.bugSubmission exception, error: " + e.getMessage());
        }
        return result;
    }

    /**
     * @Description:需求编辑页面回显 @param @param iterationCycle @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    @RequestMapping(value = "/editPage") @ResponseBody public Map<String, Object> editPage(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            IterativeWorkManage iterativeWorkManage = service.openEditPage(id);
            map.put("code", "success");
            map.put("rows", iterativeWorkManage);
        } catch (Exception e) {
            map.put("code", "failure");
            logger.error("service.openEditPage exception, error: " + e.getMessage());
        }
        return map;
    }

    /**
     * @Description:获取项目成员下拉列表 @param @param iterationCycle @param @return
     * 参数 @return BaseResponse 返回类型 @throws
     */
    @RequestMapping("/meberSelect") @ResponseBody public PlainResponse<List<Map<String, Object>>> getProjectMebersSelect(
            String proNo) {
        PlainResponse result = new PlainResponse();
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = service.getProjectMebersSelect(proNo);
            result.setData(list);
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("fail");
            logger.error("service.getProjectMebersSelect exception, error: " + e.getMessage());
        }
        return result;
    }

    @RequestMapping("/exportNew") @ResponseBody public void exportNew(HttpServletRequest request,
                                                                      HttpServletResponse response) throws Exception {
        service.queryIteWorkManageAllNew(request, response);

    }

    /**
     * @Description: 分页查询需求管理信息 @param @return 参数 @return
     * PageResponse<IterativeWorkManage> 返回类型 @throws
     */
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST, consumes = "application/json") @ResponseBody public TableSplitResult queryAll(
            HttpServletRequest request) {
        String no = StringUtils.isEmpty(request.getParameter("proNo")) ? "" : request.getParameter("proNo");
        String iteId = StringUtils.isEmpty(request.getParameter("iteId")) ? "" : request.getParameter("iteId");
        String status = StringUtils.isEmpty(request.getParameter("status")) ? "" : request.getParameter("status");
        String topic = StringUtils.isEmpty(request.getParameter("topic")) ? "" : request.getParameter("topic");
        String hwAccount = StringUtils.isEmpty(request.getParameter("hwAccount")) ? "" : request.getParameter(
                "hwAccount");
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
        String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");
        sort = transIntoSqlChar(sort);
        String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder")) ? "" : request.getParameter(
                "sortOrder");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("no", no);
        if(!iteId.isEmpty()){
            String iteName=service.queryIteName(iteId);
            map.put("iteId", iteName);
        }
        map.put("topic", topic);
        map.put("status", status);
        TableSplitResult page = new TableSplitResult();
        page.setPage(offset);
        page.setRows(limit);
        page.setQueryMap(map);
        page = service.queryAll(page, hwAccount, sort, sortOrder);
        return page;
    }
}
