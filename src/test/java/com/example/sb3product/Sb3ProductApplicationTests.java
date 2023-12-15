package com.example.sb3product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest( webEnvironment = RANDOM_PORT,
        properties = {"eureka.client.enabled=false"})
class Sb3ProductApplicationTests extends MongoDbTestBase{
    @Test
    void context(){
        assertNotNull(MongoDbTestBase.class);
    }
}
