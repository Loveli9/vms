package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.icss.mvp.dao.ICodeMasterInfoDao;
import com.icss.mvp.dao.IParameterInfo;
import com.icss.mvp.dao.IProjectKeyrolesDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.IProjectMaturityAssessmentDAO;
import com.icss.mvp.dao.IProjectParameter;
import com.icss.mvp.dao.IStarRatingDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.TemaInfoVoDao;
import com.icss.mvp.dao.project.MaturityAssessmentDao;
import com.icss.mvp.entity.CodeMasterInfo;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterInfo;
import com.icss.mvp.entity.ProjectCommentsListInfo;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectManager;
import com.icss.mvp.entity.ProjectMaturityAssessment;
import com.icss.mvp.entity.ProjectMember;
import com.icss.mvp.entity.ProjectStatus;
import com.icss.mvp.entity.ProjectStatusLighting;
import com.icss.mvp.entity.ProjectWeekLighting;
import com.icss.mvp.entity.StarRating;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.TblTimeInformation;
import com.icss.mvp.entity.TeamMembers;
import com.icss.mvp.entity.UserInfo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.project.MaturityAssessment;
import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.CookieUtils;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.ExcelUtils;
import com.icss.mvp.util.LightUp;
import com.icss.mvp.util.MathUtils;
import com.icss.mvp.util.StringUtilsLocal;

@Service("projectListService")
@SuppressWarnings("all")
@Transactional
public class ProjectListService {
    @Resource
    private HttpServletRequest request;
    @Resource
    private IProjectListDao projectListDao;
    @Resource
    private IProjectParameter projParam;
//    @Resource
//    private IOrgnizeInfoDao orgInfoDao;
    @Resource
    private IProjectKeyrolesDao projectKeyrolesDao;
    @Resource
    private IProjectParameter projectParameter;
    @Resource
    private IParameterInfo parameterInfo;
    @Resource
    private ICodeMasterInfoDao codeMasterInfo;
    @Resource
    private IStarRatingDao starRatingDao;
    @Resource
    private IProjectMaturityAssessmentDAO projectMaturityAssessmentDao;
    @Resource
    IUserManagerDao userManagerDao;
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    private MaturityAssessmentDao maturityAssessmentDao;

    @Resource
    private TemaInfoVoDao temaInfoVoDao;

    @Resource
    IProjectManagersDao projectManagersDao;

    // private final static String[] HEADERS = { "项目名称", "项目编号", "事业部", "交付部","产品",
    // "地域", "项目经理", "项目类型", "项目开始日期", "项目结束日期", "项目导入日期", "状态" };
    // private final static String[] HEADERS = { "项目名称", "团队名称", "地域",
    // "华为产品线","子产品线",
    // "PDU/SPDT", "业务线", "事业部", "交付部", "运营编码", "外部PO号", "外部PO名","项目经理",
    // "项目经理工号", "项目QA", "项目QA工号", "是否上网项目", "华为PM/接口人", "外包代表", "当前月项目总人力",
    // "运营商务模式", "项目类型", "合作类型", "是否离岸", "业务分类", "业务类型", "项目简介",
    // "项目计划开始时间","项目计划结束时间", "项目状态", "现团队类型", "推进后团队类型", "计划完成转化月份",
    // "团队现商务模式","团队推进后商务模式","商务模式计划完成转化月份","不能FP的原因","离岸人数","华为在场支撑人数",
    // "不能离场关键原因"};
    private final static Logger LOG = Logger.getLogger(ProjectListService.class);

    public Map<String, Object> getList(ProjectInfo proj, Page page) {
        List<ProjectInfo> projList = projectListDao.getList(proj, page.getSort(), page.getOrder());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", projList);
        result.put("total", projList.size());
        return result;
    }

    public ProjectInfo getByProNo(String proNo) {
        return projectListDao.getProjInfoByNo(proNo);
    }

