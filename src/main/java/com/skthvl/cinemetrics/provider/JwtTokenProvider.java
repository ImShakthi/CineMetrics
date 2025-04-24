package com.skthvl.cinemetrics.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final String jwtSecret;
  private final long jwtExpirationInMilliseconds;

  public JwtTokenProvider(
      @Value("${cinemetrics.jwt.secret}") final String jwtSecret,
      @Value("${cinemetrics.jwt.expiration-in-milliseconds}")
          final long jwtExpirationInMilliseconds) {
    this.jwtSecret = jwtSecret;
    this.jwtExpirationInMilliseconds = jwtExpirationInMilliseconds;
  }

  public String extractTokenFromHeader(final HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }

  // generate JWT token
  public String generateToken(final String subject) {
    return generateToken(subject, new Date(System.currentTimeMillis()), List.of("ROLE_USER"));
  }

  public String generateToken(final String subject, final Date currentDate) {
    return generateToken(subject, currentDate, List.of("ROLE_USER"));
  }

  public String generateTokenWithRoles(final String subject, final List<String> roles) {
    return generateToken(subject, new Date(System.currentTimeMillis()), roles);
  }

  public String generateToken(
      final String subject, final Date currentDate, final List<String> roles) {
    final Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMilliseconds);

    return Jwts.builder()
        .subject(subject)
        .claim("roles", roles)
        .issuedAt(currentDate)
        .expiration(expireDate)
        .id(UUID.randomUUID().toString())
        .signWith(key())
        .compact();
  }

  public String extractUsername(final String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public Claims extractClaims(final String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String extractJtiId(final String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getId();
  }

  // validate JWT token
  public boolean validateToken(final String token) {
    try {
      Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);
    } catch (ExpiredJwtException ex) {
      return false;
    }
    return true;
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
