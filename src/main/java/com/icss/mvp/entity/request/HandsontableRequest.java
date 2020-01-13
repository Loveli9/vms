package com.icss.mvp.entity.request;

import com.icss.mvp.entity.common.request.BaseRequest;

import java.util.List;

public class HandsontableRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private String         no;
    private String         module;
    private List           data;//数据
    private List           mergeCells;//合并规则
    private List           colWidths;//行宽度
    private List           cellsMeta;//行宽度

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public List getMergeCells() {
        return mergeCells;
    }

    public void setMergeCells(List mergeCells) {
        this.mergeCells = mergeCells;
    }

    public List getColWidths() {
        return colWidths;
    }

    public void setColWidths(List colWidths) {
        this.colWidths = colWidths;
    }

    public List getCellsMeta() {
        return cellsMeta;
    }

    public void setCellsMeta(List cellsMeta) {
        this.cellsMeta = cellsMeta;
    }
}
