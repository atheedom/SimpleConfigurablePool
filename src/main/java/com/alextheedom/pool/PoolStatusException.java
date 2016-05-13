package com.alextheedom.pool;

/**
 * Created by atheedom on 13/05/2016.
 */
public class PoolStatusException extends Exception   {

    private static final String MESSAGE = "Pool not ready or stopped";

    public PoolStatusException() {
        super(MESSAGE);
    }

    public PoolStatusException(String message) {
        super(message);
    }


}
