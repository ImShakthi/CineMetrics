package com.skthvl.cinemetrics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcademyAwardLoaderRunner implements ApplicationRunner {

  private final AcademyAwardLoader awardLoader;

  @Override
  public void run(ApplicationArguments args) {
//    awardLoader.loadOscarNominations();
  }
}
