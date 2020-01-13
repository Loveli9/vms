package com.icss.mvp.service;

import static com.icss.mvp.util.ExcelUtils.getCellValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.constant.Constants;
import com.icss.mvp.dao.IDtsTaskDao;
import com.icss.mvp.dao.IterationCycleDao;
import com.icss.mvp.dao.IterationMeasureIndexDao;
import com.icss.mvp.dao.IterativeWorkManageDao;
import com.icss.mvp.dao.IterativeWorkManageEditDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.IterativeWorkManage;
import com.icss.mvp.entity.IterativeWorkManageEdit;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectMembersLocal;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.system.DictEntryItemEntity;
import com.icss.mvp.entity.system.UserEntity;
import com.icss.mvp.service.system.DictionaryService;
import com.icss.mvp.util.CollectionUtilsLocal;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.ExcelUtils;
import com.icss.mvp.util.StringUtilsLocal;
import com.icss.mvp.util.UUIDUtil;

/**
 * @author chengchenhui
 * @ClassName: IterativeWorkManageService
 * @Description: 需求管理逻辑处理层
 * @date 2018年8月3日
 */
@Service @SuppressWarnings("all") public class IterativeWorkManageService {

    private final static Logger                     logger = Logger.getLogger(IterativeWorkManageService.class);
    @Autowired private   IterativeWorkManageDao     dao;
    @Autowired private   IterativeWorkManageEditDao editDao;
    @Autowired private   IterationCycleDao          iterationCycleDao;
    @Autowired private   IDtsTaskDao                dtsTaskDao;
//    @Autowired private   SysDictItemService         dictItemService;
    @Autowired private   IterationMeasureIndexDao   iterationMeasureIndexDao;
//    @Autowired private   ISvnTaskDao                svnTaskDao;
//    @Autowired private   SysDictItemDao             sysDictItemDao;

    /**
     * @Description: 分页查询需求管理信息 @param @return 参数 @return List<IterativeWorkManage>
     * 返回类型 @throws
     */
    public TableSplitResult queryIteWorkManageInfo(TableSplitResult page, String proNo, String type, String hwAccount,
                                                   String sort, String sortOrder) {
        TableSplitResult data = new TableSplitResult();
        // 查询总记录条数
        Integer total = 0;
        hwAccount = "admin".equals(hwAccount) ? null : hwAccount;
        List<ProjectMembersLocal> list = editDao.members(proNo, null, hwAccount);
        ProjectMembersLocal projectMembers = (list != null && list.size() == 1) ? list.get(0) : null;
        String zrAccount = projectMembers != null ? projectMembers.getZrAccount().replaceAll("^(0+)", "") : null;
        try {
            total = dao.queryTotalCount(proNo, (!"ready".equals(type) ? null : zrAccount), page);
        } catch (Exception e) {
            logger.error("dao.queryTotalCount exception, error: " + e.getMessage());
        }
        List<IterativeWorkManage> dataList = new ArrayList<>();
        if (total > 0) {
            dataList = dao.queryIteWorkManagePage(page, proNo, (!"ready".equals(type) ? null : zrAccount), sort,
                                                  sortOrder);
            getPersonLiable(dataList, proNo);
            for (IterativeWorkManage iwm : dataList) {
                IterativeWorkManageEdit edit = editDao.queryIterativeWorkManageEdit(iwm.getId());
                if (edit != null && projectMembers != null) {
                    String checkResult = edit.getCheckResult();
                    if ("refuse".equals(checkResult)) {
                        iwm.setRemarks("未通过");
                    } else if ("pass".equals(checkResult)) {
                        iwm.setRemarks("已通过");
                    } else if (checkResult.indexOf(zrAccount) == -1) {
                        iwm.setRemarks("待审核");
                    } else if (checkResult.indexOf(zrAccount) != -1) {
                        iwm.setRemarks("待其他责任人审核");
                    }
                    if (iwm.getPersonLiable() != null && iwm.getPersonLiable().length() > 0) {
                        if (iwm.getPersonLiable().indexOf(projectMembers.getName()) != -1) {
                            iwm.setDesign("PersonLiable");// 匹配责任人
                        }
                    }
                    if (projectMembers.getName().equals(edit.getCreator())) {
                        iwm.setLuNumber("creator");
                    }
                }
            }
        }
        data.setTotal(total);
        data.setRows(dataList);
        return data;
    }

    private void getPersonLiable(List<IterativeWorkManage> dataList, String proNo) {
        List<Map<String, Object>> members = dao.getProjectMebersSelect(proNo);
        Map<String, String> membersNew = new HashMap<>();
        if (members != null && members.size() > 0) {
            for (Map<String, Object> map : members) {
                membersNew.put(String.valueOf(map.get("value")).replaceAll("^(0+)", ""),
                               String.valueOf(map.get("text")));
            }
        }
        for (IterativeWorkManage iwm : dataList) {
            String personLiable = iwm.getPersonLiable();
            if (personLiable == null || personLiable.trim().length() == 0) {
                continue;
            }
            String[] personLiables = personLiable.split(",");
            List<String> personLiablesNew = new ArrayList<>();
            String newPersonLiablesCount = "";
            for (String person : personLiables) {
                if (membersNew.get(person) == null) {
                    continue;
                }
                newPersonLiablesCount += person + ",";
                personLiablesNew.add(membersNew.get(person));
            }
            if (newPersonLiablesCount.length() > 0) {
                newPersonLiablesCount = newPersonLiablesCount.substring(0, newPersonLiablesCount.length() - 1);
            }
            if (personLiable.length() != newPersonLiablesCount.length()) {
                editDao.personLiable(newPersonLiablesCount, iwm.getId());
            }
            iwm.setPersonLiable(StringUtils.strip(personLiablesNew.toString(), "[]"));
        }
    }

