package com.icss.mvp.controller.project;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.security.sasl.AuthenticationException;

import com.icss.mvp.service.ProjectListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.constant.FileTypeEnum;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.dao.IBuOverviewDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.entity.ProjectBoard;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectStateNumber;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.project.OddResponse;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.service.BuOverviewService;
import com.icss.mvp.service.ProjectInfoService;
import com.icss.mvp.service.ProjectOverviewService;
import com.icss.mvp.service.project.ProjectReviewService;
import com.icss.mvp.service.project.ProjectService;
import com.icss.mvp.service.project.SynchronizeService;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DateUtils;

//import org.apache.http.auth.AuthenticationException;

/**
 * Created by Ray on 2018/6/6.
 */
@Controller
@SuppressWarnings("all")
@RequestMapping("/projects")
public class ProjectController extends BaseController {

    private static Logger logger = Logger.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Resource
    ProjectInfoService projectInfoService;

    @Resource
    IUserManagerDao userManagerDao;

    @Resource
    private IBuOverviewDao buOverviewDao;

    @Autowired
    private ProjectReviewService projectReviewService;

    @Resource
    private ProjectOverviewService projectOverviewService;

    @Resource
    private BuOverviewService buOverviewService;

    @Autowired
    private ProjectListService projectListService;
//    @Autowired
//    ProjectCollectService          collectService;

    @RequestMapping("/page")
    @ResponseBody
    public OddResponse<ProjectInfo> describeProjects(ProjectInfo projectInfo, String client, String teamId,
                                                     PageRequest pageRequest) {

        PageResponse<ProjectInfo> result = new PageResponse<>();
        try {
            // get user authentication info

            String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);

            // authentication(projectInfo, client);
            authentication0(projectInfo, client);
            if (org.apache.commons.lang.StringUtils.isBlank(teamId)) {
                result = projectService.describeProject(projectInfo, client, pageRequest, username);
            } else {
                projectInfo.setTeamId(teamId);
                result = projectService.describeProject(projectInfo, client, pageRequest, username);
            }
        } catch (AuthenticationException e) {
            logger.error("describeProjects occurs AuthenticationException");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            logger.error("describeProjects exception, error:" + e.getMessage());
            result.setData(new ArrayList<>());
        }

        OddResponse response = new OddResponse(result);
        response.setLoginer((Integer) request.getSession().getAttribute("loginer"));

