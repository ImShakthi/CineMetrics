package com.skthvl.cinemetrics.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI documentation in the application.
 *
 * <p>This class configures the OpenAPI specification for describing the API endpoints, security,
 * and available server environments.
 */
@Configuration
public class OpenApiConfig {

  @Value("${cinemetrics.openapi.dev-url}")
  private String devUrl;

  @Value("${cinemetrics.openapi.prod-url}")
  private String prodUrl;

  /**
   * Configures and provides an OpenAPI specification for the application.
   *
   * <p>This method sets up the OpenAPI documentation with information about the API, including its
   * title, version, contact information, licensing, security schemes, and the server environments
   * for development and production.
   *
   * @return a configured {@link OpenAPI} object containing the API documentation details.
   */
  @Bean
  public OpenAPI cineMetricsOpenApi() {
    final Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    final Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("Server URL in Production environment");

    final Contact contact = new Contact();
    contact.setEmail("skthvl94@gmail.com");
    contact.setName("Sakthivel Balasubramaniam");
    contact.setUrl("https://www.imshakthi.github.io/");

    final License mitLicense =
        new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    final Info info =
        new Info()
            .title("CineMetric API")
            .version("1.0")
            .contact(contact)
            .description(
                "This API exposes endpoints to view hollywood movie details and rate them.")
            .termsOfService("https://www.imshakthi.github.io/terms")
            .license(mitLicense);

    final SecurityScheme securityScheme =
        new SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .description("Enter JWT token: Bearer <token>");

    return new OpenAPI()
        .info(info)
        .servers(List.of(prodServer, devServer))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
  }
}
