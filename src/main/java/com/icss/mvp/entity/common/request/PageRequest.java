package com.icss.mvp.entity.common.request;

public class PageRequest extends BaseRequest {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    private Integer           pageSize;

    private Integer           pageNumber       = 0;

    private Integer           offset;

    public Integer getPageSize() {
        return pageSize < 0 ? 0 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize < 0 ? 0 : pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber < 0 ? 0 : pageNumber;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setLimit(Integer limit) {
        this.pageSize = limit < 0 ? 0 : limit;
}

    public PageRequest(){
        this.pageSize = 10;
        this.pageNumber = 0;
    }

    public Integer getOffset(Integer totalCount) {
        Integer offset = 0;

        if (totalCount > 0) {
            if (pageSize <= 0) {
                pageSize = 10;
            }
            if (pageNumber == null) {
                pageNumber = 0;
            }
            // 超出页码有效范围，重新设定页码为最大页数
            if (pageSize * pageNumber >= totalCount) {
                Integer round = totalCount / pageSize;
                Integer mod = totalCount % pageSize;
                pageNumber = (mod == 0 ? round - 1 : round);
            }
            offset = pageSize * pageNumber;
        }

        return offset;
    }
}