        return response;
    }

    @RequestMapping("/list")
    @ResponseBody
    public ListResponse<ProjectInfo> listProjects(ProjectInfo projectInfo, String client) {
        return describeProjects(projectInfo, client, null, null);
    }

    /**
     * 查询pcb样本配置项目列表
     *
     * @param projectInfo
     * @param client
     * @param pageRequest
     * @return
     */
    @RequestMapping("/getPcbProject")
    @ResponseBody
    public PageResponse<Map<String, Object>> getPcbProject(ProjectInfo projectInfo, String client, PageRequest pageRequest) {
        PageResponse<Map<String, Object>> result = new PageResponse<>();
        try {
//            authentication(projectInfo, client);
            authentication0(projectInfo, client);
            pageRequest.setPageNumber(pageRequest.getPageNumber() / pageRequest.getPageSize());
            result = projectService.describePcbProject(projectInfo, client, pageRequest);
        } catch (AuthenticationException e) {
            logger.error("describePcbProjects occurs AuthenticationException");
            result.setData(new ArrayList<>());
        } catch (Exception e) {
            logger.error("describePcbProjects exception, error:" + e.getMessage());
            result.setData(new ArrayList<>());
        }
        return result;
    }

    /**
     * 选择项目导出指标信息
     *
     * @param projectInfo
     * @param client
     * @param pageRequest
     * @return
     */
    @RequestMapping("/checkboxOnclickData")
    @ResponseBody
    public TableSplitResult checkboxOnclickData(ProjectInfo projectInfo, PageRequest pageRequest) {
        TableSplitResult result = new TableSplitResult();
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        try {
            pageRequest.setPageNumber(pageRequest.getPageNumber() / pageRequest.getPageSize() + 1);
            result = projectService.checkboxOnclickData(projectInfo, pageRequest, username);
        } catch (Exception e) {
            logger.error("查询失败:" + e.getMessage());
        }
        return result;
    }

    @RequestMapping("/addFavoriteProject")
    @ResponseBody
    public Map<String, Object> addFavoriteProject(String proNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        try {
            projectService.addFavoriteProject(proNo, username);
            map.put("msg", "关注成功");
            map.put("status", "0");
        } catch (Exception e) {
            logger.error("addFavoriteProject exception, error:" + e.getMessage());
            map.put("msg", "关注失败");
            map.put("status", "1");
        }

        return map;
    }

    @RequestMapping("/deleteFromFavortie")
    @ResponseBody
    public Map<String, Object> deleteFromFavortie(String proNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        try {
            projectService.deleteFavoriteProject(proNo, username);
            map.put("msg", "取消关注成功");
            map.put("status", "0");
        } catch (Exception e) {
            logger.error("addFavoriteProject exception, error:" + e.getMessage());
            map.put("msg", "取消关注失败");
            map.put("status", "1");
        }

        return map;
    }

    @RequestMapping("/queryFavoriteProject")
    @ResponseBody
    public PageResponse<ProjectInfo> queryFavoriteProject() {
        PageResponse<ProjectInfo> result = new PageResponse<>();
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);

        try {
            List<ProjectInfo> data = projectService.queryFavoriteProject(username);
            result.setCode("0");
            result.setMessage("查询关注项目成功");
            result.setData(data);
            result.setTotalCount(data.size());
        } catch (Exception e) {
            logger.error("addFavoriteProject exception, error:" + e.getMessage());
            result.setCode("1");
            result.setMessage("查询关注项目失败");
            result.setData(new ArrayList<>());
            result.setTotalCount(0);
        }

        return result;
    }

    @RequestMapping("/projectBoard")
    @ResponseBody
    public List<ProjectStateNumber> projectBoard(ProjectInfo projectInfo) throws ParseException {
        List<String> list1 = new ArrayList<>();
        String lastday = projectInfo.getMonth();
        Map<String, Object> organizationMap = new HashMap<>();
        organizationMap.put("month", projectInfo.getMonth());

        projectInfoService.setParamNew(projectInfo, null, organizationMap);

        String midDay = "";
        String endDay = "";
        String[] date = projectInfo.getMonth().split("-");//拆分时间
        int day = Integer.valueOf(date[2]);
        if (day == 15) {
            midDay = DateUtils.getWindMonthEnd(1, projectInfo.getMonth());
            endDay = DateUtils.getWindMonthMid(1, projectInfo.getMonth());
        } else {
            midDay = DateUtils.getWindMonthMid(0, projectInfo.getMonth());
            endDay = DateUtils.getWindMonthEnd(1, projectInfo.getMonth());
        }
        organizationMap.put("lastMonth", midDay);

        List<Object> bumen = buOverviewService.getBm(organizationMap);
        List<ProjectStateNumber> list = new ArrayList<>();
        for (int i = 0; i < bumen.size(); i++) {
            ProjectStateNumber pStateNumber = new ProjectStateNumber();
            Object bm = bumen.get(i);
            organizationMap.put("bm", bm);
            List<String> no = buOverviewDao.getDoubleCycleNO(organizationMap);
            String pdu = projectService.getPdu(organizationMap);
            ProjectBoard projectBoard = projectService.getStateNumber(lastday, no);
            ProjectBoard projectBoard1 = projectService.getStateNumber(midDay, no);
            pStateNumber.setRedState(projectBoard.getRed());
            pStateNumber.setGreenState(projectBoard.getGreen());
            pStateNumber.setYellowState(projectBoard.getYellow());
            pStateNumber.setRedState1(projectBoard1.getRed());
            pStateNumber.setGreenState1(projectBoard1.getGreen());
            pStateNumber.setYellowState1(projectBoard1.getYellow());
            /*ProjectBoard tprojectBoard = projectService.getTwoWeekState(lastday, no, midDay);
            ProjectBoard tprojectBoard1 = projectService.getTwoWeekState(midDay, no, endDay);
            pStateNumber.setRed(tprojectBoard.getRed());
            pStateNumber.setYellow(tprojectBoard.getYellow());
            pStateNumber.setRed1(tprojectBoard1.getRed());
            pStateNumber.setYellow1(tprojectBoard1.getYellow());*/

            if (FileTypeEnum.checkObjFieldIsNull(pStateNumber)) {
                pStateNumber.setPdu(pdu);
                list.add(pStateNumber);
            } else {
                logger.error("对象所有成员变量都为空或0");
                continue;
            }

            List<Map<String, String>> pduspt = projectService.getPduspt(organizationMap);
            for (int j = 0; j < pduspt.size(); j++) {
                ProjectStateNumber pStateNumber1 = new ProjectStateNumber();
                Map<String, String> bm1 = pduspt.get(j);
                pStateNumber1.setPduspt("0".equals(projectInfo.getClientType()) ? bm1.get("PDU_SPDT") : bm1.get("DU"));
                organizationMap.put("bm1", "0".equals(projectInfo.getClientType()) ? bm1.get("PDU_SPDTID") : bm1.get("DUID"));
                List<String> nop = projectService.getNOByPduspt(organizationMap);
                if (nop.size() != 0) {
                    ProjectBoard projectBoard2 = projectService.getStateNumber(lastday, nop);
                    ProjectBoard projectBoard3 = projectService.getStateNumber(midDay, nop);
                    int redStatez = projectBoard2.getRed();
                    int yellowStatez = projectBoard2.getYellow();
                    int greenStatez = projectBoard2.getGreen();
                    int redState1z = projectBoard3.getRed();
                    int yellowState1z = projectBoard3.getYellow();
                    int greenState1z = projectBoard3.getGreen();
                    if (redStatez != 0 || yellowStatez != 0 || greenStatez != 0 || redState1z != 0 || yellowState1z != 0 || greenState1z != 0) {
                        pStateNumber1.setRedState(redStatez);
                        pStateNumber1.setGreenState(greenStatez);
                        pStateNumber1.setYellowState(yellowStatez);
                        pStateNumber1.setRedState1(redState1z);
                        pStateNumber1.setGreenState1(greenState1z);
                        pStateNumber1.setYellowState1(yellowState1z);
                        /*ProjectBoard nprojectBoard = projectService.getTwoWeekState(lastday, nop, midDay);
                        ProjectBoard nprojectBoard1 = projectService.getTwoWeekState(midDay, nop, endDay);
                        pStateNumber1.setRed(nprojectBoard.getRed());
                        pStateNumber1.setYellow(nprojectBoard.getYellow());
                        pStateNumber1.setRed1(nprojectBoard1.getRed());
                        pStateNumber1.setYellow1(nprojectBoard1.getYellow());*/
                        list.add(pStateNumber1);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 上线项目数量
     *
     * @return
     */
    @RequestMapping("/inLine")
    @ResponseBody
    public PlainResponse<Map<String, Object>> getProjectsInline() {
        PlainResponse<Map<String, Object>> result = new PlainResponse<>();
        try {
            String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
            UserEntity uEntity = (UserEntity) request.getSession().getAttribute("USERINFO");
            projectService.getProjectsInline(result, uEntity);
        } catch (Exception e) {
            logger.error("projectService.getProjectsInline exception, error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 上线项目数量
     *
     * @return
     */
    @RequestMapping("/releasePro")
    @ResponseBody
    public PlainResponse<Map<String, Object>> getReleasePro() {
        PlainResponse<Map<String, Object>> result = new PlainResponse<>();
        try {
            projectService.getReleasePro(result);
        } catch (Exception e) {
            logger.error("projectService.getReleasePro exception, error: " + e.getMessage());
        }
        return result;
    }

    @Autowired
    SynchronizeService synchronizeService;

    @RequestMapping("/obtain")
    @ResponseBody
    public BaseResponse obtainProjects() {
        BaseResponse result = new BaseResponse();
        try {
            // result = collectService.collectProjectInfos();
            result = synchronizeService.synchronizePIMS();
        } catch (Exception e) {
            logger.error("synchronizeService.synchronizePIMS exception, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping("/get_by_no")
    @ResponseBody
    public ProjectInfo getByNo(String no) {
        return projectListService.getByProNo(no);
    }
}
