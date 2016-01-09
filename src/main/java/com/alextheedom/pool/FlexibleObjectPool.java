package com.alextheedom.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by atheedom on 31/12/2015.
 */
public abstract class FlexibleObjectPool<T> extends AbstractObjectPool<T> {

    private ScheduledExecutorService executorService;

    private float poolSizeFactor = 0.8f;

    /**
     * Construct a flexible pool with default configuration values.
     *
     * @param supplier the supplier that creates a new instance of the object
     * @param poolSize the initial size of the pool
     */
    public FlexibleObjectPool(Supplier<T> supplier, int poolSize) {
        super(supplier, poolSize);
        int minIdle = (int) (poolSize * poolSizeFactor);
        int maxIdle = (int) (poolSize * (1 + (1 - poolSizeFactor)));
        provokePoolMonitor(minIdle, maxIdle, 3000);
    }

    /**
     * Construct a flexible pool.
     * <p/>
     * Set starting pool size equal to the average of the minIdle and maxIdle.
     *
     * @param supplier           the supplier that creates a new instance of the object
     * @param minIdle            the minimum pool size that the monitor should attempt to maintain
     * @param maxIdle            the maximum pool size that the monitor should attempt to maintain
     * @param validationInterval the interval at which to fire the monitor
     */
    public FlexibleObjectPool(Supplier<T> supplier, int minIdle, int maxIdle, int validationInterval) {
        super(supplier, (maxIdle + minIdle) / 2);
        provokePoolMonitor(minIdle, maxIdle, validationInterval);
    }

    public FlexibleObjectPool(Supplier<T> supplier, FlexiblePoolConfig config) {
        super(supplier, (config.maxIdle + config.minIdle) / 2);
        provokePoolMonitor(config.minIdle, config.maxIdle, config.validationInterval);
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
        if (minIdle < 1 || minIdle > maxIdle) {
            throw new IllegalArgumentException("minIdle must be at least 1, and at least equal to maxIdle");
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> {
            int size = getCurrentPoolSize();
            if (size < minIdle) {
                int sizeToBeAdded = minIdle - size;
                for (int i = 0; i < sizeToBeAdded; i++) {
                    try {
                        add();
                    } catch (Exception e) {
                        break;
                    }
                }
            } else if (size > maxIdle) {
                int sizeToBeRemoved = size - maxIdle;
                for (int i = 0; i < sizeToBeRemoved; i++) {
                    destroy();
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


    public class FlexiblePoolConfig {

        protected final int minIdle;
        protected final int maxIdle;
        protected final int validationInterval;

        public FlexiblePoolConfig(final int minIdle, final int maxIdle, final int validationInterval) {
            this.minIdle = minIdle;
            this.maxIdle = maxIdle;
            this.validationInterval = validationInterval;
        }

    }
}
