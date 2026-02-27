package com.delivery.food.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
@ComponentScan("com.delivery.food")
public class AuthServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApp.class, args);
    }
}