package com.icss.mvp.service;

import com.icss.mvp.dao.GeneralSituationDao;
import com.icss.mvp.dao.IBuOverviewDao;
import com.icss.mvp.dao.ICodeMasterInfoDao;
import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.IUserManagerDao;
import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.dao.SysHwdeptDao;
import com.icss.mvp.dao.TblAreaDao;
import com.icss.mvp.dao.TemaInfoVoDao;
import com.icss.mvp.entity.*;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@SuppressWarnings("all")
public class TeamInfoService {
    @Autowired
    private TemaInfoVoDao teamInfoVoDao;
    @Autowired
    private IBuOverviewDao buOverviewDao;
    @Resource
	private ICodeMasterInfoDao codeMasterInfo;
	@Resource
	IUserManagerDao userManagerDao;
	@Resource
	private OpDepartmentDao opDepartmentDao;
	@Resource
	private SysHwdeptDao SysHwdeptDao;
	@Resource
	private TblAreaDao TblAreaDao;
	
	@Resource
	private IProjectManagersDao projectManagersDao;
	
	@Resource
	private  GeneralSituationService generalSituationService;
	
	@Autowired
	private GeneralSituationDao generalSituationDao;

    private static final String NAME = "name";
    private static final String TM = "tm";
    /*public static final String AREA = "area";
    public static final String HW_DEPTS = "hwDepts";
    public static final String OPT_DEPTS = "optDepts";*/

    private final static Logger LOG = Logger.getLogger(ProjectListService.class);

    /*public List<TeamInfoVo> teamInfos(TeamInfo teamInfo,
                                            String username,
                                            PageInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
		setParamNew(teamInfo, username, map);
        Integer index = pageInfo.getCurrentPage();
        if (null != index){
            int count = teamInfoVoDao.teamInfoCount(map);
            pageInfo.setTotalRecord(count);
            int fromIndex = (index - 1) * pageInfo.getPageSize();
            map.put("fromIndex", fromIndex);
            map.put("pageSize", pageInfo.getPageSize());
        }
        List<TeamInfoVo> teamInfoVos = teamInfoVoDao.teamInfos(map);

        return teamInfoVos;
    }*/
    
    public PageResponse<ProjectTeamVo> teamInfos(String teamName,String tm, String username,PageRequest pageRequest){
		PageResponse<ProjectTeamVo> data = new PageResponse<>();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(pageRequest.getPageSize());
		pageInfo.setCurrentPage(pageRequest.getPageNumber()/pageRequest.getPageSize()+1);
		List<ProjectTeamVo> teamInfos=getTeamInfos(teamName,tm,username,pageInfo);
			
		data.setTotalCount(pageInfo.getTotalRecord());
		data.setData(teamInfos);	
		return data;
	}
    
    public List<ProjectTeamVo> getTeamInfos(String teamName,
    										String tm,
								            String username,
								            PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		Integer index = pageInfo.getCurrentPage();
		if (null != index){
		int count = teamInfoVoDao.teamCount(teamName,tm);
		pageInfo.setTotalRecord(count);
		int fromIndex = (index - 1) * pageInfo.getPageSize();
		map.put("fromIndex", fromIndex);
		map.put("pageSize", pageInfo.getPageSize());
		map.put("teamName", teamName);
		map.put("tm", tm);
		}
		List<ProjectTeamVo> teamInfos = teamInfoVoDao.getTeamInfos(map);
		for(ProjectTeamVo teamInfo : teamInfos){
			String teamId = teamInfo.getTeamId();
			int teamSize = teamInfoVoDao.getTeamSize(teamId);
			teamInfo.setTeamSize(teamSize);
		}
		return teamInfos;
	}
    
    /*public List<ProjectTeamVo> teamInfos(ProjectTeamVo projectTeamVo,
						            String username,
						            PageInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		setParamNew(projectTeamVo, username, map);
		Integer index = pageInfo.getCurrentPage();
		if (null != index){
		int count = teamInfoVoDao.teamInfoCount(map);
		pageInfo.setTotalRecord(count);
		int fromIndex = (index - 1) * pageInfo.getPageSize();
		map.put("fromIndex", fromIndex);
		map.put("pageSize", pageInfo.getPageSize());
		}
		List<ProjectTeamVo> teamInfos = new ArrayList<>();
		try {
			teamInfos = teamInfoVoDao.teamInfos(map);
			for(ProjectTeamVo teamInfo : teamInfos){
				String teamId = teamInfo.getTeamNo();
				int teamSize = teamInfoVoDao.getTeamSize(teamId);
				teamInfo.setTeamSize(teamSize);
			}
		} catch (Exception e) {
			logger.error("teamInfoVoDao.teamInfos exception, error: "+e.getMessage());
			LOG.error("团队查询失败", e);
		}
		return teamInfos;
    }*/
    
