package com.icss.mvp.entity.capacity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.icss.mvp.entity.common.BaseEntity;

/**
 * @author Ray
 * @date 2018/12/28
 */
public class WorkloadEntity extends BaseEntity {

    /**
     * default serial version UID
     */
    private static final long         serialVersionUID = 1L;

    /**
     * 归属人
     */
    private String                    author;

    /**
     * 统计阶段，年月，2018.10，2019.01
     */
    private String                    period;

    /**
     * 统计类型，Java/C/Python/Golang/用例
     */
    private String                    type;

    /**
     * 工作量，代码行数，用例个数
     */
    private int                       amount;

    /**
     * 提交次数
     */
    private int                       times;

    /**
     * 最后提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date                      lastCommitTime;

    /**
     * 提交记录，每次提交，或者汇总结果
     */
    @JsonIgnore
    private List<RecordEntity>        record           = new ArrayList<>();

    @JsonIgnore
    private Map<String, RecordEntity> records          = new HashMap<>();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Date getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(Date lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }

    public List<RecordEntity> getRecord() {
        return record;
    }

    public void setRecord(List<RecordEntity> record) {
        this.record = record;
    }

    public Map<String, RecordEntity> getRecords() {
        if (record != null && !record.isEmpty()) {
            records = record.stream().collect(Collectors.toMap(RecordEntity::getFileType, Function.identity()));
        } else {
            records = new HashMap<>();
        }
        return records;
    }

    protected void setRecords(Map<String, RecordEntity> records) {
        this.records = records;
    }
}
