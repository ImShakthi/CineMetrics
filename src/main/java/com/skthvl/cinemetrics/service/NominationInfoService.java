package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.model.dto.MovieAwardDto;
import com.skthvl.cinemetrics.repository.NominationRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NominationInfoService {

  private final NominationRepository nominationRepository;

  public NominationInfoService(final NominationRepository nominationRepository) {
    this.nominationRepository = nominationRepository;
  }

  public List<MovieAwardDto> getMovieAwardInfo(final String title) {
    final var awardDetails = nominationRepository.findAwardDetailsByTitleIgnoreCase(title);
    if (awardDetails.isEmpty()) {
      throw new MovieNotFoundException("Movie not found for title: " + title);
    }
    return awardDetails;
  }
}
