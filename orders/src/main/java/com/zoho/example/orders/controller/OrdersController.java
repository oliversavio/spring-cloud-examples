package com.zoho.example.orders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class OrdersController {

    @Value("${some.config.value}")
    private String exampleConfigValue;

    @Autowired
    private WebClient.Builder loadBalanced;

    @Autowired
    private ReactiveCircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/")
    public String index() {
        Mono<String> r = loadBalanced.build().get()
                .uri("http://catalog/books")
                .retrieve().bodyToMono(String.class);
        String books = r.block();
        return "Book Orders: " + books;
    }

    @GetMapping("/cborders")
    public String cbBookOrders() {
        Mono<String> r = loadBalanced.build().get()
                .uri("http://catalog/books")
                .retrieve().bodyToMono(String.class)
                .transform(it -> circuitBreakerFactory.create("slow")
                        .run(it, throwable -> fallbackMethod()));

        String books = r.block() ;
        return "Book Orders: " + books;
    }

    @GetMapping("/testconfig")
    public String configTest() {
        return exampleConfigValue;
    }


    private Mono<String> fallbackMethod() {
        return Mono.just("Catalog is unavailable");
    }
}