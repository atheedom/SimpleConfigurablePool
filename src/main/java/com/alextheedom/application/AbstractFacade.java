package com.alextheedom.application;

import com.alextheedom.pool.AbstractObjectPool;
import com.alextheedom.pool.PoolStatusException;

/**
 * Created by atheedom on 13/05/2016.
 */
abstract class AbstractFacade<T> {

    /**
     * Change the size of a pool.
     * <p/>
     * Note that if the pool size is to be reduced and this method is called at run time it is possible that the final
     * pool size achieved is more than the poolSize required. This is because some objects
     * in the pool have been polled and are currently in use. If the remaining objects in the pool number less
     * than the reduction required only those in the pool will be removed.
     *
     * @param newPoolSize new pool size
     */
    public void changePoolSize(int newPoolSize, AbstractObjectPool<T> pool) throws PoolStatusException {

        if (pool != null) {
            int size = pool.getCurrentPoolSize();
            if (size < newPoolSize) {
                int sizeToBeAdded = newPoolSize - size;
                for (int i = 0; i < sizeToBeAdded; i++) {
                    pool.add();
                }
            } else if (size > newPoolSize) {
                int sizeToBeRemoved = size - newPoolSize;

                for (int i = 0; i < sizeToBeRemoved; i++) {
                    pool.destroy();
                }
            }
        }
    }
}
