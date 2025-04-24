package com.skthvl.cinemetrics.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skthvl.cinemetrics.provider.JwtTokenProvider;
import com.skthvl.cinemetrics.service.InvalidatedTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        final Claims claims = jwtTokenProvider.extractClaims(token);

        final String username = claims.getSubject();
        ObjectMapper mapper = new ObjectMapper();
        final List<String> roles =
            mapper.convertValue(claims.get("roles"), new TypeReference<>() {});

        List<GrantedAuthority> authorities =
            roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        final UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