    public List<ProjectTeamVo> teamInfos(ProjectTeamVo projectTeamVo,
    		String username) {
    	Map<String, Object> map = new HashMap<>();
		setParamNew(projectTeamVo, username, map);
    	List<ProjectTeamVo> teamInfos = teamInfoVoDao.teamInfos(map);
    	
    	return teamInfos;
    }

    /**
     * 查询用户具有权限的团队编号列表
     * @param teamInfo
     * @param username
     * @return
     */
    public Set<String> teamNos(ProjectTeamVo projectTeamVo, String username) {
        Map<String, Object> map = new HashMap<>();
		setParamNew(projectTeamVo, username, map);
        
        return new HashSet<>(teamInfoVoDao.teamNos(map));
    }

	public void setParamNew(ProjectTeamVo projectTeamVo, String username, Map<String, Object> parameter){
		UserInfo userInfo = userManagerDao.getUserInfoByName(username);
		/*if("0".equals(projectTeam.getClientType())){//华为
			if (StringUtilsLocal.isBlank(projectTeam.getHwpdu())) {
				projectTeam.setHwpdu(userInfo.getHwpdu()==null?"0":userInfo.getHwpdu());
			}
			if (StringUtilsLocal.isBlank(projectTeam.getHwzpdu())) {
				projectTeam.setHwzpdu(userInfo.getHwzpdu()==null?"":userInfo.getHwzpdu());
			}
			if (StringUtilsLocal.isBlank(projectTeam.getPduSpdt())) {
				projectTeam.setPduSpdt(userInfo.getPduspdt()==null?"":userInfo.getPduspdt());
			}
		}else if ("1".equals(projectTeam.getClientType())) {//中软
			if (StringUtilsLocal.isBlank(projectTeam.getBu())) {
				projectTeam.setBu(userInfo.getBu()==null?"0":userInfo.getBu());
			}
			if (StringUtilsLocal.isBlank(projectTeam.getPdu())) {
				projectTeam.setPdu(userInfo.getDu()==null?"":userInfo.getDu());
			}
			if (StringUtilsLocal.isBlank(projectTeam.getDu())) {
				projectTeam.setDu(userInfo.getDept()==null?"":userInfo.getDept());
			}
		}*/
		/*parameter.put("client", projectTeam.getClientType());
		if("0".equals(projectTeam.getClientType())){//华为
			List<String> hwpduId = CollectionUtilsLocal.splitToList(projectTeam.getHwpdu());
			List<String> hwzpduId = CollectionUtilsLocal.splitToList(projectTeam.getHwzpdu());
			List<String> pduSpdtId = CollectionUtilsLocal.splitToList(projectTeam.getPduSpdt());
			parameter.put("hwpduId", hwpduId);
			parameter.put("hwzpduId", hwzpduId);
			parameter.put("pduSpdtId", pduSpdtId);
		}else if ("1".equals(projectTeam.getClientType())) {//中软
			List<String> buId = CollectionUtilsLocal.splitToList(projectTeam.getBu());
			List<String> pduId = CollectionUtilsLocal.splitToList(projectTeam.getPdu());
			List<String> duId = CollectionUtilsLocal.splitToList(projectTeam.getDu());
			parameter.put("buId", buId);
			parameter.put("pduId", pduId);
			parameter.put("duId", duId);
		}*/
		/*List<String> areas = CollectionUtilsLocal.splitToList(projectTeam.getArea());
		parameter.put("areas", areas);*/
		parameter.put("teamName", projectTeamVo.getTeamName());
		parameter.put("tm", projectTeamVo.getTm());
	}
    /**
     * 设置查询参数
     * @param teamInfo
     * @param username
     * @param map 参数集合
     */
    public void setParam(ProjectTeam projectTeam, String username, Map<String, Object> map){
        String auth = buOverviewDao.getAuthOpDepartment(username);
        Map<String, Object> authMap = AuthUtil.authMap(auth);
        if (StringUtils.isNotBlank(projectTeam.getTeamName())){
            map.put(NAME, projectTeam.getTeamName());
        }

        /*if (StringUtils.isNotBlank(teamInfo.getTm())){
            map.put(TM, teamInfo.getTm());
        }

        if (StringUtils.isBlank(teamInfo.getArea())){
            map.put(AREA, authMap.get(AuthUtil.AREA));
        }else {
            map.put(AREA, Arrays.asList(teamInfo.getArea().split(",")));
        }*/
        //添加中软部门编号
        /*deptParam(map,
                (Map<String, Object>) authMap.get(AuthUtil.OPT_DEPT),
                teamInfo.getBu(),
                teamInfo.getPdu(),
                teamInfo.getDu(),
                OPT_DEPTS);*/
        //添加华为部门编号
        /*deptParam(map,
                (Map<String, Object>) authMap.get(AuthUtil.HW_DEPT),
                teamInfo.getHwpdu(),
                teamInfo.getHwzpdu(),
                teamInfo.getPduSpdt(),
                HW_DEPTS);*/
    }

