package com.skthvl.cinemetrics.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration class for setting up Jasypt-based encryption in the application. */
@Configuration
public class JasyptConfig {

  @Value("${cinemetrics.env.jasypt-encryptor-password}")
  private String jasyptEncryptorPassword;

  /**
   * Creates and configures a Jasypt string encryptor bean for encrypting and decrypting sensitive
   * data within the application.
   *
   * @return a configured {@link StringEncryptor} instance for encrypting and decrypting strings.
   */
  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {

    final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(jasyptEncryptorPassword);
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");

    final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setConfig(config);

    return encryptor;
  }
}
