package com.skthvl.cinemetrics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for setting up JPA auditing and transaction management in the application.
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {}
