package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.entity.Nomination;
import com.skthvl.cinemetrics.repository.DataFileMigrationRepository;
import com.skthvl.cinemetrics.repository.MovieRepository;
import com.skthvl.cinemetrics.repository.NominationRepository;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AcademyAwardLoaderTest {

  @Mock private MovieRepository movieRepository;

  @Mock private NominationRepository nominationRepository;

  @Mock private DataFileMigrationRepository dataFileMigrationRepository;

  @InjectMocks private AcademyAwardLoader testLoader;

  @BeforeEach
  void setUp() {
    testLoader =
        new AcademyAwardLoader(movieRepository, nominationRepository, dataFileMigrationRepository);

    ReflectionTestUtils.setField(testLoader, "csvPath", "test-data-academy-awards.csv");
  }

  @Test
  void testExecute_shouldSaveBestPictureNominations() {
    // Given
    Movie mockMovie =
        Movie.builder()
            .title("Forrest Gump")
            .releaseYear(1994)
            .boxOfficeAmountUsd(BigInteger.ZERO)
            .build();

    Movie mockMovie2 =
        Movie.builder()
            .title("Cavalcade")
            .releaseYear(1933)
            .boxOfficeAmountUsd(BigInteger.ZERO)
            .build();

    when(movieRepository.findMovieByTitleAndReleaseYear("Forrest Gump", 1994))
        .thenReturn(List.of(mockMovie));
    when(movieRepository.findMovieByTitleAndReleaseYear("Cavalcade", 1933)).thenReturn(List.of());
    when(movieRepository.save(any(Movie.class))).thenReturn(mockMovie2);

    // When
    testLoader.loadOscarNominations();

    // Then
    ArgumentCaptor<List<Nomination>> captor = ArgumentCaptor.forClass(List.class);
    verify(nominationRepository).saveAll(captor.capture());
    final List<Nomination> saved = captor.getValue();

    assertEquals(2, saved.size());
    assertEquals("Forrest Gump", saved.getFirst().getNominee());
    assertTrue(saved.getFirst().isHasWon());
    assertEquals(1994, saved.getFirst().getReleaseYear());

    assertEquals("Cavalcade", saved.getLast().getNominee());
    assertEquals(1933, saved.getLast().getReleaseYear());
  }
}
