package com.icss.mvp.entity;

public class PlainRequest<T> {

    /**
     * default serial version UID
     */
    private static final long serialVersionUID = 1L;

    private T                 data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
