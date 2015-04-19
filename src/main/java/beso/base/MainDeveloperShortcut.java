package beso.base;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import beso.main.Launchable;

public class MainDeveloperShortcut {

  /* starting app over @primary sucks
   * starting app over parameter sucks
   * starting app over this sucks less */
  public static void main(final String... args) {
    int TARGET_NUMBER = 2; // <---- SET ME
    // known targets
    final String[] targets = new String[20];
    targets[0] = "nextMatchesFootball";
    targets[1] = "addQuotasCurrent";
    targets[2] = "wagerOnRecommandations";
    // run it
    final ApplicationContext context = new AnnotationConfigApplicationContext(SpringBesoConfig.class);
    Launchable greatStuff = (Launchable) context.getBean(targets[TARGET_NUMBER]);
    greatStuff.launch();
    ((ConfigurableApplicationContext) context).close();
  }
}
