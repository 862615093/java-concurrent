package com.ww.concurrent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ConcurrentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrentApplication.class, args);
    }
}