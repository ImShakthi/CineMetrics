package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.response.MovieAwardInfoResponse;
import com.skthvl.cinemetrics.model.response.MovieInfoResponse;
import com.skthvl.cinemetrics.service.NominationInfoService;
import java.util.List;
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
  public ResponseEntity<MovieInfoResponse> getMovieDetailsByTitle(@PathVariable String title) {
    final var movieDetails = omdbApiClient.getMoveDetailsByTitle(title);

    log.info("Movie Details: {}", movieDetails);

    return ResponseEntity.ok(movieMapper.toMovieInfoResponse(movieDetails));
  }

  @GetMapping("/{title}/oscar")
  public ResponseEntity<List<MovieAwardInfoResponse>> checkMovieWonBestPictureAward(
      @PathVariable final String title) {
    final var movieAwards =
        nominationInfoService.getMovieAwardInfo(title).stream()
            .map(movieMapper::toMovieAwardInfoResponse)
            .toList();

    return ResponseEntity.ok(movieAwards);
  }
}
