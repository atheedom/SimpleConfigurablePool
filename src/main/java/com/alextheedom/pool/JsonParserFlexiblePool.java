package com.alextheedom.pool;

import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Creates a pool of BOON JSON Parsers
 */
public class JsonParserFlexiblePool extends FlexibleObjectPool<JsonParserAndMapper> {

    private LinkedBlockingQueue<JsonParserAndMapper> pool = new LinkedBlockingQueue<>();
    private int pollTimeout = 3000;


    public JsonParserFlexiblePool() {
        initialize(20, pool);
    }

    public JsonParserFlexiblePool(int poolSize) {
        initialize(poolSize, pool);
    }

    public JsonParserFlexiblePool(int poolSize, int pollTimeout) {
        initialize(poolSize, pool);
        this.pollTimeout = pollTimeout;
    }

    public JsonParserFlexiblePool(int poolSize, int minIdle, int maxIdle, int validationInterval) {
        super( minIdle,  maxIdle,  validationInterval);
        initialize(poolSize, pool);
    }

    public JsonParserFlexiblePool(int poolSize, int pollTimeout, FlexiblePoolConfig flexiblePoolConfig) {
        super(flexiblePoolConfig);
        initialize(poolSize, pool);
        this.pollTimeout = pollTimeout;
    }

    /**
     * Borrow one BOON JSON parser object from the pool.
     *
     * @return returns one BOON JSON parser object
     * @throws PoolDepletionException thrown if there are no BOON JSON parser objects in the pool
     * @throws InterruptedException
     */
    @Override
    public JsonParserAndMapper borrowObject() throws PoolDepletionException, InterruptedException {
        JsonParserAndMapper jsonParserAndMapper = pool.poll(this.pollTimeout, TimeUnit.MILLISECONDS);

        if (jsonParserAndMapper == null) {
            throw new PoolDepletionException("The JSON parser pool is empty and was not replenished within timeout limits.");
        }

        return jsonParserAndMapper;
    }

    /**
     * Creates a BOON JSON parser object.
     *
     * @return a new BOON JSON parser object
     */
    @Override
    protected JsonParserAndMapper createObject() {
        return new JsonParserFactory().useAnnotations().usePropertiesFirst().create();
    }

}
