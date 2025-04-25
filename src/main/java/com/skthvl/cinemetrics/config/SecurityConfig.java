package com.skthvl.cinemetrics.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.skthvl.cinemetrics.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

  @Value("#{'${cinemetrics.security.cors-allowed-origins}'.split(',')}")
  private List<String> corsAllowedUrls;

  private static final String[] PUBLIC_NON_APP_APIs =
      new String[] {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/api-docs/**",
        "/h2-console/**",
        "/webjars/**",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security"
      };

  private static final String[] PUBLIC_NO_AUTH_APP_APIs =
      new String[] {
        "/api/v1/movies/search",
        "/api/v1/movies/{title}/oscar",
        "/api/v1/movies/{title}/ratings",
        "/api/v1/auth/login"
      };

  private static final String[] AUTH_APP_APIs =
      new String[] {
        "/api/v1/movies/{movieId}/ratings", "/api/v1/ratings/top/**", "/api/v1/auth/logout"
      };

  private static final String[] ADMIN_AUTH_APP_APIs = new String[] {"/api/v1/users"};

  private final JwtAuthenticationFilter jwtFilter;

  public SecurityConfig(final JwtAuthenticationFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            auth ->
                auth
                    // public apis (without JWT)
                    .requestMatchers(PUBLIC_NON_APP_APIs)
                    .permitAll()
                    .requestMatchers(PUBLIC_NO_AUTH_APP_APIs)
                    .permitAll()

                    // auth apis (with JWT)
                    .requestMatchers(AUTH_APP_APIs)
                    // .authenticated()
                    .hasAnyRole("USER", "ADMIN")

                    // admin apis (with JWT)
                    .requestMatchers(ADMIN_AUTH_APP_APIs)
                    .hasAnyRole("ADMIN")
                    // .authenticated()

                    // Other APIs
                    .anyRequest()
                    .authenticated())

        // Stateless session (required for JWT)
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        //        .authenticationProvider(authenticationProvider)

        // added JWT filter
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

        // Exception handling
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                        (request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) ->
                            response.sendError(HttpServletResponse.SC_NOT_FOUND)))

        // for h2 console
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(corsAllowedUrls);
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true); // required if using cookies or Authorization headers

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
