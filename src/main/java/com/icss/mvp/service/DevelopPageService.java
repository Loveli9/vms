package com.icss.mvp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.icss.mvp.dao.project.IProjectDao;
import com.icss.mvp.entity.project.ProjectBaseEntity;
import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.IProjectManagersDao;
import com.icss.mvp.dao.ISvnTaskDao;
import com.icss.mvp.dao.statistics.IProgramingDao;
import com.icss.mvp.entity.GerenCode;
import com.icss.mvp.entity.MonthlyManpower;
import com.icss.mvp.entity.ProjectMember;
import com.icss.mvp.entity.capacity.AbilityEntity;
import com.icss.mvp.entity.capacity.WorkloadEntity;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.constant.FileTypeEnum;
import com.icss.mvp.constant.FileTypeEnum.FileType;

@Service
@SuppressWarnings("all")
public class DevelopPageService {

    @Resource
    private ISvnTaskDao   dao;

    @Resource
    IProjectManagersDao   projectManagersDao;

    @Autowired
    IProgramingDao        programingDao;
    
    @Autowired
    IProjectDao           projectDao;

    private static Logger logger = Logger.getLogger(DevelopPageService.class);
    
    public static String[] getLast12Months(){
		//Java获取最近12个月的月份
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String dateString = sdf.format(cal.getTime());
		List<String> rqList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			dateString = sdf.format(cal.getTime());
			rqList.add(dateString.substring(0, 7));
			cal.add(Calendar.MONTH, -1);
		}
		//存入string数组
		String[] months = {(String)rqList.get(11),(String)rqList.get(10),(String)rqList.get(9),(String)rqList.get(8),(String)rqList.get(7),(String)rqList.get(6),
				(String)rqList.get(5),(String)rqList.get(4),(String)rqList.get(3),(String)rqList.get(2),(String)rqList.get(1),(String)rqList.get(0)};
		
