package com.skthvl.cinemetrics;

import static com.skthvl.cinemetrics.stubs.WireMockStubs.stubGetMoveDetailsByTitle;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
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
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock(port = 0)
class CineMetricsApplicationTests {

  static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8");
  static final GenericContainer<?> redis =
      new GenericContainer<>(DockerImageName.parse("redis:7.0-alpine")).withExposedPorts(6379);

  @LocalServerPort private Integer port;
  @Autowired private UserAccountRepository userAccountRepository;

  @BeforeAll
  static void beforeAll() {
    mySQLContainer.start();
    redis.start();
  }

  @AfterAll
  static void afterAll() {
    mySQLContainer.stop();
    redis.stop();
  }

  @DynamicPropertySource
  static void configureProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
    registry.add("spring.data.redis.host", redis::getHost);
    registry.add("spring.data.redis.port", redis.getMappedPort(6379)::toString);
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
        .statusCode(201)
        .body("message", equalTo("user created successfully"));

    // user is created yet
    final var afterUserCreateCall = userAccountRepository.findByName("test_user_1");
    assertNotNull(afterUserCreateCall);
    assertTrue(afterUserCreateCall.isPresent());

    given()
        .basePath("/api/v1/users")
        .contentType(ContentType.JSON)
        .body(jsonBody)
        .when()
        .delete()
        .then()
        .statusCode(200)
        .body("message", equalTo("user deleted successfully"));

    // user deleted
    final var afterUserDeleted = userAccountRepository.findByName("test_user_1");
    assertNotNull(afterUserDeleted);
    assertTrue(afterUserDeleted.isEmpty());
  }

  @Test
  void getMovieDetailsAndCheckAwardDetails_whenMovieExists() {
    final String title = "Forrest Gump";

    stubGetMoveDetailsByTitle(title);

    // get movie details
    given()
        .basePath("/api/v1/movies/{title}")
        .pathParam("title", title)
        .accept(ContentType.JSON)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("title", equalTo("Forrest Gump"))
        .body("year", equalTo("2010"))
        .body("rated", equalTo("PG-13"))
        .body("released", equalTo("16 Jul 2010"));

    // get movie award details
//    given()
//        .basePath("/api/v1/movies/{title}/oscar")
//        .pathParam("title", title)
//        .accept(ContentType.JSON)
//        .when()
//        .get()
//        .then()
//        .statusCode(200)
//        .body("title", equalTo("Forrest Gump"))
//        .body("year", equalTo("2010"))
//        .body("rated", equalTo("PG-13"))
//        .body("released", equalTo("16 Jul 2010"));
  }
}
