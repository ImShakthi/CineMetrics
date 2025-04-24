package com.skthvl.cinemetrics.mapper;

import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
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
  @Mapping(target = "year", source = "year")
  @Mapping(target = "rated", source = "rated")
  @Mapping(target = "released", source = "released")
  MovieInfoResponse toMovieInfoResponse(final MovieDto response);

  @Mapping(target = "releaseYear", source = "releaseYear")
  @Mapping(target = "hasWon", source = "hasWon")
  MovieAwardInfoResponse toMovieAwardInfoResponse(final MovieAwardDto response);

  @Mapping(target = "movieId", source = "movieId")
  @Mapping(target = "title", source = "response.title")
  @Mapping(target = "year", source = "response.year")
  @Mapping(target = "rated", source = "response.rated")
  @Mapping(target = "released", source = "response.released")
  MovieDto toMovieDto(final MovieDetailsResponse response, final String movieId);
}
