package com.alextheedom;

import com.alextheedom.application.JsonParserFixedPool;
import com.alextheedom.pool.PoolDepletionException;
import org.boon.json.JsonParserAndMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by atheedom on 31/12/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonParserFixedPool.class})
public class FixedPoolTest {

    private JsonParserFixedPool jsonParserFixedPool;

    @After
    public void teardown() {
        jsonParserFixedPool.destroyPool();
    }

    @Test
    public void ShouldInitialiseWith20Objects() {

        // arrange and act
        jsonParserFixedPool = new JsonParserFixedPool(20, 3000);

        // assert
        assertThat(jsonParserFixedPool.getCurrentPoolSize()).isEqualTo(20);
    }


    @Test
    public void ShouldThrowPoolDepletionException() throws Exception {

        // arrange
        jsonParserFixedPool = new JsonParserFixedPool(1);

        // act
        jsonParserFixedPool.acquire(); // Retrieves the only instance from the queue

        // assert
        assertThatThrownBy(jsonParserFixedPool::acquire).isInstanceOf(PoolDepletionException.class);
    }

    @Test
    public void ShouldReturnObjectToPool() throws Exception {

        // arrange
        jsonParserFixedPool = new JsonParserFixedPool(10);

        // act
        JsonParserAndMapper jsonParserAndMapper = jsonParserFixedPool.acquire();
        jsonParserFixedPool.surrender(jsonParserAndMapper);

        // assert
        assertThat(jsonParserFixedPool.getCurrentPoolSize()).isEqualTo(10);
    }


    @Test
    public void ShouldDestroyPool() {

        // arrange
        jsonParserFixedPool = new JsonParserFixedPool(1);

        // act
        jsonParserFixedPool.destroyPool();

        // assert
        assertThat(jsonParserFixedPool.isPoolNull()).isTrue();
    }


    @Test
    public void ShouldCheckPoolStatus() throws Exception {

        // arrange
        jsonParserFixedPool = new JsonParserFixedPool(1);

        // act
        Whitebox.invokeMethod(jsonParserFixedPool, "checkPoolStatus");

        // assert
        assertThat(true).isTrue();
    }


}
