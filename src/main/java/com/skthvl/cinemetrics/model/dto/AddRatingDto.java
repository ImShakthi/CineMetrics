package com.skthvl.cinemetrics.model.dto;

import lombok.Builder;

@Builder
public record AddRatingDto(int rating, Long movieId, String userName, String comment) {}
