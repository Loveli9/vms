package com.icss.mvp.entity.system;

import com.icss.mvp.entity.common.CommonEntity;

/**
 * Created by Ray on 2019/8/5.
 *
 * @author Ray
 * @date 2019/8/5 14:57
 */
public class AuthorityEntity extends CommonEntity {

    private static final long serialVersionUID = 1L;

    private String            name;
    private String            description;

    private String            creator;
    private String            operator;

    private String            authorized;

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }
}
