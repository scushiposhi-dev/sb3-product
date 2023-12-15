package com.example.sb3product;

import com.example.sb3product.message.Event;
import com.example.sb3product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.function.Consumer;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT,properties = {"spring.main.allow-bean-definition-overriding=true",
        "eureka.client.enabled=false"})
class ProductOperationTest extends MongoDbTestBase {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    @Qualifier("messageProcessor")
    Consumer<Event<Integer, Product>> messageProcessor;

    @BeforeEach
    void setUp() {
        StepVerifier.create(productRepository.deleteAll()).verifyComplete();
    }

    @Test
    void getProductOK() {
        Product product = Product.builder().name("my name").weight(80).productId(1).build();

        sendCreateProductEvent(product);
        WebTestClient.BodyContentSpec prodBodyContentSpec = getAndVerifyProduct(product.getProductId(), HttpStatus.OK);
        prodBodyContentSpec.jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.weight").isEqualTo(product.getWeight());

    }

    private void sendCreateProductEvent(Product product) {

        Event<Integer, Product> productEvent = Event.<Integer, Product>builder().eventType(Event.Type.CREATE).data(product).key(product.getProductId()).build();
        messageProcessor.accept(productEvent);
    }

    private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId, HttpStatus httpStatus) {
        return webTestClient.get()
                .uri("v1/product/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(httpStatus)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }
}
