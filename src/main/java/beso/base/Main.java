package beso.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import beso.main.Launchable;
import beso.tools.BesoAsciiArtTable;

public class Main {

  private static Map<String, Launchable> getLaunchables(final ApplicationContext context) {
    final Map<String, Object> components = context.getBeansWithAnnotation(Component.class);
    final Map<String, Launchable> launchables = new HashMap<>();
    for (String key : components.keySet()) {
      if (components.get(key) instanceof Launchable) {
        launchables.put(key, (Launchable) components.get(key));
      }
    }
    return launchables;
  }

  private static String getPossibleTargets(final ApplicationContext context) {
    Map<String, Launchable> launchables = getLaunchables(context);
    BesoAsciiArtTable table = new BesoAsciiArtTable();
    table.addHeadline("POSSIBLE TARGETS");
    table.addHeaderCols("target", "description");
    table.setMaxColumnWidth(50);
    for (String key : launchables.keySet()) {
      table.add(key, launchables.get(key).getDoc());
    }
    return table.getOutput();
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
            System.out.println(getPossibleTargets(context));
          } else if (context.containsBean(target)) {
            Launchable greatStuff = (Launchable) context.getBean(target);
            greatStuff.launch();
          } else {
            System.err.println("unknown target " + target + "." + System.lineSeparator() + getPossibleTargets(context));
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
