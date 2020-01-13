package com.icss.mvp.entity.project;

import com.icss.mvp.entity.common.response.PageResponse;

/**
 * DO NOT USE, will be removed
 *
 * @author Ray
 * @date 2019/12/19 10:49
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Deprecated
public class OddResponse<T> extends PageResponse<T> {

    private Integer loginer;

    public Integer getLoginer() {
        return loginer;
    }

    public void setLoginer(Integer loginer) {
        this.loginer = loginer;
    }

    /**
     * 有性能问题
     * 
     * @param element
     * @param <E>
     */
    public <E extends PageResponse> OddResponse(E element){

        super();

        super.setData(element.getData());
        super.setCode(element.getCode());
        super.setMessage(element.getMessage());
        super.setSucceed(element.getSucceed());
        super.setPageSize(element.getPageSize());
        super.setPageNumber(element.getPageNumber());
        super.setTotalCount(element.getTotalCount());
    }
}
