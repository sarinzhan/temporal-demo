package com.example.temporaldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EntityScan({"com.example.temporaldemo", "com.beeline.temporalmini"})
//@EnableJpaRepositories({"com.example.temporaldemo", "com.beeline.temporalmini"})
//@EnableScheduling
public class TemporalDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemporalDemoApplication.class, args);
    }
}
