package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.mapper.RatingMapper;
import com.skthvl.cinemetrics.model.dto.AddRatingDto;
import com.skthvl.cinemetrics.model.request.CreateRatingRequest;
import com.skthvl.cinemetrics.model.response.MessageResponse;
import com.skthvl.cinemetrics.model.response.RatingResponse;
import com.skthvl.cinemetrics.model.response.TopRatedMovieResponse;
import com.skthvl.cinemetrics.service.RatingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Validated
public class RatingController {

  private final RatingService ratingService;
  private final RatingMapper ratingMapper;

  public RatingController(final RatingService ratingService, final RatingMapper ratingMapper) {
    this.ratingService = ratingService;
    this.ratingMapper = ratingMapper;
  }

  @GetMapping("/movies/{title}/ratings")
  public ResponseEntity<List<RatingResponse>> getRatings(
      @PathVariable(name = "title") @NotBlank(message = "title must not be empty")
          final String title) {
    log.info("Getting ratings for movie: {}", title);
    final var ratingDto = ratingService.getRatingInfo(title);

    return ResponseEntity.ok(ratingMapper.toRatingResponse(ratingDto));
  }

  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/movies/{movieId}/ratings")
  public ResponseEntity<MessageResponse> createRating(
      final Authentication authentication,
      @PathVariable(name = "movieId") @NotBlank(message = "title must not be empty")
          final String movieId,
      @RequestBody @Valid final CreateRatingRequest request) {

    if (authentication == null) {
      return ResponseEntity.status(401).body(new MessageResponse("authentication is required"));
    }

    final var userName = authentication.getName();
    log.info("User name: {}", userName);

    final AddRatingDto rating =
        AddRatingDto.builder()
            .movieId(new BigInteger(movieId))
            .rating(request.getRating())
            .comment(request.getComment())
            .userName(userName)
            .build();

    final var ratingDto = ratingService.addRating(rating);

    return ResponseEntity.ok(
        new MessageResponse("added rating to movie: " + ratingDto.getMovieTitle()));
  }

  @GetMapping("/ratings/top")
  public ResponseEntity<List<TopRatedMovieResponse>> getTop10Ratings(
      @RequestParam(name = "limit") final int limit) {
    log.info("Getting top 10 rated movies");
    final var topRatedMovies = ratingService.getTopRatedMovies(limit);
    log.info("Top 10 rated movies: {}", topRatedMovies);

    return ResponseEntity.ok(ratingMapper.toTopRatedMovieResponse(topRatedMovies));
  }
}
