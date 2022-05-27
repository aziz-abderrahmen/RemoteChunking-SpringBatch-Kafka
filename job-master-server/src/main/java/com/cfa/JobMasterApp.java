package com.cfa;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class JobMasterApp {

  public static void main(String[] args) {
    SpringApplication.run(JobMasterApp.class, args);
  }

}
