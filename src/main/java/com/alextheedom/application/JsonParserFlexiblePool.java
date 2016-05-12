package com.alextheedom.application;

import com.alextheedom.pool.FlexibleObjectPool;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;

import java.util.function.Supplier;

/**
 * Creates a pool of BOON JSON Parsers
 */
public class JsonParserFlexiblePool extends FlexibleObjectPool<JsonParserAndMapper> {

    private static Supplier<JsonParserAndMapper> createJSONParserAndMapper() {
        return () -> new JsonParserFactory().useAnnotations().usePropertiesFirst().create();
    }

    protected JsonParserFlexiblePool() {
        super(createJSONParserAndMapper(), 20);
    }

    public JsonParserFlexiblePool(int poolSize) {
        super(createJSONParserAndMapper(), poolSize);
    }

    public JsonParserFlexiblePool(int poolSize, int pollTimeout) {
        super(createJSONParserAndMapper(), poolSize, pollTimeout, 0);
    }

    public JsonParserFlexiblePool(int minIdle, int maxIdle, int validationInterval) {
        super(createJSONParserAndMapper(), minIdle, maxIdle, validationInterval);
    }

    public JsonParserFlexiblePool(FlexiblePoolConfig flexiblePoolConfig) {
        super(createJSONParserAndMapper(), flexiblePoolConfig);
    }


}
