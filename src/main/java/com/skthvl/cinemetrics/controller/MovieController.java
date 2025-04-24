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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
@Slf4j
@Validated
public class MovieController {

  private final MovieService movieService;
  private final NominationInfoService nominationInfoService;
  private final MovieMapper movieMapper;

  public MovieController(
          final MovieService movieService, final MovieMapper movieMapper,
          final NominationInfoService movieInfoService) {
      this.movieService = movieService;
      this.movieMapper = movieMapper;
    this.nominationInfoService = movieInfoService;
  }

  //TODO: get movieid
  @GetMapping("/search")
  public ResponseEntity<MovieInfoResponse> getMovieDetailsByTitle(
      @RequestParam(value = "title") final String title) {
    final var movieDetails = movieService.getMovieInfoByTitle(title);

    log.info("Movie Details: {}", movieDetails);

    return ResponseEntity.ok(movieMapper.toMovieInfoResponse(movieDetails));
  }

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
