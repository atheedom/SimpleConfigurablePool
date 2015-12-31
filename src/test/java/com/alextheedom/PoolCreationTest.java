package com.alextheedom;

import com.alextheedom.pool.JsonParserPool;
import com.alextheedom.pool.PoolDepletionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by atheedom on 31/12/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class PoolCreationTest {

    @Test
    public void ShouldInitialiseWith20Objects(){

        // arrange and act
        JsonParserPool jsonParserPool = new JsonParserPool(20);

        // assert
        assertThat(jsonParserPool.getPool().size()).isEqualTo(20);
    }


    @Test
    public void ShouldThrowDepletionException() throws PoolDepletionException, InterruptedException {

        // arrange
        JsonParserPool jsonParserPool = new JsonParserPool(1);

        // act
        jsonParserPool.borrowObject();

        // assert
        assertThatThrownBy(jsonParserPool::borrowObject).isInstanceOf(PoolDepletionException.class);

    }


    @Test
    public void ShouldDestroyPool(){

        // arrange
        JsonParserPool jsonParserPool = new JsonParserPool(1);

        // act
        jsonParserPool.destroyPool();

        // assert
        assertThat(jsonParserPool.getPool()).isNullOrEmpty();

    }







}
