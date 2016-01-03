package com.alextheedom;

import com.alextheedom.pool.JsonParserFixedPool;

/**
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {


        JsonParserFixedPool jsonParserFixedPool = new JsonParserFixedPool(20);

        jsonParserFixedPool.borrow();



    }
}
