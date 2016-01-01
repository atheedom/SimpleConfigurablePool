package com.alextheedom;

import com.alextheedom.pool.JsonParserFixedPool;
import com.alextheedom.pool.PoolDepletionException;

/**
 *
 */
public class App {
    public static void main( String[] args ) throws InterruptedException, PoolDepletionException {


        JsonParserFixedPool jsonParserFixedPool = new JsonParserFixedPool(20);

        jsonParserFixedPool.borrowObject();



    }
}
