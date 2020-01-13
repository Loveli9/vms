package com.icss.mvp.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.ParameterInfoNew;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.service.PcbListService;
import com.icss.mvp.util.CookieUtils;

/**
 * 月指标计算
 * 
 * @author chenweipu
 *
 */
@Controller
@RequestMapping("/pcbList")
public class PcbListController {
	private static Logger logger = Logger.getLogger(PcbListController.class);

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PcbListService pcbListService;

	/**
	 * @Description:加载Pbc测试效率指标
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getPcbTestEfficient")
	@ResponseBody
	public ListResponse<Object> getPcbTestEfficient(@ModelAttribute ProjectInfo projectInfo) {
		ListResponse<Object> result = new ListResponse<Object>();
		try {
			String[] ids = new String[] { "45", "71" };
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectInfo.setProjectState(URLDecoder.decode(projectInfo.getProjectState(), "UTF-8"));
			result.setData(pcbListService.getPcbTestEfficient(projectInfo, username,ids));
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
	 * @Description:加载Pbc测试效率指标，以项目为单位
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getPcbTestEfficientXM")
	@ResponseBody
	public ListResponse<Map<String, Object>> getPcbTestEfficientXM(@ModelAttribute ProjectInfo projectInfo) {
		ListResponse<Map<String, Object>> result = new ListResponse<Map<String, Object>>();
		try {
			String[] ids = new String[] { "45", "71" };
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			projectInfo.setProjectState(URLDecoder.decode(projectInfo.getProjectState(), "UTF-8"));
			result.setData(pcbListService.getPcbTestEfficientXM(projectInfo, username, ids));
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
	 * @Description:加载Pbc指标值,项目汇总
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getPcbMeasureValue")
	@ResponseBody
	public ListResponse<Object> getPcbMeasureValue(ProjectInfo projectInfo) {
		ListResponse<Object> result = new ListResponse<Object>();		
		try {
			String[] ids = projectInfo.getMeasureId().split(",");
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			result.setData(pcbListService.getPcbValuesByMouth(projectInfo, username, ids));
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
	 * @Description:加载Pbc指标，以项目为单位导出
	 * @author 156425
	 * @date 2018年11月1日
	 */
	@RequestMapping("/exportResearch")
	@ResponseBody
	public void exportResearch(HttpServletResponse response, String nos) {
		try {
			String no = nos.substring(0,nos.length()-1);
            byte[] fileContents = pcbListService.exportResearch(no);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "研发效率信息" + sf.format(new Date()).toString() + ".xlsx";
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes(), "iso-8859-1"));

            response.getOutputStream().write(fileContents);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("导出失败", e);
        }
	}
	/**
	 * @Description:加载Pbc指标，以项目为单位
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getPcbMeasureValueXM")
	@ResponseBody
	public PageResponse<Map<String, Object>> getPcbMeasureValueXM(ProjectInfo projectInfo, PageRequest pageRequest) {
		PageResponse<Map<String, Object>> result = new PageResponse<>();
		try {
			String[] ids = projectInfo.getMeasureId().split(",");
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			result = pcbListService.getPcbValuesToday(projectInfo, username, ids, pageRequest);
			result.setMessage("返回成功");
			result.setCode("success");
		} catch (Exception e) {
			logger.error("交易失败", e);
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	
	@RequestMapping(value = "/editPcbProjectConfig")
	@ResponseBody
	public BaseResponse editPcbProjectConfig(@RequestBody List<Map<String,Object>> list){
		BaseResponse result = new BaseResponse();
		try {			
			pcbListService.editPcbProjectConfig(list);		
			result.setCode("success");
		} catch (Exception e) {
			logger.error("配置失败：", e);
			result.setCode("failure");
			result.setMessage("配置失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/insertPcbProjectConfig")
	@ResponseBody
	public BaseResponse insertPcbProjectConfig(@RequestBody List<Map<String,Object>> list){
		BaseResponse result = new BaseResponse();
		try {			
			pcbListService.insertPcbProjectConfig(list);		
			result.setCode("success");
		} catch (Exception e) {
			logger.error("配置失败：", e);
			result.setCode("failure");
			result.setMessage("配置失败");
		}
		return result;
	}

	/**
	 * @Description:加载Pbc迭代效率,项目汇总
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getPcbIterativeEfficiency")
	@ResponseBody
	public ListResponse<Object> getPcbIterativeEfficiency(ProjectInfo projectInfo) {
		ListResponse<Object> result = new ListResponse<Object>();
		try {
			String[] ids = projectInfo.getMeasureId().split(",");
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			result.setData(pcbListService.getPcbValuesByIteration(projectInfo, username, ids));
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
	 * @Description:加载Pbc迭代指标，以项目为单位导出
	 * @author 156425
	 * @date 2018年11月1日
	 */
	@RequestMapping("/exportIteration")
	@ResponseBody
	public void exportIteration(HttpServletResponse response, String nos) {
		try {
			String no = nos.substring(0,nos.length()-1);
            byte[] fileContents = pcbListService.exportIteration(no);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "迭代效率信息" + sf.format(new Date()).toString() + ".xlsx";
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes(), "iso-8859-1"));

            response.getOutputStream().write(fileContents);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("导出失败", e);
        }
	}
	/**
	 * @Description:加载Pbc迭代效率指标，以项目为单位
	 * @author chenweipu
	 * @date 2018年7月9日
	 */
	@RequestMapping("/getPcbIterativeEfficiencyXM")
	@ResponseBody
	public PageResponse<Map<String, Object>> getPcbIterativeEfficiencyXM(ProjectInfo projectInfo, PageRequest pageRequest) {
		PageResponse<Map<String, Object>> result = new PageResponse<>();
		try {
			String[] ids = projectInfo.getMeasureId().split(",");
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
			result = pcbListService.getPcbValuesTodayMeasure(projectInfo, username, ids, pageRequest);
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
	 * @Description:修改指标上下限 目标值
	 * @author zqq
	 * @date 
	 */
	@RequestMapping("/updateParameterInfoById")
	@ResponseBody
	public PageResponse<Map<String, Object>> updateParameterInfoById(@RequestBody ParameterInfoNew parameterInfoNew) {
		PageResponse<Map<String, Object>> result = new PageResponse<>();
		if(parameterInfoNew == null || parameterInfoNew.getId() == null) {
			logger.error("修改指标上下限失败，参数错误parameterInfoNew：" + parameterInfoNew);
			result.setMessage("返回失败");
			result.setCode("failure");
			return result;
		}
		try {
			Integer count = pcbListService.updateParameterInfoById(parameterInfoNew);
			if(count == 0) {
				logger.error("修改指标上下限数据库执行失败，parameterInfoNew：" + parameterInfoNew);
				result.setMessage("返回失败");
				result.setCode("failure");
			}else {
				result.setMessage("返回成功");
				result.setCode("success");
			}
		} catch (Exception e) {
			logger.error("交易失败", e);
			result.setMessage("返回失败");
			result.setCode("failure");
		}
		return result;
	}
	
	
}
