package com.moyuyo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MOYUYO 后端服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.moyuyo")
public class MoyuyoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyuyoApplication.class, args);
    }
}
