package com.zoho.example.catalog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    @Value("${server.port}")
    String port;

    @GetMapping("/books")
    public String index() {
        return String.format("{\"port\": %s, \"books\": [\"Harry Potter\", \"Mary Poppins\"]}", port);
    }
}
