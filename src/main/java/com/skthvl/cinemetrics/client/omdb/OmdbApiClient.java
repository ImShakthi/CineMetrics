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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OmdbApiClient {

  private final RestClient omdbApiRestClient;

  @Value("${cinemetrics.client.omdbapi.api-key}")
  private String apiKey;

  public OmdbApiClient(@Qualifier("omdbApiRestClient") final RestClient omdbApiRestClient) {
    this.omdbApiRestClient = omdbApiRestClient;
  }

  public MovieDetailsResponse getMoveDetailsByTitle(final String title) {
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
