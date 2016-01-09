package com.alextheedom.application;

import com.alextheedom.pool.AbstractObjectPool;
import com.alextheedom.pool.PoolDepletionException;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

import java.util.function.Supplier;

/**
 * Creates a pool of BOON JSON Parsers
 */
public class JsonParserFixedPool extends AbstractObjectPool<JsonParserAndMapper> {

    private static Supplier<JsonParserAndMapper> createJSONParserAndMapper() {
        return () -> new JsonParserFactory().useAnnotations().usePropertiesFirst().create();
    }

    public JsonParserFixedPool(int poolSize, int poolTimeout) {
        super(createJSONParserAndMapper(), poolSize, poolTimeout);
    }

    public JsonParserFixedPool(int poolSize) {
        super(createJSONParserAndMapper(), poolSize);
    }

    protected JsonParserAndMapper handleDepletion(Supplier<JsonParserAndMapper> supplier) throws PoolDepletionException {
        return supplier.get();
    }

}
