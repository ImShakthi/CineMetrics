package com.skthvl.cinemetrics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvalidatedTokenService {

  public InvalidatedTokenService() {}

  @CachePut(value = "invalidated-tokens", key = "#token")
  public boolean invalidateToken(final String token) {
    log.info("invalidating token: {}", token);
    return true;
  }

  @Cacheable(value = "invalidated-tokens", key = "#token")
  public boolean isTokenInvalidated(final String token) {
    log.info("checking if token is invalidated: {}", token);

    // by default, token is not invalidated
    return false;
  }
}
