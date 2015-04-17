package beso.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import beso.main.Launchable;

public class Main {

  private static List<String> getLaunchables(final ApplicationContext context) {
    final Map<String, Object> components = context.getBeansWithAnnotation(Component.class);
    final List<String> launchables = new ArrayList<>();
    for (String key : components.keySet()) {
      if (components.get(key) instanceof Launchable) {
        launchables.add(key);
      }
    }
    return launchables;
  }

  public static void main(final String... args) {
    final ApplicationContext context = new AnnotationConfigApplicationContext(SpringBesoConfig.class);
    if (args.length == 0) {
      // use primary Launchable
      final Launchable greatStuff = context.getBean(Launchable.class);
      greatStuff.launch();
    } else {
      // parse options
      try {
        final CommandLineParser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(Beso.getOptions(), args);
        if (commandLine.hasOption('h')) {
          Beso.printUsage(0);
        } else if (commandLine.hasOption('t')) {
          String target = commandLine.getOptionValue("t");
          if (target.equals("possibleTargets")) {
            System.out.println(StringUtils.join(getLaunchables(context), ", "));
          } else if (context.containsBean(target)) {
            Launchable greatStuff = (Launchable) context.getBean(target);
            greatStuff.launch();
          } else {
            System.err.println("unknown target " + target + ". Possible targets: " + StringUtils.join(getLaunchables(context), ", "));
            Beso.printUsage(1);
          }
        }
      } catch (ParseException exception) {
        exception.printStackTrace();
      }
    }
    ((ConfigurableApplicationContext) context).close();
  }
}
