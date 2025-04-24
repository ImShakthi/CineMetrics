package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.dto.MovieDto;
import com.skthvl.cinemetrics.repository.MovieRepository;
import java.math.BigInteger;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MovieService {

  private final OmdbApiClient omdbApiClient;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public MovieService(
      final OmdbApiClient omdbApiClient,
      final MovieRepository movieRepository,
      final MovieMapper movieMapper) {
    this.omdbApiClient = omdbApiClient;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  @Transactional
  public MovieDto getMovieInfoByTitle(final String title) {
    final Movie movie = findOrFetchAndSaveByTitle(title);
    return movieMapper.toMovieDto(movie);
  }

  @Transactional
  public Movie getMovieById(final BigInteger id) {
    return movieRepository
        .findById(id)
        .map(this::reloadIfBoxOfficeEmpty)
        .orElseThrow(MovieNotFoundException::new);
  }

  private Movie reloadIfBoxOfficeEmpty(final Movie movie) {
    if (!movie.isBoxOfficeEmpty()) {
      return movie;
    }

    final Movie refreshedMovie = fetchMovieFromApi(movie.getTitle());
    refreshedMovie.setId(movie.getId()); // Preserve original ID
    return movieRepository.save(refreshedMovie);
  }

  private Movie findOrFetchAndSaveByTitle(final String title) {
    Optional<Movie> existingMovie = movieRepository.findMovieByTitle(title);
    if (existingMovie.isPresent() && !existingMovie.get().isBoxOfficeEmpty()) {
      return existingMovie.get();
    }

    final Movie newMovie = fetchMovieFromApi(title);

    existingMovie.ifPresent(movie -> newMovie.setId(movie.getId()));

    return movieRepository.save(newMovie);
  }

  private Movie fetchMovieFromApi(final String title) {
    final var response = omdbApiClient.getMoveDetailsByTitle(title);
    if (response == null) {
      throw new MovieNotFoundException("Movie not found in OMDb API for title: " + title);
    }
    return movieMapper.toMovie(response);
  }
}