    /**
     * 根据参数(bu, pdu, du) 或 (hwpdu, hwzpdu, pduSpdt)初始化查询参数
     * @param paraMap
     * @param authMap 
     * @param bu
     * @param pdu
     * @param du
     * @param prefix paraMap的key
     */
    private void deptParam(Map<String, Object> paraMap,
                           Map<String, Object> authMap,
                           String bu,
                           String pdu,
                           String du,
                           String prefix){
        Set<String> set = new HashSet<>();
        if (StringUtils.isNotBlank(bu)
                && StringUtils.isNotBlank(pdu)
                && StringUtils.isNotBlank(du)){
            set.addAll(Arrays.asList(du.split(",")));
            paraMap.put(prefix, set);
            return;
        }

        if (StringUtils.isNotBlank(bu)
                && StringUtils.isNotBlank(pdu)
                && StringUtils.isBlank(du)){
            String[] bus = bu.split(",");
            for (String buNo : bus) {
                Map<String, List<String>> pduMap = (Map<String, List<String>>) authMap.get(buNo);
                if(pduMap==null) {
                	continue;
                }
                String[] pdus = pdu.split(",");
                for (String pduNo : pdus) {
                    set.addAll(pduMap.get(pduNo));
                }
            }
            paraMap.put(prefix, set);
            return;
        }

        if (StringUtils.isNotBlank(bu)
                && StringUtils.isBlank(pdu)
                && StringUtils.isBlank(du)){
            String[] bus = bu.split(",");
            for (String buNo : bus) {
                Map<String, List<String>> pduMap = (Map<String, List<String>>) authMap.get(buNo);
                if(pduMap==null) {
                	continue;
                }
                for (List<String> list : pduMap.values()) {
                    set.addAll(list);
                }
            }
            paraMap.put(prefix, set);
            return;
        }

        if (StringUtils.isBlank(bu)
                && StringUtils.isBlank(pdu)
                && StringUtils.isBlank(du)){
            for (String buNo : authMap.keySet()) {
                Map<String, List<String>> pduMap = (Map<String, List<String>>) authMap.get(buNo);
                if(pduMap==null) {
                	continue;
                }
                for (List<String> list : pduMap.values()) {
                    set.addAll(list);
                }

            }
            paraMap.put(prefix, set);
            return;
        }
    }

