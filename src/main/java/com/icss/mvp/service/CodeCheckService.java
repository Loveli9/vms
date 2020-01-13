package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.ICodeCheckDao;
import com.icss.mvp.dao.IProjectSourceConfigDao;
import com.icss.mvp.dao.ISvnTaskDao;
import com.icss.mvp.dao.project.IProjectDao;
import com.icss.mvp.entity.CodeCheck;
import com.icss.mvp.entity.RepositoryInfo;
import com.icss.mvp.entity.project.ProjectBaseEntity;
import com.icss.mvp.util.DateUtils;
import com.icss.mvp.util.HttpExecuteUtils;
import com.icss.mvp.util.StringUtilsLocal;
import com.icss.mvp.util.Tools;

@Service
public class CodeCheckService {
	private final static Logger logger = Logger.getLogger(CodeCheckService.class);

	@Value("${codeCheckUrl}")
	private String codeCheckUrl;

	@Resource
	HttpServletRequest request;

	@Resource
	HttpServletResponse response;

	@Autowired
	private ICodeCheckDao codeCheckDao;

    @Autowired
    private IProjectSourceConfigDao sourceDao;
	@Autowired
	ISvnTaskDao svnTaskDao;

	/**
	 * 保存代码检视结果
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int insertCodeCheck(String no,String token,String id) {
		List<CodeCheck> listInsert = new ArrayList<>();
		Map<String, Object> para = new HashMap<>();
		// 获取上次采集的最后一天的日期
		String last = getLastCollectTime(no);
		para.put("pageSize", 2000);
		// 根据上次采集的最后一天的日期得到下一天的日期
		para.put("startTime", DateUtils.getNextDay(last));
		// 得到今天的日期
		para.put("endTime", DateUtils.getTodayDay());
		
		List<RepositoryInfo> infos = sourceDao.searchRepositoryByNos(no,"6",id);
		if (infos == null || !(infos.size()>0)) {
			return 0;
        }
		for (RepositoryInfo repositoryInfo : infos) {
			String pbiName = repositoryInfo.getBranch();
			if(StringUtilsLocal.isBlank(pbiName)) {
				return 2;
			}
			para.put("pbiName", pbiName);
  		}
		try {
			for (int i = 1; true; i++) {
				para.remove("pageNum");
				para.put("pageNum", i);
				String codeCheckXML = HttpExecuteUtils.httpGet(codeCheckUrl, para);
				Document document = Tools.getDocumentByXml(codeCheckXML);// XML
				Element results = document.getRootElement();// results
				Element result = results.element("result");// result
				if (result == null) {
					break;
				}
				Element adviceList = result.element("adviceList");// adviceList
				List<Element> advices = adviceList.elements("advice");// advice
				if (advices == null || advices.size() == 0) {
					break;
				}
				for (Element advice : advices) {
					CodeCheck cc = new CodeCheck();
					cc.setAdviceId("".equals(advice.element("adviceId").getText()) ? null
							: advice.element("adviceId").getText());
					cc.setTaskId(
							"".equals(advice.element("taskId").getText()) ? null : advice.element("taskId").getText());
					cc.setVersionName("".equals(advice.element("versionName").getText()) ? null
							: advice.element("versionName").getText());
					cc.setAssigneer("".equals(advice.element("assigneer").getText()) ? null
							: advice.element("assigneer").getText());
					cc.setReporter("".equals(advice.element("reporter").getText()) ? null
							: advice.element("reporter").getText());
					cc.setNeedConfirm("".equals(advice.element("needConfirm").getText()) ? null
							: advice.element("needConfirm").getText());
					cc.setParentCurse("".equals(advice.element("parentCurse").getText()) ? null
							: advice.element("parentCurse").getText());
					cc.setParentCurseEName("".equals(advice.element("parentCurseEName").getText()) ? null
							: advice.element("parentCurseEName").getText());
					cc.setChildCurse("".equals(advice.element("childCurse").getText()) ? null
							: advice.element("childCurse").getText());
					cc.setChildCurseEName("".equals(advice.element("childCurseEName").getText()) ? null
							: advice.element("childCurseEName").getText());
					cc.setContent("".equals(advice.element("content").getText()) ? null
							: advice.element("content").getText());
					cc.setCreateTime("".equals(advice.element("createTime").getText()) ? null
							: advice.element("createTime").getText());
					cc.setUpdateTime("".equals(advice.element("updateTime").getText()) ? null
							: advice.element("updateTime").getText());
					cc.setEndTime("".equals(advice.element("endTime").getText()) ? null
							: advice.element("endTime").getText());
					cc.setCriticalLevel("".equals(advice.element("criticalLevel").getText()) ? null
							: advice.element("criticalLevel").getText());
					cc.setFileFullPath("".equals(advice.element("fileFullPath").getText()) ? null
							: advice.element("fileFullPath").getText());
					cc.setFirstLine("".equals(advice.element("firstLine").getText()) ? null
							: advice.element("firstLine").getText());
					cc.setLineRange("".equals(advice.element("lineRange").getText()) ? null
							: advice.element("lineRange").getText());
					cc.setSolution("".equals(advice.element("solution").getText()) ? null
							: advice.element("solution").getText());
					cc.setStatus(
							"".equals(advice.element("status").getText()) ? null : advice.element("status").getText());
					cc.setIssueKey("".equals(advice.element("issueKey").getText()) ? null
							: advice.element("issueKey").getText());
					cc.setIsDelete("".equals(advice.element("isDelete").getText()) ? null
							: Integer.valueOf(advice.element("isDelete").getText()));
					listInsert.add(cc);
				}
			}
			if (listInsert != null && listInsert.size() > 0) {
				codeCheckDao.insertCodeCheck(listInsert);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("NO", no);
			map.put("lasttime", new Date());
			map.put("id", id);
			svnTaskDao.insertlasttime(map);
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
            logger.info(e.getMessage());
			return 0;
		}
	}

    @Autowired
    IProjectDao projectDao;

    public String getLastCollectTime(String projectId) {
        // 获取上次采集的最后一天的日期
        String result = codeCheckDao.getLastday(projectId);

        // 如果没有采集过就用项目起始日期
        if (StringUtils.isBlank(result)) {
            ProjectBaseEntity project = null;
            try {
                List<ProjectBaseEntity> projects = projectDao.getProjectList(Collections.singleton(projectId));
                if (projects != null && !projects.isEmpty()) {
                    project = projects.get(0);
                }
            } catch (Exception e) {
                logger.error("projectDao.getProjectList exception, error: " + e.getMessage());
            }

            if (project != null && project.getBeginDate() != null) {
                result = DateUtils.formatDate(DateUtils.SHORT_FORMAT_GENERAL, project.getBeginDate());
            }
        }

        return result;
    }
}
