package com.skthvl.cinemetrics.mapper;

import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
import com.skthvl.cinemetrics.entity.Movie;
import com.skthvl.cinemetrics.model.dto.MovieAwardDto;
import com.skthvl.cinemetrics.model.dto.MovieDto;
import com.skthvl.cinemetrics.model.response.MovieAwardInfoResponse;
import com.skthvl.cinemetrics.model.response.MovieInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {

  @Mapping(target = "movieId", source = "movieId")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "releaseYear", source = "releaseYear")
  @Mapping(target = "boxOfficeAmountUsd", source = "boxOfficeAmountUsd")
  @Mapping(target = "rated", source = "rated")
  @Mapping(target = "released", source = "released")
  @Mapping(target = "runtime", source = "runtime")
  @Mapping(target = "genre", source = "genre")
  @Mapping(target = "director", source = "director")
  @Mapping(target = "writer", source = "writer")
  @Mapping(target = "actors", source = "actors")
  @Mapping(target = "plot", source = "plot")
  @Mapping(target = "language", source = "language")
  @Mapping(target = "country", source = "country")
  @Mapping(target = "awards", source = "awards")
  @Mapping(target = "poster", source = "poster")
  @Mapping(target = "metascore", source = "metascore")
  @Mapping(target = "imdbRating", source = "imdbRating")
  @Mapping(target = "imdbVotes", source = "imdbVotes")
  @Mapping(target = "imdbID", source = "imdbID")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "dvd", source = "dvd")
  @Mapping(target = "production", source = "production")
  @Mapping(target = "website", source = "website")
  MovieInfoResponse toMovieInfoResponse(final MovieDto response);

  @Mapping(target = "releaseYear", source = "releaseYear")
  @Mapping(target = "hasWon", source = "hasWon")
  MovieAwardInfoResponse toMovieAwardInfoResponse(final MovieAwardDto response);

  @Mapping(target = "movieId", source = "id")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "releaseYear", source = "releaseYear")
  @Mapping(target = "rated", source = "rated")
  @Mapping(target = "released", source = "released")
  @Mapping(target = "runtime", source = "runtime")
  @Mapping(target = "genre", source = "genre")
  @Mapping(target = "director", source = "director")
  @Mapping(target = "writer", source = "writer")
  @Mapping(target = "actors", source = "actors")
  @Mapping(target = "plot", source = "plot")
  @Mapping(target = "language", source = "language")
  @Mapping(target = "country", source = "country")
  @Mapping(target = "awards", source = "awards")
  @Mapping(target = "poster", source = "poster")
  @Mapping(target = "metascore", source = "metascore")
  @Mapping(target = "imdbRating", source = "imdbRating")
  @Mapping(target = "imdbVotes", source = "imdbVotes")
  @Mapping(target = "imdbID", source = "imdbID")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "dvd", source = "dvd")
  @Mapping(target = "boxOfficeAmountUsd", source = "boxOfficeAmountUsd")
  @Mapping(target = "production", source = "production")
  @Mapping(target = "website", source = "website")
  MovieDto toMovieDto(final Movie movie);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "title", source = "title")
  @Mapping(target = "releaseYear", expression = "java(Integer.parseInt(response.getYear()))")
  @Mapping(target = "rated", source = "rated")
  @Mapping(target = "released", source = "released")
  @Mapping(target = "runtime", source = "runtime")
  @Mapping(target = "genre", source = "genre")
  @Mapping(target = "director", source = "director")
  @Mapping(target = "writer", source = "writer")
  @Mapping(target = "actors", source = "actors")
  @Mapping(target = "plot", source = "plot")
  @Mapping(target = "language", source = "language")
  @Mapping(target = "country", source = "country")
  @Mapping(target = "awards", source = "awards")
  @Mapping(target = "poster", source = "poster")
  @Mapping(target = "metascore", source = "metascore")
  @Mapping(target = "imdbRating", source = "imdbRating")
  @Mapping(target = "imdbVotes", source = "imdbVotes")
  @Mapping(target = "imdbID", source = "imdbID")
  @Mapping(target = "type", source = "type")
  @Mapping(target = "dvd", source = "dvd")
  @Mapping(target = "boxOfficeAmountUsd", expression = "java(response.parseBoxOffice())")
  @Mapping(target = "production", source = "production")
  @Mapping(target = "website", source = "website")
  Movie toMovie(final MovieDetailsResponse response);
}
