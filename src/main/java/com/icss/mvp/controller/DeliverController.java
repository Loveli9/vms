package com.icss.mvp.controller;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.Deliver;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.DeliverService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjianbin
 * @date 2019/12/06
 */
@RestController @RequestMapping("/deliver") public class DeliverController {

    private Logger logger = Logger.getLogger(DeliverController.class);

    @Autowired DeliverService deliverService;

    @RequestMapping("/queryList") @ResponseBody public TableSplitResult queryList(HttpServletRequest request) {
        String iteName = request.getParameter("iteName") == null ? "" : request.getParameter("iteName");
        String username=request.getParameter("username");
        String proNo = StringUtils.isNotBlank(request.getParameter("proNo")) ? request.getParameter("proNo") : request.getParameter("projNo");
        int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
        // 每页显示条数
        int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
        // 第几页
        String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");
        String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder")) ? "" : request.getParameter(
                "sortOrder");
        TableSplitResult page = new TableSplitResult();
        page.setPage(offset);
        page.setRows(limit);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("iteName", iteName);
        map.put("proNo", proNo);
        map.put("username", request.getParameter("username"));
        page.setQueryMap(map);
        if("admin".equals(username) || StringUtils.isNotBlank(proNo)){
            page = deliverService.queryAll(page, sort, sortOrder);
        }else {
            page = deliverService.queryList(page, sort, sortOrder);
        }
        return page;
    }

    @RequestMapping("/add") @ResponseBody public Map add(@RequestBody Deliver deliver) {
        Map<String, Object> map = new HashMap<>(16);
        if (deliver.getName() == null) {
            map.put("code", "fail");
        } else {
            deliverService.add(deliver);
            map.put("code", "success");
        }

        return map;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST) @ResponseBody public Map<String, Object> editPage(
            String id) {
        Map<String, Object> map = new HashMap<String, Object>(16);
        try {
            Deliver deliver = deliverService.queryEditPageInfo(id);
            map.put("code", "success");
            map.put("rows", deliver);
        } catch (Exception e) {
            map.put("code", "failure");
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    @RequestMapping("/update") @ResponseBody public Map<String, Object> update(@RequestBody Deliver deliver) {
        Map<String, Object> map = new HashMap<String, Object>(16);
        try {
            deliverService.update(deliver);
            map.put("code", "success");
        } catch (Exception e) {
            map.put("code", "failure");
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * @Description:删除当前选中的支付件信息 @param @param ids @param @return 参数 @return
     * BaseResponse 返回类型 @throws
     */
    @RequestMapping("/delete") @ResponseBody public BaseResponse delete(@RequestBody String[] ids) {
        BaseResponse result = new BaseResponse();
        try {
            deliverService.delete(ids);
            result.setCode("success");
        } catch (Exception e) {
            result.setCode("faliure");
            logger.error(e.getMessage(), e);
            result.setError(CommonResultCode.EXCEPTION, e.getMessage());
        }
        return result;
    }
}
