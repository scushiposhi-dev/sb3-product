package com.example.sb3product.service;

import com.example.sb3product.exceptions.InvalidInputException;
import com.example.sb3product.exceptions.NotFoundException;
import com.example.sb3product.mapper.ProductMapper;
import com.example.sb3product.repository.ProductRepository;
import com.example.sb3product.util.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

import static java.util.logging.Level.ALL;

@Service
@Slf4j
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductService(ProductMapper productMapper, ProductRepository productRepository,ServiceUtil serviceUtil) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.serviceUtil=serviceUtil;
    }

    @Transactional(readOnly = true)
    public Mono<Product> findByProductId(Integer productId) {
        log.info("productId requested= {}",productId);

        return productRepository.findByProductId(productId)
                .log("found")
                .map(productMapper::toDto)
                .map(this::setServiceAddress)
                .log("mapped")
                .switchIfEmpty(Mono.error(new NotFoundException("NOT FOUND ID:"+productId)));
    }

    public Mono<Void> deleteByProductId(int productId){
        return productRepository.deleteByProductId(productId);
    }

    private Product setServiceAddress(Product product) {
        product.setServiceAddress(serviceUtil.getServiceAddress());
        return product;
    }

//    @Transactional
    public Mono<Product> createProduct(Product data) {
        return productRepository.save(productMapper.toEntity(data))
                .onErrorMap(DuplicateKeyException.class,
                        dup->new InvalidInputException("Duplicate prod id:"+data.getProductId()))
                .map(productMapper::toDto)
                .log("mapped")
                .map(this::setServiceAddress);
    }
}
