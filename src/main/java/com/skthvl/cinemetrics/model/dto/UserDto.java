package com.skthvl.cinemetrics.model.dto;

import lombok.Builder;

@Builder
public record UserDto(String userName, String password) {}
