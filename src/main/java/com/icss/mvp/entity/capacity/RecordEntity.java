package com.icss.mvp.entity.capacity;

import com.icss.mvp.entity.common.BaseEntity;

/**
 * Created by Ray on 2019/5/20.
 *
 * @author Ray
 * @date 2019/5/20 10:55
 */
public class RecordEntity extends BaseEntity {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 归属人
     */
    private String            author;

    /**
     * 统计阶段，年月，2018.10，2019.01
     */
    private String            period;

    /**
     * 工作量，代码行数，用例个数
     */
    private int               commitLine;

    /**
     * 文件类型
     */
    private String            fileType;

    /**
     * 类型权重
     */
    private double            weight;

    /**
     * 折算代码行数
     */
    private double            amount;

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

    public int getCommitLine() {
        return commitLine;
    }

    public void setCommitLine(int commitLine) {
        this.commitLine = commitLine;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAmount() {
        amount = commitLine * weight;
        return amount;
    }

    protected void setAmount(double amount) {
        this.amount = amount;
    }
}
