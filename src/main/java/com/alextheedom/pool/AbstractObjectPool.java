/*
 * Copyright (C) Indigo Code Collective - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Indigo Code Collective, 2014
 */

package com.alextheedom.pool;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * An abstract class to be implemented by an object pool
 */
public abstract class AbstractObjectPool<T> implements Pool<T> {

    public enum PoolState {
        STARTING, STARTED, STOPPING, STOPPED
    }

    private PoolState currentStatus;
    private BlockingQueue<T> pool= new LinkedBlockingQueue<>();
    private int pollTimeout;
    private final int poolSize;
    private final Supplier<T> supplier;

    protected AbstractObjectPool(Supplier<T> supplier) {
        this.supplier = supplier;
        this.poolSize = 100;
        initialize();
    }

    protected AbstractObjectPool(Supplier<T> supplier, int poolSize) {
        this.supplier = supplier;
        this.poolSize = poolSize;
        initialize();
    }

    protected AbstractObjectPool(Supplier<T> supplier, int poolSize, int pollTimeout) {
        this.supplier = supplier;
        this.pollTimeout = pollTimeout;
        this.poolSize = poolSize;
        initialize();
    }


    /**
     * Initialise the pool and populate it with poolSize number of objects
     */
    private void initialize() {
        updatePoolStatus(PoolState.STARTING);
        if (poolSize < 1) {
            throw new IllegalArgumentException("Pool Size must be at least 1");
        }
        while (pool.size() < poolSize) {
            pool.add(supplier.get());
        }
        updatePoolStatus(PoolState.STARTED);
    }

    /**
     * Sets the pools current status
     *
     * @param currentStatus the pool's current status
     */
    private void updatePoolStatus(PoolState currentStatus) {
        this.currentStatus = currentStatus;
    }


    /**
     * Returns the current pool size
     *
     * @return number of objects in the pool
     */
    public final int getCurrentPoolSize() {
        return pool.size();
    }

    /**
     * Is the pool null
     *
     * @return true if null otherwise false
     */
    public final boolean isPoolNull() {
        return pool == null;
    }


    /**
     * Returns the status of the current pool.
     *
     * @return the current pool status
     */
    public final PoolState poolState() {
        return this.currentStatus;
    }


    /**
     * Gets the next free object from the pool.
     * <p/>
     * Different strategies can be implemented to deal with a
     * situation where the pool doesn't contain any objects.
     * <p/>
     * Some possible options:
     * <p/>
     * 1. a new object will be created and given to the caller of this method.
     * 2. a PoolDepletionException is thrown
     * 3. wait for a specified time for an object to be returned
     *
     * @return T borrowed object
     * @throws PoolDepletionException thrown if the pool has been depleted
     * @throws InterruptedException
     */
    public final T acquire() throws Exception {
        checkPoolStatus();
        T t = pool.poll(pollTimeout, TimeUnit.MILLISECONDS);

        if (t == null) {
            return handleDepletion(supplier);
        }

        return t;
    }


    /**
     * Returns object back to the pool.
     * <p/>
     * Possible implementation may include code to clean/reset the
     * object to initial values.
     *
     * @param object object to be returned
     */
    public final void surrender(T object) throws Exception {
        checkPoolStatus();
        if (object == null) {
            return;
        }
        this.pool.offer(object);
    }


    /**
     * Adds object to the pool.
     *
     * @return true if added successfully otherwise false
     */
    public final boolean add() throws Exception {
        checkPoolStatus();
        return pool.add(supplier.get());
    }

    /**
     * What to do if there's nothing to return within the expected time. options
     * are to create an instance manually, or alternatively throw an exception.
     * returning null would be a bad idea, though.
     *
     * @param supplier
     *            the supplier that feeds this pool - can be used to get a
     *            guaranteed, but slow instance.
     * @return the value for the client to use
     * @throws PoolDepletionException
     *             if that is how the pool instance should respond.
     */
    protected T handleDepletion(Supplier<T> supplier) throws PoolDepletionException{
        throw new PoolDepletionException("The pool is empty and was not replenished within timeout limits.");
    }

    /**
     * Checks that the pool is running and ready for use otherwise it throws an exception
     *
     * @throws Exception thrown if pool not ready or shutting down
     */
    private void checkPoolStatus() throws Exception {
        if (poolState() != PoolState.STARTED) throw new Exception("Pool not ready or stopped");
    }

    /**
     * Destroys an object.
     */
    public void destroy() {
       pool.poll();
    }


    /**
     * Destroys the entire pool.
     */
    public void destroyPool() {
        updatePoolStatus(PoolState.STOPPING);
        while (pool != null && !pool.isEmpty()) {
            destroy();
        }
        pool = null;
        updatePoolStatus(PoolState.STOPPED);
    }

}

