package com.icss.mvp.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.icss.mvp.dao.ILabelDAO;
import com.icss.mvp.entity.Label;

@Service
public class LabelService{
	@Autowired
	private ILabelDAO labelDAO;
	
	public List<Map<String, Object>> queryLabel(String selCategory){
		return labelDAO.queryLabel(selCategory);
	}
	
	public void updateLabel(Label label) {
		labelDAO.updateLabel(label);
	}
	
	public void updateDeleteLabel(int id) {
		labelDAO.updateDeleteLabel(id);
	};
	
	public void insertLabel(Label label) {
		labelDAO.insertLabel(label);
	};	
	
}
