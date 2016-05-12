package com.alextheedom;

import com.alextheedom.application.JsonParserFacade;
import com.alextheedom.application.JsonPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by atheedom on 12/05/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class JsonParserFlexiblePoolTest {

    private static final JsonParserFacade JSON_PARSER = JsonPoolFactory.getParserFacadeInstance();
    private String testEntityJson;
    private TestEntity testEntityObject;

    @After
    public void teardown() {
        JSON_PARSER.destroyPool();
    }

    @Before
    public void setup() {

        testEntityJson = "{\"name\":\"John Smith\",\"age\":30}";
        testEntityObject = new TestEntity("John Smith", 30);

    }

    @Test
    public void ShouldInitialiseWith15Objects() throws Exception {

        // arrange and act
        JsonParserFacade JSON_PARSER = JsonPoolFactory.getParserFacadeInstance(15);

        // assert
        assertThat(JSON_PARSER.getJsonParserFixedPool().getCurrentPoolSize()).isEqualTo(15);
    }

    @Test
    public void ShouldParseJSONToObject() throws Exception {

        // arrange
        JsonParserFacade JSON_PARSER = JsonPoolFactory.getParserFacadeInstance(15);

        // act
        TestEntity testEntity = JSON_PARSER.parse(TestEntity.class, testEntityJson);

        // assert
        assertThat(testEntity).isEqualTo(testEntityObject);
    }


    public class TestEntity {

        private String name;
        private int age;

        public TestEntity(String name, int age) {
            this.age = age;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestEntity)) return false;
            TestEntity that = (TestEntity) o;
            return age == that.age &&
                    Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

}
