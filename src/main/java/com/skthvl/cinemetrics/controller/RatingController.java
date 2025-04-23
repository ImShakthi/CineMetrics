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
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/ratings")
@Validated
public class RatingController {

  private final RatingService ratingService;
  private final RatingMapper ratingMapper;

  public RatingController(final RatingService ratingService, final RatingMapper ratingMapper) {
    this.ratingService = ratingService;
    this.ratingMapper = ratingMapper;
  }

  @GetMapping("/{title}")
  public ResponseEntity<List<RatingResponse>> getRatings(
      @PathVariable @NotBlank(message = "title must not be empty") final String title) {
    final var ratingDto = ratingService.getRatingInfo(title);

    return ResponseEntity.ok(ratingMapper.toRatingResponse(ratingDto));
  }

  @SecurityRequirement(name = "bearerAuth")
  @PostMapping()
  public ResponseEntity<MessageResponse> createRating(
      final Authentication authentication, @RequestBody @Valid final CreateRatingRequest request) {

    final var userName = authentication.getName();
    log.info("User name: {}", userName);

    final AddRatingDto rating =
        AddRatingDto.builder()
            .movieId(request.getMovieId())
            .rating(request.getRating())
            .comment(request.getComment())
            .userName(userName)
            .build();

    ratingService.addRating(rating);

    return ResponseEntity.ok(new MessageResponse("rating created successfully"));
  }

  @GetMapping("/top-10")
  public ResponseEntity<List<TopRatedMovieResponse>> getTop10Ratings() {

    final var topRatedMovies = ratingService.getTop10RatedMovies();

    return ResponseEntity.ok(ratingMapper.toTopRatedMovieResponse(topRatedMovies));
  }
}
