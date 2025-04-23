package com.skthvl.cinemetrics.filter;

import com.skthvl.cinemetrics.provider.JwtTokenProvider;
import com.skthvl.cinemetrics.service.InvalidatedTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final InvalidatedTokenService invalidatedTokenService;

  public JwtAuthenticationFilter(
      final JwtTokenProvider jwtTokenProvider,
      final InvalidatedTokenService invalidatedTokenService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.invalidatedTokenService = invalidatedTokenService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      final String token = header.substring(7);

      if (invalidatedTokenService.isTokenInvalidated(token)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalidated");
      }

      if (jwtTokenProvider.validateToken(token)) {
        final String username = jwtTokenProvider.extractUsername(token);

        final UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(username, null, null);

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
