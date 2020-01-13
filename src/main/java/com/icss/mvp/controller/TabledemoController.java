package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.entity.IterativeWorkManage;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.IterativeWorkManageService;

@Controller
@RequestMapping("/tabledemoController")
@SuppressWarnings("all")
public class TabledemoController {

	@Autowired
	private IterativeWorkManageService service;

	@RequestMapping(value = "/tabledemo", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public TableSplitResult tabledemo(HttpServletRequest request) {
		String iteId = StringUtils.isEmpty(request.getParameter("iteId")) ? "" : request.getParameter("iteId");
		String prior = StringUtils.isEmpty(request.getParameter("prior")) ? "" : request.getParameter("prior");
		String proNo = StringUtils.isEmpty(request.getParameter("proNo")) ? "" : request.getParameter("proNo");
		String status = StringUtils.isEmpty(request.getParameter("status")) ? "" : request.getParameter("status");
		String topic = StringUtils.isEmpty(request.getParameter("topic")) ? "" : request.getParameter("topic");
		int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));// 每页显示条数
		int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));// 第几页
		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");// 排序字段
		sort = transIntoSqlChar(sort);
		String sortOrder = StringUtils.isEmpty(request.getParameter("order")) ? ""
				: request.getParameter("order");// 排序方式
		TableSplitResult page = new TableSplitResult();
		page.setPage(offset);
		page.setRows(limit);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("iteId", iteId);
		map.put("prior", prior);
		map.put("topic", topic);
		map.put("proNo", proNo);
		map.put("status", status);
		page.setQueryMap(map);
//		page = service.queryIteWorkManageInfo(page, proNo, sort, sortOrder);
		return page;
	}

	private String transIntoSqlChar(String sort) {
		String sortName = "";
		if ("prior".equals(sort)) {
			sortName = "prior";
		} else if ("status".equals(sort)) {
			sortName = "status";
		} else if ("iteId".equals(sort)) {
			sortName = "ite_id";
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
		}
		return sortName;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editIteWorkManageInfo(@RequestBody IterativeWorkManage iterativeWorkManage) {
		BaseResponse result = new BaseResponse();
		try {
			service.editIteWorkManageInfo(iterativeWorkManage);
			result.setCode("success");
		} catch (Exception e) {
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
}
