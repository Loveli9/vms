package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.icss.mvp.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.MemberInforDao;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.ExcelImportUtils;
import com.icss.mvp.util.ExcelUtils;

@Service
@SuppressWarnings("all")
public class MemberInforService {
    private final static Logger LOG = Logger.getLogger(MemberInforService.class);

    @Autowired
    private MemberInforDao MemberInforDao;

    public ProjectInformation projectInformation(String projectId) {
        ProjectInformation date = null;
        try {
            date = MemberInforDao.projectInformation(projectId);
        } catch (Exception e) {
            LOG.error("MemberInforDao.projectInformation exception, error: " + e.getMessage());
        }
        return date;
    }

    public List<ProjectPostInfor> memberinformation(String projectId) {
        List<ProjectPostInfor> data = new ArrayList();
        List<String> posts = new ArrayList();
        try {
            List<ProjectPostInfor> list = MemberInforDao.memberinformation(projectId);
            for (ProjectPostInfor map : list) {
                String post = map.getPost();
                if (!posts.contains(post)) {
                    map.setRank(projectPostRank(post));
                    posts.add(post);
                    data.add(map);
                } else {
                    int i = posts.indexOf(post);
                    ProjectPostInfor maps = data.get(i);
                    //maps.setActual((maps.getActual()+map.getActual()));
                    maps.setBudget((maps.getBudget() + map.getBudget()));
                    data.set(i, maps);
                }
            }
        } catch (Exception e) {
            LOG.error("MemberInforDao.memberinformation exception, error: " + e.getMessage());
        }
        return data;
    }

    public String projectPostRank(String post) {
        String rank = " ";
        switch (post) {
            case "PM":
                rank = "6级";
                break;
            case "SE":
                rank = "5级";
                break;
            case "开发工程师":
                rank = "5级";
                break;
        }
        return rank;
    }

    public void saveMembersInformation(ProjectMembersInformation project) {
        MemberInforDao.saveMembersInformation(project);
    }

    public TableSplitResult queryMembersInformation(String projectId, Integer limit, Integer offset) {
        TableSplitResult data = new TableSplitResult();
        try {
            com.github.pagehelper.Page page = PageHelper.startPage((offset == null ? 0 : offset) + 1, limit);
            List<ProjectMembersInformation> list = MemberInforDao.queryMembersInformation(projectId);
            data.setTotal((int) page.getTotal());
            data.setRows(list);
        } catch (Exception e) {
            LOG.error("export excel failed!", e);
        }
        return data;
    }

    public ProjectMembersInformation editDataPosts(String id) {
        ProjectMembersInformation data = MemberInforDao.editDataPosts(id);
        return data;
    }

    public void saveProjectInformation(ProjectMembersInformation project) {
        if ("2".equals(project.getBackbone())) {
            project.setBackbone("1");
        } else {
            project.setBackbone("0");
        }//是否骨干数值调整
        String time = DateUtils.getTodayDay();
        double workingLift = DateUtils.differenceTime(project.getGraduation(), time) / 365;//工作年限
        project.setWorkingLift(Double.toString(decimal(workingLift)));
        String channelRank = "";//正常通道职级
        if (workingLift > 19) {
            project.setChannelRank("12级");
        } else {
            double channel = Math.floor(workingLift);
            channelRank = getChannelRank(project.getEducation(), channel);
            project.setChannelRank(channelRank);
        }
        double payInterval = project.getLastAdjustmeny().isEmpty() ? (DateUtils.differenceTime(project.getEntry(), time) / 365)
                : (DateUtils.differenceTime(project.getLastAdjustmeny(), time) / 365);//调薪间隔
        project.setPayInterval(Double.toString(decimal(payInterval)));
        double workingHours = DateUtils.differenceTime(project.getJobStart(), time) / 365;//当前岗位工作时长
        project.setWorkingHours(Double.toString(decimal(workingHours)));
        int i = 0;//绩效评分
        List<Integer> achievements = achievementsValue(project.getAchievementsFour(), i);
        Integer achievement = achievements.get(0);
        i = achievements.get(1);
        achievements = achievementsValue(project.getAchievementsThree(), i);
        achievement += achievements.get(0);
        i = achievements.get(1);
        achievements = achievementsValue(project.getAchievementsTwo(), i);
        achievement += achievements.get(0);
        i = achievements.get(1);
        achievements = achievementsValue(project.getAchievementsOne(), i);
        achievement += achievements.get(0);
        i = achievements.get(1);
        double performanceScore = 0;
        if (i != 0) {
            performanceScore = (double) achievement / i;
        }
        project.setPerformanceScore(Double.toString(decimal(performanceScore)));
        String rankInversion = "1";//是否审视职级倒挂
        if ((Integer.parseInt(channelRank.substring(0, channelRank.length() - 1)) - Integer.parseInt(project.getRank().substring(0, project.getRank().length() - 1))) > 1) {
            rankInversion = "0";
        }
        project.setRankInversion(rankInversion);
        double payAdjusetmment = payInterval * performanceScore / 3 * ("0".equals(project.getBackbone()) ? 2 : 1) * ("0".equals(rankInversion) ? 1.5 : 1.0);//调薪指数
        project.setPayAdjusetmment(Double.toString(decimal(payAdjusetmment)));
        String changeOfPost = workingHours > 2 ? "0" : "1";//是否建议换岗
        project.setChangeOfPost(changeOfPost);
        String elimination = "1";//是否建议淘汰
        if (performanceScore < 3) {
            elimination = "0";
        } else if (performanceScore == 3 && i == 4) {
            elimination = "0";
        }
        project.setElimination(elimination);
        MemberInforDao.saveProjectInformation(project);
    }