    public byte[] exportExcel(ProjectInfo proj) {
        List<ProjectInfo> projList = projectListDao.getList(proj, "", "");
        proj = new ProjectInfo();
        InputStream is = this.getClass().getResourceAsStream("/excel/project-info-template.xlsx");
        Workbook wb = null;
        try {
            wb = new XSSFWorkbook(is);
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            return null;
        }
        Sheet sheet = wb.getSheetAt(0);
        Row row;
        Cell cell;
        // CellStyle cellStyle = wb.createCellStyle();
        // CreationHelper creationHelper = wb.getCreationHelper();
        // cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(
        // "yyyy-MM-dd"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // for (int i = 0; i < HEADERS.length; i++) {
        // cell = row.createCell(i);
        // cell.setCellValue(HEADERS[i]);
        // }
        for (int i = 0; i < projList.size(); i++) {
            proj = projList.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellValue(proj.getName());
            cell = row.createCell(1);
            cell.setCellValue("");
            cell = row.createCell(2);
            cell.setCellValue(proj.getArea());
            cell = row.createCell(3);
            cell.setCellValue(proj.getHwpdu());
            cell = row.createCell(4);
            cell.setCellValue(proj.getHwzpdu());
            cell = row.createCell(5);
            cell.setCellValue(proj.getPduSpdt());
            cell = row.createCell(6);
            cell.setCellValue(proj.getBu());
            cell = row.createCell(7);
            cell.setCellValue(proj.getPdu());
            cell = row.createCell(8);
            cell.setCellValue(proj.getDu());
            cell = row.createCell(9);
            cell.setCellValue(proj.getNo());
            cell = row.createCell(10);
            cell.setCellValue("");
            cell = row.createCell(11);
            cell.setCellValue("");
            String pm = proj.getPm();
            if (null != pm && !"".equals(pm)) {
                String[] pms = pm.split(" ");
                if (pms.length >= 2) {
                    cell = row.createCell(12);
                    cell.setCellValue(pms[0]);
                    cell = row.createCell(13);
                    cell.setCellValue(pms[1]);
                } else {
                    cell = row.createCell(12);
                    cell.setCellValue("");
                    cell = row.createCell(13);
                    cell.setCellValue("");
                }
            } else {
                cell = row.createCell(12);
                cell.setCellValue("");
                cell = row.createCell(13);
                cell.setCellValue("");
            }
            cell = row.createCell(14);
            cell.setCellValue("");
            cell = row.createCell(15);
            cell.setCellValue("");
            cell = row.createCell(16);
            cell.setCellValue("");
            cell = row.createCell(17);
            cell.setCellValue("");
            cell = row.createCell(18);
            cell.setCellValue("");
            cell = row.createCell(19);
            cell.setCellValue("");
            cell = row.createCell(20);
            cell.setCellValue(proj.getType());
            cell = row.createCell(21);
            cell.setCellValue(proj.getProjectType());
            cell = row.createCell(22);
            cell.setCellValue(proj.getCoopType());
            cell = row.createCell(23);
            cell.setCellValue(proj.getIsOffshore());
            cell = row.createCell(24);
            cell.setCellValue("");
            cell = row.createCell(25);
            cell.setCellValue("");
            cell = row.createCell(26);
            cell.setCellValue(proj.getProjectSynopsis());
            cell = row.createCell(27);
            Date date;
            if (null != proj.getStartDate()) {
                date = proj.getStartDate();
                cell.setCellValue(df.format(date));
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(28);
            if (null != proj.getEndDate()) {
                date = proj.getStartDate();
                cell.setCellValue(df.format(date));
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(29);
            cell.setCellValue(proj.getProjectState());
            cell = row.createCell(30);
            cell.setCellValue("");
            cell = row.createCell(31);
            cell.setCellValue("");
            cell = row.createCell(32);
            cell.setCellValue("");
            cell = row.createCell(33);
            cell.setCellValue(proj.getType());
            cell = row.createCell(34);
            cell.setCellValue(proj.getType());
            cell = row.createCell(35);
            cell.setCellValue("");
            cell = row.createCell(36);
            cell.setCellValue("");
            cell = row.createCell(37);
            cell.setCellValue("");
            cell = row.createCell(38);
            cell.setCellValue("");
            cell = row.createCell(39);
            cell.setCellValue("");
        }
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            try {
                wb.write(os);
            } catch (IOException e) {
                LOG.error("export excel failed!", e);
            }
            return os.toByteArray();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOG.error("Output file failed!" + e.getMessage());
                }
            }
        }
    }

    public ProjectInfo getProjInfo(String buName, String projNo) {
        return projectListDao.getProjInfo(buName, projNo);
    }

    public Map<String, Object> getProjectSelectInfo() {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("businessUnit", projectListDao.getBusinessUnit());
        result.put("deliveryUnit", projectListDao.getDeliveryUnit());
        result.put("countArea", projectListDao.getCountArea());
        result.put("projectType", projectListDao.getProjectType());
        result.put("projectManager", projectListDao.getProjectManager());
        return result;
    }

    public Map<String, String> insertInfo(ProjectDetailInfo proj, String param, ProjectManager projManger,
                                          Date imptDate) {
        Map<String, String> result = new HashMap<String, String>();
        String message;
        if ("add".equals(param)) {
            int projInfoRow = projectListDao.insertInfo(proj);
            int projParamRow = insertProjParam(proj, imptDate);
            int memberRow = projectListDao.insertMember(projManger);
            if (projInfoRow > 0 && projParamRow > 0 && memberRow > 0) {
                message = "保存成功";
            } else {
                message = "保存失败";
            }

        } else {
            int projInfoRow = projectListDao.updateInfo(proj);
            int memberRow = projectListDao.updateMemberInfo(projManger);
            if (projInfoRow > 0 && memberRow > 0) {
                message = "保存成功";
            } else {
                message = "保存失败";
            }
        }
        result.put("param", param);
        result.put("message", message);
        return result;

    }

    public Map<String, Object> queryName(String projNo) {
        ProjectManager proManager = null;
        try {
            proManager = projectListDao.isExitMember(projNo);
        } catch (Exception e) {
            LOG.error("projectListDao.isExitMember exception, error: " + e.getMessage());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", proManager);
        return result;
    }

    public int insertProjParam(ProjectDetailInfo proj, Date imptDate) {
        List<ParameterInfo> paramInfos = parameterInfo.queryParameterInfo();
        List<Object> listInfo = new ArrayList<Object>();
        for (ParameterInfo paramInfo : paramInfos) {
            Map<String, Object> mapInfo = new HashMap<String, Object>();
            mapInfo.put("no", proj.getNo());
            mapInfo.put("id", paramInfo.getId());
            mapInfo.put("unit", paramInfo.getUnit());
            mapInfo.put("source_value", paramInfo.getSource_value());
            mapInfo.put("parameter", paramInfo.getParameter());
            mapInfo.put("isDisplay", 1);
            mapInfo.put("create_date", imptDate);
            mapInfo.put("creator", "admin");
            mapInfo.put("update_date", imptDate);
            mapInfo.put("update_user", "admin");
            listInfo.add(mapInfo);
        }
        return projectParameter.insertParamInfo(listInfo);
    }

    public Map<String, Object> importProjects(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
            for (int i = 1; i <= rowSize; i++) {
                row = sheet.getRow(i);
                ProjectDetailInfo projInfo = new ProjectDetailInfo();
                String cell01Value = getCellFormatValue(ExcelUtils.getCell(map, row, "项目名称"));
                String no = getCellFormatValue(ExcelUtils.getCell(map, row, "运营编码"));
                if (cell01Value == null || no == null || rowSize == 0) {
                    result.put("STATUS", "FAILED");
                    result.put("MESSAGE", "没有要导入的数据！");
                    return result;
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                projInfo.setName(cell01Value);
                projInfo.setNo(no);
                projInfo.setProjectType(isProjectType(getCellFormatValue(ExcelUtils.getCell(map, row, "项目类型"))));
                projInfo.setProjectSynopsis(getCellFormatValue(ExcelUtils.getCell(map, row, "项目简介")));
                projInfo.setProjectState(getCellFormatValue(ExcelUtils.getCell(map, row, "项目状态")));
                projInfo.setCoopType(getCellFormatValue(ExcelUtils.getCell(map, row, "合作类型")));
                projInfo.setIsOffshore(getCellFormatValue(ExcelUtils.getCell(map, row, "是否离岸")));
                projInfo.setType(isPayType(getCellFormatValue(ExcelUtils.getCell(map, row, "运营商务模式"))));
                projInfo.setHwpdu(getCellFormatValue(ExcelUtils.getCell(map, row, "华为产品线")));
                projInfo.setHwzpdu(getCellFormatValue(ExcelUtils.getCell(map, row, "子产品线")));
                projInfo.setPduSpdt(getCellFormatValue(ExcelUtils.getCell(map, row, "PDU/SPDT")));
                projInfo.setBu(getCellFormatValue(ExcelUtils.getCell(map, row, "业务线")));
                projInfo.setPdu(getCellFormatValue(ExcelUtils.getCell(map, row, "事业部")));
                projInfo.setDu(getCellFormatValue(ExcelUtils.getCell(map, row, "交付部")));
                projInfo.setArea(isArea(getCellFormatValue(ExcelUtils.getCell(map, row, "地域"))));
                projInfo.setStartDate(CovertDate(ExcelUtils.getCell(map, row, "项目计划开始时间")));
                projInfo.setEndDate(CovertDate(ExcelUtils.getCell(map, row, "项目计划结束时间")));
                projInfo.setPo(getCellFormatValue(ExcelUtils.getCell(map, row, "外部PO号")));
                String date = df.format(new Date());
                Date imptDate = null;
                try {
                    imptDate = df.parse(date);
                } catch (ParseException e) {
                    LOG.error("时间转换异常", e);
                }
                projInfo.setImportDate(imptDate);
                projInfo.setImportUser("admin");
                projectListDao.replaceInfo(projInfo);
                // if (NameAndNo(projInfo.getName(),projInfo.getNo())==null
                // &&("不存在项目").equals(judgeProjInfo(projInfo.getNo()))
                // ){

                // try {
                // insertProjParam(projInfo, imptDate);
                // } catch (Exception e) {
                // LOG.error(projInfo.getName() + "导入项目已存在");
                // }

                // projInfoLists.add(projInfo);
                ProjectManager projMember = new ProjectManager();
                projMember.setNo(no);
                String pmName = getCellFormatValue(ExcelUtils.getCell(map, row, "项目经理")) + " "
                        + getCellFormatValue(ExcelUtils.getCell(map, row, "项目经理工号"));
                ;
                String pmNo = getCellFormatValue(ExcelUtils.getCell(map, row, "项目经理工号"));
                ;
                projMember.setName(pmName);
                projMember.setPosition("PM");
                if (pmNo == null) {
                    projMember.setAccount("pm");
                } else {
                    projMember.setAccount(pmName);
                }
                projMember.setStartDate(projInfo.getStartDate());
                projMember.setEndDate(projInfo.getEndDate());
                projMember.setImportDate(imptDate);
                projectListDao.replaceMember(projMember);
                LOG.info(projInfo.getName() + "导入成功");
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    public Map<String, Object> importPromember(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            if (file.getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (file.getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "当前文件不是excel文件");
                LOG.info("当前文件不是excel文件");
                return result;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
            for (int i = 1; i <= rowSize; i++) {
                row = sheet.getRow(i);
                // ProjectDetailInfo projInfo = new ProjectDetailInfo();
                ProjectMember promember = new ProjectMember();
                String cell01Value = getCellFormatValue(ExcelUtils.getCell(map, row, "姓名"));
                String no = getCellFormatValue(ExcelUtils.getCell(map, row, "项目编号"));
                if (cell01Value == null || no == null || rowSize == 0) {
                    result.put("STATUS", "FAILED");
                    result.put("MESSAGE", "没有要导入的数据！");
                    LOG.info("人员信导入失败");
                    continue;
                }

                promember.setName(cell01Value);
                promember.setNo(no);
                promember.setPo(getCellFormatValue(ExcelUtils.getCell(map, row, "项目PO")));
                promember.setCompany(getCellFormatValue(ExcelUtils.getCell(map, row, "外包公司")));
                promember.setType(getCellFormatValue(ExcelUtils.getCell(map, row, "合作类型")));
                promember.setProline(getCellFormatValue(ExcelUtils.getCell(map, row, "产品线")));
                promember.setSubproline(getCellFormatValue(ExcelUtils.getCell(map, row, "子产品线")));
                promember.setPdu(getCellFormatValue(ExcelUtils.getCell(map, row, "PDU/SPDT")));
                promember.setArea(getCellFormatValue(ExcelUtils.getCell(map, row, "地域/代表处")));
                promember.setMode(getCellFormatValue(ExcelUtils.getCell(map, row, "合作模式")));
                promember.setStatus(getCellFormatValue(ExcelUtils.getCell(map, row, "当前状态")));
                promember.setAuthor(getCellFormatValue(ExcelUtils.getCell(map, row, "华为域帐号/W3帐号")));
                promember.setRole(getCellFormatValue(ExcelUtils.getCell(map, row, "角色")));
                promember.setSkill(getCellFormatValue(ExcelUtils.getCell(map, row, "技能")));
                promember.setTeam(getCellFormatValue(ExcelUtils.getCell(map, row, "项目组")));
                promember.setUpdatetime(CovertDate(ExcelUtils.getCell(map, row, "最后修改时间")));
                promember.setIsKeyStaffs(getCellFormatValue(ExcelUtils.getCell(map, row, "是否骨干员工")));
                promember.setIdCardNo(getCellFormatValue(ExcelUtils.getCell(map, row, "已匹配身份证号码")));
                promember.setGrade(getCellFormatValue(ExcelUtils.getCell(map, row, "职级")));
                promember.setGradeCertificationTime(CovertDate(ExcelUtils.getCell(map, row, "职级认证时间")));
                projectListDao.insertmemberInfo(promember);

            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    public Map<String, Object> importClock(MultipartFile file, String time) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (time == null || time == "") {
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "工时时间必须选择");
            LOG.info("工时时间必须选择");
            return result;
        }
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            // workbook = new XSSFWorkbook(is);
            if (file.getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (file.getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "当前文件不是excel文件");
                LOG.info("当前文件不是excel文件");
                return result;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 2);
            for (int i = 3; i <= rowSize; i++) {
                row = sheet.getRow(i);
                TblTimeInformation tblTimeInformation = new TblTimeInformation();
                String zrAccount = getCellFormatValue(ExcelUtils.getCell(map, row, "工号"));
                if (zrAccount == null) {
                    LOG.info("工时数据工号不能为空,导入失败");
                    continue;
                }
                tblTimeInformation.setZrAccount(zrAccount);
                tblTimeInformation.setName(getCellFormatValue(ExcelUtils.getCell(map, row, "姓名")));
                tblTimeInformation
                        .setStandardParticipation(getCellFormatValue(ExcelUtils.getCell(map, row, "当月应该出勤(天)")));
                tblTimeInformation
                        .setActualParticipation(getCellFormatValue(ExcelUtils.getCell(map, row, "当月实际出勤(天)")));
                tblTimeInformation.setStandardLaborHour(getCellFormatValue(ExcelUtils.getCell(map, row, "月标准出勤工时")));
                tblTimeInformation.setActualLaborHour(getCellFormatValue(ExcelUtils.getCell(map, row, "月实际出勤工时")));
                try {
                    tblTimeInformation
                            .setStatisticalTime(DateUtils.SHORT_FORMAT_GENERAL.parse(time.substring(0, time.length() - 2) + "01"));
                } catch (ParseException e) {
                    LOG.error("时间序列化异常" + e.getMessage());
                }
                projectListDao.delTimeInformation(zrAccount, time.substring(0, time.length() - 2).replace("-", ""));
                projectListDao.inserTimeInformation(tblTimeInformation);
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    public String NameAndNo(String name, String no) {
        String message = null;
        if (name == null || no == null) {
            message = "不能为空";
        }
        return message;
    }

//    public String getBU(String bu) {
//        List<OrganizeInfo> orgInfos = orgInfoDao.getBU(bu);
//        if (orgInfos.size() == 0) {
//            bu = null;
//        }
//        return bu;
//    }

//    public String getPDU(String bu, String pdu) {
//        List<OrganizeInfo> orgInfos = orgInfoDao.getBU(pdu);
//        if (orgInfos.size() == 0) {
//            pdu = null;
//            return pdu;
//        }
//        if (bu == null) {
//            return pdu;
//        }
//        List<String> list = new ArrayList<String>();
//        for (OrganizeInfo infos : orgInfos) {
//            infos.setLevel(0);
//            infos.setNodeName(bu);
//            int nodeId = orgInfoDao.getNodeId(infos);
//            List<OrganizeInfo> orgs = orgInfoDao.getPDU(nodeId);
//            for (OrganizeInfo org : orgs) {
//                list.add(org.getNodeName());
//            }
//        }
//        if (!list.contains(pdu)) {
//            pdu = null;
//        }
//        return pdu;
//    }

    public String judgeProjInfo(String no) {
        String projMessage = null;
        ProjectDetailInfo proj = projectListDao.isExit(no);
        if (proj != null) {
            projMessage = "已存在项目";
        } else {
            projMessage = "不存在项目";
        }
        return projMessage;
    }

    public String judgeMemberInfo(String no) {
        String MemberMessage = null;
        ProjectManager projM = projectListDao.isExitMember(no);
        if (projM != null) {
            MemberMessage = "项目信息存在";
        } else {
            MemberMessage = "不存在项目信息";
        }
        return MemberMessage;
    }

    public String Exitmember(String author) {
        String Message = null;
        ProjectMember projm = projectListDao.queryMember(author);
        if (projm != null) {
            Message = "人员信息已存在";
        } else {
            Message = "人员信息不存在";
        }
        return Message;
    }

    private String getCellFormatValue(Cell cell) {
        String strCell = null;
        if (cell == null) {
            return strCell;
        }
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = null;
                break;
        }
        return strCell;
    }

    public Date CovertDate(Cell cell) {
        Date dates = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        if (null == cell) {
            return dates;
        }
        Date cellDate = null;
        try {
            cellDate = df.parse(cell.getStringCellValue());
        } catch (ParseException e) {
            try {
                cellDate = df1.parse(cell.getStringCellValue());
            } catch (ParseException e1) {
                cellDate = null;
            }

        } catch (IllegalStateException e) {
            dates = null;
        }
        dates = null;
        if (null == cellDate) {
            return dates;
        }
        dates = cellDate;
        return dates;
    }

    @SuppressWarnings("unused")
    public Date CovertDate(Cell cell, SimpleDateFormat df) {
        Date cellDate = null;
        if (null == cell) {
            return cellDate;
        }
        try {
            cellDate = df.parse(cell.getStringCellValue());
        } catch (ParseException e) {
            cellDate = null;
        }
        return cellDate;
    }

    public Double StringToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            return null;
        }

    }

    public String isProjectType(String projectType) {
        CodeMasterInfo codeInfo = new CodeMasterInfo();
        List<String> list = new ArrayList<String>();
        if (projectType == null) {
            projectType = null;
        }
        codeInfo.setCodekey("ProjectType");
        List<CodeMasterInfo> codeMaster = codeMasterInfo.getList(codeInfo);
        for (CodeMasterInfo code : codeMaster) {
            list.add(code.getName());
        }
        if (!list.contains(projectType)) {
            projectType = null;
        }
        return projectType;
    }

    public String isPayType(String payType) {
        CodeMasterInfo codeInfo = new CodeMasterInfo();
        List<String> list = new ArrayList<String>();
        if (payType == null) {
            payType = null;
        }
        codeInfo.setCodekey("PayType");
        List<CodeMasterInfo> codeMaster = codeMasterInfo.getList(codeInfo);
        for (CodeMasterInfo code : codeMaster) {
            list.add(code.getName());
        }
        if (!list.contains(payType)) {
            payType = null;
        }
        return payType;
    }

    public String isPayDepart(String payDepart) {
        CodeMasterInfo codeInfo = new CodeMasterInfo();
        List<String> list = new ArrayList<String>();
        if (payDepart == null) {
            payDepart = null;
        }
        codeInfo.setCodekey("DeliveryDepartment");
        List<CodeMasterInfo> codeMaster = codeMasterInfo.getList(codeInfo);
        for (CodeMasterInfo code : codeMaster) {
            list.add(code.getName());
        }
        if (!list.contains(payDepart)) {
            payDepart = null;
        }
        return payDepart;
    }

    public String isArea(String area) {
        CodeMasterInfo codeInfo = new CodeMasterInfo();
        List<String> list = new ArrayList<String>();
        if (area == null) {
            area = null;
        }
        codeInfo.setCodekey("area");
        List<CodeMasterInfo> codeMaster = codeMasterInfo.getList(codeInfo);
        for (CodeMasterInfo code : codeMaster) {
            list.add(code.getName());
        }
        if (!list.contains(area)) {
            area = null;
        }
        return area;
    }

    public Map<String, Object> importKeyRoles(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            // projectKeyrolesDao.batchDelete();
            List<ProjectKeyroles> projectKeyroles = new ArrayList<>();
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
            for (int i = 1; i <= rowSize; i++) {
                row = sheet.getRow(i);
                ProjectKeyroles keyroles = new ProjectKeyroles();
                String name = getCellFormatValue(ExcelUtils.getCell(map, row, "姓名"));
                String zrAccount = getCellFormatValue(ExcelUtils.getCell(map, row, "工号"));
                String no = getCellFormatValue(ExcelUtils.getCell(map, row, "项目编码"));
                if (name == null || zrAccount == null || no == null) {
                    LOG.error("用户名和项目编号不能为空");
                    continue;
                }
                keyroles.setNo(no);
                keyroles.setName(name);
                keyroles.setZrAccount(zrAccount);
                keyroles.setPosition(getCellFormatValue(ExcelUtils.getCell(map, row, "关键角色类型")));
                keyroles.setReplyResults(getCellFormatValue(ExcelUtils.getCell(map, row, "答辩结果")));
                keyroles.setProCompetence(getCellFormatValue(ExcelUtils.getCell(map, row, "岗位胜任度")));
                keyroles.setStatus(getCellFormatValue(ExcelUtils.getCell(map, row, "状态")));
                projectKeyroles.add(keyroles);
            }
            projectKeyrolesDao.insertInfos(projectKeyroles);
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    public Map<String, Object> importRDPM(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 2) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }

            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 2);
            for (int i = 2; i <= rowSize; i++) {
                row = sheet.getRow(i);
                // LOG.info(getCellFormatValue(ExcelUtils.getCell(map, row, "考试类别")));
                // LOG.info(getCellFormatValue(ExcelUtils.getCell(map, row, "是否通过")));
                // LOG.info(getCellFormatValue(ExcelUtils.getCell(map, row, "工号")));
                String zrAccount = getCellFormatValue(ExcelUtils.getCell(map, row, "工号"));
                String examType = getCellFormatValue(ExcelUtils.getCell(map, row, "考试类别"));
                String rdpmExam = getCellFormatValue(ExcelUtils.getCell(map, row, "是否通过"));
                if (examType == null || zrAccount == null || rdpmExam == null) {
                    LOG.error("用户名和项目编号不能为空");
                    continue;
                }
                List<ProjectKeyroles> list = projectKeyrolesDao.queryProjectKeyrolesZrAccount(zrAccount);
                for (ProjectKeyroles projectKey : list) {
                    if ("通过".equals(projectKey.getRdpmExam())) {
                        continue;
                    }
                    if ("PMP".equals(examType) || "RDPM".equals(examType)) {
                        if ("通过".equals(rdpmExam)) {
                            projectKey.setRdpmExam(rdpmExam);
                        } else {
                            projectKey.setRdpmExam("未通过");
                        }
                        projectKeyrolesDao.updateMemberBase(projectKey);
                        projectKeyrolesDao.updateProjectStaff(projectKey);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    public Map<String, Object> importMaturityAssessment(MultipartFile file, String time) {
        Map<String, Object> result = new HashMap<String, Object>();
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String[] timeArray = time.split(" ~ ");
        String start = timeArray[0];
        String end = timeArray[1];
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();// 默认有第0行
            Row row = null;
            if (rowSize < 4) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 4);
            List<ProjectMaturityAssessment> pmas = new ArrayList<>();
            Set<String> nos = new HashSet<>();
            for (int i = 4; i <= rowSize; i++) {
                ProjectMaturityAssessment pma = new ProjectMaturityAssessment();
                row = sheet.getRow(i);
                String proName = getCellFormatValue(ExcelUtils.getCell(map, row, "项目名称"));
                if (proName == null) {
                    LOG.error("项目名称不能为空");
                    continue;
                }
                String proNo = projectMaturityAssessmentDao.getProjectNo(proName);
                if (proNo == null || "".equals(proNo.trim())) {
                    continue;
                }
                if (!nos.contains(proNo)) {// 判断是否有重复的项目，如果有跳出
                    nos.add(proNo);
                } else {
                    continue;
                }
                String d = getCellFormatValue(ExcelUtils.getCell(map, row, "项目实际运作与独立交付差距 注：黄、红灯必填"));
                String difference = d == null ? "" : d;
                pma.setNo(proNo);
                pma.setBu(getCellFormatValue(ExcelUtils.getCell(map, row, "业务线")));
                pma.setPdu(getCellFormatValue(ExcelUtils.getCell(map, row, "事业部")));
                pma.setDu(getCellFormatValue(ExcelUtils.getCell(map, row, "交付部")));
                pma.setHwpdu(getCellFormatValue(ExcelUtils.getCell(map, row, "一级部门")));
                pma.setHwzpdu(getCellFormatValue(ExcelUtils.getCell(map, row, "二级部门")));
                pma.setPduSpdt(getCellFormatValue(ExcelUtils.getCell(map, row, "三级部门")));
                pma.setProName(proName);
                pma.setHwVersion(getCellFormatValue(ExcelUtils.getCell(map, row, "华为版本号")));
                pma.setType(getCellFormatValue(ExcelUtils.getCell(map, row, "商务模式")));
                pma.setArea(getCellFormatValue(ExcelUtils.getCell(map, row, "地域")));
                pma.setPmName(getCellFormatValue(ExcelUtils.getCell(map, row, "PM姓名")));
                pma.setPmNo(getCellFormatValue(ExcelUtils.getCell(map, row, "PM工号")));
                pma.setQaName(getCellFormatValue(ExcelUtils.getCell(map, row, "QA姓名")));
                pma.setQaNo(getCellFormatValue(ExcelUtils.getCell(map, row, "QA工号")));
                pma.setProResource(getCellFormatValue(ExcelUtils.getCell(map, row, "项目总人力")));
                pma.setNeedInterface(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "需求及接口明确"))));
                pma.setAcceptStandard(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "验收标准明确"))));
                // pma.setBaseVersion(judge(getCellFormatValue(row.getCell(17))));
                pma.setWorkloadNeed(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "工作量估计与需求澄清"))));
                pma.setSowReview(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "SOW评审"))));
                pma.setPlan(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "计划"))));
                // pma.setNeed(judge(getCellFormatValue(row.getCell(21))));
                // pma.setScheme(judge(getCellFormatValue(row.getCell(22))));
                pma.setRpReview(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "RP评审"))));
                pma.setTestCase(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "测试用例"))));
                pma.setCodeReview(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "代码质量"))));
                pma.setDeveloperTest(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "开发者测试"))));
                pma.setTestEvaluation(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "转测试评估"))));
                pma.setIteratorTest(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "迭代测试"))));
                pma.setIteraorExport(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "迭代出口评估"))));
                pma.setSdvTest(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "SDV测试"))));
                pma.setSitTest(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "SIT测试"))));
                pma.setUatTest(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "UAT测试	"))));
                pma.setRrTest(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "RR评审"))));
                pma.setProPlan(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "项目计划"))));
                pma.setStructureChart(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "结构图"))));
                pma.setStrategyPlan(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "测试策略与计划"))));
                pma.setScenariosCase(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "测试方案与用例实现"))));
                pma.setTestProcedure(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "测试过程"))));
                pma.setTestDeliverables(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "测试交付件"))));
                pma.setScheduleManagement(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "进度管理"))));
                pma.setRequirementsManagement(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "端到端需求管理"))));
                pma.setChangeManagement(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "变更管理"))));
                pma.setRiskManagement(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "风险管理"))));
                pma.setCostCare(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "成本看护&nbsp;"))));
                // pma.setEngineAbility(judge(getCellFormatValue(row.getCell(44))));
                pma.setKeyRoleStability(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "关键角色稳定"))));
                pma.setPm(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "PM"))));
                pma.setBaSe(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "BA/SE"))));
                pma.setMde(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "MDE"))));
                pma.setTse(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "TSE"))));
                pma.setTc(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "TC"))));
                pma.setKeyRole(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "关键角色&nbsp;"))));
                pma.setPhaseAccept(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "阶段验收"))));
                pma.setDeliveryPreparation(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "交付件准备情况"))));
                pma.setProcessTrace(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "流程跟踪"))));
                pma.setProjectAccept(judge(getCellFormatValue(ExcelUtils.getCell(map, row, "结项验收"))));
                String totalScore = getCellFormatValue(ExcelUtils.getCell(map, row, "总分(自动计算)"));
                if (totalScore == null || "".equals(totalScore.trim())) {
                    pma.setTotalScore(null);
                } else {
                    pma.setTotalScore(totalScore);
                }
                pma.setDifference(difference);
                pma.setMark(getCellFormatValue(ExcelUtils.getCell(map, row, "标记")));
                pma.setStartTime(start);
                pma.setEndTime(end);
                pmas.add(pma);
            }
            // projectMaturityAssessmentDao.deleteProjectMaturityAssessment();
            if (pmas.size() > 0) {
                projectMaturityAssessmentDao.replaceProjectMaturityAssessment(pmas);
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    /**
     * 先根据项目名称去项目表里查出项目编号，查不出项目编号的不导入361成熟度表内
     *
     * @param proName
     * @return
     */
    private String getProjectNo(String proName) {
        String proNo = projectMaturityAssessmentDao.getProjectNo(proName);
        if (proNo == null || "".equals(proNo.trim())) {
            return proName;
        }
        return proNo;
    }

    /**
     * 判断分值表里是否为"_"
     *
     * @param str
     * @return
     */
    private String judge(String str) {
        if (str != null) {
            try {
                Double.valueOf(str.trim());
                return str.trim();
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    public Map<String, Object> initMaturityAssessment(ProjectInfo proj) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> maps = setMap(proj, null, null);
        List<Map<String, Object>> list = projectMaturityAssessmentDao.initMaturityAssessment(maps);
        result.put("sumPros", list.size());
        int red = 0;
        int yellow = 0;
        int green = 0;
        int no = 0;
        List<String> sumList = new ArrayList<>();
        List<String> redList = new ArrayList<>();
        List<String> yellowList = new ArrayList<>();
        List<String> greenList = new ArrayList<>();
        List<String> noList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            sumList.add((String) (map.get("NO")));
        }
        result.put("sumList", sumList);
        for (Map<String, Object> map : list) {
            // 2012业务线：80＜＝红灯,80＜黄灯＜＝85,85＜绿灯
            if (map.get("TOTALSCORE") == null || "".equals(map.get("TOTALSCORE"))) {
                no += 1;
                noList.add((String) (map.get("NO")));
            } else {
                Double score = Double.valueOf((String) (map.get("TOTALSCORE")));
                if (score <= 80.0) {
                    red += 1;
                    redList.add((String) (map.get("NO")));
                } else if (score > 80.0 && score <= 85.0) {
                    yellow += 1;
                    yellowList.add((String) (map.get("NO")));
                } else if (score > 85.0) {
                    green += 1;
                    greenList.add((String) (map.get("NO")));
                } else {
                    no += 1;
                    noList.add((String) (map.get("NO")));
                }
            }
        }
        result.put("redPros", red);
        result.put("yellowPros", yellow);
        result.put("greenPros", green);
        result.put("noPros", no);
        result.put("redList", redList);
        result.put("yellowList", yellowList);
        result.put("greenList", greenList);
        result.put("noList", noList);
        return result;
    }

    ;

    private Map<String, Object> setMap(ProjectInfo proj, String start, String end) {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(proj.getArea())) {
            String[] arr = proj.getArea().split(",");
            map.put("area", Arrays.asList(arr));
        } else {
            map.put("area", list);
        }
        if (StringUtils.isNotBlank(proj.getHwpdu())) {
            String[] arr = proj.getHwpdu().split(",");
            map.put("hwpdu", Arrays.asList(arr));
        } else {
            map.put("hwpdu", list);
        }
        if (StringUtils.isNotBlank(proj.getHwzpdu())) {
            String[] arr = proj.getHwzpdu().split(",");
            map.put("hwzpdu", Arrays.asList(arr));
        } else {
            map.put("hwzpdu", list);
        }
        if (StringUtils.isNotBlank(proj.getPduSpdt())) {
            String[] arr = proj.getPduSpdt().split(",");
            map.put("pduSpdt", Arrays.asList(arr));
        } else {
            map.put("pduSpdt", list);
        }
        if (StringUtils.isNotBlank(proj.getBu())) {
            String[] arr = proj.getBu().split(",");
            map.put("bu", Arrays.asList(arr));
        } else {
            map.put("bu", list);
        }
        if (StringUtils.isNotBlank(proj.getPdu())) {
            String[] arr = proj.getPdu().split(",");
            map.put("pdu", Arrays.asList(arr));
        } else {
            map.put("pdu", list);
        }
        if (StringUtils.isNotBlank(proj.getDu())) {
            String[] arr = proj.getDu().split(",");
            map.put("du", Arrays.asList(arr));
        } else {
            map.put("du", list);
        }
        if (StringUtils.isNotBlank(start)) {
            map.put("start", start);
        }
        if (StringUtils.isNotBlank(end)) {
            map.put("end", end);
        }
        return map;
    }

    /**
     * 先根据项目编号去项目表里查出项目名称，再去361成熟度表内查出 相关数据
     *
     * @param proName
     * @return
     */
    private String getProjectName(String proNo) {
        String proName = projectMaturityAssessmentDao.getProjectName(proNo);
        return proName;
    }

    public Map<String, Object> everyScore(String projNo) {
        String proName = getProjectName(projNo);
        Map<String, Object> map = projectMaturityAssessmentDao.everyScore(proName);
        Map<String, Object> result = new HashMap<>();
        if (map == null || map.size() == 0) {
            return null;
        }
        Set<String> keySet = map.keySet();
        String temp = null;
        for (String key : keySet) {
            if (map.get(key) == null) {
                result.put(key, "#f8f8f8");
            } else {
                temp = (String) (map.get(key));
                if ("".equals(temp.trim())) {
                    result.put(key, "#f8f8f8");
                } else {
                    // 绿灯>=2.5，2.5>黄灯>=1.5，红灯<1.5
                    Double score = Double.valueOf(temp);
                    if (score >= 2.5) {
                        result.put(key, "#3ddc66");
                    } else if (score >= 1.5 && score < 2.5) {
                        result.put(key, "#ecb739");
                    } else if (score < 1.5) {
                        result.put(key, "#f05132");
                    }
                }
            }

        }
        return result;
    }

    public Map<String, Object> comments(String projNo) {
        String proName = getProjectName(projNo);
        return projectMaturityAssessmentDao.comments(proName);
    }

    public TableSplitResult commentsList(String projNo, PageRequest pageRequest) {
        TableSplitResult ret = new TableSplitResult();

        com.github.pagehelper.Page page = PageHelper.startPage(
                (pageRequest.getPageNumber() == null ? 0 : (pageRequest.getPageNumber() - 1)) + 1,
                pageRequest.getPageSize());
        List<ProjectCommentsListInfo> re = projectMaturityAssessmentDao.searchCommentsList(projNo);
        ret.setRows(re);
        ret.setTotal((int) page.getTotal());
        ret.setPage(pageRequest.getPageNumber());
        // if (re.isEmpty()) {
        // ProjectCommentsListInfo projectCommentsListInfo = new
        // ProjectCommentsListInfo();
        // //设置id
        // projectCommentsListInfo.setNo(projNo);
        // //设置问题
        // Map<String, Object> result = comments(projNo);
        // projectCommentsListInfo.setQuestion(result.get("DIFFERENCE") == null?"":
        // result.get("DIFFERENCE").toString());
        // //设置创建、修改时间
        // Date date = new Date();
        // projectCommentsListInfo.setCreateTime(date);
        // projectCommentsListInfo.setModifyTime(date);
        // //设置是否是361成熟度评估问题
        // projectCommentsListInfo.setIs361(1);
        // //设置是否删除
        // projectCommentsListInfo.setIsDeleted(0);
        // //首次保存
        // editCommentsList(projectCommentsListInfo);
        // re.add(projectCommentsListInfo);
        // }
        return ret;
    }

    public void editCommentsList(ProjectCommentsListInfo projectCommentsListInfo) {
        projectMaturityAssessmentDao.replaceCommentsList(projectCommentsListInfo);
    }

    public byte[] export361(ProjectInfo proj, String color) {
        Map<String, Object> map = initMaturityAssessment(proj);
        List<String> list = new ArrayList<>();
        if ("red".equals(color.trim())) {
            list = (List<String>) map.get("redList");
        } else if ("yellow".equals(color.trim())) {
            list = (List<String>) map.get("yellowList");
        } else if ("green".equals(color.trim())) {
            list = (List<String>) map.get("greenList");
        } else if ("no".equals(color.trim())) {
            list = (List<String>) map.get("noList");
        } else if ("sum".equals(color.trim())) {
            list = (List<String>) map.get("sumList");
        }
        List<ProjectMaturityAssessment> projList = new ArrayList<>();
        ProjectMaturityAssessment pma = null;
        for (String no : list) {
            pma = projectMaturityAssessmentDao.export361(no);
            projList.add(pma);
        }
        InputStream is = this.getClass().getResourceAsStream("/excel/361Maturity-template.xlsx");
        Workbook wb = null;
        try {
            wb = new XSSFWorkbook(is);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            return null;
        }
        Sheet sheet = wb.getSheetAt(0);
        Row row = null;
        Cell cell = null;
        for (int i = 0; i < projList.size(); i++) {
            pma = projList.get(i);
            row = sheet.createRow(i + 4);
            cell = row.createCell(0);
            // cell.setCellValue(pma.getBu());
            cell.setCellValue("");
            cell = row.createCell(1);
            // cell.setCellValue(pma.getPdu());
            cell.setCellValue("");
            cell = row.createCell(2);
            // cell.setCellValue(pma.getDu());
            cell.setCellValue("");
            cell = row.createCell(3);
            cell.setCellValue(pma.getHwpdu());
            cell = row.createCell(4);
            cell.setCellValue(pma.getHwzpdu());
            cell = row.createCell(5);
            cell.setCellValue(pma.getPduSpdt());
            cell = row.createCell(6);
            cell.setCellValue(pma.getProName());
            cell = row.createCell(7);
            cell.setCellValue(pma.getHwVersion());
            cell = row.createCell(8);
            cell.setCellValue(pma.getType());
            cell = row.createCell(9);
            cell.setCellValue(pma.getArea());
            cell = row.createCell(10);
            cell.setCellValue(pma.getPmName());
            cell = row.createCell(11);
            cell.setCellValue(pma.getPmNo());
            cell = row.createCell(12);
            cell.setCellValue(pma.getQaName());
            cell = row.createCell(13);
            cell.setCellValue(pma.getQaNo());
            cell = row.createCell(14);
            cell.setCellValue(pma.getProResource());
            cell = row.createCell(15);
            cell.setCellValue(pma.getNeedInterface());
            cell = row.createCell(16);
            cell.setCellValue(pma.getAcceptStandard());
            // cell = row.createCell(17);
            // cell.setCellValue(pma.getBaseVersion());
            cell = row.createCell(17);
            cell.setCellValue(pma.getWorkloadNeed());
            cell = row.createCell(18);
            cell.setCellValue(pma.getSowReview());
            cell = row.createCell(19);
            cell.setCellValue(pma.getPlan());
            // cell = row.createCell(21);
            // cell.setCellValue(pma.getNeed());
            // cell = row.createCell(22);
            // cell.setCellValue(pma.getScheme());
            cell = row.createCell(20);
            cell.setCellValue(pma.getRpReview());
            cell = row.createCell(21);
            cell.setCellValue(pma.getTestCase());
            cell = row.createCell(22);
            cell.setCellValue(pma.getCodeReview());
            cell = row.createCell(23);
            cell.setCellValue(pma.getDeveloperTest());
            cell = row.createCell(24);
            cell.setCellValue(pma.getTestEvaluation());
            cell = row.createCell(25);
            cell.setCellValue(pma.getIteratorTest());
            cell = row.createCell(26);
            cell.setCellValue(pma.getIteraorExport());
            cell = row.createCell(27);
            cell.setCellValue(pma.getSdvTest());
            cell = row.createCell(28);
            cell.setCellValue(pma.getSitTest());
            cell = row.createCell(29);
            cell.setCellValue(pma.getUatTest());
            cell = row.createCell(30);
            cell.setCellValue(pma.getRrTest());
            cell = row.createCell(31);
            cell.setCellValue(pma.getProPlan());
            cell = row.createCell(32);
            cell.setCellValue(pma.getStructureChart());
            cell = row.createCell(33);
            cell.setCellValue(pma.getStrategyPlan());
            cell = row.createCell(34);
            cell.setCellValue(pma.getScenariosCase());
            cell = row.createCell(35);
            cell.setCellValue(pma.getTestProcedure());
            cell = row.createCell(36);
            cell.setCellValue(pma.getTestDeliverables());
            cell = row.createCell(37);
            cell.setCellValue(pma.getScheduleManagement());
            cell = row.createCell(38);
            cell.setCellValue(pma.getRequirementsManagement());
            cell = row.createCell(39);
            cell.setCellValue(pma.getChangeManagement());
            cell = row.createCell(40);
            cell.setCellValue(pma.getRiskManagement());
            cell = row.createCell(41);
            cell.setCellValue(pma.getCostCare());
            // cell.setCellValue(pma.getEngineAbility());
            cell = row.createCell(42);
            cell.setCellValue(pma.getKeyRoleStability());
            cell = row.createCell(43);
            cell.setCellValue(pma.getPm());
            cell = row.createCell(44);
            cell.setCellValue(pma.getBaSe());
            cell = row.createCell(45);
            cell.setCellValue(pma.getMde());
            cell = row.createCell(46);
            cell.setCellValue(pma.getTse());
            cell = row.createCell(47);
            cell.setCellValue(pma.getTc());
            cell = row.createCell(48);
            cell.setCellValue(pma.getKeyRole());
            cell = row.createCell(49);
            cell.setCellValue(pma.getPhaseAccept());
            cell = row.createCell(50);
            cell.setCellValue(pma.getDeliveryPreparation());
            cell = row.createCell(51);
            cell.setCellValue(pma.getProcessTrace());
            cell = row.createCell(52);
            cell.setCellValue(pma.getProjectAccept());
            cell = row.createCell(53);
            cell.setCellValue(pma.getTotalScore());
            cell = row.createCell(54);
            cell.setCellValue(pma.getDifference());
            cell = row.createCell(55);
            cell.setCellValue(pma.getMark());
        }
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            try {
                wb.write(os);
            } catch (IOException e) {
                LOG.error("export excel failed!", e);
            }
            return os.toByteArray();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            LOG.error("Output file failed!" + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public Map<String, Object> importpromemberOMBadd(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            // workbook = new XSSFWorkbook(is);
            if (file.getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (file.getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "当前文件不是excel文件");
                LOG.info("当前文件不是excel文件");
                return result;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
            for (int i = 2; i <= rowSize; i++) {
                row = sheet.getRow(i);
                TblTimeInformation tblTimeInformation = new TblTimeInformation();
                String idCard = getCellFormatValue(ExcelUtils.getCell(map, row, "身份证号码"));
                String zrAccount = getCellFormatValue(ExcelUtils.getCell(map, row, "工号"));
                if (zrAccount == null || idCard == null) {
                    LOG.info("身份证号和工号不能为空,导入失败");
                    continue;
                }
                List<ProjectMember> member = projectListDao.queryMembersByIdCard(idCard);
                if (member == null || member.size() <= 0) {
                    LOG.info("OMP数据不存在对应信息");
                    continue;
                }
                ProjectMember projectMember = member.get(0);
                projectMember.setZrAccount(zrAccount);
                projectListDao.insertmemberInfo(projectMember);
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    public Map<String, Object> importStar(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();// 默认有第0行
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
            List<StarRating> stars = new ArrayList<>();
            for (int i = 1; i <= rowSize; i++) {
                StarRating star = new StarRating();
                row = sheet.getRow(i);
                String id = getCellFormatValue(ExcelUtils.getCell(map, row, "流水号"));
                String name = getCellFormatValue(ExcelUtils.getCell(map, row, "项目名称"));
                String no = getCellFormatValue(ExcelUtils.getCell(map, row, "运营项目编码"));
                String level = getCellFormatValue(ExcelUtils.getCell(map, row, "项目星级"));
                String status = getCellFormatValue(ExcelUtils.getCell(map, row, "审批状态"));
                if (id == null || name == null || no == null || level == null || status == null) {
                    LOG.error("流水号,项目名称,运营项目编码,项目星级,审批状态都不能为空");
                    continue;
                }
                int statusNum = 0;
                if ("已通过".equals(status)) {
                    statusNum = 1;
                } else {
                    statusNum = 0;
                }
                star.setId(id);
                star.setName(name);
                star.setLevel(level);
                star.setNo(no);
                star.setStatus(statusNum);
                star.setPeopleNum(getCellFormatValue(ExcelUtils.getCell(map, row, "项目总人数")));
                star.setBonusDate(getCellFormatValue(ExcelUtils.getCell(map, row, "奖金申请月份")));
                star.setCycle(getCellFormatValue(ExcelUtils.getCell(map, row, "项目周期")));
                // String proNo = getProjectNo(id);
                stars.add(star);
            }
            starRatingDao.insertStarRating(stars);
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        return result;
    }

    /**
     * @Description: 分类查询每种类别的项目列表
     * @author Administrator
     * @date 2018年7月5日
     * @version V1.0
     */
    public List<Map<String, Object>> loadTabsByTypes(Map<String, Object> result, String type) {
        String strSql = "";
        // 红灯
        List<String> listRed = (List<String>) result.get("redList");
        // 黄灯
        List<String> listYellow = (List<String>) result.get("yellowList");
        // 绿灯
        List<String> listGreen = (List<String>) result.get("greenList");
        // 未测评
        List<String> wcpList = (List<String>) result.get("noList");
        if ("red".equals(type)) {
            strSql = StringUtilsLocal.listToSqlIn(listRed);
        } else if ("yellow".equals(type)) {
            strSql = StringUtilsLocal.listToSqlIn(listYellow);
        } else if ("green".equals(type)) {
            strSql = StringUtilsLocal.listToSqlIn(listGreen);
        } else if ("wcp".equals(type)) {
            strSql = StringUtilsLocal.listToSqlIn(wcpList);
        }
        if (StringUtils.isEmpty(strSql)) {
            return null;
        }
        return projectMaturityAssessmentDao.loadTabsByTypes("(" + strSql + ")");
    }

    public Map<String, Object> circularChartAssessment(ProjectInfo projectInfo, Integer continuousCycle, String cycle) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<String> time = DateUtils.evaluationCycle(continuousCycle, cycle, 0);// 获取时间区间
            String start = time.get(0);
            String end = time.get(1);
            Map<String, Object> maps = setMap(projectInfo, start, end);
            List<Map<String, Object>> list = projectMaturityAssessmentDao.assessmentChart(maps);
            Integer red = 0;
            Integer yellow = 0;
            Integer green = 0;
            Integer noPros = 0;
            if (null != list && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    Object obj = map.get("TOTALSCORE");
                    int sum = MathUtils.parseIntSmooth(obj, -1);
                    noPros = sum < 0 ? (noPros + 1) : noPros;
                    red = (sum > 0 && sum <= 80) ? (red + 1) : red;
                    yellow = (sum > 80 && sum <= 85) ? (yellow + 1) : yellow;
                    green = sum > 85 ? (green + 1) : green;
                }
            }
            result.put("red", red);
            result.put("yellow", yellow);
            result.put("green", green);
            result.put("noPros", noPros);
        } catch (Exception e) {
            LOG.error("read file failed!" + e.getMessage());
        }
        return result;
    }

    public List<List<Integer>> lineChartAssessment(ProjectInfo projectInfo, String cycle) {
        List<List<Integer>> resultList = new ArrayList<List<Integer>>();
        List<Integer> reds = new ArrayList<>();
        List<Integer> yellows = new ArrayList<>();
        List<Integer> greens = new ArrayList<>();
        List<Integer> noProses = new ArrayList<>();
        for (int i = 4; i > -1; i--) {
            List<String> time = DateUtils.evaluationCycle(1, cycle, i);
            try {
                String start = time.get(0);
                String end = time.get(1);
                Map<String, Object> maps = setMap(projectInfo, start, end);// 对时间区间和部门信息进行封装
                List<Map<String, Object>> list = projectMaturityAssessmentDao.assessmentChart(maps);
                Integer red = 0;
                Integer yellow = 0;
                Integer green = 0;
                Integer noPros = 0;
                if (null != list && list.size() > 0) {
                    for (Map<String, Object> map : list) {
                        Object obj = map.get("TOTALSCORE");
                        int sum = MathUtils.parseIntSmooth(obj, -1);
                        noPros = sum < 0 ? (noPros + 1) : noPros;
                        red = (sum > 0 && sum <= 80) ? (red + 1) : red;
                        yellow = (sum > 80 && sum <= 85) ? (yellow + 1) : yellow;
                        green = sum > 85 ? (green + 1) : green;
                    }
                }
                reds.add(red);
                yellows.add(yellow);
                greens.add(green);
                noProses.add(noPros);
            } catch (Exception e) {
                LOG.error("read file failed!" + e.getMessage());
            }
        }
        resultList.add(reds);
        resultList.add(yellows);
        resultList.add(greens);
        resultList.add(noProses);
        return resultList;
    }

    public List<Map<String, Object>> departmentList(ProjectInfo projectInfo, String client, String departmentalLevel,
                                                    String cycle) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String username = CookieUtils.getCookie(request, CookieUtils.COOKIE_KEY_USERNAME);
        UserInfo userInfo = userManagerDao.getUserInfoByName(username);
        if (StringUtils.isBlank(projectInfo.getHwpdu()) || StringUtils.isBlank(projectInfo.getBuId())) {
            projectInfo.setHwpdu("32");
        }
        Map<String, Object> params = new HashMap<>();
        String level = "2";// 部门级别
        if ("2".equals(departmentalLevel)) {
            level = "3";
        }
        setValue2Param(client, params, projectInfo, userInfo);
        params.put("level", level);
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        if ("2".equals(client)) {
            lists = projectMaturityAssessmentDao.hwDepartment(params);
        } else if ("1".equals(client)) {
            lists = projectMaturityAssessmentDao.zrDepartment(params);
        }
        List<Map<String, Object>> list = new ArrayList<>();// 本周361评估和项目信息
        List<Map<String, Object>> lastList = new ArrayList<>();// 上周361评估信息
        for (int i = 0; i < 2; i++) {// 获取时间区间并查询
            List<String> time = DateUtils.evaluationCycle(1, cycle, i);
            try {
                params.put("start", time.get(0));
                params.put("end", time.get(1));
            } catch (Exception e) {
                LOG.error("时间区间获取错误");
                params.put("start", " ");
                params.put("end", " ");
            }
            if (i == 0) {
                list = projectMaturityAssessmentDao.DepartmentData(params);
            } else if (i == 1) {
                lastList = projectMaturityAssessmentDao.DepartmentData(params);
            }
        }
        String projectName = "";
        for (Map<String, Object> map : lists) {
            Map<String, Object> resultMap = new HashMap<>();
            ProjectWeekLighting light = new ProjectWeekLighting();
            Object obj = map.get("dept_name");
            String project = obj == null ? "" : String.valueOf(obj);
            if ("2".equals(client)) {
                if ("".equals(userInfo.getHwpdu()) || userInfo.getHwpdu() == null) {
                    return resultList;
                }
                if ("1".equals(departmentalLevel)) {// 华为二级部门
                    projectName = "HWZPDU";
                    light = WeeksLighting(list, lastList, project, light, projectName);// 获取两周的点灯数据
                } else if ("2".equals(departmentalLevel)) {// 华为三级部门
                    projectName = "PDU_SPDT";
                    light = WeeksLighting(list, lastList, project, light, projectName);
                }
            } else if ("1".equals(client)) {
                if ("".equals(userInfo.getBu()) || userInfo.getBu() == null) {
                    return resultList;
                }
                if ("1".equals(departmentalLevel)) {// 中软二级部门
                    projectName = "PDU";
                    light = WeeksLighting(list, lastList, project, light, projectName);
                } else if ("2".equals(departmentalLevel)) {// 中软三级部门
                    projectName = "DU";
                    light = WeeksLighting(list, lastList, project, light, projectName);
                }
            }
            resultMap.put("project", project);
            resultMap.put("red", light.getRed());
            resultMap.put("yellow", light.getYellow());
            resultMap.put("green", light.getGreen());
            resultMap.put("noPros", light.getNoPros());
            resultMap.put("lastRed", light.getLastRed());
            resultMap.put("lastYellow", light.getLastYellow());
            resultMap.put("lastGreen", light.getLastGreen());
            resultMap.put("lastNoPros", light.getLastNoPros());
            resultList.add(resultMap);
        }
        return resultList;
    }

    // 361评估的本周和上周数据获取
    public ProjectWeekLighting WeeksLighting(List<Map<String, Object>> list, List<Map<String, Object>> lastList,
                                             String project, ProjectWeekLighting light, String projectName) {
        Object obj = null;
        if (list.size() > 0 && list != null) {
            for (Map<String, Object> map : list) {
                if (project.equals(map.get(projectName))) {
                    Integer red = light.getRed();
                    Integer yellow = light.getYellow();
                    Integer green = light.getGreen();
                    Integer noPros = light.getNoPros();
                    obj = map.get("TOTAL_SCORE");
                    int sum = MathUtils.parseIntSmooth(obj, -1);
                    light.setNoPros(sum < 0 ? (noPros + 1) : noPros);
                    light.setRed((sum > 0 && sum <= 80) ? (red + 1) : red);
                    light.setYellow((sum > 80 && sum <= 85) ? (yellow + 1) : yellow);
                    light.setGreen(sum > 85 ? (green + 1) : green);
                }
            }
        }
        if (lastList.size() > 0 && lastList != null) {
            for (Map<String, Object> lastMap : lastList) {
                if (project.equals(lastMap.get(projectName))) {
                    Integer lastRed = light.getLastRed();
                    Integer lastYellow = light.getLastYellow();
                    Integer lastGreen = light.getLastGreen();
                    Integer lastNoPros = light.getLastNoPros();
                    obj = lastMap.get("TOTAL_SCORE");
                    int sum = MathUtils.parseIntSmooth(obj, -1);
                    light.setLastNoPros(sum < 0 ? (lastNoPros + 1) : lastNoPros);
                    light.setLastRed((sum > 0 && sum <= 80) ? (lastRed + 1) : lastRed);
                    light.setLastYellow((sum > 80 && sum <= 85) ? (lastYellow + 1) : lastYellow);
                    light.setLastGreen(sum > 85 ? (lastGreen + 1) : lastGreen);
                }
            }
        }
        return light;
    }

    // 筛选权限
    private void setValue2Param(String client, Map<String, Object> params, ProjectInfo projectInfo, UserInfo userInfo) {
        if ("2".equals(client)) {// 华为
            List<String> hwpduId = CollectionUtilsLocal.splitToList(projectInfo.getHwpdu());
            List<String> hwzpduId = CollectionUtilsLocal.splitToList(projectInfo.getHwzpdu());
            List<String> pduSpdtId = CollectionUtilsLocal.splitToList(projectInfo.getPduSpdt());
            if (hwpduId.size() == 0 || hwpduId == null) {
                hwpduId = CollectionUtilsLocal.splitToList(userInfo.getHwpdu());
            }
            if (hwzpduId.size() == 0 || hwzpduId == null) {
                hwzpduId = CollectionUtilsLocal.splitToList(userInfo.getHwzpdu());
            }
            if (pduSpdtId.size() == 0 || pduSpdtId == null) {
                pduSpdtId = CollectionUtilsLocal.splitToList(userInfo.getPduspdt());
            }
            params.put("hwpdu", hwpduId);
            params.put("hwzpdu", hwzpduId);
            params.put("pduSpdt", pduSpdtId);
        } else if ("1".equals(client)) {// 中软
            List<String> buId = CollectionUtilsLocal.splitToList(projectInfo.getBu());
            List<String> pduId = CollectionUtilsLocal.splitToList(projectInfo.getPdu());
            List<String> duId = CollectionUtilsLocal.splitToList(projectInfo.getDu());
            if (buId.size() == 0 || buId == null) {
                buId = CollectionUtilsLocal.splitToList(userInfo.getBu());
            }
            if (pduId.size() == 0 || pduId == null) {
                pduId = CollectionUtilsLocal.splitToList(userInfo.getDu());
            }
            if (duId.size() == 0 || duId == null) {
                duId = CollectionUtilsLocal.splitToList(userInfo.getDept());
            }
            params.put("bu", buId);
            params.put("pdu", pduId);
            params.put("du", duId);
        }
    }

    public ProjectStatusLighting projectStatusAnalysis(ProjectInfo projectInfo, String date) {
        String username = CookieUtils.value(request, CookieUtils.USER_NAME);
        Map<String, Object> map = new HashMap<>();
        ProjectStatusLighting list = new ProjectStatusLighting();
        projectInfoService.setParamNew(projectInfo, username, map);
        map.put("date", date);
        try {
            List<ProjectStatus> data = projectMaturityAssessmentDao.projectOverview(map);
            List<Integer> vals = new ArrayList<>();
            for (ProjectStatus status : data) {
                Integer val = status.getStatusReview();
                vals.add(val);
            }
            list = LightUp.getColors(vals);
        } catch (Exception e) {
            LOG.error("read file failed!" + e.getMessage());
        }
        return list;
    }

    public TableSplitResult projectOverview(ProjectInfo projectInfo, Integer limit, Integer offset, String date) {
        TableSplitResult data = new TableSplitResult();
        String username = CookieUtils.value(request, CookieUtils.USER_NAME);
        Map<String, Object> map = new HashMap<>();
        projectInfoService.setParamNew(projectInfo, username, map);
        map.put("date", date);
        map.put("project_status", 1);
        try {
            com.github.pagehelper.Page page = PageHelper.startPage((offset == null ? 0 : offset) + 1, limit);
            List<ProjectStatus> list = projectMaturityAssessmentDao.projectOverview(map);
            data.setTotal((int) page.getTotal());
            for (ProjectStatus status : list) {
                if ("0".equals(projectInfo.getClientType())) {
                    status.setClientType("0");
                    status.setBusinessId(status.getHwpduId());
                    status.setCauseId(status.getHwzpduId());
                    status.setDeliverId(status.getPduSpdtId());
                    status.setBusinessUnit(status.getHwzpdu());
                    status.setDepartment(status.getPduSpdt());
                } else if ("1".equals(projectInfo.getClientType())) {
                    status.setClientType("1");
                    status.setBusinessId(status.getBuId());
                    status.setCauseId(status.getPduId());
                    status.setDeliverId(status.getDuId());
                    status.setBusinessUnit(status.getPdu());
                    status.setDepartment(status.getDu());
                }
            }
            data.setRows(list);
        } catch (Exception e) {
            LOG.error("read file failed!" + e.getMessage());
        }
        return data;
    }

    public List<MaturityAssessment> getAssessmentCategory(String createTime, String projNo, String template) {
        List<MaturityAssessment> everyScore = new ArrayList<>();
        try {
            everyScore = maturityAssessmentDao.getAssessmentCategory(createTime, projNo, template);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return everyScore;
    }


    /**
     * 361成熟度
     *
     * @Description: excel导入项目评估
     */
    public Map<String, Object> importAssessment(MultipartFile file, String template, String createTime, HttpServletRequest request) {
        int sucNum = 0;
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }

            List<Map<String, Object>> assessmentIdList = maturityAssessmentDao.getIdByTemplate(template);

            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 4);
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 4; i <= rowSize; i++) {
                try {
                    List<Map<String, Object>> assessmentList = new ArrayList<>();
                    row = sheet.getRow(i);
                    String proNo = getCellFormatValue(ExcelUtils.getCell(map, row, "项目ID"));
                    for (Map<String, Object> maps : assessmentIdList) {
                        Map<String, Object> assessmentMap = new HashMap<String, Object>();
                        String assessmentValue = getCellFormatValue(ExcelUtils.getCell(map, row, StringUtilsLocal.valueOf(
                                maps.get("name"))));
                        if (StringUtilsLocal.isBlank(assessmentValue) || "-".equals(assessmentValue)) {
                            assessmentValue = "";
                        }
                        assessmentMap.put("assessmentId", StringUtilsLocal.valueOf(maps.get("id")));
                        assessmentMap.put("result", assessmentValue);
                        assessmentMap.put("no", proNo);
                        assessmentMap.put("template", template);
                        Date date = new Date();
                        assessmentMap.put("createTime", createTime);
                        assessmentMap.put("updateTime", date);
                        assessmentList.add(assessmentMap);
                    }
                    if (assessmentList.isEmpty()) {
                        continue;
                    }
                    int a = 0;
                    a = maturityAssessmentDao.insertAssessmentCategory(assessmentList);
                    if (a > 0) {
                        sucNum += assessmentList.size();
                    }
                } catch (Exception e) {
                    LOG.error(e);
                }
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        result.put("sucNum", sucNum);
        return result;
    }

    /**
     * 361成熟度
     *
     * @Description: 获取template_361
     */
    public List<Map<String, Object>> getTemplateIs361() {
        return maturityAssessmentDao.getTemplateIs361();
    }

    /**
     * 361成熟度模板值
     *
     * @Description: 获取361模板值
     */
    public String getTemplateValue(String proNo) {
        return maturityAssessmentDao.getTemplateValue(proNo);
    }

    public void updateTemplateValue(String proNo, String template) {
        try {
            maturityAssessmentDao.updateTemplateValue(proNo, template);
        } catch (Exception e) {
            LOG.error("maturityAssessmentDao.updateTemplateValue exception, error: " + e.getMessage());
        }
    }

    public Map<String, Object> importRank(MultipartFile file) {
        int sucNum = 0;
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        List<Integer> numberList = new ArrayList<>();

        try {
            is = file.getInputStream();
            workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowSize = sheet.getLastRowNum();
            Row row = null;
            if (rowSize < 1) {
                result.put("STATUS", "FAILED");
                result.put("MESSAGE", "没有要导入的数据！");
                return result;
            }
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
            List<String> rankList = temaInfoVoDao.getRankList();
            List<String> statusList = Arrays.asList("在岗", "后备", "离职");

            for (int i = 1; i <= rowSize; i++) {
                try {
                    row = sheet.getRow(i);
                    String account = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "工号")))
                            ? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "工号")).split("\\.")[0])
                            : null;
                    String memberName = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "姓名")))
                            ? StringUtilsLocal.clearSpaceAndLine(getCellFormatValue(ExcelUtils.getCell(map, row, "姓名")))
                            : null;
                    String status = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "人员状态")))
                            ? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "人员状态")))
                            : null;
                    String rank = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "职级")))
                            ? StringUtilsLocal.clearSpaceAndLine(getCellFormatValue(ExcelUtils.getCell(map, row, "职级")))
                            .toUpperCase()
                            : null;
                    String idCard = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "身份证号")))
                            ? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "身份证号")))
                            .toUpperCase()
                            : null;
                    String businessGroup = StringUtils
                            .isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "业务群")))
                            ? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "业务群")))
                            : null;
                    String businessLine = StringUtils
                            .isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "业务线")))
                            ? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "业务线")))
                            : null;
                    String causeUnit = StringUtils
                            .isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "事业部")))
                            ? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "事业部")))
                            : null;
                    String deleveryUnit = StringUtils
                            .isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "交付部")))
                            ? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "交付部")))
                            : null;
                    if (StringUtils.isBlank(account)) {
//						result.put("account", "empty");
                        numberList.add(i + 1);
                        continue;
                    }

                    if (StringUtils.isNotBlank(account) && account.length() > 10) {
//						result.put("chinasoftAccountSize", chinasoft);
                        numberList.add(i + 1);
                        continue;
                    }

                    if (StringUtils.isBlank(memberName)) {
//						result.put("memberName", "empty");
                        numberList.add(i + 1);
                        continue;
                    }

                    if (StringUtils.isNotBlank(idCard) && idCard.length() != 18) {
//						result.put("idCardLen", "fault");
                        numberList.add(i + 1);
                        continue;
                    }

                    status = StringUtils.isNotBlank(status) && "在职".equals(status) ? "在岗" : status;
                    if (StringUtils.isNotBlank(status) && !statusList.contains(status)) {
                        numberList.add(i + 1);
                        continue;
                    }

                    if (StringUtils.isNotBlank(rank) && !rankList.contains(rank) && !"不涉及".equals(rank)) {
//						result.put("rank", "false");
                        numberList.add(i + 1);
                        continue;
                    }

                    if (StringUtils.isNotBlank(account) && !Pattern.compile("^[-\\+]?[\\d]*$").matcher(account).matches()) {
//						result.put("account", "false");
                        numberList.add(i + 1);
                        continue;
                    }

                    if (StringUtils.isNotBlank(memberName) && !Pattern.compile("^[\\u4e00-\\u9fa5\\s]+$").matcher(memberName).matches()) {
//						result.put("memberName", "false");
                        numberList.add(i + 1);
                        continue;
                    }

                    if (StringUtils.isNotBlank(idCard) && !Pattern.compile("^[A-Za-z0-9\\s]+$").matcher(idCard).matches()) {
//						result.put("idCard", "false");
                        numberList.add(i + 1);
                        continue;
                    }

                    TeamMembers teamMembers = new TeamMembers();
                    String chinasoftAccount = StringUtilsLocal.zeroFill(account, 10);

                    teamMembers.setZrAccount(chinasoftAccount);
                    teamMembers.setName(memberName);
                    teamMembers.setStatus("在职".equals(status) ? "在岗" : status);
                    teamMembers.setIdCard(idCard);
                    teamMembers.setRank("不涉及".equals(rank) ? "" : rank);
                    teamMembers.setBusinessGroup(businessGroup);
                    teamMembers.setBusinessLine(businessLine);
                    teamMembers.setCauseUnit(causeUnit);
                    teamMembers.setDeliveryUnit(deleveryUnit);

                    int memberBaseCount = projectManagersDao.getMemberRankCount(chinasoftAccount);
                    int a = 0;

                    if (memberBaseCount == 0) {
                        a = projectManagersDao.addRankToArchive(teamMembers);
                    } else {
                        a = projectManagersDao.updateRankToArchive(teamMembers);
                    }

                    if (a > 0) {
                        sucNum += 1;
                    }
                } catch (Exception e) {
                    LOG.error(e);
                }
            }
        } catch (IOException e) {
            LOG.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        result.put("sucNum", sucNum);
        result.put("numberList", numberList);
        return result;
    }
}
