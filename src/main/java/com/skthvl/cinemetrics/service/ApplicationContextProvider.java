package com.skthvl.cinemetrics.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
  private static ApplicationContext context;

  public static <T> T getBean(Class<T> beanClass) {
    return ApplicationContextProvider.context.getBean(beanClass);
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext)
      throws BeansException {
    ApplicationContextProvider.context = applicationContext;
  }
}
