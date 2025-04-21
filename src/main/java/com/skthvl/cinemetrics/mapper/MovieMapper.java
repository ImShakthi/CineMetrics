package com.skthvl.cinemetrics.mapper;

import com.skthvl.cinemetrics.client.omdb.dto.MovieDetailsResponse;
import com.skthvl.cinemetrics.model.response.MovieInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieMapper {

  @Mapping(target = "title", source = "title")
  @Mapping(target = "year", source = "year")
  @Mapping(target = "rated", source = "rated")
  @Mapping(target = "released", source = "released")
  MovieInfoResponse toMovieInfoResponse(final MovieDetailsResponse response);
}
