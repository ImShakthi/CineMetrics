package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.mapper.RatingMapper;
import com.skthvl.cinemetrics.model.response.RatingResponse;
import com.skthvl.cinemetrics.service.RatingService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
