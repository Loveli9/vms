package com.icss.mvp.entity;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Ray on 2018/5/8.
 */
public class CommitInfo {

    // /**
    // * default serial version UID
    // */
    // private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String               id;

    private Date                 createTime;

    private Date                 modifyTime;

    private boolean              isDeleted;

    private int                  repositoryId;

    private String               repositoryUrl;

    private String               version;

    private String               author;

    private Date                 commitTime;

    private String               message;

    private Set<String>          affectedPaths;

    private int                  status;

    private Map<String, Integer> statistics;

    private String               jsonStatistics;

    private int                  modifyLineCount;

    private int                  deleteLineCount;

    public String getId() {
        if (StringUtils.isBlank(id)) {
            id = (UUID.randomUUID()).toString();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<String> getAffectedPaths() {
        return affectedPaths;
    }

    public void setAffectedPaths(Set<String> affectedPaths) {
        this.affectedPaths = affectedPaths;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<String, Integer> statistics) {
        this.statistics = statistics;
    }

    public String getJsonStatistics() {
        return jsonStatistics;
    }

    public void setJsonStatistics(String jsonStatistics) {
        this.jsonStatistics = jsonStatistics;
    }

    public int getModifyLineCount() {
        if (statistics != null && !statistics.isEmpty()) {
            int sum = 0;
            for (Integer entry : statistics.values()) {
                sum += entry != null ? entry : 0;
            }
            modifyLineCount = sum;
        }
        return modifyLineCount;
    }

    public void setModifyLineCount(int modifyLineCount) {
        this.modifyLineCount = modifyLineCount;
    }

    public int getDeleteLineCount() {
        return deleteLineCount;
    }

    public void setDeleteLineCount(int deleteLineCount) {
        this.deleteLineCount = deleteLineCount;
    }
}
