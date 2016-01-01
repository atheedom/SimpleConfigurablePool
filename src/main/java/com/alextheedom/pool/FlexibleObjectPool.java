package com.alextheedom.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by atheedom on 31/12/2015.
 */
public abstract class FlexibleObjectPool<T> extends AbstractObjectPool<T> {

    private ScheduledExecutorService executorService;

    protected FlexibleObjectPool() {
        provokePoolMonitor(10, 20, 3000);
    }

    protected FlexibleObjectPool(int minIdle, int maxIdle, int validationInterval) {
        provokePoolMonitor(minIdle, maxIdle, validationInterval);
    }
    

    /**
     * Checks pool conditions in a separate thread.
     * <p/>
     * If pool size rises to above maxIdle then the number of objects
     * deleted is equal to the excess over the minIdle.
     * <p/>
     * If pool size drops below minIdle new objects are created to
     * bring it up to the minIdle level.
     *
     * @param minIdle            minimum number of objects idling in the object pool
     * @param maxIdle            maximum number of objects idling in the object pool
     * @param validationInterval time in milli-seconds for periodical checking of minIdle / maxIdle conditions in a separate thread.
     *                           When the number of objects is less than minIdle, missing instances will be created.
     *                           When the number of objects is greater than maxIdle, excess instances will be removed.
     */
    protected void provokePoolMonitor(final int minIdle, final int maxIdle, int validationInterval) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> {
            int size = getPool().size();
            if (size < minIdle) {
                int sizeToBeAdded = minIdle - size;
                for (int i = 0; i < sizeToBeAdded; i++) {
                    getPool().add(createObject());
                }
            } else if (size > maxIdle) {
                int sizeToBeRemoved = size - maxIdle;
                for (int i = 0; i < sizeToBeRemoved; i++) {
                    destroyObject(getPool());
                }
            }

        }, validationInterval, validationInterval, TimeUnit.MILLISECONDS);

    }


    /**
     * Shutdown this pool's executor service and deletes the object pool.
     */
    public void destroyPool() {

        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }

        // Destroys the entire pool.
        super.destroyPool();
    }

}
