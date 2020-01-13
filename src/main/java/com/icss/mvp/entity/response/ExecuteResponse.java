package com.icss.mvp.entity.response;

import java.util.HashMap;
import java.util.Map;

import com.icss.mvp.entity.common.BaseEntity;
import org.apache.http.HttpStatus;

/**
 * Created by Ray on 2018/12/4.
 */
public class ExecuteResponse extends BaseEntity {

    /**
     * default serial version UID
     */
    private static final long   serialVersionUID = 1L;

    private int                 statusCode;

    private String              httpEntity;

    private Map<String, String> cookieStore;
    
    public ExecuteResponse(){
        this.statusCode = HttpStatus.SC_OK;
        this.httpEntity = "";
        this.cookieStore = new HashMap<>();
    }
    
    public boolean isSucceed() {
        return HttpStatus.SC_OK == statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHttpEntity() {
        return httpEntity;
    }

    public void setHttpEntity(String httpEntity) {
        this.httpEntity = httpEntity;
    }

    public Map<String, String> getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(Map<String, String> cookieStore) {
        this.cookieStore = cookieStore;
    }
}
