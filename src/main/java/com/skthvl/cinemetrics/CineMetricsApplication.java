package com.skthvl.cinemetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CineMetricsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CineMetricsApplication.class, args);
  }
}
