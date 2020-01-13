package com.icss.mvp.entity;

import com.icss.mvp.entity.common.CommonEntity;

/**
 * Created by up on 2019/2/25.
 */
public class HandsontableInfo extends CommonEntity {
//    private String            id;
    private String            no;           //项目编号
    private String            module;       //表格名称

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

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
}
