package com.icss.mvp.dao;

import com.icss.mvp.entity.IsourceIssueInfo;

import java.util.List;


public interface IIsourceIssueTaskDao
{
    /**
     * 
     * <pre>
     * <b>描述：保存IsourceIssue日志</b>
     *  @param list
     *  @return
     * </pre>
     */
    int saveLogList(List<IsourceIssueInfo> list);
}
