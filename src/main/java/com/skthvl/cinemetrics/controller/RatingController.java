package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.mapper.RatingMapper;
import com.skthvl.cinemetrics.model.response.RatingResponse;
import com.skthvl.cinemetrics.service.RatingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/movies")
public class RatingController {

  private final RatingService ratingService;
  private final RatingMapper ratingMapper;

  public RatingController(final RatingService ratingService, final RatingMapper ratingMapper) {
    this.ratingService = ratingService;
    this.ratingMapper = ratingMapper;
  }

  @GetMapping("/{title}/ratings")
  public ResponseEntity<List<RatingResponse>> getRatings(@PathVariable final String title) {
    final var ratingDto = ratingService.getRatingInfo(title);

    return ResponseEntity.ok(ratingMapper.toRatingResponse(ratingDto));
  }

  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/{title}/ratings")
  public ResponseEntity<String> creatRating(
      final Authentication authentication, @PathVariable final String title) {

    final var userName = authentication.getName();
    log.info("User name: {}", userName);

    return ResponseEntity.ok(String.format("Rating created for %s", title));
  }
}
