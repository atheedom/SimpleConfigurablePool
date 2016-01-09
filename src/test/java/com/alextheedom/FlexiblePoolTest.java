package com.alextheedom;

import com.alextheedom.application.JsonParserFlexiblePool;
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
        assertThat(jsonParserFlexiblePool.getCurrentPoolSize()).isEqualTo(15);
    }


    @Test
    public void ShouldReturnObjectToPool() throws Exception {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(15);

        // act
        JsonParserAndMapper jsonParserAndMapper = jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.surrender(jsonParserAndMapper);

        // assert
        assertThat(jsonParserFlexiblePool.getCurrentPoolSize()).isEqualTo(15);
    }


    @Test
    public void ShouldDestroyPool() {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(15);

        // act
        jsonParserFlexiblePool.destroyPool();

        // assert
        assertThat(jsonParserFlexiblePool.isPoolNull()).isTrue();
    }

    /**
     * Should restore the pool size to minIdle.
     *
     * @throws Exception
     */
    @Test
    public void ShouldReplenishPoolViaMonitor() throws Exception {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(10, 20, 100); // poolSize = (20 + 10) / 2 = 15

        // act
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();

        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();

        Thread.sleep(200);

        // assert
        assertThat(jsonParserFlexiblePool.getCurrentPoolSize()).isEqualTo(10);
    }

    /**
     * Set the validation interval very high to ensure that it is never provoked
     * and the pool is never replenished by the monitor.
     *
     * @throws Exception
     */
    @Test
    public void ShouldThrowDepletionException() throws Exception {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(1, 1, 1_000_000); // int poolSize, int minIdle, int maxIdle, int validationInterval

        // act
        jsonParserFlexiblePool.acquire();

        // assert
        assertThatThrownBy(jsonParserFlexiblePool::acquire).isInstanceOf(PoolDepletionException.class);
    }

    @Test
    public void ShouldCreateWithConfigsPool() throws Exception {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(10, 20, 100); // poolSize = (20 + 10) / 2 = 15

        // act
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();

        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();

        Thread.sleep(200);

        // assert
        assertThat(jsonParserFlexiblePool.getCurrentPoolSize()).isEqualTo(10);
    }

}
