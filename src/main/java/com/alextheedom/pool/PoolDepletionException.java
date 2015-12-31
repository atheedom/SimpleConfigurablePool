package com.alextheedom.pool;

/**
 * Created by atheedom on 29/06/15.
 */
public class PoolDepletionException extends Exception   {

    private static final String MESSAGE = "The pool has been depleted";

    public PoolDepletionException() {
        super(MESSAGE);
    }

    public PoolDepletionException(String message) {
        super(message);
    }


}
