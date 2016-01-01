package com.alextheedom;

import com.alextheedom.pool.JsonParserFixedPool;
import com.alextheedom.pool.PoolDepletionException;
import org.boon.json.JsonParserAndMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by atheedom on 31/12/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class FixedPoolTest {

    private JsonParserFixedPool jsonParserFixedPool;

    @After
    public void teardown(){
        jsonParserFixedPool.destroyPool();
    }

    @Test
    public void ShouldInitialiseWith20Objects(){

        // arrange and act
        jsonParserFixedPool = new JsonParserFixedPool(20);

        // assert
        assertThat(jsonParserFixedPool.getPool().size()).isEqualTo(20);
    }


    @Test
    public void ShouldThrowDepletionException() throws PoolDepletionException, InterruptedException {

        // arrange
        jsonParserFixedPool = new JsonParserFixedPool(1);

        // act
        jsonParserFixedPool.borrowObject();

        // assert
        assertThatThrownBy(jsonParserFixedPool::borrowObject).isInstanceOf(PoolDepletionException.class);
    }

    @Test
    public void ShouldReturnObjectToPool() throws InterruptedException, PoolDepletionException {

        // arrange
        jsonParserFixedPool = new JsonParserFixedPool(10);

        // act
        JsonParserAndMapper jsonParserAndMapper = jsonParserFixedPool.borrowObject();
        jsonParserFixedPool.returnObject(jsonParserAndMapper);

        // assert
        assertThat(jsonParserFixedPool.getPool().size()).isEqualTo(10);
    }


    @Test
    public void ShouldDestroyPool(){

        // arrange
        jsonParserFixedPool = new JsonParserFixedPool(1);

        // act
        jsonParserFixedPool.destroyPool();

        // assert
        assertThat(jsonParserFixedPool.getPool()).isNullOrEmpty();
    }




}
