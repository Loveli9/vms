package com.icss.mvp.entity.capacity;

import com.icss.mvp.entity.common.BaseEntity;

/**
 * Created by Ray on 2019/5/20.
 *
 * @author Ray
 * @date 2019/5/20 11:19
 */
public class ChangeEntity extends BaseEntity {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

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
}
