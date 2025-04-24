package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.client.omdb.OmdbApiClient;
import com.skthvl.cinemetrics.exception.MovieNotFoundException;
import com.skthvl.cinemetrics.mapper.MovieMapper;
import com.skthvl.cinemetrics.model.dto.MovieDto;
import com.skthvl.cinemetrics.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  public MovieDto getMovieInfoByTitle(final String title) {
    final var movieDetails = omdbApiClient.getMoveDetailsByTitle(title);

    final var movie =
        movieRepository
            .findMovieByTitleAndReleaseYear(title, Integer.parseInt(movieDetails.getYear()))
            .stream()
            .findFirst()
            .orElseThrow(MovieNotFoundException::new);
    log.debug("Movie exists: {}", movie.getTitle());
    return movieMapper.toMovieDto(movieDetails, movie.getId().toString());
  }
}
