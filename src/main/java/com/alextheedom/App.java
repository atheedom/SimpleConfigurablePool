package com.alextheedom;

import com.alextheedom.pool.JsonParserPool;
import com.alextheedom.pool.PoolDepletionException;

/**
 *
 */
public class App {
    public static void main( String[] args ) throws InterruptedException, PoolDepletionException {


        JsonParserPool jsonParserPool = new JsonParserPool(20);

        jsonParserPool.borrowObject();



    }
}
