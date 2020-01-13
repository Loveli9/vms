package com.icss.mvp.dao;


import com.icss.mvp.entity.ProjectComment;

public interface ProjectCommentDao {
		int insertComment(ProjectComment comment);
		String selectComment(String project_id);
		int updateComment(ProjectComment ProjectComment);
		int cleanComment(ProjectComment ProjectComment);
}
