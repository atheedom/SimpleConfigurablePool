package com.alextheedom.application;

import com.alextheedom.pool.AbstractObjectPool;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Creates a pool of BOON JSON Parsers
 */
public class JsonParserFixedPool extends AbstractObjectPool<JsonParserAndMapper> {

    public JsonParserFixedPool(int poolSize, int poolTimeout, LinkedBlockingQueue<JsonParserAndMapper> pool) {
        super(poolSize, poolTimeout, pool);
    }

    public JsonParserFixedPool(int poolSize) {
        super(poolSize);
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
