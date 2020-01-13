package com.icss.mvp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ProjectCommentsListInfo;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectManager;
import com.icss.mvp.entity.ProjectStatusLighting;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.service.ProjectListService;

/**
 * 
 * @author zhuwei
 * @see 项目度量信息列表
 * 
 */
@RestController
@RequestMapping("/project")
public class ProjectListController
{
	@Resource
	private HttpServletRequest request;
	@Resource
	private ProjectListService projectListService;
	@Resource
	private IProjectListDao projectListDao;
	
	private static Logger logger = Logger.getLogger(IterativeWorkManageController.class);

	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> queryList(ProjectInfo proj, Page page)
	{
		return projectListService.getList(proj, page);
	}

	@RequestMapping("/download")
	public void download(HttpServletResponse response, ProjectInfo proj) throws Exception
	{
		byte[] fileContents = projectListService.exportExcel(proj);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "项目信息" + sf.format(new Date()).toString() + ".xlsx";
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));

		response.getOutputStream().write(fileContents);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	@RequestMapping("/downloadNew")
	public void downloadNew(HttpServletResponse response) throws Exception
	{
		byte[] fileContents = projectListService.exportExcel(new ProjectInfo());
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "项目信息" + sf.format(new Date()).toString() + ".xlsx";
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));
		
		response.getOutputStream().write(fileContents);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	@RequestMapping("maintain")
	public String goMaintainPage(HttpServletRequest request, String no)
	{
		request.setAttribute("no", no);
		return "/projectMaintain/maintain";
	}

	@RequestMapping("parameterEditor")
	public String goParameterEditorPage(HttpServletRequest request, String projNo)
	{
		request.setAttribute("projNo", projNo);
		return "/projectMaintain/projectParameterEditor";
	}

	@RequestMapping("proj")
	public String goProjPage(HttpServletRequest request)
	{
		return "/projectList/projectList";
	}

	@RequestMapping("init")
	@ResponseBody
	public Map<String, Object> init()
	{
		return projectListService.getProjectSelectInfo();
	}

	@RequestMapping("addInfo")
	public String addProjInfo(HttpServletRequest request, String parma)
	{
		request.setAttribute("parma", parma);
		return "/projectInfo/projectInfo";
	}

	@RequestMapping("updateInfo")
	public String updateProjInfo(HttpServletRequest request, String no)
	{
		request.setAttribute("no", no);
		return "/projectInfo/projectInfo";
	}

	@RequestMapping("/insertInfo")
	@ResponseBody
	public Map<String,String> insertInfo(HttpServletRequest request) throws ParseException
	{
		ProjectDetailInfo proj=new ProjectDetailInfo();
		String param=request.getParameter("param");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 String date=df.format(new Date());
		 Date imptDate=df.parse(date);
		 proj.setImportDate(imptDate);
		 proj.setImportUser("导入者");
		 Date start=null;
		 Date end=null;
		 if(startTime!=""){
			 start=df.parse(startTime);
		 }
		 if(endTime!=""){
		  end=df.parse(endTime);
		 }
		 proj.setStartDate(start);
		 proj.setEndDate(end); 
		 proj.setNo(request.getParameter("no"));
		 proj.setName(request.getParameter("name"));
		 proj.setProjectType(request.getParameter("projectType"));
		 proj.setType(request.getParameter("type"));
		 proj.setBu(request.getParameter("bu"));
		 proj.setPdu(request.getParameter("pdu"));
		 proj.setDu(request.getParameter("du"));
		 proj.setArea(request.getParameter("area"));
		 proj.setPo(request.getParameter("po"));
		 proj.setVersion(request.getParameter("version"));
		 proj.setCountOfDay(projectListService.StringToDouble(request.getParameter("countOfDay")));
		 proj.setCountOfMonth(projectListService.StringToDouble(request.getParameter("countOfMonth")));
		 proj.setWorkerCount(projectListService.StringToDouble(request.getParameter("workerCount")));
		 proj.setPredictMoney(projectListService.StringToDouble(request.getParameter("predictMoney")));
		 ProjectManager projManger=new ProjectManager();
		  String manager=request.getParameter("manager");
		  projManger.setNo(proj.getNo());
		  projManger.setName(manager);
		  projManger.setPosition("PM");
		  projManger.setAccount(manager);
		  projManger.setStartDate(proj.getStartDate());
		  projManger.setEndDate(proj.getEndDate());
		  projManger.setImportDate(imptDate);
		 return projectListService.insertInfo(proj,param,projManger,imptDate);
		
	}
	
	@RequestMapping("/queryPM")
	@ResponseBody
	public Map<String, Object> queryName(String projNo)
	{

		return projectListService.queryName(projNo);
	}

	@RequestMapping("/isExitProj")
	@ResponseBody
	public String isExit(String no){

		ProjectDetailInfo projInfo=projectListDao.isExit(no);
		String message=null;
		if(projInfo!=null){
			return message="项目已存在，请重新输入！";
		}
		return message;
	}

	@RequestMapping("projectTemplate")
	@ResponseBody
	public void downloadParamTemplate(HttpServletResponse response,String templateName)
			throws IOException
	{
		InputStream is = this.getClass().getResourceAsStream(
				"/excel/"+templateName+"-template.xlsx");
		if(is==null) {
			return;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = templateName+"模板" + sf.format(new Date()).toString()
				+ ".xlsx";
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));

		response.getOutputStream().write(IOUtils.toByteArray(is));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	@RequestMapping("autowebload")
	@ResponseBody
	public void downloadcollectionweb(HttpServletResponse response,String autoName)
			throws IOException
	{
		InputStream is = this.getClass().getResourceAsStream(
				"/excel/"+autoName+".zip");
		if(is==null) {
			return;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = autoName+sf.format(new Date()).toString()
				+ ".zip";
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));
		
		response.getOutputStream().write(IOUtils.toByteArray(is));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	@RequestMapping("/import")
	@ResponseBody
	public Map<String, Object> importProjList(
			@RequestParam(value = "file") MultipartFile file)
	{
		Map<String, Object> result = projectListService.importProjects(file);
		return result;
   }
	
	@RequestMapping("/importpromember")
	@ResponseBody
	public Map<String, Object> importPromember(
			@RequestParam(value = "file") MultipartFile file)
	{
		Map<String, Object> result = projectListService.importPromember(file);
		return result;
   }
	@RequestMapping("/importpromemberOMBadd")
	@ResponseBody
	public Map<String, Object> importpromemberOMBadd(
			@RequestParam(value = "file") MultipartFile file)
	{
		Map<String, Object> result = projectListService.importpromemberOMBadd(file);
		return result;
	}
			
	@RequestMapping("/importclock")
	@ResponseBody
	public Map<String, Object> importClock(
			@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "time")String time)
	{
		Map<String, Object> result = projectListService.importClock(file,time);
		return result;
    }
	
	@RequestMapping("/importstar")
	@ResponseBody
	public Map<String, Object> importStar(
			@RequestParam(value = "file") MultipartFile file)
	{
		Map<String, Object> result = projectListService.importStar(file);
		return result;
    }
	
	@RequestMapping("/importKeyRoles")
	@ResponseBody
	public Map<String, Object> importKeyRoles(
			@RequestParam(value = "file") MultipartFile file)
	{
		Map<String, Object> result = projectListService.importKeyRoles(file);
		return result;
	}
	
	@RequestMapping("/importRDPM")
	@ResponseBody
	public Map<String, Object> importRDPM(
			@RequestParam(value = "file") MultipartFile file)
	{
		Map<String, Object> result = projectListService.importRDPM(file);
		return result;
	}
	
	@RequestMapping("/importMaturityAssessment")
	@ResponseBody
	public Map<String, Object> importMaturityAssessment(
			@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "datepickerCycle1") String time)
	{
		Map<String, Object> result = projectListService.importMaturityAssessment(file,time);
		return result;
	}
	
	@RequestMapping("/initMaturityAssessment")
	@ResponseBody
	public Map<String, Object> initMaturityAssessment(ProjectInfo proj)
	{
		Map<String, Object> result = projectListService.initMaturityAssessment(proj);
		return result;
	}
	
	@RequestMapping("/departmentList")
	@ResponseBody
	public List<Map<String, Object>> departmentList(ProjectInfo projectInfo,String client,String departmentalLevel,String cycle)
	{
		return projectListService.departmentList(projectInfo,client,departmentalLevel,cycle);
	}
	
	@RequestMapping("/circularChartAssessment")
	@ResponseBody
	public Map<String, Object> circularChartAssessment(ProjectInfo projectInfo, Integer continuousCycle, String cycle)
	{
		return projectListService.circularChartAssessment(projectInfo,continuousCycle,cycle);
	}
	
	@RequestMapping("/lineChartAssessment")
	@ResponseBody
	public List<List<Integer>> lineChartAssessment(ProjectInfo projectInfo,String cycle)
	{
		return projectListService.lineChartAssessment(projectInfo,cycle);
	}
	
	@RequestMapping("/everyScore")
	@ResponseBody
	public Map<String, Object> everyScore(String projNo)
	{
		Map<String, Object> result = projectListService.everyScore(projNo);
		return result;
	}
	
	@RequestMapping("/comments")
	@ResponseBody
	public Map<String, Object> comments(String projNo)
	{
		Map<String, Object> result = projectListService.comments(projNo);
		return result;
	}
	
	@RequestMapping("/commentsList")
	@ResponseBody
	public TableSplitResult commentsList(String projNo, PageRequest pageRequest)
	{
		return projectListService.commentsList(projNo, pageRequest);
	}
	
	@RequestMapping("/editCommentsList")
	@ResponseBody
	public BaseResponse editCommentsList(@RequestBody ProjectCommentsListInfo projectCommentsListInfo){
        BaseResponse ret = new BaseResponse();
        try {
        	Date date = new Date();
    		projectCommentsListInfo.setModifyTime(date);
        	projectListService.editCommentsList(projectCommentsListInfo);
        } catch (Exception e) {
            logger.error("保存数据异常",e);
            ret.setErrorMessage("-100","保存数据异常");
        }
        return ret;
    }
	
	@RequestMapping("/export361")
	@ResponseBody
	public void export361(ProjectInfo proj,String color,HttpServletResponse response) throws Exception
	{
		byte[] fileContents = projectListService.export361(proj,color);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "361成熟度" + sf.format(new Date()).toString() + ".xlsx";
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(fileName.getBytes(), "iso-8859-1"));
		response.getOutputStream().write(fileContents);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	
	@RequestMapping("/loadTabsByTypes")
	@ResponseBody
	public List<Map<String, Object>> loadTabsByTypes(ProjectInfo proj)
	{
		Map<String, Object> result = projectListService.initMaturityAssessment(proj);
		List<Map<String, Object>> list = projectListService.loadTabsByTypes(result,proj.getColorType());
		return list;
	}
	
	@RequestMapping("/projectStatus")
	@ResponseBody
	public ProjectStatusLighting projectStatusAnalysis(ProjectInfo projectInfo,String date)
	{
		return projectListService.projectStatusAnalysis(projectInfo,date);
	}
	
	@RequestMapping("/projectOverview")
	@ResponseBody
	public TableSplitResult projectOverview(ProjectInfo projectInfo,Integer limit,Integer offset,String date)
	{
		projectInfo.setProjectState("在行");
		TableSplitResult page = new TableSplitResult();
		page.setPage(offset);
        page.setRows(limit);
        page =  projectListService.projectOverview(projectInfo,limit,offset,date);
        return page;
	}
	
	/**
	 * 361成熟度
	* @Description: 根据模板获取项目评估项
	* @param     参数
	* @return TableSplitResult<List<Map<String, Object>>>    返回类型
	* @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAssessmentCategory")
	@ResponseBody
	public TableSplitResult<List<Map<String, Object>>> getAssessmentCategory(String createTime,String projNo,String template) {
		@SuppressWarnings("rawtypes")
		TableSplitResult page = new TableSplitResult();
		 page.setRows(projectListService.getAssessmentCategory(createTime,projNo,template));
		return page;
	}
	/**
	 * 361成熟度
	* @Description: excel导入项目评估
	* @param     参数
	* @return void    返回类型
	* @throws
	 */
	@RequestMapping("/importAssessment")
	@ResponseBody
	public Map<String, Object> importAssessment(@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "template") String template,@RequestParam(value = "createTime") String createTime,
			HttpServletRequest request) {
		Map<String, Object> result = projectListService.importAssessment(file,template,createTime,request);
		return result;
	}
	
	/**
	 * 361成熟度
	* @Description: 获取template_361
	 */
	@RequestMapping("/getTemplateIs361")
	@ResponseBody
	public PlainResponse<List<Map<String, Object>>> getTemplateIs361(){
		PlainResponse<List<Map<String, Object>>> result = new PlainResponse<List<Map<String, Object>>>();
		try {
			result.setData(projectListService.getTemplateIs361());
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getTemplateIs361 exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 361成熟度模板值
	 * @Description: 获取templateValue
	 */
	@RequestMapping("/getTemplateValue")
	@ResponseBody
	public PlainResponse<String> getTemplateValue(@RequestParam(value = "proNo") String proNo){
		PlainResponse<String> result = new PlainResponse<String>();
		try {
			result.setData(projectListService.getTemplateValue(proNo));
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getTemplateValue exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 更新361成熟度模板值
	 * @Description: 更新templateValue
	 */
	@RequestMapping("/updateTemplateValue")
	@ResponseBody
	public PlainResponse<String> updateTemplateValue(@RequestParam(value = "proNo") String proNo,
			@RequestParam(value = "template") String template){
		PlainResponse<String> result = new PlainResponse<String>();
		try {
			projectListService.updateTemplateValue(proNo,template);
			result.setCode("success");
		} catch (Exception e) {
			logger.error("getTemplateValue exception, error:", e);
			result.setCode("fail");
			result.setErrorMessage("error", e.getMessage());
		}
		return result;
	}
	
	/**
     * excel导入人员职级
     * @param file
     * @param teamId
     * @param request
     * @return
     */
    @RequestMapping("/importRank")
	@ResponseBody
	public Map<String, Object> importRank(@RequestParam(value = "file") MultipartFile file) {
		Map<String, Object> result = projectListService.importRank(file);
		return result;
	}
}
