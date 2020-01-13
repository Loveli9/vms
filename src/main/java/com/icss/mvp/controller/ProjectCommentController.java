package com.icss.mvp.controller;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.icss.mvp.entity.ProjectComment;
import com.icss.mvp.service.ProjectCommentService;
@RestController
@RequestMapping("/comment")
public class ProjectCommentController {
	
	@Resource	
	private ProjectCommentService projectCommentService;
	
	
	public ProjectCommentService getProjectCommentService() {
		return projectCommentService;
	}


	public void setProjectCommentService(ProjectCommentService projectCommentService) {
		this.projectCommentService = projectCommentService;
	}


	@RequestMapping("/insertComment")
	@ResponseBody
	public int insertComment(Date create_time,Date modify_time,int is_deleted,String comment,String project_id){
		ProjectComment ProjectComment=new ProjectComment();
		ProjectComment.setCreate_time(create_time);
		ProjectComment.setModify_time(modify_time);
		ProjectComment.setIs_deleted(is_deleted);
		ProjectComment.setComment(comment);
		ProjectComment.setProject_id(project_id);	
		return projectCommentService.insertComment(ProjectComment);
	}
	
	
	@RequestMapping("/selectComment")
	@ResponseBody
	public String selectComment(String project_id) {		
		return projectCommentService.selectComment(project_id);
	}
	
	@RequestMapping("/updateComment")
	@ResponseBody	
	public int updateComment(Date modify_time,String comment,String project_id) {
		ProjectComment ProjectComment=new ProjectComment();	
		ProjectComment.setModify_time(modify_time);
		ProjectComment.setComment(comment);
		ProjectComment.setProject_id(project_id);
		return projectCommentService.updateComment(ProjectComment);
	}
	
	@RequestMapping("/cleanComment")
	@ResponseBody
	public int cleanComment(Date modify_time,int is_deleted,String project_id) {
		ProjectComment ProjectComment=new ProjectComment();
		ProjectComment.setModify_time(modify_time);
		ProjectComment.setProject_id(project_id);
		ProjectComment.setIs_deleted(is_deleted);
		return projectCommentService.cleanComment(ProjectComment);
	}
	
	
}
