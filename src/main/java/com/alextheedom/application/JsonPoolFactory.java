package com.alextheedom.application;

/**
 * Created by atheedom on 27/11/2014.
 */
public class JsonPoolFactory {

    private static JsonParserFacade jsonParserFacade;

    /**
     * Returns an instance of the json parser facade.
     * <p>
     * The first call constructs the facade and subsequent calls returns
     * a reference to the previously created.
     *
     * @return JsonParserFacade instance
     */
    public static JsonParserFacade getParserFacadeInstance() {

        if (jsonParserFacade == null) {
            synchronized (JsonPoolFactory.class) {
                if (jsonParserFacade == null) {
                    jsonParserFacade = new JsonParserFacade();
                }
            }
        }
        return jsonParserFacade;
    }

    /**
     * Returns an instance of the json parser facade with pool of the given size.
     * If the pool size is different from the pool's current size the pool is distroyed
     * and recreated.
     * <p>
     * The first call constructs the facade and subsequent calls returns
     * a reference to the previously created, as long as the pool size has not changed.
     *
     * @param newPoolSize pool size
     * @return JsonParserFacade instance
     */
    public static JsonParserFacade getParserFacadeInstance(int newPoolSize) throws Exception {

        if (jsonParserFacade == null || jsonParserFacade.getJsonParserFixedPool().getCurrentPoolSize() != newPoolSize) {
            synchronized (JsonPoolFactory.class) {
                if (jsonParserFacade == null || jsonParserFacade.getJsonParserFixedPool().getCurrentPoolSize() != newPoolSize) {
                    if (jsonParserFacade != null) {
                        jsonParserFacade.changePoolSize(newPoolSize);
                    } else {
                        jsonParserFacade = new JsonParserFacade(newPoolSize);
                    }
                }
            }
        }
        return jsonParserFacade;
    }





    /**
     * Destroys the pool and sets the parser facade to null
     * <p>
     * This method should be called if the pool is to be reinitialised with a new pool
     * of a different size.
     */
    public static void destroyParserFacadeInstance() {
        jsonParserFacade.destroyPool();
        jsonParserFacade = null;
    }
}

