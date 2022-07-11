package com.phenoxp.reactive.orderservice.assignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webclient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8888")
                .build();
    }
}
