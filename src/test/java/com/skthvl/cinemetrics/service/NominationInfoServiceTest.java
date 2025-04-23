package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.model.dto.MovieAwardDto;
import com.skthvl.cinemetrics.repository.NominationRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NominationInfoServiceTest {

  @Mock private NominationRepository nominationRepository;

  private NominationInfoService nominationInfoService;

  @BeforeEach
  void setUp() {
    nominationInfoService = new NominationInfoService(nominationRepository);
  }

  @Test
  void getMovieAwardInfo_shouldReturnAwardDetails_whenMovieExists() {
    // Given
    String title = "Forrest Gump";
    String category = "Best Picture";
    MovieAwardDto awardDto = new MovieAwardDto(1994, true);
    when(nominationRepository.findAwardDetailsByTitleIgnoreCase(title, category))
        .thenReturn(List.of(awardDto));

    // When
    final List<MovieAwardDto> result = nominationInfoService.getMovieAwardInfo(title, category);

    // Then
    assertEquals(1, result.size());
    assertTrue(result.getFirst().isHasWon());
    verify(nominationRepository).findAwardDetailsByTitleIgnoreCase(title, category);
  }

  @Test
  void getMovieAwardInfo_shouldThrowException_whenMovieNotFound() {
    // Given
    String title = "Unknown Movie";
    String category = "Best Picture";
    when(nominationRepository.findAwardDetailsByTitleIgnoreCase(title, category))
        .thenReturn(List.of());

    // When & Then
    MovieNotFoundException exception =
        assertThrows(
            MovieNotFoundException.class,
            () -> nominationInfoService.getMovieAwardInfo(title, category));

    assertEquals("Movie not found for title: Unknown Movie", exception.getMessage());
    verify(nominationRepository).findAwardDetailsByTitleIgnoreCase(title, category);
  }
}
