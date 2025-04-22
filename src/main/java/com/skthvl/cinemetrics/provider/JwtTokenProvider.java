package com.skthvl.cinemetrics.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final String jwtSecret;
  private final long jwtExpirationInSeconds;

  public JwtTokenProvider(
      @Value("${cinemetrics.jwt.secret}") final String jwtSecret,
      @Value("${cinemetrics.jwt.expiration-in-seconds}") final long jwtExpirationInSeconds) {
    this.jwtSecret = jwtSecret;
    this.jwtExpirationInSeconds = jwtExpirationInSeconds;
  }

  // generate JWT token
  public String generateToken(final String subject) {
    return generateToken(subject, new Date());
  }

  public String generateToken(final String subject, final Date currentDate) {
    final Date expireDate = new Date(currentDate.getTime() + jwtExpirationInSeconds);
    return Jwts.builder()
        .subject(subject)
        .issuedAt(currentDate)
        .expiration(expireDate)
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

  // validate JWT token
  public boolean validateToken(final String token) {
    Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);
    return true;
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
