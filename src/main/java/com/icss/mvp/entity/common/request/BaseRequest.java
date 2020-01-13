package com.icss.mvp.entity.common.request;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.icss.mvp.entity.common.BaseEntity;

public class BaseRequest extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JSONField(label = "essential")
    private String            requestId;

    private String            userId;

    public String getRequestId() {
        if (StringUtils.isBlank(requestId)) {
            requestId = (UUID.randomUUID()).toString();
        }
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
