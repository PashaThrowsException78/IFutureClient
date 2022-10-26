package com.tabakov.ifutureclient.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientConfig {
    @Value("${server.url:http://127.0.0.1:8080}")
    private String url = "127.0.0.1:8080";

    @Value("${account.id.min:1}")
    private int idLowBound = 1;

    @Value("${account.id.max:5}")
    private int idHighBound = 5;

    @Value("${account.amount.min:1}")
    private int amountLowBound = 1;

    @Value("${account.amount.max:100}")
    private int amountHighBound = 100;

    @Value("${rCount:3}")
    private int rCount = 3;

    @Value("${wCount:3}")
    private int wCount = 3;

    @PostConstruct
    public void init() {
        System.out.println(this);
    }

}
