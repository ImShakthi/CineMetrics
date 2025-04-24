package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.dto.MovieDto;
import com.skthvl.cinemetrics.repository.MovieRepository;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock private OmdbApiClient omdbApiClient;
  @Mock private MovieRepository movieRepository;
  @Mock private MovieMapper movieMapper;

  @InjectMocks private MovieService movieService;

  @Test
  void shouldReturnMovieDto_whenMovieExists() {
    final String title = "Inception";
    final String releaseYear = "2010";

    final MovieDetailsResponse mockResponse =
        MovieDetailsResponse.builder()
            .title(title)
            .year(releaseYear)
            .rated("PG-13")
            .released("16 Jul 2010")
            .build();

    final Movie mockMovie =
        Movie.builder()
            .id(BigInteger.ONE)
            .title(title)
            .releaseYear(Integer.parseInt(releaseYear))
            .build();

    final MovieDto expectedDto =
        new MovieDto(title, releaseYear, "PG-13", "16 Jul 2010", mockMovie.getId().toString());

    when(omdbApiClient.getMoveDetailsByTitle(title)).thenReturn(mockResponse);
    when(movieRepository.findMovieByTitleAndReleaseYear(title, 2010))
        .thenReturn(List.of(mockMovie));
    when(movieMapper.toMovieDto(mockResponse, mockMovie.getId().toString()))
        .thenReturn(expectedDto);

    final MovieDto result = movieService.getMovieInfoByTitle(title);

    assertNotNull(result);
    assertEquals(expectedDto.getTitle(), result.getTitle());
    assertEquals(expectedDto.getMovieId(), result.getMovieId());
  }

  @Test
  void shouldThrowException_whenMovieNotFound() {
    final String title = "Unknown";
    final String releaseYear = "1999";

    final MovieDetailsResponse mockResponse =
        MovieDetailsResponse.builder().title(title).year(releaseYear).build();

    when(omdbApiClient.getMoveDetailsByTitle(title)).thenReturn(mockResponse);
    when(movieRepository.findMovieByTitleAndReleaseYear(title, 1999)).thenReturn(List.of());

    assertThrows(MovieNotFoundException.class, () -> movieService.getMovieInfoByTitle(title));
  }
}
