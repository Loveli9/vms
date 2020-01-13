package com.icss.mvp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.icss.mvp.util.StringUtilsLocal;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.ICodeMasterInfoDao;
import com.icss.mvp.entity.CodeMasterInfo;

@Service("codeMasterInfoService")
@Transactional
public class CodeMasterInfoService {

	@Resource
	private ICodeMasterInfoDao codeMasterInfoDao;
	
	public CodeMasterInfo getCodeMaster(@Param("codeM")CodeMasterInfo codeM)
	{
		if((null == codeM.getName()||"".equals(codeM.getName()))&&(null == codeM.getValue() || "".equals(codeM.getValue())))
		{
			return codeM;
		}
		
		CodeMasterInfo codeMasterInfo = new CodeMasterInfo();
		List<CodeMasterInfo> list = codeMasterInfoDao.getList(codeM);
		for (CodeMasterInfo c : list) {
			codeMasterInfo = c;
		}
		return null != codeMasterInfo ? codeMasterInfo : codeM;
	}
	public List<CodeMasterInfo> getList(@Param("codeM")CodeMasterInfo codeM)
	{
		return codeMasterInfoDao.getList(codeM);	
	}
	
	public List<Map<String, Object>> getCodeMasterOrder(CodeMasterInfo c) {
		List<Map<String, Object>> master = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotBlank(c.getCodekey()) && ("status".equals(c.getCodekey()) || "rank".equals(c.getCodekey()))) {
			List<Map<String, Object>> statusList = new ArrayList<Map<String, Object>>();
			if("status".equals(c.getCodekey())){
				statusList = codeMasterInfoDao.getMemberStatusValue();
			}else if("rank".equals(c.getCodekey())){
				statusList = codeMasterInfoDao.getMemberRankValue();
			}
			
			for (Map<String, Object> status : statusList) {
				Map<String, Object> map = new HashMap<String, Object>();
				if ("status".equals(c.getCodekey())) {
					map.put("value", StringUtilsLocal.valueOf(status.get("val")));
					map.put("text", StringUtilsLocal.valueOf(status.get("NAME")));
				}else if("rank".equals(c.getCodekey())){
					map.put("value", StringUtilsLocal.valueOf(status.get("rank_name")));
					map.put("text", StringUtilsLocal.valueOf(status.get("VALUE")));
				}
				master.add(map);
			}
		} else {
			List<CodeMasterInfo> model = codeMasterInfoDao.getCodeMasterOrderByValue(c);
			if (model != null && model.size() > 0) {
				for (CodeMasterInfo codeMasterInfo : model) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("value", codeMasterInfo.getValue());
					map.put("text", codeMasterInfo.getName());
					master.add(map);
				}
			}
		}

		return master;
	}
	public List<CodeMasterInfo> getCodeMasterOrderByValue(CodeMasterInfo codeM) {
		return codeMasterInfoDao.getCodeMasterOrderByValue(codeM);
	}

	public List<Map<String, Object>> getCodeMasterOrderByValues(CodeMasterInfo codeM) {
		return codeMasterInfoDao.getCodeMasterOrderByValues(codeM);
	}
	
	public Map<String, Object> getMemberStatusValue() {
		List<Map<String, Object>> list = codeMasterInfoDao.getMemberStatusValue();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}
	
	public List<Map<String, Object>> getProjectPOCode(String no) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String teamId = codeMasterInfoDao.getTeamId(no);
		
		if(StringUtils.isNotBlank(teamId)){
			list = codeMasterInfoDao.getProjectPOCodeByTeam(teamId);
		}else{
			list = codeMasterInfoDao.getProjectPOCodeByProject(no);
		}
		
		return list;
	}
	
	public Map<String, Object> getMemberSVN(String no) {
		List<Map<String, Object>> list = codeMasterInfoDao.getMemberSVN(no);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}
	public Map<String, Object> getMemberRankValue() {
		List<Map<String, Object>> list = codeMasterInfoDao.getMemberRankValue();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}

	public List<Map<String, Object>>getMemberRankValues() {
		List<Map<String, Object>> list = codeMasterInfoDao.getMemberRankValues();
		return list;
	}
	
}
