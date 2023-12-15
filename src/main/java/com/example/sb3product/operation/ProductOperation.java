package com.example.sb3product.operation;

import com.example.sb3product.service.ProductService;
import org.openapitools.api.ProductServiceApi;
import org.openapitools.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class ProductOperation implements ProductServiceApi {
    private final ProductService productService;

    @Autowired
    public ProductOperation(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Mono<Void> deleteProduct(Integer productId, ServerWebExchange exchange) {
        return productService.deleteByProductId(productId);
    }

    @Override
    public Mono<Product> getProduct(Integer productId, ServerWebExchange exchange) {
        return productService.findByProductId(productId);
    }

    @PostMapping(path = "/v1/product")
    public Mono<Product> create(Product product){
        return productService.createProduct(product);
    }
}