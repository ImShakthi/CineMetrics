package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.response.MovieAwardInfoResponse;
import com.skthvl.cinemetrics.model.response.MovieInfoResponse;
import com.skthvl.cinemetrics.service.MovieService;
import com.skthvl.cinemetrics.service.NominationInfoService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MovieController serves as the REST API controller for handling operations related to movies. It
 * provides endpoints for retrieving movie details and checking if specific movies have won any
 * awards based on their titles.
 *
 * <p>This controller interacts with the following components: - MovieService: Used to fetch and
 * manage movie-related data. - NominationInfoService: Used to fetch award and nomination-related
 * information. - MovieMapper: Used to map entities and DTOs to response models.
 */
@RestController
@RequestMapping("/api/v1/movies")
@Slf4j
@Validated
public class MovieController {

  private final MovieService movieService;
  private final NominationInfoService nominationInfoService;
  private final MovieMapper movieMapper;

  /**
   * Constructs a new instance of {@code MovieController} with the specified services and mapper.
   *
   * @param movieService the service responsible for handling movie-related business logic
   * @param movieMapper the mapper for converting between movie entities, DTOs, and response models
   * @param movieInfoService the service responsible for managing award and nomination information
   *     for movies
   */
  public MovieController(
      final MovieService movieService,
      final MovieMapper movieMapper,
      final NominationInfoService movieInfoService) {
    this.movieService = movieService;
    this.movieMapper = movieMapper;
    this.nominationInfoService = movieInfoService;
  }

  /**
   * Retrieves movie details based on the provided title.
   *
   * @param title the title of the movie to fetch details for
   * @return a ResponseEntity containing a MovieInfoResponse object with the movie details
   */
  @GetMapping("/search")
  public ResponseEntity<MovieInfoResponse> getMovieDetailsByTitle(
      @RequestParam(value = "title") final String title) {
    final var movieDetails = movieService.getMovieInfoByTitle(title);

    log.info("Movie Details: {}", movieDetails);

    return ResponseEntity.ok(movieMapper.toMovieInfoResponse(movieDetails));
  }

  /**
   * Checks if a movie with the specified title has won awards in the given category. If the
   * category is not provided, it defaults to "best picture".
   *
   * @param title the title of the movie to check for awards
   * @param category the award category to filter by (optional, defaults to "best picture")
   * @return a ResponseEntity containing a list of MovieAwardInfoResponse objects with details about
   *     the movie's award status
   */
  @GetMapping("/{title}/oscar")
  public ResponseEntity<List<MovieAwardInfoResponse>> checkMovieWonAwardsByTitle(
      @PathVariable final String title,
      @RequestParam(name = "category", required = false) final String category) {

    final var movieAwards =
        nominationInfoService.getMovieAwardInfo(title, getCategory(category)).stream()
            .map(movieMapper::toMovieAwardInfoResponse)
            .toList();

    return ResponseEntity.ok(movieAwards);
  }

  private String getCategory(final String category) {
    return (category == null) ? "best picture" : category.toLowerCase();
  }
}
