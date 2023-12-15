package com.example.sb3product.repository;

import com.example.sb3product.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductEntity,String> {
    Mono<ProductEntity> findByProductId(int productId);
    Mono<Void> deleteByProductId(int productId);
}
