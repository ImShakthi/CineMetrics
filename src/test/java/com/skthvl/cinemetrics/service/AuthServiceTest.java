package com.skthvl.cinemetrics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.model.dto.UserDto;
import com.skthvl.cinemetrics.provider.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserAccountService userAccountService;
  @Mock private JwtTokenProvider jwtTokenProvider;

  private AuthService authService;

  @BeforeEach
  void setUp() {
    authService = new AuthService(userAccountService, jwtTokenProvider);
  }

  @Test
  void authenticateAndGenerateToken_shouldReturnToken_whenCredentialsAreValid() {
    // Given
    final UserDto userDto = new UserDto("test_user_name", "securePassword123");

    when(userAccountService.isUserCredentialValid(userDto)).thenReturn(true);
    when(jwtTokenProvider.generateToken("test_user_name")).thenReturn("mocked-jwt-token");

    // When
    final String token = authService.authenticateAndGenerateToken(userDto);

    // Then
    assertEquals("mocked-jwt-token", token);
    verify(userAccountService).isUserCredentialValid(userDto);
    verify(jwtTokenProvider).generateToken("test_user_name");
  }

  @Test
  void authenticateAndGenerateToken_shouldReturnEmptyString_whenCredentialsAreInvalid() {
    // Given
    final UserDto userDto = new UserDto("test_user_name", "wrongPassword");

    when(userAccountService.isUserCredentialValid(userDto)).thenReturn(false);

    // When
    final String token = authService.authenticateAndGenerateToken(userDto);

    // Then
    assertEquals("", token);
    verify(userAccountService).isUserCredentialValid(userDto);
    verify(jwtTokenProvider, never()).generateToken(any());
  }
}
