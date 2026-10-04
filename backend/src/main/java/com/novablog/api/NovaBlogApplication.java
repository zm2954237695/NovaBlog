package com.novablog.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.novablog.api.repository")
public class NovaBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaBlogApplication.class, args);
    }
}
