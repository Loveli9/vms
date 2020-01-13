package com.icss.mvp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.IQmsMaturityDao;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.Qmsproblem;
import com.icss.mvp.entity.Qmsresult;
import com.icss.mvp.entity.QualityMonthlyReport;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.QmsMaturityService;

@Controller
@RequestMapping("/qmsMaturity")
public class QmsMaturityController {

	private final static Logger LOG = Logger.getLogger(QmsMaturityController.class);

	@Autowired
	private QmsMaturityService qmsMaturityService;
	
	@Autowired
	private IQmsMaturityDao qmsMaturityDao;

	@Resource
	private HttpServletRequest request;

	/**
	 * 查询QMS成熟度类别
	 * 
	 * @return
	 */
	@RequestMapping("/qmsTypes")
	@ResponseBody
	public PlainResponse<List<String>> qmsTypes() {
		PlainResponse<List<String>> result = new PlainResponse<>();
		try {
			result.setData(qmsMaturityDao.qmsTypes());
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询QMS问题类别
	 * 
	 * @return
	 */
	@RequestMapping("/qmsProblemType")
	@ResponseBody
	public PlainResponse<List<Qmsproblem>> qmsProblemType(String source) {
		PlainResponse<List<Qmsproblem>> result = new PlainResponse<>();
		try {
			result.setData(qmsMaturityService.qmsProblemType(source));
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询QMS成熟度列表
	 * 
	 * @param no
	 * @param source
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/queryQMSresults")
	@ResponseBody
	public PlainResponse<List<Qmsresult>> queryQMSresults(String no, String source , String select){
		PlainResponse<List<Qmsresult>> result = new PlainResponse<>();
		try {
			source = new String(source.getBytes("ISO8859-1"), "UTF-8");
			result.setData(qmsMaturityService.queryQMSresults(no,source ,select));
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e.getMessage());
		}
		return result;
	}
	/** 
	 * QMS成熟度列表保存
	 * @param Qmsresult
	 * @return
	 */
	@RequestMapping(value = "/queryQMSresultsSave", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse queryQMSresultsSave( @RequestBody Qmsresult Qmsresult){
		BaseResponse result = new BaseResponse();
		try {
			Qmsresult.setType("3");
			qmsMaturityDao.queryQMSresultsSave(Qmsresult);
			result.setCode("success");
		} catch (Exception e) {
			LOG.info(e.getMessage());
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	/** 
	 * QMS成熟度列表保存(前五个标签页)
	 * @param Qmsresult
	 * @return
	 */
	@RequestMapping(value = "/queryQMSresultsSaves", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse queryQMSresultsSaves( @RequestBody List<Map<String, Object>> list){
		BaseResponse result = new BaseResponse();
		try {
			qmsMaturityDao.queryQMSresultsSaves(list);
			result.setCode("success");
		} catch (Exception e) {
			LOG.info(e.getMessage());
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	/**
	 * 查询指定QMS成熟度
	 * 
	 * @param no
	 * @param source
	 * @return
	 */
	@RequestMapping("/queryQMS")
	@ResponseBody
	public PlainResponse<Qmsresult> queryQMS(@RequestBody Qmsresult qmsresult) {
		PlainResponse<Qmsresult> result = new PlainResponse<>();
		try {
			result.setData(qmsMaturityService.queryQMS(qmsresult.getNo(), String.valueOf(qmsresult.getId())));
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e.getMessage());
		}
		return result;
	}

	/**
	 * 修改后保存
	 * 
	 * @param qmsresult
	 */
	@RequestMapping(value = "/replaceQMSresult", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse replaceQMSresult(@RequestBody Qmsresult qmsresult) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			qmsMaturityService.replaceQMSresult(qmsresult);
			baseResponse.setCode("200");
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e.getMessage());
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

	/**
	 * 范围判断
	 * 
	 * @param
	 */
	@RequestMapping("/qmsRanges")
	@ResponseBody
	public BaseResponse qmsRanges(Integer qmsId, String type) {
		BaseResponse baseResponse = new BaseResponse();
		try {
			List<Integer> ranges = qmsMaturityService.qmsRanges(qmsId);
			if ("prve".equals(type) && qmsId - 1 < ranges.get(0)) {
				baseResponse.setCode("1");
			} else if ("next".equals(type) && qmsId + 1 > ranges.get(ranges.size() - 1)) {
				baseResponse.setCode("10");
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e.getMessage());
			baseResponse.setError(CommonResultCode.EXCEPTION, e.getMessage());
		}
		return baseResponse;
	}

	/** 
	 * 获取项目中每个标签页的符合度
	 * @param no
	 * @return
	 */
	@RequestMapping("/sumConform")
	@ResponseBody
	public PlainResponse<Map<String, Object>> sumConform(String no,String source) {
		PlainResponse<Map<String, Object>> result = new PlainResponse<>();
		result.setData(qmsMaturityService.sumConform(no,source));
		return result;
	}
	
	/**
	 * QMS项目维度信息
	 */
	@RequestMapping("/qmsDimension")
	@ResponseBody
	public TableSplitResult<List<QualityMonthlyReport>> qmsDimension(ProjectInfo projectInfo,PageRequest pageRequest) {
		TableSplitResult<List<QualityMonthlyReport>> ret = new TableSplitResult<List<QualityMonthlyReport>>();
		try {
			pageRequest.setPageNumber(pageRequest.getPageNumber()/pageRequest.getPageSize()+1);
			ret = qmsMaturityService.qmsDimension(projectInfo,pageRequest);
		} catch (Exception e) {
			LOG.error("查询数据异常", e);
		}
		return ret;
	}
	
	/**
	 * QMS报表
	 */
	@RequestMapping("/qmsReport")
	@ResponseBody
	public TableSplitResult<List<Qmsproblem>> qmsReport(ProjectInfo projectInfo) {
		TableSplitResult<List<Qmsproblem>> ret = new TableSplitResult<List<Qmsproblem>>();
		try {
			ret = qmsMaturityService.qmsReport(projectInfo);
		} catch (Exception e) {
			LOG.error("查询数据异常", e);
		}
		return ret;
	}
	/**
	 * qms饼状图
	 * @param projectInfo
	 * @return
	 */
	@RequestMapping("/qmsSector")
	@ResponseBody
	public TableSplitResult<Map<String, Object>> qmsSector(ProjectInfo projectInfo) {
		TableSplitResult<Map<String, Object>> ret = new TableSplitResult<Map<String, Object>>();
		try {
			ret.setRows(qmsMaturityService.qmsSector(projectInfo));
		} catch (Exception e) {
			LOG.error("查询数据异常", e);
		}
		return ret;
	}
	/**
	 * qms柱状图
	 * @param projectInfo
	 * @return
	 */
	@RequestMapping("/qmsHistogram")
	@ResponseBody
	public TableSplitResult<Map<String, Object>> qmsHistogram(ProjectInfo projectInfo) {
		TableSplitResult<Map<String, Object>> ret = new TableSplitResult<Map<String, Object>>();
		try {
			ret.setRows(qmsMaturityService.qmsHistogram(projectInfo));
		} catch (Exception e) {
			LOG.error("查询数据异常", e);
		}
		return ret;
	}
	
	/**
	 * 获取qms下拉菜单中的数据
	 * @param source
	 * @return
	 */
	@RequestMapping("/multipleMenu")
	@ResponseBody
	public PlainResponse<List<String>> multipleMenu(String source) {
		PlainResponse<List<String>> result = new PlainResponse<>();
		result.setData(qmsMaturityService.multipleMenu(source));
		return result;
	}
	
	@RequestMapping("/getSelectQuestion")
	@ResponseBody
	public ListResponse<Map<String, Object>> getSelectQuestion(String source){
		ListResponse<Map<String, Object>> response = new ListResponse<Map<String, Object>>();
		List<Map<String, Object>> data = new ArrayList<>(); 
		try {
			data = qmsMaturityService.getSelectQuestion(URLDecoder.decode(source,"UTF-8"));
			response.setCode("success");
			response.setData(data);
		} catch (Exception e) {
			response.setCode("failure");
			response.setMessage("操作失败");
		}
		return response;
    }
	
	@RequestMapping("/saveSelectQuestion")
	@ResponseBody
	public BaseResponse saveSelectQuestion(String no,String qmsId,String pid){
		BaseResponse result = new BaseResponse();
		try {
			qmsMaturityDao.saveSelectQuestion(no,qmsId,pid);
			result.setCode("success");
		} catch (Exception e) {
			LOG.info(e.getMessage());
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/qmsReportSave", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse qmsReportSave( @RequestBody Qmsproblem Qmsproblem){
		BaseResponse result = new BaseResponse();
		Qmsproblem.setType("3");
		try {
			qmsMaturityDao.qmsReportSave(Qmsproblem);
			result.setCode("success");
		} catch (Exception e) {
			LOG.info(e.getMessage());
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
	
	/**
	 * QMS导入
	 * @param file
	 * @return
	 */
	@RequestMapping("/importQMS")
	@ResponseBody
	public Map<String, Object> importQMS(@RequestParam(value = "file") MultipartFile file) {
		Map<String, Object> result = qmsMaturityService.importQMS(file);
		return result;
	}
	
	/**
	 * 汇总-QMS项目 信息导出
	 * @param response
	 */
	@RequestMapping("/exportQMS")
	public void exportQMS(HttpServletResponse response,String date) {
		try {
			byte[] fileContents = qmsMaturityService.exportQMS(date);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = "汇总-QMS项目信息" + sf.format(new Date()).toString() + ".xlsx";
			// 设置response参数，可以打开下载页面
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));

			response.getOutputStream().write(fileContents);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			LOG.error("导出失败", e);
		}
	}
	
	@RequestMapping("/qmsSelfchecking")
	@ResponseBody
	public List<QualityMonthlyReport> qmsSelfchecking(String month) {
		List<QualityMonthlyReport> ret = new ArrayList<>();
		try {
			ret = qmsMaturityService.qmsSelfchecking(month);
		} catch (Exception e) {
			LOG.error("查询数据异常", e);
		}
		return ret;
	}
	
	@RequestMapping("/getPersonLiable")
	@ResponseBody
	public List<Qmsresult> getPersonLiable(String no) {
		List<Qmsresult> ret = new ArrayList<>();
		try {
			ret = qmsMaturityDao.getPersonLiable(no);
		} catch (Exception e) {
			LOG.error("查询数据异常", e);
		}
		return ret;
	}
	
	@RequestMapping("/savePersonLiable")
	@ResponseBody
	public BaseResponse savePersonLiable(String no,String qmsId,String account){
		BaseResponse result = new BaseResponse();
		try {
			qmsMaturityDao.savePersonLiable(no,qmsId,account);
			result.setCode("success");
		} catch (Exception e) {
			LOG.info(e.getMessage());
			result.setCode("failure");
			result.setMessage("操作失败");
		}
		return result;
	}
}