    public List<Integer> achievementsValue(String achievement, int i) {
        List<Integer> val = new ArrayList<>();
        Integer value = 0;
        if (!achievement.isEmpty()) {
            switch (achievement) {
                case "A":
                    value = 6;
                    break;
                case "B+":
                    value = 4;
                    break;
                case "B":
                    value = 3;
                    break;
                case "C":
                    value = 1;
                    break;
                case "D":
                    value = 0;
                    break;
                case "0":
                    value = 0;
                    break;
            }
            i++;
        }
        val.add(value);
        val.add(i);
        return val;
    }

    public String getChannelRank(String education, double channel) {
        int channels = 0;
        if ("大专".equals(education)) {
            channels = (int) Math.floor(channel / 2) + 3;
        } else if ("本科".equals(education)) {
            if ((int) channel == 0) {
                channels = 3;
            } else {
                channels = (int) Math.floor(channel / 2) + 4;
            }
        } else if ("硕士".equals(education)) {
            if ((int) channel == 0) {
                channels = 4;
            } else {
                channels = (int) Math.floor((channel - 1) / 2) + 5;
            }
        }
        channels = channels > 12 ? 12 : channels;
        String channelRank = String.valueOf(channels) + "级";
        return channelRank;
    }

    public double decimal(double d) {//保留两位小数
        long l = Math.round(d * 100); // 四舍五入
        return l / 100.0;
    }

