package com.icss.mvp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ProjectParameterValue;
import com.icss.mvp.service.ProjectParameterValueService;

/**
 * 
 * @author diaokeqi
 * 
 */
@Controller
@RequestMapping("/projParaValue")
public class ProjectParmeterValueController
{

	@Resource
	private ProjectParameterValueService projectParameterValueService;

	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> getList(
			ProjectParameterValue projectParameterValue, Page page)
	{
		return projectParameterValueService
				.getList(projectParameterValue, page);
	}

	@RequestMapping("/update")
	@ResponseBody
	public Map<String, Object> updateParameterValue(
			ProjectParameterValue paraValue, Page page)
	{
		// Date update_date = new Date();
		// paraValue.setUpdate_date(update_date);
		// paraValue.setMonth(paraValue.getMonth()+"-22 22:22:22");
		return projectParameterValueService.updateParameterValue(paraValue);

	}

	@RequestMapping("paramTemplate")
	@ResponseBody
	public void downloadParamTemplate(HttpServletResponse response, ProjectParameterValue paramVo, String date)
			throws IOException, ParseException
	{
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM");
		paramVo.setYearMonth(sf1.parse(date));
		byte[] bytes = projectParameterValueService.getTemplateBtyes(paramVo);

		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "项目参数信息模板" + sf.format(new Date()).toString()
				+ ".xlsx";
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	@RequestMapping("/import")
	@ResponseBody
	public Map<String, Object> importParamList(@RequestParam(value = "file") MultipartFile file, String projNo)
	{
		Map<String, Object> result = projectParameterValueService
				.importParams(file, projNo);
		return result;
	}
}
