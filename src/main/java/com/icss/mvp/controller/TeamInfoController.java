package com.icss.mvp.controller;

import com.icss.mvp.entity.ProjectTeamVo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.service.TeamInfoService;
import com.icss.mvp.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;

@RestController
@RequestMapping("/teamInfo")
public class TeamInfoController {
	private static Logger logger = Logger.getLogger(TeamInfoController.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TeamInfoService teamInfoService;

    /*@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody*/
    /*public Map<String, Object> list(@ModelAttribute TeamInfo teamInfo, @ModelAttribute PageInfo pageInfo){
        Map<String, Object> map = new HashMap<>();
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            teamInfo.setName(URLDecoder.decode(teamInfo.getName(), "UTF-8"));
            teamInfo.setTm(URLDecoder.decode(teamInfo.getTm(), "UTF-8"));
//            List<TeamInfoVo> teamInfoVos = teamInfoService.teamInfos(teamInfo, username, pageInfo);
//            map.put("pageInfo", jsonObject.get("pageInfo"));
//            map.put("data", jsonObject.get("data"));
//            map.put("msg", "返回成功");
//            map.put("status", "0");
//            String title = "[{\"field\":\"团队名称\",\"title\":\"团队名称\"},{\"field\":\"团队经理\",\"title\":\"团队经理\"},{\"field\":\"地域\",\"title\":\"地域\"},{\"field\":\"华为产品线\",\"title\":\"华为产品线\"},{\"field\":\"子产品线\",\"title\":\"子产品线\"},{\"field\":\"PDU/SPDT\",\"title\":\"PDU/SPDT\"},{\"field\":\"中软业务线\",\"title\":\"中软业务线\"},{\"field\":\"中软事业部\",\"title\":\"中软事业部\"},{\"field\":\"中软交付部\",\"title\":\"中软交付部\"}]";
            String title = "[{\"field\":\"团队名称\",\"title\":\"团队名称\"},{\"field\":\"团队经理\",\"title\":\"团队经理\"}]";
            
            Object gridTitles = JSON.parse(title);
            map.put("gridTitles", gridTitles);
            JSONObject json = new JSONObject();
//            json.put("total", pageInfo.getTotalRecord());
            json.put("total", teamInfoVos.size());
            JSONArray jsonArray = new JSONArray();
            for (TeamInfoVo teamInfoVo : teamInfoVos) {
                JSONObject teamInfoObject = new JSONObject();
                teamInfoObject.put("团队编码", teamInfoVo.getNo());
                teamInfoObject.put("团队名称", teamInfoVo.getName());
                teamInfoObject.put("团队经理", teamInfoVo.getTm());
                teamInfoObject.put("子产品线", teamInfoVo.getHwzpdu());
                teamInfoObject.put("华为产品线", teamInfoVo.getHwpdu());
                teamInfoObject.put("地域", teamInfoVo.getArea());
                teamInfoObject.put("PDU/SPDT", teamInfoVo.getPduSpdt());
                teamInfoObject.put("中软业务线", teamInfoVo.getBu());
                teamInfoObject.put("中软事业部", teamInfoVo.getPdu());
                teamInfoObject.put("中软交付部", teamInfoVo.getDu());
                jsonArray.add(teamInfoObject);
            }
            json.put("rows", jsonArray);
            map.put("gridDatas", json);
        }catch (Exception e){
            logger.error("jsonArray.add exception, error: "+e.getMessage());
            map.put("msg", "返回失败");
            map.put("status", "1");
        }
        return map;
    }*/
    
    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    @ResponseBody
    public PageResponse<ProjectTeamVo> teams(String teamName,String tm, PageRequest pageRequest)
	{
		PageResponse<ProjectTeamVo> result = null;
		try {
			result = new PageResponse<>();
			String username = CookieUtils.value(request, CookieUtils.USER_NAME);
            result = teamInfoService.teamInfos(URLDecoder.decode(teamName, "UTF-8"),URLDecoder.decode(tm, "UTF-8"),username,pageRequest);
		} catch (Exception e) {
			logger.error("teamInfoService.teamInfos exception, error: "+e.getMessage());
		}
		return result;
	}
    /*public PageResponse<ProjectTeamVo> teams(@ModelAttribute ProjectTeamVo projectTeamVo, @ModelAttribute PageInfo pageInfo){
//        Map<String, Object> map = new HashMap<>();
    	List<ProjectTeamVo> teamInfos = new ArrayList<ProjectTeamVo>();
        
        try {
            String username = CookieUtils.value(request, CookieUtils.USER_NAME);
//            if(projectTeam.getTeamName()!=null&&projectTeam.getTeamName()!=""){
//            	projectTeam.setTeamName(URLDecoder.decode(projectTeam.getTeamName(), "UTF-8"));
//            }
            projectTeamVo.setTeamName(URLDecoder.decode(projectTeamVo.getTeamName(), "UTF-8"));
            projectTeamVo.setTm(URLDecoder.decode(projectTeamVo.getTm(), "UTF-8"));
            teamInfos = teamInfoService.teamInfos(projectTeamVo, username, pageInfo);
//            map.put("pageInfo", jsonObject.get("pageInfo"));
//            map.put("data", jsonObject.get("data"));
//            map.put("msg", "返回成功");
//            map.put("status", "0");
//            String title = "[{\"field\":\"团队名称\",\"title\":\"团队名称\"},{\"field\":\"团队经理\",\"title\":\"团队经理\"},{\"field\":\"团队人数\",\"title\":\"团队人数\"}]";
            
//            Object gridTitles = JSON.parse(title);
//            map.put("gridTitles", gridTitles);
//            JSONObject json = new JSONObject();
//            json.put("total", pageInfo.getTotalRecord());
//            json.put("total", teamInfos.size());
//            JSONArray jsonArray = new JSONArray();
            for (ProjectTeamVo teamInfo : teamInfos) {
                JSONObject teamInfoObject = new JSONObject();
                teamInfoObject.put("团队编码", teamInfo.getTeamNo());
                teamInfoObject.put("团队名称", teamInfo.getTeamName());
                teamInfoObject.put("团队经理", teamInfo.getTm());
                teamInfoObject.put("团队人数", teamInfo.getTeamSize());
                jsonArray.add(teamInfoObject);
            }
//            json.put("rows", jsonArray);
//            map.put("gridDatas", json);
        }catch (Exception e){
            logger.error("jsonArray.add exception, error: "+e.getMessage());
//            map.put("msg", "返回失败");
//            map.put("status", "1");
        }
        return  teamInfos;
    }*/

    @RequestMapping("/import")
	@ResponseBody
	public Map<String, Object> importTeamList(
			@RequestParam(value = "file") MultipartFile file)
	{
		Map<String, Object> result = teamInfoService.importTeam(file);
		return result;
   }
    
    /**
     * excel导入团队成员
     * @param file
     * @param teamId
     * @param request
     * @return
     */
    @RequestMapping("/importTeamMembers")
	@ResponseBody
	public Map<String, Object> importTeamMembers(@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "teamId") String teamId,
			HttpServletRequest request) {
		Map<String, Object> result = teamInfoService.importTeamMembers(file,teamId,request);
		return result;
	}
}
