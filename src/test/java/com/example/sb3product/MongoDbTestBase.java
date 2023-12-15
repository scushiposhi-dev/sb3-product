package com.example.sb3product;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class MongoDbTestBase {

    private static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0.4"));
    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.host",mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port",()->mongoDBContainer.getMappedPort(27017));
        registry.add("spring.data.mongodb.database",()->"test");
    }
}
