package com.skthvl.cinemetrics.controller;

import com.skthvl.cinemetrics.model.dto.UserDto;
import com.skthvl.cinemetrics.model.request.LoginRequest;
import com.skthvl.cinemetrics.model.response.LoginResponse;
import com.skthvl.cinemetrics.provider.JwtTokenProvider;
import com.skthvl.cinemetrics.service.AuthService;
import com.skthvl.cinemetrics.service.InvalidatedTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AuthController {

  private final AuthService authService;
  private final JwtTokenProvider jwtTokenProvider;
  private final InvalidatedTokenService invalidatedTokenService;

  public AuthController(
      final AuthService authService,
      final JwtTokenProvider jwtTokenProvider,
      final InvalidatedTokenService invalidatedTokenService) {
    this.authService = authService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.invalidatedTokenService = invalidatedTokenService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid final LoginRequest request) {
    final var token =
        authService.authenticateAndGenerateToken(new UserDto(request.getUsername(), request.getPassword()));
    log.info("Login token generated: {}", token);

    return ResponseEntity.ok(new LoginResponse(token));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(final HttpServletRequest request) {
    final var token = jwtTokenProvider.extractTokenFromHeader(request);

    if (token != null) {
      invalidatedTokenService.invalidateToken(token);
    }

    return ResponseEntity.ok().build();
  }
}
