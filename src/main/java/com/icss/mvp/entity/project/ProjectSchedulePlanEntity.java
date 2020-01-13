package com.icss.mvp.entity.project;

import com.icss.mvp.entity.common.CommonEntity;

import java.util.List;

/**
 * Created by up on 2019/2/14.
 */
public class ProjectSchedulePlanEntity extends CommonEntity {

//    private String            id;
    private String            no;       //项目编号
    private String            planName; //里程碑计划名称
    private Integer           planType; //里程碑计划类型 1：里程碑，2：迭代，3：版本

    private List<SchedulePlanEntity> list;

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

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    public List<SchedulePlanEntity> getList() {
        return list;
    }

    public void setList(List<SchedulePlanEntity> list) {
        this.list = list;
    }
}
