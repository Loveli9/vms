package com.icss.mvp.entity.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.icss.mvp.entity.common.request.BaseRequest;

/**
 * Created by Ray on 2018/8/28.
 */
public class RepositoryRequest extends BaseRequest {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    @JSONField(label = "id")
    private String            projectId;

    @JSONField(label = "id")
    private int               repositoryId;

    /**
     * repository address url
     */
    private String            url;

    /**
     * login account
     */
    private String            account;

    /**
     * login certificate
     */
    private String            secret;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }
}
