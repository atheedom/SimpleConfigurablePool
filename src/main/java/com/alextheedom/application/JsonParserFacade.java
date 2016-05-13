package com.alextheedom.application;


import com.alextheedom.pool.AbstractObjectPool;
import com.alextheedom.pool.PoolDepletionException;
import com.alextheedom.pool.PoolStatusException;
import org.boon.json.JsonParserAndMapper;

import java.util.List;

/**
 * A facade providing access to the boon pool
 * <p/>
 * Created by atheedom on 27/11/2014.
 */
public class JsonParserFacade extends AbstractFacade<JsonParserAndMapper> {

    private AbstractObjectPool<JsonParserAndMapper> jsonParserFixedPool;

    public JsonParserFacade() {
        jsonParserFixedPool = new JsonParserFixedPool(100);
    }

    public JsonParserFacade(int poolSize) {
        jsonParserFixedPool = new JsonParserFixedPool(poolSize);
    }

    /**
     * Parses a JSON String to the given class.
     *
     * @param type  the type
     * @param value the JSON to parse
     * @param <T>   the return type
     * @return the parsed object
     */
    public synchronized final <T> T parse(Class<T> type, String value) throws InterruptedException, PoolStatusException, PoolDepletionException {

        JsonParserAndMapper jsonParserAndMapper = null;
        T obj;
        try {
            jsonParserAndMapper = jsonParserFixedPool.acquire();
            obj = jsonParserAndMapper.parse(type, value);
        } finally {
            if (jsonParserAndMapper != null) {
                jsonParserFixedPool.surrender(jsonParserAndMapper);
            }
        }

        return obj;
    }


    /**
     * Parse a JSON String to a list of objects.
     *
     * @param type  the type
     * @param value the JSON to parse
     * @param <T>   the return type
     * @return the parsed list of objects
     */
    public synchronized final <T> List<T> parseList(Class<T> type, String value) throws InterruptedException, PoolStatusException, PoolDepletionException {

        JsonParserAndMapper jsonParserAndMapper = null;
        List<T> obj;
        try {
            jsonParserAndMapper = jsonParserFixedPool.acquire();
            obj = jsonParserAndMapper.parseList(type, value);
        } finally {
            if (jsonParserAndMapper != null) {
                jsonParserFixedPool.surrender(jsonParserAndMapper);
            }
        }

        return obj;
    }


    /**
     * Returns the current JSON parser pool
     *
     * @return the JSON parser fixed pool instance
     */
    public AbstractObjectPool<JsonParserAndMapper> getJsonParserFixedPool() {
        return jsonParserFixedPool;
    }


    /**
     * Destroys the parser pool.
     * <p/>
     * NOTE: This method will not shutdown the executor.
     */
    public void destroyPool() {
        if (jsonParserFixedPool != null) {
            jsonParserFixedPool.destroyPool();
            jsonParserFixedPool = null;
        }
    }


    /**
     * Changes the size of the pool to the given size
     *
     * @param newPoolSize the size of the new pool
     * @throws PoolStatusException thrown if pool not ready or shutting down
     */
    public void changePoolSize(int newPoolSize) throws PoolStatusException {
        super.changePoolSize(newPoolSize, jsonParserFixedPool);
    }

}
