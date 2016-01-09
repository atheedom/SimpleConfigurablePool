package com.alextheedom.pool;

/**
 * Created by atheedom on 03/01/2016.
 */
public interface Pool<T> {

    T acquire() throws Exception;
    void surrender(T item) throws Exception;

}