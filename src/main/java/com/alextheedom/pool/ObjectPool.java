package com.alextheedom.pool;

/**
 * Created by atheedom on 31/12/2015.
 */
@FunctionalInterface
public interface ObjectPool<T> {

    T borrowObject() throws PoolDepletionException, InterruptedException;

}
