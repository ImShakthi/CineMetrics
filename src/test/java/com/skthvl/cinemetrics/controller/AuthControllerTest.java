package com.skthvl.cinemetrics.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.skthvl.cinemetrics.model.dto.UserDto;
import com.skthvl.cinemetrics.model.request.LoginRequest;
import com.skthvl.cinemetrics.model.response.LoginResponse;
import com.skthvl.cinemetrics.model.response.MessageResponse;
import com.skthvl.cinemetrics.provider.JwtTokenProvider;
import com.skthvl.cinemetrics.service.AuthService;
import com.skthvl.cinemetrics.service.InvalidatedTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthControllerTest {

  private AuthService authService;
  private JwtTokenProvider jwtTokenProvider;
  private InvalidatedTokenService invalidatedTokenService;
  private AuthController authController;

  @BeforeEach
  void setUp() {
    authService = mock(AuthService.class);
    jwtTokenProvider = mock(JwtTokenProvider.class);
    invalidatedTokenService = mock(InvalidatedTokenService.class);

    authController = new AuthController(authService, jwtTokenProvider, invalidatedTokenService);
  }

  @Test
  void login_ShouldReturnTokenResponse() {
    // Given
    final UserDto userDto = UserDto.builder().userName("testuser").password("testpass").build();
    var request = new LoginRequest("testuser", "testpass");
    var expectedToken = "mocked.jwt.token";

    when(authService.authenticateAndGenerateToken(
            userDto))
        .thenReturn(expectedToken);

    // When
    ResponseEntity<LoginResponse> response = authController.login(request);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(expectedToken, response.getBody().getToken());

    ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
    verify(authService).authenticateAndGenerateToken(captor.capture());
    assertEquals("testuser", captor.getValue().userName());
    assertEquals("testpass", captor.getValue().password());
  }

  @Test
  void logout_ShouldInvalidateToken_WhenTokenIsPresent() {
    // Given
    final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(jwtTokenProvider.extractTokenFromHeader(mockRequest)).thenReturn("token123");

    // When
    ResponseEntity<MessageResponse> response = authController.logout(mockRequest);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals("access token invalidated successfully", response.getBody().message());
    verify(invalidatedTokenService).invalidateToken("token123");
  }

  @Test
  void logout_ShouldNotCallInvalidateToken_WhenTokenIsNull() {
    // Given
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    when(jwtTokenProvider.extractTokenFromHeader(mockRequest)).thenReturn(null);

    // When
    ResponseEntity<MessageResponse> response = authController.logout(mockRequest);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals("token is missing", response.getBody().message());
    verify(invalidatedTokenService, never()).invalidateToken(anyString());
  }
}
