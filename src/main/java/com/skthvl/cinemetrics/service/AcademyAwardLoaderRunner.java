package com.skthvl.cinemetrics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Runner component that loads Academy Award nominations data during application startup. Uses
 * constructor injection via Lombok's @RequiredArgsConstructor.
 */
@Component
@RequiredArgsConstructor
public class AcademyAwardLoaderRunner implements ApplicationRunner {

  /** Service responsible for loading Academy Award nomination data. */
  private final AcademyAwardLoader awardLoader;

  /** Executes the Oscar nominations loading process when the application starts. */
  @Override
  public void run(ApplicationArguments args) {
    awardLoader.loadOscarNominations();
  }
}
