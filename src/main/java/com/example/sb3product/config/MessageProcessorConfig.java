package com.example.sb3product.config;

import com.example.sb3product.exceptions.EventProcessingException;
import com.example.sb3product.message.Event;
import com.example.sb3product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class MessageProcessorConfig {
    private final ProductService productService;

    @Bean
    public Consumer<Event<Integer,Product>> messageProcessor(){

        return event-> {
            log.info("Process message at:{}", event.getEventCreatedAt());
            switch (event.getEventType()){
                case CREATE -> productService.createProduct(event.getData()).block();
                case DELETE -> productService.deleteByProductId(event.getKey()).block();
                default -> Flux.error(EventProcessingException::new).log("consumer.event");
            }
            log.info("message processed");
        };
    }

}
