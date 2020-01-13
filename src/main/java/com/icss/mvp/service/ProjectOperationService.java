package com.icss.mvp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.dao.IProjectOperationDao;
import com.icss.mvp.entity.TableSplitResult;
import com.icss.mvp.entity.common.request.PageRequest;
@Service
public class ProjectOperationService {
	@Autowired
	private IProjectOperationDao projectOperationDao;

	public void saveProjectOperation(String proNo, String userName, String message) {
		projectOperationDao.saveProjectOperation(proNo, userName, message);
	}
	public TableSplitResult<List<Map<String, String>>> queryProjectOperation(String proNo, PageRequest page) {
		TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<List<Map<String, String>>>();
		result.setRows(projectOperationDao.queryProjectOperationList(proNo, page));
		result.setTotal(projectOperationDao.queryProjectOperationCount(proNo));
		return result;
	}

	public TableSplitResult<List<Map<String, String>>> queryProjectOperationClone(PageRequest page) {
		TableSplitResult<List<Map<String, String>>> result = new TableSplitResult<List<Map<String, String>>>();
		result.setRows(projectOperationDao.queryProjectOperationListClone(page));
		result.setTotal(projectOperationDao.queryProjectOperationCountClone());
		return result;
	}
}