    public Map<String, Object> importTeam(MultipartFile file){
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
			
			// --get department and area id
			List<Map<String,Object>> opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("2",new HashSet<>());
			Map<String,Object> opDepartmentsBu = new HashMap<>();
			for (Map<String,Object> opDepartment: opDepartmentsList ) {
				opDepartmentsBu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")),opDepartment.get("dept_id"));
			}
			opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("3",new HashSet<>());
			Map<String,Object> opDepartmentsPdu = new HashMap<>();
			for (Map<String,Object> opDepartment: opDepartmentsList ) {
				opDepartmentsPdu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")),opDepartment.get("dept_id"));
			}
			opDepartmentsList = opDepartmentDao.getOpDepartmentBylevel("4",new HashSet<>());
			Map<String,Object> opDepartmentsDu = new HashMap<>();
			for (Map<String,Object> opDepartment: opDepartmentsList ) {
				opDepartmentsDu.put(StringUtilsLocal.valueOf(opDepartment.get("dept_name")),opDepartment.get("dept_id"));
			}
			List<Map<String,Object>> SysHwdeptList= SysHwdeptDao.getSysHwdeptBylevel("1",new HashSet<>());
			Map<String,Object> SysHwdeptsPdu  = new HashMap<>();
			for (Map<String,Object> SysHwdept: SysHwdeptList ) {
				SysHwdeptsPdu.put(StringUtilsLocal.valueOf(SysHwdept.get("dept_name")),SysHwdept.get("dept_id").toString());
			}
			SysHwdeptList = SysHwdeptDao.getSysHwdeptBylevel("2",new HashSet<>());
			Map<String,Object> SysHwdeptsZpdu = new HashMap<>();
			for (Map<String,Object> SysHwdept: SysHwdeptList ) {
				SysHwdeptsZpdu.put(StringUtilsLocal.valueOf(SysHwdept.get("dept_name")),SysHwdept.get("dept_id").toString());
			}
			SysHwdeptList = SysHwdeptDao.getSysHwdeptBylevel("3",new HashSet<>());
			Map<String,Object> SysHwdeptsPduSpdt = new HashMap<>();
			for (Map<String,Object> SysHwdept: SysHwdeptList ) {
				SysHwdeptsPduSpdt.put(StringUtilsLocal.valueOf(SysHwdept.get("dept_name")),SysHwdept.get("dept_id").toString());
			}
			List<TblArea> TblAreaList = TblAreaDao.getAllTblArea();
			Map<String,Object> TblArea = new HashMap<>();
			for (TblArea TblAreas: TblAreaList ) {
				TblArea.put(TblAreas.getAreaName(),TblAreas.getAreaCode());
			}
			// --end
			
			for (int i = 1; i <= rowSize; i++) {
				row = sheet.getRow(i);
				TeamInfoVo teamInfoVo = new TeamInfoVo();
				String cell01Value = getCellFormatValue(ExcelUtils.getCell(map, row, "团队名称"));
				String no = getCellFormatValue(ExcelUtils.getCell(map, row, "团队编码"));
				if (cell01Value == null || no == null || rowSize == 0) {
					result.put("STATUS", "FAILED");
					result.put("MESSAGE", "没有要导入的数据！");
					return result;
				}

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				teamInfoVo.setName(cell01Value);
				teamInfoVo.setNo(no);
				teamInfoVo.setTm(getCellFormatValue(ExcelUtils.getCell(map, row, "团队经理")));
				teamInfoVo.setTmid(getCellFormatValue(ExcelUtils.getCell(map, row, "团队经理工号")));
				teamInfoVo.setCooptype(getCellFormatValue(ExcelUtils.getCell(map, row, "合作类型")));
				teamInfoVo.setHwpdu(getCellFormatValue(ExcelUtils.getCell(map, row, "华为产品线")));
				teamInfoVo.setHwzpdu(getCellFormatValue(ExcelUtils.getCell(map, row, "子产品线")));
				teamInfoVo.setPduSpdt(getCellFormatValue(ExcelUtils.getCell(map, row, "PDU/SPDT")));
				teamInfoVo.setBu(getCellFormatValue(ExcelUtils.getCell(map, row, "业务线")));
				teamInfoVo.setPdu(getCellFormatValue(ExcelUtils.getCell(map, row, "事业部")));
				teamInfoVo.setDu(getCellFormatValue(ExcelUtils.getCell(map, row, "交付部")));
				teamInfoVo.setArea(isArea(getCellFormatValue(ExcelUtils.getCell(map, row, "地域"))));
				String date = df.format(new Date());
				Date imptDate = null;
				try {
					imptDate = df.parse(date);
				} catch (ParseException e) {
					LOG.error("时间转换异常", e);
				}
				teamInfoVo.setImportDate(imptDate);
				teamInfoVo.setImportuser("admin");
				teamInfoVo.setHwpduid((String) SysHwdeptsPdu.get(teamInfoVo.getHwpdu()));
				teamInfoVo.setHwzpduid((String) SysHwdeptsZpdu.get(teamInfoVo.getHwzpdu()));
				teamInfoVo.setPduSpdtid((String) SysHwdeptsPduSpdt.get(teamInfoVo.getPduSpdt()));
				teamInfoVo.setBuid((String) opDepartmentsBu.get(teamInfoVo.getBu()));
				teamInfoVo.setPduid((String) opDepartmentsPdu.get(teamInfoVo.getPdu()));
				teamInfoVo.setDuid((String) opDepartmentsDu.get(teamInfoVo.getDu()));
				teamInfoVo.setAreaid((String) TblArea.get(teamInfoVo.getArea()));
				
				teamInfoVoDao.replaceInfo(teamInfoVo);				
				LOG.info(teamInfoVo.getName() + "导入成功");
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
     * excel导入团队成员
     * @param file
     * @param teamId
     * @param request
     * @return
     */
	public Map<String, Object> importTeamMembers(MultipartFile file, String teamId, HttpServletRequest request) {
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
			Map<String, Integer> map = ExcelUtils.getMapName2Num(sheet, 1);
			List<String> list = new ArrayList<>();
			List<String> rankList = teamInfoVoDao.getRankList();
			List<String> statusList = Arrays.asList("在岗", "后备", "离职");
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
			list.add("PL");
			list.add("认证工程师");
			for (int i = 1; i <= rowSize; i++) {
				try {
					row = sheet.getRow(i);
					String account = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "中软工号")))
							? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "中软工号")).split("\\.")[0])
							: null;
					String memberName = StringUtilsLocal
							.clearSpaceAndLine(getCellFormatValue(ExcelUtils.getCell(map, row, "成员姓名")));
					String huaweiAccount = StringUtils
							.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "华为工号")))
									? StringUtilsLocal.clearSpaceAndLine(
                            getCellFormatValue(ExcelUtils.getCell(map, row, "华为工号")).split("\\.")[0])
									: null;
					String svnAccount = StringUtils
							.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "SVN/GIT帐号")))
									? getCellFormatValue(ExcelUtils.getCell(map, row, "SVN/GIT帐号")).split("\\.")[0]
									: null;
					String station = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "岗位")))
							? StringUtilsLocal.clearSpaceAndLine(getCellFormatValue(ExcelUtils.getCell(map, row, "岗位")))
									.toUpperCase()
							: null;
					String status = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "状态")))
							? StringUtilsLocal.clearSpaceAndLine(getCellFormatValue(ExcelUtils.getCell(map, row, "状态")))
							: null;
					String rank = StringUtils.isNotBlank(getCellFormatValue(ExcelUtils.getCell(map, row, "职级")))
							? StringUtilsLocal.clearSpaceAndLine(getCellFormatValue(ExcelUtils.getCell(map, row, "职级")))
									.toUpperCase()
							: null;
					if (StringUtils.isBlank(account)) {
						result.put("account", "empty");
						break;
					}

					if (StringUtils.isNotBlank(account) && account.length() > 10) {
						result.put("chinasoftAccountSize", account.length());
					}
					if (StringUtils.isBlank(memberName)) {
						result.put("memberName", "empty");
						break;
					}
					if (StringUtils.isBlank(huaweiAccount)) {
						result.put("huaweiAccount", "empty");
						break;
					}
					if (StringUtils.isNotBlank(station) && !list.contains(station)) {
						result.put("station", "flase");
						break;
					}
					status = StringUtils.isNotBlank(status) && "在职".equals(status) ? "在岗" : status;
					if (StringUtils.isNotBlank(status) && !statusList.contains(status)) {
						result.put("status", "flase");
						break;
					}
					if (StringUtils.isNotBlank(rank) && !rankList.contains(rank)) {
						result.put("rank", "flase");
						break;
					}

					if (StringUtils.isNotBlank(account)
							&& !Pattern.compile("^[-\\+]?[\\d]*$").matcher(account).matches()) {
						result.put("account", "false");
						break;
					}

					if (StringUtils.isNotBlank(huaweiAccount)
							&& !Pattern.compile("^[A-Za-z0-9\\s]+$").matcher(huaweiAccount).matches()) {
						result.put("huaweiAccount", "false");
						break;
					}

					if (StringUtils.isNotBlank(memberName)
							&& !Pattern.compile("^[\\u4e00-\\u9fa5\\s]+$").matcher(memberName).matches()) {
						result.put("memberName", "false");
						break;
					}

					String chinasoftAccount = StringUtilsLocal.zeroFill(account, 10);
					TeamMembers teamMembers = new TeamMembers();
					Map<String, Object> rankMap = generalSituationService.teamMemberEcho(teamId, chinasoftAccount);

					teamMembers.setName(memberName);
					teamMembers.setHwAccount(huaweiAccount);
					teamMembers.setSvnGitNo(StringUtils.isNotBlank(svnAccount) ? svnAccount.replaceAll(" ", "")
							: null != rankMap ? StringUtilsLocal.valueOf(rankMap.get("SVN_GIT_NO")) : "");
					teamMembers.setRole(StringUtils.isNotBlank(station) ? station
							: null != rankMap ? StringUtilsLocal.valueOf(rankMap.get("ROLE")) : "");
					teamMembers.setTeamId(teamId);
					teamMembers.setZrAccount(chinasoftAccount);
					teamMembers.setStatus(StringUtils.isNotBlank(status) ? status
							: null != rankMap ? StringUtilsLocal.valueOf(rankMap.get("STATUS")) : "");
					teamMembers.setRank(StringUtils.isNotBlank(rank) ? rank
							: null != rankMap ? StringUtilsLocal.valueOf(rankMap.get("RANK")) : "");

					int memberBaseCount = teamInfoVoDao.getMemberBaseCount(chinasoftAccount);
					int teamStaffCount = teamInfoVoDao.getTeamStaffCount(teamId, chinasoftAccount);
					int a = 0;
					int b = 0;

					if (memberBaseCount == 0) {
						a = projectManagersDao.addTeamMemberToMemberBase(teamMembers);
					} else {
						a = projectManagersDao.updateTeamMemberToMemberBase(teamMembers);
					}

					if (teamStaffCount == 0) {
						b = projectManagersDao.addTeamMemberToTeamStaff(teamMembers);
					} else {
						b = projectManagersDao.updateTeamMemberToTeamStaff(teamMembers);
					}

					generalSituationDao.alterRankOfProjectStaff(teamMembers.getStatus(), teamMembers.getRank(),
							teamMembers.getZrAccount(), "two");
					generalSituationDao.alterRankOfTeamStaff(teamMembers.getStatus(), teamMembers.getRank(),
							teamMembers.getZrAccount(), "two");
					
					if (a + b > 1) {
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
		return result;
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
			BigDecimal big = new BigDecimal(cell.getNumericCellValue());
			strCell = big.toPlainString();
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
    
    public TeamInfoVo getTeamInfoVo(String buName, String teamNo) {
		return teamInfoVoDao.getTeamInfoVo(buName, teamNo);
	}
    
    public Map<String, Object> getTeamOverveiwData(String no) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	if(StringUtils.isBlank(no)){
			LOG.error("parameter illegal");
			return result;
		}
		TeamInfoVo teamInfoVo = teamInfoVoDao.getTeamInfoVo(null, no);
		result.put("no", teamInfoVo.getNo());
		result.put("name", teamInfoVo.getName());
		result.put("bu", teamInfoVo.getBu());
		result.put("buid", teamInfoVo.getBuid());
		result.put("pdu", teamInfoVo.getPdu());
		result.put("pduid", teamInfoVo.getPduid());
		result.put("du", teamInfoVo.getDu());
		result.put("duid", teamInfoVo.getDuid());
		result.put("area", teamInfoVo.getArea());
		result.put("areaid", teamInfoVo.getAreaid());
		result.put("import_date", teamInfoVo.getImportDate());
		result.put("tm", teamInfoVo.getTm());
		result.put("tmid", teamInfoVo.getTmid());
		result.put("hwpdu", teamInfoVo.getHwpdu());
		result.put("hwpduid", teamInfoVo.getHwpduid());
		result.put("hwzpdu", teamInfoVo.getHwzpdu());
		result.put("hwzpduid", teamInfoVo.getHwzpduid());
		result.put("pduSpdt", teamInfoVo.getPduSpdt());
		result.put("pduSpdtid", teamInfoVo.getPduSpdtid());
		result.put("coopType", teamInfoVo.getCooptype());

		return result;
	}
    
    /**
     * 查询团队信息
     * @param teamNo
     * @return
     */
    public List<ProjectInfo> getTeamInfo(String teamId) {
    	if(StringUtils.isBlank(teamId)){
			LOG.error("parameter illegal");
			return new ArrayList<>();
		}
		List<ProjectInfo> projectInfo = new ArrayList<>();
		try {
			projectInfo = teamInfoVoDao.getTeamInfo(teamId);
		} catch (Exception e) {
			LOG.error("查询团队信息失败", e);
		}
		return projectInfo;
	}
}
