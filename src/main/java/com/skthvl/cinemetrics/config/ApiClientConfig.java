package com.skthvl.cinemetrics.config;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ApiClientConfig {

  @Value("${cinemetrics.client.omdbapi.base-url}")
  private String baseUrl;

  @Value("${cinemetrics.client.omdbapi.api-key}")
  private String apiKey;

  @Bean(name = "omdbApiRestClient")
  public RestClient omdbApiRestClient(RestClient.Builder builder) {
    return builder
        .requestFactory(new HttpComponentsClientHttpRequestFactory())
        .baseUrl(baseUrl)
        .defaultUriVariables(Map.of("apikey", apiKey))
        .build();
  }
}
