package beso.base;

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
}