		return months;
	}
    
    public Map<String, Object> developSearchList(String no, String type, String codeType, String roleType) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	List<GerenCode> list = new ArrayList<GerenCode>();
		String[] months = DateUtils.getLatestMonths(12);
		result.put("head", months);
		result.put("body", list);
		if (!"all".equals(codeType)) {// 获取不同语言的文件后缀
            codeType = FileType.getFileTypes(codeType);
        }
        
        if (StringUtils.isBlank(roleType)) {
            logger.error("职位参数为空");
            return null;
        }
        List<Map<String, Object>> projd;
        if ("all".equals(roleType)) {
            projd = projectManagersDao.queryOMPUserSelected(no);
        } else {
            Map<String, Object> map = new HashMap<>();
            if (!setValue(map, "role", roleType)) {
                logger.error("职位参数序列化异常");
                return null;
            }
            map.put("no", no);
            projd = projectManagersDao.queryOMPUserSelectedDevelop(map);
        }

        if ("1".equals(type)) {// 根据message统计代码量
            return getCodeByMessageList(projd, no, codeType);
        }
        int firsts = 0, seconds = 0, thirds = 0, fourths = 0, fifths = 0, sixths = 0, sevenths = 0, eighths = 0, ninths = 0, tenths = 0, elevenths = 0, twelfths = 0, allSum = 0;
        for (Map<String, Object> map : projd) {
            GerenCode gerenCode = new GerenCode();
            String svnGitNo = StringUtilsLocal.valueOf(map.get("svn_git_no"));
            String author = StringUtilsLocal.valueOf(map.get("HW_ACCOUNT"));
            String name = StringUtilsLocal.valueOf(map.get("NAME"));
            String role = StringUtilsLocal.valueOf(map.get("ROLE"));
            gerenCode.setName(name);
            gerenCode.setNumber(author);
            gerenCode.setRole(role);

            // 一次性统计当前开发人员代码量
            List<Map<String, String>> codeMap = dao.getCodeNumByParam(no, svnGitNo, author, codeType);
            List<Map<String, Object>> listMap = new ArrayList<>();
            List<Map<String, String>> codeListMap = new ArrayList<>();
            List<Map<String, String>> codeListMaps = new ArrayList<>();
            // 获取代码分类
            FileTypeEnum file = new FileTypeEnum();
            List<Map<String, Object>> mapsList = FileTypeEnum.getAllTypesMap();
            // 获取提交次数、最后提交时间
            listMap = dao.searchNewSubMessage(no, svnGitNo, author);
            for (Map<String, Object> maps : mapsList) {
                for (Map.Entry<String, Object> entry : maps.entrySet()) {
                    try {
                        // 获取每种类型代码量(折算)
                        codeListMap = dao.getCodeNumByParamY(no, svnGitNo, author, entry.getValue().toString());
                        for (Map<String, String> mapl : codeListMap) {
                            mapl.put("codeType", entry.getKey());
                        }
                        codeListMaps.addAll(codeListMap);
                    } catch (Exception e) {
                        logger.error("查询个人代码详情失败" + e.getMessage());
                    }
                }
            }
            
            int sum = 0;
            for (int i = 0; i < months.length; i++) {
				String month = months[i];
				switch (i+1) {
				case 1:
					gerenCode.setFirst(setMonthValue(month, codeMap));
                    sum += gerenCode.getJanuary();
                    firsts += gerenCode.getFirst();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 2:
					gerenCode.setSecond(setMonthValue(month, codeMap));
                    sum += gerenCode.getSecond();
                    seconds += gerenCode.getSecond();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 3:
					gerenCode.setThird(setMonthValue(month, codeMap));
                    sum += gerenCode.getThird();
                    thirds += gerenCode.getThird();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 4:
					gerenCode.setFourth(setMonthValue(month, codeMap));
                    sum += gerenCode.getFourth();
                    fourths += gerenCode.getFourth();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 5:
					gerenCode.setFifth(setMonthValue(month, codeMap));
                    sum += gerenCode.getFifth();
                    fifths += gerenCode.getFifth();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 6:
					gerenCode.setSixth(setMonthValue(month, codeMap));
                    sum += gerenCode.getSixth();
                    sixths += gerenCode.getSixth();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 7:
					gerenCode.setSeventh(setMonthValue(month, codeMap));
                    sum += gerenCode.getSeventh();
                    sevenths += gerenCode.getSeventh();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 8:
					gerenCode.setEighth(setMonthValue(month, codeMap));
                    sum += gerenCode.getEighth();
                    eighths += gerenCode.getEighth();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;					
				case 9:
					gerenCode.setNinth(setMonthValue(month, codeMap));
                    sum += gerenCode.getNinth();
                    ninths += gerenCode.getNinth();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 10:
					gerenCode.setTenth(setMonthValue(month, codeMap));
                    sum += gerenCode.getTenth();
                    tenths += gerenCode.getTenth();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 11:
					gerenCode.setEleventh(setMonthValue(month, codeMap));
                    sum += gerenCode.getEleventh();
                    elevenths += gerenCode.getEleventh();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				case 12:
					gerenCode.setTwelfth(setMonthValue(month, codeMap));
                    sum += gerenCode.getTwelfth();
                    twelfths += gerenCode.getTwelfth();
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
					break;
				default:
					break;
				}
				gerenCode.setSum(sum);
            }
            allSum = firsts + seconds + thirds + fourths + fifths + sixths 
            		+ sevenths + eighths + ninths + tenths + elevenths + twelfths;
           gerenCode.setFirsts(firsts);
           gerenCode.setSeconds(seconds);
           gerenCode.setThirds(thirds);
           gerenCode.setFourths(fourths);
           gerenCode.setFifths(fifths);
           gerenCode.setSixths(sixths);
           gerenCode.setSevenths(sevenths);
           gerenCode.setEighths(eighths);
           gerenCode.setNinths(ninths);
           gerenCode.setTenths(tenths);
           gerenCode.setElevenths(elevenths);
           gerenCode.setTwelfths(twelfths);
           gerenCode.setSums(allSum);
           list.add(gerenCode);
        }
        
    	return result;
    }

    public List<GerenCode> developSearch(String no, String type, String codeType, String roleType) {
        if (!"all".equals(codeType)) {// 获取不同语言的文件后缀
            codeType = FileType.getFileTypes(codeType);
        }
        
        try {
  		  	roleType = URLDecoder.decode(roleType, "UTF-8");
	  	} catch (UnsupportedEncodingException e) {
	  		logger.error("URLDecoder.decode exception, error: "+e.getMessage());
	  	}
        
        List<GerenCode> list = new ArrayList<GerenCode>();
        if (StringUtils.isBlank(roleType)) {
            logger.error("职位参数为空");
            return list;
        }

        List<Map<String, Object>> projd;
        if ("all".equals(roleType)) {
            projd = projectManagersDao.queryOMPUserSelected(no);
        } else {
            Map<String, Object> map = new HashMap<>();
            if (!setValue(map, "role", roleType)) {
                logger.error("职位参数序列化异常");
                return list;
            }
            map.put("no", no);
            projd = projectManagersDao.queryOMPUserSelectedDevelop(map);
        }

        if ("1".equals(type)) {// 根据message统计代码量
            return getCodeByMessage(projd, no, codeType);
        }
        
        //最近12个月的月份
        String[] months = DevelopPageService.getLast12Months();
        
        int januarys = 0, februarys = 0, marchs = 0, aprils = 0, mays = 0, junes = 0, julys = 0, augusts = 0, septembers = 0, octobers = 0, novembers = 0, decembers = 0, allSum = 0;
        for (Map<String, Object> map : projd) {
            GerenCode gerenCode = new GerenCode();

            String svnGitNo = StringUtilsLocal.valueOf(map.get("svn_git_no"));
            String author = StringUtilsLocal.valueOf(map.get("HW_ACCOUNT"));
            String name = StringUtilsLocal.valueOf(map.get("NAME"));
            String role = StringUtilsLocal.valueOf(map.get("ROLE"));
            gerenCode.setName(name);
            gerenCode.setNumber(author);
            gerenCode.setRole(role);

            // 一次性统计当前开发人员代码量
            List<Map<String, String>> codeMap = dao.getCodeNumByCommitAndType(no, svnGitNo, author, codeType,months[0], months[11]);

            List<Map<String, Object>> listMap = new ArrayList<>();
            List<Map<String, String>> codeListMap = new ArrayList<>();
            List<Map<String, String>> codeListMaps = new ArrayList<>();
            // 获取代码分类
            List<Map<String, Object>> mapsList = FileTypeEnum.getAllTypesMap();
            // 获取提交次数、最后提交时间
            listMap = dao.searchSubMessage(no, svnGitNo, author,months[0], months[11]);
            for (Map<String, Object> maps : mapsList) {
                for (Map.Entry<String, Object> entry : maps.entrySet()) {
                    try {
                        // 获取每种类型代码量(折算)
                        codeListMap = dao.getCodeNumByCommitAndTypeY(no, svnGitNo, author, entry.getValue().toString(),months[0], months[11]);
                        for (Map<String, String> mapl : codeListMap) {
                            mapl.put("codeType", entry.getKey());
                        }
                        codeListMaps.addAll(codeListMap);
                    } catch (Exception e) {
                        logger.error("查询个人代码详情失败" + e.getMessage());
                    }
                }
            }
            
            int sum = 0;
            for (String month : months) {
    			if ((months[0]).equals(month)) {
    				gerenCode.setJanuary(setMonthValue(month, codeMap));
    				sum += gerenCode.getJanuary();
    				januarys += gerenCode.getJanuary();
    				gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
    			} else if ((months[1]).equals(month)) {
    				gerenCode.setFebruary(setMonthValue(month, codeMap));
    				sum += gerenCode.getFebruary();
    				februarys += gerenCode.getFebruary();
    				gerenCode.setMes2(setMessage(month, codeListMaps, listMap));
    			} else if ((months[2]).equals(month)) {
    				gerenCode.setMarch(setMonthValue(month, codeMap));
    				sum += gerenCode.getMarch();
    				marchs += gerenCode.getMarch();
    				gerenCode.setMes3(setMessage(month, codeListMaps, listMap));
    			} else if ((months[3]).equals(month)) {
    				gerenCode.setApril(setMonthValue(month, codeMap));
    				sum += gerenCode.getApril();
    				aprils += gerenCode.getApril();
    				gerenCode.setMes4(setMessage(month, codeListMaps, listMap));
    			} else if ((months[4]).equals(month)) {
    				gerenCode.setMay(setMonthValue(month, codeMap));
    				sum += gerenCode.getMay();
    				mays += gerenCode.getMay();
    				gerenCode.setMes5(setMessage(month, codeListMaps, listMap));
    			} else if ((months[5]).equals(month)) {
    				gerenCode.setJune(setMonthValue(month, codeMap));
    				sum += gerenCode.getJune();
    				junes += gerenCode.getJune();
    				gerenCode.setMes6(setMessage(month, codeListMaps, listMap));
    			} else if ((months[6]).equals(month)) {
    				gerenCode.setJuly(setMonthValue(month, codeMap));
    				sum += gerenCode.getJuly();
    				julys += gerenCode.getJuly();
    				gerenCode.setMes7(setMessage(month, codeListMaps, listMap));
    			} else if ((months[7]).equals(month)) {
    				gerenCode.setAugust(setMonthValue(month, codeMap));
    				sum += gerenCode.getAugust();
    				augusts += gerenCode.getAugust();
    				gerenCode.setMes8(setMessage(month, codeListMaps, listMap));
    			} else if ((months[8]).equals(month)) {
    				gerenCode.setSeptember(setMonthValue(month, codeMap));
    				sum += gerenCode.getSeptember();
    				septembers += gerenCode.getSeptember();
    				gerenCode.setMes9(setMessage(month, codeListMaps, listMap));
    			} else if ((months[9]).equals(month)) {
    				gerenCode.setOctober(setMonthValue(month, codeMap));
    				sum += gerenCode.getOctober();
    				octobers += gerenCode.getOctober();
    				gerenCode.setMes10(setMessage(month, codeListMaps, listMap));
    			} else if ((months[10]).equals(month)) {
    				gerenCode.setNovember(setMonthValue(month, codeMap));
    				sum += gerenCode.getNovember();
    				novembers += gerenCode.getNovember();
    				gerenCode.setMes11(setMessage(month, codeListMaps, listMap));
    			} else if ((months[11]).equals(month)) {
    				gerenCode.setDecember(setMonthValue(month, codeMap));
    				sum += gerenCode.getDecember();
    				decembers += gerenCode.getDecember();
    				gerenCode.setMes12(setMessage(month, codeListMaps, listMap));
    			}
                gerenCode.setSum(sum);
            }
            allSum = januarys + februarys + marchs + aprils + mays + junes + julys + augusts + septembers + octobers
                     + novembers + decembers;
            gerenCode.setJanuarys(januarys);
            gerenCode.setFebruarys(februarys);
            gerenCode.setMarchs(marchs);
            gerenCode.setAprils(aprils);
            gerenCode.setMays(mays);
            gerenCode.setJunes(junes);
            gerenCode.setJulys(julys);
            gerenCode.setAugusts(augusts);
            gerenCode.setSeptembers(septembers);
            gerenCode.setOctobers(octobers);
            gerenCode.setNovembers(novembers);
            gerenCode.setDecembers(decembers);
            gerenCode.setSums(allSum);
            list.add(gerenCode);
        }
        return list;
    }

    public List<WorkloadEntity> summarizeAmountMonthly(String projectId, String codeType, Date beginTime, Date endTime) {
        Set<String> types = new HashSet<>();
        if (StringUtils.isNotBlank(codeType) && !"all".equalsIgnoreCase(codeType)) {
            // 获取不同语言的文件后缀
            types = FileType.getFileTypeSet(codeType);
        }

        List<WorkloadEntity> response = summarizeAmount(projectId, beginTime, endTime, types);
        if (response == null || response.isEmpty()) {
            response = new ArrayList<>();
        }

        return response;
    }

    // Set<String> roles = CollectionUtilsLocal.splitToSet(roleType);

    // Map<String, Object> params = new HashMap<>();
    // params.put("no", projectId);
    // params.put("role",roles);
    // List<Map<String, Object>> members = projectManagersDao.queryOMPUserSelectedDevelop(params);

    // Map<String, AbilityEntity> entityMap = getStringAbilityMap(projectId, roles);
    // if (entityMap == null || entityMap.isEmpty()) {
    // return new ArrayList<>();
    // }
    // Set<String> members = new HashSet<>(entityMap.keySet());

    // List<AbilityEntity> members = getStringAbilities(projectId, roles);

    // Map<String, Double> languages = null;
    // try {
    // languages = programingDao.getLanguage().stream().
    // collect(Collectors.toMap(LanguageEntity::getFileType, LanguageEntity::getWeight));
    // } catch (Exception e) {
    // logger.error("programingDao.getLanguage exception, error: "+e.getMessage());
    // }

    // Map<String ,AbilityEntity> abilities = new HashMap<>();
    // if (response != null && !response.isEmpty()) {
    // for (WorkloadEntity workload : response) {
    // String fileType = workload.getType();
    // if (!languages.containsKey(fileType)) {
    // continue;
    // }
    //
    // double weight = languages.get(fileType);
    // if (weight == 0) {
    // continue;
    // }
    //
    // String author = workload.getAuthor();
    // String member = members.stream().filter(o -> o.equalsIgnoreCase(author)
    // || author.substring(1).equalsIgnoreCase(o)).findFirst().get();
    // if(StringUtils.isBlank(member)){
    // continue;
    // }
    //
    // AbilityEntity ability = entityMap.containsKey(member)?
    // entityMap.get(member):
    // new AbilityEntity(member);
    //
    // ability.getWorkload().add(workload);
    //
    // entityMap.put(member, ability);
    // }
    // }

    // return new ArrayList<>(entityMap.values());

    private List<WorkloadEntity> summarizeAmount(String projectId, Date beginTime, Date endTime, Set<String> types) {
        List<WorkloadEntity> result = null;
        try {
            Map<String, Date> scale = getTimescale(projectId, beginTime, endTime);
            if (!scale.isEmpty()) {
                String begin = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("StartTime"));
                String end = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("FinishTime"));

                result = programingDao.summarizeAmountMonthly(projectId, null, types, begin, end);
            }
        } catch (Exception e) {
            logger.error("programingDao.summarizeAmountMonthly exception, error: "+e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new ArrayList<>();
        }

        return result;
    }

    private Map<String, AbilityEntity> getStringAbilityMap(String projectId, Set<String> roles) {
        Map<String, AbilityEntity> result = null;
        try {
            List<AbilityEntity> entities = programingDao.getTeamMembers(projectId, roles);
            if (entities != null && !entities.isEmpty()) {
                // if (authorType == "1") {
                // result = entities.stream().collect(Collectors.toMap(AbilityEntity::getRelatedAccount,
                // Function.<AbilityEntity> identity()));
                // } else {
                // result = entities.stream().collect(Collectors.toMap(AbilityEntity::getAccount,
                // Function.<AbilityEntity> identity()));
                // }
                result = entities.stream().collect(Collectors.toMap(AbilityEntity::getRelatedAccount,
                                                                    Function.<AbilityEntity> identity()));
            }
        } catch (Exception e) {
            logger.error("programingDao.getTeamMembers exception, error: "+e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new HashMap<>();
        }

        return result;
    }

    private List<AbilityEntity> getStringAbilities(String projectId, Set<String> roles) {
        List<AbilityEntity> result = null;
        try {
            result = programingDao.getTeamMembers(projectId, roles);
        } catch (Exception e) {
            logger.error("programingDao.getTeamMembers exception, error: "+e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new ArrayList<>();
        }

        return result;
    }

    public String[] summarizeCommitMonthlyFormer(String projectId) {
        String[] result = new String[] { "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-" };

        List<Map<String, Object>> response = projectManagersDao.summarizeCommitMonthly(projectId);
        if (response != null && !response.isEmpty()) {
            for (Map<String, Object> commits : response) {
                int times = smoothParseInt(commits.get("times"));
                int month = smoothParseInt(commits.get("month"));
                if (times > 0 && month > 0) {
                    result[month - 1] = String.valueOf(times);
                }
            }
        }
        return result;
    }

    public List<WorkloadEntity> summarizeCommitMonthly(String projectId, Date beginTime, Date endTime) {
        List<WorkloadEntity> result = null;
        try {
            Map<String, Date> scale = getTimescale(projectId, beginTime, endTime);
            if (!scale.isEmpty()) {
                String begin = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("StartTime"));
                String end = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, scale.get("FinishTime"));

                result = programingDao.summarizeCommitMonthly(projectId, begin, end);
            }
        } catch (Exception e) {
            logger.error("programingDao.summarizeCommitMonthly exception, error: "+e.getMessage());
        }

        if (result == null || result.isEmpty()) {
            result = new ArrayList<>();
        }

        return result;
    }

    /**
     * 根据项目起止时间获取时间区间
     *
     * @param projectId 项目编号
     * @return
     */
    public Map<String, Date> getTimescale(String projectId, Date beginTime, Date endTime) {
        ProjectBaseEntity project = null;
        try {
            List<ProjectBaseEntity> projectList = projectDao.getProjectList(Collections.singleton(projectId));
            if (projectList != null && !projectList.isEmpty()) {
                project = projectList.get(0);
            }
        } catch (Exception e) {
            logger.error("projectDao.getProjectList exception, error: " + e.getMessage());
        }

        Date first = beginTime;
        Date firstNext = endTime;

        Map<String, Date> result = new HashMap<>();
        if (project != null) {
            Date start = project.getBeginDate();
            Date finish = project.getFinishDate();

            first = (start != null && start.after(beginTime)) ? start : beginTime;
            firstNext = (finish != null && finish.before(endTime)) ? finish : endTime;
        }

        if (first.before(firstNext)) {
            result.put("StartTime", first);
            result.put("FinishTime", firstNext);
        }

        return result;
    }

    private int smoothParseInt(Object input) {
        int result = 0;

        String value = input == null ? "" : String.valueOf(input);
        if (org.apache.commons.lang.StringUtils.isNotBlank(value)) {
            try {
                result = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("smoothParseInt Integer.parseInt error, input: " + input);
                result = 0;
            }
        }

        return result;
    }

    public Integer setMonthValue(String month, List<Map<String, String>> codeMap) {
        Double num = 0.0;
        if (codeMap != null) {
            for (Map<String, String> map : codeMap) {
                if (StringUtils.isNotEmpty(map.get("month")) && map.get("month").equals(month)) {
                    if (StringUtils.isNotEmpty(map.get("val"))) {
                        num = Double.valueOf(map.get("val"));
                    }
                }
            }
        }
        return num.intValue();
    }

    /**
     * 根据message获取个人代码量
     * 
     * @param projd
     * @return
     */
    public Map<String, Object> getCodeByMessageList(List<Map<String, Object>> projd, String no, String codeType) {
    	Map<String, Object> result = new HashMap<String, Object>();
		List<MonthlyManpower> body = new ArrayList<>();
		String[] months = DateUtils.getLatestMonths(12);
		result.put("head", months);
		result.put("body", body);
		
		return result;
    }
    
    /**
     * 根据message获取个人代码量
     * 
     * @param projd
     * @return
     */
    public List<GerenCode> getCodeByMessage(List<Map<String, Object>> projd, String no, String codeType) {
        // 获取所有信息
        List<GerenCode> li = new ArrayList<GerenCode>();

        //最近12个月的月份
        String[] months = DevelopPageService.getLast12Months();
        
        for (Map<String, Object> map : projd) {
            GerenCode gerenCode = new GerenCode();
            String svnGitNo = StringUtilsLocal.valueOf(map.get("svn_git_no"));
            String author = StringUtilsLocal.valueOf(map.get("HW_ACCOUNT"));

            String name = StringUtilsLocal.valueOf(map.get("NAME"));
            String role = StringUtilsLocal.valueOf(map.get("ROLE"));
            gerenCode.setName(name);
            gerenCode.setNumber(author);
            gerenCode.setRole(role);
            // 一次性统计当前开发人员代码量
            List<Map<String, String>> codeMap = dao.getCodeNumByMessageAndType(no, svnGitNo, author, codeType, months[0], months[11]);
            List<Map<String, Object>> listMap = new ArrayList<>();
            List<Map<String, String>> codeListMap = new ArrayList<>();
            List<Map<String, String>> codeListMaps = new ArrayList<>();
            // 获取代码分类
            List<Map<String, Object>> mapsList = FileTypeEnum.getAllTypesMap();
            // 获取提交次数、最后提交时间
            listMap = dao.searchSubMessage(no, svnGitNo, author, months[0], months[11]);
            for (Map<String, Object> maps : mapsList) {
                for (Map.Entry<String, Object> entry : maps.entrySet()) {
                    try {
                        // 获取每种类型代码量(折算)
                        codeListMap = dao.getCodeNumByMessageAndTypeY(no, svnGitNo, author, entry.getValue().toString(), months[0], months[11]);
                        for (Map<String, String> mapl : codeListMap) {
                            mapl.put("codeType", entry.getKey());
                        }
                        codeListMaps.addAll(codeListMap);
                    } catch (Exception e) {
                        logger.error("查询个人代码详情失败" + e.getMessage());
                    }
                }
            }
            for (String month : months) {
                int sum = 0;
                if (month.equals(months[0])) {
                    gerenCode.setJanuary(setMonthValue(month, codeMap));
                    gerenCode.setMes1(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[1])) {
                    gerenCode.setFebruary(setMonthValue(month, codeMap));
                    gerenCode.setMes2(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[2])) {
                    gerenCode.setMarch(setMonthValue(month, codeMap));
                    gerenCode.setMes3(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[3])) {
                    gerenCode.setApril(setMonthValue(month, codeMap));
                    gerenCode.setMes4(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[4])) {
                    gerenCode.setMay(setMonthValue(month, codeMap));
                    gerenCode.setMes5(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[5])) {
                    gerenCode.setJune(setMonthValue(month, codeMap));
                    gerenCode.setMes6(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[6])) {
                    gerenCode.setJuly(setMonthValue(month, codeMap));
                    gerenCode.setMes7(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[7])) {
                    gerenCode.setAugust(setMonthValue(month, codeMap));
                    gerenCode.setMes8(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[8])) {
                    gerenCode.setSeptember(setMonthValue(month, codeMap));
                    gerenCode.setMes9(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[9])) {
                    gerenCode.setOctober(setMonthValue(month, codeMap));
                    gerenCode.setMes10(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[10])) {
                    gerenCode.setNovember(setMonthValue(month, codeMap));
                    gerenCode.setMes11(setMessage(month, codeListMaps, listMap));
                } else if (month.equals(months[11])) {
                    gerenCode.setDecember(setMonthValue(month, codeMap));
                    gerenCode.setMes12(setMessage(month, codeListMaps, listMap));
                }
            }
            li.add(gerenCode);
        }
        return li;
    }

    /**
     * 代码详情数据封装
     * 
     * @param month
     * @param codeListMap
     * @param listMap
     * @return
     */
    private String setMessage(String month, List<Map<String, String>> codeListMap, List<Map<String, Object>> listMap) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer meString = new StringBuffer();
        for (Map<String, String> map : codeListMap) {
            if (map.get("month").equals(month)) {
                meString.append(map.get("codeType") + "代码量：" + String.valueOf(map.get("val")));
                meString.append("<br/>");
            }
        }
        for (Map<String, Object> map : listMap) {
            if (map.get("month").equals(month)) {
                try {
                    meString.append("提交次数：" + map.get("subCount"));
                    meString.append("<br/>");
                    meString.append("最后提交时间：" + format.format((map.get("lastSubTime"))));
                    meString.append("<br/>");
                } catch (Exception e) {
                    logger.error("日期格式转换异常：" + e.getMessage());
                }

            }
        }
        return meString.toString();
    }

    public void SortMembers(List<ProjectMember> members) {
        Collections.sort(members, new Comparator<ProjectMember>() {

            public int compare(ProjectMember arg0, ProjectMember arg1) {
                return arg1.getAuthor().compareTo(arg0.getAuthor());
            }
        });
    }

    private boolean setValue(Map<String, Object> map, String key, String value) {
        List<String> valueList = new ArrayList<>();
        if (value != null && !"".equals(value)) {
            String[] values = value.split(",");
            for (String value1 : values) {
                valueList.add(value1);
            }
        }
        map.put(key, valueList);
        if (valueList.size() == 0) {
            return false;
        }
        return true;
    }

}
