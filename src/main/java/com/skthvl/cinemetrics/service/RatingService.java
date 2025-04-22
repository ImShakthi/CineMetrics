package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.repository.RatingRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RatingService {
  private final RatingRepository ratingRepository;

  public RatingService(final RatingRepository ratingRepository) {
    this.ratingRepository = ratingRepository;
  }

  public List<RatingDto> getRatingInfo(final String title) {
    return ratingRepository.findRatingDetailsByTitleIgnoreCase(title);
  }
}
