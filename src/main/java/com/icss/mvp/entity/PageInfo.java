package com.icss.mvp.entity;

public class PageInfo {

	private Integer pageSize; // 每页大小

	private Integer totalRecord;// 总条数

	private Integer totalPage;// 总页数

	private Integer currentPage;// 当前页

	private Integer fromIndex;// 起始索引

	public Integer getFromIndex() {
		return fromIndex;
	}

	/**
	 * 根据pageSize和当前的页码currentPage设置fromIndex
	 */
	public void initPageInfo() {
		if (null != pageSize && null != currentPage) {
			Integer fromIndex = (currentPage - 1) * pageSize;
			this.fromIndex = fromIndex;
		}
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

    /**
     * 设置总条数
     * 
     * @param totalRecord
     */
    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
        if (this.pageSize != null && this.pageSize > 0) {
            // this.totalPage = (int) Math.ceil(totalRecord / pageSize);

            int remainder = this.totalRecord % this.pageSize;
            int quotient = this.totalRecord / this.pageSize;
            this.totalPage = remainder == 0 ? quotient : quotient + 1;
        }
    }

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public void setFromIndex(Integer fromIndex) {
		this.fromIndex = fromIndex;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}
