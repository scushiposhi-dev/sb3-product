package com.example.sb3product;

import com.example.sb3product.entity.ProductEntity;
import com.example.sb3product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import reactor.test.StepVerifier;

@DataMongoTest
class ProductRepositoryTest extends MongoDbTestBase {
    @Autowired
    private ProductRepository productRepository;
    private ProductEntity savedProduct;

    @BeforeEach
    void setupDB() {
        StepVerifier.create(productRepository.deleteAll()).verifyComplete();

        ProductEntity product = ProductEntity.builder().name("a").productId(1).weight(1).build();

        StepVerifier.create(productRepository.save(product))
                .consumeNextWith(created->savedProduct=created)
                .verifyComplete();
    }

    @Test
    void create() {

        ProductEntity newProduct = ProductEntity.builder().name("b").productId(2).weight(2).build();

        StepVerifier.create(productRepository.save(newProduct))
                .expectNextMatches(createdProduct -> createdProduct.getProductId() == newProduct.getProductId())
                .verifyComplete();

        StepVerifier.create(productRepository.findByProductId(newProduct.getProductId()))
                .expectNextMatches(found -> areProductEqual(newProduct, found))
                .verifyComplete();
    }

    @Test
    void update() {

        savedProduct.setName("a2");

        StepVerifier.create(productRepository.save(savedProduct))
                .expectNextMatches(updated -> updated.getName().equals(savedProduct.getName()))
                .verifyComplete();
    }

    @Test
    void delete() {
        StepVerifier.create(productRepository.delete(savedProduct)).verifyComplete();
        StepVerifier.create(productRepository.existsById(savedProduct.getId())).
                expectNext(false)
                .verifyComplete();
    }

    @Test
    void duplicateError() {
        ProductEntity productEntity = ProductEntity.builder()
                .productId(savedProduct.getProductId())
                .name(savedProduct.getName())
                .weight(1).build();

        StepVerifier.create(productRepository.save(productEntity))
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void optimisticLockError() {
        ProductEntity retrievedProductId1 = productRepository.findByProductId(savedProduct.getProductId()).block();
        ProductEntity retrievedProductId1Bis = productRepository.findByProductId(savedProduct.getProductId()).block();

        retrievedProductId1.setName("a1");
        productRepository.save(retrievedProductId1).block();

        StepVerifier.create(productRepository.save(retrievedProductId1Bis))
                .expectError(OptimisticLockingFailureException.class)
                .verify();

        StepVerifier.create(productRepository.findById(savedProduct.getId()))
                .expectNextMatches(found ->
                        found.getVersion() == 1
                        && found.getName().equals("a1"))
                .verifyComplete();
    }

    private boolean areProductEqual(ProductEntity p1, ProductEntity p2) {
        return p1.getId().equals(p2.getId())
                &&
                p1.getVersion() == p2.getVersion()
                &&
                p1.getProductId() == p2.getProductId()
                &&
                p1.getName().equals(p2.getName())
                &&
                p1.getWeight() == p2.getWeight();
    }
}
