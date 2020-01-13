package com.icss.mvp.entity.project;

import com.icss.mvp.entity.common.CommonEntity;

import java.util.Date;

/**
 * Created by up on 2019/2/14.
 */
public class SchedulePlanEntity extends CommonEntity {

//    private String            id;
    private String            projectScheduleId;
    private String            name;
    private String            description;
    private String            scheduleType;
    private String            scheduleIcon;
    private Date            plannedFinishDate;
    private Date            actualFinishDate;
    private String            scheduleState;


//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getProjectScheduleId() {
        return projectScheduleId;
    }

    public void setProjectScheduleId(String projectScheduleId) {
        this.projectScheduleId = projectScheduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleIcon() {
        return scheduleIcon;
    }

    public void setScheduleIcon(String scheduleIcon) {
        this.scheduleIcon = scheduleIcon;
    }

    public Date getPlannedFinishDate() {
        return plannedFinishDate;
    }

    public void setPlannedFinishDate(Date plannedFinishDate) {
        this.plannedFinishDate = plannedFinishDate;
    }

    public Date getActualFinishDate() {
        return actualFinishDate;
    }

    public void setActualFinishDate(Date actualFinishDate) {
        this.actualFinishDate = actualFinishDate;
    }

    public String getScheduleState() {
        return scheduleState;
    }

    public void setScheduleState(String scheduleState) {
        this.scheduleState = scheduleState;
    }
}
