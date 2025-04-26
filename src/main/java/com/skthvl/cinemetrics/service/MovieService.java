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

/**
 * Service for managing movie operations, integrating local database storage with OMDB API data.
 * Handles fetching, caching, and retrieval of movie information.
 *
 * @see com.skthvl.cinemetrics.client.omdb.OmdbApiClient
 * @see com.skthvl.cinemetrics.repository.MovieRepository
 */
@Slf4j
@Service
public class MovieService {

  private final OmdbApiClient omdbApiClient;
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  /**
   * Creates a new MovieService instance.
   *
   * @param omdbApiClient client for fetching movie data from OMDB API
   * @param movieRepository repository for movie data persistence
   * @param movieMapper mapper for converting between entities and DTOs
   * @throws IllegalArgumentException if any of the required dependencies are null
   * @since 1.0
   */
  public MovieService(
      final OmdbApiClient omdbApiClient,
      final MovieRepository movieRepository,
      final MovieMapper movieMapper) {
    this.omdbApiClient = omdbApiClient;
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  /**
   * Retrieves movie information by its title. If the movie is not found in the local database, it
   * fetches the information from an external API and stores it in the database.
   *
   * @param title the title of the movie to retrieve
   * @return a {@link MovieDto} containing the movie details
   * @throws MovieNotFoundException if the movie is not found in the database or external API
   */
  @Transactional
  public MovieDto getMovieInfoByTitle(final String title) {
    final Movie movie = findOrFetchAndSaveByTitle(title);
    return movieMapper.toMovieDto(movie);
  }

  /**
   * Retrieves a movie by its unique identifier. If the movie's box office information is missing,
   * it updates the movie data by reloading from an external API. Throws an exception if the movie
   * is not found.
   *
   * @param id the unique identifier of the movie to retrieve
   * @return the {@link Movie} entity with the specified ID
   * @throws MovieNotFoundException if the movie with the specified ID is not found
   */
  @Transactional
  public Movie getMovieById(final BigInteger id) {
    return movieRepository
        .findById(id)
        .map(this::reloadIfBoxOfficeEmpty)
        .orElseThrow(MovieNotFoundException::new);
  }

  /**
   * Reloads movie data from API if box office information is missing.
   *
   * @param movie the movie entity to check and potentially reload
   * @return updated movie entity with complete information
   */
  private Movie reloadIfBoxOfficeEmpty(final Movie movie) {
    if (!movie.isBoxOfficeEmpty()) {
      return movie;
    }

    final Movie refreshedMovie = fetchMovieFromApi(movie.getTitle());
    refreshedMovie.setId(movie.getId()); // Preserve original ID
    return movieRepository.save(refreshedMovie);
  }

  /**
   * Finds movie by title in database or fetches it from API.
   *
   * @param title the movie title to search for
   * @return movie entity from database or newly fetched from API
   */
  private Movie findOrFetchAndSaveByTitle(final String title) {
    Optional<Movie> existingMovie = movieRepository.findMovieByTitle(title);
    if (existingMovie.isPresent() && !existingMovie.get().isBoxOfficeEmpty()) {
      return existingMovie.get();
    }

    final Movie newMovie = fetchMovieFromApi(title);

    existingMovie.ifPresent(movie -> newMovie.setId(movie.getId()));

    return movieRepository.save(newMovie);
  }

  /**
   * Fetches movie information from OMDB API.
   *
   * @param title the movie title to fetch
   * @return movie entity created from API response
   * @throws MovieNotFoundException if movie is not found in API
   */
  private Movie fetchMovieFromApi(final String title) {
    final var response = omdbApiClient.getMoveDetailsByTitle(title);
    if (response == null) {
      throw new MovieNotFoundException("Movie not found in OMDb API for title: " + title);
    }
    return movieMapper.toMovie(response);
  }
}
