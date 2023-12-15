package com.example.sb3product.bootsrap;

import com.example.sb3product.entity.ProductEntity;
import com.example.sb3product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class Bootstrap implements CommandLineRunner {
    @Autowired
    ProductRepository productRepository;
    @Override
    public void run(String... args) {
        ProductEntity first = ProductEntity.builder().productId(1).name("first").weight(70).build();
        ProductEntity second = ProductEntity.builder().productId(2).name("second").weight(80).build();
        productRepository.saveAll(Arrays.asList(first,second)).collectList();
    }
}
