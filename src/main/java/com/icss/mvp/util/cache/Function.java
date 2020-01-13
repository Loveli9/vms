package com.icss.mvp.util.cache;

/**
 * Created by Ray on 2018/11/21.
 */
public interface Function<E, T> {

    T callback(E e);
}
