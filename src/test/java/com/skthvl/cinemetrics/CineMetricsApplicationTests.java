package com.skthvl.cinemetrics;

import static com.skthvl.cinemetrics.stubs.WireMockStubs.stubGetMoveDetailsByTitle;
import static com.skthvl.cinemetrics.stubs.WireMockStubs.stubGetMoveDetailsByTitleAndYear;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.skthvl.cinemetrics.repository.UserAccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    final var userName = "test_user_1";
    final String jsonBody =
        """
          {"username":"%s","password":"password123"}
          """
            .formatted(userName);

    // create table
    userAccountRepository.deleteAll();

    // user not created yet
    assertUserDoesntExist(userName);

    // create user
    assertCreateUser(jsonBody);

    // check user created
    assertUserExists(userName);

    // login and fetch token
    final String token = getToken(jsonBody);
    log.info("token: {}", token);

    // delete user
    assertDeleteUser(jsonBody);

    // check after user deleted
    assertUserDoesntExist(userName);
  }

  @Test
  void getMovieDetailsAndCheckAwardDetails_whenMovieExists() {
    final String title = "Forrest Gump";

    // get movie details
    assertGetMovieDetails(title);

    // get movie award details
    assertGetAwardMovieDetails(title);
  }

  private void assertCreateUser(final String createUserJsonBody) {
    given()
        .basePath("/api/v1/users")
        .contentType(ContentType.JSON)
        .body(createUserJsonBody)
        .when()
        .post()
        .then()
        .statusCode(201)
        .body("message", equalTo("user created successfully"));
  }

  private String getToken(final String loginRequestJsonBody) {
    return given()
        .basePath("/api/v1/login")
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body(loginRequestJsonBody)
        .when()
        .post()
        .then()
        .statusCode(200)
        .extract()
        .response()
        .body()
        .jsonPath()
        .getString("token");
  }

  private void assertDeleteUser(final String deleteUserJsonBody) {
    given()
        .basePath("/api/v1/users")
        .contentType(ContentType.JSON)
        .body(deleteUserJsonBody)
        .when()
        .delete()
        .then()
        .statusCode(200)
        .body("message", equalTo("user deleted successfully"));
  }

  private void assertUserExists(final String userName) {
    final var user = userAccountRepository.findByName(userName);
    assertNotNull(user);
    assertTrue(user.isPresent());
  }

  private void assertUserDoesntExist(final String userName) {
    final var user = userAccountRepository.findByName(userName);
    assertNotNull(user);
    assertTrue(user.isEmpty());
  }

  private void assertGetMovieDetails(final String title) {
    stubGetMoveDetailsByTitle(title);

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
  }

  private void assertGetAwardMovieDetails(final String title) {
    stubGetMoveDetailsByTitleAndYear(title, "1994");
    given()
        .basePath("/api/v1/movies/{title}/oscar")
        .pathParam("title", title)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("releaseYear", hasItems(1994))
        .body("hasWon", hasItems(true));
  }
}
