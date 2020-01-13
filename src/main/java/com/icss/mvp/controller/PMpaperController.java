package com.icss.mvp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.service.PMpaperService;

@Controller
@RequestMapping("/PMpaper")
public class PMpaperController {

	private final static Logger LOG = Logger.getLogger(PMpaperController.class);

	@Autowired
	private PMpaperService pmService;

	/**
	 * 导出所有低产出人员
	 * 
	 * @return
	 */
	@RequestMapping("/exportPMpaper")
	@ResponseBody
	public void exportLowloc(String no, HttpServletResponse response) throws Exception {
		try {
			byte[] fileContents = pmService.exportLowloc(no);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "PM周报" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
	}

	/**
	 * 项目Bug统计
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/bugCount")
	@ResponseBody
	public TableSplitResult bugCount(HttpServletRequest request) {
		String no = StringUtils.isEmpty(request.getParameter("no")) ? "" : request.getParameter("no");
		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");// 排序字段
		sort = transIntoSqlCharBug(sort);
		String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder")) ? ""
				: request.getParameter("sortOrder");// 排序方式
		TableSplitResult data = pmService.dtsCount(no, sort, sortOrder);
		return data;
	}

	private String transIntoSqlCharBug(String sort) {
		String sortName = "";
		if ("critical".equals(sort)) {
			sortName = "critical";
		} else if ("major".equals(sort)) {
			sortName = "major";
		} else if ("minor".equals(sort)) {
			sortName = "minor";
		} else if ("suggestion".equals(sort)) {
			sortName = "suggestion";
		} else if ("alls".equals(sort)) {
			sortName = "alls";
		} else if ("di".equals(sort)) {
			sortName = "di";
		}
		return sortName;
	}

	/**
	 * 项目Story统计
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/storyCount")
	@ResponseBody
	public TableSplitResult storyCount(HttpServletRequest request) {
		String no = StringUtils.isEmpty(request.getParameter("no")) ? "" : request.getParameter("no");
		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");// 排序字段
		sort = transIntoSqlCharStory(sort);
		String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder")) ? ""
				: request.getParameter("sortOrder");// 排序方式
		TableSplitResult data = pmService.story(no, sort, sortOrder);
		return data;
	}

	private String transIntoSqlCharStory(String sort) {
		List<String> listName = new ArrayList<>();
		String sortName = "";
		for (int i = 1; i <= 6; i++) {
			listName.add("status" + String.valueOf(i));
		}
		if (listName.contains(sort)) {
			sortName = sort;
		} else if ("alls".equals(sort)) {
			sortName = "alls";
		}
		return sortName;
	}
	/**
	 * Bug提单详情
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/bugSubmission")
	@ResponseBody
	public TableSplitResult bugSubmission(HttpServletRequest request) {
		String no = StringUtils.isEmpty(request.getParameter("no")) ? "" : request.getParameter("no");
		String sort = StringUtils.isEmpty(request.getParameter("sort")) ? "" : request.getParameter("sort");// 排序字段
		sort = transIntoSqlCharStory(sort);
		String sortOrder = StringUtils.isEmpty(request.getParameter("sortOrder")) ? ""
				: request.getParameter("sortOrder");// 排序方式
		TableSplitResult data = pmService.bugSubmission(no, sort, sortOrder);
		return data;
	}

}
