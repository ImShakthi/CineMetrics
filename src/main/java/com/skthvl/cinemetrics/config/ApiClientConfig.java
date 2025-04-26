package com.skthvl.cinemetrics.config;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Configuration class for initializing and configuring the OMDB API RestClient. This class defines
 * beans and related settings required for communicating with the OMDB API.
 */
@Configuration
public class ApiClientConfig {

  @Value("${cinemetrics.client.omdbapi.base-url}")
  private String baseUrl;

  @Value("${cinemetrics.client.omdbapi.api-key}")
  private String apiKey;

  /**
   * Configures and provides a Spring-managed {@link RestClient} for interacting with the OMDB API.
   * The client is initialized with the base URL and API key for authentication.
   *
   * @param builder a {@link RestClient.Builder} instance for configuring the RestClient.
   * @return a configured {@link RestClient} instance for communicating with the OMDB API.
   */
  @Bean(name = "omdbApiRestClient")
  public RestClient omdbApiRestClient(RestClient.Builder builder) {
    return builder
        .requestFactory(new HttpComponentsClientHttpRequestFactory())
        .baseUrl(baseUrl)
        .defaultUriVariables(Map.of("apikey", apiKey))
        .build();
  }
}
