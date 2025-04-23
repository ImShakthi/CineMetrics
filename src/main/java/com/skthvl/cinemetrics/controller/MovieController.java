package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.response.MovieAwardInfoResponse;
import com.skthvl.cinemetrics.model.response.MovieInfoResponse;
import com.skthvl.cinemetrics.service.NominationInfoService;
import jakarta.validation.constraints.NotBlank;
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

  private final OmdbApiClient omdbApiClient;
  private final MovieMapper movieMapper;
  private final NominationInfoService nominationInfoService;

  public MovieController(
      final OmdbApiClient omdbApiClient,
      final MovieMapper movieMapper,
      final NominationInfoService movieInfoService) {
    this.omdbApiClient = omdbApiClient;
    this.movieMapper = movieMapper;
    this.nominationInfoService = movieInfoService;
  }

  @GetMapping("/{title}")
  public ResponseEntity<MovieInfoResponse> getMovieDetailsByTitle(
      @PathVariable @NotBlank(message = "title must not be empty") final String title) {
    final var movieDetails = omdbApiClient.getMoveDetailsByTitle(title);

    log.info("Movie Details: {}", movieDetails);

    return ResponseEntity.ok(movieMapper.toMovieInfoResponse(movieDetails));
  }

  @GetMapping("/{title}/oscar/")
  public ResponseEntity<List<MovieAwardInfoResponse>> checkMovieWonBestPictureAward(
      @PathVariable @NotBlank(message = "title must not be empty") final String title,
      @RequestParam(name = "category", required = false) final String category) {

    final var movieAwards =
        nominationInfoService.getMovieAwardInfo(title, getCategory(category)).stream()
            .map(movieMapper::toMovieAwardInfoResponse)
            .toList();

    return ResponseEntity.ok(movieAwards);
  }

  private String getCategory(final String category) {
    return (category == null) ? "best picture" : category;
  }
}
