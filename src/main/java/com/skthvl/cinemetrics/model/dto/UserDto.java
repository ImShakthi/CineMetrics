package com.skthvl.cinemetrics.model.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record UserDto(String userName, String password, List<String> roles) {}
