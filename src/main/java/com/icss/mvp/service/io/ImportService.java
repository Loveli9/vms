package com.icss.mvp.service.io;

import static com.icss.mvp.util.ExcelUtils.getCellValue;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IProjectMaturityAssessmentDAO;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.constant.EPayType;
import com.icss.mvp.constant.EProjectType;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.CommonService;
import com.icss.mvp.service.IterationCycleService;
import com.icss.mvp.service.OpDepartmentService;
import com.icss.mvp.service.SysHwdeptService;
import com.icss.mvp.service.TblAreaService;
import com.icss.mvp.service.project.ProjectService;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.ExcelUtils;
import com.icss.mvp.util.UUIDUtil;

/**
 * Created by Ray on 2018/8/9.
 */
@Service("ImportService")
public class ImportService extends CommonService {

    private static Logger  logger = Logger.getLogger(ImportService.class);

    @Autowired
    private ProjectService projectService;
    @Autowired
    private IProjectInfoDao iProjectInfoDao;

	@Resource
	private IterationCycleService cycleService;
	
	@Resource
    private TblAreaService tblAreaService;
	
	@Resource
    private SysHwdeptService sysHwdeptService;

    @Resource
    private OpDepartmentService opDepartmentService;
	
    @Resource
    private IProjectMaturityAssessmentDAO projectMaturityAssessmentDao;

    public BaseResponse importProjects(MultipartFile file) {
        BaseResponse result = new BaseResponse();

        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            if (rowSize < 1) {
                result.setErrorMessage("EmptyData", "没有要导入的数据！");
                logger.error("importProjects error: 没有要导入的数据");
                return result;
            }

            Map<String, Integer> header = ExcelUtils.getHeaderMap(sheet.getRow(0));
            for (int i = 1; i <= rowSize; i++) {
                try {
					Row row = sheet.getRow(i);
					createTeam(header, row);
					ProjectDetailInfo project = createProject(header, row);
					if (StringUtils.isBlank(project.getNo())) {
					    continue;
					}

//					createManager(header, row, project);
					
					//TODO: save team info to database
					// read 
//					createTeam(header, row);
					
					logger.info("importProjects success: [" + project.getName() + "] 项目信息导入完成");
					logger.info(project.getName() +"为当前项目配置默认迭代信息");
					String id = UUIDUtil.getNew();
					IterationCycle cle = new IterationCycle();
					cle.setId(id);
					cle.setIteName("迭代1");
					cle.setIsDeleted("0");
					cle.setProNo(project.getNo());
					cle.setPlanStartDate(project.getStartDate());
					cle.setPlanEndDate(project.getEndDate());
					cle.setStartDate(project.getStartDate());
					cle.setEndDate(project.getEndDate());
					cycleService.deployIteration(cle);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("cycleService.deployIteration exception, error: "+e.getMessage());
				}
            }
        } catch (IOException e) {
            logger.info("importProjects exception: " + e.getMessage());
            result.setErrorMessage("IOException", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
        }

        // result.put("STATUS", "SUCCESS");
        return result;
    }

    private void createTeam(Map<String, Integer> header, Row row) {
    	ProjectTeam team = new ProjectTeam();
    	team.setTeamName(getCellValue(ExcelUtils.getCell(header, row, "团队名称")));
    	team.setTeamNo(getCellValue(ExcelUtils.getCell(header, row, "团队编号")));

        projectService.createTeam(team);
    }
    
    private void createManager(Map<String, Integer> header, Row row, ProjectDetailInfo project) {
        ProjectManager manager = new ProjectManager();
        manager.setNo(project.getNo());
        manager.setName(getCellValue(ExcelUtils.getCell(header, row, "项目经理")));
        manager.setAccount(getCellValue(ExcelUtils.getCell(header, row, "项目经理工号")));
        manager.setPosition("PM");

        manager.setStartDate(project.getStartDate());
        manager.setEndDate(project.getEndDate());
        manager.setImportDate(project.getImportDate());

        projectService.createManger(manager);
    }

    private ProjectDetailInfo createProject(Map<String, Integer> header, Row row) {
        ProjectDetailInfo project = new ProjectDetailInfo();

        String projectName = getCellValue(ExcelUtils.getCell(header, row, "项目名称"));
        String projectNo = getCellValue(ExcelUtils.getCell(header, row, "项目ID"));
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(projectNo)) {
            return project;
        }

        project.setName(projectName);
        project.setNo(projectNo);

        readProjectInfo(header, row, project);

