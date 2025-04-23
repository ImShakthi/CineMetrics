package com.skthvl.cinemetrics;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.skthvl.cinemetrics.repository.UserAccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = "spring.config.name=application-itest")
class CineMetricsApplicationTests {

  static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8");

  @LocalServerPort private Integer port;
  @Autowired private UserAccountRepository userAccountRepository;

  @BeforeAll
  static void beforeAll() {
    mySQLContainer.start();
  }

  @AfterAll
  static void afterAll() {
    mySQLContainer.stop();
  }

  @DynamicPropertySource
  static void configureProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @BeforeEach
  void backEach() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  void contextLoads() {}

  @Test
  void addAndDeleteUser() {
    // create table
    userAccountRepository.deleteAll();

    // user not created yet
    final var beforeUserCreated = userAccountRepository.findByName("test_user_1");
    assertNotNull(beforeUserCreated);
    assertTrue(beforeUserCreated.isEmpty());

    final String jsonBody =
        """
      {"username":"test_user_1","password":"password123"}
      """;
    given()
        .basePath("/api/v1/users")
        .contentType(ContentType.JSON)
        .body(jsonBody)
        .when()
        .post()
        .then()
        .statusCode(201);

    // user is created yet
    final var afterUserCreateCall = userAccountRepository.findByName("test_user_1");
    assertNotNull(afterUserCreateCall);
    assertTrue(afterUserCreateCall.isPresent());
  }
}
