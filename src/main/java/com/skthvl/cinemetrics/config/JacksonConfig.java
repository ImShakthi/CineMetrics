package com.skthvl.cinemetrics.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for Jackson ObjectMapper in the application. */
@Configuration
public class JacksonConfig {
  /**
   * Configures and provides a Jackson {@link ObjectMapper} as a Spring-managed bean.
   *
   * @return a configured {@link ObjectMapper} instance.
   */
  @Bean
  public ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();

    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return mapper;
  }
}