    public void setCellValue(int i, String value, Row row, CellStyle cellStyle) {
        Cell cell = row.createCell(i);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 表头样式
     *
     * @param wb
     * @return
     */
    public CellStyle getHeadStyle(Workbook wb) {
        //表头样式
        CellStyle headerStyle = wb.createCellStyle();
        //水平居中
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //设置边框
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        //设置颜色
        headerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    /**
     * 单元格样式
     *
     * @param wb
     * @return
     */
    public CellStyle getCellStyle(Workbook wb) {
        // 单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        //设置边框
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        //设置自动换行
        cellStyle.setWrapText(true);
        //设置字体
        Font cellFont = wb.createFont();
        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }

    public byte[] returnWb2Byte(Workbook wb) {
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
//				strCell = String.valueOf(cell.getNumericCellValue());
                BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                strCell = big.toPlainString();
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_FORMULA: //公式类型
                try {
                    strCell = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    strCell = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            default:
                strCell = null;
                break;
        }
        return strCell;
    }

    /**
     * excel导入成员
     *
     * @param file
     * @param proNo
     * @param request
     * @return
     */
    public Map<String, Object> importMembers(MultipartFile file, String proNo, HttpServletRequest request) {
        int memberCount = 0;
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        ExcelImportUtils excelImportUtils = new ExcelImportUtils();
        UserEntity userInfo = (UserEntity) request.getSession().getAttribute(Constants.USER_KEY);
        String userId = userInfo.getId();// 获取当前登录人
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
            List<String> list = new ArrayList<>();
            list.add("开发工程师");
            list.add("测试工程师");
            list.add("PM");
            list.add("产品经理");
            list.add("SE");
            list.add("MDE");
            list.add("BA");
            list.add("IA");
            list.add("TC");
            list.add("TSE");
            list.add("QA");
            list.add("TL");
            list.add("UI");
            list.add("运维工程师");
            list.add("数据工程师");
            list.add("资料工程师");
            for (int i = 1; i <= rowSize; i++) {
                try {
                    row = sheet.getRow(i);
                    String keyRole = getCellFormatValue(excelImportUtils.getCellValue(sheet, i, map, "是否SOW要求的关键角色"));
                    String budget = StringUtils.isNotBlank(getCellFormatValue(excelImportUtils.getCellValue(sheet, i, map, "预算人数"))) ?
                            getCellFormatValue(excelImportUtils.getCellValue(sheet, i, map, "预算人数")).split("\\.")[0] : null;

                    String account = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "工号"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "工号")).split("\\.")[0] : null;
                    String name = getCellFormatValue(ExcelUtils.getCell(map, row, "姓名"));
                    String area = getCellFormatValue(ExcelUtils.getCell(map, row, "地域"));
                    String education = getCellFormatValue(ExcelUtils.getCell(map, row, "学历"));
                    String school = getCellFormatValue(ExcelUtils.getCell(map, row, "是否211"));
                    String graduation = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "毕业时间"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "毕业时间")).split("\\.")[0] : null;
                    String entry = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "入职时间"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "入职时间")).split("\\.")[0] : null;
                    String quitday = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "离职时间"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "离职时间")).split("\\.")[0] : null;
                    String projectEntry = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "当前项目入项时间（运营平台）后备人员（关键角色都需要）"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "当前项目入项时间（运营平台）后备人员（关键角色都需要）")).split("\\.")[0] : null;
                    String projectOutput = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "当前项目出项时间（运营平台)"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "当前项目出项时间（运营平台)")).split("\\.")[0] : null;
                    String jobStart = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "从事当前岗位起始时间"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "从事当前岗位起始时间")).split("\\.")[0] : null;
                    String lastAdjustmeny = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "上次调薪时间"))) ?
                            getCellFormatValue(ExcelUtils.getCell(map, row, "上次调薪时间")).split("\\.")[0] : null;
                    String siling = getCellFormatValue(ExcelUtils.getCell(map, row, "司龄"));
                    String achievementsFour = getCellFormatValue(ExcelUtils.getCell(map, row, "倒数第四次绩效")).toUpperCase();
                    String achievementsThree = getCellFormatValue(ExcelUtils.getCell(map, row, "倒数第三次绩效")).toUpperCase();
                    String achievementsTwo = getCellFormatValue(ExcelUtils.getCell(map, row, "倒数第二次绩效")).toUpperCase();
                    String achievementsOne = getCellFormatValue(ExcelUtils.getCell(map, row, "最近一次绩效")).toUpperCase();
                    String rank = getCellFormatValue(ExcelUtils.getCell(map, row, "职级")).toUpperCase();
                    String post = getCellFormatValue(ExcelUtils.getCell(map, row, "岗位")).toUpperCase();
                    String backupPersonel = getCellFormatValue(ExcelUtils.getCell(map, row, "后备人员（关键角色都需要）"));
                    String staffMentor = getCellFormatValue(ExcelUtils.getCell(map, row, "员工导师（关键角色新上岗或新员工）"));
                    String backbone = getCellFormatValue(ExcelUtils.getCell(map, row, "是否骨干"));
                    String ompKeyrles = getCellFormatValue(ExcelUtils.getCell(map, row, "是否OMP首期验收的关键角色"));
                    String entryData = getCellFormatValue(ExcelUtils.getCell(map, row, "OMP是否在项目启动两周内入项"));
                    String incentiveMatrix = getCellFormatValue(ExcelUtils.getCell(map, row, "激励矩阵分类"));
                    String workingLift = getCellFormatValue(ExcelUtils.getCell(map, row, "工作年限"));
                    String channelRank = getCellFormatValue(ExcelUtils.getCell(map, row, "正常通道职级"));
                    String payInterval = getCellFormatValue(ExcelUtils.getCell(map, row, "调薪间隔"));
                    String workingHours = getCellFormatValue(ExcelUtils.getCell(map, row, "当前岗位工作时长"));
                    String performanceScore = getCellFormatValue(ExcelUtils.getCell(map, row, "绩效评分"));
                    String rankInversion = getCellFormatValue(ExcelUtils.getCell(map, row, "是否审视职级倒挂"));
                    String payAdjusetmment = getCellFormatValue(ExcelUtils.getCell(map, row, "调薪指数"));
                    String changeOfPost = getCellFormatValue(ExcelUtils.getCell(map, row, "是否建议换岗"));
                    String elimination = getCellFormatValue(ExcelUtils.getCell(map, row, "是否建议淘汰"));
                    int zr = account.length();
                    if (zr > 10) {
                        result.put("zrAccountSize", zr);
                    }
                    if (!list.contains(post)) {
                        result.put("post", "false");
                        break;
                    }
                    if (!("大专".equals(education) || "本科".equals(education) || "硕士".equals(education))) {
                        result.put("education", "false");
                    }
                    if (StringUtils.isBlank(graduation)) {
                        result.put("graduation", "false");
                    }
                    if (StringUtils.isBlank(entry)) {
                        result.put("entry", "false");
                    }
                    if (StringUtils.isBlank(achievementsFour)) {
                        result.put("achievementsFour", "false");
                    }
                    if (StringUtils.isBlank(achievementsThree)) {
                        result.put("achievementsThree", "false");
                    }
                    if (StringUtils.isBlank(achievementsTwo)) {
                        result.put("achievementsTwo", "false");
                    }
                    if (StringUtils.isBlank(achievementsOne)) {
                        result.put("achievementsOne", "false");
                    }
                    if (StringUtils.isBlank(rank)) {
                        result.put("rank", "false");
                    }
                    if (StringUtils.isBlank(backbone)) {
                        result.put("backbone", "false");
                    }
                    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                    if (!pattern.matcher(account).matches()) {
                        break;
                    } else {
                        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
                        ProjectPostInfor projectPostInfor = new ProjectPostInfor();
                        ProjectMembersInformation projectMembersInfo = new ProjectMembersInformation();
                        String zrAccount = decimalFormat.format(Integer.valueOf(account));
                        try {
                            if (StringUtils.isNotBlank(keyRole)) {
                                if ("是".equals(keyRole)) {
                                    projectPostInfor.setRoleOrPost("角色");
                                } else {
                                    projectPostInfor.setRoleOrPost("岗位");
                                }
                                String keyRoleVal = "是".equals(keyRole) ? "0" : "1";
                                projectPostInfor.setKeyRole(keyRoleVal);
                                projectPostInfor.setPost(post);
                                projectPostInfor.setBudget(StringUtils.isNotBlank(budget) ? Integer.parseInt(budget) : null);
                                List<ProjectPostInfor> projectPost = MemberInforDao.getPostCount(proNo, projectPostInfor);
                                if (projectPost.size() < 1) {
                                    MemberInforDao.addPostInformation(proNo, projectPostInfor);
                                } else {
                                    if ((projectPostInfor.getPost() + projectPostInfor.getKeyRole()).equals(projectPost.get(0).getPost() + projectPost.get(0).getKeyRole())) {
                                        if (!projectPostInfor.getBudget().equals(projectPost.get(0).getBudget())) {
                                            projectPostInfor.setBudget(projectPostInfor.getBudget() + projectPost.get(0).getBudget());
                                            MemberInforDao.updatePostInformation(proNo, projectPostInfor);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("MemberInforDao.updatePostInformation exception, errror: " + e.getMessage());
                        }
                        projectMembersInfo.setNo(proNo);
                        projectMembersInfo.setZrAccount(zrAccount);
                        projectMembersInfo.setName(name);
                        projectMembersInfo.setArea(area);
                        projectMembersInfo.setEducation(education);
                        projectMembersInfo.setSchool("是".equals(school) ? "0" : "1");
                        projectMembersInfo.setGraduation(graduation != null || graduation.length() != 0 ? transformDate(graduation) : null);
                        projectMembersInfo.setEntry(entry != null || entry.length() != 0 ? transformDate(entry) : null);
                        projectMembersInfo.setQuitday(quitday != null ? transformDate(quitday) : null);
                        projectMembersInfo.setSiling(String.format("%.2f", Double.parseDouble(siling)));
                        projectMembersInfo.setAchievementsFour(achievementsFour);
                        projectMembersInfo.setAchievementsThree(achievementsThree);
                        projectMembersInfo.setAchievementsTwo(achievementsTwo);
                        projectMembersInfo.setAchievementsOne(achievementsOne);
                        projectMembersInfo.setRank(rank);
                        projectMembersInfo.setPost(post);
                        projectMembersInfo.setBackupPersonel(backupPersonel);
                        projectMembersInfo.setStaffMentor(staffMentor);
                        projectMembersInfo.setBackbone("是".equals(backbone) ? "0" : "1");
                        projectMembersInfo.setOmpKeyrles("是".equals(ompKeyrles) ? "0" : "1");
                        projectMembersInfo.setEntryData("是".equals(entryData) ? "0" : "1");
                        projectMembersInfo.setProjectEntry(projectEntry != null || projectEntry.length() != 0 ? transformDate(projectEntry) : null);
                        projectMembersInfo.setProjectOutput(projectOutput != null || projectOutput.length() != 0 ? transformDate(projectOutput) : null);
                        projectMembersInfo.setJobStart(jobStart != null || jobStart.length() != 0 ? transformDate(jobStart) : null);
                        projectMembersInfo.setLastAdjustmeny(lastAdjustmeny != null || lastAdjustmeny.length() != 0 ? transformDate(lastAdjustmeny) : null);
                        projectMembersInfo.setIncentiveMatrix(incentiveMatrix);
                        projectMembersInfo.setWorkingLift(String.format("%.2f", Double.parseDouble(workingLift)));
                        projectMembersInfo.setChannelRank(channelRank);
                        projectMembersInfo.setPayInterval(String.format("%.2f", Double.parseDouble(payInterval)));
                        projectMembersInfo.setWorkingHours(String.format("%.2f", Double.parseDouble(workingHours)));
                        projectMembersInfo.setPerformanceScore(String.format("%.2f", Double.parseDouble(performanceScore)));
                        projectMembersInfo.setRankInversion("是".equals(rankInversion) ? "0" : "1");
                        projectMembersInfo.setPayAdjusetmment(String.format("%.2f", Double.parseDouble(payAdjusetmment)));
                        projectMembersInfo.setChangeOfPost("是".equals(changeOfPost) ? "0" : "1");
                        projectMembersInfo.setElimination("是".equals(elimination) ? "0" : "1");
                        projectMembersInfo.setKeyRole(StringUtils.isNotBlank(keyRole) ? "是".equals(keyRole) ? "0" : "1" : null);
                        int zrAccountSize = MemberInforDao.getZRAccountCount(proNo, zrAccount);
                        if (zrAccountSize < 1) {
                            int a = MemberInforDao.addMemberInfo(projectMembersInfo);
                            if (a > 0) {
                                memberCount += 1;
                            }
                        }
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
        result.put("memberCount", memberCount);
        return result;
    }

    private String transformDate(String val) {
        Date date = null;
        SimpleDateFormat sdf = null;
        String lastDate = null;
        if (val != null || val.length() != 0) {
            date = new Date();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                int intVal = Integer.parseInt(val);
                Calendar calendar = new GregorianCalendar(1900, 0, -1);
                calendar.add(Calendar.DATE, intVal);
                date = calendar.getTime();
                lastDate = sdf.format(date);
            } catch (Exception e) {
                LOG.error("number transform date failed: " + e.getMessage());
            }
        }
        return lastDate;
    }

    /**
     * 导出成员
     *
     * @param proNo
     * @return
     */
    public byte[] exportMembers(String proNo) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("成员信息");
        CellStyle headerStyle = getHeadStyle(wb);
        CellStyle cellStyle = getCellStyle(wb);
        int border = 1;

        /************************生成表格头***************************/

        int num = 0;
        Date newtime = new Date();
        Row row = sheet.createRow(num);
        setCellValue(0, "预算岗位", row, headerStyle);
        setCellValue(1, "是否SOW要求的关键角色", row, headerStyle);
        setCellValue(2, "预算职级", row, headerStyle);
        setCellValue(3, "预算人数", row, headerStyle);
        setCellValue(4, "实际匹配人数", row, headerStyle);
        setCellValue(5, "工号", row, headerStyle);
        setCellValue(6, "姓名", row, headerStyle);
        setCellValue(7, "地域", row, headerStyle);
        setCellValue(8, "学历", row, headerStyle);
        setCellValue(9, "是否211", row, headerStyle);
        setCellValue(10, "毕业时间", row, headerStyle);
        setCellValue(11, "入职时间", row, headerStyle);
        setCellValue(12, "离职时间", row, headerStyle);
        setCellValue(13, "司龄", row, headerStyle);
        setCellValue(14, "倒数第四次绩效", row, headerStyle);
        setCellValue(15, "倒数第三次绩效", row, headerStyle);
        setCellValue(16, "倒数第二次绩效", row, headerStyle);
        setCellValue(17, "最近一次绩效", row, headerStyle);
        setCellValue(18, "职级", row, headerStyle);
        setCellValue(19, "岗位", row, headerStyle);
        setCellValue(20, "后备人员（关键角色都需要）", row, headerStyle);
        setCellValue(21, "员工导师（关键角色新上岗或新员工）", row, headerStyle);
        setCellValue(22, "是否骨干", row, headerStyle);
        setCellValue(23, "是否OMP首期验收的关键角色", row, headerStyle);
        setCellValue(24, "OMP是否在项目启动两周内入项", row, headerStyle);
        setCellValue(25, "当前项目入项时间（运营平台）后备人员（关键角色都需要）", row, headerStyle);
        setCellValue(26, "当前项目出项时间（运营平台)", row, headerStyle);
        setCellValue(27, "从事当前岗位起始时间", row, headerStyle);
        setCellValue(28, "上次调薪时间", row, headerStyle);
        setCellValue(29, "激励矩阵分类", row, headerStyle);
        setCellValue(30, "工作年限", row, headerStyle);
        setCellValue(31, "正常通道职级", row, headerStyle);
        setCellValue(32, "调薪间隔", row, headerStyle);
        setCellValue(33, "当前岗位工作时长", row, headerStyle);
        setCellValue(34, "绩效评分", row, headerStyle);
        setCellValue(35, "是否审视职级倒挂", row, headerStyle);
        setCellValue(36, "调薪指数", row, headerStyle);
        setCellValue(37, "是否建议换岗", row, headerStyle);
        setCellValue(38, "是否建议淘汰", row, headerStyle);

        sheet.autoSizeColumn(0);
        sheet.setColumnWidth(0, sheet.getColumnWidth(0) * 3);
        for (int i = 1; i <= 38; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
        }
        num++;

        /*********************获取项目表格信息************************/

        List<ProjectMembersInformation> projectMembersInfo = MemberInforDao.queryProjectMembersInfo(proNo);
        List<ProjectPostInfor> projectPostInfo = MemberInforDao.queryProjectPostInfo(proNo);
        List<ProjectMembersInformation> posts = MemberInforDao.queryActualNumberOfPost(proNo);
        if (projectMembersInfo == null || projectMembersInfo.size() <= 0) {
            return returnWb2Byte(wb);
        }

        Map<Object, String> map = new HashMap<Object, String>();
        Object position = null;
        String demand = null;
        for (ProjectPostInfor projectpost : projectPostInfo) {
            position = projectpost.getPost() + projectpost.getKeyRole();
            demand = projectpost.getBudget().toString();
            map.put(position, demand);
        }

        Map<Object, String> map1 = new HashMap<Object, String>();
        Object position1 = null;
        String actual = null;
        for (ProjectMembersInformation projpost : posts) {
            position1 = projpost.getPost() + projpost.getKeyRole();
            actual = projpost.getActual();

            map1.put(position1, actual);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, String>> dataset = new ArrayList<Map<String, String>>();

        for (ProjectMembersInformation member : projectMembersInfo) {
            row = sheet.createRow(num);
            member.setBudgetRank(projectPostRank(member.getPost()));

            Set<Object> get = map.keySet();
            for (Object test : get) {
                if ((member.getPost() + member.getKeyRole()).equals(test)) {
                    member.setBudget(map.get(test).toString());
                }
            }

            Set<Object> get1 = map1.keySet();
            for (Object test : get1) {
                if ((member.getPost() + member.getKeyRole()).equals(test)) {
                    member.setActual(map1.get(test).toString());
                }
            }

            Map<String, String> map3 = new HashMap<String, String>();
            map3.put("budgetPost", member.getPost());
            map3.put("sowkeyRole", member.getKeyRole());
            dataset.add(map3);

            String graduation = null;
            String entry = null;
            String quitday = null;
            String projectEntry = null;
            String projectOutput = null;
            String jobStart = null;
            String lastAdjustmeny = null;
            try {
                graduation = member.getGraduation() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, format.parse(member.getGraduation()));
                entry = member.getEntry() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, format.parse(member.getEntry()));
                quitday = member.getQuitday() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, format.parse(member.getQuitday()));
                projectEntry = member.getProjectEntry() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, format.parse(member.getProjectEntry()));
                projectOutput = member.getProjectOutput() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, format.parse(member.getProjectOutput()));
                jobStart = member.getJobStart() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, format.parse(member.getJobStart()));
                lastAdjustmeny = member.getLastAdjustmeny() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, format.parse(member.getLastAdjustmeny()));
            } catch (ParseException e) {
                LOG.error("DateUtils.formatDate exception, error: " + e.getMessage());
            }
            setCellValue(0, StringUtils.isNotBlank(member.getPost()) ? member.getPost() : "", row, cellStyle);
            setCellValue(1, StringUtils.isNotBlank(member.getKeyRole()) ? "1".equals(member.getKeyRole()) ? "否" : "是" : "", row, cellStyle);
            setCellValue(2, StringUtils.isNotBlank(member.getBudgetRank()) ? member.getBudgetRank() : "", row, cellStyle);
            setCellValue(3, StringUtils.isNotBlank(member.getBudget()) ? member.getBudget() : "", row, cellStyle);
            setCellValue(4, StringUtils.isNotBlank(member.getActual()) ? member.getActual() : "", row, cellStyle);

            setCellValue(5, StringUtils.isNotBlank(member.getZrAccount()) ? member.getZrAccount() : "", row, cellStyle);
            setCellValue(6, StringUtils.isNotBlank(member.getName()) ? member.getName() : "", row, cellStyle);
            setCellValue(7, StringUtils.isNotBlank(member.getArea()) ? member.getArea() : "", row, cellStyle);
            setCellValue(8, StringUtils.isNotBlank(member.getEducation()) ? member.getEducation() : "", row, cellStyle);
            setCellValue(9, StringUtils.isNotBlank(member.getSchool()) ? "1".equals(member.getSchool()) ? "否" : "是" : "", row, cellStyle);
            setCellValue(10, graduation, row, cellStyle);
            setCellValue(11, entry, row, cellStyle);
            setCellValue(12, quitday, row, cellStyle);
            setCellValue(13, StringUtils.isNotBlank(member.getSiling()) ? member.getSiling() : "", row, cellStyle);
            setCellValue(14, StringUtils.isNotBlank(member.getAchievementsFour()) ? member.getAchievementsFour() : "", row, cellStyle);
            setCellValue(15, StringUtils.isNotBlank(member.getAchievementsThree()) ? member.getAchievementsThree() : "", row, cellStyle);
            setCellValue(16, StringUtils.isNotBlank(member.getAchievementsTwo()) ? member.getAchievementsTwo() : "", row, cellStyle);
            setCellValue(17, StringUtils.isNotBlank(member.getAchievementsOne()) ? member.getAchievementsOne() : "", row, cellStyle);
            setCellValue(18, StringUtils.isNotBlank(member.getRank()) ? member.getRank() : "", row, cellStyle);
            setCellValue(19, StringUtils.isNotBlank(member.getPost()) ? member.getPost() : "", row, cellStyle);
            setCellValue(20, StringUtils.isNotBlank(member.getBackupPersonel()) ? member.getBackupPersonel() : "", row, cellStyle);
            setCellValue(21, StringUtils.isNotBlank(member.getStaffMentor()) ? member.getStaffMentor() : "", row, cellStyle);
            setCellValue(22, StringUtils.isNotBlank(member.getBackbone()) ? "1".equals(member.getBackbone()) ? "否" : "是" : "", row, cellStyle);
            setCellValue(23, StringUtils.isNotBlank(member.getOmpKeyrles()) ? "1".equals(member.getOmpKeyrles()) ? "否" : "是" : "", row, cellStyle);
            setCellValue(24, StringUtils.isNotBlank(member.getEntryData()) ? "1".equals(member.getEntryData()) ? "否" : "是" : "", row, cellStyle);
            setCellValue(25, projectEntry, row, cellStyle);
            setCellValue(26, projectOutput, row, cellStyle);
            setCellValue(27, jobStart, row, cellStyle);
            setCellValue(28, lastAdjustmeny, row, cellStyle);
            setCellValue(29, StringUtils.isNotBlank(member.getIncentiveMatrix()) ? member.getIncentiveMatrix() : "", row, cellStyle);
            setCellValue(30, StringUtils.isNotBlank(member.getWorkingLift()) ? member.getWorkingLift() : "", row, cellStyle);
            setCellValue(31, StringUtils.isNotBlank(member.getChannelRank()) ? member.getChannelRank() : "", row, cellStyle);
            setCellValue(32, StringUtils.isNotBlank(member.getPayInterval()) ? member.getPayInterval() : "", row, cellStyle);
            setCellValue(33, StringUtils.isNotBlank(member.getWorkingHours()) ? member.getWorkingHours() : "", row, cellStyle);
            setCellValue(34, StringUtils.isNotBlank(member.getPerformanceScore()) ? member.getPerformanceScore() : "", row, cellStyle);
            setCellValue(35, StringUtils.isNotBlank(member.getRankInversion()) ? "1".equals(member.getRankInversion()) ? "否" : "是" : "", row, cellStyle);
            setCellValue(36, StringUtils.isNotBlank(member.getPayAdjusetmment()) ? member.getPayAdjusetmment() : "", row, cellStyle);
            setCellValue(37, StringUtils.isNotBlank(member.getChangeOfPost()) ? "1".equals(member.getChangeOfPost()) ? "否" : "是" : "", row, cellStyle);
            setCellValue(38, StringUtils.isNotBlank(member.getElimination()) ? "1".equals(member.getElimination()) ? "否" : "是" : "", row, cellStyle);
            num++;
        }
        int initialRow = 0;
        int terminationRow = 0;
        int i = 0;
        for (i = 0; i < dataset.size(); i++) {
            if (i < dataset.size() - 1) {
                if (StringUtils.isNotBlank(dataset.get(i).get("sowkeyRole"))) {
                    if (dataset.get(i).get("budgetPost").equals(dataset.get(i + 1).get("budgetPost"))
                            && dataset.get(i).get("sowkeyRole").equals(dataset.get(i + 1).get("sowkeyRole"))) {
                        for (int j = i + 2; j <= dataset.size(); j++) {
                            if (j < dataset.size()) {
                                if (dataset.get(i).get("budgetPost").equals(dataset.get(j).get("budgetPost"))
                                        && dataset.get(i).get("sowkeyRole").equals(dataset.get(j).get("sowkeyRole"))) {
                                    initialRow = i;
                                }
                            } else {
                                terminationRow = j - 1;
                                if (terminationRow - initialRow > 0) {
                                    CellRangeAddress region = new CellRangeAddress(initialRow + 1, terminationRow + 1, 0, 0);
                                    sheet.addMergedRegion(region);
                                    RegionUtil.setBorderBottom(border, region, sheet, wb);
                                    RegionUtil.setBorderLeft(border, region, sheet, wb);
                                    RegionUtil.setBorderRight(border, region, sheet, wb);
                                    RegionUtil.setBorderTop(border, region, sheet, wb);

                                    CellRangeAddress region1 = new CellRangeAddress(initialRow + 1, terminationRow + 1, 1, 1);
                                    sheet.addMergedRegion(region1);
                                    RegionUtil.setBorderBottom(border, region1, sheet, wb);
                                    RegionUtil.setBorderLeft(border, region1, sheet, wb);
                                    RegionUtil.setBorderRight(border, region1, sheet, wb);
                                    RegionUtil.setBorderTop(border, region1, sheet, wb);

                                    CellRangeAddress region2 = new CellRangeAddress(initialRow + 1, terminationRow + 1, 2, 2);
                                    sheet.addMergedRegion(region2);
                                    RegionUtil.setBorderBottom(border, region2, sheet, wb);
                                    RegionUtil.setBorderLeft(border, region2, sheet, wb);
                                    RegionUtil.setBorderRight(border, region2, sheet, wb);
                                    RegionUtil.setBorderTop(border, region2, sheet, wb);

                                    CellRangeAddress region3 = new CellRangeAddress(initialRow + 1, terminationRow + 1, 3, 3);
                                    sheet.addMergedRegion(region3);
                                    RegionUtil.setBorderBottom(border, region3, sheet, wb);
                                    RegionUtil.setBorderLeft(border, region3, sheet, wb);
                                    RegionUtil.setBorderRight(border, region3, sheet, wb);
                                    RegionUtil.setBorderTop(border, region3, sheet, wb);

                                    CellRangeAddress region4 = new CellRangeAddress(initialRow + 1, terminationRow + 1, 4, 4);
                                    sheet.addMergedRegion(region4);
                                    RegionUtil.setBorderBottom(border, region4, sheet, wb);
                                    RegionUtil.setBorderLeft(border, region4, sheet, wb);
                                    RegionUtil.setBorderRight(border, region4, sheet, wb);
                                    RegionUtil.setBorderTop(border, region4, sheet, wb);

                                    initialRow = 0;
                                    i = terminationRow;
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }
        return returnWb2Byte(wb);
    }

    public List<ProjectMembersInformation> listByProjectNo(String projectNo) {
        return this.MemberInforDao.queryProjectMembersInfo(projectNo);
    }
}
