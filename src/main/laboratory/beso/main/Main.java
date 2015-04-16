package beso.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import beso.base.SpringBesoConfig;

public class Main {

  public static void main(final String... args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringBesoConfig.class);
    Launchable greatStuff = context.getBean(Launchable.class);
    greatStuff.launch(args);
    ((ConfigurableApplicationContext) context).close();
  }
}
