package com.skthvl.cinemetrics.client.omdb;

import static com.skthvl.cinemetrics.exception.ErrorConstants.INVALID_API_KEY;
import static com.skthvl.cinemetrics.exception.ErrorConstants.MOVIE_NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
import com.skthvl.cinemetrics.client.omdb.dto.OmdbBaseResponse;
import com.skthvl.cinemetrics.exception.ApiClientException;
import com.skthvl.cinemetrics.exception.InvalidApiKeyException;
import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Client for interacting with the Open Movie Database (OMDB) API. This class handles HTTP requests
 * to fetch movie details using the OMDB API. Responses are cached to improve performance and reduce
 * API calls.
 */
@Slf4j
@Configuration
public class OmdbApiClient {

  /** RestClient instance configured for OMDB API calls. */
  private final RestClient omdbApiRestClient;

  /** API key required for authentication with OMDB API. */
  @Value("${cinemetrics.client.omdbapi.api-key}")
  private String apiKey;

  /**
   * Constructs an OmdbApiClient with the specified RestClient.
   *
   * @param omdbApiRestClient The REST client configured for OMDB API communication
   */
  public OmdbApiClient(@Qualifier("omdbApiRestClient") final RestClient omdbApiRestClient) {
    this.omdbApiRestClient = omdbApiRestClient;
  }

  /**
   * Retrieves detailed information about a movie using its title. Utilizes the OMDB API to fetch
   * the movie details and caches the results.
   *
   * @param title the title of the movie to retrieve details for
   * @return a {@link MovieDetailsResponse} containing the movie details, such as title, year,
   *     director, plot, and more
   * @throws ApiClientException if an error occurs while fetching movie details or the movie is not
   *     found
   */
  @Cacheable(value = "movies", key = "#title")
  public MovieDetailsResponse getMoveDetailsByTitle(final String title) {
    log.info("getting movie details by title: {}", title);
    final var response =
        Optional.ofNullable(
                omdbApiRestClient
                    .get()
                    .uri("?apikey={apiKey}&t={title}", apiKey, title)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(MovieDetailsResponse.class))
            .orElseThrow(
                () -> new ApiClientException("error in getting move details by title " + title));

    return handleResponse(response);
  }

  /**
   * Retrieves detailed information about a movie using its title and release year. Utilizes the
   * OMDB API and caches the results for improved performance.
   *
   * @param title the title of the movie to retrieve details for
   * @param year the release year of the movie to retrieve details for
   * @return a {@link MovieDetailsResponse} containing the movie details, such as title, year,
   *     director, plot, and more
   * @throws ApiClientException if an error occurs while fetching the movie details or if the movie
   *     is not found
   */
  @Cacheable(value = "movie", key = "#title+'-'+#year")
  public MovieDetailsResponse getMoveDetailsByTitleAndYear(final String title, final int year) {
    log.info("getting movie details by title: {} and year: {}", title, year);
    final var response =
        Optional.ofNullable(
                omdbApiRestClient
                    .get()
                    .uri("?apikey={apiKey}&t={title}&y={year}", apiKey, title, year)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(MovieDetailsResponse.class))
            .orElseThrow(
                () -> new ApiClientException("error in getting move details by title " + title));

    return handleResponse(response);
  }

  private <T extends OmdbBaseResponse> T handleResponse(final T resp) {
    if ("False".equalsIgnoreCase(resp.getResponse())) {
      switch (resp.getError()) {
        case MOVIE_NOT_FOUND:
          throw new MovieNotFoundException(resp.getError());
        case INVALID_API_KEY:
          throw new InvalidApiKeyException(resp.getError());
        default:
          throw new ApiClientException(resp.getError());
      }
    }
    return resp;
  }
}
