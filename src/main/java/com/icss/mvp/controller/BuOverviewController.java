package com.icss.mvp.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.icss.mvp.util.CookieUtils;
//import org.apache.http.auth.AuthenticationException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.MonthlyManpowerList;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectKeyrolesList;
import com.icss.mvp.entity.ProjectScheduleList;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.BuOverviewService;
import com.icss.mvp.service.ProjectListService;
import com.icss.mvp.service.TeamInfoService;

@Controller
@RequestMapping("/bu")
public class BuOverviewController extends BaseController {
	private static Logger logger = Logger.getLogger(BuOverviewController.class);
	@Resource
	private HttpServletRequest request;
	@Resource
	private BuOverviewService buOverviewService;
	@Resource
	private ProjectListService projectListService;
	@Resource
	private TeamInfoService teamInfoService;
	@Resource
	HttpServletResponse response;

//	@RequestMapping("/opts")
//	@ResponseBody
//	public List<OrganizeMgmer> getBUs() {
//		return buOverviewService.getBUs();
//	}
//
//	@RequestMapping("/projSummary")
//	@ResponseBody
//	public List<ProjectSummary> getProjSummary(String buName) {
//		return buOverviewService.getProjSummaries(buName);
//	}
//
//	@RequestMapping("/monthSummary")
//	@ResponseBody
//	public List<MonthSummary> getMonthSummary(String buName) {
//		return buOverviewService.getMonthSummary(buName);
//	}

//	@RequestMapping("/projProduct")
//	@ResponseBody
//	public List<ProjProductivity> getProjProductivity(String buName) {
//		return buOverviewService.getProjProductity(buName);
//	}

//	@RequestMapping("/projDetailTab")
//	@ResponseBody
//	public Map<String, Object> getProjDetail(ProjectInfo proj) throws Exception{
//		String username = this.currentUserName();
//		Map<String, Object> lightUpMap = projectListService.initMaturityAssessment(proj);
//		return buOverviewService.getProjDetail(proj, username, lightUpMap);
//	}

	private String currentUserName() {
		String username = "";
		for (Cookie cookie : request.getCookies()) {
			if ("username".equals(cookie.getName())) {
				username = cookie.getValue();
				break;
			}
		}
		return username;
	}

