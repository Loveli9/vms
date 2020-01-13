package com.icss.mvp.service;

import java.util.*;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.IPersonalBulidTimeDao;
import com.icss.mvp.dao.VersionStrucDao;
import com.icss.mvp.entity.PersonalBulidTime;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.util.LocalDateUtils;

@Service
public class PersonalBulidTimeService {

    private final static Logger LOG = Logger.getLogger(PersonalBulidTimeService.class);

    @Autowired private IPersonalBulidTimeDao personalBulidTimeDao;

    @Autowired private VersionStrucDao vDao;

    /**
     * 最先查询当月已经采集的构建信息条数
     *
     * @return
     */
    public Integer queryNowBulid(String month, String projNo) {
        return personalBulidTimeDao.queryNowBulid(month, projNo);
    }

    /**
     * 先删除当月已经保存过的构建信息
     *
     * @return
     */
    public void deleteNowBulid(String month, String projNo) {
        personalBulidTimeDao.deleteNowBulid(month, projNo);
    }

    /**
     * 重新保存构建信息，确保刷新
     *
     * @return
     */
    public void insertBulid(List<PersonalBulidTime> listInsert) {
        try {
            personalBulidTimeDao.insertBulid(listInsert);
        } catch (Exception e) {
            // TODO: handle exception
            LOG.info(e);
        }
    }

    /**
     * 查询每个月的构建信息
     *
     * @return
     */
    public List<Map<String, Object>> buildPerMonth(String month, String projNo) {
		return personalBulidTimeDao.buildPerMonth(month, projNo);
	}

	/**
	 * 获得项目的所有构建条数
	 * 
	 * @return
	 */
	public Integer queryAllBuilds(String projNo) {
		return personalBulidTimeDao.queryAllBuilds(projNo);
	}

	/**
	 * 配置新smartIDE
	 * 
	 * @return
	 */
	public String savePbiId(String projNo, String pbiId, String pbiName) {
		personalBulidTimeDao.deletePbiId(projNo);
		if (pbiId == null) {
			pbiId = "";
		}
		if (pbiName == null) {
			pbiName = "";
		}
		try {
			personalBulidTimeDao.savePbiId(projNo, pbiId, pbiName);
			return "ok";
		} catch (Exception e) {
			// TODO: handle exception
			return "no";
		}
	}

	/**
	 * 读取pbi_id
	 * 
	 * @return
	 */
	public String queryPbiId(String projNo) {
		String pbi_id = personalBulidTimeDao.queryPbiId(projNo);
		if ("".equals(pbi_id)) {
			pbi_id = null;
		}
		return pbi_id;
	}

	/**
	 * 读取pbi_name
	 * 
	 * @return
	 */
	public String queryPbiName(String projNo) {
		String pbi_name = personalBulidTimeDao.queryPbiName(projNo);
		if ("".equals(pbi_name)) {
			pbi_name = null;
		}
		return pbi_name;
	}

	/**
	 * @Description: 获取个人构建时长最新值
	 * @author Administrator
	 * @date 2018年6月29日
	 */
	public String queryBuildTimeNew(String no) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 获取部门级别个人构建时长(首页图表)
	 */
	public Map<String, Object> getPersonalBulidTimeByNos(ProjectInfo proj) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> prons = new ArrayList<String>();
		// 查询项目编号
		Map<String, Object> maps = setMap(proj);
		List<ProjectInfo> listNos = vDao.querySelectedNos(maps);
		for (ProjectInfo pros : listNos) {
			prons.add(pros.getNo());
		}
		String sqlStr = StringUtilsLocal.listToSqlIn(prons);
		String months[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		for (String month : months) {
			double value = 0;
			// 计算每个月多个项目的版本构建时长均值
			Double buildTime = personalBulidTimeDao.personBuildTime(proj.getYear() + month, "(" + sqlStr + ")");
			Double buildCounts = personalBulidTimeDao.personBuildTimeCounts(proj.getYear() + month, "(" + sqlStr + ")");
			if (buildTime * buildCounts > 0) {
				value = buildTime / buildCounts;
			}
            // String month2 = EMonth.fromMonth(month).toString();
            // map.put(month2, StringUtilsLocal.keepTwoDecimals(value));
            map.put(LocalDateUtils.getAbbreviatedMonth(month).toUpperCase(), StringUtilsLocal.keepTwoDecimals(value));
		}
		return map;
	}

	private Map<String, Object> setMap(ProjectInfo proj) {
		Map<String, Object> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		if (proj.getArea() != null && !"".equals(proj.getArea())) {
			String[] arr = proj.getArea().split(",");
			map.put("area", Arrays.asList(arr));
		} else {
			map.put("area", list);
		}
		if (proj.getHwpdu() != null && !"".equals(proj.getHwpdu())) {
			String[] arr = proj.getHwpdu().split(",");
			map.put("hwpdu", Arrays.asList(arr));
		} else {
			map.put("hwpdu", list);
		}
		if (proj.getHwzpdu() != null && !"".equals(proj.getHwzpdu())) {
			String[] arr = proj.getHwzpdu().split(",");
			map.put("hwzpdu", Arrays.asList(arr));
		} else {
			map.put("hwzpdu", list);
		}
		if (proj.getPduSpdt() != null && !"".equals(proj.getPduSpdt())) {
			String[] arr = proj.getPduSpdt().split(",");
			map.put("pduSpdt", Arrays.asList(arr));
		} else {
			map.put("pduSpdt", list);
		}
		if (proj.getBu() != null && !"".equals(proj.getBu())) {
			String[] arr = proj.getBu().split(",");
			map.put("bu", Arrays.asList(arr));
		} else {
			map.put("bu", list);
		}
		if (proj.getPdu() != null && !"".equals(proj.getPdu())) {
			String[] arr = proj.getPdu().split(",");
			map.put("pdu", Arrays.asList(arr));
		} else {
			map.put("pdu", list);
		}
		if (proj.getDu() != null && !"".equals(proj.getDu())) {
			String[] arr = proj.getDu().split(",");
			map.put("du", Arrays.asList(arr));
		} else {
			map.put("du", list);
		}
		return map;
	}

	public Integer isHaveConfigIde(String proNo) {
		return personalBulidTimeDao.isHaveConfigIde(proNo);
	}

}