        project.setImportDate(new Date());
        project.setImportUser(getCookie(COOKIE_KEY_USERNAME));

        if (projectService.createProject(project) == -1) {
            project = new ProjectDetailInfo();
        }

        return project;
    }

    private void readProjectInfo(Map<String, Integer> map, Row row, ProjectDetailInfo project) {
    	
        String projectType = getCellValue(ExcelUtils.getCell(map, row, "项目类型"));
        if (EProjectType.getByTitle(projectType) != null) {
            project.setProjectType(projectType);
        }

        project.setProjectSynopsis(getCellValue(ExcelUtils.getCell(map, row, "项目简介")));
        project.setProjectState(getCellValue(ExcelUtils.getCell(map, row, "项目状态")));
        project.setCoopType(getCellValue(ExcelUtils.getCell(map, row, "合作类型")));
        project.setIsOffshore(getCellValue(ExcelUtils.getCell(map, row, "是否离岸")));

        String payType = getCellValue(ExcelUtils.getCell(map, row, "运营商务模式"));
        if (EPayType.getByTitle(payType) != null) {
            project.setType(payType);
        }

        project.setHwpdu(getCellValue(ExcelUtils.getCell(map, row, "一级部门")));
        project.setHwzpdu(getCellValue(ExcelUtils.getCell(map, row, "二级部门")));
        project.setPduSpdt(getCellValue(ExcelUtils.getCell(map, row, "三级部门")));
        Map<String,Object> mapHw = sysHwdeptService.getHwOrganizaMap(project.getHwpdu(),project.getHwzpdu(),project.getPduSpdt());
        project.setHwpduId((null==mapHw || mapHw.size() == 0)?"":mapHw.get("lv1").toString());
        project.setHwzpduId((null==mapHw || mapHw.size() == 0)?"":mapHw.get("lv2").toString());
        project.setPduSpdtId((null==mapHw || mapHw.size() == 0)?"":mapHw.get("lv3").toString());

        project.setBu(getCellValue(ExcelUtils.getCell(map, row, "业务线")));
        project.setPdu(getCellValue(ExcelUtils.getCell(map, row, "事业部")));
        project.setDu(getCellValue(ExcelUtils.getCell(map, row, "交付部")));
        Map<String, Object> mapZR = opDepartmentService.getZrOrganizaMap(project.getBu(),project.getPdu(),project.getDu());
        project.setBuId((null==mapZR || mapZR.size() == 0)?"":mapZR.get("lv1").toString());
        project.setPduId((null==mapZR || mapZR.size() == 0)?"":mapZR.get("lv2").toString());
        project.setDuId((null==mapZR || mapZR.size() == 0)?"":mapZR.get("lv3").toString());
        project.setArea(getCellValue(ExcelUtils.getCell(map, row, "地域")));
        String areaCode = tblAreaService.getArea(project.getArea());
        project.setAreaId(areaCode);
        
        project.setPm(getCellValue(ExcelUtils.getCell(map, row, "项目经理")));
        project.setPmId(getCellValue(ExcelUtils.getCell(map, row, "项目经理工号")));
        
        String startDate = getCellValue(ExcelUtils.getCell(map, row, "项目计划开始时间"));
        project.setStartDate(DateUtils.parseDate(DateUtils.SHORT_FORMAT_GENERAL, startDate));
        String endDate = getCellValue(ExcelUtils.getCell(map, row, "项目计划结束时间"));
        project.setEndDate(DateUtils.parseDate(DateUtils.SHORT_FORMAT_GENERAL, endDate));

        project.setPo(getCellValue(ExcelUtils.getCell(map, row, "外部PO号")));
        
        project.setTeamName(getCellValue(ExcelUtils.getCell(map, row, "团队名称")));
        int teamCode = tblAreaService.getTeam(project.getTeamName());
        project.setTeamId(teamCode);
    }

    public BaseResponse importKeyRoles(MultipartFile file) {
        BaseResponse result = new BaseResponse();

        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            if (rowSize < 1) {
                result.setErrorMessage("EmptyData", "没有要导入的数据！");
                logger.error("importKeyRoles error: 没有要导入的数据");
                return result;
            }

            List<ProjectKeyroles> keyRoles = new ArrayList<>();
            Map<String, Integer> header = ExcelUtils.getHeaderMap(sheet.getRow(0));
            for (int i = 1; i <= rowSize; i++) {
                Row row = sheet.getRow(i);

                String name = getCellValue(ExcelUtils.getCell(header, row, "姓名"));
                String account = getCellValue(ExcelUtils.getCell(header, row, "工号"));
                String projectNo = getCellValue(ExcelUtils.getCell(header, row, "项目编码"));
                if (StringUtils.isBlank(name) || StringUtils.isBlank(account) || StringUtils.isBlank(projectNo)) {
                    continue;
                }

                ProjectKeyroles keyRole = new ProjectKeyroles();
                keyRole.setName(name);
                keyRole.setZrAccount(account);
                keyRole.setNo(projectNo);
                keyRole.setPosition(getCellValue(ExcelUtils.getCell(header, row, "关键角色类型")));
                keyRole.setReplyResults(getCellValue(ExcelUtils.getCell(header, row, "答辩结果")));
                keyRole.setProCompetence(getCellValue(ExcelUtils.getCell(header, row, "岗位胜任度")));
                keyRole.setStatus(getCellValue(ExcelUtils.getCell(header, row, "状态")));

                keyRoles.add(keyRole);
            }

            projectService.createKeyRoles(keyRoles);
        } catch (IOException e) {
            logger.info("importKeyRoles exception: " + e.getMessage());
            result.setErrorMessage("IOException", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
        }

        return result;
    }

    public BaseResponse importRDPM(MultipartFile file) {
        BaseResponse result = new BaseResponse();

        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            if (rowSize < 2) {
                result.setErrorMessage("EmptyData", "没有要导入的数据！");
                logger.error("importRDPM error: 没有要导入的数据");
                return result;
            }

            Map<String, Integer> header = ExcelUtils.getHeaderMap(sheet.getRow(1));
            for (int i = 2; i <= rowSize; i++) {
                Row row = sheet.getRow(i);

                String type = getCellValue(ExcelUtils.getCell(header, row, "考试类别"));
                if (!"PMP".equalsIgnoreCase(type) && !"RDPM".equalsIgnoreCase(type)) {
                    continue;
                }

                String account = getCellValue(ExcelUtils.getCell(header, row, "工号"));
                String score = getCellValue(ExcelUtils.getCell(header, row, "是否通过"));
                if (StringUtils.isBlank(account) || StringUtils.isBlank(score)) {
                    continue;
                }

                projectService.updateScore(account, "通过".equals(score));
            }
        } catch (IOException e) {
            logger.info("importRDPM exception: " + e.getMessage());
            result.setErrorMessage("IOException", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
        }

        return result;
    }

    public BaseResponse importOMP(MultipartFile file) {
        BaseResponse result = new BaseResponse();

        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            if (rowSize < 1) {
                result.setErrorMessage("EmptyData", "没有要导入的数据！");
                logger.error("importMembers error: 没有要导入的数据");
                return result;
            }

            Map<String, Integer> header = ExcelUtils.getHeaderMap(sheet.getRow(0));
            for (int i = 1; i <= rowSize; i++) {
                createOMP(header, sheet.getRow(i));
            }
        } catch (IOException e) {
            logger.info("importMembers exception: " + e.getMessage());
            result.setErrorMessage("IOException", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
        }

        return result;
    }

    private void createOMP(Map<String, Integer> header, Row row) {
        ProjectMember memberOMP = new ProjectMember();
        String name = getCellValue(ExcelUtils.getCell(header, row, "姓名"));
        String projectNo = getCellValue(ExcelUtils.getCell(header, row, "项目编号"));
        if (StringUtils.isBlank(name) || StringUtils.isBlank(projectNo)) {
            return;
        }

        memberOMP.setName(name);
        memberOMP.setNo(projectNo);
        memberOMP.setPo(getCellValue(ExcelUtils.getCell(header, row, "项目PO")));

        String account = getCellValue(ExcelUtils.getCell(header, row, "华为域帐号/W3帐号"));
        memberOMP.setAuthor(StringUtils.isNotBlank(account) ? account.toLowerCase() : "");

        // member.setCompany(getCellValue(ExcelUtils.getCell(header, row, "外包公司")));
        // member.setType(getCellValue(ExcelUtils.getCell(header, row, "合作类型")));
        // member.setProline(getCellValue(ExcelUtils.getCell(header, row, "产品线")));
        // member.setSubproline(getCellValue(ExcelUtils.getCell(header, row, "子产品线")));
        // member.setPdu(getCellValue(ExcelUtils.getCell(header, row, "PDU/SPDT")));
        // member.setPdu(getCellValue(ExcelUtils.getCell(map, row, "PDU/SPDT")));
        // member.setArea(getCellValue(ExcelUtils.getCell(map, row, "地域/代表处")));
        // member.setMode(getCellValue(ExcelUtils.getCell(map, row, "合作模式")));
        memberOMP.setStatus(getCellValue(ExcelUtils.getCell(header, row, "当前状态")));

        memberOMP.setRole(getCellValue(ExcelUtils.getCell(header, row, "角色")));
        memberOMP.setSkill(getCellValue(ExcelUtils.getCell(header, row, "技能")));
        memberOMP.setTeam(getCellValue(ExcelUtils.getCell(header, row, "项目组")));
        // member.setUpdatetime(CovertDate(ExcelUtils.getCell(map, row, "最后修改时间")));

        String cellValue = getCellValue(ExcelUtils.getCell(header, row, "是否骨干员工"));
        memberOMP.setIsKeyStaffs(StringUtils.isBlank(cellValue) ? "否" : cellValue);
        memberOMP.setIdCardNo(getCellValue(ExcelUtils.getCell(header, row, "身份证号")));
        // member.setGrade(getCellFormatValue(ExcelUtils.getCell(map, row, "职级")));
        // member.setGradeCertificationTime(CovertDate(ExcelUtils.getCell(map, row, "职级认证时间")));

        projectService.createOMP(memberOMP);
    }

    public BaseResponse importMembers(MultipartFile file) {
        BaseResponse result = new BaseResponse();

        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            if (rowSize < 1) {
                result.setErrorMessage("EmptyData", "没有要导入的数据！");
                logger.error("importOMP error: 没有要导入的数据");
                return result;
            }

            Map<String, Integer> header = ExcelUtils.getHeaderMap(sheet.getRow(0));
            for (int i = 2; i <= rowSize; i++) {
                Row row = sheet.getRow(i);

                String idCard = getCellValue(ExcelUtils.getCell(header, row, "身份证号"));
                String account = getCellValue(ExcelUtils.getCell(header, row, "工号"));
                if (StringUtils.isBlank(account) || StringUtils.isBlank(idCard)) {
                    continue;
                }

                ProjectMember member = projectService.getMemberByIdentity(idCard);
                if (member == null) {
                    continue;
                }

                member.setZrAccount(account);
                projectService.createOMP(member);
            }
        } catch (IOException e) {
            logger.info("importOMP exception: " + e.getMessage());
            result.setErrorMessage("IOException", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
        }

        return result;
    }

    public BaseResponse importClock(MultipartFile file, String time) {
        BaseResponse result = new BaseResponse();
        if (StringUtils.isBlank(time)) {
            result.setErrorMessage("InvalidParameter", "工时时间必须选择！");
            logger.error("importClock error: 工作时间未提供");
            return result;
        }

        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            if (rowSize < 1) {
                result.setErrorMessage("EmptyData", "没有要导入的数据！");
                logger.error("importOMP error: 没有要导入的数据");
                return result;
            }

            // time = time.substring(0, time.length() - 2);
            Date dateTime = formatToFirstDayOfMonth(time);

            Map<String, Integer> header = ExcelUtils.getHeaderMap(sheet.getRow(1));
            for (int i = 3; i <= rowSize; i++) {
                Row row = sheet.getRow(i);

                String account = getCellValue(ExcelUtils.getCell(header, row, "工号"));
                if (StringUtils.isBlank(account)) {
                    continue;
                }

                TblTimeInformation clockRecord = new TblTimeInformation();
                clockRecord.setZrAccount(account);
                clockRecord.setName(getCellValue(ExcelUtils.getCell(header, row, "姓名")));
                clockRecord.setStandardParticipation(getCellValue(ExcelUtils.getCell(header, row, "当月应该出勤(天)")));
                clockRecord.setActualParticipation(getCellValue(ExcelUtils.getCell(header, row, "当月实际出勤(天)")));
                clockRecord.setStandardLaborHour(getCellValue(ExcelUtils.getCell(header, row, "月标准出勤工时")));
                clockRecord.setActualLaborHour(getCellValue(ExcelUtils.getCell(header, row, "月实际出勤工时")));
                clockRecord.setStatisticalTime(dateTime);

                // projectListDao.delTimeInformation(zrAccount, time.replace("-", ""));
                // projectListDao.inserTimeInformation(clockRecord);
                projectService.createClock(clockRecord);
            }
        } catch (IOException e) {
            logger.info("importOMP exception: " + e.getMessage());
            result.setErrorMessage("IOException", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
        }

        return result;
    }

    private Date parseTime(String time) {
        Date result;
        try {
            result = DateUtils.SHORT_FORMAT_GENERAL.parse(time + "01");
        } catch (ParseException e) {
            result = null;
        }

        return result;
    }

    private Date formatToFirstDayOfMonth(String time) {
        Date result;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            result = format.parse(time);
        } catch (ParseException e) {
            result = null;
        }

        return result;
    }

    public BaseResponse import361MaturityProblem(MultipartFile file) {
        BaseResponse result = new BaseResponse();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            if (rowSize < 1) {
                result.setErrorMessage("EmptyData", "没有要导入的数据！");
                logger.error("import361MaturityProblem error: 没有要导入的数据");
                return result;
            }

            Map<String, Integer> header = ExcelUtils.getHeaderMap(sheet.getRow(0));
            List<ProjectCommentsListInfo> infos = new ArrayList<>();
            for (int i = 1; i <= rowSize; i++) {
                Row row = sheet.getRow(i);

                String projName = getCellValue(ExcelUtils.getCell(header, row, "项目名称"));
                ProjectCommentsListInfo projectCommentsListInfo = new ProjectCommentsListInfo();
                String projNo = iProjectInfoDao.getProjectNoByName(projName);
                if(StringUtilsLocal.isBlank(projNo)){
                    continue;
                }
                projectCommentsListInfo.setNo(projNo);
                String q = getCellValue(ExcelUtils.getCell(header, row, "问题/风险描述"));
                String question = q == null ? "" : q;
                projectCommentsListInfo.setQuestion(question);
                projectCommentsListInfo.setImprMeasure(getCellValue(ExcelUtils.getCell(header, row, "解决措施")));
                projectCommentsListInfo.setProgressDesc(getCellValue(ExcelUtils.getCell(header, row, "解决措施进展")));
                String state = getCellValue(ExcelUtils.getCell(header, row, "问题/风险状态"));
                if("关闭".equals(state)){
                    state = "Closed";
                }else if("打开".equals(state)){
                    state = "Open";
                }
                projectCommentsListInfo.setState(state);
                String personLiable = getCellValue(ExcelUtils.getCell(header, row, "责任人工号"));
                projectCommentsListInfo.setPersonLiable(personLiable==null?"":personLiable.replaceAll("^(0+)", ""));
                SimpleDateFormat str = new SimpleDateFormat("yyyy-MM-dd");
                String createTime = getCellValue(ExcelUtils.getCell(header, row, "提出时间"));
                try {
                    Date date = DateUtils.SHORT_FORMAT_GENERAL.parse(createTime);
                    projectCommentsListInfo.setCreateTime(date);
                    projectCommentsListInfo.setModifyTime(date);
                    createTime = str.format(date);
                } catch (ParseException e) {
                    Date date = new Date();
                    projectCommentsListInfo.setCreateTime(date);
                    projectCommentsListInfo.setModifyTime(date);
                    createTime = str.format(date);
                }
                String finishTime = getCellValue(ExcelUtils.getCell(header, row, "计划关闭时间"));
                String actualFinishTime = getCellValue(ExcelUtils.getCell(header, row, "实际关闭时间"));
                try {
                    projectCommentsListInfo.setFinishTime(
                            StringUtilsLocal.isBlank(finishTime)?null:DateUtils.SHORT_FORMAT_GENERAL.parse(finishTime));
                    projectCommentsListInfo.setActualFinishTime(StringUtilsLocal.isBlank(finishTime)?null:DateUtils.SHORT_FORMAT_GENERAL.parse(actualFinishTime));
                } catch (ParseException e) {
                }
                //设置是否是361成熟度评估问题
                projectCommentsListInfo.setIs361(1);
                //设置是否删除
                projectCommentsListInfo.setIsDeleted(0);
                //清除数据库中的重复数据
                Map<String,Object> map = new HashMap<>();
                map.put("no",projNo);
                map.put("is361",1);
                map.put("question", question);
                map.put("createTime", createTime);
                projectMaturityAssessmentDao.deleteProjectMaturityComments(map);
                //首次保存
                infos.add(projectCommentsListInfo);
                //每100条数据保存一次
                if(infos.size()>=100){
                    projectMaturityAssessmentDao.replaceCommentsLists(infos);
                    infos.clear();
                }

            }
            if(infos.size() > 0){
                projectMaturityAssessmentDao.replaceCommentsLists(infos);
            }
        } catch (IOException e) {
            logger.info("import361MaturityProblem exception: " + e.getMessage());
            result.setErrorMessage("IOException", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // logger.error("read file failed!" + e.getMessage());
                }
            }
        }

        return result;
    }
}
