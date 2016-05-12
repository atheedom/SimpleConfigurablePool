package com.alextheedom.application;


import com.alextheedom.pool.PoolDepletionException;
import org.boon.json.JsonParserAndMapper;

import java.util.List;

/**
 * A facade providing access to the boon pool
 * <p/>
 * Created by atheedom on 27/11/2014.
 */
public class JsonParserFacade {

    private JsonParserFixedPool jsonParserPool; // was JsonParserPool

    public JsonParserFacade() {
        jsonParserPool = new JsonParserFixedPool(100);
    }

    public JsonParserFacade(int poolSize) {
        jsonParserPool = new JsonParserFixedPool(poolSize);
    }

    /**
     * Parses a JSON String to the given class.
     *
     * @param type  the type
     * @param value the JSON to parse
     * @param <T>   the return type
     * @return the parsed object
     */
    public synchronized final <T> T parse(Class<T> type, String value) throws Exception {

        JsonParserAndMapper jsonParserAndMapper = null;
        T obj = null;
        try {
            jsonParserAndMapper = jsonParserPool.acquire();
            obj = jsonParserAndMapper.parse(type, value);
        } catch (PoolDepletionException e) {

        } catch (Exception e) {
            throw e;
        } finally {
            if (jsonParserAndMapper != null) {
                jsonParserPool.surrender(jsonParserAndMapper);
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
    public synchronized final <T> List<T> parseList(Class<T> type, String value) throws Exception {

        JsonParserAndMapper jsonParserAndMapper = null;
        List<T> obj = null;
        try {
            jsonParserAndMapper = jsonParserPool.acquire();
            obj = jsonParserAndMapper.parseList(type, value);
        } catch (PoolDepletionException e) {

        } catch (Exception e) {

            throw e;
        } finally {
            if (jsonParserAndMapper != null) {
                jsonParserPool.surrender(jsonParserAndMapper);
            }
        }

        return obj;
    }


    /**
     * Returns the current JSON parser pool
     *
     * @return
     */
    public JsonParserFixedPool getJsonParserPool() {
        return jsonParserPool;
    }


    /**
     * Destroys the parser pool.
     * <p/>
     * NOTE: This method will not shutdown the executor.
     */
    public void destroyPool() {
        jsonParserPool.destroyPool();
        jsonParserPool = null;
    }


    /**
     * Change the size of a pool.
     * <p/>
     * Note that if the pool size is to be reduced and this method is called at run time it is possible that the final
     * pool size achieved is more than the poolSize required. This is because some objects
     * in the pool have been polled and are currently in use. If the remaining objects in the pool number less
     * than the reduction required only those in the pool will be removed.
     *
     * @param newPoolSize new pool size
     */
    public void changePoolSize(int newPoolSize) throws Exception {

        if (jsonParserPool != null) {
            int size = jsonParserPool.getCurrentPoolSize();
            if (size < newPoolSize) {
                int sizeToBeAdded = newPoolSize - size;
                for (int i = 0; i < sizeToBeAdded; i++) {
                    jsonParserPool.add();
                }
            } else if (size > newPoolSize) {
                int sizeToBeRemoved = size - newPoolSize;

                for (int i = 0; i < sizeToBeRemoved; i++) {
                    jsonParserPool.destroy();
                }
            }
        }
    }
}
