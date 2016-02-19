package com.cisco.microservices.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ProductWebApplication is the entry point of your whole application, which starts tomcat servers  
 * 
 * @author Sandip Bastapure
 */
@SpringBootApplication
public class ProductWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductWebApplication.class, args);
    }
}
