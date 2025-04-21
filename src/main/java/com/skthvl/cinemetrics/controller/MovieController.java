package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.response.MovieInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
@Slf4j
public class MovieController {

  private final OmdbApiClient omdbApiClient;
  private final MovieMapper movieMapper;

  public MovieController(final OmdbApiClient omdbApiClient, final MovieMapper movieMapper) {
    this.omdbApiClient = omdbApiClient;
    this.movieMapper = movieMapper;
  }

  @GetMapping("/{title}")
  public ResponseEntity<MovieInfoResponse> getMovieDetailsByTitle(@PathVariable String title) {
    final var movieDetails = omdbApiClient.getMoveDetailsByTitle(title);

    log.info("Movie Details: {}", movieDetails);

    return ResponseEntity.ok(movieMapper.toMovieInfoResponse(movieDetails));
  }
}
