package com.icss.mvp.entity.pageHelper;

/**
 * 
 * @author zqq
 * 
 *
 */
public class PageEntity {
	private int page;
	private int rows;
	//排序字段
	private String sort;
	//排序顺序 asc升序，desc降序
	private String order;
	
	public PageEntity() {
		super();
	}
	
	public PageEntity(int page,int rows) {
		this.page = page;
		this.rows = rows;		
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort()
	{
		return sort;
	}

	public void setSort(String sort)
	{
		this.sort = sort;
	}

	public String getOrder()
	{
		return order;
	}

	public void setOrder(String order)
	{
		this.order = order;
	}
}
