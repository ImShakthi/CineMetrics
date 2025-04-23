package com.skthvl.cinemetrics.service;

import com.skthvl.cinemetrics.model.dto.UserDto;
import com.skthvl.cinemetrics.provider.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserAccountService userAccountService;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(
      final UserAccountService userAccountService, final JwtTokenProvider jwtTokenProvider) {
    this.userAccountService = userAccountService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public String authenticateAndGenerateToken(final UserDto userDto) {
    if (userAccountService.isUserCredentialValid(userDto)) {
      return jwtTokenProvider.generateToken(userDto.userName());
    }
    return "";
  }
}
