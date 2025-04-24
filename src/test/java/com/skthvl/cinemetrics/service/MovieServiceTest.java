package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.dto.MovieDto;
import com.skthvl.cinemetrics.repository.MovieRepository;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock private MovieRepository movieRepository;
  @Mock private OmdbApiClient omdbApiClient;
  @Mock private MovieMapper movieMapper;

  @InjectMocks private MovieService movieService;

  @Test
  void getMovieInfoByTitle_shouldReturnDtoIfMovieInDbWithBoxOffice() {
    // Given
    String title = "Inception";
    Movie movie = mock(Movie.class);
    MovieDto dto = new MovieDto();

    when(movieRepository.findMovieByTitle(title)).thenReturn(Optional.of(movie));
    when(movie.isBoxOfficeEmpty()).thenReturn(false);
    when(movieMapper.toMovieDto(movie)).thenReturn(dto);

    // When
    MovieDto result = movieService.getMovieInfoByTitle(title);

    // Then
    assertEquals(dto, result);
    verify(omdbApiClient, never()).getMoveDetailsByTitle(any());
  }

  @Test
  void getMovieInfoByTitle_shouldFetchFromOmdbIfBoxOfficeEmpty() {
    // Given
    String title = "Tenet";
    Movie existingMovie = mock(Movie.class);
    MovieDetailsResponse response = new MovieDetailsResponse();
    Movie newMovie = new Movie();
    MovieDto dto = new MovieDto();

    when(movieRepository.findMovieByTitle(title)).thenReturn(Optional.of(existingMovie));
    when(existingMovie.isBoxOfficeEmpty()).thenReturn(true);

    when(omdbApiClient.getMoveDetailsByTitle(title)).thenReturn(response);
    when(movieMapper.toMovie(response)).thenReturn(newMovie);
    when(movieRepository.save(newMovie)).thenReturn(newMovie);
    when(movieMapper.toMovieDto(newMovie)).thenReturn(dto);

    // When
    MovieDto result = movieService.getMovieInfoByTitle(title);

    // Then
    assertEquals(dto, result);
    verify(movieRepository).save(newMovie);
  }

  @Test
  void getMovieInfoByTitle_shouldFetchFromOmdbIfNotFoundInDb() {
    // Given
    String title = "Dune";
    MovieDetailsResponse response = new MovieDetailsResponse();
    Movie movie = new Movie();
    MovieDto dto = new MovieDto();

    when(movieRepository.findMovieByTitle(title)).thenReturn(Optional.empty());
    when(omdbApiClient.getMoveDetailsByTitle(title)).thenReturn(response);
    when(movieMapper.toMovie(response)).thenReturn(movie);
    when(movieRepository.save(movie)).thenReturn(movie);
    when(movieMapper.toMovieDto(movie)).thenReturn(dto);

    // When
    MovieDto result = movieService.getMovieInfoByTitle(title);

    // Then
    assertEquals(dto, result);
    verify(movieRepository).save(movie);
  }

  @Test
  void getMovieById_shouldReturnMovieIfPresentWithBoxOffice() {
    // Given
    BigInteger id = BigInteger.ONE;
    Movie movie = mock(Movie.class);
    when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
    when(movie.isBoxOfficeEmpty()).thenReturn(false);

    // When
    Movie result = movieService.getMovieById(id);

    // Then
    assertEquals(movie, result);
    verify(omdbApiClient, never()).getMoveDetailsByTitle(any());
  }

  @Test
  void getMovieById_shouldFetchFromOmdbIfBoxOfficeEmpty() {
    // Given
    BigInteger id = BigInteger.ONE;
    String title = "Gladiator";
    Movie existingMovie = Movie.builder().title(title).boxOfficeAmountUsd(BigInteger.ZERO).build();
    MovieDetailsResponse response = new MovieDetailsResponse();
    Movie updatedMovie = new Movie();

    when(movieRepository.findById(id)).thenReturn(Optional.of(existingMovie));
    when(omdbApiClient.getMoveDetailsByTitle(title)).thenReturn(response);
    when(movieMapper.toMovie(response)).thenReturn(updatedMovie);
    when(movieRepository.save(updatedMovie)).thenReturn(updatedMovie);

    // When
    Movie result = movieService.getMovieById(id);

    // Then
    assertEquals(updatedMovie, result);
    verify(movieRepository).save(updatedMovie);
  }

  @Test
  void getMovieById_shouldThrowIfMovieNotFound() {
    // Given
    BigInteger id = BigInteger.valueOf(999);
    when(movieRepository.findById(id)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(id));
  }

  @Test
  void getMovieInfoByTitle_shouldThrowIfOmdbReturnsNull() {
    // Given
    String title = "NonExistent";
    when(movieRepository.findMovieByTitle(title)).thenReturn(Optional.empty());
    when(omdbApiClient.getMoveDetailsByTitle(title)).thenReturn(null);

    // When & Then
    assertThrows(MovieNotFoundException.class, () -> movieService.getMovieInfoByTitle(title));
  }
}
