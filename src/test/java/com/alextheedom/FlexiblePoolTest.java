package com.alextheedom;

import com.alextheedom.pool.JsonParserFlexiblePool;
import com.alextheedom.pool.PoolDepletionException;
import org.boon.json.JsonParserAndMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by atheedom on 31/12/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class FlexiblePoolTest {

    private JsonParserFlexiblePool jsonParserFlexiblePool;

    @After
    public void teardown() {
        jsonParserFlexiblePool.destroyPool();
    }

    @Test
    public void ShouldInitialiseWith15Objects() {

        // arrange and act
        jsonParserFlexiblePool = new JsonParserFlexiblePool(15);

        // assert
        assertThat(jsonParserFlexiblePool.getPool().size()).isEqualTo(15);
    }


    @Test
    public void ShouldReturnObjectToPool() throws InterruptedException, PoolDepletionException {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(15);

        // act
        JsonParserAndMapper jsonParserAndMapper = jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.returnObject(jsonParserAndMapper);

        // assert
        assertThat(jsonParserFlexiblePool.getPool().size()).isEqualTo(15);
    }


    @Test
    public void ShouldDestroyPool() {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(15);

        // act
        jsonParserFlexiblePool.destroyPool();

        // assert
        assertThat(jsonParserFlexiblePool.getPool()).isNullOrEmpty();
    }

    @Test
    public void ShouldReplenishPool() throws InterruptedException, PoolDepletionException {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(10);

        // act
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();

        Thread.sleep(4000);

        // assert
        assertThat(jsonParserFlexiblePool.getPool().size()).isEqualTo(10);

    }

    @Test
    public void ShouldCreateWithConfigsPool() throws InterruptedException, PoolDepletionException {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(10, 10, 15, 2000);

        // act
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();
        jsonParserFlexiblePool.borrowObject();

        Thread.sleep(4000);

        // assert
        assertThat(jsonParserFlexiblePool.getPool().size()).isEqualTo(10);

    }

}
