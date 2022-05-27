package com.cfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class WorkerApp {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApp.class, args);
    }
}