    /**
     * @Description: 新增迭代管理内容 @param 参数 @return void 返回类型 @throws
     */
    public void addIteWorkManageInfo(IterativeWorkManage iteInfo, HttpServletRequest request) throws Exception {
        iteInfo.setId(UUIDUtil.getNew());
        iteInfo.setCreateTime(new Date());
        iteInfo.setUpdateTime(new Date());
        if (iteInfo.getPlanStartTime() != null) {
            String planStartTime = DateUtils.STANDARD_FORMAT_GENERAL.format(iteInfo.getPlanStartTime()).substring(0,
                                                                                                                  10);
            planStartTime += " 00:00:00";
            iteInfo.setPlanStartTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(planStartTime));
        }
        if (iteInfo.getPlanEndTime() != null) {
            String planEndTime = DateUtils.STANDARD_FORMAT_GENERAL.format(iteInfo.getPlanEndTime()).substring(0, 10);
            planEndTime += " 23:59:59";
            iteInfo.setPlanEndTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(planEndTime));
        }
        if (iteInfo.getStartTime() != null) {
            String startTime = DateUtils.STANDARD_FORMAT_GENERAL.format(iteInfo.getStartTime()).substring(0, 10);
            startTime += " 00:00:00";
            iteInfo.setStartTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(startTime));
        }
        if (iteInfo.getEndTime() != null) {
            String endTime = DateUtils.STANDARD_FORMAT_GENERAL.format(iteInfo.getEndTime()).substring(0, 10);
            endTime += " 23:59:59";
            iteInfo.setEndTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(endTime));
        }
        iteInfo.setExpectHours(String.valueOf(Double.valueOf(iteInfo.getExpectHours())));
        iteInfo.setActualHours(iteInfo.getExpectHours());// 将预计工作量记录备份，用于需求修改后工作量变更时进行计算
        iteInfo.setIsDeleted(0);
        UserEntity userInfo = (UserEntity) request.getSession().getAttribute(Constants.USER_KEY);
        String userId = userInfo.getId();// 获取当前登录人
        if (StringUtils.isEmpty(userId)) {
            iteInfo.setCreator("");
        } else {
            iteInfo.setCreator(userId);
        }
        int a = dao.addIteWorkManageInfo(iteInfo);
    }

    /**
     * @param iterativeWorkManage
     * @Description: 编辑需求管理工作内容 @param 参数 @return void 返回类型 @throws
     */

    public void editIteWorkManageInfo(IterativeWorkManage iterativeWorkManage) throws Exception {
        iterativeWorkManage.setUpdateTime(new Date());
        IterativeWorkManage temp = dao.openEditPage(iterativeWorkManage.getId());
        if (!iterativeWorkManage.getExpectHours().equals(temp.getActualHours()) && "1".equals(temp.getChange())) {
            iterativeWorkManage.setChange("2");
        }
        if ("1".equals(iterativeWorkManage.getChange()) && !"1".equals(temp.getChange())) {
            iterativeWorkManage.setActualHours(iterativeWorkManage.getExpectHours());
        }
        dao.editIteWorkManageInfo(iterativeWorkManage);
    }

    /**
     * @param IterativeWorkManageEdit
     * @Description: 编辑需求管理工作内容 暂存另一张表
     */

    public String editIteWorkManageEdit(IterativeWorkManage iterativeWorkManage) throws Exception {
        // 责任人直接修改原表
        // editDao.personLiable(iterativeWorkManage.getPersonLiable(),
        // iterativeWorkManage.getId());

        IterativeWorkManage temp = dao.openEditPage(iterativeWorkManage.getId());
        if (!iterativeWorkManage.getExpectHours().equals(temp.getActualHours()) && "1".equals(temp.getChange())) {
            iterativeWorkManage.setChange("2");
        }
        if ("1".equals(iterativeWorkManage.getChange()) && !"1".equals(temp.getChange())) {
            iterativeWorkManage.setActualHours(iterativeWorkManage.getExpectHours());
        }

        IterativeWorkManageEdit iterativeWorkManageEdit = new IterativeWorkManageEdit();
        iterativeWorkManageEdit.setId(iterativeWorkManage.getId());
        iterativeWorkManageEdit.setIteType(iterativeWorkManage.getIteType());
        iterativeWorkManageEdit.setTopic(iterativeWorkManage.getTopic());
        ProjectMembersLocal member = editDao.members(iterativeWorkManage.getProNo(), null,
                                                     iterativeWorkManage.getCreator()).get(0);
        iterativeWorkManageEdit.setCreator(member.getName());
        // Integer solver = iterativeWorkManage.getPersonLiable() == null
        // || "".equals(iterativeWorkManage.getPersonLiable()) ? null
        // : iterativeWorkManage.getPersonLiable().split(",").length;
        // iterativeWorkManageEdit.setSolver(solver == null ? null :
        // String.valueOf(solver));
        iterativeWorkManageEdit.setSolver(iterativeWorkManage.getSolvePerson());
        iterativeWorkManageEdit.setPrior(iterativeWorkManage.getPrior());
        iterativeWorkManageEdit.setImportance(iterativeWorkManage.getImportance());
        iterativeWorkManageEdit.setStatus(iterativeWorkManage.getStatus());
        iterativeWorkManageEdit.setChange(iterativeWorkManage.getChange());
        iterativeWorkManageEdit.setVersion(iterativeWorkManage.getVersion());
        iterativeWorkManageEdit.setCreateTime(new Date());
        iterativeWorkManageEdit.setUpdateTime(new Date());
        iterativeWorkManageEdit.setIteId(iterativeWorkManage.getIteId());
        iterativeWorkManageEdit.setProNo(iterativeWorkManage.getProNo());
        if (iterativeWorkManage.getStartTime() != null) {
            String startTime = DateUtils.STANDARD_FORMAT_GENERAL.format(iterativeWorkManage.getStartTime()).substring(0,
                                                                                                                      10);
            startTime += " 00:00:00";
            iterativeWorkManageEdit.setStartTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(startTime));
        } else {
            iterativeWorkManageEdit.setStartTime(null);
        }
        if (iterativeWorkManage.getEndTime() != null) {
            String endTime = DateUtils.STANDARD_FORMAT_GENERAL.format(iterativeWorkManage.getEndTime()).substring(0,
                                                                                                                  10);
            endTime += " 23:59:59";
            iterativeWorkManageEdit.setEndTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(endTime));
        } else {
            iterativeWorkManageEdit.setEndTime(null);
        }
        iterativeWorkManageEdit.setDescribeInfo(iterativeWorkManage.getDescribeInfo());
        iterativeWorkManageEdit.setIsDeleted(iterativeWorkManage.getIsDeleted());
        iterativeWorkManageEdit.setExpectHours(iterativeWorkManage.getExpectHours());
        iterativeWorkManageEdit.setActualHours(iterativeWorkManage.getActualHours());
        iterativeWorkManageEdit.setWrField(iterativeWorkManage.getWrField());
        if (iterativeWorkManage.getPlanStartTime() != null) {
            String planStartTime = DateUtils.STANDARD_FORMAT_GENERAL.format(
                    iterativeWorkManage.getPlanStartTime()).substring(0, 10);
            planStartTime += " 00:00:00";
            iterativeWorkManageEdit.setPlanStartTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(planStartTime));
        } else {
            iterativeWorkManageEdit.setPlanStartTime(null);
        }
        if (iterativeWorkManage.getPlanEndTime() != null) {
            String planEndTime = DateUtils.STANDARD_FORMAT_GENERAL.format(
                    iterativeWorkManage.getPlanEndTime()).substring(0, 10);
            planEndTime += " 23:59:59";
            iterativeWorkManageEdit.setPlanEndTime(DateUtils.STANDARD_FORMAT_GENERAL.parse(planEndTime));
        } else {
            iterativeWorkManageEdit.setPlanEndTime(null);
        }
        iterativeWorkManageEdit.setCodeAmount(iterativeWorkManage.getCodeAmount());
        iterativeWorkManageEdit.setPersonLiable(iterativeWorkManage.getPersonLiable());
        iterativeWorkManageEdit.setFinalimit(iterativeWorkManage.getFinalimit());
        iterativeWorkManageEdit.setCheckResult("");
        // if (iterativeWorkManage.getPersonLiable() == null ||
        // "".equals(iterativeWorkManage.getPersonLiable())) {
        // return "error";
        // }
        // if (iterativeWorkManage.getPersonLiable().split(",").length == 1
        // &&
        // iterativeWorkManage.getPersonLiable().equals(member.getZrAccount().replaceAll("^(0+)",
        // ""))) {
        // return "self";
        // }
        editDao.readyToCheck(iterativeWorkManageEdit);
        if (compareCheck(iterativeWorkManage.getProNo(), iterativeWorkManage.getId()).size() == 0) {
            editDao.deleteEdit(iterativeWorkManage.getId());
            // 责任人直接修改原表
            editDao.personLiable(iterativeWorkManage.getPersonLiable(), iterativeWorkManage.getId());
            return "only";
        } else {
            if (iterativeWorkManage.getPersonLiable() == null || "".equals(iterativeWorkManage.getPersonLiable())) {
                editDao.deleteEdit(iterativeWorkManage.getId());
                return "error";
            } else if (iterativeWorkManage.getPersonLiable().split(",").length == 1
                       && iterativeWorkManage.getPersonLiable().equals(member.getZrAccount().replaceAll("^(0+)", ""))) {
                editDao.deleteEdit(iterativeWorkManage.getId());
                return "self";
            }
        }
        // 责任人直接修改原表
        editDao.personLiable(iterativeWorkManage.getPersonLiable(), iterativeWorkManage.getId());
        return "success";
    }

    public List<String> compareCheck(String proNo, String id) {
        IterativeWorkManage iterativeWorkManage = dao.openEditPage(id);
        IterativeWorkManageEdit iterativeWorkManageEdit = editDao.queryIterativeWorkManageEdit(id);
        // 两两对比，将值改变了的封装成日志记录，责任人不用审核
        List<String> report = new ArrayList<>();
        if (iterativeWorkManageEdit == null) {
            // report.add("没有待审核内容！");
            return report;
        }
        String creator = iterativeWorkManageEdit.getCreator();
        if (compareUtil(iterativeWorkManage.getTopic(), iterativeWorkManageEdit.getTopic())) {
            report.add(creator + " 将需求名称更新为 " + iterativeWorkManageEdit.getTopic() + ";");
        }
        if (compareUtil(iterativeWorkManage.getPrior(), iterativeWorkManageEdit.getPrior())) {
            report.add(creator + " 将优先级更新为 " + editDao.dataDictionary("work_prior", iterativeWorkManageEdit.getPrior())
                       + ";");
        }
        if (compareUtil(iterativeWorkManage.getImportance(), iterativeWorkManageEdit.getImportance())) {
            report.add(creator + " 将重要性更新为 " + editDao.dataDictionary("work_importance",
                                                                      iterativeWorkManageEdit.getImportance()) + ";");
        }
        if (compareUtil(iterativeWorkManage.getStatus(), iterativeWorkManageEdit.getStatus())) {
            report.add(creator + " 将状态更新为 " + editDao.dataDictionary("work_status", iterativeWorkManageEdit.getStatus())
                       + ";");
        }
        if (compareUtil(iterativeWorkManage.getChange(), iterativeWorkManageEdit.getChange())) {
            report.add(
                    creator + " 将工作量变更更新为 " + editDao.dataDictionary("work_change", iterativeWorkManageEdit.getChange())
                    + ";");
        }
        if (iterativeWorkManage.getFinalimit() != iterativeWorkManageEdit.getFinalimit()) {
            report.add(creator + " 将完成度更新为 " + iterativeWorkManageEdit.getFinalimit() + ";");
        }
        if (compareUtil(iterativeWorkManage.getIteId(), iterativeWorkManageEdit.getIteId())) {
            report.add(creator + " 将迭代更新为 " + dao.getIteNameById(proNo, iterativeWorkManageEdit.getIteId()) + ";");
        }
        if (compareUtil(iterativeWorkManage.getStartTime(), iterativeWorkManageEdit.getStartTime())) {
            report.add(creator + " 将实际开始日期更新为 " + DateUtils.STANDARD_FORMAT_GENERAL.format(
                    iterativeWorkManageEdit.getStartTime()) + ";");
        }
        if (compareUtil(iterativeWorkManage.getEndTime(), iterativeWorkManageEdit.getEndTime())) {
            report.add(creator + " 将实际完成日期更新为 " + DateUtils.STANDARD_FORMAT_GENERAL.format(
                    iterativeWorkManageEdit.getEndTime()) + ";");
        }
        if (compareUtil(iterativeWorkManage.getDescribeInfo(), iterativeWorkManageEdit.getDescribeInfo())) {
            report.add(creator + " 将工作描述更新为 " + iterativeWorkManageEdit.getDescribeInfo() + ";");
        }
        if (compareUtil(iterativeWorkManage.getExpectHours(), iterativeWorkManageEdit.getExpectHours())) {
            report.add(creator + " 将预计工作量更新为 " + iterativeWorkManageEdit.getExpectHours() + ";");
        }
        if (compareUtil(iterativeWorkManage.getPlanStartTime(), iterativeWorkManageEdit.getPlanStartTime())) {
            report.add(creator + " 将计划启动日期更新为 " + DateUtils.STANDARD_FORMAT_GENERAL.format(
                    iterativeWorkManageEdit.getPlanStartTime()) + ";");
        }
        if (compareUtil(iterativeWorkManage.getPlanEndTime(), iterativeWorkManageEdit.getPlanEndTime())) {
            report.add(creator + " 将计划完成日期更新为 " + DateUtils.STANDARD_FORMAT_GENERAL.format(
                    iterativeWorkManageEdit.getPlanEndTime()) + ";");
        }
        if (compareUtil(iterativeWorkManage.getCodeAmount(), iterativeWorkManageEdit.getCodeAmount())) {
            report.add(creator + " 将预估代码量更新为 " + iterativeWorkManageEdit.getCodeAmount() + ";");
        }
        if (iterativeWorkManageEdit.getIsDeleted() != 0) {
            report.add(creator + " 删除了此需求;");
        }
        // if (report.size() == 0) {
        // report.add("没有待审核内容！");
        // }
        return report;
    }

    private boolean compareUtil(Object oldObj, Object newObj) {
        if (oldObj != null && !oldObj.equals(newObj)) {
            return true;
        } else if (oldObj == null && newObj != null) {
            return true;
        }
        return false;
    }

    /**
     * 审核
     */
    public void checkResult(String result, String id, String person, String no) {
        if ("refuse".equals(result)) {
            editDao.checkResult(result, id);
        } else if ("pass".equals(result)) {
            IterativeWorkManageEdit edit = editDao.queryIterativeWorkManageEdit(id);
            String personLiable = edit.getPersonLiable();
            String checkResult = edit.getCheckResult();
            if (personLiable != null && !"refuse".equals(checkResult)) {
                String zrAccount = editDao.members(no, null, person).get(0).getZrAccount().replaceAll("^(0+)", "");
                if (checkResult.indexOf(zrAccount) == -1) {
                    checkResult += "".equals(checkResult) ? zrAccount : "," + zrAccount;
                    if (checkResult.length() != personLiable.length()) {
                        result = checkResult;
                    }
                    editDao.checkResult(result, id);
                }
            }
            edit = editDao.queryIterativeWorkManageEdit(id);
            if ("pass".equals(edit.getCheckResult())) {
                editDao.editIterativeWorkManage(edit);
            }
        }
    }

    /**
     * 撤销审核
     */
    public void revocation(String id) {
        editDao.deleteEdit(id);
    }

    /**
     * @throws Exception
     * @Description: 删除迭代管理工作内容 @param 参数 @return void 返回类型 @throws
     */
    public Map<String, Object> deleteIteWorkManageInfo(String[] ids) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int succeed = 0;
        int failed = 0;
        String hwAccount = ids[ids.length - 1];
        for (int i = 0; i < ids.length - 1; i++) {
            String id = ids[i];
            IterativeWorkManage iterativeWorkManage = dao.openEditPage(id);
            iterativeWorkManage.setIsDeleted(1);
            iterativeWorkManage.setCreator(hwAccount);
            String result = editIteWorkManageEdit(iterativeWorkManage);
            if (result.equals("success")) {
                succeed += 1;
            } else {
                failed += 1;
            }
            // int a = dao.deleteIteWorkManageInfo(id);
        }
        map.put("succeed", succeed);
        map.put("failed", failed);
        return map;
    }

    public Map<String, Object> delete(String id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int number = dao.deleteIteWorkManage(id);
        if (number != 0) {
            map.put("code", "success");
        } else {
            map.put("code", "fail");
        }

        return map;
    }

    /**
     * 项目组成员
     *
     * @param no
     * @param hwAccount
     * @return
     */
    public List<ProjectMembersLocal> members(String no, String hwAccount) {
        return editDao.members(no, null, hwAccount);
    }

    /**
     * @Description:导入需求管理工作内容 @param @param file @param @return 参数 @return
     * Map<String,Object> 返回类型 @throws
     */
    public Map<String, Object> importIteWorkInfo(MultipartFile file, String proNo, String hwAccount,
                                                 HttpServletRequest request) {
        int sucNum = 0;
        int repeatNum = 0;
        Map<String, Object> result = new HashMap<String, Object>();
        InputStream is = null;
        Workbook workbook = null;
        String topic = null;
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
            Map<String, String> priorMap = getDictMap("work_prior");
            Map<String, String> statusMap = getDictMap("work_status");
            Map<String, String> importanceMap = getDictMap("work_importance");
            Map<String, String> changeMap = getDictMap("work_change");
            // 迭代
            Map<String, String> iteListMap = getIterMap(proNo);
            Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
            for (int i = 1; i <= rowSize; i++) {
                row = sheet.getRow(i);
                topic = getCellFormatValue(ExcelUtils.getCell(map, row, "需求名称(必填)"));
                if (StringUtils.isEmpty(topic) || "需求1".equals(topic) || "需求2".equals(topic)) {
                    continue;
                }
                int isNum = dao.getTopicCountIteWorkManage(proNo, topic);
                IterativeWorkManage iteInfo = null;
                if (isNum < 1) {// 新加
                    iteInfo = new IterativeWorkManage();
                    iteInfo.setId(UUIDUtil.getNew());// 主键id
                    iteInfo.setCreator(userId);// 创建人
                    iteInfo.setProNo(proNo);// 项目编号
                    iteInfo.setIteType("1");// 工作类型
                    iteInfo.setWrField("1");// 领域
                    iteInfo.setCreateTime(new Date());// 创建时间
                    iteInfo.setIsDeleted(0);
                    iteInfo.setTopic(topic);
                } else {// 修改
                    repeatNum += 1;
                    continue;
                    // iteInfo = dao.openIteWorkManageInfo(proNo, topic);
                }
                // 字符串判空处理
                String prior = getCellFormatValue(ExcelUtils.getCell(map, row, "优先级"));
                String importance = getCellFormatValue(ExcelUtils.getCell(map, row, "重要程度"));
                String planStartTime = getCellValue(ExcelUtils.getCell(map, row, "计划启动时间(必填)"));
                String planEndTime = getCellValue(ExcelUtils.getCell(map, row, "计划完成时间(必填)"));
                String personLiable = getCellFormatValue(ExcelUtils.getCell(map, row, "责任人"));
                String iteName = getCellFormatValue(ExcelUtils.getCell(map, row, "迭代"));
                String status = getCellFormatValue(ExcelUtils.getCell(map, row, "状态"));
                String finalimit = getCellFormatValue(ExcelUtils.getCell(map, row, "完成度(%)"));
                String expectHours = getCellValue(ExcelUtils.getCell(map, row, "预计工作量(人天)(必填)"));
                String change = getCellValue(ExcelUtils.getCell(map, row, "需求变更"));
                String startTime = getCellValue(ExcelUtils.getCell(map, row, "实际启动时间"));
                String endTime = getCellValue(ExcelUtils.getCell(map, row, "实际完成时间"));
                String codeAmount = getCellValue(ExcelUtils.getCell(map, row, "预估代码量(KLOC)"));
                String describeInfo = getCellValue(ExcelUtils.getCell(map, row, "工作描述"));
                // 处理责任人显示
                List<String> valids = new ArrayList<>();
                if (StringUtils.isNotBlank(personLiable)) {
                    personLiable = personLiable.replace("，", ",");
                    Set<String> persons = CollectionUtilsLocal.splitToSet(personLiable);
                    String[] account = null;
                    List<String> accounts = new ArrayList<String>();
                    for (String person : persons) {
                        account = dao.getZrAccountByName(proNo, person);
                        if (account != null && account.length > 0) {
                            accounts.add(account[0].replaceAll("^(0+)", ""));
                        }
                    }
                    for (String zRaccount : accounts) {
                        if (StringUtils.isNotBlank(zRaccount)) {
                            valids.add(zRaccount.trim());
                        }
                    }
                }
                iteInfo.setPersonLiable(StringUtils.join(valids, ","));
                // 处理迭代显示
                if (StringUtils.isNotBlank(iteName)) {
                    String iteId = iteListMap.get(iteName);
                    if (StringUtils.isNotBlank(iteId)) {
                        iteInfo.setIteId(iteId);
                    }
                } else {
                    iteInfo.setIteId("");
                }
                iteInfo.setPrior(getValueByName(priorMap, StringUtils.isNotBlank(prior) ? prior : "低", "1"));// 优先级
                iteInfo.setImportance(
                        getValueByName(importanceMap, StringUtils.isNotBlank(importance) ? importance : "重要",
                                       "2"));// 重要性
                iteInfo.setStatus(
                        getValueByName(statusMap, StringUtils.isNotBlank(status) ? status : "需求分析", "1"));// 状态
                iteInfo.setChange(
                        getValueByName(changeMap, StringUtils.isNotBlank(change) ? change : "正常交付", "1"));// 需求变更
                expectHours = StringUtils.isNotBlank(expectHours) ? String.valueOf(
                        Double.valueOf(expectHours.trim())) : "0";
                iteInfo.setExpectHours(expectHours);
                iteInfo.setCodeAmount(StringUtils.isNotBlank(codeAmount) ? codeAmount.trim() : "");
                iteInfo.setDescribeInfo(StringUtils.isNotBlank(describeInfo) ? describeInfo.trim() : "");
                // 处理finalimit的小数点
                if (StringUtils.isNotBlank(finalimit)) {
                    if (finalimit.indexOf(".") > 0) {
                        finalimit = finalimit.replaceAll("0+?$", "");// 去掉多余的0
                        finalimit = finalimit.replaceAll("[.]$", "");// 如最后一位是.则去掉
                    }
                    iteInfo.setFinalimit(Integer.parseInt(finalimit));
                } else {
                    iteInfo.setFinalimit(0);// 百分比
                }
                iteInfo.setPlanStartTime(
                        StringUtils.isNotBlank(planStartTime) ? DateUtils.parseDate(DateUtils.STANDARD_FORMAT_GENERAL,
                                                                                    planStartTime.substring(0, 10)
                                                                                    + " 00:00:00") : null);
                iteInfo.setPlanEndTime(
                        StringUtils.isNotBlank(planEndTime) ? DateUtils.parseDate(DateUtils.STANDARD_FORMAT_GENERAL,
                                                                                  planEndTime.substring(0, 10)
                                                                                  + " 23:59:59") : null);
                iteInfo.setStartTime(
                        StringUtils.isNotBlank(startTime) ? DateUtils.parseDate(DateUtils.STANDARD_FORMAT_GENERAL,
                                                                                startTime.substring(0, 10)
                                                                                + " 00:00:00") : null);
                iteInfo.setEndTime(
                        StringUtils.isNotBlank(endTime) ? DateUtils.parseDate(DateUtils.STANDARD_FORMAT_GENERAL,
                                                                              endTime.substring(0, 10)
                                                                              + " 23:59:59") : null);
                iteInfo.setUpdateTime(new Date());// 更新时间
                int a = 0;
                if (isNum < 1) {
                    iteInfo.setActualHours(expectHours);
                    a = dao.addIteWorkManageInfo(iteInfo);
                } else {
                    if ("1".equals(iteInfo.getChange())) {
                        iteInfo.setActualHours(expectHours);
                    }
                    // a = dao.editIteWorkManageInfoByTopic(proNo, iteInfo);
                    iteInfo.setCreator(hwAccount);
                    iteInfo.setSolvePerson(hwAccount);
                    String mess = editIteWorkManageEdit(iteInfo);
                    if ("success".equals(mess)) {
                        a = 1;
                    }
                }
                if (a > 0) {
                    sucNum += 1;
                }
            }
        } catch (IOException e) {
            logger.error("read file failed!", e);
            result.put("STATUS", "FAILED");
            result.put("MESSAGE", "读取文件失败！");
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("read file failed!" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("read file failed!" + e.getMessage());
                }
            }
        }
        result.put("STATUS", "SUCCESS");
        result.put("sucNum", sucNum);
        result.put("repeatNum", repeatNum);
        return result;
    }

    /**
     * topic相同值存在个数
     *
     * @param topic
     * @return
     */
    public int topicCount(String proNo, String topic) {
        return dao.getTopicCountIteWorkManage(proNo, topic);
    }

    /**
     * @throws IOException
     * @Description:导出需求管理工作内容 @param @param file @param @return 参数 @return
     * Map<String,Object> 返回类型 @throws
     */
    public byte[] export(String proNo) throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/excel/demand-template.xlsx");
        Workbook wb = new XSSFWorkbook(is);
        Sheet sheet = wb.getSheetAt(0);
        /********************* 获取项目表格信息 ************************/
        List<IterativeWorkManage> iteInfos = dao.queryIteWorkManageAll(proNo);
        if (iteInfos == null || iteInfos.size() <= 0) {
            return returnWb2Byte(wb);
        }
        Map<String, String> statusMap = getDictMaps("work_status");
        Map<String, String> changeMap = getDictMaps("work_change");
        Map<String, String> priorMap = getDictMaps("work_prior");
        Map<String, String> importanceMap = getDictMaps("work_importance");
        Map<String, String> iteListMap = getIterMaps(proNo);
        Map<String, String> memberMap = new HashMap<>();
        List<ProjectMembersLocal> members = dao.getProjectMebers(proNo);
        if (members != null && !members.isEmpty()) {
            for (ProjectMembersLocal membersLocal : members) {
                if (StringUtils.isNotBlank(membersLocal.getZrAccount())) {
                    membersLocal.setZrAccount(membersLocal.getZrAccount().replaceAll("^(0+)", ""));
                }
            }
            memberMap = members.stream().collect(
                    Collectors.toMap(ProjectMembersLocal::getZrAccount, ProjectMembersLocal::getName));
        }
        Row row = null;
        for (int i = 0; i < iteInfos.size(); i++) {
            IterativeWorkManage iterative = iteInfos.get(i);
            row = sheet.getRow(i + 1);
            // 字符串判空处理
            String planStartTime =
                    iterative.getPlanStartTime() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                                       iterative.getPlanStartTime());
            String planEndTime =
                    iterative.getPlanEndTime() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                                     iterative.getPlanEndTime());
            String startTime =
                    iterative.getStartTime() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                                   iterative.getStartTime());
            String endTime =
                    iterative.getEndTime() == null ? null : DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL,
                                                                                 iterative.getEndTime());
            // 处理select和date的空值
            setCellValue(0, StringUtils.isNotBlank(iterative.getTopic()) ? iterative.getTopic() : "", row);
            String ptype = getValueByName(priorMap,
                                          StringUtils.isNotBlank(iterative.getPrior()) ? iterative.getPrior() : "1",
                                          "低");// 优先级
            setCellValue(1, ptype, row);
            String mtype = getValueByName(importanceMap, StringUtils.isNotBlank(
                    iterative.getImportance()) ? iterative.getImportance() : "3", "一般");
            setCellValue(2, mtype, row);
            setCellValue(3, planStartTime, row);
            setCellValue(4, planEndTime, row);
            // 处理责任人显示
            if (StringUtils.isNotBlank(iterative.getPersonLiable())) {
                Set<String> accounts = CollectionUtilsLocal.splitToSet(iterative.getPersonLiable());
                List<String> valids = new ArrayList<>();
                for (String account : accounts) {
                    String person = memberMap.get(account);
                    if (StringUtils.isNotBlank(person)) {
                        valids.add(person);
                    }
                }
                setCellValue(5, StringUtils.join(valids, ","), row);
            } else {
                setCellValue(5, "", row);
            }
            String itype = getValueByName(iteListMap,
                                          StringUtils.isNotBlank(iterative.getIteId()) ? iterative.getIteId() : "wfp",
                                          "未分配");
            setCellValue(6, itype, row);
            String stype = getValueByName(statusMap,
                                          StringUtils.isNotBlank(iterative.getStatus()) ? iterative.getStatus() : "1",
                                          "需求分析");
            setCellValue(7, stype, row);
            Integer finalimit = iterative.getFinalimit();// 百分比
            setCellValue(8, (finalimit >= 0 && finalimit <= 100) ? finalimit.toString() : "", row);
            setCellValue(9, StringUtils.isNotBlank(iterative.getExpectHours()) ? iterative.getExpectHours() : "", row);
            String ctype = getValueByName(changeMap,
                                          StringUtils.isNotBlank(iterative.getChange()) ? iterative.getChange() : "1",
                                          "正常交付");
            setCellValue(10, ctype, row);
            setCellValue(11, startTime, row);
            setCellValue(12, endTime, row);
            setCellValue(13, StringUtils.isNotBlank(iterative.getCodeAmount()) ? iterative.getCodeAmount() : "", row);
            setCellValue(14, StringUtils.isNotBlank(iterative.getDescribeInfo()) ? iterative.getDescribeInfo() : "",
                         row);
        }
        return returnWb2Byte(wb);
    }

    private String getValueByName(Map<String, String> map, String value, String defaultValue) {
        return map.containsKey(value) ? map.get(value) : defaultValue;
    }

    @Autowired
    DictionaryService dictionaryService;
    
    private List<DictEntryItemEntity> describeEntryItems(String entryCode) {
        DictEntryItemEntity entity = new DictEntryItemEntity();
        entity.setEntryCode(entryCode);

        return dictionaryService.listAllEntryItems(entity);
    }
    
    // 使用stream将list转成map(导入时name查val)
    private Map<String, String> getDictMap(String type) {

        Map<String, String> result = describeEntryItems(type).stream().collect(Collectors.toMap(DictEntryItemEntity::getKey,
                                                                                                DictEntryItemEntity::getValue));

        return MapUtils.isEmpty(result) ? new HashMap<>() : result;
    }

    // 使用stream将list转成map(导出时val查name)
    private Map<String, String> getDictMaps(String type) {

        Map<String, String> result = describeEntryItems(type).stream().collect(Collectors.toMap(DictEntryItemEntity::getValue,
                                                                                                DictEntryItemEntity::getKey));

        return MapUtils.isEmpty(result) ? new HashMap<>() : result;
    }

    // 封装迭代(导入时name查id)
    private Map<String, String> getIterMap(String proNo) {
        Map<String, String> result = null;
        try {
            List<IterationCycle> response = iterationCycleDao.getIteNameSelectByProNo(proNo);
            if (response != null && !response.isEmpty()) {
                result = response.stream().collect(Collectors.toMap(IterationCycle::getIteName, IterationCycle::getId));
            }
        } catch (Exception e) {
            logger.error("iterationCycleDao.getIteNameSelectByProNo excption, error: " + e.getMessage());
        }
        return (result == null || result.isEmpty()) ? new HashMap<>() : result;
    }

    // 封装迭代(导出时id查name)
    private Map<String, String> getIterMaps(String proNo) {
        Map<String, String> result = null;
        try {
            List<IterationCycle> response = iterationCycleDao.getIteNameSelectByProNo(proNo);
            if (response != null && !response.isEmpty()) {
                result = response.stream().collect(Collectors.toMap(IterationCycle::getId, IterationCycle::getIteName));
            }
        } catch (Exception e) {
            logger.error("iterationCycleDao.getIteNameSelectByProNo excption, error: " + e.getMessage());
        }
        return (result == null || result.isEmpty()) ? new HashMap<>() : result;
    }

    public void setCellValue(int i, String value, Row row) {
        Cell cell = row.getCell(i);
        cell.setCellValue(value);
    }

    /**
     * 表头样式
     *
     * @param wb
     * @return
     */
    public CellStyle getHeadStyle(Workbook wb) {
        // 表头样式
        CellStyle headerStyle = wb.createCellStyle();
        // 水平居中
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // 垂直居中
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 设置边框
        headerStyle.setBorderTop(CellStyle.BORDER_THIN);
        headerStyle.setBorderRight(CellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
        // 设置颜色
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
        // 垂直居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        // 设置边框
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        // 设置自动换行
        cellStyle.setWrapText(true);
        // 设置字体
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
                logger.error("export excel failed!", e);
            }
            return os.toByteArray();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    logger.error("read file failed!" + e.getMessage());
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("Output file failed!" + e.getMessage());
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

    /**
     * 需求管理编辑页回显
     *
     * @param id
     * @return
     */
    public IterativeWorkManage openEditPage(String id) throws Exception {
        IterativeWorkManage iterativeWorkManage = dao.openEditPage(id);
        IterativeWorkManageEdit iterativeWorkManageEdit = editDao.queryIterativeWorkManageEdit(id);
        if (iterativeWorkManageEdit != null) {
            if ("refuse".equals(iterativeWorkManageEdit.getCheckResult()) || "pass".equals(
                    iterativeWorkManageEdit.getCheckResult())) {
                iterativeWorkManage.setRemarks("finished");
            }
        } else {
            iterativeWorkManage.setRemarks("finished");
        }
        return iterativeWorkManage;
    }

    public List<Map<String, Object>> completion(String iteId, String proNo) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            List<Map<String, Object>> cycle = iterationCycleDao.completion(iteId);
            List<Map<String, Object>> list = getProjectMebersSelect(proNo);
            for (Map<String, Object> map : cycle) {
                Map<String, Object> resultMap = new HashMap<>();
                Object obj = map.get("id");
                String ids = obj == null ? "" : String.valueOf(obj);
                obj = map.get("topic");
                String topic = obj == null ? "" : String.valueOf(obj);
                obj = map.get("prior");
                String prior = obj == null ? "" : String.valueOf(obj);
                if ("1".equals(prior)) {
                    prior = "低";
                } else if ("2".equals(prior)) {
                    prior = "中";
                } else if ("3".equals(prior)) {
                    prior = "高";
                }
                obj = map.get("importance");
                String importance = obj == null ? "" : String.valueOf(obj);
                if ("1".equals(importance)) {
                    importance = "关键";
                } else if ("2".equals(importance)) {
                    importance = "重要";
                } else if ("3".equals(importance)) {
                    importance = "一般";
                } else if ("4".equals(importance)) {
                    importance = "提示";
                }
                obj = map.get("STATUS");
                String status1 = obj == null ? "" : String.valueOf(obj);
                if ("1".equals(status1)) {
                    status1 = "需求分析";
                } else if ("2".equals(status1)) {
                    status1 = "需求设计";
                } else if ("3".equals(status1)) {
                    status1 = "开发";
                } else if ("4".equals(status1)) {
                    status1 = "测试";
                } else if ("5".equals(status1)) {
                    status1 = "验收";
                } else if ("6".equals(status1)) {
                    status1 = "已关闭";
                }
                obj = map.get("finalimit");
                String finalimit = obj == null ? "0" : String.valueOf(obj);
                obj = map.get("person_liable");
                String person_liable = obj == null ? "" : String.valueOf(obj);
                List<String> result = Arrays.asList(person_liable.split(" |,"));
                person_liable = person_liable.replace(" ", "");
                String name2 = "";
                if (person_liable == null || "".equals(person_liable)) {
                    person_liable = "-";
                } else {
                    for (Map<String, Object> map2 : list) {
                        obj = map2.get("value");
                        String account = obj == null ? "" : String.valueOf(obj);
                        if (result.contains(account)) {
                            obj = map2.get("text");
                            String names = obj == null ? "" : String.valueOf(obj);
                            name2 += names + ",";
                        }
                    }
                    if (name2 != "") {
                        name2 = name2.substring(0, name2.length() - 1);
                    }
                }
                obj = map.get("display");
                String display = obj == null ? "0" : String.valueOf(obj);
                resultMap.put("ids", ids);
                resultMap.put("topic", topic);
                resultMap.put("prior", prior);
                resultMap.put("importance", importance);
                resultMap.put("status1", status1);
                resultMap.put("finalimit", finalimit);
                resultMap.put("person_liable", name2);
                resultMap.put("display", display);
                resultList.add(resultMap);
            }
        } catch (Exception e) {
            logger.error("export excel failed!", e);
        }
        return resultList;
    }

    public Map<String, List<String>> burnoutFigure(String iteId) {
        Map<String, List<String>> retmap = new HashMap<>();
        if (StringUtilsLocal.isBlank(iteId)) {
            return retmap;
        }
        IterationCycle cycle = iterationCycleDao.queryEditPageInfo(iteId);
        Integer total = dao.queryTotalCountByIteIdNot5(iteId);
        List<Map<String, Object>> iteTotalList = dao.queryIteTotalByEndTime(iteId);
        Map<String, Object> iteTotalMap = new HashMap<>();
        String day = "";
        if (iteTotalList != null) {
            for (Map<String, Object> map : iteTotalList) {
                day = StringUtilsLocal.valueOf(map.get("day"));
                if (!StringUtilsLocal.isBlank(day)) {
                    iteTotalMap.put(day, map.get("num"));
                }
            }
        }
        if (cycle.getStartDate().after(cycle.getEndDate())) {
            return retmap;
        }
        String startDate = DateUtils.format.format(cycle.getStartDate());
        String endDate = DateUtils.format.format(cycle.getEndDate());
        day = startDate;
        int totalnew = total;
        List<String> month = new ArrayList<>();
        List<String> shiji = new ArrayList<>();
        while (true) {
            month.add(day);
            Long num = (Long) iteTotalMap.get(day);
            if (num != null) {
                totalnew = totalnew - num.intValue();
            }
            shiji.add(totalnew + "");
            if (endDate.equals(day)) {
                break;
            }
            day = DateUtils.getSystemFewDay(day, 1);
        }
        retmap.put("month", month);
        retmap.put("value", shiji);
        List<Map<String, Object>> complete = iterationCycleDao.queryCompletionDegree(iteId);
        List<String> names = new ArrayList<>();
        List<String> finalimit = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        int a = 0;
        if (complete.size() > 4) {
            a = 4;
        } else {
            a = complete.size();
        }
        for (int i = 0; i < a; i++) {
            Object obj = complete.get(i).get("id");
            String id = obj == null ? "" : String.valueOf(obj);
            obj = complete.get(i).get("topic");
            String topic = obj == null ? "" : String.valueOf(obj);
            obj = complete.get(i).get("finalimit");
            String finalimit1 = obj == null ? "0" : String.valueOf(obj);
            ids.add(id);
            names.add(topic);
            finalimit.add(finalimit1);
        }
        retmap.put("ids", ids);
        retmap.put("names", names);
        retmap.put("finalimit", finalimit);
        return retmap;
    }

    public Map<String, Object> getStoryData(String iteId) {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtilsLocal.isBlank(iteId)) {
            return retMap;
        }
        IterationCycle cycle = iterationCycleDao.queryEditPageInfo(iteId);
        String endDate = DateUtils.format.format(cycle.getEndDate());
        Integer total = dao.queryTotalCountByIteIdNot5(iteId);
        retMap.put("total", total);
        Map<String, Object> map = dao.queryIteDaysByEndTime(iteId);
        Integer days = Integer.parseInt(StringUtilsLocal.valueOf(map.get("days")));
        Double num = StringUtilsLocal.parseDouble(map.get("num"));
        retMap.put("num", num);
        if (total == 0) {
            retMap.put("closedLoopRate", 0);
        } else {
            retMap.put("closedLoopRate", StringUtilsLocal.keepTwoDecimals(num / total));
        }
        if (num == 0) {
            retMap.put("days", 0);
        } else {
            retMap.put("days", StringUtilsLocal.keepTwoDecimals(days / num));
        }
        map = dao.queryIteOverDue(iteId);
        retMap.put("overDue", map.get("num"));
        return retMap;
    }

    public Map<String, Object> getBugData(String iteId) {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtilsLocal.isBlank(iteId)) {
            return retMap;
        }
        IterationCycle cycle = iterationCycleDao.queryEditPageInfo(iteId);
        String startTime = DateUtils.SHORT_FORMAT_GENERAL.format(cycle.getStartDate());
        String endTime = DateUtils.SHORT_FORMAT_GENERAL.format(cycle.getEndDate());
        Integer total = dtsTaskDao.dtsIterTotal(cycle.getProNo(), startTime, endTime);
        retMap.put("total", total);
        Map<String, Object> map = dtsTaskDao.dtsIterTotalNotClo(cycle.getProNo(), startTime, endTime);
        Integer days = Integer.parseInt(StringUtilsLocal.valueOf(map.get("days")));
        Double num = StringUtilsLocal.parseDouble(map.get("num"));
        retMap.put("num", total - num);
        if (total == 0) {
            retMap.put("closedLoopRate", 0);
        } else {
            retMap.put("closedLoopRate", StringUtilsLocal.keepTwoDecimals((total - num) / total));
        }
        if (num == 0) {
            retMap.put("days", 0);
        } else {
            retMap.put("days", StringUtilsLocal.keepTwoDecimals(days / num));
        }
        map = dtsTaskDao.dtsIterOverDueTotal(cycle.getProNo(), startTime, endTime);
        retMap.put("overDue", map.get("num"));
        return retMap;
    }

    public Map<String, Object> bugSubmission(String no) {
        Map<String, Object> retMap = new HashMap<>();
        Integer people = iterationCycleDao.bugSubmissionPeople(no);
        Integer Zr = iterationCycleDao.bugSubmissionZR(no);
        List<Map<String, Object>> list = iterationCycleDao.bugSubmission(no);
        Integer critical = 0;
        Integer major = 0;
        Integer minor = 0;
        Integer suggestion = 0;
        Integer total = 0;
        double number = 0.0;
        double closedLoopRate = 0.0;
        for (Map<String, Object> map : list) {
            critical = Integer.valueOf(String.valueOf(
                    (map.get("critical") == null || "".equals(map.get("critical"))) ? "0" : map.get("critical")));
            major = Integer.valueOf(
                    String.valueOf((map.get("major") == null || "".equals(map.get("major"))) ? "0" : map.get("major")));
            minor = Integer.valueOf(
                    String.valueOf((map.get("minor") == null || "".equals(map.get("minor"))) ? "0" : map.get("minor")));
            suggestion = Integer.valueOf(String.valueOf(
                    (map.get("suggestion") == null || "".equals(map.get("suggestion"))) ? "0" : map.get("suggestion")));
            total = Integer.valueOf(
                    String.valueOf((map.get("alls") == null || "".equals(map.get("alls"))) ? "0" : map.get("alls")));
            if (people == 0) {
                number = 0.0;
            } else {
                number = StringUtilsLocal.keepTwoDecimals((float) total / (float) people);
            }
            if (total == 0) {
                closedLoopRate = 0.0;
            } else {
                closedLoopRate = StringUtilsLocal.keepTwoDecimals((float) Zr / (float) total);
            }
        }
        retMap.put("critical", critical);
        retMap.put("major", major);
        retMap.put("minor", minor);
        retMap.put("suggestion", suggestion);
        retMap.put("sum", total);
        retMap.put("number", number);
        retMap.put("closedLoopRate", closedLoopRate);
        return retMap;
    }

    /**
     * 获取项目成员下拉列表
     *
     * @param proNo
     * @return
     */
    public List<Map<String, Object>> getProjectMebersSelect(String proNo) throws Exception {
        List<Map<String, Object>> list = dao.getProjectMebersSelect(proNo);
        if (null != list && list.size() > 0) {
            for (Map<String, Object> map : list) {
                map.put("value",
                        StringUtils.isNotBlank(StringUtilsLocal.valueOf(map.get("value"))) ? StringUtilsLocal.valueOf(
                                map.get("value")).replaceAll("^(0+)", "") : "");
                map.put("text", StringUtilsLocal.valueOf(map.get("text")));
            }
        }
        return list;
    }

    /**
     * 燃尽图保存显示状态
     *
     * @return
     */
    public int editCompletion(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            Object obj = map.get("ids");
            String id = (String) obj;
            obj = map.get("display");
            String display = (String) obj;
            iterationCycleDao.editCompletion(id, display);
        }
        return 0;
    }

    public void queryIteWorkManageAllNew(HttpServletRequest request, HttpServletResponse response) {
        List<IterativeWorkManage> list = dao.queryIteWorkManageAllNew();
        String[] title = { "项目名称", "需求/Story", "优先级", "状态", "迭代", "责任人", "工作量变更", "工作量(/人天)", "审核结果" };
        //excel文件名
        String fileName = "需求信息表" + System.currentTimeMillis() + ".xls";
        //需求sheet名
        String sheetName = "需求信息表";
        String[][] content = new String[list.size()][];
        Map<String, String> memberMap = new HashMap<>();
        List<ProjectMembersLocal> members = dao.getProjectMebers(null);
        if (members != null && !members.isEmpty()) {
            for (ProjectMembersLocal membersLocal : members) {
                if (StringUtils.isNotBlank(membersLocal.getZrAccount())) {
                    membersLocal.setZrAccount(membersLocal.getZrAccount().replaceAll("^(0+)", ""));
                }
                if (membersLocal.getName() == null) {
                    membersLocal.setName("");
                }
            }
            memberMap = members.stream().collect(
                    Collectors.toMap(ProjectMembersLocal::getZrAccount, ProjectMembersLocal::getName));
        }
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            IterativeWorkManage obj = list.get(i);
            content[i][0] = obj.getIteName();
            content[i][1] = obj.getTopic();
            content[i][2] = obj.getPrior();
            content[i][3] = obj.getStatus();
            content[i][4] = obj.getIteId();
            if (StringUtils.isNotBlank(obj.getPersonLiable())) {
                Set<String> accounts = CollectionUtilsLocal.splitToSet(obj.getPersonLiable());
                List<String> valids = new ArrayList<>();
                for (String account : accounts) {
                    String person = memberMap.get(account);
                    if (StringUtils.isNotBlank(person)) {
                        valids.add(person);
                    }
                }
                content[i][5] = StringUtils.join(valids, ",");
            } else {
                content[i][5] = null;
            }
            content[i][6] = obj.getChange();
            content[i][7] = obj.getExpectHours();
            content[i][8] = obj.getRemarks();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = getHSSFWorkbook(sheetName, title, content, null);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        CellStyle style1 = getCellStyle(wb);
        HSSFCell cell = null;
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style1);
            //            cell.setCellStyle(style);
        }

        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);

            }
        }
        return wb;
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public TableSplitResult queryAll(TableSplitResult page, String hwAccount, String sort, String sortOrder) {
        TableSplitResult data = new TableSplitResult();
        // 查询总记录条数
        Integer total = 0;
        //        hwAccount = "admin".equals(hwAccount) ? null : hwAccount;
        List<ProjectInfo> proNo = new ArrayList<ProjectInfo>();
        String no = StringUtilsLocal.valueOf(page.getQueryMap().get("no"));
        if(null != no && "" != no){
            ProjectInfo p = new ProjectInfo();
            p.setNo(no);
            proNo.add(p);
            total = dao.queryAllCount(proNo);
            List<IterativeWorkManage> dataList = new ArrayList<>();
            if (total > 0) {
                dataList = dao.queryAllIteWorkManage(page, proNo, sort, sortOrder);
                getPersonLiable(dataList, null);
            }
            data.setTotal(total);
            data.setRows(dataList);
            return data;
        }else{
            if (!"admin".equals(hwAccount)) {
                List<ProjectMembersLocal> list = editDao.queryAllMembers(null, hwAccount);
                ProjectMembersLocal projectMembers = (list != null && list.size() == 1) ? list.get(0) : null;
                proNo = dao.queryProNO(projectMembers.getZrAccount());
                if (proNo.size() == 0) {
                    ProjectInfo p = new ProjectInfo();
                    p.setNo("无效项目名称");
                    proNo.add(p);
                }
                total = dao.queryAllCount(proNo);
                List<IterativeWorkManage> dataList = new ArrayList<>();
                if (total > 0) {
                    dataList = dao.queryAllIteWorkManage(page, proNo, sort, sortOrder);
                    getPersonLiable(dataList, null);
                }
                data.setTotal(total);
                data.setRows(dataList);
                return data;
            } else {
                try {
                    total = dao.queryAllCount(proNo);
                } catch (Exception e) {
                    logger.error("dao.queryTotalCount exception, error: " + e.getMessage());
                }
                List<IterativeWorkManage> dataList = new ArrayList<>();
                if (total > 0) {
                    dataList = dao.queryAllIteWorkManage(page, proNo, sort, sortOrder);
                    getPersonLiable(dataList, null);
                }
                data.setTotal(total);
                data.setRows(dataList);
                return data;
            }
        }
    }

    public String queryIteName(String id){
        return dao.queryIteName(id);
    }
}
