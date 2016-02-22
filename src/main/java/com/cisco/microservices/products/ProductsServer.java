package com.cisco.microservices.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(ProductWebApplication.class)
public class ProductsServer {

    public static void main(String[] args) {
        SpringApplication.run(ProductsServer.class, args);
    }
}
