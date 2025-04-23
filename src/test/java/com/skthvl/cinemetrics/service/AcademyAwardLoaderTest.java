package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.entity.Nomination;
import com.skthvl.cinemetrics.repository.MovieRepository;
import com.skthvl.cinemetrics.repository.NominationRepository;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AcademyAwardLoaderTest {

  @Mock private MovieRepository movieRepository;

  @Mock private NominationRepository nominationRepository;

  @InjectMocks private AcademyAwardLoader testLoader;

  @BeforeEach
  void setUp() {
    testLoader = new AcademyAwardLoader(movieRepository, nominationRepository);

    ReflectionTestUtils.setField(testLoader, "csvPath", "test-data-academy-awards.csv");
  }

  @Test
  void testExecute_shouldSaveBestPictureNominations() {

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
    when(movieRepository.findMovieByTitleAndReleaseYear("Cavalcade", 1933))
        .thenReturn(List.of());
    when(movieRepository.save(any(Movie.class))).thenReturn(mockMovie2);

    // Act
    testLoader.execute();

    // Assert
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
