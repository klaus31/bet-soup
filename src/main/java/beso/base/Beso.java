package beso.base;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class Beso {

  public static final void exitWithError(final String message) {
    System.err.println(message);
    Thread.dumpStack();
    System.exit(666);
  }

  public static final void exitWithErrorIf(final boolean thisIsTrue) {
    exitWithErrorIf(thisIsTrue, "FAaAiiLl NOo.oOo.oOo.oOo.oOo.oO");
  }

  public static final void exitWithErrorIf(final boolean thisIsTrue, final String message) {
    if (thisIsTrue) {
      exitWithError(message);
    }
  }

  public static final void exitWithOkIf(final boolean thisIsTrue, final String message) {
    if (thisIsTrue) {
      System.out.println(message);
      System.exit(0);
    }
  }

  public static Options getOptions() {
    final Options options = new Options();
    OptionBuilder.withLongOpt("help");
    OptionBuilder.withDescription("Show this help");
    options.addOption(OptionBuilder.create("h"));
    OptionBuilder.withLongOpt("target");
    OptionBuilder.withDescription("The main class to launch. For a list with all possible targets use -t possibleTargets.");
    OptionBuilder.withArgName("target");
    OptionBuilder.hasArg();
    options.addOption(OptionBuilder.create("t"));
    return options;
  }

  public static void printUsage(final int status) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("java -jar bet-soup-<version>.jar", getOptions());
    System.exit(status);
  }
}
