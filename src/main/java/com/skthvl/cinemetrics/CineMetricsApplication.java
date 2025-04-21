package com.skthvl.cinemetrics;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class CineMetricsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CineMetricsApplication.class, args);
  }
}
