package com.skthvl.cinemetrics;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.config.name=application-itest")
class CineMetricsApplicationTests {

  static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8");

  @DynamicPropertySource
  static void configureProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @BeforeAll
  static void beforeAll() {
    mySQLContainer.start();
  }

  @AfterAll
  static void afterAll() {
    mySQLContainer.stop();
  }

  @Test
  void contextLoads() {}
}
