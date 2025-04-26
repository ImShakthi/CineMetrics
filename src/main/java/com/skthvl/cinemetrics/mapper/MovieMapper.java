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

  MovieInfoResponse toMovieInfoResponse(final MovieDto response);

  @Mapping(target = "releaseYear", source = "releaseYear")
  @Mapping(target = "hasWon", source = "hasWon")
  MovieAwardInfoResponse toMovieAwardInfoResponse(final MovieAwardDto response);

  @Mapping(target = "movieId", source = "id")
  MovieDto toMovieDto(final Movie movie);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "releaseYear", expression = "java(Integer.parseInt(response.getYear()))")
  @Mapping(target = "boxOfficeAmountUsd", expression = "java(response.parseBoxOffice())")
  Movie toMovie(final MovieDetailsResponse response);
}
