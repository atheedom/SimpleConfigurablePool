package com.alextheedom.application;

import com.alextheedom.pool.FlexibleObjectPool;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

/**
 * Creates a pool of BOON JSON Parsers
 */
public class JsonParserFlexiblePool extends FlexibleObjectPool<JsonParserAndMapper> {

    public JsonParserFlexiblePool() {
        super(20);
    }

    public JsonParserFlexiblePool(int poolSize) {
        super(poolSize);
    }

    public JsonParserFlexiblePool(int poolSize, int pollTimeout) {
        super(poolSize, pollTimeout, 0);
    }

    public JsonParserFlexiblePool(int poolSize, int minIdle, int maxIdle, int validationInterval) {
        super( minIdle,  maxIdle,  validationInterval);
    }

    public JsonParserFlexiblePool(int poolSize, int pollTimeout, FlexiblePoolConfig flexiblePoolConfig) {
        super(flexiblePoolConfig);
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
