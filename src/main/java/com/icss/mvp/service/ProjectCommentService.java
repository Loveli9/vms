package com.icss.mvp.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.ProjectCommentDao;
import com.icss.mvp.entity.ProjectComment;

@Service("ProjectCommentService")
@Transactional
public class ProjectCommentService {
	@Resource
	ProjectCommentDao projectCommentDao;
	
	public int insertComment(ProjectComment comment) {
		int i=projectCommentDao.insertComment(comment);
		return i;
	}
	
	public String selectComment(String project_id) {
		String comment=new String();
		comment=projectCommentDao.selectComment(project_id);
		return comment;
	}

	public int updateComment(ProjectComment ProjectComment) {
		int i=projectCommentDao.updateComment(ProjectComment);
		return i;
	}
	
	
	public int cleanComment(ProjectComment ProjectComment) {
		
		int i=projectCommentDao.cleanComment(ProjectComment);
		return i;
		
	}
}
