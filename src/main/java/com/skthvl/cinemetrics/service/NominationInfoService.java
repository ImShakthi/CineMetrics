package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.model.dto.MovieAwardDto;
import com.skthvl.cinemetrics.repository.NominationRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class that handles movie award and nomination information retrieval operations.
 */
@Service
@Slf4j
public class NominationInfoService {

  private final NominationRepository nominationRepository;

  /**
   * Constructs NominationInfoService with required repository dependency.
   *
   * @param nominationRepository repository for accessing nomination data
   */
  public NominationInfoService(final NominationRepository nominationRepository) {
    this.nominationRepository = nominationRepository;
  }

  /**
   * Retrieves award information for a specific movie and category.
   *
   * @param title movie title to search for
   * @param category award category to filter by
   * @return list of movie awards matching the criteria
   * @throws MovieNotFoundException if no awards found for the given title
   */
  public List<MovieAwardDto> getMovieAwardInfo(final String title, final String category) {
    final var awardDetails =
        nominationRepository.findAwardDetailsByTitleIgnoreCase(title, category);
    if (awardDetails.isEmpty()) {
      throw new MovieNotFoundException("Movie not found for title: " + title);
    }
    return awardDetails;
  }
}
