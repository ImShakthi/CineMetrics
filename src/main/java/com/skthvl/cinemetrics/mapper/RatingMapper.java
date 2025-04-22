package com.skthvl.cinemetrics.mapper;

import com.skthvl.cinemetrics.model.dto.RatingDto;
import com.skthvl.cinemetrics.model.dto.TopRatedMovieDto;
import com.skthvl.cinemetrics.model.response.RatingResponse;
import com.skthvl.cinemetrics.model.response.TopRatedMovieResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {
  @Mapping(target = "rating", source = "rating")
  @Mapping(target = "movieTitle", source = "movieTitle")
  @Mapping(target = "userName", source = "userName")
  @Mapping(target = "comment", source = "comment")
  RatingResponse to(final RatingDto rating);

  List<RatingResponse> toRatingResponse(final List<RatingDto> rating);

  @Mapping(target = "title", source = "title")
  @Mapping(target = "averageRating", source = "averageRating")
  @Mapping(target = "boxOfficeValue", source = "boxOfficeAmountUsd")
  TopRatedMovieResponse to(final TopRatedMovieDto topRatedMovieDto);

  List<TopRatedMovieResponse> toTopRatedMovieResponse(
      final List<TopRatedMovieDto> topRatedMovieDtos);
}
