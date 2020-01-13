package com.icss.mvp.entity.common.response;

/**
 * @NAME:
 * @Author: wwx550362
 * @Date: 2019/12/12 17:12
 * @Version 1.0
 */
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Result<T> implements Serializable {
    protected boolean success = false;
    protected String result;
    protected String errorsList;
    protected T data;
    protected final List<String> errors = new ArrayList(0);
    protected final List<String> messages = new ArrayList(0);
    protected Exception exception = null;

    public Result() {
        this.success = true;
    }

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String result) {
        this.success = success;
        this.result = result;
    }

    public Result(boolean success, String result, T data) {
        this.success = success;
        this.result = result;
        this.data = data;
    }

    public String getErrorsList() {
        return errorsList;
    }

    public void setErrorsList(String errorsList) {
        this.errorsList = errorsList;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Result(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void addErrors(List<String> errors) {
        this.errors.addAll(errors);
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void addMessages(List<String> messages) {
        this.messages.addAll(messages);
    }

    public String getException() {
        try {
            if (this.exception == null) {
                return null;
            } else {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                this.exception.printStackTrace(pw);
                return sw.toString();
            }
        } catch (Exception var3) {
            return "bad getErrorInfoFromException";
        }
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
