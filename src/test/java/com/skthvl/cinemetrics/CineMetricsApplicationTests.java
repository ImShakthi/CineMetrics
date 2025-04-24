package com.skthvl.cinemetrics;

import static com.skthvl.cinemetrics.stubs.WireMockStubs.stubGetMoveDetailsByTitle;
import static com.skthvl.cinemetrics.stubs.WireMockStubs.stubGetMoveDetailsByTitleAndYear;
import static com.skthvl.cinemetrics.stubs.WireMockStubs.stubGetMoveDetailsByTitleAndYearByBoxOffice;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.skthvl.cinemetrics.entity.UserAccount;
import com.skthvl.cinemetrics.repository.RatingRepository;
import com.skthvl.cinemetrics.repository.UserAccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
  @Autowired private RatingRepository ratingRepository;

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

    userAccountRepository.deleteAll();
    ratingRepository.deleteAll();
  }

  private final String userName = "test_user_1";
  private final String password = "password123";
  private final String userJson =
      """
          {"username":"%s","password":"%s"}
      """
          .formatted(userName, password);

  @Test
  void contextLoads() {}

  @Nested
  class UserLifecycleTests {

    @Test
    void testFullUserJourney() {
      createAdminUser();
      final String adminToken = getToken("admin", "admin123");

      assertUserDoesntExist(userName);
      assertCreateUser(userJson, adminToken);
      assertUserExists(userName);

      final String userToken = getToken(userName, password);
      assertMovieSearchAndAward();
      assertRatingsLifecycle(userToken);
      assertTopRatedLogic(userToken);

      assertLogout(userToken);
      assertUnauthorizedAccess(userToken);

      assertDeleteUser(adminToken, userJson);
      assertUserDoesntExist(userName);
    }

    void createAdminUser() {
      userAccountRepository.save(
          UserAccount.builder()
              .name("admin")
              .passwordHash("$2a$10$VCLsbE5ST5d/OtCRbXEVZuDZvnT9gj9Z3MRINuU8yaGXBP9/5LeDa")
              .build());
    }

    void assertMovieSearchAndAward() {
      final String title = "forrest gump";
      final String year = "1994";
      assertGetMovieDetails(title, year);
      assertGetAwardMovieDetails(title, year);
    }

    void assertRatingsLifecycle(String token) {
      String title = "Black Swan";
      assertEquals("[]", getRatingsForMovieBy(title).asString());

      addRating(token, 1, 85, "good movie");

      var response = getRatingsForMovieBy(title);
      assertNotNull(response);
      assertTrue(response.asString().contains("Black Swan"));
    }

    void assertTopRatedLogic(String token) {
      stubGetMoveDetailsByTitleAndYearByBoxOffice("The Fighter", "2010", "90000000");
      addRating(token, 2, 85, "good movie");

      stubGetMoveDetailsByTitleAndYearByBoxOffice("Inception", "2010", "60000000");
      addRating(token, 3, 70, "ok movie");

      stubGetMoveDetailsByTitleAndYearByBoxOffice("The Kids Are All Right", "2010", "85000000");
      addRating(token, 4, 95, "awesome movie");

      assertTopRated(token);
    }

    private String getToken(String username, String password) {
      String json = "{\"username\":\"%s\",\"password\":\"%s\"}".formatted(username, password);
      return given()
          .basePath("/api/v1/login")
          .contentType(ContentType.JSON)
          .body(json)
          .when()
          .post()
          .then()
          .extract()
          .path("token");
    }

    private void assertLogout(String token) {
      given()
          .header("Authorization", "Bearer " + token)
          .basePath("/api/v1/logout")
          .when()
          .post()
          .then()
          .statusCode(200);
    }

    private void assertDeleteUser(final String token, final String json) {
      given()
          .header("Authorization", "Bearer " + token)
          .basePath("/api/v1/users")
          .contentType(ContentType.JSON)
          .body(json)
          .when()
          .delete()
          .then()
          .statusCode(200)
          .body("message", equalTo("user deleted successfully"));
    }

    private void assertUserExists(final String username) {
      var user = userAccountRepository.findByName(username);
      assertTrue(user.isPresent());
    }

    private void assertUserDoesntExist(final String username) {
      var user = userAccountRepository.findByName(username);
      assertTrue(user.isEmpty());
    }

    private void assertCreateUser(String json, String token) {
      given()
          .header("Authorization", "Bearer " + token)
          .basePath("/api/v1/users")
          .contentType(ContentType.JSON)
          .body(json)
          .when()
          .post()
          .then()
          .statusCode(201)
          .body("message", equalTo("user created successfully"));
    }

    void assertUnauthorizedAccess(String token) {
      given()
          .header("Authorization", "Bearer " + token)
          .basePath("/api/v1/ratings/top")
          .queryParam("limit", 10)
          .when()
          .get()
          .then()
          .statusCode(401);
    }

    private void assertGetMovieDetails(final String title, final String year) {
      stubGetMoveDetailsByTitle(title, year);

      given()
          .basePath("/api/v1/movies/search")
          .queryParam("title", title)
          .accept(ContentType.JSON)
          .when()
          .get()
          .then()
          .statusCode(200)
          .body("title", equalTo(title))
          .body("releaseYear", equalTo(year))
          .body("rated", equalTo("PG-13"))
          .body("released", equalTo("16 Jul 2010"));
    }

    private void addRating(
        final String token, final int movieId, final int rating, final String comment) {
      final var ratingJsonBody =
          """
          {
              "rating": %d,
              "comment": "%s"
          }
          """
              .formatted(rating, comment);
      given()
          .header("Authorization", "Bearer " + token)
          .basePath("/api/v1/movies/{movieId}/ratings")
          .pathParam("movieId", movieId)
          .contentType(ContentType.JSON)
          .accept(ContentType.JSON)
          .body(ratingJsonBody)
          .when()
          .post()
          .then()
          .statusCode(200)
          .body("message", containsString("added rating to movie:"));
    }

    private void assertGetAwardMovieDetails(final String title, final String year) {
      stubGetMoveDetailsByTitleAndYear(title, year);
      given()
          .basePath("/api/v1/movies/{title}/oscar")
          .pathParam("title", title)
          .when()
          .get()
          .then()
          .statusCode(200)
          .body("releaseYear", hasItems(Integer.parseInt(year)))
          .body("hasWon", hasItems(true));
    }

    private void assertTopRated(final String token) {
      final var response =
          given()
              .header("Authorization", "Bearer " + token)
              .basePath("/api/v1/ratings/top")
              .queryParam("limit", 10)
              .accept(ContentType.JSON)
              .when()
              .get()
              .then()
              .extract()
              .response();

      assertNotNull(response);
      assertEquals(200, response.getStatusCode());
      assertEquals(getExpectedTopRatedJson(), response.prettyPrint());
    }

    private Response getRatingsForMovieBy(final String title) {
      stubGetMoveDetailsByTitle(title, "2010");

      return given()
          .basePath("/api/v1/movies/{title}/ratings")
          .pathParam("title", title)
          .when()
          .get()
          .then()
          .statusCode(200)
          .extract()
          .response();
    }

    private String getExpectedTopRatedJson() {
      return """
        [
            {
                "title": "Black Swan",
                "averageRating": 85.00,
                "boxOfficeValue": 292587330
            },
            {
                "title": "The Fighter",
                "averageRating": 85.00,
                "boxOfficeValue": 90000000
            },
            {
                "title": "The Kids Are All Right",
                "averageRating": 95.00,
                "boxOfficeValue": 85000000
            },
            {
                "title": "Inception",
                "averageRating": 70.00,
                "boxOfficeValue": 60000000
            }
        ]""";
    }
  }
}
