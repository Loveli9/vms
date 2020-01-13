package com.icss.mvp.entity.common;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Ray
 * @date 2018/7/27
 */
public class CommonEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 主键Id
     */
    private String            id;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date              createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date              modifyTime;
    /**
     * 逻辑删除标识 1:已删除数据，0:正常数据
     */
    private int               isDeleted;

    public String getId() {
        return id;
    }

    public <T> void setId(T id) {
        this.id = id != null ? String.valueOf(id) : null;
    }

    public Date getCreateTime() {
        if (createTime == null) {
            createTime = new Date();
        }
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        if (modifyTime == null) {
            modifyTime = new Date();
        }
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
