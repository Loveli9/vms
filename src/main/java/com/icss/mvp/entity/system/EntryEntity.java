package com.icss.mvp.entity.system;

import com.icss.mvp.entity.common.CommonEntity;

/**
 * Entry (in data dictionary)
 *
 * @author Ray
 * @date 2019/3/19 14:09
 */
public class EntryEntity extends CommonEntity {

    private static final long serialVersionUID = 1L;

    private String            name;

    private String            code;

    private String            description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
