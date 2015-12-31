/*
 * Copyright (C) Indigo Code Collective - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Indigo Code Collective, 2014
 */

package com.alextheedom.pool;


import java.util.AbstractQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 */
public abstract class AbstractObjectPool<T> implements ObjectPool{

    private AbstractQueue<T> pool;
    private ScheduledExecutorService executorService;

    public AbstractObjectPool() {
    }


    /**
     * Initialise the pool and populate it with poolSize number of objects
     *
     * @param poolSize the size of object to initialise the pool
     */
    protected void initialize(int poolSize, AbstractQueue<T> pool) {
        setPool(pool);
        for (int i = 0; i < poolSize; i++) {
            pool.add(createObject());
        }
    }

    /**
     * Gets the pool
     *
     * @return AbstractQueue pool object
     */
    public AbstractQueue<T> getPool() {
        return pool;
    }


    /**
     * Sets the pool.
     *
     * @param pool the pool to set
     */
    private void setPool(AbstractQueue<T> pool) {
        this.pool = pool;
    }


    /**
     * Gets the next free object from the pool.
     *
     * Different strategies can be implemented to deal with a
     * situation where the pool doesn't contain any objects.
     *
     * Some possible options:
     *
     * 1. a new object will be created and given to the caller of this method.
     * 2. a PoolDepletionException is thrown
     * 3. wait for a specified time for an object to be returned
     *
     * @return T borrowed object
     */
    public abstract T borrowObject() throws PoolDepletionException, InterruptedException;


    /**
     * Returns object back to the pool.
     *
     * Possible implementation may include code to clean/reset the
     * object to initial values.
     *
     * @param object object to be returned
     */
    public void returnObject(T object) {
        if (object == null) {
            return;
        }
        this.pool.offer(object);
    }


    /**
     * Creates a new object.
     *
     * @return T new object
     */
    protected abstract T createObject();


    /**
     * Destroys an object.
     *
     * @param pool the pool to destroy
     */
    protected void destroyObject(AbstractQueue<T> pool){
        T object = pool.poll();
        object = null;
    }

    /**
     * Shutdown this pool's executor service and deletes the queue pool.
     *
     * NOTE: This method should be overridden in the subclass as the executor reference
     * will be null
     */
    public void shutdown() {

        // Destroys the entire pool.
        destroyPool();

        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    /**
     * Destroys the entire pool.
     */
    public void destroyPool() {
        while (!pool.isEmpty()) {
            destroyObject(pool);
        }
        pool = null;
    }

}

