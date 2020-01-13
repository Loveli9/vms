package com.icss.mvp.entity.capacity;

import com.icss.mvp.entity.common.BaseEntity;

/**
 * @author Ray
 * @date 2018/12/29
 */
public class LanguageEntity extends BaseEntity {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String            fileType;

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
