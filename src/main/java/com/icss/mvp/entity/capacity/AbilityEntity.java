package com.icss.mvp.entity.capacity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icss.mvp.entity.common.BaseEntity;

/**
 * @author Ray
 * @date 2018/12/28
 */
public class AbilityEntity extends BaseEntity {

    /**
     * default serial version UID
     */
    private static final long           serialVersionUID = 1L;

    /**
     * 员工姓名
     */
    private String                      name;
    /**
     * 员工工号
     */
    private String                      account;
    /**
     * 团队角色
     */
    private String                      role;

    /**
     * 关联账号，svn，git，dts，tmss 的对应账号
     */
    private String                      relatedAccount;

    /**
     * 工作量
     */
    @JsonIgnore
    private List<WorkloadEntity>        workload         = new ArrayList<>();

    /**
     * 工作量，key为统计时间周期，如每月
     */
    private Map<String, WorkloadEntity> workloads        = new HashMap<>();

    public AbilityEntity(){

    }

    public AbilityEntity(String... accounts){
        this.account = accounts.length > 0 ? accounts[0] : account;
        this.relatedAccount = accounts.length > 1 ? accounts[1] : relatedAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRelatedAccount() {
		return relatedAccount;
    }

    public void setRelatedAccount(String relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public List<WorkloadEntity> getWorkload() {
        return workload;
    }

    public void setWorkload(List<WorkloadEntity> workload) {
        this.workload = workload;
    }

    public Map<String, WorkloadEntity> getWorkloads() {
        if (workload != null && !workload.isEmpty()) {
            workloads = workload.stream().collect(Collectors.toMap(WorkloadEntity::getPeriod, Function.identity()));
        } else {
            workloads = new HashMap<>();
        }

        return workloads;
    }

    protected void setWorkloads(Map<String, WorkloadEntity> workloads) {
        this.workloads = workloads;
    }
}
