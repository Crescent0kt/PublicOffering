package com.example.publicoffering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class PublicOfferingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicOfferingApplication.class, args);
    }

}
