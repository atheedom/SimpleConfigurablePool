package com.alextheedom.pool;

/**
 * Created by atheedom on 03/01/2016.
 */
public interface Pool<T> {

    T borrow() throws Exception;
    void returnObject(T item) throws Exception;

}