package com.skthvl.cinemetrics.mapper;

import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.model.dto.MovieAwardDto;
import com.skthvl.cinemetrics.model.dto.MovieDto;
import com.skthvl.cinemetrics.model.response.MovieAwardInfoResponse;
import com.skthvl.cinemetrics.model.response.MovieInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between movie-related data models. Utilizes MapStruct for
 * generating implementations. This interface serves as a central place to handle mapping logic
 * between entity models, DTOs, and responses.
 */
@Mapper(componentModel = "spring")
public interface MovieMapper {

  /**
   * Maps a {@link MovieDto} object to a {@link MovieInfoResponse} object.
   *
   * @param response the {@link MovieDto} object containing movie details to be converted
   * @return the corresponding {@link MovieInfoResponse} object
   */
  MovieInfoResponse toMovieInfoResponse(final MovieDto response);

  /**
   * Maps a {@link MovieAwardDto} object to a {@link MovieAwardInfoResponse} object.
   *
   * @param response the {@link MovieAwardDto} object containing award-related details to be
   *     converted
   * @return the corresponding {@link MovieAwardInfoResponse} object
   */
  @Mapping(target = "releaseYear", source = "releaseYear")
  @Mapping(target = "hasWon", source = "hasWon")
  MovieAwardInfoResponse toMovieAwardInfoResponse(final MovieAwardDto response);

  /**
   * Converts a {@link Movie} entity to a {@link MovieDto}.
   *
   * @param movie the {@link Movie} entity to be converted
   * @return the corresponding {@link MovieDto} object
   */
  @Mapping(target = "movieId", source = "id")
  MovieDto toMovieDto(final Movie movie);

  /**
   * Maps a {@link MovieDetailsResponse} object to a {@link Movie} object. This method uses custom
   * expressions to transform certain fields during the mapping process.
   *
   * @param response the {@link MovieDetailsResponse} object containing movie details to be
   *     converted
   * @return the corresponding {@link Movie} entity with mapped fields
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "releaseYear", expression = "java(Integer.parseInt(response.getYear()))")
  @Mapping(target = "boxOfficeAmountUsd", expression = "java(response.parseBoxOffice())")
  Movie toMovie(final MovieDetailsResponse response);
}
