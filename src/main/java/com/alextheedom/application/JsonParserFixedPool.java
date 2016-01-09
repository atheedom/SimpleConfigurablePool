package com.alextheedom.application;

import com.alextheedom.pool.AbstractObjectPool;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

/**
 * Creates a pool of BOON JSON Parsers
 */
public class JsonParserFixedPool extends AbstractObjectPool<JsonParserAndMapper> {

    public JsonParserFixedPool(int poolSize, int poolTimeout) {
        super(poolSize, poolTimeout);
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
