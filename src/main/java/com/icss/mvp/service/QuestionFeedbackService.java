package com.icss.mvp.service;

import com.icss.mvp.dao.SysAnswerDao;
import com.icss.mvp.dao.SysQuestionDao;
import com.icss.mvp.entity.PageInfo;
import com.icss.mvp.entity.SysAnswer;
import com.icss.mvp.entity.SysQuestion;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class QuestionFeedbackService {
	@Autowired
	private SysQuestionDao sysQuestionDao;
	@Autowired
	private SysAnswerDao sysAnswerDao;
	// 问题停用状态
	private final String QUESTION_STOP_USE_CODE = "2";
	private final String ANSWER_ACCEPTED_CODE = "1";
	private final String QUESTION_SOLVED_CODE = "1";

	/**
	 * 新增问题 并设置问题的创建时间、停止状态、解决状态
	 * 
	 * @param sysQuestion
	 */
	public void addQuestion(SysQuestion sysQuestion) {
		sysQuestion.init();
		sysQuestionDao.insert(sysQuestion);
	}

	public void editQuestion(String id, String content, String stopState, String userId) {
		if (StringUtils.isBlank(id)) {
			throw new IllegalArgumentException("问题id不能为空!");
		}
		SysQuestion sysQuestion = new SysQuestion();
		sysQuestion.setId(id);
		sysQuestion.setContent(content);
		if (QUESTION_STOP_USE_CODE.equals(stopState)) {
			sysQuestion.setStopState(stopState);
			sysQuestion.setStopDate(new Date());
			sysQuestion.setStopUser(userId);
		}

		sysQuestionDao.updateByPrimaryKeySelective(sysQuestion);
	}

	public void addAnswer(SysAnswer sysAnswer) {
		sysAnswer.init();
		sysAnswerDao.insert(sysAnswer);
//		sysAnswerDao.updateSolveState(sysAnswer.getQuestionId());
	}

	/**
	 * 修改回复的内容、或者回复被接受修改接受状态和问题的解决状态
	 * 
	 * @param answerId
	 * @param content
	 * @param acceptState
	 */
	public void editAnswer(String answerId, String content, String acceptState) {
		SysAnswer sysAnswer = new SysAnswer();
		sysAnswer.setId(answerId);
		sysAnswer.setContent(content);
		boolean isSolve = false;
		if (ANSWER_ACCEPTED_CODE.equals(acceptState)) {
			isSolve = true;
			sysAnswer.setAcceptState(acceptState);
		}
		sysAnswerDao.updateByPrimaryKeySelective(sysAnswer);
		if (isSolve) {
			SysQuestion sysQuestion = new SysQuestion();
			sysQuestion.setSolveState(QUESTION_SOLVED_CODE);
			SysAnswer answer = sysAnswerDao.selectByPrimaryKey(answerId);
			sysQuestion.setId(answer.getQuestionId());
			sysQuestionDao.updateByPrimaryKeySelective(sysQuestion);
		}
	}

	public List<Map<String, Object>> questionList(SysQuestion sysQuestion, PageInfo pageInfo) {
		// int count = sysQuestionDao.selectQuestionCount(sysQuestion);
		// if (pageInfo.getCurrentPage() < 1) {
		// pageInfo.setCurrentPage(1);
		// } else if (pageInfo.getCurrentPage() > (count / pageInfo.getPageSize()) + 1)
		// {
		// pageInfo.setCurrentPage(count / pageInfo.getPageSize() + 1);
		// }
		// pageInfo.initPageInfo();
		// pageInfo.setTotalRecord(count);
		if (sysQuestion.getName() != null) {
			if ("".equals(sysQuestion.getName().trim()) || "undefined".equals(sysQuestion.getName())) {
				sysQuestion.setName(null);
			}
		}
		if (sysQuestion.getUserId() != null) {
			if ("".equals(sysQuestion.getUserId().trim()) || "undefined".equals(sysQuestion.getUserId())) {
				sysQuestion.setUserId(null);
			}
		}
		return sysQuestionDao.selectQuestions(sysQuestion, pageInfo);
	}

	public List<SysAnswer> answersList(String questionId, PageInfo pageInfo) {
		int count = sysAnswerDao.selectAnswerCount(questionId);
		pageInfo.initPageInfo();
		pageInfo.setTotalRecord(count);
		return sysAnswerDao.selectAnswers(questionId, pageInfo);
	}

	public List<Map<String, Object>> details(String questionId) {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = sysQuestionDao.details(questionId);
		list.add(map);
		return list;
	}
	
	public void updateSolveState(String solveState,String id) {
		sysQuestionDao.updateSolveState(solveState, id);
	}
}