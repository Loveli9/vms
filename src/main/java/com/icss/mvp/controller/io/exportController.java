package com.icss.mvp.controller.io;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.entity.NetworkSecurity;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.io.ExportService;
import com.icss.mvp.util.AESOperator;
import com.icss.mvp.util.DateUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by up on 2018/10/9.
 */
@Controller
@RequestMapping("/export")
public class exportController {
	private static Logger logger = Logger.getLogger(exportController.class);
	@Autowired
	private ExportService exportService;

	@RequestMapping("/downloadWeekly")
	public void downloadWeekly(HttpServletResponse response) {
		try {
			byte[] fileContents = exportService.downloadWeekly();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "项目采集周报" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));

			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
	}

	@RequestMapping("/downloadIndex")
	public void downloadIndex(HttpServletResponse response) {
		try {
			byte[] fileContents = exportService.downloadIndex();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "部分项目指标采集" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));

			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
	}

	@RequestMapping("/downloadIndicators")
	public void downloadNew(HttpServletResponse response, ProjectInfo proj) {
		try {
			byte[] fileContents = exportService.exportExcel(proj.getNo());
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "指标信息" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));

			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
	}

	/**
	 * 导出qms问题跟踪信息
	 * 
	 * @param response
	 * @param proj
	 */
	@RequestMapping("/downloadProblemQMS")
	public void downloadProblemQMS(HttpServletResponse response, ProjectInfo proj) {
		try {
			byte[] fileContents = exportService.downloadProblemQMS(proj.getNo());
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "审计问题跟踪" + sf.format(new Date()).toString() + ".xlsx";
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

	@RequestMapping("/exportKX")
	@ResponseBody
	public void exportKX(HttpServletResponse response, ProjectInfo projectInfo, String date) {
		try {
			PageRequest pageRequest = new PageRequest();
			byte[] fileContents = exportService.exportKX(projectInfo, pageRequest, date);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "可信质量报表" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
	}

	@RequestMapping("/exportNetSecReport")
	@ResponseBody
	public void exportNetSecReport(HttpServletResponse response, NetworkSecurity netSec, String date, String type) {
		try {
			byte[] fileContents = exportService.exportNetSecReport(netSec, date, type);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "网络安全报表" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("交易失败", e);
		}
	}

	/*
	 * 返回加密字符串方法
	 */
	@RequestMapping("/encrypt")
	@ResponseBody
	public PlainResponse<String> encrypt(String orgion) {
		PlainResponse<String> result = new PlainResponse<String>();

		try {
			result.setData(AESOperator.getInstance().encrypt(orgion));
		} catch (Exception e) {
			result.setError(CommonResultCode.INTERNAL, e.getMessage());
		}
		return result;
	}

	/**
	 * 导出项目成本
	 * 
	 * @param response
	 * @param proNo
	 * @param date
	 */
	@RequestMapping("/exportXMCB")
	public void exportXMCB(HttpServletResponse response, String proNo, String date, String nextDate) {
		try {
			byte[] fileContents = exportService.exportXMCB(proNo, date, nextDate);
			String fileName = "项目成本" + DateUtils.SHORT_FORMAT_GENERAL.format(DateUtils.SHORT_FORMAT_GENERAL.parse(date))
					+ "——" + DateUtils.SHORT_FORMAT_GENERAL.format(DateUtils.SHORT_FORMAT_GENERAL.parse(nextDate)) + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));

			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("exportController exportXMCB 导出失败:", e);
		}
	}

	/*
	 * @RequestMapping("/downloadIndicatorsByTeamId") public void
	 * downloadIndicatorsByTeamId(HttpServletResponse response,ProjectInfo proj)
	 * { try { byte[] fileContents = exportService.exportExcel(proj.getNo());
	 * SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss"); String
	 * fileName = "指标信息" + sf.format(new Date()).toString() + ".xlsx"; //
	 * 设置response参数，可以打开下载页面 response.reset();
	 * response.setContentType("application/vnd.ms-excel;charset=utf-8");
	 * response.setHeader("Content-Disposition", "attachment;filename=" + new
	 * String(fileName.getBytes(), "iso-8859-1"));
	 * 
	 * response.getOutputStream().write(fileContents);
	 * response.getOutputStream().flush(); response.getOutputStream().close(); }
	 * catch (Exception e) { logger.error("交易失败", e); } }
	 */
}
