package com.skthvl.cinemetrics.client.omdb.dto;

/**
 * Base interface for OMDB API responses. Provides common fields that are present in all OMDB API
 * responses, including success/error indicators.
 */
public interface OmdbBaseResponse {
  /**
   * Returns the response status from OMDB API.
   *
   * @return String containing "True" for successful responses or "False" for failures
   */
  String getResponse();

  /**
   * Returns error message if the API request failed.
   *
   * @return String containing error message or null if request was successful
   */
  String getError();
}
