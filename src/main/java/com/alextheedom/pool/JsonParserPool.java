package com.alextheedom.pool;

import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 */
public class JsonParserPool extends AbstractObjectPool<JsonParserAndMapper> {

    private LinkedBlockingQueue<JsonParserAndMapper> pool = new LinkedBlockingQueue<>();
    private int poolSize = 20;
    private int pollTimeout = 3000;

    public JsonParserPool() {
        initialize(poolSize, pool);
    }

    public JsonParserPool(int poolSize) {
        initialize(poolSize, pool);
    }

    @Override
    public JsonParserAndMapper borrowObject() throws PoolDepletionException, InterruptedException {
        JsonParserAndMapper jsonParserAndMapper = pool.poll(this.pollTimeout, TimeUnit.MILLISECONDS);

        if (jsonParserAndMapper == null){
            throw new PoolDepletionException("The JSON parser pool is empty and was not replenished within timeout limits.");
        }

        return jsonParserAndMapper;
    }

    @Override
    protected JsonParserAndMapper createObject() {
        return new JsonParserFactory().useAnnotations().usePropertiesFirst().create();
    }

}
