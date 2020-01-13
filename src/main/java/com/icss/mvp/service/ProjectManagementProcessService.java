package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import com.icss.mvp.dao.IProjectManagementProcessDao;
import com.icss.mvp.entity.MeasureComment;
import com.icss.mvp.entity.QualityPlanModule;
import com.icss.mvp.util.IOUtils;
import com.icss.mvp.util.UUIDUtil;

@Service
@PropertySource("classpath:task.properties")
public class ProjectManagementProcessService {
	private final static Logger LOG = Logger.getLogger(ProjectManagementProcessService.class);

	@Value("${handsontable-path}")
	public String handsontablePath;

	@Autowired
	private IProjectManagementProcessDao projectManagementProcessDao;

	/**
	 * 结果质量指标
	 */
	public Map<String, Object> resultQuality(String proNo) {
		Map<String, Object> result = new HashMap<>();
		List<MeasureComment> mis = projectManagementProcessDao.queryMeasureList(proNo);
		List<String> lab = new ArrayList<>();// 流程
		Map<String, List<Map<String, Object>>> categor = new HashMap<>();// 类目
		List<Map<String, Object>> measures = new ArrayList<>();// 指标详情
		try {
			for (MeasureComment mc : mis) {
				Map<String, Object> m = new HashMap<>();
				m.put("labelTitle", supplement(mc.getLabelTitle()));
				m.put("measureCategory", supplement(mc.getMeasureCategory()));
				m.put("measureName", supplement(mc.getMeasureName()));
				m.put("collectType", supplement(mc.getCollectType()));
				m.put("upper", supplement(mc.getUpper()));
				m.put("lower", supplement(mc.getLower()));
				m.put("challenge", supplement(mc.getChallenge()));
				m.put("target", supplement(mc.getTarget()));
				m.put("unit", supplement(mc.getUnit()));
				measures.add(m);
				if (lab.contains(mc.getLabelTitle())) {
					List<Map<String, Object>> cat = categor.get(mc.getLabelTitle());
					boolean flag = true;
					for (Map<String, Object> ma : cat) {
						if (ma.get("category").equals(mc.getMeasureCategory())) {
							ma.replace("num", Integer.valueOf(String.valueOf(ma.get("num"))) + 1);
							flag = false;
							break;
						}
					}
					if (flag) {
						Map<String, Object> p = new HashMap<>();
						p.put("category", mc.getMeasureCategory());
						p.put("num", 1);
						cat.add(p);
						categor.replace(mc.getLabelTitle(), cat);
					}
				} else {
					lab.add(mc.getLabelTitle());
					List<Map<String, Object>> cat = new ArrayList<>();
					Map<String, Object> p = new HashMap<>();
					p.put("category", mc.getMeasureCategory());
					p.put("num", 1);
					cat.add(p);
					categor.put(mc.getLabelTitle(), cat);
				}
			}
			result.put("lab", lab);
			result.put("categor", categor);
			result.put("measures", measures);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.info(e);
		}
		return result;
	}

	private String supplement(Object measure) {
		if (measure == null || "".equals(measure)) {
			return "-";
		}
		return String.valueOf(measure);
	}

	/**
	 * 指标分类个数
	 */
	public List<Map<String, Object>> categorys() {
		return projectManagementProcessDao.categorys();
	}

	/**
	 * 质量策划添加新模块
	 */
	public void saveModule(QualityPlanModule qualityPlanModule) {
		qualityPlanModule.init();
		projectManagementProcessDao.saveModule(qualityPlanModule);
	}

	/**
	 * 质量策划查询某模块
	 */
	public QualityPlanModule queryModule(String no, String module) {
		return projectManagementProcessDao.queryModule(no, module);
	}

	/**
	 * 质量策划查询所有模块
	 */
	public List<QualityPlanModule> queryModules(String no) {
		return projectManagementProcessDao.queryModules(no);
	}

	/**
	 * 删除模块
	 */
	public void sureDeletemodule(QualityPlanModule qualityPlanModule) {
		projectManagementProcessDao.deleteExceltable(qualityPlanModule.getNo(), qualityPlanModule.getModuleName());
		projectManagementProcessDao.deleteQualityplanmodule(qualityPlanModule.getNo(),
				qualityPlanModule.getModuleName());
		String path = handsontablePath + qualityPlanModule.getNo() + "/handsontable_"
				+ qualityPlanModule.getModuleName() + ".json";
		IOUtils.deleteFile(path);
	}

}