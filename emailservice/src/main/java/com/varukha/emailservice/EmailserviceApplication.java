package com.varukha.emailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailserviceApplication.class, args);
    }
}
