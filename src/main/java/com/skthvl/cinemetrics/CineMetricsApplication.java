package com.skthvl.cinemetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * The CineMetricsApplication class serves as the entry point for the CineMetrics application. It is
 * a Spring Boot application that enables caching and initializes the application context.
 *
 * <p>This class is annotated with: - @EnableCaching: Enables Spring's annotation-driven cache
 * management capability. - @SpringBootApplication: Denotes a configuration class that declares one
 * or more @Bean methods and triggers auto-configuration, component scanning, and additional
 * features of Spring Boot.
 *
 * <p>The main method calls SpringApplication.run to bootstrap the application.
 */
@EnableCaching
@SpringBootApplication
public class CineMetricsApplication {

  /**
   * The main method serves as the entry point for the CineMetrics application.
   *
   * @param args command-line arguments passed to the application at runtime.
   */
  public static void main(String[] args) {
    SpringApplication.run(CineMetricsApplication.class, args);
  }
}
