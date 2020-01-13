package com.icss.mvp.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.service.ProjectOperationService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/projectOperation")
public class ProjectOperationController {
	
	private static Logger logger = Logger.getLogger(ProjectOperationController.class);
	@Autowired
	private ProjectOperationService projectOperationService;
	
	@RequestMapping("/queryProjectOperation")
	@ResponseBody
	public TableSplitResult<List<Map<String, String>>> queryProjectOperation(String proNo, PageRequest page) {
		TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<List<Map<String, String>>>();
		try {
			result = projectOperationService.queryProjectOperation(proNo, page);
		} catch (Exception e) {
			logger.error("queryProjectOperation exception, error:", e);
		}
		return result;
	}

	@RequestMapping("/queryProjectOperationClone")
	@ResponseBody
	public TableSplitResult<List<Map<String, String>>> queryProjectOperationClone(HttpServletRequest request, PageRequest page) {
		int limit = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
		int offset = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
		page.setPageSize(limit);
		page.setOffset(offset);
		TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<List<Map<String, String>>>();
		try {
			result = projectOperationService.queryProjectOperationClone(page);
		} catch (Exception e) {
			logger.error("queryProjectOperationClone exception, error:", e);
		}
		return result;
	}
}
