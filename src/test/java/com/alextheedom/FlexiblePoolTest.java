package com.alextheedom;

import com.alextheedom.application.JsonParserFlexiblePool;
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

    @Test
    public void ShouldReplenishPool() throws Exception {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(10);

        // act
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();

        Thread.sleep(4000);

        // assert
        assertThat(jsonParserFlexiblePool.getCurrentPoolSize()).isEqualTo(10);

    }

    @Test
    public void ShouldCreateWithConfigsPool() throws Exception {

        // arrange
        jsonParserFlexiblePool = new JsonParserFlexiblePool(10, 10, 15, 2000);

        // act
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();
        jsonParserFlexiblePool.acquire();

        Thread.sleep(4000);

        // assert
        assertThat(jsonParserFlexiblePool.getCurrentPoolSize()).isEqualTo(10);

    }

}
