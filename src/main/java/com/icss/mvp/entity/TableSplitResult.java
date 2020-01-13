package com.icss.mvp.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
/**
* @ClassName: TableSplitResult
* @Description: 分页数据封装
* @author chengchenhui
* @date 2018年8月6日
* @param <T>
 */
@SuppressWarnings("all")
public class TableSplitResult<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private  Integer page;//第几页
    private Integer total;//总记录
    private T rows;//每页行数
    private Map<String, Object> queryMap;//查询条件
    

	public TableSplitResult() {
    }
 
    public TableSplitResult(Integer page, Integer total, T rows) {
        this.page = page;
        this.total = total;
        this.rows = rows;
    }
 
    public Integer getPage() {
        return page;
    }
 
    public void setPage(Integer page) {
        this.page = page;
    }
 
    public Integer getTotal() {
        return total;
    }
 
    public void setTotal(Integer total) {
        this.total = total;
    }
 
    public T getRows() {
        return rows;
    }
 
    public void setRows(T rows) {
        this.rows = rows;
    }

	public Map<String, Object> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, Object> queryMap) {
		this.queryMap = queryMap;
	}

	public void setErr(T rows,int page) {
        this.rows = rows;
        this.page = page;
        this.total = 0;
    }


    
}
