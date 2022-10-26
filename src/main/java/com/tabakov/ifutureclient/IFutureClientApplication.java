package com.tabakov.ifutureclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class IFutureClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(IFutureClientApplication.class, args);
    }

}