	@RequestMapping("/download")
	public void download(ProjectInfo proj, HttpServletResponse response) throws Exception {
		Map<String, Object> lightUpMap = projectListService.initMaturityAssessment(proj);
		byte[] fileContents = buOverviewService.exportExcel(proj, this.currentUserName(), lightUpMap);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "项目详细信息" + sf.format(new Date()).toString() + ".xls";
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment;filename=" + new String(fileName.getBytes(), "iso-8859-1"));
		response.getOutputStream().write(fileContents);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

//	@RequestMapping("/projIndicators")
//	@ResponseBody
//	public Map<String, Object> getProjIndicators(ProjProductivity proj) {
//		return buOverviewService.getProjIndicators(proj);
//	}

	@RequestMapping("/projView")
	public String getProjView(HttpServletRequest request, String buName, String projNo) {
		request.setAttribute("proj", projectListService.getProjInfo(buName, projNo));
		return "HTML/xiangmuNew";
	}
	
	@RequestMapping("/teamView")
	public String getTeamView(HttpServletRequest request, String buName, String teamNo) {
		request.setAttribute("team", teamInfoService.getTeamInfoVo(buName, teamNo));
		return "HTML/team";
	}
	
//	@RequestMapping("/teamOverviewData")
//	@ResponseBody
//	public Map<String, Object> getTeamOverview(String no) {
//		return teamInfoService.getTeamOverveiwData(no);
//	}
	
	@RequestMapping("/teamInfo")
	@ResponseBody
	public List<ProjectInfo> getTeamInfo(String teamId) {
		return teamInfoService.getTeamInfo(teamId);
	}

	@RequestMapping("/projOverview")
	@ResponseBody
	public Map<String, Object> getProjOverviewGrid(String buName, String no) {
		return buOverviewService.getProjOverveiw(no);
	}

	@RequestMapping("/projOverviewData")
	@ResponseBody
	public Map<String, Object> getProjOverview(String no) {
		return buOverviewService.getProjOverveiwData(no);
	}
	@RequestMapping("/projOverviewDatas")
	@ResponseBody
	public Map<String, Object> getProjOverviews(String reprname) {
		return buOverviewService.getProjOverveiwDatas(reprname);
	}
//	@RequestMapping("/getSpecProjChartData")
//	@ResponseBody
//	public List<ProjProductivity> getSpecProjChartData(String no) {
//		return buOverviewService.getSpecProjChartData(no);
//	}

//	@RequestMapping("/ProjCategory")
//	@ResponseBody
//	public Map<String, Map<String, Map<String, List<ProjectInfo>>>> getProjCategory() {
//		return buOverviewService.getProjCategory();
//	}

	@RequestMapping("/meausreResult")
	@ResponseBody
	public Map<String, Object> getMeasureResult(String projNo, String bigType, String smallType) {
		return buOverviewService.getMeasureResult(projNo, bigType, smallType);

	}

	@RequestMapping("/getProjectKeyrolesNo")
	@ResponseBody
	public List<ProjectKeyroles> getProjectKeyrolesNo(String userid) {
		return buOverviewService.getProjectKeyrolesNo(userid);
	}

	@RequestMapping("/insertInfo")
	@ResponseBody
	public Map<String, Object> insertInfo(@RequestBody ProjectKeyrolesList projRoles) {
		return buOverviewService.insertInfo(projRoles.getProjRoles());
	}

//	@RequestMapping("/getProjectScheduleNo")
//	@ResponseBody
//	public List<ProjectSchedule> getProjectScheduleNo(String no) {
//		return buOverviewService.getProjectScheduleNo(no);
//	}

	@RequestMapping("/insertInfoProjectSchedule")
	@ResponseBody
	public Map<String, Object> insertInfoProjectSchedule(@RequestBody ProjectScheduleList projSchedule) {
		return buOverviewService.insertInfoProjectSchedule(projSchedule.getProjSchedule());
	}

//	@RequestMapping("/area")
//	@ResponseBody
//	public List<String> getAreas() {
//		return buOverviewService.getAreas();
//	}

//	@RequestMapping("/getZhongruanYWX")
//	@ResponseBody
//	public List<Map<String, Object>> getZhongruanYWX() {
//		return buOverviewService.getZhongruanYWX();
//	}

//	@RequestMapping("/getZhongruanSYB")
//	@ResponseBody
//	public List<Map<String, Object>> getZhongruanSYB(String ywxval) {
//		return buOverviewService.getZhongruanSYB(ywxval);
//	}

//	@RequestMapping("/getZhongruanJFB")
//	@ResponseBody
//	public List<Map<String, Object>> getZhongruanJFB(String sybval) {
//		return buOverviewService.getZhongruanJFB(sybval);
//	}

    @RequestMapping("/getHWCPX")
    @ResponseBody
    public List<Map<String, Object>> getHWCPX() {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Set<String> authorized = getAuthorizedTrunk();
            List<Map<String, Object>> records = buOverviewService.getHWCPX();
            if (records != null && !records.isEmpty()) {
                for (Map<String, Object> item : records) {
                    String id = String.valueOf(item != null ? item.get("HWCPXID") : null);
                    if (authorized.contains(id)) {
                        result.add(item);
                    }
                }
            }
        } catch (AuthenticationException e) {
			logger.error("buOverviewService.getHWCPX exception, error: "+e.getMessage());
        }

        return result;
    }
	@RequestMapping("/getHwdept")
	@ResponseBody
	public Map<String, Object> getHwdept(String level,String ids) {
		Map<String, Object> map = new HashMap<>();
		try {
			String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
			List<Map<String,Object>> opDeparts= buOverviewService.getHwdept(level,ids,username);
			map.put("data", opDeparts);
			map.put("msg", "返回成功");
			map.put("status", "0");
		}catch (Exception e){
			logger.error("buOverviewService.getHwdept exception, error: "+e.getMessage());
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}

	@RequestMapping("/getZCPX")
	@ResponseBody
	public List<Map<String, Object>> getZCPX(String hwcpxval) {
		return buOverviewService.getZCPX(hwcpxval);
	}

	@RequestMapping("/getPDUorSPDT")
	@ResponseBody
	public List<Map<String, Object>> getPDUorSPDT(String zcpxval) {
		return buOverviewService.getPDUorSPDT(zcpxval);
	}

	@RequestMapping("/getZRaccountByName")
	@ResponseBody
	public List<Map<String, Object>> getZRaccountByName(String name) {
		return buOverviewService.getZRaccountByName(name);
	}

	@RequestMapping("/insertInvestmentProportion")
	@ResponseBody
	public Map<String, Object> insertInvestmentProportion(@RequestBody MonthlyManpowerList manpowerList) {
		return buOverviewService.insertInvestmentProportion(manpowerList);
	}

	@RequestMapping("/deleteInvestmentProportion")
	@ResponseBody
	public BaseResponse deleteInvestmentProportion(String projNo, String name) {
		BaseResponse result = new BaseResponse();
		try {			
			buOverviewService.deleteInvestmentProportion(projNo, name);	
			result.setCode("success");
		} catch (Exception e) {
			logger.error("删除 投入比例失败：", e);
			result.setCode("failure");
		}
		return result;
	}
	
	/**
	 * 获取人力投入统计 actualFlag=0 实际工时 1 标准工时
	 * 
	 * @param projNo
	 * @return
	 */
	@RequestMapping("/getMonthlyManpowerProportion")
	@ResponseBody
	public Map<String, Object> getMonthlyManpowerProportion(String projNo) {
		return buOverviewService.getMonthlyManpowerProportion(projNo);
	}

    /**
     * 查询信息cookie保存
     * 
     * @param info
     */
    @RequestMapping("/queryMess")
    @ResponseBody
    public void queryMess(ProjectInfo info) {
        addCookie("name", info.getName());
        addCookie("pm", info.getPm());
        addCookie("area", info.getArea());
        addCookie("hwpdu", info.getHwpdu());
        addCookie("hwzpdu", info.getHwzpdu());
        addCookie("pduSpdt", info.getPduSpdt());
        addCookie("bu", info.getBu());
        addCookie("pdu", info.getPdu());
        addCookie("du", info.getDu());
    }

//	/**
//	 * 本地cookie添加新键值
//	 *
//	 * @param key
//	 * @param value
//	 * @param response
//	 */
//	private static void addCookie(String key, String value, HttpServletResponse response) {
//		if (StringUtils.isNotBlank(value)) {
//			try {
//				Cookie cookie = new Cookie(key, URLEncoder.encode(value, "utf-8"));
//				cookie.setPath("/");
//				cookie.setMaxAge(60);
//				response.addCookie(cookie);
//			} catch (UnsupportedEncodingException e) {
//				logger.error("response.addCookie exception, error: "+e.getMessage()):
//			}
//		}
//	}
}
